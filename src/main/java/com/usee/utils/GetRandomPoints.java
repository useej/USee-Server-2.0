package com.usee.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class GetRandomPoints
{
	private static double EARTH_RADIUS = 6378.137;
	private static double rad(double d)
	{
		return d * Math.PI / 180.0;
	}
	public static double getDistance(double lat1, double lng1, double lat2, double lng2)
	{
		double radLat1 = rad(lat1);
		double radLat2 = rad(lat2);
		double a = radLat1 - radLat2;
		double b = rad(lng1) - rad(lng2);
		double s = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(a/2),2) + 
				Math.cos(radLat1)*Math.cos(radLat2)*Math.pow(Math.sin(b/2),2)));
		s = s * EARTH_RADIUS;
		s = Math.round(s * 10000) / 10000;
		return s;
	}
	
	public static double[] getAround1(double lat,double lon,double raidus){  
        Double latitude = lat;  
        Double longitude = lon;  

        Double degree = (24901*1609)/360.0;  

        double raidusMile = raidus;  

        Double dpmLat = 1/degree;  
        Double radiusLat = dpmLat*raidusMile;  
        Double minLat = latitude - radiusLat;  
        Double maxLat = latitude + radiusLat;  

        Double mpdLng = degree*Math.cos(latitude * (Math.PI/180));  
        Double dpmLng = 1 / mpdLng;  
        Double radiusLng = dpmLng*raidusMile;  
        Double minLng = longitude - radiusLng;  
        Double maxLng = longitude + radiusLng;  

        return new double[]{minLat,maxLat,minLng,maxLng};  

    }  
	
	
	public static List<Object> getAround(double lat,double lon ,double raidus, int n) {
		List<Object> list = new ArrayList<Object>();
        double[] result = getAround1(lat, lon, raidus);
        double _lat = result[1] - result[0];
    	double _lon = result[3] - result[2];
    	
    	for (double d : result) {
			System.out.println(d);
		}
    	
        for(int i=0; i<n; i++){
        	Random random = new Random();
        	double randomNum = random.nextDouble();
        	double randomLat = result[0] + _lat * randomNum;
        	double randomLng = result[2] + _lon * randomNum;
        	double distance = getDistance(lat, lon, randomLat, randomLng);
        	if(distance*1000 < raidus) {
        	 	Map<String, String> map = new HashMap<String, String>();
                map.put("randomLat", randomLat+"");
                map.put("randomLng", randomLng+"");
                map.put("distance", distance + "");
                list.add(map);
        	} else {
        		i--;
        	}
        }
        return list;
    }


}
