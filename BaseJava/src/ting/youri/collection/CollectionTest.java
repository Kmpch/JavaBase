package ting.youri.collection;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

import org.junit.Test;



public class CollectionTest {

	private static final int TOTAL_COUNT = 100000;
	
	private String[] array;
	private Set<String> set;
	private List<String> list;
	private Queue<String> queue;
	private Map<String, String> map;
	
	public CollectionTest() {
		// 对数据进行初始化
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
		
	}
}
