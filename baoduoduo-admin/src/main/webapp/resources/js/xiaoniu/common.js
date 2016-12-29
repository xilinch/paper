var commonTable ={
		myPageSize:10,
		tableQueryParams:{},
		loadDateURI:"",
		updateURI:"",
		insertURI:"",
		batchUpdateValidURI:"",
		batchDeleteURI:"",
		recordIdKey:"id",
		title:"",
		toolbar:'#table_tb',
		columns:[],
		onLoadSuccess:function(){},
		onBeforeRefresh:function(pageNumber, pageSize){},
		pageNum:1,
		fitColumns:true,
		fit:true,
		draggable:true,
		pagination:true,
		rownumbers:true,
		nowrap:true,
		init:function(){
			$("#html_table").datagrid({
				title:this.title,
				loadMsg:"加载中....",
				url	   :	this.loadDateURI,
				fitColumns:this.fitColumns,
				fit:this.fit,
				nowrap:this.nowrap,
				queryParams : this.tableQueryParams,
				pageNumber: this.pageNum,
				toolbar:this.toolbar,
				idField:this.recordIdKey,
				pagination:this.pagination,
				rownumbers:this.rownumbers,
				columns:[this.columns],
				onLoadSuccess:function(){
					commonTable.onLoadSuccess();
				}
			});
			var p = $('#html_table').datagrid('getPager');
			p.pagination({
						beforePageText : "页码",
						afterPageText : '共 {pages} 页',
						displayMsg: '第 {from} 到 {to} 共 {total} 条记录',
					    onBeforeRefresh : function(){
					    	commonTable.onBeforeRefresh();
					    }
					});
		},
		//add window
		beforeInitAddWindow:function(){},
		initAddWindow:function(){
			this.beforeInitAddWindow();
			$("#edit_form").attr("action",commonTable.insertURI);
			$("#htm_edit").panel({title:"添加"});
			$("#edit_form .opt_btn").hide();
			$("#edit_form .loading").show();
			$("#htm_edit").window('open');
			$("#edit_form .opt_btn").show();
			$("#edit_form .loading").hide();
		},
		
		//update window
		beforeInitUpdateWindow:function(){},
		initUpdateWindow:function(index){
			this.beforeInitUpdateWindow(index);
			$("#edit_form").attr("action",commonTable.updateURI);
			$("#htm_edit").panel({title:"修改"});
			$("#edit_form .opt_btn").hide();
			$("#edit_form .loading").show();
			$("#htm_edit").window('open');
			$("#edit_form .opt_btn").show();
			$("#edit_form .loading").hide();
		},
		
		//save
		beforeSave:function(){return true;},
		afterSave:function(result){
			$("#edit_form .opt_btn").show();
			$("#edit_form .loading").hide();
			if ( result['resultCode'] == 0 ) {
				$("#html_table").datagrid("reload");
				$('#htm_edit').window('close');
			} else {
				$.messager.alert('提示',result['msg']);
			}
		},
		save:function(){
			var b = this.beforeSave();
			if(b == false){
				return;
			}
			$("#edit_form .opt_btn").hide();
			$("#edit_form .loading").show();
			$.post($("#edit_form").attr("action"),$("#edit_form").serialize(),function(result){
				commonTable.afterSave(result);
			},"json");
		},
		
		//batchDelete
		batchDelete:function(){
			var rows = $('#html_table').datagrid('getSelections');	
			if(isSelected(rows)){
				$.messager.confirm('操作记录', '您确定要彻底删除已选中的记录?', function(r){ 	
					if(r){				
						var ids = [];
						for(var i=0;i<rows.length;i+=1){		
							ids.push(rows[i][commonTable.recordIdKey]);	
							rowIndex = $('#html_table').datagrid('getRowIndex',rows[i]);				
						}	
						$('#html_table').datagrid('clearSelections'); //清除所有已选择的记录，避免重复提交id值	
						$('#html_table').datagrid('loading');
						$.post(commonTable.batchDeleteURI + ids,function(result){
							$('#html_table').datagrid('loaded');
							if(result['resultCode'] == 0) {
								$.messager.alert('提示',"成功" + ids.length + "条记录！");
								$("#html_table").datagrid("reload");
							} else {
								$.messager.alert('提示',result['msg']);
							}
							return false;
						});	
					}	
				});		
			}
		},
		
		//batchUpdateValid
		updateTrueTipsMsg:"确定要生效？",
		updateTrueSuccessTipsMsg:"成功生效",
		updateFalseSuccessTipsMsg:"成功失效",
		updateFalseTipsMsg:"确定要失效？",
		publishTipsMsg:"确定哟发布？",
		publishSuccessTipsMsg:"成功发布",
		cancelPublishTipsMsg:"确定撤销发布？",
		cancelPublishSuccessTipsMsg:"成功撤销",
		batchUpdateValid:function(valid,tipsMsg,successTipsMsg){
			var rows = $('#html_table').datagrid('getSelections');	
			if(isSelected(rows)){
				$.messager.confirm('操作记录', tipsMsg, function(r){ 	
					if(r){				
						var ids = [];
						for(var i=0;i<rows.length;i+=1){		
							ids.push(rows[i][commonTable.recordIdKey]);	
							rowIndex = $('#html_table').datagrid('getRowIndex',rows[i]);				
						}	
						$('#html_table').datagrid('clearSelections'); //清除所有已选择的记录，避免重复提交id值	
						$('#html_table').datagrid('loading');
						$.post(commonTable.batchUpdateValidURI + ids,{'valid':valid},function(result){
							$('#html_table').datagrid('loaded');
							if(result['resultCode'] == 0) {
								$.messager.alert('提示',successTipsMsg + ids.length + "条记录！");
								$("#html_table").datagrid("reload");
							} else {
								$.messager.alert('提示',result['msg']);
							}
							return false;
						});	
					}	
				});		
			}
		},
		batchUpdateValidTrue:function(){
			this.batchUpdateValid(1, this.updateTrueTipsMsg, this.updateTrueSuccessTipsMsg);
		},
		batchUpdateValidFalse:function(){
			this.batchUpdateValid(0, this.updateFalseTipsMsg, this.updateFalseSuccessTipsMsg);
		},
		batchPublish:function(){
			this.batchUpdateValid(1, this.publishTipsMsg, this.publishSuccessTipsMsg);
		},
		batchCancelPublish:function(){
			this.batchUpdateValid(0, this.cancelPublishTipsMsg, this.cancelPublishSuccessTipsMsg);
		},
		
		// define window
		addWindowCloseCallBack:function(){	},
		defineAddWindow:function(width,height){
			$("#htm_edit").window({
				title : '添加',
				modal : true,
				width : width,
				height : height,
				shadow : false,
				closed : true,
				minimizable : false,
				maximizable : false,
				collapsible : false,
				draggable : true,
				iconCls : 'icon-add',
				resizable : false,
				onClose : function(){
					$("#htm_edit .clear-input").val('');
					$("#htm_edit .clear-textbox").textbox('setValue','');
					$("#htm_edit .clear-numberbox").numberbox('setValue',1);
					$("#htm_edit .clear-combobox").combobox('setValue',1);
					commonTable.addWindowCloseCallBack();
				},
			});
		}
			
		
};
var createTimeColumn = {field:'createTime',title:'创建时间',align:'center',
		formatter:function(value,row,index){
			return dateTools.LongTimeToDateString(value);		
		}
	},
	updateTimeColumn = {field:'updateTime',title:'更新时间',align:'center',
		formatter:function(value,row,index){
			return dateTools.LongTimeToDateString(value);		
		}
	},
	publishTimeColumn = {field:'publishTime',title:'发布时间',align:'center',
			formatter:function(value,row,index){
				return dateTools.LongTimeToDateString(value);		
			}
		},
	validColumn = {field:'valid',title: '有效性',align:'center',
		formatter: function(value,row,index){
			if(value == 1) {
					img = "/resources/3rd/easyUI/themes/icons/ok.png";
					return "<img title='有效' class='htm_column_img'  src='" + img + "'/>";
				}
				img = "/resources/3rd/easyUI/themes/icons/tip.png";
				return "<img title='无效' class='htm_column_img' src='" + img + "'/>";
			}
	},
	publishColumn = {field:'valid',title: '发布状态',align:'center',
			formatter: function(value,row,index){
				if(value == 1) {
						img = "/resources/3rd/easyUI/themes/icons/ok.png";
						return "<img title='已发布' class='htm_column_img'  src='" + img + "'/>";
					}
					img = "/resources/3rd/easyUI/themes/icons/tip.png";
					return "<img title='未发布' class='htm_column_img' src='" + img + "'/>";
				}
		};

/**
 * 判断是否选中要删除的记录
 */
function isSelected(rows) {
	if(rows.length > 0){
		return true;
	}else{
		$.messager.alert('操作失败','请先选择记录，再执行操作!','error');
		return false;
	}
}

/**
 * 显示载入提示
 */
function showPageLoading() {
	var $loading = $("<div></div>");
	$loading.text('载入中...').addClass('page_loading_tip');
	$("body").append($loading);
}

/**
 * 移除载入提示
 */
function removePageLoading() {
	$(".page_loading_tip").remove();
	$("#main-box").removeClass("none");
}

	