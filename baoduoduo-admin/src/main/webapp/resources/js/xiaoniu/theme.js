var navMenus;
$(function(){
	
});

/**
 * 初始化菜单
 */
function init(){
	var token = $("meta[name='_csrf']").attr("content");  
	var header = $("meta[name='_csrf_header']").attr("content");  
/*	$.post("/secure/menu/queryMenu",{},function(result){
		if(result["resultCode"] == 0){
			parse(result);
		} else {
			alert(result["msg"]);
		}
	},"json");*/
	$.ajax({
	    url: "/secure/menu/queryMenu",
	    type: "POST",
	    dataType: "json",
	    beforeSend: function (xhr) {
	    	xhr.setRequestHeader(header, token);
	    },
	    success: function (result) {
	    	if(result["resultCode"] == 0){
				parse(result);
			} else {
				alert(result["msg"]);
			}
	    },
	    error: function (xhr, textStatus, errorThrow) {
	        alert(xhr.readyState);
	    }
	});
	
}

/**
 * 解析菜单数据
 * @param result
 */
function parse(result){
	navMenus = result["data"];
	for(var i = 0; i < navMenus.length; i++) {//一级菜单
		var menu = navMenus[i];
		var menuStr = '';
		menuStr += "<ul>";
		var subMenus = menu.subMenus;
		for ( var j = 0; j < subMenus.length; j++) {//二级菜单
			var subMenu = subMenus[j];
			menuStr += '<li><div><a ref="' + subMenu.menuid + '" href="#" rel="' + subMenu.url + '" ><span class="icon '+subMenu.icon+'" >&nbsp;</span><span class="nav">' + subMenu.menuname + '</span></a></div></li>';
		}
		menuStr += "</ul>";
		$('#nav').accordion('add', {
			title: menu.menuname,
            content: menuStr,
            iconCls: 'icon '+menu.icon
		});
	}
	
	tabClose();
	tabCloseEven();
	$("#loading").fadeOut(function() {
		$('#tabs').tabs({});
		$("#header,#footer,#indexTab").fadeIn();
		$("#nav").accordion({animate:true});
		$('.easyui-accordion li a').click(function(){
			selectTab($(this));
		}).hover(function(){
			$(this).parent().addClass("hover");
		},function(){
			$(this).parent().removeClass("hover");
		});
		
		selectTab($('.easyui-accordion li a').eq(0));
		
		//选中第一个
		/**
		var panels = $('#nav').accordion('panels');
		if(panels.length > 0) {
			var t = panels[0].panel('options').title;
		    $('#nav').accordion('select', t);
		}
		*/
	});
}


//获取左侧导航的图标
function getIcon(menuid){
	var icon = 'icon ';
	for(var i = 0 ; i < navMenus.length; i++) {
		var n = navMenus[i];
		var subMenus = n.subMenus;
		for(var k = 0; k < subMenus.length; k++) {
			var o = subMenus[k];
			if(o.menuid == menuid){
				icon += o.icon + ' ';
			}
		}
	}
	return icon;
}

function addTab(subtitle,url,icon){
	if(!$('#tabs').tabs('exists',subtitle)){
		$('#tabs').tabs('add',{
			title:subtitle,
			content:createFrame(url),
			closable:true,
			icon:icon
		});
	}else{
		$('#tabs').tabs('select',subtitle);
		$('#mm-tabupdate').click();
	}
	tabClose();
}

function createFrame(url)
{
	var s = '<iframe scrolling="auto" frameborder="0"  src="'+url+'" style="width:100%;height:100%;"></iframe>';
	return s;
}

function tabClose()
{
	/*双击关闭TAB选项卡*/
	$(".tabs-inner").dblclick(function(){
		var subtitle = $(this).children(".tabs-closable").text();
		$('#tabs').tabs('close',subtitle);
	});
	/*为选项卡绑定右键*/
	$(".tabs-inner").bind('contextmenu',function(e){
		$('#mm').menu('show', {
			left: e.pageX,
			top: e.pageY
		});

		var subtitle =$(this).children(".tabs-closable").text();
		$('#mm').data("currtab",subtitle);
		$('#tabs').tabs('select',subtitle);
		return false;
	});
}
//绑定右键菜单事件
function tabCloseEven()
{
	//刷新
	$('#mm-tabupdate').click(function(){
		var currTab = $('#tabs').tabs('getSelected');
		var url = $(currTab.panel('options').content).attr('src');
		$('#tabs').tabs('update',{
			tab:currTab,
			options:{
				content:createFrame(url)
			}
		});
	});
	//关闭当前
	$('#mm-tabclose').click(function(){
		var currtab_title = $('#mm').data("currtab");
		$('#tabs').tabs('close',currtab_title);
	});
	//全部关闭
	$('#mm-tabcloseall').click(function(){
		$('.tabs-inner span').each(function(i,n){
			var t = $(n).text();
			$('#tabs').tabs('close',t);
		});
	});
	//关闭除当前之外的TAB
	$('#mm-tabcloseother').click(function(){
		$('#mm-tabcloseright').click();
		$('#mm-tabcloseleft').click();
	});
	//关闭当前右侧的TAB
	$('#mm-tabcloseright').click(function(){
		var nextall = $('.tabs-selected').nextAll();
		if(nextall.length==0){
			//msgShow('系统提示','后边没有啦~~','error');
			return false;
		}
		nextall.each(function(i,n){
			var t=$('a:eq(0) span',$(n)).text();
			$('#tabs').tabs('close',t);
		});
		return false;
	});
	//关闭当前左侧的TAB
	$('#mm-tabcloseleft').click(function(){
		var prevall = $('.tabs-selected').prevAll();
		if(prevall.length==0){
			return false;
		}
		prevall.each(function(i,n){
			var t=$('a:eq(0) span',$(n)).text();
			$('#tabs').tabs('close',t);
		});
		return false;
	});

	//退出
	$("#mm-exit").click(function(){
		$('#mm').menu('hide');
	});
}

//弹出信息窗口 title:标题 msgString:提示信息 msgType:信息类型 [error,info,question,warning]
function msgShow(title, msgString, msgType) {
	$.messager.alert(title, msgString, msgType);
}

function selectTab($tab) {
	var tabTitle = $tab.children('.nav').text();

	var url = $tab.attr("rel");
	var menuId = $tab.attr("ref");
	var icon = getIcon(menuId);

	addTab(tabTitle,url,icon);
	$('.easyui-accordion li div').removeClass("selected");
	$tab.parent().addClass("selected");
}