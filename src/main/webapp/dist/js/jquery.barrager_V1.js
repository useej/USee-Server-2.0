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

		var time = new Date().getTime();
		var barrager_id = 'barrage_' + time;
		var id = '#' + barrager_id;
		var div_barrager = $("<div class='barrage' id='" + barrager_id + "'></div>").appendTo($(this));
		var window_height = $(window).height() - 220;
		
		//var yongdaos = new Array();		
		var now=new Date(); 
		
		
		numOfYongDao = 8; // If PC MAC broswer, set to 8, mobile phone set to 6
	
		if ($(window).width() < 600) {
			numOfYongDao =6;
		}
		var number = now.getSeconds()%numOfYongDao;
		 
		var bottom = (barrage.bottom == 0) ? Math.floor(number* window_height /numOfYongDao + 100) : barrage.bottom;
		
		div_barrager.css("bottom", bottom + "px");
		div_barrager_box = $("<div class='barrage_box cl'></div>").appendTo(div_barrager);
		if(barrage.img){

			div_barrager_box.append("<a class='portrait z' href='javascript:;'></a>");
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
		
		// .barrage{position: fixed;bottom:70px;right:-500px;display: inline-block;width: 500px;z-index: 99999}
		
		var i = 0;
		div_barrager.css('margin-right', i);
		var looper = setInterval(barrager, barrage.speed);

		function barrager() {

			window_width = $(window).width() + $(window).width() /5;
			
			if(window_width<800) {
				window_width = 1000;
			}
			// alert(window_width);
			
			if (i < window_width) {
				i += 1;				
				if(window_width ==1000){
						// alert('too narrow!')
						$(id).animate({right: '-'+300+'px' }, 500);
				} else {
						$(id).animate({right: '-'+200+'px' }, 500);
				}
				
				 if ( i > window_width*0.1) {
				 	  $(id).animate({ right: '+'+window_width/5+'px' }, 500);
				 }
				
				 $(id).css('margin-right', i/2);
			    
			} else {

				$(id).remove();
				i =0;
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

		 $('.barrage').remove();

	}

})(jQuery);