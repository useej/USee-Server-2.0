package com.usee.service.impl;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.usee.service.SqlInjectService;
import com.usee.utils.KeywordUtil;
import com.usee.utils.PropertiesUtil;

@Service
public class SqlInjectServiceImpl implements SqlInjectService {

	public static String CHARSET = "UTF-8";

	public String SqlInjectHandle(String jsonString) {
		ObjectMapper mapper = new ObjectMapper();
		Map<String, String> map = new HashMap<String, String>();
		try {
			map = mapper.readValue(jsonString, new TypeReference<Map<String, String>>() {
			});
		} catch (Exception e) {
			e.printStackTrace();
		}
		// 得到所有参数的key
		Set<String> keys = map.keySet();
		for (String property : keys) {
			if (PropertiesUtil.isSpecialProperties(property)) {
				// 如果参数是特殊的String 类型的
				String value = map.get(property);
				// 将特殊字符进行encode
				String handValue = null;
				try {
					handValue = KeywordUtil.SqlkeywordDecode(value);
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}
				map.replace(property, handValue);

			} else if (PropertiesUtil.isStringProperties(property)) {
				// 如果参数是 String 类型的
				String value = map.get(property);
				// 将特殊字符过滤掉
				String handValue1 = KeywordUtil.specharsFilter(value);
				// 将sql关键字过滤掉
				String handValue = KeywordUtil.SqlkeywordFilter(handValue1);
				map.replace(property, handValue);

			} else if (PropertiesUtil.isStringProperties(property)) {
				// 如果参数是 int 类型的
				String value = map.get(property);
				
				Pattern pattern = Pattern.compile("[0-9]*");
				Matcher isNum = pattern.matcher(value);
				if (value.length() > 10) {
					map.replace(property, "0");
				} else if (!isNum.matches()) {
					map.replace(property, "0");
				} else if (Integer.parseInt(value) > 2147483647) {
					map.replace(property, "0");
				}
			}
		}
		
		String json = null;
		try {
			json = mapper.writeValueAsString(map);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		return json;
	}

}
