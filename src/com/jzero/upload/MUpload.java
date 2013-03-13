package com.jzero.upload;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.WritableRaster;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import com.jzero.core.MInit;
import com.jzero.log.Log;
import com.jzero.token.MToken;
import com.jzero.util.MDate;
import com.jzero.util.MPath;
import com.jzero.util.MRecord;
import com.jzero.util.MTool;

/**
 * 2012-10-14: wangujqw@gmail.com,适合表单一起提交
 */
@SuppressWarnings("unchecked")
public class MUpload {
	private String uploadPath = MPath.me().getWebRoot() + "upload/"
			+ MDate.get_ymd(); // 上传文件的目录
	private File uploadFile;

	public MUpload init(String upPath) {
		this.uploadPath = upPath;
		init();
		return this;
	}

	public MUpload init() {
		uploadFile = new File(uploadPath);
		if (!uploadFile.exists()) {
			uploadFile.mkdirs();
		}
		return this;
	}

	private static MUpload upload = new MUpload();

	private MUpload() {
	}

	public static MUpload me() {
		return upload.init();
	}

	public static MUpload me(String path) {
		return upload.init(path);
	}

	public MRecord upload() {
		HttpServletRequest request = MInit.get().request();
		MRecord record = new MRecord();
		DiskFileItemFactory factory = new DiskFileItemFactory();
		factory.setSizeThreshold(4096); // 设置缓冲区大小，这里是4kb
		ServletFileUpload upload = new ServletFileUpload(factory);
		File tempfile = new File(System.getProperty("java.io.tmpdir"));
		factory.setRepository(tempfile);
		upload.setSizeMax(4194304); // 设置最大文件尺寸，这里是4MB
		upload.setHeaderEncoding("UTF-8");
		try {
			List<FileItem> items = upload.parseRequest(request);// 得到所有的文件
			Iterator<FileItem> i = items.iterator();
			while (i.hasNext()) {
				FileItem fi = (FileItem) i.next();
				if (fi.isFormField()) {// 如果是普通表单项目，显示表单内容。
					record.set(fi.getFieldName(), MTool.UTF8(fi.getString()));
				} else {
					String fileName = fi.getName();
					if (fileName != null) {
						String name = fi.getName();
						String extension = "";
						if (name.lastIndexOf(".") != -1) {
							extension = name.substring(name.lastIndexOf("."));
						}
						String save_name = System.currentTimeMillis()
								+ extension.toLowerCase();
						File savedFile = new File(uploadPath, save_name);
						fi.write(savedFile);
						check_process(name, savedFile);// 检查压缩图片
						record.set(fi.getFieldName(), MDate.get_ymd() + "/"
								+ save_name);
					}
				}
			}
		} catch (Exception e) {
			Log.me().write_error(e);
		}
		if (record.isContainsKey(MToken.me().getCsrf_toke_name())) {
			MToken.me()
					.csrf_verify(record.get(MToken.me().getCsrf_toke_name()));
			record.remove(MToken.me().getCsrf_toke_name());
		}
		return record;

	}

	// 检查,如果是图片,则进行压缩
	public static void check_process(String name, File saveFile) {
		String extension = "";
		if (name.lastIndexOf(".") != -1) {
			extension = name.substring(name.lastIndexOf(".") + 1);
		}
		Pattern pattern = Pattern.compile("bmp|gif|gepg|png|jpg");
		Matcher matcher = pattern.matcher(name);
		if (matcher.find()) {
			process(extension, saveFile);
		}
	}

