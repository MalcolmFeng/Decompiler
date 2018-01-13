package com.xuanjie.core;

import java.util.ArrayList;
import java.util.List;

public class ParamsConvertor {

	/**
	 * 字段的类型
	 * 
	 * @param str
	 * @param paramsOrReturn
	 * @return
	 */
	public static String paramsConvertorFieldType(String str) {
		String origal = str;
		boolean flag = false;
		if (str.length() == 1 || !str.contains("[")) {
			// 如果不是数组
			flag = true;
		} else {
			if (!str.contains("/")) {
				// 如果是数组
				flag = false;
				str = str.substring(str.length() - 1);
			}
		}

		String result = "";
		switch (str) {
		case "B":
			result = "byte";
			break;
		case "C":
			result = "char";
			break;
		case "D":
			result = "double";
			break;
		case "F":
			result = "float";
			break;
		case "I":
			result = "int";
			break;
		case "J":
			result = "long";
			break;
		case "S":
			result = "short";
			break;
		case "Z":
			result = "boolean";
			break;
		case "V":
			result = "void";
			break;
		default:
			result = str.substring(str.lastIndexOf("/") + 1, str.length() - 1);
			break;
		}
		if (flag) {
			return result;
		} else {
			// 后边加上 n-1个[]
			for (int i = 0; i < origal.length() - 1; i++) {
				result = result + "[]";
			}
			return result;
		}
	}

	/**
	 * 带泛型的参数
	 * 
	 * @param signature
	 * @param str
	 * @param paramsOrReturn
	 * @return
	 */
	public static String paramsConvertorFieldTypeWithGeneric(String signature, String str) {
		String inner = signature.substring(signature.indexOf("<") + 1, signature.indexOf(">"));
		List<String> params = new ArrayList<>();
		ParamsHandler.getParams(inner, params);

		StringBuffer sBuffer = new StringBuffer();
		for (String temp : params) {
			sBuffer.append(temp + ", ");
		}
		String result = sBuffer.toString();

		return str.substring(str.lastIndexOf("/") + 1, str.length() - 1) + "<"
				+ result.substring(0, result.length() - 2) + ">";
	}

	/**
	 * 方法的返回值类型
	 * 
	 * @param str
	 * @param paramsOrReturn
	 * @return
	 */
	public static String paramsConvertorMethodReturnType(String str) {
		String origal = str;
		boolean flag = false;
		if (str.length() == 1 || !str.contains("[")) {
			// 如果不是数组
			flag = true;
		} else {
			if (!str.contains("/")) {
				// 如果是数组
				flag = false;
				str = str.substring(str.length() - 1);
			}
		}
		String result = "";
		switch (str) {
		case "B":
			result = "byte";
			break;
		case "C":
			result = "char";
			break;
		case "D":
			result = "double";
			break;
		case "F":
			result = "float";
			break;
		case "I":
			result = "int";
			break;
		case "J":
			result = "long";
			break;
		case "S":
			result = "short";
			break;
		case "Z":
			result = "boolean";
			break;
		case "V":
			result = "void";
			break;
		default:
			result = str.substring(str.lastIndexOf("/") + 1,str.length()-1);
			break;
		}
		if (flag) {
			return result;
		} else {
			// 后边加上 n-1个[]
			for (int i = 0; i < origal.length() - 1; i++) {
				result = result + "[]";
			}
			return result;
		}
	}

}
