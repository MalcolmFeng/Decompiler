package com.xuanjie.core;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.xuanjie.bean.Class_info;
import com.xuanjie.bean.Fields_info;
import com.xuanjie.bean.Methods_info;
import com.xuanjie.bean.attribute.Attribute_Code_info;
import com.xuanjie.bean.attribute.Attribute_Exceptions_info;
import com.xuanjie.bean.attribute.Attribute_Signature_info;
import com.xuanjie.bean.attribute.Attribute_info;
import com.xuanjie.bean.constant.Constant_Class_info;
import com.xuanjie.bean.constant.Constant_Utf8_info;

public class SrcCreator {

	/**
	 * 根据 class_info Bean对象，拼接 java 源代码 src
	 * 
	 * @return
	 */
	public static String createJavaFileSrc(Class_info class_info) {

		StringBuffer sBuffer = new StringBuffer();

		Constant_Class_info constant_Class_info_ThisClass = (Constant_Class_info) class_info.getConstant_pool_Map()
				.get(class_info.getThis_class_index() - 1);
		Constant_Utf8_info constant_Utf8_info_ThisClass = (Constant_Utf8_info) class_info.getConstant_pool_Map()
				.get(constant_Class_info_ThisClass.getIndex() - 1);
		String thisClasQulified_String = constant_Utf8_info_ThisClass.getBytes();
		String thisClass_String = thisClasQulified_String.substring(thisClasQulified_String.lastIndexOf("/") + 1);

		// 权限定名--------------------------------------------------------------------------------------------
		sBuffer.append("package "
				+ thisClasQulified_String.substring(0, thisClasQulified_String.lastIndexOf("/")).replace("/", ".")
				+ "; \n\n");

		// 导包------------------------------------------------------------------------------------------------
		Set<String> set = new HashSet<>();
		for (int i = 0; i < class_info.getCp_count() - 1; i++) {
			String type = class_info.getConstant_pool_Map().get(i).getConstant_pool_info_Type();
			if (type.equals("Constant_Utf8_info")) {
				Constant_Utf8_info aa = (Constant_Utf8_info) class_info.getConstant_pool_Map().get(i);
				String info = aa.getBytes();
				if (info.contains("/") && !info.contains("(")) {
					if (info.contains("<")) {
						String string = info.substring(info.indexOf("L") + 1, info.indexOf("<"));
						set.add(string.replace("/", "."));
					} else {
						if (!info.contains(";")) {
							String string = info.substring(info.indexOf("L") + 1);
							set.add(string.replace("/", "."));
						} else {
							String string = info.substring(info.indexOf("L") + 1, info.indexOf(";"));
							set.add(string.replace("/", "."));
						}

					}
				}
			}
		}
		for (String string : set) {
			sBuffer.append("import " + string + "\n");
		}
		sBuffer.append("\n");

		// 类的开始---------------------------------------------------------------------------------------------
		String access_flag = AccessFlagConvertor.classAccessFlagConvertor(class_info.getAccess_flag());
		String superClass_String = superClassJudge(class_info);
		String interfaceClass_String = interfaceClassJudge(class_info);
		sBuffer.append(access_flag + thisClass_String + " " + superClass_String + interfaceClass_String + "{ \n\n");

		// 所有字段----------------------------------------------------------------------------------------------
		for (Fields_info fields_info : class_info.getFields_info_List()) {

			// 访问标识 字符串
			String fields_accessFlag = fields_info.getAccess_flag();
			String accessFlagString = AccessFlagConvertor.fieldAccessFlagConvertor(fields_accessFlag);

			// 字段 和 返回值 和 参数
			Constant_Utf8_info nameindexConstant = (Constant_Utf8_info) class_info.getConstant_pool_Map()
					.get(fields_info.getName_index() - 1);
			Constant_Utf8_info descriptorConstant = (Constant_Utf8_info) class_info.getConstant_pool_Map()
					.get(fields_info.getDescriptor_index() - 1);

			String field_name = nameindexConstant.getBytes(); // sum
			String descriptor = descriptorConstant.getBytes(); // 属性描述 C

			String fieldType = "";
			if (fields_info.getAttributes_count() != 0) {
				for (Attribute_info temp : fields_info.getAttributes_list()) {
					if (temp.getAttribute_type().equals("Attribute_Signature_info")) {
						Attribute_Signature_info attribute_Signature_info = (Attribute_Signature_info) temp;
						Constant_Utf8_info constant_Utf8_info = (Constant_Utf8_info) class_info.getConstant_pool_Map()
								.get(attribute_Signature_info.getSignature_index() - 1);
						String signature = constant_Utf8_info.getBytes();
						// 带有泛型的参数类型
						fieldType = ParamsConvertor.paramsConvertorFieldTypeWithGeneric(signature, descriptor);
					}
				}
			}
			if (!fieldType.contains("<") && !fieldType.contains(">")) {
				// 不带泛型的参数类型
				fieldType = ParamsConvertor.paramsConvertorFieldType(descriptor);
			}

			// 字段的开始
			sBuffer.append("\t" + accessFlagString + fieldType + " " + field_name + " = null;\n");
		}
		sBuffer.append("\n");

		// 所有方法----------------------------------------------------------------------------------------------
		for (Methods_info methods_info : class_info.getMethods_info_List()) {

			// 访问标识 字符串
			String method_accessFlag = methods_info.getAccess_flag();
			String accessFlagString = AccessFlagConvertor.methodAccessFlagConvertor(method_accessFlag);

			// 方法名 和 返回值 和 参数
			Constant_Utf8_info nameindexConstant = (Constant_Utf8_info) class_info.getConstant_pool_Map()
					.get(methods_info.getName_index() - 1);
			Constant_Utf8_info descriptorConstant = (Constant_Utf8_info) class_info.getConstant_pool_Map()
					.get(methods_info.getDescriptor_index() - 1);
			String method_name = nameindexConstant.getBytes(); // sum
			String descriptor = descriptorConstant.getBytes(); // 方法描述 (II)I

			// 拼接 throw 的异常
			String throwString = throwExceptionsJudge(class_info, methods_info);

			// 1.如果是类初始化方法
			if (method_name.equals("<clinit>")) {
				continue;
			}

			// 2.如果是构造方法
			if (method_name.equals("<init>")) {
				method_name = thisClass_String;
			}

			for (int i = 0; i < methods_info.getAttributes_count(); i++) {
				Attribute_info attributes_info = methods_info.getAttributes_list().get(i);
				String attributeType = attributes_info.getAttribute_type();
				if (attributeType.equals("Attribute_Signature_info")) {
					Attribute_Signature_info attribute_Signature_info = (Attribute_Signature_info) attributes_info;
					Constant_Utf8_info attrtypeString = (Constant_Utf8_info) class_info.getConstant_pool_Map()
							.get(attribute_Signature_info.getSignature_index() - 1);
					descriptor = attrtypeString.getBytes();
				}
			}
			// 拼接 方法返回值 方法名（方法）
			String returnNameParams = jointMethodReturnNameParams(method_name, descriptor);

			// 3.如果是 abstract 方法 或者 native 方法， 则没有方法体。
			if (method_accessFlag.substring(1, 2).equals("4") || method_accessFlag.substring(1, 2).equals("1")) {
				sBuffer.append("\t" + accessFlagString + returnNameParams + throwString + "; \n\n");
				continue;
			}

			// 4.如果是有方法体的方法
			sBuffer.append("\t" + accessFlagString + returnNameParams + throwString + "{ \n");

			// 代码 指令
			for (int i = 0; i < methods_info.getAttributes_count(); i++) {

				Attribute_info attributes_info = methods_info.getAttributes_list().get(i);
				String attributeType = attributes_info.getAttribute_type();
				if (attributeType.equals("Code")) {
					Attribute_Code_info attribute_Code_info = (Attribute_Code_info) attributes_info;

					// 属性表类型
					int attrType_index = attribute_Code_info.getAttribute_name_index();
					Constant_Utf8_info attrtypeString = (Constant_Utf8_info) class_info.getConstant_pool_Map()
							.get(attrType_index - 1);

					if (attrtypeString.getBytes().equals("Code")) {
						for (Map.Entry<Integer, String> mapTemp : attribute_Code_info.getCodeMap().entrySet()) {
							sBuffer.append("\t\t" + mapTemp.getValue() + "\n");
						}
					}
				}
			}
			sBuffer.append("\t}\n\n");
		}

		// 类的结束----------------------------------------------------------------------------------------------
		sBuffer.append("\n}\n");

		return sBuffer.toString();
	}

