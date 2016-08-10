package com.usee.dao;

public interface RandomNameDao {

	public String getRandomNameById(int id);

	public int getRandomNameIdByIdRandomName(String randomUserName);
}
