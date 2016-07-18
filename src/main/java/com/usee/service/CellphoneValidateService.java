package com.usee.service;

public interface CellphoneValidateService {  
  
    public static final String ACCOUNT = "002008";  
    public static final String PASSWORD = "Sy123789";  
    public static final String NEED_STATUS = "true";
    public static final String SMS_SEND_URI = "http://120.26.69.248/msg/HttpBatchSendSM";  
    
   
    public String sendMessage(String mobile, String msg);
}  
