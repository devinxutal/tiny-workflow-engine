package edu.thu.thss.twe.util;

import edu.thu.thss.twe.model.graph.Constants;

public class TypeUtil {

	private static String[] participant_type_names = new String[] {
			"RESOURCE_SET", "RESOURCE", "ROLE", "ORGANIZATIONAL_UNIT", "HUMAN",
			"SYSTEM" };

	public static int parseParticipantType(String typename) {
		for (int i = 0; i < participant_type_names.length; i++) {
			if (participant_type_names[i].equals(typename)) {
				return i;
			}
		}
		return -1;
	}

	public static int parseJoinType(String typename) {
		if (typename.toUpperCase().equals("AND")) {
			return Constants.JOIN_TYPE_AND;
		} else if (typename.toUpperCase().equals("XOR")) {
			return Constants.JOIN_TYPE_XOR;
		} else {
			return -1;
		}
	}

	public static int parseSplitType(String typename) {
		if (typename.toUpperCase().equals("AND")) {
			return Constants.SPLIT_TYPE_AND;
		} else if (typename.toUpperCase().equals("XOR")) {
			return Constants.SPLIT_TYPE_XOR;
		} else {
			return -1;
		}
	}

	public static int parseConditionType(String typename) {
		if (typename.toUpperCase().equals("CONDITION")) {
			return Constants.CONDITION_TYPE_CONDITION;
		} else {
			return -1;
		}
	}

}
