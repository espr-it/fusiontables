package it.espr.fusiontables.integration.bean;

import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import it.espr.fusiontables.Engine;
import it.espr.fusiontables.integration.Configuration;

public class Runner {

	private static final Logger log = LoggerFactory.getLogger(Runner.class);

	public static final void main(String[] args) {
		Engine engine = new Engine(Configuration.serviceAccountPrivateKeyFile, Configuration.serviceAccountId);
		MyDataTable myDataTable = new MyDataTable(engine);

		log.info("Querying table...");
		List<MyData> items = myDataTable.getAllConvertedItems();
		log.info("Found {} items", items.size());

		if (items.size() > 0) {
			log.info("Purging data from table...");
			myDataTable.purge();
			log.info("Data purged.");
		}

		MyData myData = new MyData(1, "First item", new Date(), "1600 Pennsylvania Ave NW, Washington, DC 20500, United States", null);
		myDataTable.insertItemType(myData);
		
		log.info("Querying table...");
		items = myDataTable.getAllConvertedItems();
		log.info("Found {} items", items.size());
		items.clear();
		
		items.add(new MyData(2, "Second item", new Date(), "Unknown street", "{ 'id' : 2 }"));
		items.add(new MyData(3, "Third item", new Date(), "234 my street", null));
		items.add(new MyData(4, "Fourth item", new Date(), "Unknown street", null));
		items.add(new MyData(5, "Fifth item", new Date(), "Unknown street", null));
		items.add(new MyData(6, "Sixth item", new Date(), "234 Elm street", null));
		items.add(new MyData(7, "Seventh item", new Date(), "Unknown street", "{ 'id' : 7 }"));
		items.add(new MyData(8, "Eighth item", new Date(), "N/A", "{ 'id' : 2, 'name' : 'Eighth item' }"));

		myDataTable.insertItemTypes(items);

		log.info("Querying table...");
		items = myDataTable.getAllConvertedItems();
		log.info("Found {} items", items.size());
		for (int i = 0; i < items.size(); i++) {
			log.info("{} item: {}", i, items.get(i));
		}
	}
}
