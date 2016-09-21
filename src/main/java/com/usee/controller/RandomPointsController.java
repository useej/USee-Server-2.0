package com.usee.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.usee.utils.GetRandomPoints;

@Controller
public class RandomPointsController {

	@ResponseBody
	@RequestMapping(value = "getRandomPoints", method = RequestMethod.POST, produces = "application/json;charset=utf-8")
	public List<Object> getRandomPoints(@RequestBody String json){
		List<Object> list = new ArrayList<Object>();
		Map<String, String> map = null;
		try {
			ObjectMapper mapper = new ObjectMapper();
			map = new HashMap<String, String>();
			map = mapper.readValue(json, new TypeReference<Map<String, String>>() {
			});
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		double lat = Double.parseDouble(map.get("lat"));
		double lon = Double.parseDouble(map.get("lon"));
		double raidus = Double.parseDouble(map.get("raidus"));
		int n = Integer.parseInt(map.get("n"));
		
		list = GetRandomPoints.getAround(lat, lon, raidus, n);
		
		return list;
		
	}
}
