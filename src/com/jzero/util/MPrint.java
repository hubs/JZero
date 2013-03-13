package com.jzero.util;

public class MPrint {
	private static String identifying="-------";
	public static void print(Object obj){
		System.out.println(identifying+"【程序调试打印数据:"+obj+"】"+identifying);
	}
	public static void print(Object ...objects ){
		System.out.println(objects);
	}
	public static void print(Object obj1 ,Object obj2,Object ...obj3){
		System.out.println(obj1+" \t "+obj2+"\t"+obj3);
	}
}
