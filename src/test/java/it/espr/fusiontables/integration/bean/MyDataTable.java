package it.espr.fusiontables.integration.bean;

import it.espr.fusiontables.ConvertingTable;
import it.espr.fusiontables.Engine;

public class MyDataTable extends ConvertingTable<MyData> {

	/**
	 * Change this to your table id.
	 */
	public static final String NAME = "1TfabMnZPa3APgUkFDZs_UN10tOnL_wp1dHr3tVHx";

	public static enum COLUMN implements Column {
		Text, Number, Location, Date, Json;
	}

	protected MyDataTable(Engine engine) {
		super(engine, new MyDataConverter(), NAME, COLUMN.values());
	}
}
