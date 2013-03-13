package com.jzero.upload;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.jzero.util.MPrint;
import com.jzero.util.MRecord;

public class MFFmpeg {
	private String ffmpegPath;
	private static final String FILE_SEPARATOR = File.separator;
	public static MFFmpeg self = new MFFmpeg();

	private MFFmpeg() {
	}

	public static MFFmpeg me() {
		return self;
	}

	public MFFmpeg init(String path) {
		this.ffmpegPath = path;
		return this;
	}

	private static boolean isSurpportedType(String type) {
		Pattern pattern = Pattern.compile(
				"(asx|asf|mpg|wmv|3gp|mp4|mov|avi|flv){1}$",
				Pattern.CASE_INSENSITIVE);
		Matcher matcher = pattern.matcher(type);
		return matcher.find();
	}

	/**
	 * 
	 * @param sourceFile
	 *            将要被转换的目标文件
	 * @param desctination
	 *            转换之后文件的存放路径 ffmpeg commandLine： ffmpeg -y -i
	 *            /usr/local/bin/lh.mp4 -ab 56 -ar 22050 -b 500 -s 320x240
	 *            /usr/local/bin/lh.flv
	 * @throws IOException
	 */
	public MRecord converToFlv(File sourceFile, String destination) {

		String fileName = sourceFile.getName();
		String surffix = fileName.substring(fileName.lastIndexOf(".") + 1);
		if (!isSurpportedType(surffix))
			throw new RuntimeException("unsurpported file type " + surffix);

		List<String> cmdParam = new LinkedList<String>();
		cmdParam.add(ffmpegPath);
		cmdParam.add("-y");
		cmdParam.add("-i");
		cmdParam.add(sourceFile.getAbsolutePath());
		cmdParam.add("-ab");
		cmdParam.add("56");
		cmdParam.add("-ar");
		cmdParam.add("22050");
		cmdParam.add("-b");
		cmdParam.add("500");
		cmdParam.add("-s");
		cmdParam.add("320*240");
		cmdParam.add(destination + FILE_SEPARATOR
				+ fileName.substring(0, fileName.lastIndexOf(".")) + ".flv");

		return execCmd(cmdParam);
	}

	/**
	 * 
	 * 获取图片的第一帧 ffmpeg commandLine： ffmpeg -y -i /usr/local/bin/lh.3gp -vframes
	 * 1 -r 1 -ac 1 -ab 2 -s 320x240 -f image2 /usr/local/bin/lh.jpg
	 * 
	 * @param sourceFile
	 *            源文件
	 * @param destination
	 *            目标文件
	 * @param surfix
	 *            要保存的图片格式：jpg,jpeg,gif
	 * @throws IOException
	 * @throws IOException
	 */
	public MRecord captureFirstFrame(File sourceFile, String destination) {
		return captureFirstFrame(sourceFile, destination, "jpg");
	}

	public MRecord captureFirstFrame(File sourceFile, String destination,
			String surfix) {
		String fileName = sourceFile.getName();
		String surffix = fileName.substring(fileName.lastIndexOf(".") + 1);
		if (!isSurpportedType(surffix))
			throw new RuntimeException("unsurpported file type " + surffix);

		List<String> cmd = new LinkedList<String>();
		cmd.add(ffmpegPath);
		cmd.add("-y");
		cmd.add("-i");
		cmd.add(sourceFile.getAbsolutePath());
		cmd.add("-vframes");
		cmd.add("1");
		cmd.add("-r");
		cmd.add("1");
		cmd.add("-ac");
		cmd.add("1");
		cmd.add("-ab");
		cmd.add("2");
		cmd.add("-s");
		cmd.add("56*56");
		cmd.add("-f");
		cmd.add("image2");
		cmd.add(destination + FILE_SEPARATOR
				+ fileName.substring(0, fileName.lastIndexOf(".")) + "."
				+ surfix);

		return execCmd(cmd);
	}

	private MRecord execCmd(List<String> cmd) {
		MRecord out = null;
		final ProcessBuilder pb = new ProcessBuilder();
		pb.redirectErrorStream(true);
		pb.command(cmd);
		try {
			final Process p = pb.start();
			InputStream in = p.getInputStream();
			out = pattInfo(in);
			// 开启单独的线程来处理输入和输出流，避免缓冲区满导致线程阻塞.
			try {
				p.waitFor();
			} catch (InterruptedException e) {
				e.printStackTrace();
				Thread.currentThread().interrupt();
			}
			p.getErrorStream().close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return out;
	}

	// 负责从返回信息中读取内容
	private String read(InputStream is) {
		BufferedReader br = null;
		StringBuffer sb = new StringBuffer();
		try {
			br = new BufferedReader(new InputStreamReader(is), 500);

			String line = "";
			while ((line = br.readLine()) != null) {
				// System.out.println(line);
				sb.append(line);
			}
			br.close();
		} catch (Exception e) {
		} finally {
			try {
				if (br != null)
					br.close();
			} catch (Exception e) {
			}
		}
		return sb.toString();
	}

	// 负责从返回的内容中解析
	/**
	 * Input #0, avi, from 'c:\join.avi': Duration: 00:00:10.68(时长), start:
	 * 0.000000(开始时间), bitrate: 166 kb/s(码率) Stream #0:0: Video: msrle
	 * ([1][0][0][0] / 0x0001)(编码格式), pal8(视频格式), 165x97(分辨率), 33.33 tbr, 33.33
	 * tbn, 33.33 tbc Metadata: title : AVI6700.tmp.avi Video #1
	 */
	public MRecord pattInfo(InputStream is) {
		String info = read(is);
		MRecord out = new MRecord();
		String regexDuration = "Duration: (.*?), start: (.*?), bitrate: (\\d*) kb\\/s";
		Pattern pattern = Pattern.compile(regexDuration);
		Matcher m = pattern.matcher(info);
		if (m.find()) {
			out.set("timelen", getTimelen(m.group(1))).set("begintime", m.group(2)).set(
					"kb", m.group(3) + "kb/s");
		}
		return out;
	}
	//格式:"00:00:10.68"
	private int getTimelen(String timelen){
		int min=0;
		String strs[] = timelen.split(":");
		if (strs[0].compareTo("0") > 0) {
			min+=Integer.valueOf(strs[0])*60*60;//秒
		}
		if(strs[1].compareTo("0")>0){
			min+=Integer.valueOf(strs[1])*60;
		}
		if(strs[2].compareTo("0")>0){
			min+=Math.round(Float.valueOf(strs[2]));
		}
		return min;
	}
	public static void main(String[] args) {
		// String ffmpegPath = "c:/ffmpeg.exe";
		// File file = new File("c:/join.avi");
		// MFFmpeg.me().init(ffmpegPath).captureFirstFrame(file, "c:/", "jpg");
		String t = "00:00:10.68";
		String strs[] = t.split(":");
		int min=0;
		if (strs[0].compareTo("0") > 0) {
			min+=Integer.valueOf(strs[0])*60*60;//秒
		}
		if(strs[1].compareTo("0")>0){
			min+=Integer.valueOf(strs[1])*60;
		}
		if(strs[2].compareTo("0")>0){
			min+=Math.round(Float.valueOf(strs[2]));
		}
		MPrint.print(min);
	}

}