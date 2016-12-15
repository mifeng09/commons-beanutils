package com.cloume.commons.beanutils;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang3.StringUtils;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Unit test for simple App.
 */
public class AppTest extends TestCase {
	class InnerClass {
		String someValue;
	}

	class ObjectToUpdate {
		int intValue;
		long longValue;
		String stringValue;
		Object[] arrayValue;
		InnerClass classValue;
	}

	/**
	 * Create the test case
	 *
	 * @param testName
	 *            name of the test case
	 */
	public AppTest(String testName) {
		super(testName);
	}

	/**
	 * @return the suite of tests being tested
	 */
	public static Test suite() {
		return new TestSuite(AppTest.class);
	}

	/**
	 * Rigourous Test :-)
	 */
	public void testUpdater() {
		InnerClass iclass = new InnerClass();
		Map<String, Object> from = new HashMap<String, Object>();
		from.put("intValue", 100);
		from.put("longValue", 200L);
		from.put("stringValue", "this is string");
		from.put("classValue", iclass);

		ObjectToUpdate updated = Updater.wrap(new ObjectToUpdate()).update(from);
		assertNull(updated.arrayValue);
		assertEquals(100, updated.intValue);
		assertEquals(200L, updated.longValue);
		assertEquals("this is string", updated.stringValue);
		assertEquals(iclass, updated.classValue);
	}

	/**
	 * 有些field不能直接set,需要转换,比如数据类型转换,结构转换等
	 */
	public void testUpdaterWithConverter() {
		Map<String, Object> from = new HashMap<String, Object>();
		from.put("intValue", "100"); /// string to int
		from.put("longValue2", 200L); /// different key
		from.put("stringValue2", "this is string");
		from.put("arrayValue", "this,is,an,array"); /// string to array

		Map<String, String> inner = new HashMap<String, String>();
		inner.put("someValue", "hello,world");
		from.put("classValue", inner);

		ObjectToUpdate updated = Updater.wrap(new ObjectToUpdate()).mapping("longValue2", "longValue").update(from,
				/// java8 can use lambda
				new IConverter() {
					@SuppressWarnings("unchecked")
					@Override
					public Entry<String, Object> convert(String key, Object value) {
						switch (key) {
						case "intValue": {
							return pair(key, Integer.parseInt(String.valueOf(value)));
						}
						case "arrayValue": {
							return pair(key, StringUtils.split(String.valueOf(value), ','));
						}
						case "stringValue2": {
							return pair("stringValue", value);
						}
						case "classValue": {
							return pair(key, Updater.wrap(new InnerClass()).update((Map<String, Object>) value));
						}
						}
						return pair(key, value);
					}
				});
		
		assertNotNull(updated.arrayValue);
		assertEquals(100, updated.intValue);
		assertEquals(200L, updated.longValue);
		assertEquals("this is string", updated.stringValue);
		assertEquals(4, updated.arrayValue.length);
		assertEquals("this", updated.arrayValue[0]);
		assertEquals("hello,world", updated.classValue.someValue);
	}
}
