package it.espr.fusiontables;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import it.espr.fusiontables.data.DataConvertException;
import it.espr.fusiontables.data.DataConverter;
import it.espr.fusiontables.data.DateConverter;

public abstract class ItemTypeConverter {

	private Map<Class<?>, DataConverter<?>> converters;

	protected ItemTypeConverter() {
		this(null);
	}

	protected ItemTypeConverter(Map<Class<?>, DataConverter<?>> converters) {
		this.converters = new HashMap<>();
		this.converters.put(Date.class, new DateConverter());
		if (converters != null) {
			this.converters.putAll(converters);
		}
	}

	protected <Data> Data toData(String value, Class<Data> type) throws DataConvertException {
		if (Utils.isBlank(value)) {
			return null;
		}

		if (this.converters.containsKey(type)) {
			return (Data) this.converters.get(type).to(value);
		}
		throw new DataConvertException("No '" + type + "' converter available");
	}

	protected <Data> String fromData(Data value, Class<Data> type) throws DataConvertException {
		if (this.converters.containsKey(type)) {
			return ((DataConverter<Data>) this.converters.get(type)).from(value);
		}
		throw new DataConvertException("No '" + type + "' converter available");
	}

	protected final void from(ItemType itemType, Item item) {
		itemType.rowId = item.get(Table.COLUMN.rowid.name(), String.class);
	}

	protected final void to(Item item, ItemType itemType) {
		item.put(Table.COLUMN.rowid.name(), itemType.rowId);
	}

}
