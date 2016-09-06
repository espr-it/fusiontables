package it.espr.fusiontables;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class ConvertingTable<T extends ItemType> extends Table {

	private static final Logger log = LoggerFactory.getLogger(ConvertingTable.class);

	private Converter<T> converter;

	protected ConvertingTable(Engine engine, Converter<T> converter, String name, Column... columns) {
		super(engine, name, columns);
		this.converter = converter;
	}

	public ItemType getConvertedItemById(String rowId) {
		try {
			return this.converter.from(this.getItemById(rowId));
		} catch (ConvertException e) {
			log.error("Problem when converting item", e);
		}
		return null;
	}

	public List<T> getAllConvertedItems() {
		return this.getConvertedItems(null, null);
	}

	public List<T> getConvertedItems(String column, String value) {
		List<Item> items = super.getItems(column, value);
		List<T> convertedItems = new ArrayList<>();
		for (Item item : items) {
			try {
				convertedItems.add(this.converter.from(item));
			} catch (ConvertException e) {
				log.error("Problem when converting item", e);
			}
		}
		return convertedItems;
	}

	public T getConvertedItem(String column, String value) {
		try {
			return this.converter.from(super.getItem(column, value));
		} catch (ConvertException e) {
			log.error("Problem when converting item", e);
		}
		return null;
	}

	public void insertItemType(T itemType) {
		try {
			super.insert(this.converter.to(itemType));
		} catch (ConvertException e) {
			log.error("Problem when converting item", e);
		}
	}

	public void insertItemTypes(Collection<T> itemTypes) {
		List<Item> items = new ArrayList<>();
		for (T itemType : itemTypes) {
			try {
				items.add(this.converter.to(itemType));
			} catch (ConvertException e) {
				log.error("Problem when converting items", e);
			}
		}
		super.insert(items);
	}

	public void deleteItemType(T itemType) {
		super.delete(itemType.rowId);
	}

	public void deleteItemTypes(Collection<T> itemTypes) {
		List<String> rowIds = new ArrayList<>();
		for (ItemType itemType : itemTypes) {
			rowIds.add(itemType.rowId);
		}
		super.delete(rowIds);
	}
}
