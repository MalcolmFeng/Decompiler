package com.xuanjie.app;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.xuanjie.bean.Class_info;
import com.xuanjie.bean.ExceptionTable;
import com.xuanjie.bean.Fields_info;
import com.xuanjie.bean.Methods_info;
import com.xuanjie.bean.OpcodeAndOperand;
import com.xuanjie.bean.attribute.Attribute_Code_info;
import com.xuanjie.bean.attribute.Attribute_ConstantValue_info;
import com.xuanjie.bean.attribute.Attribute_Deprecated_info;
import com.xuanjie.bean.attribute.Attribute_Exceptions_info;
import com.xuanjie.bean.attribute.Attribute_InnerClasses_info;
import com.xuanjie.bean.attribute.Attribute_LineNumberTable_info;
import com.xuanjie.bean.attribute.Attribute_LocalVariableTable_info;
import com.xuanjie.bean.attribute.Attribute_Signature_info;
import com.xuanjie.bean.attribute.Attribute_SourceFile_info;
import com.xuanjie.bean.attribute.Attribute_Synthetic_info;
import com.xuanjie.bean.attribute.Attribute_info;
import com.xuanjie.bean.attribute.Inner_classes_info;
import com.xuanjie.bean.attribute.Line_number_info;
import com.xuanjie.bean.attribute.Local_variable_info;
import com.xuanjie.bean.constant.Constant_Class_info;
import com.xuanjie.bean.constant.Constant_Double_info;
import com.xuanjie.bean.constant.Constant_Fieldref_info;
import com.xuanjie.bean.constant.Constant_Float_info;
import com.xuanjie.bean.constant.Constant_Integer_info;
import com.xuanjie.bean.constant.Constant_InterfaceMethodref_info;
import com.xuanjie.bean.constant.Constant_InvokeDynamic_info;
import com.xuanjie.bean.constant.Constant_Long_info;
import com.xuanjie.bean.constant.Constant_MethodHandle_info;
import com.xuanjie.bean.constant.Constant_MethodType_info;
import com.xuanjie.bean.constant.Constant_Methodref_info;
import com.xuanjie.bean.constant.Constant_NameAndType_info;
import com.xuanjie.bean.constant.Constant_String_info;
import com.xuanjie.bean.constant.Constant_Utf8_info;
import com.xuanjie.bean.constant.Constant_X_info;
import com.xuanjie.core.CodeConvertor;
import com.xuanjie.core.DataHandler;
import com.xuanjie.core.FileCreator;
import com.xuanjie.core.OperandBytesJudge;
import com.xuanjie.core.SrcCreator;
import com.xuanjie.utils.Hex;

public class App {

	private static String fileName = "Test";

	private static int len = 0;
	private static int start_pointer = 0;
	private static String hexString = ""; // 十六进制数据总串

	private static Class<Class_info> clz = null; // 存放所有数据的 class_info 表Class 对象
	private static Class_info class_info = null; // 存放所有数据的 class_info 表

