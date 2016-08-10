package com.usee.utils;

import java.util.ArrayList;
import java.util.List;
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
	
	public static int getRandomNum(List<Integer> existingList,int N){
		// generate the list containing N intergers
		int randomNum = 0;
		int M= existingList.size();
		int[] listN=new int[N];//N表示原数组的个数
		
		// System.out.println(M);
		for (int i =0; i<N;i++) {
			listN[i] = i;
		}
		for(int i=0;i<M;i++)//假如第一个数是300，放到最后一位6399的位置，把6399位置的数字放到300的位置
		{
			int temp=listN[N-1-i];
			listN[N-1-i] = existingList.get(i);
			listN[existingList.get(i)]=temp;
			
		}
		int num=N-M;
		 Random rd=new Random();
		randomNum=Math.abs(rd.nextInt(num)); 
	    
		return listN[randomNum]+1;
		
	}
	
	public static void main(String[] args) {
		List<Integer> existingList=new ArrayList();
		
		for(int i=0;i<6397;i++)
		{
			existingList.add(i, i); // 6400-1-i
		}

		// int[] testList=new int[20];
		int testNum;
		testNum =RandomNumber.getRandomNum(existingList, 6400);

		System.out.println( "testNum = : "+testNum);
		
		System.out.println( 6400 / 100); // 63 -> 63.png
		System.out.println( 50 % 100); // 99 -> colorHexCodes -> E6A473 
		// return 63_E6A473
		
	}
	
}
