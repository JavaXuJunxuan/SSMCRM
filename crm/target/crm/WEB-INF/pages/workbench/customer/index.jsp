<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%
String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath() + "/";
%>
<!DOCTYPE html>
<html>
<head>
	<base href="<%=basePath%>">
	<meta charset="UTF-8">

<link type="text/css" rel="stylesheet" href="jquery/bootstrap_3.3.0/css/bootstrap.min.css"/>
<link type="text/css" rel="stylesheet" href="jquery/bootstrap-datetimepicker-master/css/bootstrap-datetimepicker.min.css"/>
<link rel="stylesheet" type="text/css" href="jquery/bs_pagination-master/css/jquery.bs_pagination.min.css"/>

<script type="text/javascript" src="jquery/jquery-1.11.1-min.js"></script>
<script type="text/javascript" src="jquery/bootstrap_3.3.0/js/bootstrap.min.js"></script>
<script type="text/javascript" src="jquery/bootstrap-datetimepicker-master/js/bootstrap-datetimepicker.min.js"></script>
<script type="text/javascript" src="jquery/bootstrap-datetimepicker-master/locale/bootstrap-datetimepicker.zh-CN.js"></script>
<script type="text/javascript" src="jquery/bs_pagination-master/js/jquery.bs_pagination.min.js"></script>
<script type="text/javascript" src="jquery/bs_pagination-master/localization/en.js"></script>

