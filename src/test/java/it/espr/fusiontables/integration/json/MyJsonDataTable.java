package it.espr.fusiontables.integration.json;

import it.espr.fusiontables.Converter;
import it.espr.fusiontables.ConvertingTable;
import it.espr.fusiontables.Engine;

public class MyJsonDataTable extends ConvertingTable<MyJsonData> {

	/**
	 * Change this to your table id.
	 */
	public static final String NAME = "1TfabMnZPa3APgUkFDZs_UN10tOnL_wp1dHr3tVHx";

	public static enum COLUMN implements Column {
		Text, Number, Location, Date, Json;
	}

	protected MyJsonDataTable(Engine engine, Converter<MyJsonData> converter) {
		super(engine, converter, NAME, COLUMN.values());
	}
}
