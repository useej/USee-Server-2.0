package com.usee.utils;

import java.util.Random;

public class RandomNumber {
	public int getRandom(int min, int max){
		return new Random().nextInt(max - min + 1) + min;
	}
}
