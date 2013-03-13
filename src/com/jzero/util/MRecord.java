package com.jzero.util;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import com.jzero.token.MToken;

/**
 * Record
 */
public class MRecord implements Serializable {

	private static final long serialVersionUID = -3254070837297655225L;
	private Map<String, Object> columns = new HashMap<String, Object>();

	/**
	 * Return columns map.
	 */
	public Map<String, Object> getColumns() {
		return columns;
	}

	public Set<Entry<String, Object>> entrySet() {
		return columns.entrySet();
	}

	public boolean isContainsKey(String key) {
		return this.columns.containsKey(key);
	}

	/**
	 * Set columns value with map. 从页面中传进来的值对行过滤
	 */
	public MRecord setColumns_r(Map<String, Object> columns) {
		for (Map.Entry<String, Object> entry : columns.entrySet()) {
			String k = entry.getKey();
			if (k.equals(MToken.me().getCsrf_toke_name())) {
				continue;
			}
			String key = MTool.removeCharacter(k);
			String value = MTool.removeCharacter(entry.getValue());
			this.set(key, value);
		}
		return this;
	}

	public MRecord setColumns(Map<String, Object> columns) {
		this.columns.putAll(columns);
		return this;
	}

	/**
	 * Set columns value with MRecord.
	 * 
	 * @param MRecord
	 *            the MRecord
	 */
	public MRecord setColumns(MRecord MRecord) {
		columns.putAll(MRecord.getColumns());
		return this;
	}

	/**
	 * Remove attribute of this MRecord.
	 * 
	 * @param column
	 *            the column name of the MRecord
	 */
	public MRecord remove(String column) {
		columns.remove(column);
		return this;
	}

	/**
	 * Remove columns of this MRecord.
	 * 
	 * @param columns
	 *            the column names of the MRecord
	 */
	public MRecord remove(String... columns) {
		if (columns != null)
			for (String c : columns)
				this.columns.remove(c);
		return this;
	}

	/**
	 * Remove columns if it is null.
	 */
	public MRecord removeNullValueColumns() {
		for (java.util.Iterator<Entry<String, Object>> it = columns.entrySet()
				.iterator(); it.hasNext();) {
			Entry<String, Object> e = it.next();
			if (e.getValue() == null) {
				it.remove();
			}
		}
		return this;
	}

	/**
	 * Keep columns of this MRecord and remove other columns.
	 * 
	 * @param columns
	 *            the column names of the MRecord
	 */
	public MRecord keep(String... columns) {
		if (columns != null && columns.length > 0) {
			Map<String, Object> newColumns = new HashMap<String, Object>(
					columns.length);
			for (String c : columns)
				if (this.columns.containsKey(c)) // prevent put null value to
													// the newColumns
					newColumns.put(c, this.columns.get(c));
			this.columns = newColumns;
		} else
			this.columns.clear();
		return this;
	}

	/**
	 * Keep column of this MRecord and remove other columns.
	 * 
	 * @param column
	 *            the column names of the MRecord
	 */
	public MRecord keep(String column) {
		if (columns.containsKey(column)) { // prevent put null value to the
											// newColumns
			Object keepIt = columns.get(column);
			columns.clear();
			columns.put(column, keepIt);
		} else
			columns.clear();
		return this;
	}

	/**
	 * Remove all columns of this MRecord.
	 */
	public MRecord clear() {
		columns.clear();
		return this;
	}

	/**
	 * Set column to MRecord.
	 * 
	 * @param column
	 *            the column name
	 * @param value
	 *            the value of the column
	 */
	public MRecord set(String column, Object value) {
		columns.put(column, value);
		return this;
	}

	public MRecord set(String column, String value) {
		columns.put(column, value);
		return this;
	}

	// 2012-10-11
	public MRecord set(String column, String value, Object defaultValue) {
		if (MCheck.isNull(value)) {
			this.columns.put(column, defaultValue);
		} else {
			this.columns.put(column, value);
		}
		return this;
	}

