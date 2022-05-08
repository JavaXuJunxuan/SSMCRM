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

<link href="jquery/bootstrap_3.3.0/css/bootstrap.min.css" type="text/css" rel="stylesheet" />
<link type="text/css" rel="stylesheet" href="jquery/bootstrap-datetimepicker-master/css/bootstrap-datetimepicker.min.css"/>
<script type="text/javascript" src="jquery/jquery-1.11.1-min.js"></script>
<script type="text/javascript" src="jquery/bootstrap_3.3.0/js/bootstrap.min.js"></script>
<script type="text/javascript" src="jquery/bootstrap-datetimepicker-master/js/bootstrap-datetimepicker.min.js"></script>
<script type="text/javascript" src="jquery/bootstrap-datetimepicker-master/locale/bootstrap-datetimepicker.zh-CN.js"></script>

<script type="text/javascript">

	//默认情况下取消和保存按钮是隐藏的
	var cancelAndSaveBtnDefault = true;

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

		$("#remark").focus(function(){
			if(cancelAndSaveBtnDefault){
				//设置remarkDiv的高度为130px
				$("#remarkDiv").css("height","130px");
				//显示
				$("#cancelAndSaveBtn").show("2000");
				cancelAndSaveBtnDefault = false;
			}
		});
		
		$("#cancelBtn").click(function(){
			//显示
			$("#cancelAndSaveBtn").hide();
			//设置remarkDiv的高度为130px
			$("#remarkDiv").css("height","90px");
			cancelAndSaveBtnDefault = true;
		});

		$("#remarkDivList").on("mouseover",".remarkDiv",function (){
		    $(this).children("div").children("div").show();
        })

        $("#remarkDivList").on("mouseout",".remarkDiv",function (){
            $(this).children("div").children("div").hide();
        })

        $("#remarkDivList").on("mouseover",".myHref",function (){
            $(this).children("span").css("color","red");
        })

        $("#remarkDivList").on("mouseout",".myHref",function (){
            $(this).children("span").css("color","#E6E6E6");
        })

		//给所有删除交易的超链接绑定单击事件
		$("#tTbody").on("click","a[name='deleteT']",function (){
            $("#tid").val($(this).attr("Tid"));
            $("#removeTransactionModal").modal("show");
        })
        //给删除交易的模态窗口的删除按钮绑定单击事件
		$("#deleteTransactionBtn").click(function (){
            var id = $("#tid").val();
            $.ajax({
                url:"workbench/transaction/deleteTransaction.do",
                type:"post",
                data:{
                    id:id
                },
                dataType:"json",
                success:function (data){
                    if (data.code == "1"){
                        $("#removeTransactionModal").modal("hide");
                        $("#tr_"+id).remove();
                    }else {
                        alert(data.message);
                        $("#removeTransactionModal").modal("show");
                    }
                }
            })
        });
        //给客户的备注信息的编辑图标绑定单击事件
        $("#rDiv").on("click","a[name='editR']",function (){
            var id = $(this).attr("remarkId");
            var noteContent = $("#h5_"+id).text();
            $("#edit-id").val(id);
            $("#edit-noteContent").val(noteContent);
            $("#editRemarkModal").modal("show");
        });
        //更新按钮绑定单击事件进行备注更新操作
        $("#updateRemarkBtn").click(function (){
            var id = $("#edit-id").val();
            var noteContent = $("#edit-noteContent").val();
            if (noteContent == ""){
                alert("备注内容不可以为空");
                return;
            }
            $.ajax({
                url:"workbench/customerRemark/editRemark.do",
                type: "post",
                data: {
                    id:id,
                    noteContent:noteContent
                },
                dataType: "json",
                success:function (data){
                    if(data.code == "1"){
                        $("#editRemarkModal").modal("hide");
                        $("#h5_" + data.rtn.id).text(data.rtn.noteContent);
						$("#div_" + data.rtn.id + " small").text(" "+data.rtn.editTime+" 由${sessionScope.sessionUser.name}修改")
					}else {
                        alert(data.message);
                        $("#editRemarkModal").modal("show");
                    }
                }
            })
        })
		//给客户的备注信息的删除图标绑定单击事件
		$("#rDiv").on("click","a[name='deleteR']",function (){
			if (confirm("确认删除备注信息吗?")){
				var id = $(this).attr("remarkId");
				$.ajax({
					url:"workbench/customerRemark/deleteRemark.do",
					type:"post",
					data:{
						id:id
					},
					dataType:"json",
					success:function (data){
						if (data.code == "1"){
							$("#div_"+id).remove();
						}else {
							alert(data.message);
						}
					}
				})
			}
		})
        //给备注的保存按钮添加事件进行创建新备注信息
		$("#saveCreateCustomerRemarkBtn").click(function (){
			var noteContent = $("#remark").val();
			$.ajax({
				url:"workbench/customerRemark/saveCreateCustomerRemark.do",
				type:"post",
				data:{
					noteContent:noteContent,
					customerId:"${customer.id}"
				},
				dataType:"json",
				success:function (data){
					if (data.code == "1"){
						$("#remark").val("");
						var html = "";
						html +=	"<div class=\"remarkDiv\" id=\"div_"+data.rtn.id+"\" style=\"height: 60px;\">";
		    			html +=		"<img title=\""+data.rtn.createBy+"\" src=\"image/user-thumbnail.png\" style=\"width: 30px; height:30px;\">";
		    			html +=		"<div style=\"position: relative; top: -40px; left: 40px;\" >";
		    			html +=			"<h5 id=\"h5_"+data.rtn.id+"\">"+data.rtn.noteContent+"</h5>";
		    			html +=			"<font color=\"gray\">客户</font> <font color=\"gray\">-</font> <b>${customer.name}</b> <small style=\"color: gray;\">"+data.rtn.createTime+"由${sessionScope.sessionUser.name}创建</small>";
		    			html +=				"<div style=\"position: relative; left: 500px; top: -30px; height: 30px; width: 100px; display: none;\">";
		    			html +=					"<a class=\"myHref\" name=\"editR\" remarkId=\""+data.rtn.id+"\" href=\"javascript:void(0);\"><span class=\"glyphicon glyphicon-edit\" style=\"font-size: 20px; color: #E6E6E6;\"></span></a>";
		    			html +=					"&nbsp;&nbsp;&nbsp;&nbsp;";
		    			html +=					"<a class=\"myHref\" name=\"deleteR\" remarkId=\""+data.rtn.id+"\" href=\"javascript:void(0);\"><span class=\"glyphicon glyphicon-remove\" style=\"font-size: 20px; color: #E6E6E6;\"></span></a>";
		    			html +=				"</div>";
		    			html +=		"</div>";
		    			html +=	"</div>";
						$("#rDiv").append(html);
					}else {
						alert(data.message);
					}
				}
			})
		})
		//给新建联系人的保存按钮添加单击事件进行创建新联系人
		$("#saveCreateContactBtn").click(function (){
			var owner = $("#create-contactsOwner").val();
			var source = $("#create-clueSource").val();
			var fullname = $("#create-surname").val();
			var appellation = $("#create-call").val();
			var job = $("#create-job").val();
			var mphone = $("#create-mphone").val();
			var email = $("#create-email").val();
			var birthday = $("#create-birth").val();
			var customerId = $("#create-customerName").val();
			var description = $("#create-describe").val();
			var contactSummary = $("#create-contactSummary").val();
			var nextContactTime = $("#create-nextContactTime").val();
			var address = $("#create-address").val();
			if (owner == ""){
				alert("所有者不能为空");
				return;
			}
			if (name == ""){
				alert("名称不能为空");
				return;
			}
			$.ajax({
				url:"workbench/contact/saveCreateContact.do",
				type:"post",
				data:{
					owner:owner,
					source:source,
					customerId:customerId,
					fullname:fullname,
					appellation:appellation,
					email:email,
					mphone:mphone,
					job:job,
					description:description,
					contactSummary:contactSummary,
					nextContactTime:nextContactTime,
					address:address,
					birthday:birthday
				},
				dataType:"json",
				success:function (data){
					var html = "";

					if (data.code == "1"){
						html += "<tr id=\"tr_"+data.rtn.id+"\">";
						html += 	"<td><a href=\"workbench/contact/index.do?id="+data.rtn.id+" style=\"text-decoration: none;\">"+fullname+"</a></td>";
						html += 	"<td>"+email+"</td>";
						html += 	"<td>"+mphone+"</td>";
						html += 	"<td><a remarkId=\""+data.rtn.id+"\" href=\"javascript:void(0);\" style=\"text-decoration: none;\"><span class=\"glyphicon glyphicon-remove\"></span>删除</a></td>";
						html += "</tr>";
						$("#contactsTbody").append(html);
						$("#createContactsModal").modal("hide");
						$("#createContactForm")[0].reset();
					}else {
						alert(data.message);
						$("#createContactsModal").modal("show");
					}
				}
			})
		})
		//给每一个联系人的删除按钮绑定单击事件
		$("#contactsTbody").on("click","a",function (){
			var id = $(this).attr("remarkId");
			$("#removeContactsModal").modal("show");
			$("#deleteContactId").val(id);
		})
		//给删除联系人的模态窗口的删除按钮绑定事件，进行确认删除
		$("#deleteContactBtn").click(function (){
			var id = $("#deleteContactId").val();
			$.ajax({
				url:"workbench/contact/deleteContact.do",
				type:"post",
				data:{
					id:id
				},
				dataType:"json",
				success:function (data){
					if (data.code == "1"){
						$("#removeContactsModal").modal("hide");
						$("#tr_"+id).remove();
					}else {
						alert(data.message);
						$("#removeContactsModal").modal("show");
					}
				}
			})
		})
        //给编辑按钮单击单击事件进行编辑客户信息
        $("#edit-CustomerBtn").click(function (){
            var id = "${customer.id}";
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
            var id = $("#edit-cid").val();
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
                        window.location.href="workbench/customer/detail.do?id="+id;
                    }else {
                        alert(data.message);
                        $("#editCustomerModal").modal("show");
                    }
                }
            })
        })
        //删除客户
        $("#deleteCustomerBtn").click(function (){
            var id = "${customer.id}";
            if (window.confirm("确定要删除吗")){
                $.ajax({
                    url:"workbench/customer/deleteCustomerByIds.do",
                    dataType:"json",
                    type:"post",
                    data:{
                        id
                    },
                    success:function (data){
                        if (data.code == "1"){
                            window.location.href="workbench/customer/index.do";
                        }else {
                            alert(data.message);
                        }
                    }
                })
            }
        })
	});
	