	/**
	 * 类继承的父类 判断与拼接
	 * 
	 * @param class_info
	 * @return
	 */
	private static String superClassJudge(Class_info class_info) {
		Constant_Class_info constant_Class_info_ThisClass = (Constant_Class_info) class_info.getConstant_pool_Map()
				.get(class_info.getSuper_class_index() - 1);
		Constant_Utf8_info constant_Utf8_info_ThisClass = (Constant_Utf8_info) class_info.getConstant_pool_Map()
				.get(constant_Class_info_ThisClass.getIndex() - 1);
		String superClassQulified_String = constant_Utf8_info_ThisClass.getBytes();

		String superClass_String = superClassQulified_String.substring(superClassQulified_String.lastIndexOf("/") + 1);
		if (superClass_String.equals("Object")) {
			return " ";
		} else {
			return "extends " + superClass_String + " ";
		}

	}

	/**
	 * 类实现的接口 判断与拼接
	 * 
	 * @param class_info
	 * @return
	 */
	private static String interfaceClassJudge(Class_info class_info) {
		if (class_info.getInterfaces_count() == 0) {
			return "";
		} else {
			StringBuffer sBuffer = new StringBuffer();
			sBuffer.append("implements ");
			for (Integer everyInterface : class_info.getInterfacesList()) {
				Constant_Class_info constant_Class_info = (Constant_Class_info) class_info.getConstant_pool_Map()
						.get(everyInterface - 1);
				Constant_Utf8_info constant_Utf8_info = (Constant_Utf8_info) class_info.getConstant_pool_Map()
						.get(constant_Class_info.getIndex() - 1);
				String interfaceQulifiedName = constant_Utf8_info.getBytes();
				sBuffer.append(interfaceQulifiedName.substring(interfaceQulifiedName.lastIndexOf("/") + 1) + ",");
			}
			String result = sBuffer.toString();

			return result.substring(0, result.length() - 1) + " ";
		}

	}