<script type="text/javascript">

	$(function(){
		$(".mydate").datetimepicker({
			language:'zh-CN', //语言
			format:'yyyy-mm-dd',//日期的格式
			minView:'month', //可以选择的最小视图
			initialDate:new Date(),//初始化显示的日期
			autoclose:true,//设置选择完日期或者时间之后，日否自动关闭日历
			todayBtn:true,//设置是否显示"今天"按钮,默认是false
			clearBtn:true//设置是否显示"清空"按钮，默认是false
		});
		//定制字段
		$("#definedColumns > li").click(function(e) {
			//防止下拉菜单消失
	        e.stopPropagation();
	    });
		queryCustomersByConditionForPage(1,10);
		//打开创建客户的模态窗口
		$("#createCustomerBtn").click(function (){
			$.ajax({
				url:"workbench/customer/queryAllUsers.do",
				type:"get",
				dataType:"json",
				success:function (data){
					var html = "";
					$.each(data,function (){
						html += "<option value = "+this.id+">"+this.name+"</option>";
					})
					$("#create-customerOwner").html(html);
					$("#createCustomerModal").modal("show");
				}
			})
		});
		//保存创建的客户信息
		$("#saveCreateCustomerBtn").click(function (){
			var owner = $("#create-customerOwner").val();
			var name = $.trim($("#create-customerName").val());
			var website = $.trim($("#create-website").val());
			var phone = $.trim($("#create-phone").val());
			var contact_summary = $.trim($("#create-contactSummary").val());
			var next_contact_time = $("#create-nextContactTime").val();
			var description = $.trim($("#create-description").val());
			var address = $.trim($("#create-address").val());
			if (name == ""){
				alert("名称不能为空");
				return;
			}
			$.ajax({
				url: "workbench/customer/saveCreateCustomer.do",
				type: "post",
				data: {
					owner:owner,
					name:name,
					website:website,
					phone:phone,
					contactSummary:contact_summary,
					nextContactTime:next_contact_time,
					description:description,
					address:address
				},
				dataType: "json",
				success: function (data){
					if (data.code == "1"){
						$("#createCustomerModal").modal("hide");
						//刷新列表
						queryCustomersByConditionForPage(1,$("#demo_pag1").bs_pagination('getOption', 'rowsPerPage'))
						$("#createCustomerForm")[0].reset();
					}else {
						alert(data.message)
						$("#createCustomerModal").modal("show");
					}

				}
			})
		});
		//给查询按钮绑定单击事件，进而进行条件查询
		$("#queryCustomerByConditionBtn").click(function (){
			queryCustomersByConditionForPage(1,$("#demo_pag1").bs_pagination('getOption', 'rowsPerPage'));
		})
		//给全选按钮添加单击事件
		$("#checkAll").click(function (){
			$("#tBody input[type='checkbox']").prop("checked",this.checked);
		})
		//如果列表中的所有checkbox都选中，则"全选"按钮也选中
		$("#tBody").on("click","input[type='checkbox']",function (){

			if ($("#tBody input[type='checkbox']").size() == $("#tBody input[type='checkbox']:checked").size()){
				$("#checkAll").prop("checked",true);
			}else {
				$("#checkAll").prop("checked",false);
			}
		})
        //打开修改客户的模态窗口
        $("#editCustomerBtn").click(function (){
            var checkedId = $("#tBody input[type='checkbox']:checked");
            if (checkedId.size() == 0){
                alert("请先选择要修改的客户");
                return;
            }
            if (checkedId.size() > 1){
                alert("每次只能修改一个客户");
                return;
            }
            var id = checkedId.val();
            $.ajax({
                url:"workbench/customer/queryAllUsersAndCustomerById.do",
                type:"get",
				data:{
                	id:id
				},
                dataType:"json",
                success:function (data){
                    var html = "";
                    $.each(data.userList,function (){
                        html += "<option value = "+this.id+">"+this.name+"</option>";
                    })
					$("#edit-customerOwner").html(html);
					$("#edit-id").val(data.customer.id);
					$("#edit-customerOwner").val(data.customer.owner);
					$("#edit-customerName").val(data.customer.name);
					$("#edit-website").val(data.customer.website);
					$("#edit-phone").val(data.customer.phone);
					$("#edit-description").val(data.customer.description);
					$("#edit-contactSummary").val(data.customer.contactSummary);
					$("#edit-nextContactTime").val(data.customer.nextContactTime);
					$("#edit-address").val(data.customer.address);
                    $("#editCustomerModal").modal("show");
                }
            })
        })
        //保存修改的客户信息
        $("#saveEditCustomerBtn").click(function (){
			var id = $("#edit-id").val();
			var owner = $("#edit-customerOwner").val();
			var name = $.trim($("#edit-customerName").val());
			var website = $.trim($("#edit-website").val());
			var phone = $.trim($("#edit-phone").val());
			var description = $.trim($("#edit-description").val());
			var contactSummary = $.trim($("#edit-contactSummary").val());
			var nextContactTime = $.trim($("#edit-nextContactTime").val());
			var address = $.trim($("#edit-address").val());
            if (owner == ""){
                alert("所有者不能为空");
                return;
            }
            if (name == ""){
                alert("名称不能为空");
            }
            $.ajax({
                url:"workbench/customer/saveEditCustomer.do",
                type:"post",
                data:{
                    id:id,
                    owner:owner,
                    name:name,
					website:website,
					phone:phone,
                    description:description,
					contactSummary:contactSummary,
					nextContactTime:nextContactTime,
					address:address
                },
                dataType:"json",
                success:function (data){
                    if (data.code == "1"){
                        $("#editCustomerModal").modal("hide");
                        queryCustomersByConditionForPage($("#demo_pag1").bs_pagination('getOption', 'currentPage'),$("#demo_pag1").bs_pagination('getOption', 'rowsPerPage'));
                    }else {
                        alert(data.message);
                        $("#editCustomerModal").modal("show");
                    }
                }
            })
        })
		//删除客户
		$("#deleteCustomerBtn").click(function (){
			var checkIds = $("#tBody input[type='checkbox']:checked");
			if (checkIds.size() == 0){
				alert("请先选择要删除的客户");
				return;
			}
			if (window.confirm("确定要删除吗")){
				var ids = "";
				$.each(checkIds,function (){
					ids += "id=" + this.value + "&";
				})
				ids = ids.substring(0,ids.length - 1);
				$.ajax({
					url:"workbench/customer/deleteCustomerByIds.do",
					dataType:"json",
					type:"post",
					data:ids,
					success:function (data){
						if (data.code == "1"){
							queryCustomersByConditionForPage(1,$("#demo_pag1").bs_pagination('getOption', 'rowsPerPage'));
						}else {
							alert(data.message);
						}
					}
				})
			}
		})
	})

	//根据条件查找客户
	function queryCustomersByConditionForPage(pageNo, pageSize){
		var name = $.trim($("#query-name").val());
		var owner = $.trim($("#query-owner").val());
		var phone = $.trim($("#query-phone").val());
		var website = $.trim($("#query-website").val());
		var pageNo = pageNo;
		var pageSize = pageSize;

		$.ajax({
			url:"workbench/customer/queryCustomersByConditionForPage.do",
			type:"post",
			data:{
				name:name,
				owner:owner,
				phone:phone,
				website:website,
				pageNo:pageNo,
				pageSize:pageSize
			},
			dataType:"json",
			success:function (data){
				var html = "";
				$.each(data.customerList,function (index,obj){
					html += "<tr>";
					html += "	<td><input type=\"checkbox\" value = \""+obj.id+"\"/></td>";
					html += "	<td><a style=\"text-decoration: none; cursor: pointer;\" onclick=\"window.location.href='workbench/customer/detail.do?id="+obj.id+"';\">"+obj.name+"</a></td>";
					html += "	<td>"+obj.owner+"</td>";
					html += "	<td>"+obj.phone+"</td>";
					html += "	<td>"+obj.website+"</td>";
					html += "</tr>";
				})
				$("#tBody").html(html);
				//取消"全选"按钮
				$("#checkAll").prop("checked",false);
				//计算总页数
				var totalPages = 1;
				if(data.totalRows % pageSize == 0){
					totalPages = data.totalRows / pageSize;
				}else{
					totalPages = parseInt(data.totalRows / pageSize) + 1;
				}
				$("#demo_pag1").bs_pagination({
					currentPage:pageNo,//当前页号,相当于pageNo
					rowsPerPage:pageSize,//每页显示条数,相当于pageSize
					totalRows:data.totalRows,//总条数
					totalPages:totalPages,//总页数,必填参数.
					visiblePageLinks:5,//最多可以显示的卡片数
					showGoToPage:true,//是否显示"跳转到"部分,默认true--显示
					showRowsPerPage:true,//是否显示"每页显示条数"部分。默认true--显示
					showRowsInfo:true,//是否显示记录的信息，默认true--显示
					//用户每次切换页号，都自动触发本函数;
					//每次返回切换页号之后的pageNo和pageSize
					onChangePage: function(event,pageObj) { // returns page_num and rows_per_page after a link has clicked
						queryCustomersByConditionForPage(pageObj.currentPage,pageObj.rowsPerPage);
					}
				});
			}
		})
	}
	
