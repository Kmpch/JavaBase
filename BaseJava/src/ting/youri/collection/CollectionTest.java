package ting.youri.collection;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Queue;
import java.util.Set;

import org.junit.Test;

public class CollectionTest {

	private static final int TOTAL_COUNT = 1000000;
	
	private String[] array;
	private Set<String> set;
	private List<String> list;
	private Queue<String> queue;
	private Map<String, String> map;
	
	public CollectionTest() {
		//对数据进行初始化
		array = new String[CollectionTest.TOTAL_COUNT];
		set = new HashSet<>();
		list = new ArrayList<String>();
		queue = new LinkedList<>();
		map = new HashMap<>();
		
		for (int i = 0; i < CollectionTest.TOTAL_COUNT; i++) {
			
			String key = "user" + i;
			String value = "pass" + i;
			
			array[i] = key;
			set.add(key);
			list.add(key);
			queue.add(key);
			map.put(key, value);
		}
	}
	@Test
	public void testArray() {
		
		long startTime = System.currentTimeMillis();
		
		//老是遍历方法
		for (int i = 0; i < array.length; i++) {
			
			String str = array[i];
		}
		// forearch遍历 ： JDK1.5之后的方法
		for (String str : array) {
		}
		long endTime = System.currentTimeMillis();
		System.out.println("遍历array总耗时：" +
							(endTime - startTime));
	}
	
	//测试Set方法
	@Test
	public void testSet() {
		
		long startTime = System.currentTimeMillis();
		//迭代遍历
		Iterator<String> iterator = set.iterator();
		while (iterator.hasNext()) {
			String next = iterator.next();
		}
		
		 //foreach遍历 JDK1.5之后
		for(String str : set) {
			//set是无序的
		}
		long endTime = System.currentTimeMillis();
		System.out.println("遍历set的总耗时：" + (endTime - startTime));
	}
	
	@Test
	public void testList() {
		
		long startTime = System.currentTimeMillis();
		//for循环遍历
		for (int i = 0; i < list.size(); i++) {
			String str = list.get(i);
		}
		
		//foreach遍历 JDK1.5之后的方法
		for (String str : list) {
			
		}
		
		//迭代器的方式
		Iterator<String> iterList = list.iterator();
		while (iterList.hasNext()) {
			String str = iterList.next();
		}
		long endTime = System.currentTimeMillis();
		System.out.println("遍历list的总耗时：" + (endTime - startTime));
		
	}
	
	@Test
	public void testMap() {
		
		long startTime = System.currentTimeMillis();
		
		// Entry方式的迭代遍历 (Entry方式的遍历事件短)
		Iterator<Map.Entry<String, String>> iterMap = map.entrySet().iterator();
		while (iterMap.hasNext()) {
			Map.Entry<String, String> entry = iterMap.next();
			String key = entry.getKey();
			String value = entry.getValue();
		}
		
		// Entry方式的foreach遍历
		for (Map.Entry<String, String> entry: map.entrySet()) {
			String key = entry.getKey();
			String value = entry.getValue();
		}
		
		//keySet方式的迭代遍历(keySet方式遍历耗时多)
		Iterator<String> iterMapKey = map.keySet().iterator();
		while (iterMapKey.hasNext()) {
			String key = iterMapKey.next();
			String value = map.get(key);
		}
		
		//keySet方式的foreach遍历,速度慢
		for (String key : map.keySet()) {
			String value = map.get(key);
		}
		
	}
	/**
	 * 这里主要用来测试获取遍历HashMap,仅获取key的时长比较
	 * entry,keyset两种的对比
	 * 同时使用foreach的方式
	 */
	public void testGetMapKeyTime() {
		
		long entryStartTime = System.currentTimeMillis();
		for (Map.Entry<String, String> entry : map.entrySet()) {
			String key = entry.getKey();
		}
		long entryEndTime = System.currentTimeMillis();
		System.out.println("entrySet获取key:" + (entryEndTime - entryStartTime));
		long keyStartTime = System.currentTimeMillis();
		for (String key : map.keySet()) {
			
		}
		long keyEndTime = System.currentTimeMillis();
		System.out.println("keySet获取key:" + (keyEndTime - keyStartTime));
	}
	
	public static void main(String[] args) {
		
		CollectionTest test = new CollectionTest();
		test.testGetMapKeyTime();
	}
}
