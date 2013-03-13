package com.jzero.db.exp;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Types;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.dbutils.BasicRowProcessor;
import org.apache.commons.dbutils.handlers.AbstractListHandler;

import com.jzero.util.MCheck;
import com.jzero.util.MDate;
import com.jzero.util.MRecord;

/**
 * --- 2012-5-31 --- --- Administrator --- MyMapListHandler.java :
 * 继承实现Map的handleRow方法 在DBUtils中它是：result.put(rsmd.getColumnName(i),
 * rs.getObject(i));用rs.getObject()取值 就不能把数据库中的Decimal后面的小数点取值出来.所以在此实现它的方法
 */
public class MHandler extends AbstractListHandler<MRecord> {
	private static ConvertToMap convert;
	private static java.text.DecimalFormat   df   =new   java.text.DecimalFormat("0.00");  
	private static SimpleDateFormat ymd_hms=MDate.getSimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	public MHandler() {
		if (MCheck.isNull(convert)) {
			convert = new ConvertToMap();
		}
	}

	@Override
	protected MRecord handleRow(ResultSet rs) throws SQLException {
		return new MRecord().setColumns(convert.toMap(rs));
	}

	private class ConvertToMap extends BasicRowProcessor {

		@Override
		public Map<String, Object> toMap(ResultSet rs) throws SQLException {
			Map<String, Object> result = new HashMap<String, Object>();
			ResultSetMetaData rsmd = rs.getMetaData();
			int cols = rsmd.getColumnCount();
			for (int i = 1; i <= cols; i++) {
				result.put(rsmd.getColumnName(i).toLowerCase(), getTypeValue(rsmd, rs, i));
			}

			return result;
		}

	}

	private Object getTypeValue(ResultSetMetaData rsmd, ResultSet rs, int index)
			throws SQLException {
		Object result = null;
		int type = rsmd.getColumnType(index);
		switch (type) {
		case Types.NUMERIC:
		case Types.FLOAT:
		case Types.DOUBLE:
		case Types.INTEGER:
			int scale=rsmd.getScale(index);
			if(scale>0){
				result=df.format(rs.getDouble(index));
			}else{
				result=rs.getInt(index);
			}
			break;
		case Types.DATE:
			result=rs.getDate(index);break;
		case Types.TIMESTAMP:
			Object obj=rs.getTimestamp(index);
			result=MCheck.isNull(obj)?null:ymd_hms.format(rs.getTimestamp(index));
			break;
		default:
			result = rs.getObject(index);
			break;
		}
		return result;

	}

}
