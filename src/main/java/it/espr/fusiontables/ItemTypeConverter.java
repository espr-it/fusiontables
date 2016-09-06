package it.espr.fusiontables;

public abstract class ItemTypeConverter {

	protected final void from(ItemType itemType, Item item) {
		itemType.rowId = item.get(Table.COLUMN.rowid.name(), String.class);
	}

	protected final void to(Item item, ItemType itemType) {
		item.put(Table.COLUMN.rowid.name(), itemType.rowId);
	}

}
