package com.usee.utils;

import java.util.Random;

public class RandomNumber {
	/**
	 * 生成随机数
	 * @param min 
	 * @param max
	 * @return 返回一个该范围内的随机数
	 */
	public int getRandom(int min, int max){
		//输入最小最大值，返回一个该范围内的随机数
		return new Random().nextInt(max - min + 1) + min;
	}
}
