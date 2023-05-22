<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath() + "/";
%>
<html>
<head>
    <base href="<%=basePath%>">
    <meta charset="UTF-8">
    <link href="jquery/bootstrap_3.3.0/css/bootstrap.min.css" type="text/css" rel="stylesheet"/>
    <link href="jquery/bootstrap-datetimepicker-master/css/bootstrap-datetimepicker.min.css" type="text/css"
          rel="stylesheet"/>
    <link rel="stylesheet" type="text/css" href="jquery/bs_pagination-master/css/jquery.bs_pagination.min.css">

    <script type="text/javascript" src="jquery/jquery-1.11.1-min.js"></script>
    <script type="text/javascript" src="jquery/bootstrap_3.3.0/js/bootstrap.min.js"></script>
    <script type="text/javascript" src="jquery/bootstrap-datetimepicker-master/js/bootstrap-datetimepicker.js"></script>
    <script type="text/javascript"
            src="jquery/bootstrap-datetimepicker-master/locale/bootstrap-datetimepicker.zh-CN.js"></script>
    <script type="text/javascript" src="jquery/bs_pagination-master/js/jquery.bs_pagination.min.js"></script>
    <script type="text/javascript" src="jquery/bs_pagination-master/localization/en.js"></script>
    <script type="text/javascript" src="jquery/bs_typeahead/bootstrap3-typeahead.min.js"></script>
    <script type="text/javascript">
        $(function () {
            // 日期js
            $(".my-date").datetimepicker({
                language: 'zh-CN',//语言
                format: 'yyyy-mm-dd',//日期的格式
                minView: 'month',//选择最小的视图
                initialDate: new Date(),//初始化显示日期
                autoclose: true,//选择后自动关闭
                todayBtn: true,//显示今天的按钮
                clearBtn: true//清空按钮
            });
            // 1.点击市场活动源
            $("#openActivityBtn").click(function () {
                $("#findMarketActivity").modal("show");
            });
            // 2.市场活动搜索框 输入市场活动名称
            $("#searchActivityOfTranTxt").keyup(function () {
                // 收集参数
                var activityName = $("#searchActivityOfTranTxt").val();
                // 发送请求
                $.ajax({
                    url: 'workbench/transaction/searchActivity.do',
                    data: {activityName: activityName},
                    type: 'post',
                    dataType: 'json',
                    success: function (data) {
                        var htmlStr = "";
                        // 遍历获取activity
                        $.each(data, function (index, obj) {
                            htmlStr += "<tr>";
                            htmlStr += "<td><input type=\"radio\" activityName=\"" + obj.name + "\" name=\"activity\" value=\"" + obj.id + "\"/></td>";
                            htmlStr += "<td>" + obj.name + "</td>";
                            htmlStr += "<td>" + obj.startDate + "</td>";
                            htmlStr += "<td>" + obj.endDate + "</td>";
                            htmlStr += "<td>" + obj.owner + "</td>";
                            htmlStr += "</tr>";
                        });
                        $("#tBody").html(htmlStr);
                    }

                });

            });
            // 选择市场活动
            $("#tBody").on("click", "input[type='radio']", function () {
                // 选择市场的id
                var id = this.value;
                var activityName = $(this).attr("activityName");
                // 填充到添加表单上面
                $("#activityId").val(id);
                $("#create-activitySrc").val(activityName);
                // 关闭输入框
                $("#findMarketActivity").modal("hide");
            });

            // 3.联系人姓名
            $("#findContactsBtn").click(function () {
                $("#findContacts").modal("show");
            });
            // 联系人搜索框 输入fullname
            $("#searchContactsOfTranTxt").keyup(function () {
                // 收集参数
                var fullname = $("#searchContactsOfTranTxt").val();
                // 发送请求
                $.ajax({
                    url: 'workbench/transaction/searchContacts.do',
                    data: {fullname: fullname},
                    type: 'post',
                    dataType: 'json',
                    success: function (data) {
                        var htmlStr = "";
                        // 遍历获取activity
                        $.each(data, function (index, obj) {
                            htmlStr += "<tr>";
                            htmlStr += "<td><input type=\"radio\" fullname=\"" + obj.fullname + "\" name=\"activity\" value=\"" + obj.id + "\"/></td>";
                            htmlStr += "<td>" + obj.fullname + "</td>";
                            htmlStr += "<td>" + obj.email + "</td>";
                            htmlStr += "<td>" + obj.mphone + "</td>";
                            htmlStr += "</tr>";
                        });
                        $("#tBodyContacts").html(htmlStr);
                    }

                });

            });
            // 选择联系人
            $("#tBodyContacts").on("click", "input[type='radio']", function () {
                // 选择市场的id
                var id = this.value;
                var fullname = $(this).attr("fullname");
                // 填充到添加表单上面
                $("#contactsId").val(id);
                $("#create-contactsName").val(fullname);
                // 关闭输入框
                $("#findContacts").modal("hide");
            });

            //  4. 点击保存
            $("#saveTranBtn").click(function () {
                // 收集参数
                var owner = $("#create-transactionOwner").val();
                var money = $.trim($("#create-amountOfMoney").val());
                var name = $.trim($("#create-transactionName").val());
                var expectedDate = $("#create-expectedClosingDate").val();
                var customerName = $.trim($("#create-accountName").val());
                var stage = $("#create-transactionStage").val();
                var type = $("#create-transactionType").val();
                var contactsId = $("#contactsId").val();
                var source = $("#create-clueSource").val();
                var activityId = $("#activityId").val();
                var description = $.trim($("#create-describe").val());
                var contactSummary = $.trim($("#create-contactSummary").val());
                var nextContactTime = $("#create-nextContactTime").val();
                // 表单验证
                if (owner == "") {
                    alert("所有者不能为空");
                    return;
                }
                if (name == "") {
                    alert("名称不能为空");
                    return;
                }
                if (expectedDate == "") {
                    alert("预计成交日期不能为空");
                    return;
                }
                if (customerName == "") {
                    alert("客户名称不能为空");
                    return;
                }
                if (stage == "") {
                    alert("阶段不能为空");
                    return;
                }
                // 正则表达式验证成本：金额只能为非负整数
                var regExp = /^(([1-9]\d*)|0)$/;
                if (!regExp.test(money)) {
                    alert("金额要大于0");
                    return;
                }
                // 发送请求
                $.ajax({
                    url: 'workbench/transaction/saveTran.do',
                    data: {
                        owner: owner,
                        money: money,
                        name: name,
                        expectedDate: expectedDate,
                        customerName: customerName,
                        stage: stage,
                        type: type,
                        contactsId: contactsId,
                        source: source,
                        activityId: activityId,
                        description: description,
                        contactSummary: contactSummary,
                        nextContactTime: nextContactTime,
                    },
                    type: 'post',
                    dataType: 'json',
                    success: function (data) {
                        if (data.code == "1") {
                            // 成功跳转
                            window.location.href = 'workbench/transaction/index.do';
                        } else {
                            alert(data.message);
                        }
                    }
                });
            });

            // 客户自动补全
            $("#create-accountName").typeahead({
                source:function (jquery,process) {
                    // 每次键盘弹起，都自动触发本函数；向后台送请求，查询客户表中所有的名称，把客户名称以[]字符串形式返回前台，赋值给source
                    // process：是个函数，能够将['xxx','xxxxx','xxxxxx',.....]字符串赋值给source，从而完成自动补全
                    // jquery：在容器中输入的关键字
                    $.ajax({
                        url:'workbench/transaction/serachCustomer.do',
                        data:{customerName:jquery},
                        type:'post',
                        dataType:'json',
                        success:function (data) {//['xxx','xxxxx','xxxxxx',.....]
                            process(data);// 将后端查询的名称字符串通过process传给source
                        }
                    });
                }
            });
            // 给取消按钮添加单击事件
            $("#cancelCreateTranBtn").click(function () {
                window.location.href = 'workbench/transaction/index.do';
            });

        });
    </script>
