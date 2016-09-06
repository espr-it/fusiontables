package it.espr.fusiontables;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import it.espr.fusiontables.Table.Column;

public abstract class JsonConverter<T> extends ItemTypeConverter {

	private ObjectMapper objectMapper;

	protected JsonConverter(ObjectMapper objectMapper) {
		this.objectMapper = new ObjectMapper();
	}

	protected T deserialise(Item item, Column column, Class<T> type) throws ConvertException {
		String text = item.get(column.name(), String.class);
		if (isBlank(text)) {
			return null;
		}
		text = text.replace("\n", "").replace("\r", "");
		try {
			T instance = this.objectMapper.readValue(text, type);
			if (instance == null) {
				throw new ConvertException("Problem when deserialising - value is null");
			}
			return instance;
		} catch (Exception e) {
			throw new ConvertException("Problem when deserialising", e);
		}
	}

	protected String serialise(T object) throws ConvertException {
		if (isBlank(object)) {
			return null;
		}
		try {
			return objectMapper.writeValueAsString(object);
		} catch (JsonProcessingException e) {
			throw new ConvertException("Problem when serialising", e);
		}
	}

	private boolean isBlank(Object value) {
		if (value == null) {
			return true;
		}
		if (value instanceof String && ((String) value).trim().equals("")) {
			return true;
		}
		return false;
	}
}