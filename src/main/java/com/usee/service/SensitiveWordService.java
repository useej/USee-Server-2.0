package com.usee.service;

import java.util.Map;

public interface SensitiveWordService {
	public String filter(String message);
	
	public Map<String, String> getSWFileInfo();
}