	/**
	 * Get column of any mysql type
	 */
	@SuppressWarnings("unchecked")
	// (T)
	public <T> T get(String column) {
		return MCheck.isNull(columns.get(column)) ? null : (T) columns
				.get(column);
	}

	/**
	 * Get column of any mysql type. Returns defaultValue if null.
	 */
	@SuppressWarnings("unchecked")
	public <T> T get(String column, Object defaultValue) {
		Object result = columns.get(column);
		return (T) (result != null ? result : defaultValue);
	}

	/**
	 * Get column of mysql type: varchar, char, enum, set, text, tinytext,
	 * mediumtext, longtext
	 */
	public String getStr(String column) {
		return MCheck.isNull(columns.get(column)) ? "" : String.valueOf(columns
				.get(column));
	}

	/**
	 * Get column of mysql type: int, integer, tinyint(n) n > 1, smallint,
	 * mediumint
	 */
	public Integer getInt(String column, int defaultValue) {
		if (!MCheck.isNull(columns)) {
			Object _temp = columns.get(column);
			if (!MCheck.isNull(_temp)) {
				return Integer.valueOf(_temp.toString());
			}
		}
		return defaultValue;
	}

	public Integer getInt(String column) {
		return getInt(column, 0);
	}

	/**
	 * Get column of mysql type: bigint
	 */
	public Long getLong(String column) {
		return (Long) columns.get(column);
	}

	/**
	 * Get column of mysql type: date, year
	 */
	public java.sql.Date getDate(String column) {
		return (java.sql.Date) columns.get(column);
	}

	/**
	 * Get column of mysql type: time
	 */
	public java.sql.Time getTime(String column) {
		return (java.sql.Time) columns.get(column);
	}

	/**
	 * Get column of mysql type: timestamp, datetime
	 */
	public java.sql.Timestamp getTimestamp(String column) {
		return (java.sql.Timestamp) columns.get(column);
	}

	/**
	 * Get column of mysql type: real, double
	 */
	public Double getDouble(String column) {
		return (Double) columns.get(column);
	}

	/**
	 * Get column of mysql type: float
	 */
	public Float getFloat(String column) {
		return (Float) columns.get(column);
	}

	/**
	 * Get column of mysql type: bit, tinyint(1)
	 */
	public Boolean getBoolean(String column) {
		return (Boolean) columns.get(column);
	}

	/**
	 * Get column of mysql type: decimal, numeric
	 */
	public java.math.BigDecimal getBigDecimal(String column) {
		return (java.math.BigDecimal) columns.get(column);
	}

	/**
	 * Get column of mysql type: binary, varbinary, tinyblob, blob, mediumblob,
	 * longblob I have not finished the test.
	 */
	public byte[] getBytes(String column) {
		return (byte[]) columns.get(column);
	}

	/**
	 * Get column of any type that extends from Number
	 */
	public Number getNumber(String column) {
		return (Number) columns.get(column);
	}

	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(super.toString()).append(" {");
		boolean first = true;
		for (Entry<String, Object> e : columns.entrySet()) {
			if (first)
				first = false;
			else
				sb.append(", ");

			Object value = e.getValue();
			if (value != null)
				value = value.toString();
			sb.append(e.getKey()).append(":").append(value);
		}
		sb.append("}");
		return sb.toString();
	}

	public static void main(String[] args) {
		MRecord record = new MRecord();
		record.set("one", 1);
		record.set("two", 2);
		record.set("three", 3);
		MPrint.print(record.toString());
	}

	public MRecord toRecord() {
		return null;
	}

	public boolean isEmpty() {
		return this.columns.isEmpty();
	}

	public boolean equals(Object o) {
		if (!(o instanceof MRecord))
			return false;
		if (o == this)
			return true;
		return this.columns.equals(((MRecord) o).columns);
	}

	public int hashCode() {
		return columns == null ? 0 : columns.hashCode();
	}
	/**
	 * 打印显示所有的数据
	 */
	public void foreach() {
		for (Map.Entry<String, Object> entry : columns.entrySet()) {
			String key = entry.getKey();
			Object value = entry.getValue();
			MPrint.print("key=" + key + " value=" + value);
		}
	}
}