</head>
<body>

<!-- 查找市场活动 -->
<div class="modal fade" id="findMarketActivity" role="dialog">
    <div class="modal-dialog" role="document" style="width: 80%;">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal">
                    <span aria-hidden="true">×</span>
                </button>
                <h4 class="modal-title">查找市场活动</h4>
            </div>
            <div class="modal-body">
                <div class="btn-group" style="position: relative; top: 18%; left: 8px;">
                    <form class="form-inline" role="form">
                        <div class="form-group has-feedback">
                            <input id="searchActivityOfTranTxt" type="text" class="form-control" style="width: 300px;"
                                   placeholder="请输入市场活动名称，支持模糊查询">
                            <span class="glyphicon glyphicon-search form-control-feedback"></span>
                        </div>
                    </form>
                </div>
                <table id="activityTable3" class="table table-hover"
                       style="width: 900px; position: relative;top: 10px;">
                    <thead>
                    <tr style="color: #B3B3B3;">
                        <td></td>
                        <td>名称</td>
                        <td>开始日期</td>
                        <td>结束日期</td>
                        <td>所有者</td>
                    </tr>

                    </thead>
                    <tbody id="tBody">
                    </tbody>
                </table>
            </div>
        </div>
    </div>
</div>

<!-- 查找联系人 -->
<div class="modal fade" id="findContacts" role="dialog">
    <div class="modal-dialog" role="document" style="width: 80%;">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal">
                    <span aria-hidden="true">×</span>
                </button>
                <h4 class="modal-title">查找联系人</h4>
            </div>
            <div class="modal-body">
                <div class="btn-group" style="position: relative; top: 18%; left: 8px;">
                    <form class="form-inline" role="form">
                        <div class="form-group has-feedback">
                            <input id="searchContactsOfTranTxt" type="text" class="form-control" style="width: 300px;"
                                   placeholder="请输入联系人名称，支持模糊查询">
                            <span class="glyphicon glyphicon-search form-control-feedback"></span>
                        </div>
                    </form>
                </div>
                <table id="activityTable" class="table table-hover" style="width: 900px; position: relative;top: 10px;">
                    <thead>
                    <tr style="color: #B3B3B3;">
                        <td></td>
                        <td>名称</td>
                        <td>邮箱</td>
                        <td>手机</td>
                    </tr>
                    </thead>
                    <tbody id="tBodyContacts">
                    </tbody>
                </table>
            </div>
        </div>
    </div>