</script>

</head>
<body>

	<!-- 删除联系人的模态窗口 -->
	<div class="modal fade" id="removeContactsModal" role="dialog">
		<div class="modal-dialog" role="document" style="width: 30%;">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal">
						<span aria-hidden="true">×</span>
					</button>
					<h4 class="modal-title">删除联系人</h4>
				</div>
				<div class="modal-body">
					<p>您确定要删除该联系人吗？</p>
				</div>
				<div class="modal-footer">
					<input id="deleteContactId" type="hidden" value=""></input>
					<button type="button" class="btn btn-default" data-dismiss="modal">取消</button>
					<button type="button" class="btn btn-danger" id="deleteContactBtn">删除</button>
				</div>
			</div>
		</div>
	</div>

    <!-- 删除交易的模态窗口 -->
    <div class="modal fade" id="removeTransactionModal" role="dialog">
        <div class="modal-dialog" role="document" style="width: 30%;">
            <div class="modal-content">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal">
                        <span aria-hidden="true">×</span>
                    </button>
                    <h4 class="modal-title">删除交易</h4>
                </div>
                <div class="modal-body">
                    <p>您确定要删除该交易吗？</p>
                </div>
                <input type="hidden" id="tid">
                <div class="modal-footer">
                    <button type="button" class="btn btn-default" data-dismiss="modal">取消</button>
                    <button type="button"  class="btn btn-danger" id="deleteTransactionBtn">删除</button>
                </div>
            </div>
        </div>
    </div>
	
	<!-- 创建联系人的模态窗口 -->
	<div class="modal fade" id="createContactsModal" role="dialog">
		<div class="modal-dialog" role="document" style="width: 85%;">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" onclick="$('#createContactsModal').modal('hide');">
						<span aria-hidden="true">×</span>
					</button>
					<h4 class="modal-title" id="myModalLabel1">创建联系人</h4>
				</div>
				<div class="modal-body">
					<form id="createContactForm" class="form-horizontal" role="form">
					
						<div class="form-group">
							<label for="create-contactsOwner" class="col-sm-2 control-label">所有者<span style="font-size: 15px; color: red;">*</span></label>
							<div class="col-sm-10" style="width: 300px;">
								<select class="form-control" id="create-contactsOwner">
									<c:forEach items="${userList}" var="user">
										<option value="${user.id}">${user.name}</option>
									</c:forEach>
								</select>
							</div>
							<label for="create-clueSource" class="col-sm-2 control-label">来源</label>
							<div class="col-sm-10" style="width: 300px;">
								<select class="form-control" id="create-clueSource">
								  <option></option>
								  <c:forEach items="${sourceList}" var="source">
									  <option value="${source.id}">${source.value}</option>
								  </c:forEach>
								</select>
							</div>
						</div>
						
						<div class="form-group">
							<label for="create-surname" class="col-sm-2 control-label">姓名<span style="font-size: 15px; color: red;">*</span></label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control" id="create-surname">
							</div>
							<label for="create-call" class="col-sm-2 control-label">称呼</label>
							<div class="col-sm-10" style="width: 300px;">
								<select class="form-control" id="create-call">
								  <option></option>
									<c:forEach items="${appellationList}" var="appellation">
										<option value="${appellation.id}">${appellation.value}</option>
									</c:forEach>
								</select>
							</div>
							
						</div>
						
						<div class="form-group">
							<label for="create-job" class="col-sm-2 control-label">职位</label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control" id="create-job">
							</div>
							<label for="create-mphone" class="col-sm-2 control-label">手机</label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control" id="create-mphone">
							</div>
						</div>
						
						<div class="form-group" style="position: relative;">
							<label for="create-email" class="col-sm-2 control-label">邮箱</label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control" id="create-email">
							</div>
							<label for="create-birth" class="col-sm-2 control-label">生日</label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control" id="create-birth">
							</div>
						</div>
						
						<div class="form-group" style="position: relative;">
							<label for="create-customerName" class="col-sm-2 control-label">客户名称</label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control" id="create-customerName" value="${customer.name}" readonly>
							</div>
						</div>
						
						<div class="form-group" style="position: relative;">
							<label for="create-describe" class="col-sm-2 control-label">描述</label>
							<div class="col-sm-10" style="width: 81%;">
								<textarea class="form-control" rows="3" id="create-describe"></textarea>
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
                                    <input type="text" class="form-control" id="create-nextContactTime">
                                </div>
                            </div>
                        </div>

                        <div style="height: 1px; width: 103%; background-color: #D5D5D5; left: -13px; position: relative; top : 20px;"></div>

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
					<button type="button" class="btn btn-primary" id="saveCreateContactBtn">保存</button>
				</div>
			</div>
		</div>
	</div>

    <!-- 修改客户备注的模态窗口 -->
    <div class="modal fade" id="editRemarkModal" role="dialog">
        <%-- 备注的id --%>
        <input type="hidden" id="remarkId">
        <div class="modal-dialog" role="document" style="width: 40%;">
            <div class="modal-content">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal">
                        <span aria-hidden="true">×</span>
                    </button>
                    <h4 class="modal-title" id="myModalLabel">修改备注</h4>
                </div>
                <div class="modal-body">
                    <form class="form-horizontal" role="form">
                        <input type="hidden" id="edit-id">
                        <div class="form-group">
                            <label for="edit-noteContent" class="col-sm-2 control-label">内容</label>
                            <div class="col-sm-10" style="width: 81%;">
                                <textarea class="form-control" rows="3" id="edit-noteContent"></textarea>
                            </div>
                        </div>
                    </form>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
                    <button type="button" class="btn btn-primary" id="updateRemarkBtn">更新</button>
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
                    <h4 class="modal-title" id="editCustomer">修改客户</h4>
                </div>
                <div class="modal-body">
                    <form class="form-horizontal" role="form">
                        <input id="edit-cid" type="hidden" value="${customer.id}">
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
                                    <textarea class="form-control" rows="1" id="edit-address"></textarea>
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

	<!-- 返回按钮 -->
	<div style="position: relative; top: 35px; left: 10px;">
		<a href="javascript:void(0);" onclick="window.history.back();"><span class="glyphicon glyphicon-arrow-left" style="font-size: 20px; color: #DDDDDD"></span></a>
	</div>
	
	<!-- 大标题 -->
	<div style="position: relative; left: 40px; top: -30px;">
		<div class="page-header">
			<h3>${customer.name}<small><a href="http://www.bjpowernode.com" target="_blank">${customer.website}</a></small></h3>
		</div>
		<div style="position: relative; height: 50px; width: 500px;  top: -72px; left: 700px;">
			<button id="edit-CustomerBtn" type="button" class="btn btn-default"><span class="glyphicon glyphicon-edit"></span> 编辑</button>
			<button id="deleteCustomerBtn" type="button" class="btn btn-danger"><span class="glyphicon glyphicon-minus"></span> 删除</button>
		</div>
	</div>
	
	<br/>
	<br/>
	<br/>

	<!-- 详细信息 -->
	<div style="position: relative; top: -70px;">
		<div style="position: relative; left: 40px; height: 30px;">
			<div style="width: 300px; color: gray;">所有者</div>
			<div style="width: 300px;position: relative; left: 200px; top: -20px;"><b>${customer.owner}</b></div>
			<div style="width: 300px;position: relative; left: 450px; top: -40px; color: gray;">名称</div>
			<div style="width: 300px;position: relative; left: 650px; top: -60px;"><b>${customer.name}</b></div>
			<div style="height: 1px; width: 400px; background: #D5D5D5; position: relative; top: -60px;"></div>
			<div style="height: 1px; width: 400px; background: #D5D5D5; position: relative; top: -60px; left: 450px;"></div>
		</div>
		<div style="position: relative; left: 40px; height: 30px; top: 10px;">
			<div style="width: 300px; color: gray;">公司网站</div>
			<div style="width: 300px;position: relative; left: 200px; top: -20px;"><b>${customer.website}</b></div>
			<div style="width: 300px;position: relative; left: 450px; top: -20px; color: gray;">公司座机</div>
			<div style="width: 300px;position: relative; left: 650px; top: -60px;"><b>${customer.phone}</b></div>
			<div style="height: 1px; width: 400px; background: #D5D5D5; position: relative; top: -20px;"></div>
			<div style="height: 1px; width: 400px; background: #D5D5D5; position: relative; top: -20px; left: 450px;"></div>
		</div>
		<div style="position: relative; left: 40px; height: 30px; top: 20px;">
			<div style="width: 300px; color: gray;">创建者</div>
			<div style="width: 500px;position: relative; left: 200px; top: -20px;"><b>${customer.createBy}</b><small style="font-size: 10px; color: gray;">于${customer.createTime}创建</small></div>
			<div style="height: 1px; width: 550px; background: #D5D5D5; position: relative; top: -20px;"></div>
		</div>
		<div style="position: relative; left: 40px; height: 30px; top: 30px;">
			<div style="width: 300px; color: gray;">修改者</div>
			<div style="width: 500px;position: relative; left: 200px; top: -20px;"><b>${customer.editBy}</b><small style="font-size: 10px; color: gray;">${customer.editTime}</small></div>
			<div style="height: 1px; width: 550px; background: #D5D5D5; position: relative; top:0px;"></div>
		</div>
        <div style="position: relative; left: 40px; height: 30px; top: 40px;">
            <div style="width: 300px; color: gray;">联系纪要</div>
            <div style="width: 630px;position: relative; left: 200px; top: -20px;">
                <b>
					${customer.contactSummary}
                </b>
            </div>
            <div style="height: 1px; width: 850px; background: #D5D5D5; position: relative; top: 0px;"></div>
        </div>
        <div style="position: relative; left: 40px; height: 30px; top: 50px;">
            <div style="width: 300px; color: gray;">下次联系时间</div>
            <div style="width: 300px;position: relative; left: 200px; top: -20px;"><b>${customer.nextContactTime}</b></div>
            <div style="height: 1px; width: 400px; background: #D5D5D5; position: relative; top: 0px; "></div>
        </div>
		<div style="position: relative; left: 40px; height: 30px; top: 60px;">
			<div style="width: 300px; color: gray;">描述</div>
			<div style="width: 630px;position: relative; left: 200px; top: -20px;">
				<b>
					${customer.description}
				</b>
			</div>
			<div style="height: 1px; width: 850px; background: #D5D5D5; position: relative; top: 0px;"></div>
		</div>
        <div style="position: relative; left: 40px; height: 30px; top: 70px;">
            <div style="width: 300px; color: gray;">详细地址</div>
            <div style="width: 630px;position: relative; left: 200px; top: -20px;">
                <b>
					${customer.address}
                </b>
            </div>
            <div style="height: 1px; width: 850px; background: #D5D5D5; position: relative; top: 0px;"></div>
        </div>
	</div>
	
	<!-- 备注 -->
	<div id="remarkDivList" style="position: relative; top: 10px; left: 40px;">
		<div class="page-header">
			<h4>备注</h4>
		</div>
        <div id="rDiv">
		    <c:forEach items="#{remarkList}" var="remark">
		    	<div class="remarkDiv" id="div_${remark.id}" style="height: 60px;">
		    		<img title="${remark.createBy}" src="image/user-thumbnail.png" style="width: 30px; height:30px;">
		    		<div style="position: relative; top: -40px; left: 40px;" >
		    			<h5 id="h5_${remark.id}">${remark.noteContent}</h5>
		    			<font color="gray">客户</font> <font color="gray">-</font> <b>${customer.name}</b> <small style="color: gray;">${remark.editFlag == "0"?remark.createTime:remark.editTime}由${remark.editFlag == "0"?remark.createBy:remark.editBy}${remark.editFlag == "0"?"创建":"修改"}</small>
		    			<div style="position: relative; left: 500px; top: -30px; height: 30px; width: 100px; display: none;">
		    				<a class="myHref" name="editR" remarkId="${remark.id}" href="javascript:void(0);"><span class="glyphicon glyphicon-edit" style="font-size: 20px; color: #E6E6E6;"></span></a>
		    				&nbsp;&nbsp;&nbsp;&nbsp;
		    				<a class="myHref" name="deleteR" remarkId="${remark.id}" href="javascript:void(0);"><span class="glyphicon glyphicon-remove" style="font-size: 20px; color: #E6E6E6;"></span></a>
		    			</div>
		    		</div>
		    	</div>
		    </c:forEach>
        </div>

		<div id="remarkDiv" style="background-color: #E6E6E6; width: 870px; height: 90px;">
			<form role="form" style="position: relative;top: 10px; left: 10px;">
				<textarea id="remark" class="form-control" style="width: 850px; resize : none;" rows="2"  placeholder="添加备注..."></textarea>
				<p id="cancelAndSaveBtn" style="position: relative;left: 737px; top: 10px; display: none;">
					<button id="cancelBtn" type="button" class="btn btn-default">取消</button>
					<button type="button" class="btn btn-primary" id="saveCreateCustomerRemarkBtn">保存</button>
				</p>
			</form>
		</div>
	</div>
	
	<!-- 交易 -->
	<div>
		<div style="position: relative; top: 20px; left: 40px;">
			<div class="page-header">
				<h4>交易</h4>
			</div>
			<div style="position: relative;top: 0px;">
				<table id="activityTable2" class="table table-hover" style="width: 900px;">
					<thead>
						<tr style="color: #B3B3B3;">
							<td>名称</td>
							<td>金额</td>
							<td>阶段</td>
							<td>可能性</td>
							<td>预计成交日期</td>
							<td>类型</td>
							<td></td>
						</tr>
					</thead>
					<tbody id="tTbody">
						<c:forEach items="${transactionList}" var="transaction">
							<tr id="tr_${transaction.id}">
								<td><a href="workbench/transaction/detail.do?id=${transaction.id}" style="text-decoration: none;">${transaction.name}</a></td>
								<td>${transaction.money}</td>
								<td>${transaction.stage}</td>
								<td>${transaction.possibility}</td>
								<td>${transaction.expectedDate}</td>
								<td>${transaction.type}</td>
								<td><a href="javascript:void(0);" name="deleteT" Tid = "${transaction.id}" style="text-decoration: none;"><span class="glyphicon glyphicon-remove"></span>删除</a></td>
							</tr>
						</c:forEach>
					</tbody>
				</table>
			</div>
			
			<div>
				<a href="workbench/transaction/saveTransaction.do" style="text-decoration: none;"><span class="glyphicon glyphicon-plus"></span>新建交易</a>
			</div>
		</div>
	</div>
	
	<!-- 联系人 -->
	<div>
		<div style="position: relative; top: 20px; left: 40px;">
			<div class="page-header">
				<h4>联系人</h4>
			</div>
			<div style="position: relative;top: 0px;">
				<table id="activityTable" class="table table-hover" style="width: 900px;">
					<thead>
						<tr style="color: #B3B3B3;">
							<td>名称</td>
							<td>邮箱</td>
							<td>手机</td>
							<td></td>
						</tr>
					</thead>
					<tbody id="contactsTbody">
						<c:forEach items="${contactsList}" var="contact">
							<tr id="tr_${contact.id}">
								<td><a href="workbench/contact/index.do?id=${contact.id}" style="text-decoration: none;">${contact.fullname}</a></td>
								<td>${contact.email}</td>
								<td>${contact.mphone}</td>
								<td><a remarkId="${contact.id}" href="javascript:void(0);" style="text-decoration: none;"><span class="glyphicon glyphicon-remove"></span>删除</a></td>
							</tr>
						</c:forEach>
					</tbody>

				</table>
			</div>
			<div>
				<a href="javascript:void(0);" data-toggle="modal" data-target="#createContactsModal" style="text-decoration: none;"><span class="glyphicon glyphicon-plus"></span>新建联系人</a>
			</div>
		</div>
	</div>
	
	<div style="height: 200px;"></div>
</body>
</html>