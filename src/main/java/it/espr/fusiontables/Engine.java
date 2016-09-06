package it.espr.fusiontables;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.http.ByteArrayContent;
import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpContent;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.services.fusiontables.Fusiontables;
import com.google.api.services.fusiontables.model.Sqlresponse;

public class Engine {
	private static final Logger logger = LoggerFactory.getLogger(Engine.class);

	private static final String URL = "https://www.googleapis.com/fusiontables/v1/query";

	private static final HttpTransport HTTP_TRANSPORT = new NetHttpTransport();

	private static final JsonFactory JSON_FACTORY = new com.google.api.client.json.jackson2.JacksonFactory();

	public static class Pair {
		String column;

		String value;

		public Pair(String column, String value) {
			super();
			this.column = column;
			this.value = value;
		}
	}

	private GoogleCredential credential;

	private Fusiontables fusiontables;

	private final String serviceAccountPrivateKeyFile;

	private final String serviceAccountId;

	private static final Collection<String> SCOPES = new ArrayList<>();

	static {
		SCOPES.add("http://www.google.com/fusiontables/api/query");
	}

	public Engine(String serviceAccountPrivateKeyFile, String serviceAccountId) {
		this.serviceAccountPrivateKeyFile = serviceAccountPrivateKeyFile;
		this.serviceAccountId = serviceAccountId;
	}

	private Fusiontables fusion() throws Exception {
		if (credential == null) {
			File key = new File(this.getClass().getResource(this.serviceAccountPrivateKeyFile).toURI());
			this.credential = new GoogleCredential.Builder().setTransport(HTTP_TRANSPORT).setJsonFactory(JSON_FACTORY).setServiceAccountScopes(SCOPES).setServiceAccountPrivateKeyFromP12File(key).setServiceAccountId(this.serviceAccountId).build();
			this.fusiontables = new Fusiontables.Builder(HTTP_TRANSPORT, JSON_FACTORY, credential).setApplicationName("aotearoa.beer").build();
		}
		return this.fusiontables;
	}

	public void delete(String table, Collection<String> rowIds) {
		if (rowIds == null || rowIds.size() == 0) {
			logger.info("Nothing to delete from {} - empty list passed in.", table);
			return;
		}
		for (String rowId : rowIds) {
			if (rowId == null) {
				continue;
			}
			try {
				String queryDelete = "DELETE FROM " + table + " WHERE rowid='" + rowId + "'";
				this.execute(queryDelete);

			} catch (Exception e) {
				logger.error("Problem when deletingall records", e);
			}
		}
	}

	public void delete(String table) {
		try {
			String queryDelete = "DELETE FROM " + table;
			this.execute(queryDelete);

		} catch (Exception e) {
			logger.error("Problem when deleting all records", e);
		}
	}

	public void deleteAll(String table, List<Pair> where) {
		try {
			String queryCreateView = "CREATE VIEW tmp AS (SELECT * FROM " + table + where(where) + ")";
			Sqlresponse response = this.execute(queryCreateView);

			String queryDelete = "DELETE FROM " + response.getRows().get(0).get(0);
			this.execute(queryDelete);

		} catch (Exception e) {
			logger.error("Problem when deletingall records", e);
		}
	}

	public List<Item> select(String table, List<String> columns, List<Pair> where) {
		List<Item> data = new ArrayList<>();
		try {
			List<String> cols = new ArrayList<>(columns);
			String query = "SELECT " + concat(cols) + " FROM " + table + where(where);
			Sqlresponse response = this.execute(query);
			if (response.getRows() != null && response.getRows().size() > 0) {
				for (List<Object> row : response.getRows()) {
					Item item = new Item();
					for (int i = 0; i < row.size(); i++) {
						item.put(response.getColumns().get(i), row.get(i));
					}
					data.add(item);
				}
			}
		} catch (Exception e) {
			logger.error("Problem when selecting all records", e);
		}
		return data;
	}

	public Sqlresponse execute(String query) throws IOException, Exception {
		return this.execute(query, 1);
	}

	public Sqlresponse execute(String query, int retry) throws IOException, Exception {
		logger.debug("Running query {}", query);
		try {
			return this.fusion().query().sql(query).execute();
		} catch (Exception e) {
			logger.error("Problem when running query {}", query, e);
			if (retry > 0) {
				logger.error("Retrying query {} for {}. time", query, --retry, e);
				return this.execute(query, retry);
			} else {
				throw new Exception(e);
			}
		}
	}

	public void insert(String table, List<String> columns, Item item) throws Exception {
		this.insert(table, columns, Arrays.asList(item));
	}

	public void insert(String table, List<String> columns, Collection<Item> items) throws Exception {
		if (items == null || items.size() == 0) {
			logger.warn("Empty list passed for insertion, skipping...");
			return;
		}

		StringBuilder values = new StringBuilder();
		StringBuilder insert = new StringBuilder();

		int batch = 1;
		int i = 0;
		for (Item item : items) {
			i++;
			values = new StringBuilder("(");

			for (String column : columns) {
				append(values, item.get(column));
			}
			values.append(")");

			insert.append("INSERT INTO " + table + " (" + this.concat(columns) + ") VALUES " + values + "; ");

			if (i % 5 == 0 || i == items.size()) {
				logger.info("Inserting {}. batch of {} items into fusion tables.", batch++, items.size());
				logger.debug("{}", insert.toString());
				try {
					this.upload(insert.toString());
				} catch (Exception e) {
					logger.error("Problem when inserting {} items into fusion tables.", items.size(), e);
				}

				values = new StringBuilder();
				insert = new StringBuilder();
			}

		}
	}

	private void upload(String insert) throws Exception {
		for (int i = 0; i < 5; i++) {
			try {
				HttpContent content = ByteArrayContent.fromString(null, "sql=" + insert.toString());
				HttpRequest httpRequest = this.fusion().getRequestFactory().buildPostRequest(new GenericUrl(URL), content);
				httpRequest.setReadTimeout(30000);
				httpRequest.execute();
				return;
			} catch (Exception e) {
				logger.error("Problem when inserting data - {} try with {}", i, insert, e);
				Thread.sleep(3000);
			}
		}
		throw new Exception("Couldn't insert data: " + insert);
	}

	private static void append(StringBuilder sb, Object value) throws UnsupportedEncodingException {
		if (sb.length() > 1) {
			sb.append(",");
		}

		String str = "";

		if (value != null) {
			str = value.toString();
			str = str.replace("'", "\\'");
			// str = str.replace("&", "\\&");
			str = URLEncoder.encode(str, "UTF-8");
		}

		sb.append("'" + str + "'");
	}

	public String where(List<Pair> pairs) {
		StringBuilder sb = new StringBuilder(this.concatPairs(pairs));
		if (sb.length() > 0) {
			sb.insert(0, " WHERE ");
		}
		return sb.toString();
	}

	public String concatPairs(List<Pair> pairs) {
		StringBuilder sb = new StringBuilder();
		if (pairs != null && pairs.size() > 0) {
			for (Pair pair : pairs) {
				if (sb.length() > 0) {
					sb.append(",");
				}
				String operator = pair.value.startsWith("%") || pair.value.endsWith("%") ? " LIKE " : "=";
				sb.append(pair.column + operator + "'" + pair.value + "'");
			}
		}
		return sb.toString();
	}

	public String concat(List<String> tokens) {
		StringBuilder sb = new StringBuilder();
		if (tokens != null && tokens.size() > 0) {
			for (String token : tokens) {
				if (sb.length() > 0) {
					sb.append(",");
				}
				sb.append(token);
			}
		}
		return sb.toString();
	}

}