</div>


<div style="position:  relative; left: 30px;">
    <h3>创建交易</h3>
    <div style="position: relative; top: -40px; left: 70%;">
        <button type="button" class="btn btn-primary" id="saveTranBtn">保存</button>
        <button type="button" class="btn btn-default" id="cancelCreateTranBtn">取消</button>
    </div>
    <hr style="position: relative; top: -40px;">
</div>
<form class="form-horizontal" role="form" style="position: relative; top: -30px;">
    <div class="form-group">
        <label for="create-transactionOwner" class="col-sm-2 control-label">所有者<span
                style="font-size: 15px; color: red;">*</span></label>
        <div class="col-sm-10" style="width: 300px;">
            <select class="form-control" id="create-transactionOwner">
                <option></option>
                <c:forEach items="${userList}" var="user">
                    <option value="${user.id}">${user.name}</option>
                </c:forEach>
            </select>
        </div>
        <label for="create-amountOfMoney" class="col-sm-2 control-label">金额</label>
        <div class="col-sm-10" style="width: 300px;">
            <input type="text" class="form-control" id="create-amountOfMoney">
        </div>
    </div>

    <div class="form-group">
        <label for="create-transactionName" class="col-sm-2 control-label">名称<span style="font-size: 15px; color: red;">*</span></label>
        <div class="col-sm-10" style="width: 300px;">
            <input type="text" class="form-control" id="create-transactionName">
        </div>
        <label for="create-expectedClosingDate" class="col-sm-2 control-label ">预计成交日期<span
                style="font-size: 15px; color: red;">*</span></label>
        <div class="col-sm-10" style="width: 300px;">
            <input type="text" class="form-control my-date" id="create-expectedClosingDate">
        </div>
    </div>

    <div class="form-group">
        <label for="create-accountName" class="col-sm-2 control-label">客户名称<span
                style="font-size: 15px; color: red;">*</span></label>
        <div class="col-sm-10" style="width: 300px;">
            <input type="text" class="form-control" id="create-accountName" placeholder="支持自动补全，输入客户不存在则新建">
        </div>
        <label for="create-transactionStage" class="col-sm-2 control-label">阶段<span
                style="font-size: 15px; color: red;">*</span></label>
        <div class="col-sm-10" style="width: 300px;">
            <select class="form-control" id="create-transactionStage">
                <option></option>
                <c:forEach items="${stageList}" var="stage">
                    <option value="${stage.id}">${stage.value}</option>
                </c:forEach>
            </select>
        </div>
    </div>

    <div class="form-group">
        <label for="create-transactionType" class="col-sm-2 control-label">类型</label>
        <div class="col-sm-10" style="width: 300px;">
            <select class="form-control" id="create-transactionType">
                <option></option>
                <c:forEach items="${typeList}" var="type">
                    <option value="${type.id}">${type.value}</option>
                </c:forEach>
            </select>
        </div>
        <label for="create-contactsName" class="col-sm-2 control-label">联系人名称&nbsp;&nbsp;<a href="javascript:void(0);"
                                                                                            id="findContactsBtn"><span
                class="glyphicon glyphicon-search"></span></a></label>
        <div class="col-sm-10" style="width: 300px;">
            <input type="hidden" id="contactsId">
            <input type="text" class="form-control" id="create-contactsName">
        </div>
    </div>

    <div class="form-group">
        <label for="create-clueSource" class="col-sm-2 control-label">来源</label>
        <div class="col-sm-10" style="width: 300px;">
            <select class="form-control" id="create-clueSource">
                <option></option>
                <c:forEach items="${sourceList}" var="source">
                    <option value="${source.id}">${source.value}</option>
                </c:forEach>
            </select>
        </div>
        <label for="create-activitySrc" class="col-sm-2 control-label">市场活动源&nbsp;&nbsp;<a href="javascript:void(0);"
                                                                                           id="openActivityBtn"><span
                class="glyphicon glyphicon-search"></span></a></label>
        <div class="col-sm-10" style="width: 300px;">
            <input type="hidden" id="activityId">
            <input type="text" class="form-control" id="create-activitySrc">
        </div>
    </div>

    <div class="form-group">
        <label for="create-describe" class="col-sm-2 control-label">描述</label>
        <div class="col-sm-10" style="width: 70%;">
            <textarea class="form-control" rows="3" id="create-describe"></textarea>
        </div>
    </div>

    <div class="form-group">
        <label for="create-contactSummary" class="col-sm-2 control-label">联系纪要</label>
        <div class="col-sm-10" style="width: 70%;">
            <textarea class="form-control" rows="3" id="create-contactSummary"></textarea>
        </div>
    </div>

    <div class="form-group">
        <label for="create-nextContactTime" class="col-sm-2 control-label">下次联系时间</label>
        <div class="col-sm-10" style="width: 300px;">
            <input type="text" class="form-control my-date" id="create-nextContactTime">
        </div>
    </div>

</form>
</body>
</html>