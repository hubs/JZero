package com.jzero.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MMD5 {

	// MD5加码。32位
	private static String MD5(String inStr) {
		MessageDigest md5 = null;
		try {
			md5 = MessageDigest.getInstance("MD5");
		} catch (Exception e) {
			System.out.println(e.toString());
			e.printStackTrace();
			return "";
		}
		char[] charArray = inStr.toCharArray();
		byte[] byteArray = new byte[charArray.length];

		for (int i = 0; i < charArray.length; i++)
			byteArray[i] = (byte) charArray[i];

		byte[] md5Bytes = md5.digest(byteArray);

		StringBuffer hexValue = new StringBuffer();

		for (int i = 0; i < md5Bytes.length; i++) {
			int val = ((int) md5Bytes[i]) & 0xff;
			if (val < 16)
				hexValue.append("0");
			hexValue.append(Integer.toHexString(val));
		}

		return hexValue.toString();
	}

	// 可逆的加密算法
	private static String KL(String inStr) {
		char[] a = double_str(inStr).toCharArray();
		for (int i = 0; i < a.length; i++) {
			a[i] = (char) (a[i] ^ 't');
		}
		String s = new String(a);
		return s;
	}

	private static String double_str(String str) {
		return str + str;
	}

	// 加密后解密
	private static String JM(String inStr) {
		char[] a = inStr.toCharArray();
		for (int i = 0; i < a.length; i++) {
			a[i] = (char) (a[i] ^ 't');
		}
		String k = new String(a);
		return k.substring(0, k.length() / 2);
	}

	public static String StrToMD5(String str) {
		return KL(str);
	}

	public static String toMD5(String str) {
		return MD5(str);
	}

	public static String toMD5ByJAVA(String plainText) {
		StringBuffer buf = new StringBuffer("");
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			md.update(plainText.getBytes());
			byte messageDigest[] = md.digest();
			for (int i = 0; i < messageDigest.length; i++) {
				buf.append(Integer.toHexString(0xFF & messageDigest[i]));
			}
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return buf.toString();
	}

	public static String MD5ToStr(String str) {
		return JM(str);
	}
	public static void test(){
		 String username="Mufasa";
		 String realm="testrealm@host.com";
		 String nonce="dcd98b7102dd2f0e8b11d0f600bfb0c093";
		 String cnonce="0a4f113b";
		 String password="Circle Of Life";
		 String nc="00000001";
		 String method="GET";
		 String qop="auth";
		 String uri="/dir/index.html";
		 
		 String secret = MMD5.toMD5ByJAVA(username + ":" + realm + ":"
					+ password);
			String A2 = MMD5.toMD5ByJAVA(method + ":" + uri);
			String data = nonce + ":" + nc + ":" + cnonce + ":" + qop + ":" + A2;// +":"+opaque;
			String response = MMD5.toMD5ByJAVA(secret + ":" + data);
			MPrint.print(response);
	}
	public static void test2(){
		 String username="ebframe&99&0.5";
		 String realm="wdpf@service.cmcc.cn";
		 String nonce="906e924e1c519ccc073279f5421977e";
		 String cnonce="4a856905";
		 String password="a79352ec7f1946ebaaec2ceb3db64f98";
		 String nc="00001772";
		 String method="GET";
		 String qop="auth";
		 String uri="http://192.168.1.118:8080/WDPF/register?ua=ebframe&os=99&version=0.5";		
//		String client_response="7eca8854baaa6ed536e4db1135420d56";
		 
		 String secret = MMD5.toMD5ByJAVA(username + ":" + realm + ":"
					+ password);
			String A2 = MMD5.toMD5ByJAVA(method + ":" + uri);
			String data = nonce + ":" + nc + ":" + cnonce + ":" + qop + ":" + A2;// +":"+opaque;
			String response = MMD5.toMD5ByJAVA(secret + ":" + data);
			MPrint.print(response);
	}
	// // 测试主函数
	public static void main(String args[]) {
		test2();
//		String name = "root";
//		String pass = "guilinsoft";
//		String m1 = toMD5(name);
//		String m2 = StrToMD5(pass);
//		MPrint.print(m1 + "," + m2);
//		MPrint.print(MD5ToStr(m1) + "," + MD5ToStr(m2));
		// String username="ebframe&99&0.5";
		// String realm="Wang";
		// String nonce="6fabc387f9c42f6a4fd97c2c2f303485";
		// String cnonce="4a856905";
		// String password="a79352ec7f1946ebaaec2ceb3db64f98";
		// password="826e7a57fd11ba10436a0d39723d676";
		// String nc="00000001";
		// String method="GET";
		// String qop="auth";
		// String
		// uri="http://192.168.1.118:8080/WDPF/register?ua=ebframe&os=99&version=0.5";

//		String username = "ebframe&99&0.5";
//		String realm = "wdpf@service.cmcc.cn";
//		String nonce = "423fc0c1704c447fbc075d9ae57cd953";
//		String cnonce = "4a856905";
//		String password = "a79352ec7f1946ebaaec2ceb3db64f98";
//		String nc = "00000001";
//		String method = "GET";
//		String qop = "auth";
//		String uri = "http://lovepost-g3.com:28080/WDPF/register?ua=ebframe&os=99&version=0.5";
//
//		String secret = MMD5.toMD5ByJAVA(username + ":" + realm + ":"
//				+ password);
//		String A2 = MMD5.toMD5ByJAVA(method + ":" + uri);
//		String data = nonce + ":" + nc + ":" + cnonce + ":" + qop + ":" + A2;// +":"+opaque;
//		String response = MMD5.toMD5ByJAVA(secret + ":" + data);
//		MPrint.print(response);
		// f6c1dfc763bf1344d00679511bf0cbbb

		// MPrint.print(MD5.toMD5("077301004a79352ec7f1946ebaaec2ceb3db64f98"));
		// 8d36d94a9035c60eb4928eea6adf42bf

		// String username="Mufasa";
		// String realm="testrealm@host.com";
		// String nonce="dcd98b7102dd2f0e8b11d0f600bfb0c093";
		// String cnonce="0a4f113b";
		// String password="Circle Of Life";
		// String nc="00000001";
		// String method="GET";
		// String qop="auth";
		// String uri="/dir/index.html";
		// MPrint.print(MD5.toMD5("077301004:a79352ec7f1946ebaaec2ceb3db64f98"));
	}
}