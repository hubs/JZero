package com.jzero.upload;

import java.io.*;
import java.awt.*;
import java.awt.image.*;
import java.awt.Graphics;
import java.awt.color.ColorSpace;
import javax.imageio.ImageIO;

import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGImageEncoder;

public class MImageTools {
	/**
	 * 缩放图像
	 * 
	 * @param srcImageFile
	 *            源图像文件地址
	 * @param result
	 *            缩放后的图像地址
	 * @param scale
	 *            缩放比例
	 * @param flag
	 *            缩放选择:true 放大; false 缩小;
	 */
	public static void scale(File srcImageFile, String result, int scale,
			boolean flag) {
		try {
			BufferedImage src = ImageIO.read(srcImageFile); // 读入文件
			int width = src.getWidth(); // 得到源图宽
			int height = src.getHeight(); // 得到源图长
			if (flag) {
				// 放大
				width = width * scale;
				height = height * scale;
			} else {
				// 缩小
				width = width / scale;
				height = height / scale;
			}
			Image image = src.getScaledInstance(width, height,Image.SCALE_DEFAULT);//返回一个绽放版本,Image.SCALE_DEFAULT(使用默认的图像缩放算法。)
			BufferedImage tag = new BufferedImage(width, height,BufferedImage.TYPE_INT_RGB);//表示一个图像，它具有合成整数像素的 8 位 RGB 颜色分量。
			Graphics g = tag.getGraphics();
			g.drawImage(image, 0, 0, null); // 绘制缩小后的图
			g.dispose();
			ImageIO.write(tag, "JPEG", new File(result));// 输出到文件流
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	/**
	 * 指定大小
	 */
	public static void scale(File srcImageFile, String result, int width,int height) {
		try {
			BufferedImage src = ImageIO.read(srcImageFile); // 读入文件
			Image image = src.getScaledInstance(width, height,Image.SCALE_DEFAULT);//返回一个绽放版本,Image.SCALE_DEFAULT(使用默认的图像缩放算法。)
			BufferedImage tag = new BufferedImage(width, height,BufferedImage.TYPE_INT_RGB);//表示一个图像，它具有合成整数像素的 8 位 RGB 颜色分量。
			Graphics g = tag.getGraphics();
			g.drawImage(image, 0, 0, null); // 绘制缩小后的图
			g.dispose();
			ImageIO.write(tag, "JPEG", new File(result));// 输出到文件流
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 图像切割
	 * 
	 * @param srcImageFile
	 *            源图像地址
	 * @param descDir
	 *            切片目标文件夹
	 * @param destWidth
	 *            目标切片宽度
	 * @param destHeight
	 *            目标切片高度
	 */
	public static void cut(String srcImageFile, String descDir, int destWidth,
			int destHeight) {
		try {
			Image img;
			ImageFilter cropFilter;
			// 读取源图像
			BufferedImage bi = ImageIO.read(new File(srcImageFile));
			int srcWidth = bi.getHeight(); // 源图宽度
			int srcHeight = bi.getWidth(); // 源图高度
			if (srcWidth > destWidth && srcHeight > destHeight) {
				Image image = bi.getScaledInstance(srcWidth, srcHeight,Image.SCALE_DEFAULT);
				destWidth = 200; // 切片宽度
				destHeight = 150; // 切片高度
				int cols = 0; // 切片横向数量
				int rows = 0; // 切片纵向数量
				// 计算切片的横向和纵向数量
				if (srcWidth % destWidth == 0) {
					cols = srcWidth / destWidth;
				} else {
					cols = (int) Math.floor(srcWidth / destWidth) + 1;
				}
				if (srcHeight % destHeight == 0) {
					rows = srcHeight / destHeight;
				} else {
					rows = (int) Math.floor(srcHeight / destHeight) + 1;
				}
				// 循环建立切片
				// 改进的想法:是否可用多线程加快切割速度
				for (int i = 0; i < rows; i++) {
					for (int j = 0; j < cols; j++) {
						// 四个参数分别为图像起点坐标和宽高
						// 即: CropImageFilter(int x,int y,int width,int height)
						cropFilter = new CropImageFilter(j * 200, i * 150,
								destWidth, destHeight);
						img = Toolkit.getDefaultToolkit().createImage(
								new FilteredImageSource(image.getSource(),
										cropFilter));
						BufferedImage tag = new BufferedImage(destWidth,
								destHeight, BufferedImage.TYPE_INT_RGB);
						Graphics g = tag.getGraphics();
						g.drawImage(img, 0, 0, null); // 绘制缩小后的图
						g.dispose();
						// 输出为文件
						ImageIO.write(tag, "JPEG", new File(descDir
								+ "pre_map_" + i + "_" + j + ".jpg"));
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/** */
	/**
	 * 图像类型转换 GIF->JPG GIF->PNG PNG->JPG PNG->GIF(X)
	 */
	public static void convert(String source, String result) {
		try {
			File f = new File(source);
			f.canRead();
			f.canWrite();
			BufferedImage src = ImageIO.read(f);
			ImageIO.write(src, "JPG", new File(result));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/** */
	/**
	 * 彩色转为黑白
	 * 
	 * @param source
	 * @param result
	 */
	public static void gray(String source, String result) {
		try {
			BufferedImage src = ImageIO.read(new File(source));
			ColorSpace cs = ColorSpace.getInstance(ColorSpace.CS_GRAY);
			ColorConvertOp op = new ColorConvertOp(cs, null);
			src = op.filter(src, null);
			ImageIO.write(src, "JPEG", new File(result));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	/**
	 * 加水印图片
	 * 包含的属性有：String pressImg,水印图片;String targetImg,目标图片
	 */
	 public  void pressImage(String pressImg, String targetImg,    
	            int x, int y) {    
	        try {    
	            File file = new File(targetImg);  
	            Image src = ImageIO.read(file);    
	            int wideth = src.getWidth(null);    
	            int height = src.getHeight(null);    
	            BufferedImage image = new BufferedImage(wideth, height,    
	                    BufferedImage.TYPE_INT_RGB);    
	            Graphics g = image.createGraphics();    
	            g.drawImage(src, 0, 0, wideth, height, null);    
	   
	            // 水印文件    
	            File _filebiao = new File(pressImg);    
	            Image src_biao = ImageIO.read(_filebiao);    
	            int wideth_biao = src_biao.getWidth(null);    
	            int height_biao = src_biao.getHeight(null);    
	            g.drawImage(src_biao, wideth - wideth_biao - x, height    
	                    - height_biao - y, wideth_biao, height_biao, null);    
	            // /    
	            g.dispose();    
	            FileOutputStream out = new FileOutputStream(targetImg);    
	            JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(out);    
	            encoder.encode(image);    
	            out.close();    
	        } catch (Exception e) {    
	            e.printStackTrace();    
	        }    
	    }   
	/** */
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		String src="C:/1.jpg";
//		scale(src,"C:/2.jpg", 2, false);
		cut(src,"C:/3.jpg",59,59);
		convert(src, "c:/4.jpg");
		gray(src, "c:/5.jpg");
	}
}
