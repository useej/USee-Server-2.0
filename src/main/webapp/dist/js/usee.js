
		var serverPrefix = 'http://114.215.209.102/USee/'; // http://114.215.209.102/USee/
		var footerClosed = 0;
		
		// TODO:  Remove Duplications	
		function danmumethod(){
		  var method = serverPrefix+'getdmbytopic';   // // getdmbytopic'
		  return method;
		}
		
		function titlemethod(){
		  var method = serverPrefix+'gettopictitle';   //  
		  return method;
		}
		
		function send() {	
		// var topicID = 16;
		// var sendInfo = '{"topicid":"'+topicID+'"}';
		// alert(sendInfo);
		
		$.ajax({
           type: "POST",
           url: "getdmbytopic",
           dataType: "json",
           success: function (msg) {
           		alert(msg);
               if (msg) {
                   alert("Somebody" + name + " was added in list !");
               } else {
                   alert("Cannot add to list !");
               }
           },
           async : false,
			error: function(XMLHttpRequest, textStatus, errorThrown) { 
				
                    alert("Status: " + textStatus); alert("Error: " + errorThrown); 
                } ,
           data: sendInfo
       });
       }
       
       // Get First 100 danmu each time 
    function getDanmu(method) {
     	var jsonhttp = null ;
		jsonhttp = new XMLHttpRequest();
		var getdmurl = method;   // 
		jsonhttp.open("POST", getdmurl,false);
		var data = '{"topicid":"'+getTopicID()+'","pagesize":"100","pagenum":"1"}';
		jsonhttp.send(data);
		result = jsonhttp.responseText;
		ref =eval("("+result+")");
		return ref;
		}
		
		 function getTitle(method) {
     	var jsonhttp = null ;
		jsonhttp = new XMLHttpRequest();
		var getdmurl = method;   // 
		jsonhttp.open("POST", getdmurl,false);
		var data = '{"topicID":"'+getTopicID()+'"}';
		jsonhttp.send(data);
		result = jsonhttp.responseText;
		ref =eval("("+result+")");
		return ref;
		}
		
		
	function  barrager(){
		
  		if(run_once){
      		//如果是首次执行,则设置一个定时器,并且把首次执行置为false	  		
      		var totalDM = ref.danmu.length;
      		var maxLength =0 ;
      		
      		for (i =0;i<totalDM;i++) {
      			  currentLen	 = ref.danmu[i].messages.length;
      			  if(maxLength < currentLen) {
      			  			maxLength = currentLen;
      			  }
      		}
          
      		var window_width = $(window).width()  ;

			// 控制弹幕密度
			var speedRatio =0.2

			if(window_width <800) {
					speedRatio =0.5;
			}

			looper_time=maxLength/ speedRatio; // 

			if(looper_time < 120) {
					looper_time = 200;
			}

			looper=setInterval(barrager,looper_time*10); 
      	 	run_once=false;
  		}
  		
  		if( index ==0){
  			 time_elasped =0;
  		}
  		//发布一个弹幕
  		// 注册用户的URL可以取得
  		// 非注册用户 渲染 Or 随机头像 'img':'barrager.png'
  		// TODO Remove the URL
  		var userIcon  = ref.danmu[index].userIcon;
  		var imgUrl = "static/domino-mask.png"; 
  		 
  		if (userIcon.indexOf("_") < 0){  		
  				 imgUrl = "userIcons/"+ ref.danmu[index].userId +".png"
  		}
  		
     	var danmu_i={'img':imgUrl,'info':convertEmoji(ref.danmu[index].messages),'speed':1};
        
  		$('body').barrager(danmu_i);
  		index++;
  		//  定期刷新，获取新弹幕
  		
  		if (time_elasped > refreshInterval) {
  				index =0;  //  开始轮播
				time_elasped =0;
				run_once=false;
				method = danmumethod();
				ref = getDanmu(method);
      			barrager();
  		}
  		
  		//所有弹幕发布完毕，清除计时器, 轮播
 		 if(index == total){
 		 		// clear_barrage();
      			index =0;  //  开始轮播
      			time_elasped =0;
      			run_once=false;
      	}
	}
		
		function timedCount()
		{
			time_elasped=time_elasped+1;
			t = setTimeout("timedCount()",1000)
		}
		
    	function  clear_barrage(){
        	$.fn.barrager.removeAll();
    	}
		
    	function getTopicID(){		//获取QuerryString中的TopicID
			return getQueryStringByName("topicid");
	}
	
		function getTopicTitle(){		//获取QuerryString中的TopicID
			return getQueryStringByName("title");
		}
	
	
		function getQueryStringByName(name){		//根据QuerryString中的Key获取其Value
     		var result = location.search.match(new RegExp("[\?\&]" + name+ "=([^\&]+)","i"));
    		if(result == null || result.length < 1){
         		return "";
     		}
     return result[1];
	}
	
   function sleep(numberMillis) { 
			var now = new Date(); 
			var exitTime = now.getTime() + numberMillis; 
			while (true) { 
				now = new Date(); 
				if (now.getTime() > exitTime) 
				return; 
			} 
	}
	
// 自带Emoji字符串的转换
function convertEmoji(str){
    subStrings = str.split(']');
    result = "";
    
	for (i =0; i < subStrings.length;i++) {
     if ( subStrings[i].indexOf("/") >= 0 ) {
       //1. 提取Emoji字符串
    	emoji = subStrings[i].substr(subStrings[i].indexOf("/"),subStrings[i].length);
    	prefix = subStrings[i].substr(0,subStrings[i].indexOf("/"));
    	emoji = emoji + "]";
    
    	// 2. 查表把/YYY] 变成  <img>	
    	for (j=0;j<mappings.length;j++) {
    	        emj = mappings[j].match(emoji);
    			if ( emj != null && typeof(emj) !== "undefined") {
    					// alert(mappings[j]);
    					res = mappings[j].split('|');
    					imgIcon = res[1];
    					imgIcon = "static/emoji/"+imgIcon+".png";
    					result=result + prefix+ '<img src="'+imgIcon+'" style ="width: 24px;height: 24px">';
    			}
    		}
    	}
    	else { // No Emoji, remains the same.
    		result+=subStrings[i];
    	}
    }  // End For 
    
    return result;
   }

		   
		$(function(){
				$("#closeButton").click(function(){
				$("#footer").css("display","none");//点击隐藏
				footerClosed = 1;
		});
		});

	
	
