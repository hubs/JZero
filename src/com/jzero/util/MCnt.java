/**
 * --- 2011-10-25 ---
 * --- Administrator ---
 * MyCnt.java : 主要用于生成where语句
 */
package com.jzero.util;

public final class MCnt {

	private StringBuffer sb = null;

	public MCnt() {
		sb = new StringBuffer();
	}

	public static MCnt me(){
		return new MCnt();
	}

	public void clear() {
		sb.delete(0, sb.length());
	}

	/**
	 * 第一次 拼装成 key=value 或者 key>=value这种格式 AND
	 */
	public MCnt first(String key, MEnum symbols, Object value) {
		sb = new StringBuffer();
		if (symbols.getValue().equals("LIKE")) {
			value = "%" + value + "%";
		}
		sb.append(" ").append(key).append(" ").append(MEnum.get(symbols))
				.append(" '").append(value).append("' ");
		return this;
	}
	public MCnt first_eq(String key,Object value){
		return first(key,MEnum.EQ,value);
	}
	
	public MCnt in(String key,Object value){
		if(sb==null){
			sb = new StringBuffer();
		}
		sb.append(" ").append(key).append(" ").append(MEnum.IN).append(" ( ").append(value).append(" )");
		return this;
	}

	public MCnt like(String key, Object value) {
		sb.append(" AND ").append(key).append(" LIKE '%").append(value).append(
				"%' ");
		return this;
	}

	/**
	 *除了第一次,在每次的前面加上 and 语句
	 */
	public MCnt and(String key, MEnum symbols, Object value) {
		sb.append(" AND ").append(key).append(" ").append(MEnum.get(symbols))
				.append(" '").append(value).append("' ");
		return this;
	}
	public MCnt and_eq(String key,Object value){
		return and(key, MEnum.EQ, value);
	}

	public MCnt or(String key, MEnum symbols, Object value) {
		sb.append(" OR ").append(key).append(" ").append(MEnum.get(symbols))
				.append(" '").append(value).append("' ");
		return this;
	}
	/**
	 * 解析成 select * from xx where 1=1 and x1=x and (x2=x or x3=x)
	 */
	public MCnt and_in(final MCnt mCnt){
		sb.append(" AND (").append(mCnt.toStr()).append(")");
		return this;
	}
	public MCnt or_in(final MCnt mCnt){
		sb.append(" OR (").append(mCnt.toStr()).append(" ) ");
		return this;
	}
	public static void main(String[] args) {
		String where=MCnt.me().and_eq("1", 1).or_in(MCnt.me().first_eq("one", 1).or_eq("two", 1)).toStr();
		MPrint.print(where);
	}
	public MCnt or_eq(String key,Object value){
		return or(key, MEnum.EQ, value);
	}

	/**
	 *除了第一次,在每次的前面加上 and 语句
	 */
	public MCnt set(String key, MEnum symbols, Object value) {
		sb.append(" , ").append(key).append(" ").append(MEnum.get(symbols))
		.append(" '").append(value).append("' ");
		return this;
	}
	public MCnt first_set_eq(String key,  Object value) {
		sb.append(" , ").append(key).append(" ").append(MEnum.EQ).append(value);
		return this;
	}
	/**
	 * 2011-11-5,用于between
	 */
	public MCnt between(String key, String begin, String end) {
		sb.append(" ").append(key).append(" ").append(MEnum.get(MEnum.BETWEEN))
				.append(" '").append(begin).append("' ").append(
						MEnum.get(MEnum.AND)).append(" '").append(end).append(
						"' ");
		return this;
	}

	/**
	 *2011-11-11 NOT BETWEEN;
	 */
	public MCnt not_between(String key, String begin, String end) {
		sb.append(" ").append(key).append(" ").append(MEnum.get(MEnum.NOT))
				.append(" ").append(MEnum.get(MEnum.BETWEEN)).append(" '")
				.append(begin).append("' ").append(MEnum.get(MEnum.AND))
				.append(" '").append(end).append("' ");
		return this;
	}

	public String toStr() {
		String toSb = sb.toString();
		clear();
		return toSb;
	}

	@Override
	public String toString() {
		String toSb = sb.toString();
		clear();
		return toSb;
	}

}
