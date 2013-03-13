/**
 * --- 2011-10-25 ---
 * --- Administrator ---
 * MyEnum.java : 所有操作类型
 */
package com.jzero.util;

public enum MEnum {
	LT("<"), GT(">"), EQ("="), LT_E("<="), GT_E(">="), LIKE("LIKE"), OR("OR"), NOT(
			"NOT"), IN("IN"), NOT_IN("NOT_IN"), BETWEEN("BETWEEN"), AND("AND"),NOT_EQ(" !=");

	private final String value;

	private MEnum(String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}

	public static String get(MEnum e) {
		String str = null;
		switch (e) {
		case LT:
			str = MEnum.LT.getValue();
			break;
		case GT:
			str = MEnum.GT.getValue();
			break;
		case EQ:
			str = MEnum.EQ.getValue();
			break;
		case LT_E:
			str = MEnum.LT_E.getValue();
			break;
		case GT_E:
			str = MEnum.GT_E.getValue();
			break;
		case LIKE:
			str = MEnum.LIKE.getValue();
			break;
		case NOT:
			str = MEnum.NOT.getValue();
			break;
		case IN:
			str = MEnum.IN.getValue();
			break;
		case NOT_IN:
			str = MEnum.NOT_IN.getValue();
			break;
		case BETWEEN:
			str = MEnum.BETWEEN.getValue();
			break;
		case OR:
			str = MEnum.OR.getValue();
			break;
		case AND:
			str = MEnum.AND.getValue();
			break;
		case NOT_EQ:
			str=MEnum.NOT_EQ.getValue();
			break;
		}
		return str;
	}

}