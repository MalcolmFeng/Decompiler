package com.xuanjie.core;

public class AccessFlagConvertor {

	public static String classAccessFlagConvertor(String hexString) {
		String[] array = new String[4];
		for (int i = 0; i < 4; i++) {
			int startPointer = i;
			array[i] = hexString.substring(startPointer, startPointer + 1);
		}

		StringBuffer result = new StringBuffer();
		if (array[3].equals("1")) {
			result.append("public ");
			;
		}
		if (array[3].equals("2")) {
			result.append("private ");
		}
		if (array[3].equals("4")) {
			result.append("protected ");
		}
		if (array[3].equals("8")) {
			result.append("static ");
		}
		if (array[3].equals("9")) {
			result.append("public static ");
		}
		if (array[3].equals("A")) {
			result.append("private static ");
		}
		if (array[3].equals("C")) {
			result.append("protected static ");
		}

		if (array[2].equals("1")) {
			result.append("final ");
		}
		if (array[1].equals("1")) {
			result.append("native ");
		}
		if (array[1].equals("4")) {
			result.append("abstract ");
		}
		if (array[0].equals("1")) {
			result.append("synthetic ");
		}
		return result.toString();
	}

	public static String methodAccessFlagConvertor(String hexString) { // 0x0001
		String[] array = new String[4];
		for (int i = 0; i < 4; i++) {
			int startPointer = i;
			array[i] = hexString.substring(startPointer, startPointer + 1);
		}

		StringBuffer result = new StringBuffer();
		if (array[3].equals("1")) {
			result.append("public ");
			;
		}
		if (array[3].equals("2")) {
			result.append("private ");
		}
		if (array[3].equals("4")) {
			result.append("protected ");
		}
		if (array[3].equals("8")) {
			result.append("static ");
		}
		if (array[3].equals("9")) {
			result.append("public static ");
		}
		if (array[3].equals("A")) {
			result.append("private static ");
		}
		if (array[3].equals("C")) {
			result.append("protected static ");
		}

		if (array[2].equals("1")) {
			result.append("final ");
		}
		if (array[1].equals("1")) {
			result.append("native ");
		}
		if (array[1].equals("4")) {
			result.append("abstract ");
		}
		if (array[0].equals("1")) {
			result.append("synthetic ");
		}
		return result.toString();
	}

	/**
	 * 字段 访问标识 转换器。
	 * 
	 * @param hexString
	 * @return
	 */
	public static String fieldAccessFlagConvertor(String hexString) {
		String[] array = new String[4];
		for (int i = 0; i < 4; i++) {
			int startPointer = i;
			array[i] = hexString.substring(startPointer, startPointer + 1);
		}

		StringBuffer result = new StringBuffer();
		if (array[3].equals("1")) {
			result.append("public ");
			;
		}
		if (array[3].equals("2")) {
			result.append("private ");
		}
		if (array[3].equals("4")) {
			result.append("protected ");
		}
		if (array[3].equals("8")) {
			result.append("static ");
		}
		if (array[3].equals("9")) {
			result.append("public static ");
		}
		if (array[3].equals("A")) {
			result.append("private static ");
		}
		if (array[3].equals("C")) {
			result.append("protected static ");
		}

		if (array[2].equals("1")) {
			result.append("final ");
		}
		if (array[1].equals("1")) {
			result.append("native ");
		}
		if (array[1].equals("4")) {
			result.append("abstract ");
		}
		if (array[0].equals("1")) {
			result.append("synthetic ");
		}
		return result.toString();
	}

}
