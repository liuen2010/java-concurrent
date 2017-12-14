package com.liuen.java.util;

import java.util.Random;

public class RandomUtil {
	
	/**
	 * 生成一个左开右闭区间[0, n)的随机数
	 * @param n 区间的截至范围，必须是正整数
	 * @return
	 */
	public static int generateRandomNum(int n) {
		return new Random().nextInt(n);
	}
	
}
