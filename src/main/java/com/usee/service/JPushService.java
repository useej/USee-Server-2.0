package com.usee.service;

import java.util.Map;

public interface JPushService {
	
	public void push(String userID, String message, Map<String, String> extras);
	
	public void push_all(String message, Map<String, String> _extras);
}
