package com.jzero.render;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpSession;

import com.jzero.log.Log;

/**
 * image验譓 wangujqw@gmail.com
 */
public class MImaRender extends MRender {

	private static final long serialVersionUID = 1L;

	public MImaRender() {
	}

	public void render() {
		response.setContentType("image/jpeg");
		response.setHeader("Pragma", "No-cache");
		response.setHeader("Cache-Control", "no-cache");
		response.setDateHeader("Expires", 0L);
		HttpSession session = request.getSession();

		int width = 60;
		int height = 20;
		BufferedImage image = new BufferedImage(width, height, 1);

		Graphics g = image.getGraphics();

		Random random = new Random();

		g.setColor(getRandColor(200, 250));
		g.fillRect(0, 0, width, height);

		g.setFont(new Font("Times New Roman", 0, 18));

		g.setColor(getRandColor(160, 200));
		for (int i = 0; i < 155; ++i) {
			int x = random.nextInt(width);
			int y = random.nextInt(height);
			int xl = random.nextInt(12);
			int yl = random.nextInt(12);
			g.drawLine(x, y, x + xl, y + yl);
		}

		String sRand = "";
		for (int i = 0; i < 4; ++i) {
			String rand = String.valueOf(random.nextInt(10));
			sRand = sRand + rand;

			g.setColor(new Color(20 + random.nextInt(110), 20 + random
					.nextInt(110), 20 + random.nextInt(110)));
			g.drawString(rand, 13 * i + 6, 16);
		}
		session.setAttribute("rand", sRand);
		g.dispose();
		try {
			ServletOutputStream responseOutputStream = response.getOutputStream();
			ImageIO.write(image, "JPEG", responseOutputStream);
			responseOutputStream.flush();
			responseOutputStream.close();
		} catch (IOException e) {
			Log.me().write_error(e);
		}
	}

	Color getRandColor(int fc, int bc) {
		Random random = new Random();
		if (fc > 255)
			fc = 255;
		if (bc > 255)
			bc = 255;
		int r = fc + random.nextInt(bc - fc);
		int g = fc + random.nextInt(bc - fc);
		int b = fc + random.nextInt(bc - fc);
		return new Color(r, g, b);
	}

	@Override
	public boolean isJsp() {
		// TODO Auto-generated method stub
		return false;
	}
}
