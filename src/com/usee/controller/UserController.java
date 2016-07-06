package com.usee.controller;


import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.usee.moddel.User;
import com.usee.service.UserManager;


@Controller  
@RequestMapping("/user")  
public class UserController {  

@Resource
private UserManager userManager;  

@RequestMapping("/getAllUser")  
public String getAllUser(HttpServletRequest request){  
    request.setAttribute("userList", userManager.getAllUser());  
    return "/Index";  
}  

@RequestMapping("/getUser")  
public String getUser(String id,HttpServletRequest request){  
    request.setAttribute("user", userManager.getUser(id));  
    return "/editUser";  
}  

@RequestMapping("/toAddUser")  
public String toAddUser(){  
    return "/addUser";  
}  

@RequestMapping("/addUser")  
public String addUser(User user,HttpServletRequest request){  
    System.out.println("用户名：======"+user.getUserName());  
    userManager.addUser(user);  
    return "redirect:/user/getAllUser";  
}  

@RequestMapping("/delUser")  
public void delUser(String id,HttpServletResponse response){  
    String result = "{\"result\":\"error\"}";  
    if(userManager.delUser(id)){  
        result = "{\"result\":\"success\"}";  
    }  
    response.setContentType("application/json");  
    try {  
        PrintWriter out = response.getWriter();  
        out.write(result);  
    } catch (IOException e) {  
        e.printStackTrace();  
    }  
}  

@RequestMapping("/updateUser")  
public String updateUser(User user,HttpServletRequest request){  
    if(userManager.updateUser(user)){  
        user = userManager.getUser(user.getId());  
        request.setAttribute("user", user);  
        return "redirect:/user/getAllUser";  
    }else{  
        return "/error";  
    }  
}  
}  