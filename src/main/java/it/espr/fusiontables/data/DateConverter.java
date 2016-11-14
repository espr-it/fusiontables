package it.espr.fusiontables.data;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateConverter implements DataConverter<Date> {

	private static final SimpleDateFormat DEFAULT_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	private SimpleDateFormat sdf;

	public DateConverter() {
		this(null);
	}

	public DateConverter(SimpleDateFormat sdf) {
		if (sdf == null) {
			this.sdf = DEFAULT_FORMAT;
		} else {
			this.sdf = sdf;
		}
	}

	@Override
	public Date to(String date) throws DataConvertException {
		try {
			return this.sdf.parse(date);
		} catch (Exception e) {
			throw new DataConvertException("Problem when parsing date '" + date + "'", e);
		}
	}

	@Override
	public String from(Date date) throws DataConvertException {
		try {
			return this.sdf.format(date);
		} catch (Exception e) {
			throw new DataConvertException("Problem when formatting date '" + date + "'", e);
		}
	}

}
