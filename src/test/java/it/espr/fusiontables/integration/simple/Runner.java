package it.espr.fusiontables.integration.simple;

import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import it.espr.fusiontables.Engine;
import it.espr.fusiontables.Item;
import it.espr.fusiontables.Table;
import it.espr.fusiontables.integration.Configuration;

public class Runner {

	private static final Logger log = LoggerFactory.getLogger(Runner.class);

	public static final void main(String[] args) {
		Engine engine = new Engine(Configuration.serviceAccountPrivateKeyFile, Configuration.serviceAccountId);
		SimpleTable simpleTable = new SimpleTable(engine);

		log.info("Querying table...");
		List<Item> items = simpleTable.getItems();
		log.info("Found {} items", items.size());

		if (items.size() > 0) {
			log.info("Purging data from table...");
			simpleTable.purge();
			log.info("Data purged.");
		}

		Item item = createItem(1, "First item", new Date(), "1600 Pennsylvania Ave NW, Washington, DC 20500, United States", null);
		simpleTable.insert(item);

		log.info("Querying table...");
		items = simpleTable.getItems();
		log.info("Found {} items", items.size());

		items.add(createItem(2, "Second item", new Date(), "Unknown street", "{ 'id' : 2 }"));
		items.add(createItem(3, "Third item", new Date(), "234 my street", null));
		items.add(createItem(4, "Fourth item", new Date(), "Unknown street", null));
		items.add(createItem(5, "Fifth item", new Date(), "Unknown street", null));
		items.add(createItem(6, "Sixth item", new Date(), "234 Elm street", null));
		items.add(createItem(7, "Seventh item", new Date(), "Unknown street", "{ 'id' : 7 }"));
		items.add(createItem(8, "Eighth item", new Date(), "N/A", "{ 'id' : 2, 'name' : 'Eighth item' }"));

		simpleTable.insert(items);

		log.info("Querying table...");
		items = simpleTable.getItems();
		log.info("Found {} items", items.size());
		for (int i = 0; i < items.size(); i++) {
			log.info("{} item: {}", i, items.get(i).getData());
		}

		log.info("Deleting single item...");
		simpleTable.delete(items.get(3).get(Table.COLUMN.rowid.name(), String.class));

		log.info("Querying table...");
		items = simpleTable.getItems();
		log.info("Found {} items", items.size());
	}

	private static Item createItem(int number, String text, Date date, String Location, String json) {
		Item item = new Item();
		item.put(SimpleTable.COLUMN.Number.name(), number);
		item.put(SimpleTable.COLUMN.Text.name(), text);
		item.put(SimpleTable.COLUMN.Date.name(), date);
		item.put(SimpleTable.COLUMN.Location.name(), Location);
		item.put(SimpleTable.COLUMN.Json.name(), json);
		return item;
	}
}
