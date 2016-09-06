package it.espr.fusiontables;

import java.io.Serializable;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Item implements Serializable {

	private static final Logger log = LoggerFactory.getLogger(Item.class);

	private static final long serialVersionUID = 1L;

	private Map<String, Object> data;

	public Item() {
		super();
		this.data = new LinkedHashMap<>();
	}

	public void put(String column, Object value) {
		this.data.put(column, value);
	}

	public Object get(String column) {
		return this.data.get(column);
	}

	public Map<String, Object> getData() {
		return Collections.unmodifiableMap(this.data);
	}

	@SuppressWarnings("unchecked")
	public <Type> Type get(String column, Class<Type> type) {
		try {
			return (Type) this.data.get(column);
		} catch (Exception e) {
			log.error("Problem when casting {} to {}", this.data.get(column), type, e);
		}
		return null;
	}
}
