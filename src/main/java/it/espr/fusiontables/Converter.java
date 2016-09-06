package it.espr.fusiontables;

public interface Converter<ItemType> {

	public ItemType from(Item item) throws ConvertException;

	public Item to(ItemType item) throws ConvertException;
}