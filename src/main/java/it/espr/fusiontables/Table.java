package it.espr.fusiontables;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class Table {

	private static final Logger log = LoggerFactory.getLogger(Table.class);

	protected static interface Column {
		public String name();
	}

	public static enum COLUMN implements Column {
		rowid;
	}

	private Engine engine;

	protected final String name;

	protected final Map<String, Column> columns;

	protected Table(Engine engine, String name, Column... columns) {
		this.engine = engine;
		this.name = name;
		this.columns = new LinkedHashMap<>();
		for (Column column : columns) {
			this.columns.put(column.name(), column);
		}
	}

	private List<String> columns(boolean isInsert, Collection<Column> columns) {
		List<String> result = new ArrayList<>();

		if (!isInsert) {
			result.add(COLUMN.rowid.name());
		}

		for (Column column : this.columns.values()) {
			result.add(column.name());
		}

		return result;
	}

	private List<String> columns() {
		return this.columns(false);
	}

	private List<String> columns(boolean isInsert) {
		return this.columns(false, null);
	}

	public Item getItemById(String rowId) {
		List<Item> items = this.getItems(COLUMN.rowid.name(), rowId);
		return items.get(0);
	}

	public List<Item> getItems() {
		return this.getItems(null, null);
	}

	public List<Item> getItems(String column, String value) {
		List<Engine.Pair> pairs = null;
		if (!Utils.isBlank(column) && !Utils.isBlank(value)) {
			pairs = Arrays.asList(new Engine.Pair(column, value));
		}
		return this.engine.select(name, columns(), pairs);
	}

	public Item getItem(String column, String value) {
		return this.getItems(column, value).get(0);
	}

	public void purge() {
		this.engine.delete(this.name);
	}

	public void delete(String rowId) {
		this.engine.delete(this.name, Arrays.asList(rowId));
	}

	public void delete(Collection<String> rowIds) {
		this.engine.delete(this.name, rowIds);
	}

	public void insert(Item item) {
		this.insert(Arrays.asList(item));
	}

	public void insert(Collection<Item> items) {
		try {
			this.engine.insert(name, columns(true), items);
		} catch (Exception e) {
			log.error("Problem when inserting {} items into {}", items.size(), name, e);
		}
	}
}
