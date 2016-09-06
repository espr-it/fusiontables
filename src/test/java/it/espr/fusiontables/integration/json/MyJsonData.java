package it.espr.fusiontables.integration.json;

import java.util.Date;

import it.espr.fusiontables.ItemType;

@SuppressWarnings("serial")
public class MyJsonData extends ItemType {

	public static class JsonData {
		public int id;
		public String text;

		public JsonData() {
			super();
		}

		public JsonData(int id, String text) {
			super();
			this.id = id;
			this.text = text;
		}
	}

	public Integer number;

	public String text;

	public Date date;

	public String location;

	public JsonData json;

	public MyJsonData() {

	}

	public MyJsonData(Integer number, String text, Date date, String location, JsonData json) {
		super();
		this.number = number;
		this.text = text;
		this.date = date;
		this.location = location;
		this.json = json;
	}
}
