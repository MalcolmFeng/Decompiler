package com.xuanjie.core;

public class OperandBytesJudge {
	/**
	 * 十六进制 和 字节码指令 转换
	 * 
	 * @param code_everyone_hexString
	 * @return
	 */
	public static int operandBytesCount(String opcode) {
		switch (opcode) {
			case "aload":
				return 1;
			case "anewarray":
				return 2;
			case "astore":
				return 1;
			case "bipush":
				return 1;
			case "checkcast":
				return 2;
			case "dload":
				return 1;
			case "dstore":
				return 1;
			case "fload":
				return 1;
			case "fstore":
				return 1;
			case "getfield":
				return 2;
			case "getstatic":
				return 2;
			case "goto":
				return 2;
			case "goto_w":
				return 4;
			case "if_acmp<cond>":
				return 2;
			case "if_icmp<cond>":
				return 2;
			case "if<cond>":
				return 2;
			case "ifnonnull":
				return 2;
			case "ifnull":
				return 2;
			case "iinc":
				return 1;
			case "iload":
				return 1;
			case "instanceof":
				return 2;
			case "invokedynamic":
				return 2;
			case "invokeinterface":
				return 4;
			case "invokespecial":
				return 2;
			case "invokestatic":
				return 2;
			case "invokevirtual":
				return 2;
			case "istore":
				return 1;
			case "jsr":
				return 2;
			case "jsr_wbranch":
				return 4;
			case "ldc":
				return 1;
			case "ldc_w":
				return 2;
			case "ldc2_w":
				return 2;
			case "lload":
				return 1;
			case "lstore":
				return 1;
			case "multianewarray":
				return 3;
			case "new":
				return 2;
			case "putfield":
				return 2;
			case "putstatic":
				return 2;
			case "ret":
				return 1;
			case "sipush":
				return 2;
		}
		return 0;
	}

}
