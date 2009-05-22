package edu.thu.thss.twe.util;

import edu.thu.thss.twe.model.graph.DataField.DataType;

public class DataTypeUtil {

	public static DataType determineDataType(String typename) {
		try {
			return DataType.valueOf(typename.toUpperCase());
		} catch (Exception e) {
			return null;
		}
	}

	public static boolean isValidValue(DataType type, String value) {
		switch (type) {
		case BOOLEAN:
			String temp = value.toLowerCase();
			System.out.println("Value: " + value);
			if (temp.equals("true") || temp.equals("false")) {
				return true;
			} else {
				return false;
			}
		case STRING:
			return true;
		case FLOAT:
			try {
				Double.parseDouble(value);
				return true;
			} catch (Exception e) {
				return false;
			}
		case INTEGER:
			try {
				Integer.parseInt(value);
				return true;
			} catch (Exception e) {
				return false;
			}
		default:
			return true;
		}
	}
}
