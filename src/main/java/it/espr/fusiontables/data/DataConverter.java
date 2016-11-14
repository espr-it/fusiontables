package it.espr.fusiontables.data;

public interface DataConverter<Data> {

	public Data to(String value) throws DataConvertException;

	public String from(Data data) throws DataConvertException;
}
