package com.jzero.util;

import java.util.List;

import com.jzero.log.Log;

/**
 * 秒表
 * 
 * @author zozoh(zozohtnt@gmail.com)
 */
public class MSee {

	private long from;
	private long to;

	public static MSee begin() {
		MSee sw = new MSee();
		sw.start();
		return sw;
	}

	public static MSee create() {
		return new MSee();
	}

	public static MSee run(Runnable atom) {
		MSee sw = begin();
		atom.run();
		sw.stop();
		return sw;
	}

	public long start() {
		from = System.currentTimeMillis();
		return from;
	}

	public long stop() {
		to = System.currentTimeMillis();
		return to;
	}

	public long getDuration() {
		return to - from;
	}

	public long getStartTime() {
		return from;
	}

	public long getEndTime() {
		return to;
	}

	@Override
	public String toString() {
		return String.format("Total: %dms : [%s]=>[%s]", this.getDuration(),
				new java.sql.Timestamp(from).toString(),
				new java.sql.Timestamp(to).toString());
	}

	public static void ok_time(IAtom atom, boolean boolRun) {
		MSee watch = null;
		if (boolRun) {
			watch = MSee.begin();
		}
		try {
			atom.run();
		} catch (Exception e) {
			Log.me().write_error(e);
		}
		if (boolRun) {
			watch.stop();
			MPrint.print(watch.toString());
		}
	}
	public static List<MRecord> ok_time(IAtom2 atom, boolean boolRun) {
		MSee watch = null;
		List<MRecord> datas=null;
		if (boolRun) {
			watch = MSee.begin();
		}
		try {
			datas=atom.run();
		} catch (Exception e) {
			Log.me().write_error(e);
		}
		if (boolRun) {
			watch.stop();
			MPrint.print(watch.toString());
		}
		return datas;
	}
	public static void ok_time(IAtom3 atom, boolean boolRun,final List<MRecord> datas) {
		MSee watch = null;
		if (boolRun) {
			watch = MSee.begin();
		}
		try {
			atom.run(datas);
		} catch (Exception e) {
			Log.me().write_error(e);
		}
		if (boolRun) {
			watch.stop();
			MPrint.print(watch.toString());
		}
	}	
	public static int ok_time(IAtom4 atom, boolean boolRun) {
		MSee watch = null;
		int i=0;
		if (boolRun) {
			watch = MSee.begin();
		}
		try {
			i=atom.run();
		} catch (Exception e) {
			Log.me().write_error(e);
		}
		if (boolRun) {
			watch.stop();
			MPrint.print(watch.toString());
		}
		return i;
	}	
	public static void ok_sql(IAtom atom, boolean boolRun) {
		if (boolRun) {
			try {
				atom.run();
			} catch (Exception e) {
				Log.me().write_error(e);
			}
		}
	}

	public static void main(String[] args) throws Exception {
		
	}

	public interface IAtom {
		public void run() throws Exception ;
	}
	public interface IAtom2{
		public List<MRecord> run() throws Exception ;
	}
	public interface IAtom3{
		public void run(List<MRecord> datas) throws Exception;
	}
	public interface IAtom4{
		public int run() throws Exception;
	}

}