	/**
	 * 初始化所有静态字段
	 */
	static {
		try {
			byte[] data = getClassData("/Users/Malcolm/Documents/code/java/work/classparser/bin/com/xuanjie/app", fileName);
			hexString = Hex.byte2HexStr(data);

			clz = Class_info.class;
			class_info = (Class_info) clz.newInstance();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) throws Exception {

		// 1.魔数
		setData(8, "setMagic", true, new DataHandler() {

			@Override
			public Object handle(String cutStr) {

				return cutStr;
			}
		}, String.class);

		// 2.jvm 次版本
		setData(4, "setMinor_version", true, new DataHandler() {

			@Override
			public Object handle(String cutStr) {

				return Integer.parseInt(cutStr, 16) + "";
			}
		}, String.class);

		// 3.jvm 主版本
		setData(4, "setMajor_version", true, new DataHandler() {

			@Override
			public Object handle(String cutStr) {

				return Integer.parseInt(cutStr, 16) + "";
			}
		}, String.class);

		// 4.常量池个数
		String cp_count_hexstr = cutString(4);
		int cp_count = Hex.hex2Integer(cp_count_hexstr);
		class_info.setCp_count(cp_count);

		// 5.常量池
		Map<Integer, Constant_X_info> constant_pool_Map = new HashMap<Integer, Constant_X_info>();
		for (int i = 0; i < cp_count - 1; i++) {
			constant_Pool_Map_Fill(constant_pool_Map, i);
		}
		class_info.setConstant_pool_Map(constant_pool_Map);

		for (int i = 0; i < cp_count - 1; i++) {
			String type = class_info.getConstant_pool_Map().get(i).getConstant_pool_info_Type();
			if (type.equals("Constant_Utf8_info")) {
				Constant_Utf8_info aa = (Constant_Utf8_info) class_info.getConstant_pool_Map().get(i);
				System.out.println(i + "  " + aa.getBytes());
			}
			if (type.equals("Constant_Class_info")) {
				Constant_Class_info aa = (Constant_Class_info) class_info.getConstant_pool_Map().get(i);
				System.out.println(i + "  " + aa.getIndex());
			}
			if (type.equals("Constant_String_info")) {
				Constant_String_info aa = (Constant_String_info) class_info.getConstant_pool_Map().get(i);
				System.out.println(i + "  " + aa.getIndex());
			}

			if (type.equals("Constant_Fieldref_info")) {
				Constant_Fieldref_info aa = (Constant_Fieldref_info) class_info.getConstant_pool_Map().get(i);
				System.out.println(i + "  " + aa.getIndex() + " " + aa.getIndex2());
			}
			if (type.equals("Constant_Methodref_info")) {
				Constant_Methodref_info aa = (Constant_Methodref_info) class_info.getConstant_pool_Map().get(i);
				System.out.println(i + "  " + aa.getIndex() + " " + aa.getIndex2());
			}
			if (type.equals("Constant_IntegerfaceMethodref_info")) {
				Constant_InterfaceMethodref_info aa = (Constant_InterfaceMethodref_info) class_info
						.getConstant_pool_Map().get(i);
				System.out.println(i + "  " + aa.getIndex() + " " + aa.getIndex2());
			}
			if (type.equals("Constant_NameAndType_info")) {
				Constant_NameAndType_info aa = (Constant_NameAndType_info) class_info.getConstant_pool_Map().get(i);
				System.out.println(i + "  " + aa.getIndex() + " " + aa.getIndex2());
			}

			if (type.equals("Constant_MethodHandle_info")) {
				Constant_MethodHandle_info aa = (Constant_MethodHandle_info) class_info.getConstant_pool_Map().get(i);
				System.out.println(i + "  " + aa.getReference_kind() + " " + aa.getReference_index());
			}

			if (type.equals("Constant_MethodType_info")) {
				Constant_MethodType_info aa = (Constant_MethodType_info) class_info.getConstant_pool_Map().get(i);
				System.out.println(i + "  " + aa.getDescriptor_index());
			}

			if (type.equals("Constant_InvokeDynamic_info")) {
				Constant_InvokeDynamic_info aa = (Constant_InvokeDynamic_info) class_info.getConstant_pool_Map().get(i);
				System.out.println(i + "  " + aa.getName_and_type_index() + " " + aa.getBootstrap_method_attr_index());
			}
		}

		// 6.访问标识
		setData(4, "setAccess_flag", true, new DataHandler() {

			@Override
			public Object handle(String cutStr) {

				return cutStr;
			}
		}, String.class);

		// 7.本类索引
		setData(4, "setThis_class_index", true, new DataHandler() {

			@Override
			public Object handle(String cutStr) {
				return Hex.hex2Integer(cutStr);
			}
		}, int.class);

		// 8.父类索引
		setData(4, "setSuper_class_index", true, new DataHandler() {

			@Override
			public Object handle(String cutStr) {
				return Hex.hex2Integer(cutStr);
			}
		}, int.class);

		// 9.接口索引集合大小
		String interfaces_count_hexString = cutString(4);
		int interfaces_count = Hex.hex2Integer(interfaces_count_hexString);
		class_info.setInterfaces_count(interfaces_count);

		// 10.接口索引集合
		List<Integer> interfaceList = new ArrayList<>();
		for (int j = 0; j < interfaces_count; j++) {
			String hexStringInterface = cutString(4);
			interfaceList.add(Hex.hex2Integer(hexStringInterface));
		}
		class_info.setInterfacesList(interfaceList);

		// 11.字段表集合大小
		setData(4, "setFields_count", true, new DataHandler() {

			@Override
			public Object handle(String cutStr) {
				return Hex.hex2Integer(cutStr);
			}
		}, int.class);

		// 12.字段表集合
		List<Fields_info> fields_info_List = new ArrayList<>();
		for (int i = 0; i < class_info.getFields_count(); i++) {

			String access_flag_hexString = cutString(4);
			String name_index_hexString = cutString(4);
			String descriptor_index_hexString = cutString(4);
			String attributes_count_hexString = cutString(4);

			Fields_info fields_info = new Fields_info();
			fields_info.setAccess_flag(access_flag_hexString);
			fields_info.setName_index(Hex.hex2Integer(name_index_hexString));
			fields_info.setDescriptor_index(Hex.hex2Integer(descriptor_index_hexString));
			fields_info.setAttributes_count(Hex.hex2Integer(attributes_count_hexString));

			List<Attribute_info> attributes_list = new ArrayList<>();
			for (int j = 0; j < Hex.hex2Integer(attributes_count_hexString); j++) {
				attributes_info_List_Fill(attributes_list);
			}
			fields_info.setAttributes_list(attributes_list); // 2 将属性表集合 放入 字段表

			fields_info_List.add(fields_info); // 3 将字段表 放入字段表集合
		}
		class_info.setFields_info_List(fields_info_List); // 4 将字段表集合 放入 class_info 表

		// 13.方法表集合大小
		setData(4, "setMethods_count", true, new DataHandler() {
			@Override
			public Object handle(String cutStr) {
				return Hex.hex2Integer(cutStr);
			}
		}, int.class);

		// 14.方法表集合
		List<Methods_info> methods_info_List = new ArrayList<>();
		for (int i = 0; i < class_info.getMethods_count(); i++) {

			String access_flag_hexString = cutString(4);
			String name_index_hexString = cutString(4);
			String descriptor_index_hexString = cutString(4);
			String attributes_count_hexString = cutString(4);

			Methods_info methods_info = new Methods_info();
			methods_info.setAccess_flag(access_flag_hexString);
			methods_info.setName_index(Hex.hex2Integer(name_index_hexString));
			methods_info.setDescriptor_index(Hex.hex2Integer(descriptor_index_hexString));
			methods_info.setAttributes_count(Hex.hex2Integer(attributes_count_hexString));

			List<Attribute_info> attributes_list = new ArrayList<>();
			for (int j = 0; j < Hex.hex2Integer(attributes_count_hexString); j++) {
				attributes_info_List_Fill(attributes_list);
			}
			methods_info.setAttributes_list(attributes_list);

			methods_info_List.add(methods_info);
		}
		class_info.setMethods_info_List(methods_info_List);

		// 15.最后，存放其他的 属性表集合大小
		setData(4, "setAttributes_count", true, new DataHandler() {

			@Override
			public Object handle(String cutStr) {

				return Hex.hex2Integer(cutStr);
			}
		}, int.class);

		// 16.最后，存放其他的 属性表集合
		List<Attribute_info> attributes_list = new ArrayList<>();
		for (int j = 0; j < class_info.getAttributes_count(); j++) {
			attributes_info_List_Fill(attributes_list);
		}
		class_info.setAttributes(attributes_list);

		// 以下两句 用于校验
		// System.out.println("所有字节码十六进制字符串的长度" + hexString.length());
		// System.out.println("当前指针的位置" + start_pointer);

		String src = SrcCreator.createJavaFileSrc(class_info);
		System.out.println(src);

		 FileCreator.createFile(fileName, src);

	}

	public static void constant_Pool_Map_Fill(Map<Integer, Constant_X_info> constant_pool_Map, int i) {
		// 切分并初始化指针
		String tagHexString = cutString(2);

		int tag = Hex.hex2Integer(tagHexString);

		if (tag == 1) {
			Constant_Utf8_info constant_Utf8_info = new Constant_Utf8_info();

			String lengthHexString = cutString(Constant_Utf8_info.length_length * 2);
			int length = Hex.hex2Integer(lengthHexString);
			String bytes = cutString(length * Constant_Utf8_info.bytes_length * 2);
			String bytesString = Hex.hexStr2Str(bytes);

			constant_Utf8_info.setConstant_pool_info_Type("Constant_Utf8_info");
			constant_Utf8_info.setTag(tag + "");
			constant_Utf8_info.setLength(length);
			constant_Utf8_info.setBytes(bytesString);

			constant_pool_Map.put(i, constant_Utf8_info);
		} else if (tag == 3) {
			Constant_Integer_info constant_Integer_info = new Constant_Integer_info();

			String bytes = cutString(Constant_Integer_info.bytes_length * 2);
			String bytesString = Hex.hexStr2Str(bytes);

			constant_Integer_info.setConstant_pool_info_Type("Constant_Integer_info");
			constant_Integer_info.setTag(tag + "");
			constant_Integer_info.setBytes(bytesString);

			constant_pool_Map.put(i, constant_Integer_info);
		} else if (tag == 4) {
			Constant_Float_info constant_Float_info = new Constant_Float_info();

			String bytes = cutString(Constant_Float_info.bytes_length * 2);
			String bytesString = Hex.hexStr2Str(bytes);

			constant_Float_info.setConstant_pool_info_Type("Constant_Float_info");
			constant_Float_info.setTag(tag + "");
			constant_Float_info.setBytes(bytesString);

			constant_pool_Map.put(i, constant_Float_info);
		} else if (tag == 5) {
			Constant_Long_info constant_Long_info = new Constant_Long_info();

			String bytes = cutString(Constant_Long_info.bytes_length * 2);
			String bytesString = Hex.hexStr2Str(bytes);

			constant_Long_info.setConstant_pool_info_Type("Constant_Long_info");
			constant_Long_info.setTag(tag + "");
			constant_Long_info.setBytes(bytesString);

			constant_pool_Map.put(i, constant_Long_info);
		} else if (tag == 6) {
			Constant_Double_info constant_Double_info = new Constant_Double_info();

			String bytes = cutString(Constant_Double_info.bytes_length * 2);
			String bytesString = Hex.hexStr2Str(bytes);

			constant_Double_info.setConstant_pool_info_Type("Constant_Double_info");
			constant_Double_info.setTag(tag + "");
			constant_Double_info.setBytes(bytesString);

			constant_pool_Map.put(i, constant_Double_info);
		} else if (tag == 7) {
			Constant_Class_info constant_Class_info = new Constant_Class_info();

			String indexHexString = cutString(Constant_Class_info.index_length * 2);
			int indexString = Hex.hex2Integer(indexHexString);

			constant_Class_info.setConstant_pool_info_Type("Constant_Class_info");
			constant_Class_info.setTag(tag + "");
			constant_Class_info.setIndex(indexString);

			constant_pool_Map.put(i, constant_Class_info);
		} else if (tag == 8) {
			Constant_String_info constant_String_info = new Constant_String_info();

			String indexHexString = cutString(Constant_String_info.index_length * 2);
			int indexString = Hex.hex2Integer(indexHexString);

			constant_String_info.setConstant_pool_info_Type("Constant_String_info");
			constant_String_info.setTag(tag + "");
			constant_String_info.setIndex(indexString);

			constant_pool_Map.put(i, constant_String_info);
		} else if (tag == 9) {
			Constant_Fieldref_info constant_Fieldref_info = new Constant_Fieldref_info();

			String index1HexString = cutString(Constant_Fieldref_info.index_length * 2);
			int index1String = Hex.hex2Integer(index1HexString);
			String index2HexString = cutString(Constant_Fieldref_info.index2_length * 2);
			int index2String = Hex.hex2Integer(index2HexString);

			constant_Fieldref_info.setConstant_pool_info_Type("Constant_Fieldref_info");
			constant_Fieldref_info.setTag(tag + "");
			constant_Fieldref_info.setIndex(index1String);
			constant_Fieldref_info.setIndex2(index2String);

			constant_pool_Map.put(i, constant_Fieldref_info);
		} else if (tag == 10) {
			Constant_Methodref_info constant_Methodref_info = new Constant_Methodref_info();

			String index1HexString = cutString(Constant_Methodref_info.index_length * 2);
			int index1String = Hex.hex2Integer(index1HexString);
			String index2HexString = cutString(Constant_Methodref_info.index2_length * 2);
			int index2String = Hex.hex2Integer(index2HexString);

			constant_Methodref_info.setConstant_pool_info_Type("Constant_Methodref_info");
			constant_Methodref_info.setTag(tag + "");
			constant_Methodref_info.setIndex(index1String);
			constant_Methodref_info.setIndex2(index2String);

			constant_pool_Map.put(i, constant_Methodref_info);
		} else if (tag == 11) {
			Constant_InterfaceMethodref_info constant_IntegerfaceMethodref_info = new Constant_InterfaceMethodref_info();

			String index1HexString = cutString(Constant_InterfaceMethodref_info.index_length * 2);
			int index1String = Hex.hex2Integer(index1HexString);
			String index2HexString = cutString(Constant_InterfaceMethodref_info.index2_length * 2);
			int index2String = Hex.hex2Integer(index2HexString);

			constant_IntegerfaceMethodref_info.setConstant_pool_info_Type("Constant_IntegerfaceMethodref_info");
			constant_IntegerfaceMethodref_info.setTag(tag + "");
			constant_IntegerfaceMethodref_info.setIndex(index1String);
			constant_IntegerfaceMethodref_info.setIndex2(index2String);

			constant_pool_Map.put(i, constant_IntegerfaceMethodref_info);
		} else if (tag == 12) {
			Constant_NameAndType_info constant_NameAndType_info = new Constant_NameAndType_info();

			String index1HexString = cutString(Constant_NameAndType_info.index_length * 2);
			int index1String = Hex.hex2Integer(index1HexString);
			String index2HexString = cutString(Constant_NameAndType_info.index2_length * 2);
			int index2String = Hex.hex2Integer(index2HexString);

			constant_NameAndType_info.setConstant_pool_info_Type("Constant_NameAndType_info");
			constant_NameAndType_info.setTag(tag + "");
			constant_NameAndType_info.setIndex(index1String);
			constant_NameAndType_info.setIndex2(index2String);

			constant_pool_Map.put(i, constant_NameAndType_info);
		} else if (tag == 15) {
			Constant_MethodHandle_info constant_MethodHandle_info = new Constant_MethodHandle_info();

			String reference_kindHexString = cutString(Constant_MethodHandle_info.reference_kind_length * 2);
			int reference_kindString = Hex.hex2Integer(reference_kindHexString);
			String reference_indexHexString = cutString(Constant_MethodHandle_info.reference_index_length * 2);
			int reference_indexString = Hex.hex2Integer(reference_indexHexString);

			constant_MethodHandle_info.setConstant_pool_info_Type("Constant_MethodHandle_info");
			constant_MethodHandle_info.setTag(tag + "");
			constant_MethodHandle_info.setReference_kind(reference_kindString);
			constant_MethodHandle_info.setReference_index(reference_indexString);

			constant_pool_Map.put(i, constant_MethodHandle_info);
		} else if (tag == 16) {
			Constant_MethodType_info constant_MethodType_info = new Constant_MethodType_info();

			String descriptor_indexHexString = cutString(Constant_MethodType_info.descriptor_index_length * 2);
			int descriptor_indexString = Hex.hex2Integer(descriptor_indexHexString);

			constant_MethodType_info.setConstant_pool_info_Type("Constant_MethodType_info");
			constant_MethodType_info.setTag(tag + "");
			constant_MethodType_info.setDescriptor_index(descriptor_indexString);

			constant_pool_Map.put(i, constant_MethodType_info);
		} else if (tag == 18) {
			Constant_InvokeDynamic_info constant_InvokeDynamic_info = new Constant_InvokeDynamic_info();

			String bootstrap_method_attr_index_HexString = cutString(
					Constant_InvokeDynamic_info.bootstrap_method_attr_index_length * 2);
			int bootstrap_method_attr_index_String = Hex.hex2Integer(bootstrap_method_attr_index_HexString);
			String name_and_type_index_HexString = cutString(
					Constant_InvokeDynamic_info.name_and_type_index_length * 2);
			int name_and_type_index_String = Hex.hex2Integer(name_and_type_index_HexString);

			constant_InvokeDynamic_info.setConstant_pool_info_Type("Constant_InvokeDynamic_info");
			constant_InvokeDynamic_info.setTag(tag + "");
			constant_InvokeDynamic_info.setBootstrap_method_attr_index(bootstrap_method_attr_index_String);
			constant_InvokeDynamic_info.setName_and_type_index(name_and_type_index_String);

			constant_pool_Map.put(i, constant_InvokeDynamic_info);
		}
	}

	public static void attributes_info_List_Fill(List<Attribute_info> attributes_list) {
		String attribute_name_index_hexString = cutString(4);
		String attribute_length_hexString = cutString(8);

		int nameIndex = Hex.hex2Integer(attribute_name_index_hexString);
		Constant_Utf8_info typeBean = (Constant_Utf8_info) class_info.getConstant_pool_Map().get(nameIndex - 1);
		String type = typeBean.getBytes();
		int length = Hex.hex2Integer(attribute_length_hexString);

		if (type.equals("Code")) {
			String max_statck_hexString = cutString(4);
			String max_locals_hexString = cutString(4);
			String code_length_hexString = cutString(8);

			Attribute_Code_info attribute_Code_info = new Attribute_Code_info();
			attribute_Code_info.setAttribute_type("Code");
			attribute_Code_info.setAttribute_name_index(nameIndex);
			attribute_Code_info.setAttrbute_length(length);
			attribute_Code_info.setMax_statck(Hex.hex2Integer(max_statck_hexString));
			attribute_Code_info.setMax_locals(Hex.hex2Integer(max_locals_hexString));
			attribute_Code_info.setCode_length(Hex.hex2Integer(code_length_hexString));
			// 开始存放 codeMap
			Map<Integer, OpcodeAndOperand> codeMap = new HashMap<>();
			for (int k = 0; k < Hex.hex2Integer(code_length_hexString); k++) {
				OpcodeAndOperand opcodeAndOperand = new OpcodeAndOperand();
				String code_everyone_hexString = cutString(2);
				String code = CodeConvertor.codeConvertor(code_everyone_hexString.toLowerCase());
				int i = OperandBytesJudge.operandBytesCount(code);
				String operand = "";
				int operand_int = 0;
				int tempk = k;
				if (i!=0) {    // 如果有操作数。 进行指针（行数）移动
					for (int j = 0; j < i; j++) {
						k++;
					}
					operand = cutString(i*2);
					operand_int = Hex.hex2Integer(operand);
				}
				opcodeAndOperand.setOpcode(code);
				if (code.equals("invokeinterface")) {
					opcodeAndOperand.setOperand(operand);  //存放之前的字符串形式。 用的时候再分割（ 0016 02 00），解析。
				}else {
					opcodeAndOperand.setOperand(operand_int);
				}
				codeMap.put(tempk, opcodeAndOperand);
				
			}
			attribute_Code_info.setCodeMap(codeMap);
			// 开始存放异常表大小
			String exception_table_length_hexString = cutString(4);
			attribute_Code_info.setException_table_length(Hex.hex2Integer(exception_table_length_hexString));

			// 开始存放 异常表 list
			List<ExceptionTable> exceptionTablesList = new ArrayList<>();
			for (int k = 0; k < Hex.hex2Integer(exception_table_length_hexString); k++) {
				String start_pc_hexString = cutString(4);
				String end_pc_hexString = cutString(4);
				String handler_pc_hexString = cutString(4);
				String catch_type_hexString = cutString(4);

				ExceptionTable exceptionTable = new ExceptionTable();
				exceptionTable.setStart_pc(Hex.hex2Integer(start_pc_hexString));
				exceptionTable.setEnd_pc(Hex.hex2Integer(end_pc_hexString));
				exceptionTable.setHandler_pc(Hex.hexStr2Str(handler_pc_hexString));
				exceptionTable.setCatch_type(Hex.hexStr2Str(catch_type_hexString));

				exceptionTablesList.add(exceptionTable);
			}
			attribute_Code_info.setExceptionTablesList(exceptionTablesList);

			// 开始存放 属性表 个数
			String attributes_count_hexString_inner = cutString(4);
			attribute_Code_info.setAttributes_count(Hex.hex2Integer(attributes_count_hexString_inner));

			// 开始存放 code属性的 属性表 List
			List<Attribute_info> attributes_infos_List_inner = new ArrayList<>();
			for (int k = 0; k < Hex.hex2Integer(attributes_count_hexString_inner); k++) {

				String inner_attribute_name_index_hexString = cutString(4);
				String inner_attribut_length_hexString = cutString(8);
				
				int nameIndex_inner = Hex.hex2Integer(inner_attribute_name_index_hexString);
				Constant_Utf8_info typeBean_inner = (Constant_Utf8_info) class_info.getConstant_pool_Map().get(nameIndex_inner - 1);
				String type_inner = typeBean_inner.getBytes();
				int length_inner = Hex.hex2Integer(inner_attribut_length_hexString);

				if (type_inner.equals("LineNumberTable")) {

					Attribute_LineNumberTable_info attribute_LineNumberTable_info = new Attribute_LineNumberTable_info();
					attribute_LineNumberTable_info.setAttribute_type("LineNumberTable");
					attribute_LineNumberTable_info.setAttribute_name_index(nameIndex_inner);
					attribute_LineNumberTable_info.setAttrbute_length(length_inner);

					String line_number_table_length_hexString = cutString(4);

					// 存放异常 List
					List<Line_number_info> line_number_table_List = new ArrayList<>();
					for (int j = 0; j < Hex.hex2Integer(line_number_table_length_hexString); j++) {
						String start_pc_hexString = cutString(4);
						String line_number_hexString = cutString(4);

						Line_number_info line_number_info = new Line_number_info();
						line_number_info.setStart_pc(Hex.hex2Integer(start_pc_hexString));
						line_number_info.setLine_number(Hex.hex2Integer(line_number_hexString));

						line_number_table_List.add(line_number_info);
					}
					attribute_LineNumberTable_info.setLine_number_table_List(line_number_table_List);
					
					attributes_infos_List_inner.add(attribute_LineNumberTable_info); 
				}else if (type_inner.equals("LocalVariableTable")) {
					Attribute_LocalVariableTable_info attribute_LocalVariableTable_info = new Attribute_LocalVariableTable_info();
					attribute_LocalVariableTable_info.setAttribute_type("LocalVariableTable");
					attribute_LocalVariableTable_info.setAttribute_name_index(nameIndex);
					attribute_LocalVariableTable_info.setAttrbute_length(length);
					// 存放长度
					String local_variable_table_length_hexstring = cutString(4);
					attribute_LocalVariableTable_info
							.setLocal_variable_table_length(Hex.hex2Integer(local_variable_table_length_hexstring));
					// 存放 list
					List<Local_variable_info> local_variable_table_List = new ArrayList<>();
					for (int i = 0; i < Hex.hex2Integer(local_variable_table_length_hexstring); i++) {
						String start_pc = cutString(4);
						String length_pc = cutString(4);
						String name_index = cutString(4);
						String descriptor_index = cutString(4);
						String index = cutString(4);

						Local_variable_info local_variable_info = new Local_variable_info();
						local_variable_info.setStart_pc(Hex.hex2Integer(start_pc));
						local_variable_info.setLength(Hex.hex2Integer(length_pc));
						local_variable_info.setName_index(Hex.hex2Integer(name_index));
						local_variable_info.setDescriptor_index(Hex.hex2Integer(descriptor_index));
						local_variable_info.setIndex(Hex.hex2Integer(index));

						local_variable_table_List.add(local_variable_info);
					}
					attribute_LocalVariableTable_info.setLocal_variable_table_List(local_variable_table_List);

					attributes_infos_List_inner.add(attribute_LocalVariableTable_info);
				}else {
					String info_hexString_inner = cutString(length_inner * 2);
					
					Attribute_info attributes_info_inner = new Attribute_info();
					attributes_info_inner.setAttribute_type("other");
					attributes_info_inner.setAttribute_name_index(nameIndex_inner);
					attributes_info_inner.setAttrbute_length(length_inner);
					attributes_info_inner.setInfo(info_hexString_inner);
					
					attributes_infos_List_inner.add(attributes_info_inner); 
				}
			}
			attribute_Code_info.setAttributesList(attributes_infos_List_inner);

			// 最后将 code属性表 放入属性表集合中
			attributes_list.add(attribute_Code_info); // 1 将属性表 放入属性表集合
		} else if (type.equals("Exceptions")) {

			Attribute_Exceptions_info attribute_Exceptions_info = new Attribute_Exceptions_info();
			attribute_Exceptions_info.setAttribute_type("Exceptions");
			attribute_Exceptions_info.setAttribute_name_index(nameIndex);
			attribute_Exceptions_info.setAttrbute_length(length);

			String number_of_exceptions_hexString = cutString(4);
			attribute_Exceptions_info.setNumber_of_exceptions(Hex.hex2Integer(number_of_exceptions_hexString));

			// 存放异常 List
			List<Integer> exception_index_tableList = new ArrayList<>();
			for (int k = 0; k < Hex.hex2Integer(number_of_exceptions_hexString); k++) {
				String exception_index_table_hexString = cutString(4);
				exception_index_tableList.add(Hex.hex2Integer(exception_index_table_hexString));
			}
			attribute_Exceptions_info.setException_index_tableList(exception_index_tableList);

			attributes_list.add(attribute_Exceptions_info); // 1 将属性表 放入属性表集合
		} else if (type.equals("LineNumberTable")) {

			Attribute_LineNumberTable_info attribute_LineNumberTable_info = new Attribute_LineNumberTable_info();
			attribute_LineNumberTable_info.setAttribute_type("LineNumberTable");
			attribute_LineNumberTable_info.setAttribute_name_index(nameIndex);
			attribute_LineNumberTable_info.setAttrbute_length(length);

			String line_number_table_length_hexString = cutString(4);

			// 存放异常 List
			List<Line_number_info> line_number_table_List = new ArrayList<>();
			for (int k = 0; k < Hex.hex2Integer(line_number_table_length_hexString); k++) {
				String start_pc_hexString = cutString(4);
				String line_number_hexString = cutString(4);

				Line_number_info line_number_info = new Line_number_info();
				line_number_info.setStart_pc(Hex.hex2Integer(start_pc_hexString));
				line_number_info.setLine_number(Hex.hex2Integer(line_number_hexString));

				line_number_table_List.add(line_number_info);
			}
			attribute_LineNumberTable_info.setLine_number_table_List(line_number_table_List);

			attributes_list.add(attribute_LineNumberTable_info); // 1 将属性表 放入属性表集合
		} else if (type.equals("SourceFile")) {
			Attribute_SourceFile_info sourceFile = new Attribute_SourceFile_info();
			sourceFile.setAttribute_type("SourceFile");
			sourceFile.setAttribute_name_index(nameIndex);
			sourceFile.setAttrbute_length(length);

			String sourcefile_index_hexstring = cutString(4);
			sourceFile.setSourcefile_index(Hex.hex2Integer(sourcefile_index_hexstring));

			attributes_list.add(sourceFile);
		} else if (type.equals("LocalVariableTable")) {
			Attribute_LocalVariableTable_info attribute_LocalVariableTable_info = new Attribute_LocalVariableTable_info();
			attribute_LocalVariableTable_info.setAttribute_type("LocalVariableTable");
			attribute_LocalVariableTable_info.setAttribute_name_index(nameIndex);
			attribute_LocalVariableTable_info.setAttrbute_length(length);
			// 存放长度
			String local_variable_table_length_hexstring = cutString(4);
			attribute_LocalVariableTable_info
					.setLocal_variable_table_length(Hex.hex2Integer(local_variable_table_length_hexstring));
			// 存放 list
			List<Local_variable_info> local_variable_table_List = new ArrayList<>();
			for (int i = 0; i < Hex.hex2Integer(local_variable_table_length_hexstring); i++) {
				String start_pc = cutString(4);
				String length_pc = cutString(4);
				String name_index = cutString(4);
				String descriptor_index = cutString(4);
				String index = cutString(4);

				Local_variable_info local_variable_info = new Local_variable_info();
				local_variable_info.setStart_pc(Hex.hex2Integer(start_pc));
				local_variable_info.setLength(Hex.hex2Integer(length_pc));
				local_variable_info.setName_index(Hex.hex2Integer(name_index));
				local_variable_info.setDescriptor_index(Hex.hex2Integer(descriptor_index));
				local_variable_info.setIndex(Hex.hex2Integer(index));

				local_variable_table_List.add(local_variable_info);
			}
			attribute_LocalVariableTable_info.setLocal_variable_table_List(local_variable_table_List);

			attributes_list.add(attribute_LocalVariableTable_info);
		} else if (type.equals("ConstantValue")) {
			Attribute_ConstantValue_info attribute_ConstantValue_info = new Attribute_ConstantValue_info();
			attribute_ConstantValue_info.setAttribute_type("Attribute_ConstantValue_info");
			attribute_ConstantValue_info.setAttribute_name_index(nameIndex);
			attribute_ConstantValue_info.setAttrbute_length(length);

			String constantvalue_index_hexstring = cutString(4);
			attribute_ConstantValue_info.setConstantvalue_index(Hex.hex2Integer(constantvalue_index_hexstring));

			attributes_list.add(attribute_ConstantValue_info);
		} else if (type.equals("InnerClasses")) {
			Attribute_InnerClasses_info attribute_InnerClasses_info = new Attribute_InnerClasses_info();
			attribute_InnerClasses_info.setAttribute_type("InnerClasses");
			attribute_InnerClasses_info.setAttribute_name_index(nameIndex);
			attribute_InnerClasses_info.setAttrbute_length(length);
			// 存放长度
			String number_of_classes_hexstring = cutString(4);
			attribute_InnerClasses_info.setNumber_of_classes(Hex.hex2Integer(number_of_classes_hexstring));
			// 存放 list
			List<Inner_classes_info> inner_classes_info_List = new ArrayList<>();
			for (int i = 0; i < Hex.hex2Integer(number_of_classes_hexstring); i++) {
				String inner_class_info_index = cutString(4);
				String outer_class_info_index = cutString(4);
				String inner_name_index = cutString(4);
				String inner_class_access_flags = cutString(4);

				Inner_classes_info inner_classes_info = new Inner_classes_info();
				inner_classes_info.setInner_class_info_index(Hex.hex2Integer(inner_class_info_index));
				inner_classes_info.setOuter_class_info_index(Hex.hex2Integer(outer_class_info_index));
				inner_classes_info.setInner_name_index(Hex.hex2Integer(inner_name_index));
				inner_classes_info.setInner_class_access_flags(inner_class_access_flags);

				inner_classes_info_List.add(inner_classes_info);
			}
			attribute_InnerClasses_info.setInner_classes(inner_classes_info_List);

			attributes_list.add(attribute_InnerClasses_info);
		} else if (type.equals("Deprecated")) {
			Attribute_Deprecated_info attribute_Deprecated_info = new Attribute_Deprecated_info();
			attribute_Deprecated_info.setAttribute_type("Attribute_Deprecated_info");
			attribute_Deprecated_info.setAttribute_name_index(nameIndex);
			attribute_Deprecated_info.setAttrbute_length(length);

			attributes_list.add(attribute_Deprecated_info);
		} else if (type.equals("Synthetic")) {
			Attribute_Synthetic_info attribute_Synthetic_info = new Attribute_Synthetic_info();
			attribute_Synthetic_info.setAttribute_type("Attribute_Synthetic_info");
			attribute_Synthetic_info.setAttribute_name_index(nameIndex);
			attribute_Synthetic_info.setAttrbute_length(length);

			attributes_list.add(attribute_Synthetic_info);
		} else if (type.equals("Signature")) {
			Attribute_Signature_info attribute_Signature_info = new Attribute_Signature_info();
			attribute_Signature_info.setAttribute_type("Attribute_Signature_info");
			attribute_Signature_info.setAttribute_name_index(nameIndex);
			attribute_Signature_info.setAttrbute_length(length);

			String signature_index_hexstring = cutString(4);
			attribute_Signature_info.setSignature_index(Hex.hex2Integer(signature_index_hexstring));

			attributes_list.add(attribute_Signature_info);
		} else {
			// 如果是其他的 属性表， 直接切割info，存到公共属性表。
			String info_hexString = cutString(length * 2);
			Attribute_info attributes_info = new Attribute_info();
			attributes_info.setAttribute_type("other");
			attributes_info.setAttribute_name_index(nameIndex);
			attributes_info.setAttrbute_length(length);
			attributes_info.setInfo(info_hexString);

			attributes_list.add(attributes_info);
		}
	}

	/**
	 * 切分十六进制字符串 并 初始化指针
	 * 
	 * @param len
	 *            切分长度
	 * @return
	 */
	private static String cutString(int len) {
		App.len = len;
		String cutStr = hexString.substring(start_pointer, start_pointer + len);
		// 初始化指针
		start_pointer = start_pointer + App.len;
		return cutStr;
	}

	/**
	 * 往 class_info Bean 中存放数据。
	 * 
	 * @param len
	 *            切分的长度
	 * @param method
	 *            字段 set 方法的方法名
	 * @param flag
	 *            是否执行下面的方法
	 * @param dataHandler
	 *            匿名对象
	 * @param parameterTypes
	 *            set 方法的参数类型
	 */
	private static void setData(int len, String method, Boolean flag, DataHandler dataHandler,
			Class<?>... parameterTypes) {
		// 1.初始化切分长度
		App.len = len;

		// 2.进行切分字符串
		String cutStr = hexString.substring(start_pointer, start_pointer + len);

		// 3.进行数据处理
		Object data = dataHandler.handle(cutStr);

		// 判断要不要继续执行
		if (flag) {
			// 4.将数据存入 class_info 表
			try {
				Method methodRefl = clz.getMethod(method, parameterTypes);
				methodRefl.invoke(class_info, data);
			} catch (NoSuchMethodException e) {
				e.printStackTrace();
			} catch (SecurityException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				e.printStackTrace();
			}

			// 5.为下一次切分初始化指针
			start_pointer = start_pointer + App.len;
		}

	}

	/**
	 * 获取class 文件的字节数组
	 * 
	 * @param name
	 * @return
	 */
	private static byte[] getClassData(String filepath, String name) {

		String path = filepath + "/" + name.replace('.', '/') + ".class";

		InputStream iStream = null;
		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		try {
			iStream = new FileInputStream(path);

			byte[] buffer = new byte[1024];
			int temp = 0;
			while ((temp = iStream.read(buffer)) != -1) {
				byteArrayOutputStream.write(buffer, 0, temp);
			}
			if (byteArrayOutputStream != null) {
				return byteArrayOutputStream.toByteArray();
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (iStream != null) {
					iStream.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
			try {
				if (byteArrayOutputStream != null) {
					byteArrayOutputStream.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return null;
	}

}
