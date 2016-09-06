package it.espr.fusiontables.integration.json;

import com.fasterxml.jackson.databind.ObjectMapper;

import it.espr.fusiontables.ConvertException;
import it.espr.fusiontables.Converter;
import it.espr.fusiontables.Item;
import it.espr.fusiontables.JsonConverter;
import it.espr.fusiontables.integration.json.MyJsonData.JsonData;
import it.espr.fusiontables.integration.simple.SimpleTable;

public class MyJsonDataConverter extends JsonConverter<JsonData> implements Converter<MyJsonData> {

	public MyJsonDataConverter(ObjectMapper objectMapper) {
		super(objectMapper);
	}
	
	@Override
	public MyJsonData from(Item item) throws ConvertException {
		try {
			MyJsonData myData = new MyJsonData();
			// sorry we can't do automatic Date/Integer conversions from fusion tables yet!
			// myData.number = item.get(SimpleTable.COLUMN.Number.name(), Integer.class);
			// myData.date = item.get(SimpleTable.COLUMN.Date.name(), Date.class);

			myData.text = item.get(SimpleTable.COLUMN.Text.name(), String.class);
			myData.location = item.get(SimpleTable.COLUMN.Location.name(), String.class);
			myData.json = this.deserialise(item, SimpleTable.COLUMN.Json, JsonData.class);
			return myData;
		} catch (Exception e) {
			throw new ConvertException("Problem when converting fusion table item into myData object", e);
		}
	}

	@Override
	public Item to(MyJsonData myData) throws ConvertException {
		try {
			Item item = new Item();
			item.put(SimpleTable.COLUMN.Number.name(), myData.number);
			item.put(SimpleTable.COLUMN.Text.name(), myData.text);
			item.put(SimpleTable.COLUMN.Date.name(), myData.date);
			item.put(SimpleTable.COLUMN.Location.name(), myData.location);
			item.put(SimpleTable.COLUMN.Json.name(), this.serialise(myData.json));
			return item;
		} catch (Exception e) {
			throw new ConvertException("Problem when converting myData object into fusion table item", e);
		}
	}
}
