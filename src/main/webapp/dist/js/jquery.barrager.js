/*!
 *@name     jquery.barrager.js
 *@author   yaseng@uauc.net
 *@url      https://github.com/yaseng/jquery.barrager.js
 */
(function($) {

	$.fn.barrager = function(barrage) {
		barrage = $.extend({
			close:true,
			bottom: 0,
			max: 10,
			speed: 6,
			color: '#fff',
			old_ie_color : '#000000'
		}, barrage || {});

		var now=new Date(); 
		
		numOfYongDao = 1; // If PC MAC broswer, set to 8, mobile phone set to 6
	    var window_height = $(window).height() - 70;
		numOfYongDao = Math.floor( window_height / 80) +1 ;
		
		currentDanmu = new Array(numOfYongDao);
		
		if(numOfYongDao ==0){
				numOfYongDao = 1;
		}
		var maxDistance = 300;
		
		var number = Math.round((Math.random())*(numOfYongDao-1)) ; //      now.getSeconds()%numOfYongDao

		var time = new Date().getTime();
		var barrager_id = 'barrage_' + time+'_YD'+number;
		var id = '#' + barrager_id;
		var div_barrager = $("<div class='barrage' id='" + barrager_id + "'></div>").appendTo($(this));
		
		var bottomHeight = 30;
		
		if (current_DMMRight[number] > 500) {
			var bottom = (barrage.bottom == 0) ? Math.floor(number* window_height /numOfYongDao + bottomHeight) : barrage.bottom;
	    // TODO Avoid dup wait for the first one! 
	    	current_DMMRight[number] = 0;
		}
		else { // Remove this Danmu ? or place to an empty one 
			
			if(typeof(current_DMMRight[number]) === "undefined")  {
					bottom =  Math.floor(number* window_height /numOfYongDao + bottomHeight) ;
			} else 
			{
				for (i =0;i<numOfYongDao;i++) {
					bottom = bottomHeight;
					if(current_DMMRight[i] == 0)  {
						bottom =  Math.floor(i* window_height /numOfYongDao + bottomHeight) ;
						i=numOfYongDao;
					}
				}
			}
				 
		}

		if (bottom > window_height) 
				{ 
					bottom = bottom = Math.floor((numOfYongDao-1)* window_height /numOfYongDao + bottomHeight) ;;
 
				}
				
		div_barrager.css("bottom", bottom + "px");
		
		
		var info = barrage.info;
		if (info.length < 5) {
			div_barrager.css("width",   "200px");
			div_barrager.css("right",   "-400px"); 
		} 
		else if (info.length < 10)  {
			div_barrager.css("width",   "400px");
			div_barrager.css("right",   "-600px"); 
		}
		else if (info.length < 15)  {
			div_barrager.css("width",   "600px");
			div_barrager.css("right",   "-800px"); 
		}
		else if (info.length < 20)  {
			div_barrager.css("width",   "800px");
			div_barrager.css("right",   "-1200px"); 
		}
		else if (info.length < 30)  {
			div_barrager.css("width",   "1200px");
			div_barrager.css("right",   "-1400px"); 
		} else if (info.length < 40) {
			div_barrager.css("width",   "1600px");
			div_barrager.css("right",   "-1800px"); 
		} else {
			div_barrager.css("width",   "1800px");
			div_barrager.css("right",   "-2000px"); 
		}
		
		div_barrager_box = $("<div class='barrage_box cl'></div>").appendTo(div_barrager);
		if(barrage.img){

			div_barrager_box.append("<a class='portrait z' href=''></a>");
			var img = $("<img src='' >").appendTo(id + " .barrage_box .portrait");
			img.attr('src', barrage.img);
		}
		
		div_barrager_box.append(" <div class='z p'></div>");
		var colors = new Array();
		colors[0] = '#4FC0EA';
		colors[1] = '#5ED3B6';
		colors[2] = '#F8B238';
		colors[3] = '#F37E53'; 
		colors[4] = '#A3CDA5';
		
		var now=new Date(); 
		var number = now.getSeconds()%5;
		// alert(number);
		div_barrager_box.css('background-color',colors[number]);
		if(barrage.close){

			div_barrager_box.append(" <div class='close z'></div>");

		}
		
		var content = $("<a title='' href='' target='_blank'></a>").appendTo(id + " .barrage_box .p");
		content.attr({
			'href': barrage.href,
			'id': barrage.id
		}).empty().append(barrage.info);
		
		if(navigator.userAgent.indexOf("MSIE 6.0")>0  ||  navigator.userAgent.indexOf("MSIE 7.0")>0 ||  navigator.userAgent.indexOf("MSIE 8.0")>0  ){

			content.css('color', barrage.old_ie_color);

		}else{
			content.css('color', barrage.color);
		}
		
		var i = 0;
		var beginTime =  new Date().getTime();
		div_barrager.css('margin-right', i);
		var looper = setInterval(barrager, barrage.speed);
        
        
        
		function barrager() {
		
			var window_width = $(window).width()  ;
			var currentTime = new Date().getTime();
		    	 
			// 控制弹幕速度
			var speedRatio =5
			currentTime = new Date().getTime();
			runningTime = 3000;
			
			// Time count 
			if (currentTime < beginTime + duration ) {
				// Reduce speed for mobile device
		
				if(window_width <800) {
					speedRatio =10;
					// runningTime 走完屏幕画的时间
					runningTime = 5000;
				}
				middleScreen = Math.floor(window_width / 2);
	
	    		var info = barrage.info;		
				currentTime = new Date().getTime();
				var firstDanmus  = new Array();
				var width = $(window).width() ;
				dmposision  = Math.floor((new Date().getTime() - beginTime) /speedRatio) * barrage.speed;
				
				var currentdivWidth = $(id).css("width");	
				if( typeof currentdivWidth !==  "undefined") {
						currentdivWidth = parseFloat(currentdivWidth.replace(/px/,""));
				} else {
						currentdivWidth = 0;
				}
				
				fadeoutPosision = width*0.9 ;
				
				if (width<800) {
						fadeoutPosision =  width *1.2;
						middleScreen = middleScreen + 400;
				} 
				else {
						middleScreen = middleScreen + 300;
				}
				
			   mright = '+='+middleScreen;
			   $(id).animate({
  						      marginRight: mright
    						}, runningTime, function() {
    						
    			}
				);	
			
				var danmu_YD = id.substr(id.indexOf("_YD")+3,id.length)
				
				current_DMMRight[parseInt(danmu_YD)] = dmposision;
				    		  		  
				
				 if ( dmposision > fadeoutPosision ) {
				 	 $(id).fadeOut(10000);
			}
				 
			} else {
				$(id).remove();
				i=0;
 				return false;
			}

		}


		div_barrager_box.mouseover(function() {
			clearInterval(looper);
		});

		div_barrager_box.mouseout(function() {
			looper = setInterval(barrager, barrage.speed);
		});

		$(id+'.barrage .barrage_box .close').click(function(){

			$(id).remove();

		})

	}
 
	$.fn.barrager.removeAll=function(){
		 // $('.barrage').fadeOut(1000);
		 $('.barrage').remove();

	}

})(jQuery);