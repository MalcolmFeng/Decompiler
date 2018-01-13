package com.xuanjie.core;

import java.util.ArrayList;
import java.util.List;

public class ParamsHandler {

	/**
	 *  获取 方法的参数，放入 List 中
	 *  
	 * @param src
	 * @param params
	 */
	public static void getParams(String src, List<String> params) {
		
		//如果不存在任何参数
		if (src.length()==0) {
			return;
		}
		//如果都是基本数据类型
		if (!src.contains(";")) {
			pureBasicTypeFill(src, params);
		}
		//如果存在引用类型
		if (src.contains(";")) {
			
			// Ljava/lang/String;
			// Ljava/lang/String;Ljava/lang/Object;
			// 
			// Ljava/lang/String;Ljava/lang/Character;Ljava/util/Map;I
			// Ljava/lang/Character;Ljava/util/Map;I
			// [Ljava/lang/String;[C
			// IILjava/lang/Object;
			// [I
			//  Ljava/util/Map<Ljava/lang/String;Lcom/jiefupay/service/Test;>; Ljava/util/Map<Ljava/lang/String;Lcom/jiefupay/service/Test;>; I
			// Ljava/lang/String;
			
			String front = "";
			if (!src.substring(0,1).equals("L") && src.substring(src.indexOf("L")-1,src.indexOf("L")).equals("[")) { // [Ljava 的情况
				if (src.indexOf("L")==1) {
					front = src.substring(0, src.indexOf("L")-1);
				}else {
					if (!src.substring(0,1).contains("L") && src.substring(src.indexOf("L")-2, src.indexOf("L")-1).equals("[")) { // [[java 的情况
						front = src.substring(0, src.indexOf("L")-2);
					}else {
						front = src.substring(0, src.indexOf("L")-1);
					}
				}
			}else {
				front = src.substring(0, src.indexOf("L"));
			}
			
			// 开头
			pureBasicTypeFill(front, params);
			
			
			// 中间
			String middle = "";
			// 尾部
			String laString = "";
			
			String after = src.substring(src.indexOf("L"),src.indexOf(";")+1);
			if (after.contains("<")) {
				// 带有泛型的参数
				middle = src.substring(src.indexOf("L"), src.indexOf(">")+2);
				//Ljava/util/Map<Ljava/lang/String;Lcom/jiefupay/service/Test;>;
				
				String genericParamsString = middle.substring(middle.indexOf("<")+1,middle.indexOf(">"));
				List<String> genericParams = new ArrayList<>();
				getParams(genericParamsString, genericParams);
				
				StringBuffer sBuffer = new StringBuffer();
				for (String temp : genericParams) {
					sBuffer.append( temp+", ");
				}
				String result = sBuffer.toString();
				
				String tempStringafter = after.substring(0,after.indexOf("<")+1);
				
				String genericparams = result.substring(0,result.length()-2);
				params.add(middle.substring(tempStringafter.lastIndexOf("/") + 1, tempStringafter.indexOf("<")) +"<"+genericparams +">");
				
				laString = src.substring(src.indexOf(">") + 2);
			}else {
				//不带泛型的参数
				middle = after.substring(after.indexOf("L"), after.indexOf(";"));
				params.add(middle.substring(middle.lastIndexOf("/") + 1, middle.length()));
				laString = src.substring(src.indexOf(";") + 1);
			}
			
			
			// 尾部
			
			getParams(laString, params);
			
		}
	}
	
	
	public static void pureBasicTypeFill(String src, List<String> params) {
		for (int i = 0; i < src.length(); i++) {
			String content = "";
			
			// 判断是否为一维数组
			if(src.substring(i,i+1).equals("[")) {
				content = src.substring(i, i + 2);
				// 判断是否为二维数组
				if (src.substring(i,i+2).equals("[[")) {
					content = src.substring(i, i + 3);
					// 判断是否是三维数组
					if (src.substring(i,i+3).equals("[[[")) {
						content = src.substring(i, i + 4);
						if (src.substring(i,i+4).equals("[[[[")) {
							content = src.substring(i, i + 5);
						}else {
							content = src.substring(i, i + 4);
							i++;
							i++;
							i++;
						}
					}else {
						content = src.substring(i, i + 3);
						i++;
						i++;
					}
				}else {
					content = src.substring(i, i + 2);
					i++;
				}
			}else {
				content = src.substring(i, i + 1);
			}
			
			String type = ParamsConvertor.paramsConvertorMethodReturnType(content);
			params.add(type);
		}
	}
}
