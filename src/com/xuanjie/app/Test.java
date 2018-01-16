package com.xuanjie.app;

import java.text.StringCharacterIterator;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;

import org.apache.commons.lang.StringUtils;

public class Test {
	
//	private List<Map<Integer, List<Map<String, String>>>> niubi;
	public Test() {
		
	}
	
	public Test(JFrame obj,JFrame jFrame) {
		
	}

	private void hello() {
		
	}
	
	public static void main(String[] args) {
		

		// new 对象
		String bbb = new String("123qwe");
		String ccc = new String();
		String string = new String("oieurtwe");
		string.compareToIgnoreCase("asdfasdf");
		new String();
		new String("123123123");
		new StringCharacterIterator("asdfasdf", 123, 321, 12);
		new JFrame(new String("123123"));
		new String(new String(new String("asdfasdfasdf")));
		
		// 方法调用
		String string2=string.concat(string);
		new JFrame(new String("asdfasdfas")).getContentPane();
		Test test = new Test(new JFrame(), new JFrame(new String("qwetrytetw")));
		Test test3 = new Test();
		
		// 调用私有方法
		test3.hello();
		
		// 调用接口方法 
		Test test1 = new Test();
		List<Test> aaa = new ArrayList<>();
		aaa.add(test);
		
		// 调用静态方法
		System.out.println("asdfasdf");
		new String(StringUtils.chomp("adsfasdfasdf"));
	}

}