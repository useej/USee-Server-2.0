
var xinm = new Array();
var phone = new Array();
loadData();

var nametxt = $('.name');
var phonetxt = $('.phone');
var pcount = xinm.length-1;//参加人数
var runing = true;
var td = 5;//内定中奖,从最小奖开始，共10个名额
var num = 0;
var t;
//开始停止
function start() {

	if (runing) {
		runing = false;
		$('#btntxt').removeClass('start').addClass('stop');
		$('#btntxt').html('停止');
		startNum()
	} else {
		runing = true;
		$('#btntxt').removeClass('stop').addClass('start');
		$('#btntxt').html('开始');
		stop();
		zd();//内定中奖
	}
}
//循环参加名单
function startNum() {
	num = Math.floor(Math.random() * pcount);
	nametxt.html(xinm[num]);
	phonetxt.html(phone[num]);
	t = setTimeout(startNum, 0);
}
//停止跳动
function stop() {
	pcount = xinm.length-1;
	clearInterval(t);
	t = 0;
}
//从一等奖开始指定前3名
function zd() {
	if(td <= 3){
		  /*
		if (td == 1) {
			nametxt.html('周一一');
			phonetxt.html('15112345678');
			$('.list').prepend("<p>"+td+' '+"周一一 -- 15112345678</p>");
		}
		if (td == 2) {
			nametxt.html('李二二');
			phonetxt.html('151000000000');
			$('.list').prepend("<p>"+td+' '+"李二二 -- 151000000000</p>");
		}
		if (td == 3) {
			nametxt.html('张三三');
			phonetxt.html('1511111111');
			$('.list').prepend("<p>"+td+' '+"张三三 -- 1511111111</p>");
		}
		*/
	}else if(td > 3){
		//打印中奖者名单
	}
	
	$('.list').prepend("<p>"+td+'. '+xinm[num]+" -- "+phone[num]+"</p>");
	if(pcount <= xinm.length - td){
			$(document.getElementById("btntxt")).hide();
			alert("抽奖结束");
			// Disable the button 
			
	}
	//将已中奖者从数组中"删除",防止二次中奖
	xinm.splice($.inArray(xinm[num], xinm), 1);
	phone.splice($.inArray(phone[num], phone), 1);	
	td = td - 1;
}


function loadData()
{	
     var jsonhttp = null ;
	jsonhttp = new XMLHttpRequest();
		// var getdmurl = "http://114.215.209.102/USee/getdmbytopic";
	var getdmurl = 'http://121.42.149.46/USee/getuserdmByInterval';   // 
    jsonhttp.open("POST", getdmurl,false);
    jsonhttp.setRequestHeader("Content-Type","application/json");
		
	var data = '{"topicID":"2","startTime":"2016-9-10 17:33:52","endTime":"2016-9-23 16:39:12"}';
	jsonhttp.send(data);
	result = jsonhttp.responseText;
	ref = eval("("+result+")");
	alert(result);
	len = ref.intervaldanmu.length;
	for(i=0;i<len;i++) {
		entry = ref.intervaldanmu[i];
		xinm[i] = entry.nickName;
		phone[i] = entry.message;
	}

}

