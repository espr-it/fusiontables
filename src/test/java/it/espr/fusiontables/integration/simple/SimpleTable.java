package it.espr.fusiontables.integration.simple;

import it.espr.fusiontables.Engine;
import it.espr.fusiontables.Table;

public class SimpleTable extends Table {

	/**
	 * Change this to your table id.
	 */
	public static final String NAME = "1TfabMnZPa3APgUkFDZs_UN10tOnL_wp1dHr3tVHx";

	public static enum COLUMN implements Column {
		Text, Number, Location, Date, Json;
	}

	protected SimpleTable(Engine engine) {
		super(engine, NAME, COLUMN.values());
	}
}
