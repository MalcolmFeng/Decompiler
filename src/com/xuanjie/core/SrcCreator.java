package com.xuanjie.core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.log4j.helpers.OnlyOnceErrorHandler;

import java.util.Set;
import java.util.TreeMap;

import com.xuanjie.bean.Class_info;
import com.xuanjie.bean.FieldNameAndType;
import com.xuanjie.bean.Fields_info;
import com.xuanjie.bean.Methods_info;
import com.xuanjie.bean.OpcodeAndOperand;
import com.xuanjie.bean.SrcAndType;
import com.xuanjie.bean.attribute.Attribute_Code_info;
import com.xuanjie.bean.attribute.Attribute_Exceptions_info;
import com.xuanjie.bean.attribute.Attribute_LineNumberTable_info;
import com.xuanjie.bean.attribute.Attribute_LocalVariableTable_info;
import com.xuanjie.bean.attribute.Attribute_Signature_info;
import com.xuanjie.bean.attribute.Attribute_info;
import com.xuanjie.bean.attribute.Line_number_info;
import com.xuanjie.bean.attribute.Local_variable_info;
import com.xuanjie.bean.constant.Constant_Class_info;
import com.xuanjie.bean.constant.Constant_Fieldref_info;
import com.xuanjie.bean.constant.Constant_InterfaceMethodref_info;
import com.xuanjie.bean.constant.Constant_Methodref_info;
import com.xuanjie.bean.constant.Constant_NameAndType_info;
import com.xuanjie.bean.constant.Constant_String_info;
import com.xuanjie.bean.constant.Constant_Utf8_info;
import com.xuanjie.bean.constant.Constant_X_info;
import com.xuanjie.utils.Hex;

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
		Constant_Class_info constant_Class_info_thisClass_Index = (Constant_Class_info) class_info
				.getConstant_pool_Map().get(class_info.getThis_class_index() - 1);
		Constant_Utf8_info constant_Utf8_info_thisClass_Bytes = (Constant_Utf8_info) class_info.getConstant_pool_Map()
				.get(constant_Class_info_thisClass_Index.getIndex() - 1);

		for (String string : set) {
			if (string.startsWith("java.lang.") || string.startsWith(constant_Utf8_info_thisClass_Bytes.getBytes().replaceAll("/", "."))) {
				continue;
			}
			sBuffer.append("import " + string + "\n");
		}
		sBuffer.append("\n");

		// 类的开始---------------------------------------------------------------------------------------------
		String access_flag = AccessFlagConvertor.classAccessFlagConvertor(class_info.getAccess_flag());
		String superClass_String = superClassJudge(class_info);
		String interfaceClass_String = interfaceClassJudge(class_info);
		sBuffer.append(
				access_flag + "class " + thisClass_String + " " + superClass_String + interfaceClass_String + "{ \n\n");

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

			// Code 属性的 属性表 LineNumberTable 和 LocalVariableTable
			Map<Integer, FieldNameAndType> variableNameMap = new HashMap<>();
			TreeMap<Integer, Integer> lineMap = new TreeMap<>();

			// 开始往上边两个变量写入数据
			for (int i = 0; i < methods_info.getAttributes_count(); i++) {
				Attribute_info attributes_info = methods_info.getAttributes_list().get(i);
				String attributeType = attributes_info.getAttribute_type();
				if (attributeType.equals("Code")) {
					Attribute_Code_info attribute_Code_info = (Attribute_Code_info) attributes_info;

					for (Attribute_info attinfo : attribute_Code_info.getAttributesList()) {
						String attrType = attinfo.getAttribute_type();

						// 数据的读取与存放 行数对应、变量名 slot 对应。
						if (attrType.equals("LocalVariableTable")) {
							// 本地变量名称 和 局部变量表的对应
							Attribute_LocalVariableTable_info attribute_LocalVariableTable_info = (Attribute_LocalVariableTable_info) attinfo;
							for (Local_variable_info local_variable_info : attribute_LocalVariableTable_info
									.getLocal_variable_table_List()) {
								Constant_Utf8_info constant_Utf8_info_variableName = (Constant_Utf8_info) class_info
										.getConstant_pool_Map().get(local_variable_info.getName_index() - 1);
								Constant_Utf8_info constant_Utf8_info_Descriptor = (Constant_Utf8_info) class_info
										.getConstant_pool_Map().get(local_variable_info.getDescriptor_index() - 1);

								int slotIndex = local_variable_info.getIndex();
								String variableName = constant_Utf8_info_variableName.getBytes();
								String type = constant_Utf8_info_Descriptor.getBytes();

								FieldNameAndType fieldNameAndType = new FieldNameAndType();
								fieldNameAndType.setName(variableName);
								fieldNameAndType.setType(type);
								variableNameMap.put(slotIndex, fieldNameAndType);
							}
						} else if (attrType.equals("LineNumberTable")) {
							// 行数的对应
							Attribute_LineNumberTable_info attribute_LineNumberTable_info = (Attribute_LineNumberTable_info) attinfo;
							for (Line_number_info line_number_info : attribute_LineNumberTable_info
									.getLine_number_table_List()) {
								int javaLine = line_number_info.getLine_number();
								int bytecodeLine = line_number_info.getStart_pc();
								lineMap.put(javaLine, bytecodeLine);
							}
						}
					}
				}
			}

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

			// 拼接 方法返回值 方法名（方法）
			String returnNameParams = jointMethodReturnNameParams(method_name, descriptor, variableNameMap);

			// 3.如果是 abstract 方法 或者 native 方法， 则没有方法体。
			if (method_accessFlag.substring(1, 2).equals("4") || method_accessFlag.substring(1, 2).equals("1")) {
				sBuffer.append("\t" + accessFlagString + returnNameParams + throwString + "; \n\n");
				continue;
			}

			// 4.如果是有方法体的方法
			sBuffer.append("\t" + accessFlagString + returnNameParams + throwString + "{ \n");

			// 5.方法体
			for (int i = 0; i < methods_info.getAttributes_count(); i++) {

				Attribute_info attributes_info = methods_info.getAttributes_list().get(i);
				String attributeType = attributes_info.getAttribute_type();

				if (attributeType.equals("Code")) {
					Attribute_Code_info attribute_Code_info = (Attribute_Code_info) attributes_info;

					// 存放 一行 java 代码的 多行 opcode 码的 map，
					TreeMap<Integer, OpcodeAndOperand> oneLineJava_OpcodeAndOperand_tempMap = null;

					// 方法体只有一行代码
					if (lineMap.size() <= 1) {
						oneLineJava_OpcodeAndOperand_tempMap = new TreeMap<>();
						int z = 1;
						for (Entry<Integer, OpcodeAndOperand> temp : attribute_Code_info.getCodeMap().entrySet()) {
							OpcodeAndOperand opcodeAndOperand = temp.getValue();
							if (opcodeAndOperand == null) {
								continue;
							}
							z++;
							oneLineJava_OpcodeAndOperand_tempMap.put(z, opcodeAndOperand);
						}
						
						// 4.处理本行java代码（多行 opcode 代码）
						StringBuffer onelineSrcBuffer = oneJavaLineHandlerDecompiler(class_info, variableNameMap,
								oneLineJava_OpcodeAndOperand_tempMap);

						// 5.拼接本行 java 代码
						if (onelineSrcBuffer.length()==0) {
							sBuffer.append("\t\t" + onelineSrcBuffer + "\n");
						}else {
							sBuffer.append("\t\t" + onelineSrcBuffer + ";\n");
						}
						
					} else {
						// 方法体有多行代码,遍历每一行java 代码进行拼接
						for (Map.Entry<Integer, Integer> lineTemp : lineMap.entrySet()) {
							Integer lineNumber = lineTemp.getKey(); // 当前 java 行数

							// 判断是否是最后一行
							if (lineNumber != lineMap.lastKey()) {

								oneJavaLineHandler(class_info, sBuffer, variableNameMap, lineMap, attribute_Code_info,
										lineTemp, lineNumber);
							} else {
								// 当前 src 行数 等于
								break;
							}

						}
					}

				}
			}
			sBuffer.append("\t}\n\n");
		}
		// }
		// 类的结束----------------------------------------------------------------------------------------------
		sBuffer.append("\n}\n");

		return sBuffer.toString();
	}

	/**
	 * 一行 java 代码的处理
	 * @param class_info
	 * @param sBuffer
	 * @param variableNameMap
	 * @param lineMap
	 * @param attribute_Code_info
	 * @param lineTemp
	 * @param lineNumber
	 */
	public static void oneJavaLineHandler(Class_info class_info, StringBuffer sBuffer,
			Map<Integer, FieldNameAndType> variableNameMap, TreeMap<Integer, Integer> lineMap,
			Attribute_Code_info attribute_Code_info, Map.Entry<Integer, Integer> lineTemp, Integer lineNumber) {
		
		// 1.存放 一行 java 代码的 多行 opcode 码的 map，
		TreeMap<Integer, OpcodeAndOperand> oneLineJava_OpcodeAndOperand_tempMap;

		// 2.获取在此行 JAVA 代码的 opcode 开始和结束
		Integer o = 1;
		Integer opcodelineTemp = 0;
		opcodelineTemp = judgeNullLine(lineMap, lineNumber, opcodelineTemp, o); // 递归，返回

		Integer startOpcodeLineNumber = lineTemp.getValue(); // 开始 opcode 行数
		Integer endOpcodeLneNumber = opcodelineTemp - 1; // 结束 opcode 行数

		// 3.遍历 一行 java 代码的多行opcode，存储到oneLineJava_OpcodeAndOperand_tempMap
		oneLineJava_OpcodeAndOperand_tempMap = new TreeMap<>();
		int z = 1;
		for (int j = startOpcodeLineNumber; j <= endOpcodeLneNumber; j++) {
			OpcodeAndOperand opcodeAndOperand = attribute_Code_info.getCodeMap().get(j);
			if (opcodeAndOperand == null) {
				continue;
			}
			z++;
			oneLineJava_OpcodeAndOperand_tempMap.put(z, opcodeAndOperand);
		}

		// 4.处理本行java代码（多行 opcode 代码）
		StringBuffer onelineSrcBuffer = oneJavaLineHandlerDecompiler(class_info, variableNameMap,
				oneLineJava_OpcodeAndOperand_tempMap);

		// 5.拼接本行 java 代码
		sBuffer.append("\t\t" + onelineSrcBuffer + ";\n");
	}

	/**
	 * 处理一行 java 代码 的反编译逻辑 opcode --> java Src
	 * 
	 * @param class_info
	 * @param variableNameMap
	 * @param oneLineJava_OpcodeAndOperand_tempMap
	 * @return
	 */
	public static StringBuffer oneJavaLineHandlerDecompiler(Class_info class_info,
			Map<Integer, FieldNameAndType> variableNameMap,
			TreeMap<Integer, OpcodeAndOperand> oneLineJava_OpcodeAndOperand_tempMap) {

		// 暂时存放一行所拼接的 java 代码的 StringBuffer。
		StringBuffer onelineSrcBuffer = new StringBuffer();
		// 暂时存放未构造的new 的对象
		Map<Integer, SrcAndType> notConstructedMap = new TreeMap<>();
		// 暂时存放已经构造的new 的对象
		Map<Integer, SrcAndType> alreadyConstructedMap = new TreeMap<>();
		// 暂时存放有用参数的 map
		Map<Integer, SrcAndType> map = new TreeMap<>();

		// 处理临时存到的 map (遍历一行 java 代码中的 多行字节码指令)
		for (Map.Entry<Integer, OpcodeAndOperand> temp : oneLineJava_OpcodeAndOperand_tempMap.entrySet()) {
			int line = temp.getKey();
			String opcode = temp.getValue().getOpcode();
			Object operand_Object = temp.getValue().getOperand();

			if (opcode.equals("new")) { // new 对象
				int operand = (int) operand_Object;
				Constant_X_info constant_X_info = class_info.getConstant_pool_Map().get(operand - 1);
				Constant_Class_info constant_Class_info_newclassname = (Constant_Class_info) constant_X_info;
				Constant_Utf8_info constant_Utf8_info_class = (Constant_Utf8_info) class_info.getConstant_pool_Map()
						.get(constant_Class_info_newclassname.getIndex() - 1);
				String newclass_qulitifyName = constant_Utf8_info_class.getBytes();
				String newclassname = newclass_qulitifyName.substring(newclass_qulitifyName.lastIndexOf("/") + 1);

				// 遇到 new ，将此对象放入暂时存放对象的 Map 中最后一个。
				SrcAndType srcAndType = new SrcAndType();
				srcAndType.setType(newclass_qulitifyName);
				srcAndType.setSrc(newclassname);
				srcAndType.setTime("new");
				srcAndType.setFlag(false); // 设置为未执行构造。
				notConstructedMap.put(notConstructedMap.size() + 1, srcAndType);
				map.put(map.size() + 1, srcAndType);

			} else if (opcode.equals("dup")) { // 复制对象值并压入栈顶
				continue;
			} else if (opcode.contains("store_")) {
				String slot = opcode.substring(opcode.lastIndexOf("_") + 1);
				FieldNameAndType fieldNameAndType = variableNameMap.get(Integer.parseInt(slot));
				String name = fieldNameAndType.getName();
				String typeQualitfy = fieldNameAndType.getType();
				String type = typeQualitfy.substring(typeQualitfy.lastIndexOf("/") + 1, typeQualitfy.length() - 1);

				onelineSrcBuffer.insert(0, type + " " + name + " = ");
				
				for (Entry<Integer, SrcAndType> tempIn : map.entrySet()) {
					tempIn.getValue();
				}
			} else if (opcode.equals("astore")) {
				int operand = (int) operand_Object;
				int slot = operand;
				FieldNameAndType fieldNameAndType = variableNameMap.get(slot);
				String name = fieldNameAndType.getName();
				String typeQualitfy = fieldNameAndType.getType();
				String type = typeQualitfy.substring(typeQualitfy.lastIndexOf("/") + 1, typeQualitfy.length() - 1);

				onelineSrcBuffer.insert(0, type + " " + name + " = ");
			} else if (opcode.contains("ldc")) {
				int operand = (int) operand_Object;
				Constant_X_info constant_X_info = class_info.getConstant_pool_Map().get(operand - 1);
				Constant_String_info constant_String_info = (Constant_String_info) constant_X_info;
				Constant_Utf8_info constant_Utf8_info_class = (Constant_Utf8_info) class_info.getConstant_pool_Map()
						.get(constant_String_info.getIndex() - 1);
				String strValue = constant_Utf8_info_class.getBytes();

				SrcAndType srcAndType = new SrcAndType();
				srcAndType.setTime("new");
				srcAndType.setFlag(true);
				srcAndType.setSrc("\"" + strValue + "\"");
				srcAndType.setType("java.lang.String");
				map.put(map.size() + 1, srcAndType);

			} else if (opcode.contains("bipush")) {
				int operand = (int) operand_Object;

				SrcAndType srcAndType = new SrcAndType();
				srcAndType.setTime("new");
				srcAndType.setFlag(true);
				srcAndType.setSrc(operand + "");
				srcAndType.setType("java.lang.String");
				map.put(map.size() + 1, srcAndType);

			} else if (opcode.contains("sipush")) {
				int operand = (int) operand_Object;

				SrcAndType srcAndType = new SrcAndType();
				srcAndType.setTime("new");
				srcAndType.setFlag(true);
				srcAndType.setSrc(operand + "");
				srcAndType.setType("java.lang.String");
				map.put(map.size() + 1, srcAndType);

			} else if (opcode.contains("load_")) {
				String slot = opcode.substring(opcode.lastIndexOf("_") + 1);
				FieldNameAndType fieldNameAndType = variableNameMap.get(Integer.parseInt(slot));
				String fieldName = fieldNameAndType.getName();
				String type = fieldNameAndType.getType();

				// 既然是 load 到栈，那么一定有用，放到有用 map 中。
				SrcAndType srcAndType = new SrcAndType();
				srcAndType.setType(type);
				srcAndType.setSrc(fieldName);
				srcAndType.setTime("old");
				srcAndType.setFlag(true);
				map.put(map.size() + 1, srcAndType);

			} else if (opcode.equals("aload")) {
				int slot = (int) operand_Object;
				FieldNameAndType fieldNameAndType = variableNameMap.get(slot);
				String fieldName = fieldNameAndType.getName();
				String type = fieldNameAndType.getType();

				// 既然是 load 到栈，那么一定有用，放到有用 map 中。
				SrcAndType srcAndType = new SrcAndType();
				srcAndType.setType(type);
				srcAndType.setSrc(fieldName);
				srcAndType.setTime("old");
				srcAndType.setFlag(true);
				map.put(map.size() + 1, srcAndType);

			} else if (opcode.equals("getfield")) {
				int slot = (int) operand_Object;
				Constant_Fieldref_info constant_Fieldref_info = (Constant_Fieldref_info) class_info
						.getConstant_pool_Map().get(slot - 1);
				System.out.println(constant_Fieldref_info);

			} else if (opcode.equals("putfield")) {
				int slot = (int) operand_Object;
				Constant_Fieldref_info constant_Fieldref_info = (Constant_Fieldref_info) class_info
						.getConstant_pool_Map().get(slot - 1);

				// 指向 class 的 index 和 指向 nameandtype 的 index
				Constant_Class_info constant_Class_info = (Constant_Class_info) class_info.getConstant_pool_Map()
						.get(constant_Fieldref_info.getIndex() - 1);
				Constant_NameAndType_info constant_NameAndType_info = (Constant_NameAndType_info) class_info
						.getConstant_pool_Map().get(constant_Fieldref_info.getIndex2() - 1);

				// class
				Constant_Utf8_info constant_Utf8_info_class = (Constant_Utf8_info) class_info.getConstant_pool_Map()
						.get(constant_Class_info.getIndex() - 1);
				// name and type
				Constant_Utf8_info constant_Utf8_info_name = (Constant_Utf8_info) class_info.getConstant_pool_Map()
						.get(constant_NameAndType_info.getIndex() - 1);
				Constant_Utf8_info constant_Utf8_info_type = (Constant_Utf8_info) class_info.getConstant_pool_Map()
						.get(constant_NameAndType_info.getIndex2() - 1);

				String className = constant_Utf8_info_class.getBytes();
				String invoke_field_name = constant_Utf8_info_name.getBytes();
				String invoke_field_type = constant_Utf8_info_type.getBytes();

				// 既然是 load 到栈，那么一定有用，放到有用 map 中。

				onelineSrcBuffer.append(map.get(1).getSrc() + "." + invoke_field_name + " = ");
				for (int j = 2; j <= map.size(); j++) {
					onelineSrcBuffer.append(map.get(j).getSrc());
				}

			} else if (opcode.equals("getstatic")) {
				int slot = (int) operand_Object;
				Constant_Fieldref_info constant_Fieldref_info = (Constant_Fieldref_info) class_info
						.getConstant_pool_Map().get(slot - 1);
				// 指向 class 的 index 和 指向 nameandtype 的 index
				Constant_Class_info constant_Class_info = (Constant_Class_info) class_info.getConstant_pool_Map()
						.get(constant_Fieldref_info.getIndex() - 1);
				Constant_NameAndType_info constant_NameAndType_info = (Constant_NameAndType_info) class_info
						.getConstant_pool_Map().get(constant_Fieldref_info.getIndex2() - 1);

				// class
				Constant_Utf8_info constant_Utf8_info_class = (Constant_Utf8_info) class_info.getConstant_pool_Map()
						.get(constant_Class_info.getIndex() - 1);
				// name and type
				Constant_Utf8_info constant_Utf8_info_name = (Constant_Utf8_info) class_info.getConstant_pool_Map()
						.get(constant_NameAndType_info.getIndex() - 1);
				Constant_Utf8_info constant_Utf8_info_type = (Constant_Utf8_info) class_info.getConstant_pool_Map()
						.get(constant_NameAndType_info.getIndex2() - 1);

				String className = constant_Utf8_info_class.getBytes();
				String invoke_field_name = constant_Utf8_info_name.getBytes();
				String invoke_field_type = constant_Utf8_info_type.getBytes();

				// 既然是 load 到栈，那么一定有用，放到有用 map 中。
				SrcAndType srcAndType = new SrcAndType();
				srcAndType.setType(invoke_field_type);
				srcAndType.setSrc(className.substring(className.lastIndexOf("/") + 1) + "." + invoke_field_name);
				srcAndType.setTime("old");
				srcAndType.setFlag(true);
				map.put(map.size() + 1, srcAndType);

			} else if (opcode.equals("pop")) {
				continue;
			} else if (opcode.equals("invokespecial")) { // 执行构造方法
				int operand = (int) operand_Object;

				onelineSrcBuffer = new StringBuffer();
				Constant_X_info constant_X_info = class_info.getConstant_pool_Map().get(operand - 1);
				Constant_Methodref_info specialmethod = (Constant_Methodref_info) constant_X_info;
				// 指向 class 的 index 和 指向 nameandtype 的 index
				Constant_Class_info constant_Class_info = (Constant_Class_info) class_info.getConstant_pool_Map()
						.get(specialmethod.getIndex() - 1);
				Constant_NameAndType_info constant_NameAndType_info = (Constant_NameAndType_info) class_info
						.getConstant_pool_Map().get(specialmethod.getIndex2() - 1);

				// class
				Constant_Utf8_info constant_Utf8_info_class = (Constant_Utf8_info) class_info.getConstant_pool_Map()
						.get(constant_Class_info.getIndex() - 1);
				// name and type
				Constant_Utf8_info constant_Utf8_info_name = (Constant_Utf8_info) class_info.getConstant_pool_Map()
						.get(constant_NameAndType_info.getIndex() - 1);
				Constant_Utf8_info constant_Utf8_info_type = (Constant_Utf8_info) class_info.getConstant_pool_Map()
						.get(constant_NameAndType_info.getIndex2() - 1);

				String className = constant_Utf8_info_class.getBytes();

				String invoke_class_Name = className.substring(className.lastIndexOf("/") + 1);
				String invoke_method_name = constant_Utf8_info_name.getBytes();
				String invoke_type_name = constant_Utf8_info_type.getBytes();

				// 如果是调用构造方法
				if (invoke_class_Name.equals("Object") && invoke_method_name.equals("<init>")) {
					onelineSrcBuffer.append("super()");
				}else if (invoke_method_name.equals("<init>")) {
					// 未构造的map中有的时候，才开始构造。
					if (notConstructedMap.size() > 0) {
						// notConstructedMap中取出最后一个，并移除。
						SrcAndType srcAndType = notConstructedMap.get(notConstructedMap.size());
						notConstructedMap.remove(notConstructedMap.size());

						String tempSrc = srcAndType.getSrc();
						// 当前调用的构造类 跟从notConstructedMap所取出的未构造的一致，才进行构造
						if (invoke_class_Name.contains(tempSrc)) {
							// 拼接 new 代码（注意：同一行代码，后构造的拼接到前边）
							onelineSrcBuffer.insert(0, "new " + tempSrc + "(");

							// 存放待移除的参数
							List<Integer> waitRemoveKey = new ArrayList<>();

							// 正向遍历 map，遇到第一个src 匹配的，设置为 true，以后即开始拼接参数。
							int position = 0;
							for (int j = map.size(); j >= 1; j--) {
								if (map.get(j).getSrc().equals(tempSrc)) {
									position = j;
									break;
								}
							}
							// 从第 position+1 个开始遍历 map，拼接构造的参数。
							for (int j = position + 1; j <= map.size(); j++) {
								SrcAndType map_allTempObject = map.get(j);

								// 添加到等待移除的List
								waitRemoveKey.add(j);

								// 拼接参数
								onelineSrcBuffer.append(map_allTempObject.getSrc() + ",");
							}
							// 移除所有用完的参数
							for (Integer integer : waitRemoveKey) {
								map.remove(integer);
							}

							// 判断是否有参数，如果有，去掉最后一个逗号
							if (onelineSrcBuffer.charAt(onelineSrcBuffer.length() - 1) == ',') {
								onelineSrcBuffer.deleteCharAt(onelineSrcBuffer.length() - 1);
							}

							onelineSrcBuffer.append(")");

							// 现在 map 中，最后一个即为所构造的类，后边的都已经移除 设置为已经构造。
							map.get(map.size()).setFlag(true);
							map.get(map.size()).setSrc(onelineSrcBuffer.toString());

							// 将已经构造完的，放入构造完毕map;
							alreadyConstructedMap.put(alreadyConstructedMap.size() + 1, map.get(map.size()));
						}
					}
				} else {
					// 调用私有方法
					invokevirtual_Handler(onelineSrcBuffer, map, invoke_method_name,invoke_type_name);
				}
			} else if (opcode.equals("invokevirtual")) {
				int operand = (int) operand_Object;

				onelineSrcBuffer = new StringBuffer();
				Constant_Methodref_info virtualmethod = (Constant_Methodref_info) class_info.getConstant_pool_Map()
						.get(operand - 1);
				// 指向 class 的 index 和 指向 nameandtype 的 index
				Constant_Class_info constant_Class_info = (Constant_Class_info) class_info.getConstant_pool_Map()
						.get(virtualmethod.getIndex() - 1);
				Constant_NameAndType_info constant_NameAndType_info = (Constant_NameAndType_info) class_info
						.getConstant_pool_Map().get(virtualmethod.getIndex2() - 1);

				// class
				Constant_Utf8_info constant_Utf8_info_class = (Constant_Utf8_info) class_info.getConstant_pool_Map()
						.get(constant_Class_info.getIndex() - 1);
				// name and type
				Constant_Utf8_info constant_Utf8_info_name = (Constant_Utf8_info) class_info.getConstant_pool_Map()
						.get(constant_NameAndType_info.getIndex() - 1);
				Constant_Utf8_info constant_Utf8_info_type = (Constant_Utf8_info) class_info.getConstant_pool_Map()
						.get(constant_NameAndType_info.getIndex2() - 1);

				String className = constant_Utf8_info_class.getBytes();
				String invoke_method_name = constant_Utf8_info_name.getBytes();
				String invoke_type_name = constant_Utf8_info_type.getBytes();

				// 调用公共方法
				invokevirtual_Handler(onelineSrcBuffer, map, invoke_method_name, invoke_type_name);

			} else if (opcode.equals("invokeinterface")) {
				String operand = (String) operand_Object;
				// invokeinterface 0016 02 00
				int operand_front = Hex.hex2Integer(operand.substring(0, 4));

				Constant_X_info constant_X_info = class_info.getConstant_pool_Map().get(operand_front - 1);
				Constant_InterfaceMethodref_info interfacemethodref = (Constant_InterfaceMethodref_info) constant_X_info;
				// 指向 class 的 index 和 指向 nameandtype 的 index
				Constant_Class_info constant_Class_info = (Constant_Class_info) class_info.getConstant_pool_Map()
						.get(interfacemethodref.getIndex() - 1);
				System.out.println(constant_Class_info);
				Constant_NameAndType_info constant_NameAndType_info = (Constant_NameAndType_info) class_info
						.getConstant_pool_Map().get(interfacemethodref.getIndex2() - 1);

				// class
				Constant_Utf8_info constant_Utf8_info_class = (Constant_Utf8_info) class_info.getConstant_pool_Map()
						.get(constant_Class_info.getIndex() - 1);
				// name and type
				Constant_Utf8_info constant_Utf8_info_name = (Constant_Utf8_info) class_info.getConstant_pool_Map()
						.get(constant_NameAndType_info.getIndex() - 1);
				Constant_Utf8_info constant_Utf8_info_type = (Constant_Utf8_info) class_info.getConstant_pool_Map()
						.get(constant_NameAndType_info.getIndex2() - 1);

				String classQulifiedName = constant_Utf8_info_class.getBytes();
				String invoke_method_name = constant_Utf8_info_name.getBytes();
				String invoke_type_name = constant_Utf8_info_type.getBytes();

				invokevirtual_Handler(onelineSrcBuffer, map, invoke_method_name, invoke_type_name);
			} else if (opcode.equals("invokestatic")) {

				int operand = (int) operand_Object;

				onelineSrcBuffer = new StringBuffer();
				Constant_Methodref_info virtualmethod = (Constant_Methodref_info) class_info.getConstant_pool_Map()
						.get(operand - 1);
				// 指向 class 的 index 和 指向 nameandtype 的 index
				Constant_Class_info constant_Class_info = (Constant_Class_info) class_info.getConstant_pool_Map()
						.get(virtualmethod.getIndex() - 1);
				Constant_NameAndType_info constant_NameAndType_info = (Constant_NameAndType_info) class_info
						.getConstant_pool_Map().get(virtualmethod.getIndex2() - 1);

				// class
				Constant_Utf8_info constant_Utf8_info_class = (Constant_Utf8_info) class_info.getConstant_pool_Map()
						.get(constant_Class_info.getIndex() - 1);
				// name and type
				Constant_Utf8_info constant_Utf8_info_name = (Constant_Utf8_info) class_info.getConstant_pool_Map()
						.get(constant_NameAndType_info.getIndex() - 1);
				Constant_Utf8_info constant_Utf8_info_type = (Constant_Utf8_info) class_info.getConstant_pool_Map()
						.get(constant_NameAndType_info.getIndex2() - 1);

				String className = constant_Utf8_info_class.getBytes();
				String invoke_method_name = constant_Utf8_info_name.getBytes();
				String invoke_type_name = constant_Utf8_info_type.getBytes();

				// 取出参数 map 的第一个，拼接 属性.方法名称(
				onelineSrcBuffer
						.append(className.substring(className.lastIndexOf("/") + 1) + "." + invoke_method_name + "(");

				List<Integer> waitRemoveKey = new ArrayList<>();
				if (map.size() > 1) {
					for (int j = 2; j <= map.size(); j++) {
						SrcAndType srcAndType = map.get(j);
						onelineSrcBuffer.append(srcAndType.getSrc() + ",");
						waitRemoveKey.add(j);
					}
					for (Integer integer : waitRemoveKey) {
						map.remove(integer);
					}
				} else {
					SrcAndType srcAndType = map.get(1);
					onelineSrcBuffer.append(srcAndType.getSrc() + ",");
				}

				// 判断是否有参数，如果有，去掉最后一个逗号
				if (onelineSrcBuffer.charAt(onelineSrcBuffer.length() - 1) == ',') {
					onelineSrcBuffer.deleteCharAt(onelineSrcBuffer.length() - 1);
				}
				onelineSrcBuffer.append(")");

				SrcAndType srcAndType = new SrcAndType();
				srcAndType.setSrc(onelineSrcBuffer.toString());
				srcAndType.setFlag(true);
				srcAndType.setTime("old");
				String returntypeTemp = invoke_type_name.substring(invoke_type_name.lastIndexOf(")"),
						invoke_type_name.length() - 1);
				String returntype = ParamsConvertor.paramsConvertorMethodReturnType(returntypeTemp);
				srcAndType.setType(returntype);

				map.put(map.size() + 1, srcAndType);
			}

		}
		return onelineSrcBuffer;
	}

	/**
	 * 调用虚方法 和 私有方法 所调用的方法
	 * 
	 * @param onelineSrcBuffer
	 * @param map
	 * @param invoke_method_name
	 */
	public static void invokevirtual_Handler(StringBuffer onelineSrcBuffer, Map<Integer, SrcAndType> map,
			String invoke_method_name,String invoke_type_name) {
		// 取出参数 map 的第一个，拼接 属性.方法名称(
		onelineSrcBuffer.append(map.get(1).getSrc() + "." + invoke_method_name + "(");

		List<Integer> waitRemoveKey = new ArrayList<>();
		for (int j = 2; j <= map.size(); j++) {
			SrcAndType srcAndType = map.get(j);
			onelineSrcBuffer.append(srcAndType.getSrc() + ",");
			waitRemoveKey.add(j);
		}
		for (Integer integer : waitRemoveKey) {
			map.remove(integer);
		}

		// 判断是否有参数，如果有，去掉最后一个逗号
		if (onelineSrcBuffer.charAt(onelineSrcBuffer.length() - 1) == ',') {
			onelineSrcBuffer.deleteCharAt(onelineSrcBuffer.length() - 1);
		}
		onelineSrcBuffer.append(")");
		
		SrcAndType srcAndType = new SrcAndType();
		srcAndType.setSrc(onelineSrcBuffer.toString());
		srcAndType.setFlag(true);
		srcAndType.setTime("old");
		String returntypeTemp = invoke_type_name.substring(invoke_type_name.lastIndexOf(")"),
				invoke_type_name.length() - 1);
		String returntype = ParamsConvertor.paramsConvertorMethodReturnType(returntypeTemp);
		srcAndType.setType(returntype);

		map.put(map.size() + 1, srcAndType);
		
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
	private static String jointMethodReturnNameParams(String method_name, String descriptor,
			Map<Integer, FieldNameAndType> variableNameMap) {

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
			FieldNameAndType fieldNameAndType = variableNameMap.get(i);
			sBuffer.append(string + " " + fieldNameAndType.getName() + ", ");
		}
		String result = sBuffer.toString();

		return returnFinalString + " " + method_name + "(" + result.substring(0, result.length() - 2) + ")";
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

	public static Integer judgeNullLine(TreeMap<Integer, Integer> lineMap, Integer lineNumber, Integer opcodelineTemp,
			int o) {
		if (lineNumber.intValue() > lineMap.lastKey().intValue()) {
			return null;
		}

		opcodelineTemp = lineMap.get(lineNumber + o);
		if (opcodelineTemp == null) {
			o = o + 1;
			return judgeNullLine(lineMap, lineNumber, opcodelineTemp, o);
		} else {
			return opcodelineTemp;
		}
	}

}
