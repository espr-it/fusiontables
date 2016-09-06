package it.espr.fusiontables.integration.json;

import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;

import it.espr.fusiontables.Engine;
import it.espr.fusiontables.integration.Configuration;
import it.espr.fusiontables.integration.json.MyJsonData.JsonData;

public class Runner {

	private static final Logger log = LoggerFactory.getLogger(Runner.class);

	public static final void main(String[] args) {
		Engine engine = new Engine(Configuration.serviceAccountPrivateKeyFile, Configuration.serviceAccountId);
		MyJsonDataConverter myJsonDataConverter = new MyJsonDataConverter(new ObjectMapper());
		
		MyJsonDataTable myDataTable = new MyJsonDataTable(engine, myJsonDataConverter);

		log.info("Purging data from table...");
		myDataTable.purge();
		log.info("Data purged.");

		MyJsonData myData = new MyJsonData(1, "First item", new Date(), "1600 Pennsylvania Ave NW, Washington, DC 20500, United States", null);
		myDataTable.insertItemType(myData);

		log.info("Querying table...");
		List<MyJsonData> items = myDataTable.getAllConvertedItems();
		log.info("Found {} items", items.size());
		items.clear();

		items.add(new MyJsonData(2, "Second item", new Date(), "Unknown street", new JsonData(2, "second")));
		items.add(new MyJsonData(3, "Third item", new Date(), "234 my street", new JsonData(3, "third")));
		items.add(new MyJsonData(4, "Fourth item", new Date(), "Unknown street", null));
		items.add(new MyJsonData(5, "Fifth item", new Date(), "Unknown street", null));
		items.add(new MyJsonData(6, "Sixth item", new Date(), "234 Elm street", null));
		items.add(new MyJsonData(7, "Seventh item", new Date(), "Unknown street", new JsonData(7, "seventh")));
		items.add(new MyJsonData(8, "Eighth item", new Date(), "N/A", new JsonData(8, "eighth")));

		myDataTable.insertItemTypes(items);

		log.info("Querying table...");
		items = myDataTable.getAllConvertedItems();
		log.info("Found {} items", items.size());
		for (int i = 0; i < items.size(); i++) {
			log.info("{} item: {}", i, items.get(i));
		}
	}
}
