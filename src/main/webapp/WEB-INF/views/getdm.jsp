<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>  
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>  
<!DOCTYPE html PUBLIC"-//W3C//DTD HTML 4.01 Transitional//EN""http://www.w3.org/TR/html4/loose.dtd">  
<html>  
    <head>  
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">  
        <title>GetDanmu</title>  
    </head>  
    <body>  
        <form action="getdmbytopic" method="post">
        	<input type="text" name="topicId" />
        	<input type="text" name="pageNum" />
        	<input type="text" name="pageSize" />
        	<input type="submit" value="提交">
        </form>
        <c:out value="${danmu}"></c:out> 
        
        <form action="getdmdetails" method="post">
        	<input type="text" name="danmuId" />
        	<input type="submit" value="提交">
        </form>
        <c:out value="${danmuDetails}"></c:out> 
    </body>  
</html>  