// function getDanmu(getdmurl, dmTime) {
//   var jsonhttp = null ;
//   jsonhttp = new XMLHttpRequest();
//   jsonhttp.open("POST", getdmurl,false);
//   var data = JSON.stringify({createTime: dmTime});
//   jsonhttp.send(data);
//   result = jsonhttp.responseText;
//   ref = eval("("+result+")");
//   return ref;
// }

function getDanmu(danmuTime) {
  $.ajax({
    type: "POST",
    // 由于服务器没有处理json/application，所以要指定ContentType
    contentType: "text/plain",
    dataType: "json",
    url: "/USee/getnewdanmu",
    data: JSON.stringify({
      createTime: danmuTime
    }),
    success: function(json) {
      console.log(json);
      var typeData = json.result;
      $("#danmu").empty();
      $("#danmuInfo").empty();
      $("#danmuInfo").append("<p  align='right'>至今，新用户创建了" + typeData.length + "条弹幕</p>");
      $.each(typeData, function(i, n) {
        var tbBody = ""
        tbBody += "<tr><td class='danmu-title'>" + n.title + "</td>" + "<td class='danmu-content'>" + n.danmu + "</td></tr>";
        $("#danmu").append(tbBody);
      });
    },
    error: function(json) {
      alert("加载失败");
    }
  });
}

$(document).ready(function() {
  $('#search').datetimepicker({
    startDate: '-10y',
    endDate: '+0d',
    format: 'yyyy-mm-dd',
    autoclose: true,
    minView: 2,
    viewSelect: 2,
    language: 'zh-CN'
  }).val("2016-9-19");

  getDanmu($('#search').val())

  $("#search").change(function() {
    // 监听日期输入框，一旦变化则请求新数据
    getDanmu($(this).val())
  })
})
