package ting.youri.collection;

import java.util.Comparator;

public class ComparableTest implements Comparable {

	/**
	 * Comparable接口 来自于 java.lang
	 * 传入一个对象与当前对象进行比较，返回负数，0，整数 表示小于，等于，大于当前对象
	 * 
	 */
	@Override
	public int compareTo(Object o) {
		// TODO Auto-generated method stub
		return 0;
	}

}
class ComparatorTest implements  Comparator {

	/**
	 * Comparator来源于java.util
	 * a negative integer, zero, or a positive integer as 
	 * the first argument is less than, equal to, or greater than the second.
	 */
	@Override
	public int compare(Object o1, Object o2) {
		// TODO Auto-generated method stub
		return 0;
	}
	
}