	/**
	 * 方法的参数，名称，返回值 字符串拼接。
	 * 
	 * @param method_name
	 * @param descriptor
	 * @return
	 */
	private static String jointMethodReturnNameParams(String method_name, String descriptor) {

		// (Ljava/lang/String;Ljava/lang/Character;Ljava/util/Map<Ljava/lang/String;Lcom/jiefupay/service/Test;>;I)Ljava/lang/String;
		String paramsOriString = descriptor.substring(descriptor.indexOf("(") + 1, descriptor.lastIndexOf(")")); // 参数类型
		String returnOriString = descriptor.substring(descriptor.lastIndexOf(")") + 1); // 返回类型

		// 解析 返回值类型
		String returnFinalString = ParamsConvertor.paramsConvertorMethodReturnType(returnOriString);

		// 解析 参数类型
		List<String> params = new ArrayList<>();
		ParamsHandler.getParams(paramsOriString, params);

		// 如果没有参数
		if (params.size() == 0) {
			return returnFinalString + " " + method_name + "()";
		}
		// 如果是 main 函数
		if (method_name.equals("main")) {
			return "void main(String[] args)";
		}

		StringBuffer sBuffer = new StringBuffer();
		int i = 0;
		for (String string : params) {
			i++;
			sBuffer.append(string + " args" + i + ",");
		}
		String result = sBuffer.toString();

		return returnFinalString + " " + method_name + "(" + result.substring(0, result.length() - 1) + ")";
	}

	/**
	 * 方法 是否有 throws 异常的判断 并 字符串拼接
	 * 
	 * @param class_info
	 * @param methods_info
	 * @return
	 */
	private static String throwExceptionsJudge(Class_info class_info, Methods_info methods_info) {
		StringBuffer stringBuffer = new StringBuffer();
		Boolean flag = false;
		for (int i = 0; i < methods_info.getAttributes_count(); i++) {
			Attribute_info attributes_info = methods_info.getAttributes_list().get(i);
			String attributeType = attributes_info.getAttribute_type();
			if (attributeType.equals("Exceptions")) {
				// 设置为有 throws 异常
				flag = true;
				// 获取 方法的Attribute_Exceptions_info属性，进行解析
				Attribute_Exceptions_info attribute_Exceptions_info = (Attribute_Exceptions_info) attributes_info;
				// 拼装 throws 的异常
				for (Integer iterable_element : attribute_Exceptions_info.getException_index_tableList()) {
					Constant_Class_info constant_Class_info = (Constant_Class_info) class_info.getConstant_pool_Map()
							.get(iterable_element - 1);
					Constant_Utf8_info constant_Utf8_info = (Constant_Utf8_info) class_info.getConstant_pool_Map()
							.get(constant_Class_info.getIndex() - 1);
					String allname = constant_Utf8_info.getBytes();
					stringBuffer.append(allname.substring(allname.lastIndexOf("/") + 1) + ", ");
				}
			}
		}
		if (flag) {
			// 如果有 throws 异常
			String string = stringBuffer.toString();
			return " throws " + string.substring(0, string.length() - 2);
		} else {
			// 如果没有 throws 异常
			return "";
		}
	}

}
