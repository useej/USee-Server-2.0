package com.usee.utils;

import static org.junit.Assert.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import org.junit.Before;
import org.junit.Test;

public class TimeUtilTest {

	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void test() throws ParseException {
		TimeUtil timeUtil= new TimeUtil();
		System.out.println(timeUtil.currentTime);
		System.out.println(timeUtil.currentTimeStamp);
		System.out.println(timeUtil.timestamp2Date("1252639886"));
		System.out.println(timeUtil.date2Timestamp("2016-08-02 14:11:37"));
	}

}
