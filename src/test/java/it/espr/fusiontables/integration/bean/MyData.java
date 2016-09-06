package it.espr.fusiontables.integration.bean;

import java.util.Date;

import it.espr.fusiontables.ItemType;

@SuppressWarnings("serial")
public class MyData extends ItemType {

	public Integer number;

	public String text;

	public Date date;

	public String location;

	public String json;

	public MyData() {

	}

	public MyData(Integer number, String text, Date date, String location, String json) {
		super();
		this.number = number;
		this.text = text;
		this.date = date;
		this.location = location;
		this.json = json;
	}
}