	// 图片压缩
	private static void process(String extension, File desc_file) {

		// 图片
		BufferedImage src = null;
		try {
			src = javax.imageio.ImageIO.read(desc_file);
		} catch (IOException e) {
			e.printStackTrace();
		}
		Image image = null;
		BufferedImage oimage = null;
		image = src.getScaledInstance(src.getWidth(), src.getHeight(),
				Image.SCALE_DEFAULT);
		oimage = new BufferedImage(src.getWidth(), src.getHeight(),
				Image.SCALE_DEFAULT);
		Graphics g = oimage.getGraphics();
		g.drawImage(image, 0, 0, null);
		g.dispose();
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		try {
			ImageIO.write(oimage, extension, bos);
		} catch (IOException e) {
			e.printStackTrace();
		}

		FileOutputStream out = null;
		try {
			out = new FileOutputStream(desc_file);
			out.write(bos.toByteArray()); // 写文件
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (out != null) {
					out.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}

	public List<MRecord> mulupload() {
		List<MRecord> lst = new ArrayList<MRecord>();
		HttpServletRequest request = MInit.get().request();
		DiskFileItemFactory factory = new DiskFileItemFactory();
		factory.setSizeThreshold(4096 * 10); // 设置缓冲区大小，这里是4kb
		ServletFileUpload upload = new ServletFileUpload(factory);
		File tempfile = new File(System.getProperty("java.io.tmpdir"));
		factory.setRepository(tempfile);
		upload.setHeaderEncoding("UTF-8");
		try {
			List<FileItem> items = upload.parseRequest(request);// 得到所有的文件
			Iterator<FileItem> i = items.iterator();
			while (i.hasNext()) {
				FileItem fi = (FileItem) i.next();
				if (!fi.isFormField()) {// 如果是普通表单项目，显示表单内容。
					String fileName = fi.getName();
					if (fileName != null) {
						String name = fi.getName();
						String extension = "";
						if (name.lastIndexOf(".") != -1) {
							extension = name.substring(name.lastIndexOf("."));
						}
						String save_name = System.currentTimeMillis()
								+ extension;
						File savedFile = new File(uploadPath, save_name);
						fi.write(savedFile);
						check_process(name, savedFile);// 检查压缩图片
						MRecord record = new MRecord().set(fi.getFieldName(),
								MDate.get_ymd() + "/" + save_name).set("name",
								save_name);
						lst.add(record);
					}
				}
			}
		} catch (Exception e) {
			Log.me().write_error(e);
		}
		return lst;
	}

	/**
	 * 支持中文,文件名长度无限制 from http://royzhou1985.iteye.com/blog/336866 不支持国际化
	 */
	public void download(String filename, String filePath) throws Exception {
		HttpServletRequest request = MInit.get().request();
		HttpServletResponse response = MInit.get().response();
		response.setContentType("text/html;charset=utf-8");
		request.setCharacterEncoding("UTF-8");
		java.io.BufferedInputStream bis = null;
		java.io.BufferedOutputStream bos = null;
		try {
			long fileLength = new File(filePath).length();
			response.setContentType("application/x-msdownload;");
			response.setHeader("Content-disposition", "attachment; filename="
					+ new String(filename.getBytes("GBK"), "ISO8859-1"));
			response.setHeader("Content-Length", String.valueOf(fileLength));
			bis = new BufferedInputStream(new FileInputStream(filePath));
			bos = new BufferedOutputStream(response.getOutputStream());
			byte[] buff = new byte[2048];
			int bytesRead;
			while (-1 != (bytesRead = bis.read(buff, 0, buff.length))) {
				bos.write(buff, 0, bytesRead);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (bis != null)
				bis.close();
			if (bos != null)
				bos.close();
		}
	}

	//图片缩略
	private static BufferedImage resize(BufferedImage source, int targetW,int targetH) {
		// targetW，targetH分别表示目标长和宽
		int type = source.getType();
		BufferedImage target = null;
		double sx = (double) targetW / source.getWidth();
		double sy = (double) targetH / source.getHeight();
		// 这里想实现在targetW，targetH范围内实现等比缩放。如果不需要等比缩放
		// 则将下面的if else语句注释即可
		if (sx > sy) {
			sx = sy;
			targetW = (int) (sx * source.getWidth());
		} else {
			sy = sx;
			targetH = (int) (sy * source.getHeight());
		}
		if (type == BufferedImage.TYPE_CUSTOM) { // handmade
			ColorModel cm = source.getColorModel();
			WritableRaster raster = cm.createCompatibleWritableRaster(targetW,
					targetH);
			boolean alphaPremultiplied = cm.isAlphaPremultiplied();
			target = new BufferedImage(cm, raster, alphaPremultiplied, null);
		} else
			target = new BufferedImage(targetW, targetH, type);
		Graphics2D g = target.createGraphics();
		g.setRenderingHint(RenderingHints.KEY_RENDERING,
				RenderingHints.VALUE_RENDER_QUALITY);
		g.drawRenderedImage(source, AffineTransform.getScaleInstance(sx, sy));
		g.dispose();
		return target;
	}
	/**
	 * @param fromFileStr :压缩前文件
	 * @param saveToFileStr:压缩后文件
	 * @param width	:图片宽
	 * @param hight :图片高
	 * @throws Exception
	 */
	public static void saveImageAsJpg(String fromFileStr, String saveToFileStr,int width, int hight) throws Exception {
		BufferedImage srcImage;
		String imgType = "jpeg";
		if (fromFileStr.toLowerCase().endsWith(".png")) {
			imgType = "png";
		}
		File saveFile = new File(saveToFileStr);
		File fromFile = new File(fromFileStr);
		srcImage = ImageIO.read(fromFile);
		if (width > 0 || hight > 0) {
			srcImage = resize(srcImage, width, hight);
		}
		ImageIO.write(srcImage, imgType, saveFile);
	}

	public static void main(String argv[]) {
		try {
			// 参数1(from),参数2(to),参数3(宽),参数4(高)
			saveImageAsJpg("E:/Document/My Pictures/3.gif", "c:/6.gif",
					50, 50);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}
