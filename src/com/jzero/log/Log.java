package com.jzero.log;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;

import com.jzero.db.cache.MFile;
import com.jzero.util.MCheck;
import com.jzero.util.MDate;
import com.jzero.util.MPath;
import com.jzero.util.MPro;
import com.jzero.util.Msg;

public final class Log {

	private boolean enabled;
	private String level;
	private static BufferedWriter write = null;
	private static Log log = new Log();
	private static StringBuffer sb = new StringBuffer();
	private static boolean is_load = false;
	private boolean reload;// 如果为true,则每次都加载

	private Log() {
	}

	public static Log me() {
		return log;
	}

	public void init() {
		MPro pro = MPro.me().load_file(Msg.CONFIG);
		setEnabled(pro.getBool("log_enabled"));
		setLevel(pro.getStr("log_level"));
		setReload(pro.getBool("log_reload"));
		if (isReload()) {
			is_load = false;
		} else {
			is_load = true;
		}
	}

	public synchronized void write_log(LogEnum level, Exception e) {
		write_log(level, e.getMessage());
	}

	private void clear(StringBuffer  ss) {
			if(!MCheck.isNull(ss)){
				ss.delete(0, ss.length());
			}
	}

	public void write_error(Exception e) {
		clear(sb);
		StackTraceElement[] elements= e.fillInStackTrace().getStackTrace();
		for(StackTraceElement row:elements){
			if(!MCheck.isNull(row.getFileName())){
				if(row.getFileName().equals(getClass().getSimpleName()+".java")){
					continue;
				}
				sb.append("\n ERROR INFO : "+e)
				.append("\t").append(" =>").append(row.getFileName()).append(".").append(row.getClassName()).append(".").append(row.getLineNumber());
			}
		}
		write_log(LogEnum.ERROR,sb.toString());
	}

	public void write_debug(String msg) {
		write_log(LogEnum.DEBUG, msg);
	}

	public void write_info(String msg) {
		write_log(LogEnum.INFO, msg);
	}
	/**
	 * 直接写入日记文件
	 */
	public void write(String msg){
		String log_file = MPath.me().getWebInfPath() + "log";
		String log_file_name = MDate.get_ymd() + ".txt";
		File file = MFile.createFile(log_file, log_file_name);
		try {
			write = MFile.getWriter(file, true);
			write.append(MDate.get_ymd_hms() + ": "+ msg + "\n");
		} catch (IOException e) {
			try {
				write.append(MDate.get_ymd_hms() + ": "+ e.getMessage() + "\n");
			} catch (IOException e1) {
			}
		} finally {
			try {
				write.flush();
				write.close();
			} catch (IOException e) {
				try {
					write.append(MDate.get_ymd_hms() + "『" + level+ "』: " + e.getMessage() + "\n");
				} catch (IOException e1) {
				}
			}
		}
	}
	public  void write_log(LogEnum level, String msg) {
		if (!is_load) {
			init();
		}
		// 如果开启了,则写入文件
		if (isEnabled()) {
			boolean bool = bool_log(level);
			if (bool) {
				String log_file = MPath.me().getWebInfPath() + "log";
				String log_file_name = MDate.get_ymd() + ".txt";
				File file = MFile.createFile(log_file, log_file_name);
				try {
					write = MFile.getWriter(file, true);
					write.append(MDate.get_ymd_hms() + "[" + level + "]: "
							+ msg + "\n");
				} catch (IOException e) {
					try {
						write.append(MDate.get_ymd_hms() + "『" + level + "』: "
								+ e.getMessage() + "\n");
					} catch (IOException e1) {
					}
				} finally {
					try {
						write.flush();
						write.close();
					} catch (IOException e) {
						try {
							write.append(MDate.get_ymd_hms() + "『" + level
									+ "』: " + e.getMessage() + "\n");
						} catch (IOException e1) {
						}
					}

				}
			}

		}
	}

	/**
	 * 1、如果日记级别为空或者NONE,则不记录内容 2、如果日记的内容与配置文件内容一致时写入日记 3、如果是ALL,则全部显示
	 */
	private boolean bool_log(LogEnum level) {
		return getLevel().equals(LogEnum.ALL.toString()) ? true : MCheck
				.isNull(getLevel())
				|| getLevel().equals(LogEnum.NONE) ? false : level.toString()
				.equals(getLevel()) ? true : false;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public String getLevel() {
		return level;
	}

	public void setLevel(String level) {
		this.level = level;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public boolean isReload() {
		return reload;
	}

	public void setReload(boolean reload) {
		this.reload = reload;
	}
}