</script>
</head>
<body>

	<!-- 创建客户的模态窗口 -->
	<div class="modal fade" id="createCustomerModal" role="dialog">
		<div class="modal-dialog" role="document" style="width: 85%;">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal">
						<span aria-hidden="true">×</span>
					</button>
					<h4 class="modal-title" id="myModalLabel1">创建客户</h4>
				</div>
				<div class="modal-body">
					<form id="createCustomerForm" class="form-horizontal" role="form">
					
						<div class="form-group">
							<label for="create-customerOwner" class="col-sm-2 control-label">所有者<span style="font-size: 15px; color: red;">*</span></label>
							<div class="col-sm-10" style="width: 300px;">
								<select class="form-control" id="create-customerOwner">
								  <%--<option>zhangsan</option>
								  <option>lisi</option>
								  <option>wangwu</option>--%>
								</select>
							</div>
							<label for="create-customerName" class="col-sm-2 control-label">名称<span style="font-size: 15px; color: red;">*</span></label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control" id="create-customerName">
							</div>
						</div>
						
						<div class="form-group">
                            <label for="create-website" class="col-sm-2 control-label">公司网站</label>
                            <div class="col-sm-10" style="width: 300px;">
                                <input type="text" class="form-control" id="create-website">
                            </div>
							<label for="create-phone" class="col-sm-2 control-label">公司座机</label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control" id="create-phone">
							</div>
						</div>
						<div class="form-group">
							<label for="create-description" class="col-sm-2 control-label">描述</label>
							<div class="col-sm-10" style="width: 81%;">
								<textarea class="form-control" rows="3" id="create-description"></textarea>
							</div>
						</div>
						<div style="height: 1px; width: 103%; background-color: #D5D5D5; left: -13px; position: relative;"></div>

                        <div style="position: relative;top: 15px;">
                            <div class="form-group">
                                <label for="create-contactSummary" class="col-sm-2 control-label">联系纪要</label>
                                <div class="col-sm-10" style="width: 81%;">
                                    <textarea class="form-control" rows="3" id="create-contactSummary"></textarea>
                                </div>
                            </div>
                            <div class="form-group">
                                <label for="create-nextContactTime" class="col-sm-2 control-label">下次联系时间</label>
                                <div class="col-sm-10" style="width: 300px;">
                                    <input type="text" class="form-control mydate" id="create-nextContactTime" readonly>
                                </div>
                            </div>
                        </div>

                        <div style="height: 1px; width: 103%; background-color: #D5D5D5; left: -13px; position: relative; top : 10px;"></div>

                        <div style="position: relative;top: 20px;">
                            <div class="form-group">
                                <label for="create-address" class="col-sm-2 control-label">详细地址</label>
                                <div class="col-sm-10" style="width: 81%;">
                                    <textarea class="form-control" rows="1" id="create-address"></textarea>
                                </div>
                            </div>
                        </div>
					</form>
					
				</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
					<button type="button" class="btn btn-primary" id="saveCreateCustomerBtn">保存</button>
				</div>
			</div>
		</div>
	</div>
	
	<!-- 修改客户的模态窗口 -->
	<div class="modal fade" id="editCustomerModal" role="dialog">
		<div class="modal-dialog" role="document" style="width: 85%;">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal">
						<span aria-hidden="true">×</span>
					</button>
					<h4 class="modal-title" id="myModalLabel">修改客户</h4>
				</div>
				<div class="modal-body">
					<form class="form-horizontal" role="form">
                        <input type="hidden" id="edit-id">
						<div class="form-group">
							<label for="edit-customerOwner" class="col-sm-2 control-label">所有者<span style="font-size: 15px; color: red;">*</span></label>
							<div class="col-sm-10" style="width: 300px;">
								<select class="form-control" id="edit-customerOwner">
								  <%--<option>zhangsan</option>
								  <option>lisi</option>
								  <option>wangwu</option>--%>
								</select>
							</div>
							<label for="edit-customerName" class="col-sm-2 control-label">名称<span style="font-size: 15px; color: red;">*</span></label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control" id="edit-customerName">
							</div>
						</div>
						
						<div class="form-group">
                            <label for="edit-website" class="col-sm-2 control-label">公司网站</label>
                            <div class="col-sm-10" style="width: 300px;">
                                <input type="text" class="form-control" id="edit-website">
                            </div>
							<label for="edit-phone" class="col-sm-2 control-label">公司座机</label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control" id="edit-phone">
							</div>
						</div>
						
						<div class="form-group">
							<label for="edit-description" class="col-sm-2 control-label">描述</label>
							<div class="col-sm-10" style="width: 81%;">
								<textarea class="form-control" rows="3" id="edit-description"></textarea>
							</div>
						</div>
						
						<div style="height: 1px; width: 103%; background-color: #D5D5D5; left: -13px; position: relative;"></div>

                        <div style="position: relative;top: 15px;">
                            <div class="form-group">
                                <label for="edit-contactSummary" class="col-sm-2 control-label">联系纪要</label>
                                <div class="col-sm-10" style="width: 81%;">
                                    <textarea class="form-control" rows="3" id="edit-contactSummary"></textarea>
                                </div>
                            </div>
                            <div class="form-group">
                                <label for="edit-nextContactTime" class="col-sm-2 control-label">下次联系时间</label>
                                <div class="col-sm-10" style="width: 300px;">
                                    <input type="text" class="form-control mydate" id="edit-nextContactTime" readonly>
                                </div>
                            </div>
                        </div>

                        <div style="height: 1px; width: 103%; background-color: #D5D5D5; left: -13px; position: relative; top : 10px;"></div>

                        <div style="position: relative;top: 20px;">
                            <div class="form-group">
                                <label for="edit-address" class="col-sm-2 control-label">详细地址</label>
                                <div class="col-sm-10" style="width: 81%;">
                                    <textarea class="form-control" rows="1" id="edit-address">北京大兴大族企业湾</textarea>
                                </div>
                            </div>
                        </div>
					</form>
					
				</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
					<button type="button" class="btn btn-primary" id="saveEditCustomerBtn">更新</button>
				</div>
			</div>
		</div>
	</div>
	
	
	
	
	<div>
		<div style="position: relative; left: 10px; top: -10px;">
			<div class="page-header">
				<h3>客户列表</h3>
			</div>
		</div>
	</div>
	
	<div style="position: relative; top: -20px; left: 0px; width: 100%; height: 100%;">
	
		<div style="width: 100%; position: absolute;top: 5px; left: 10px;">
		
			<div class="btn-toolbar" role="toolbar" style="height: 80px;">
				<form class="form-inline" role="form" style="position: relative;top: 8%; left: 5px;">
				  
				  <div class="form-group">
				    <div class="input-group">
				      <div class="input-group-addon">名称</div>
				      <input id="query-name" class="form-control" type="text">
				    </div>
				  </div>
				  
				  <div class="form-group">
				    <div class="input-group">
				      <div class="input-group-addon">所有者</div>
				      <input id="query-owner" class="form-control" type="text">
				    </div>
				  </div>
				  
				  <div class="form-group">
				    <div class="input-group">
				      <div class="input-group-addon">公司座机</div>
				      <input id="query-phone" class="form-control" type="text">
				    </div>
				  </div>
				  
				  <div class="form-group">
				    <div class="input-group">
				      <div class="input-group-addon">公司网站</div>
				      <input id="query-website" class="form-control" type="text">
				    </div>
				  </div>
				  
				  <button id="queryCustomerByConditionBtn" type="button" class="btn btn-default">查询</button>
				  
				</form>
			</div>
			<div class="btn-toolbar" role="toolbar" style="background-color: #F7F7F7; height: 50px; position: relative;top: 5px;">
				<div class="btn-group" style="position: relative; top: 18%;">
				  <button type="button" class="btn btn-primary" id="createCustomerBtn"><span class="glyphicon glyphicon-plus"></span> 创建</button>
				  <button type="button" class="btn btn-default" id="editCustomerBtn"><span class="glyphicon glyphicon-pencil"></span> 修改</button>
				  <button type="button" class="btn btn-danger" id="deleteCustomerBtn"><span class="glyphicon glyphicon-minus"></span> 删除</button>
				</div>
				
			</div>
			<div style="position: relative;top: 10px;">
				<table class="table table-hover">
					<thead>
						<tr style="color: #B3B3B3;">
							<td><input type="checkbox" id="checkAll"/></td>
							<td>名称</td>
							<td>所有者</td>
							<td>公司座机</td>
							<td>公司网站</td>
						</tr>
					</thead>
					<tbody id="tBody">
						<%--<tr>
							<td><input type="checkbox" /></td>
							<td><a style="text-decoration: none; cursor: pointer;" onclick="window.location.href='detail.jsp';">动力节点</a></td>
							<td>zhangsan</td>
							<td>010-84846003</td>
							<td>http://www.bjpowernode.com</td>
						</tr>--%>
					</tbody>
				</table>
				<div id="demo_pag1"></div>
			</div>
			<%--<div style="height: 50px; position: relative;top: 30px;">
				<div>
					<button type="button" class="btn btn-default" style="cursor: default;">共<b>50</b>条记录</button>
				</div>
				<div class="btn-group" style="position: relative;top: -34px; left: 110px;">
					<button type="button" class="btn btn-default" style="cursor: default;">显示</button>
					<div class="btn-group">
						<button type="button" class="btn btn-default dropdown-toggle" data-toggle="dropdown">
							10
							<span class="caret"></span>
						</button>
						<ul class="dropdown-menu" role="menu">
							<li><a href="#">20</a></li>
							<li><a href="#">30</a></li>
						</ul>
					</div>
					<button type="button" class="btn btn-default" style="cursor: default;">条/页</button>
				</div>
				<div style="position: relative;top: -88px; left: 285px;">
					<nav>
						<ul class="pagination">
							<li class="disabled"><a href="#">首页</a></li>
							<li class="disabled"><a href="#">上一页</a></li>
							<li class="active"><a href="#">1</a></li>
							<li><a href="#">2</a></li>
							<li><a href="#">3</a></li>
							<li><a href="#">4</a></li>
							<li><a href="#">5</a></li>
							<li><a href="#">下一页</a></li>
							<li class="disabled"><a href="#">末页</a></li>
						</ul>
					</nav>
				</div>
			</div>--%>
		</div>
		
	</div>
</body>
</html>