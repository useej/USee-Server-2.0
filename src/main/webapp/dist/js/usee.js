		
		var serverPrefix = 'http://114.215.141.67/USee/'; // http://114.215.141.67/USee/

		function danmumethod(){
		  var method = 'getdmbytopic';   // // getdmbytopic'
		  return method;
		}
		
		// http://114.215.141.67/USee/gettopictitle
		function titlemethod(){
		  var method = 'gettopictitle';   //  
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
       
       // Get First 100 a time

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
      		// alert("MAXDMSIze:"+maxLength);
      		var window_width = $(window).width()  ;
			// 控制弹幕速度
			var speedRatio =0.2

			looper_time=maxLength/ speedRatio; // 
			// alert("Looper_Time ="+looper_time*12);
			looper=setInterval(barrager,looper_time*12); 
      	 	run_once=false;
  		}
  		
  		if( index ==0){
  			 time_elasped =0;
  		}
  		//发布一个弹幕
  		// 注册用户的URL可以取得
  		// 非注册用户 渲染 Or 随机头像 'img':'barrager.png'
  		// TODO Remove the URL
  		var imgUrl = "userIcons/"+ ref.danmu[index].userId +".png"
     	var danmu_i={'img':imgUrl,'info':ref.danmu[index].messages,'speed':1};
        
  		$('body').barrager(danmu_i);
  		//索引自增
  		index++;
  		
  		//  定期刷新，获取新弹幕
  		// UI Update ! 
  		if (time_elasped > refreshInterval) {
  				clear_barrage();
  				// alert (time_elasped);
				index =0;  //  开始轮播
				time_elasped =0;
				// clearTimeout(t);
				// timedCount();
				run_once=false;
				method = danmumethod();
				ref = getDanmu(method);
      			barrager();
  		}
  		
  		//所有弹幕发布完毕，清除计时器。
 		 if(index == total){
 		 	clear_barrage();
      		index =0;  //  开始轮播
      		time_elasped =0;
      		run_once=false;
      		// barrager();
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
			// alert(getQueryStringByName("title"));
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