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
    <script type="text/javascript">
        $(function () {
            //1.创建---创建按钮单击事件
            $("#createActivityBtn").click(function () {
                // 初始化工作，加载一些js代码
                // 重置form
                $("#createActivityForm").get(0).reset();
                // 弹出创建市场模态窗口
                $("#createActivityModal").modal("show");
            });

            // 1.创建---保存按钮
            $("#SaveCreateActivityBtn").click(function () {
                // 一、发请求--1.收集参数(表单验证)--2.发送请求
                //1.收集表单参数
                var owner = $("#create-marketActivityOwner").val();
                var name = $.trim($("#create-marketActivityName").val());
                var startDate = $("#create-startTime").val();
                var endDate = $("#create-endTime").val();
                var cost = $.trim($("#create-cost").val());
                var description = $.trim($("#create-describe").val());
                //1.表单验证
                if (owner == "") {
                    alert("请输入所有者");
                    return;
                }
                if (name == "") {
                    alert("请输入名称");
                    return;
                }
                if (startDate == "") {
                    alert("请输入开始日期");
                    return;
                }
                if (endDate == "") {
                    alert("请输入结束日期");
                    return;
                }
                if (startDate > endDate) {
                    alert("结束日期不能比开始日期小");
                    return;
                }
                if (cost == "") {
                    alert("请输入成本");
                    return;
                }
                var regExp = /^(([1-9]\d*)|0)$/;
                if (!regExp.test(cost)) {
                    alert("成本不能为负数");
                    return;
                }
                //2.发送请求
                $.ajax({
                    url: 'workbench/activity/saveCreateActivity.do',
                    data: {
                        owner: owner,
                        name: name,
                        startDate: startDate,
                        endDate: endDate,
                        cost: cost,
                        description: description
                    },
                    type: 'post',
                    dataType: 'json',
                    //2.1 处理响应
                    success: function (data) {
                        if (data.code == "1") {// 成功
                            //1.关闭模态窗口
                            $("#createActivityModal").modal("hide");
                            //2. 刷新查询
                            queryActivityByConditionForPage(1, 10);
                        } else {
                            //提示
                            alert(data.message);
                            $("#createActivityModal").modal("show");
                        }
                    }
                });

            });

            // 1.创建---日期js
            $(".my-date").datetimepicker({
                language: 'zh-CN',//语言
                format: 'yyyy-mm-dd',//日期的格式
                minView: 'month',//选择最小的视图
                initialDate: new Date(),//初始化显示日期
                autoclose: true,//选择后自动关闭
                todayBtn: true,//显示今天的按钮
                clearBtn: true//清空按钮
            });

            //2.查询。当市场活动主页面加载完成，查询所有数据的第一页和总条数
            queryActivityByConditionForPage(1, 10);

            //3.按条件查询
            $("#queryActivityBtn").click(function () {
                //按照条件查询
                queryActivityByConditionForPage(1, 10);
            });

            //4.全选： 按钮添加单击事件
            $("#chckAll").click(function () {
                //判断 如果全选按钮选中，列表都选中checkbox
                // if(this.checked==true){
                //     $("#tBody input[type='checkbox']").prop("checked",true); //获取表格列表下的checkbox
                // }else{
                //     $("#tBody input[type='checkbox']").prop("checked",false); //获取表格列表下的checkbox
                // }
                $("#tBody input[type='checkbox']").prop("checked", this.checked);
            });

            //4.全选：单个框全选后，全选框也选中
            $("#tBody").on("click", "input[type='checkbox']", function () {
                // 所有的checkbox全部选中，全选也选中
                if ($("#tBody input[type='checkbox']").size() == $("#tBody input[type='checkbox']:checked").size()) {
                    $("#chckAll").prop("checked", true);
                } else {
                    $("#chckAll").prop("checked", false);
                }
            });

            //5.删除： 删除按钮
            $("#deleteBtn").click(function () {
                // 收集参数
                // 5.1获取列表中选中的checkbox
                var checkedIds = $("#tBody input[type='checkbox']:checked");
                if (checkedIds.size() == 0) {
                    alert("请选择删除的事件");
                    return;
                }

                if (window.confirm("确定删除吗？")) {
                    //5.2获取id--遍历数组
                    // this 就是每次遍历的值
                    // 把id=xxx&id=xxx...&id=xxx发送到后台
                    var ids = "";
                    $.each(checkedIds, function () {
                        ids += "id=" + this.value + "&";
                    });
                    // 取出&
                    ids = ids.substr(0, ids.length - 1);

                    //5.3发送请求
                    $.ajax({
                        url: 'workbench/activity/deleteActivityIds.do',
                        data: ids,
                        type: 'post',
                        //响应
                        dataType: 'json',
                        success: function (data) {
                            if (data.code == '1') {
                                //删除成功
                                //刷新
                                queryActivityByConditionForPage(1, 10);
                            } else {
                                alert(data.message);
                            }
                        }
                    });
                }

            });


            //修改
            //修改1.查询
            $("#editActivityBtn").click(function () {
                //收集参数获取选中的checkbox
                var checkedIds = $("#tBody input[type='checkbox']:checked");
                if (checkedIds.size() == 0) {
                    alert("请选择修改的事件");
                    return;
                }
                //只能选择1个
                if (checkedIds.size() == 1) {
                    var id = checkedIds[0].value;
                    //1发送请求
                    $.ajax({
                        url: 'workbench/activity/selectActivityById.do',
                        data: {
                            id: id
                        },
                        type: 'post',
                        // 2处理响应
                        dataType: 'json',
                        success: function (data) {
                            //查询成功
                            $("#edit-id").val(data.id);
                            $("#edit-marketActivityOwner").val(data.owner);
                            $("#edit-marketActivityName").val(data.name);
                            $("#edit-startTime").val(data.startDate);
                            $("#edit-endTime").val(data.endDate);
                            $("#edit-cost").val(data.cost);
                            $("#edit-describe").val(data.description);
                            // 弹出创建市场模态窗口
                            $("#editActivityModal").modal("show");
                        }
                    });
                } else {
                    alert("只能选择一条修改");
                    return;
                }
            });

            //修改 2 保存
            $("#updateSaveBtn").click(function () {
                //2.1封装参数
                var id = $("#edit-id").val();
                var name = $.trim($("#edit-marketActivityName").val());
                var owner = $("#edit-marketActivityOwner").val();
                var startDate = $("#edit-startTime").val();
                var endDate = $("#edit-endTime").val();
                var cost = $("#edit-cost").val();
                var description = $.trim($("#edit-describe").val());
                //2.2表单验证
                if (owner == "") {
                    alert("所有者不能为空");
                    return;
                }
                if (name == "") {
                    alert("名称不能为空");
                    return;
                }
                if (startDate != "" && endDate != "") {
                    // 使用字符串大小进行日期比较
                    if (startDate > endDate) {
                        alert("开始日期不能大于结束日期");
                        return;
                    }
                }
                // 正则表达式验证成本：成本只能为非负整数
                var regExp = /^(([1-9]\d*)|0)$/;
                if (!regExp.test(cost)) {
                    alert("成本只能为非负整数");
                    return;
                }

                //2.2 发送请求
                $.ajax({
                    url: 'workbench/activity/editActivityById.do',
                    data: {
                        id: id,
                        name: name,
                        owner: owner,
                        startDate: startDate,
                        endDate: endDate,
                        cost: cost,
                        description: description
                    },
                    type: 'post',
                    //做出响应
                    dataType: 'json',
                    success: function (data) {
                        if (data.code == '1') {
                            // 当前页
                            $("#editActivityModal").modal("hide");
                            queryActivityByConditionForPage($("#pagDiv").bs_pagination('getOption', 'currentPage'),
                                $("#pagDiv").bs_pagination('getOption', 'rowsPerPage'));
                        } else {
                            alert(data.message);
                        }
                    }
                });
            });

            // 下载:全选
            $("#exportActivityAllBtn").click(function () {
                window.location.href = "workbench/activity/exportAllActivities.do";
            });

            //下载：选中
            $("#exportActivityXzBtn").click(function () {
                var eventIds = $("#tBody input[type='checkbox']:checked");
                var ids = "?";
                $.each(eventIds, function () {
                    ids += "id=" + this.value + "&";
                });
                ids = ids.substr(0, ids.length - 1);
                window.location.href = "workbench/activity/exportSelectActivities.do" + ids;
            });

            // 6 导入
            $("#importOpenActivityBtn").click(function () {
                // 6.1 点击导入弹出导入框
                $("#importActivityModal").modal("show");
                // 6.2 点击导入按钮
                $("#importActivityBtn").click(function () {
                    // 收集参数
                    var activityFileName = $("#activityFile").val();
                    // 截取文件类型
                    var suffix = activityFileName.substr(activityFileName.lastIndexOf(".") + 1).toLocaleLowerCase();
                    if (suffix != "xls") {
                        alert("只支持xls文件");
                        return;
                    }
                    // 获取文件--$("#activityFile").get(0)获取DOM对象，然后可以上传多个文件，选取第一个文件files[0]
                    var activityFile = $("#activityFile").get(0).files[0];
                    if (activityFile.size > 5 * 1024 * 1024) {
                        alert("文件不能大于5MB");
                        return;
                    }
                    // FormData是ajax提供的接口,可以模拟K-V对向后台提交参数;
                    // FormData最大的优势是不但能提交文本数据，还能提交二进制数据
                    var formData = new FormData();
                    formData.append("activityFile", activityFile);
                    // 发送请求
                    $.ajax({
                        url: 'workbench/activity/importActivities.do',
                        data:formData,
                        processData:false,// 不转换为字符串
                        contentType:false,// 不编码
                        type: 'post',
                        dataType: 'json',
                        success: function (data) {
                            if (data.code == "1") {
                                // 成功导入
                                $("#importActivityModal").modal("hide");
                                queryActivityByConditionForPage(1, $("#pagDiv").bs_pagination('getOption', 'rowsPerPage'));
                                alert("成功导入" + data.retDate + "条数据");

                            } else {
                                alert(data.message);
                                $("#importActivityModal").modal("show");
                            }
                        }
                    });

                });

            });


        });

        function queryActivityByConditionForPage(pageNo, pageSize) {
            //2.查询。当市场活动主页面加载完成，查询所有数据的第一页和总条数
            //2.1收集查询的参数
            var name = $("#query-name").val();
            var owner = $("#query-owner").val();
            var startDate = $("#query-startDate").val();
            var endDate = $("#query-endDate").val();
            // var pageNo = 1;//页码
            // var pageSize = 10;//显示的数量
            //2.2发送请求
            $.ajax({
                url: 'workbench/activity/queryActivityByConditionForPage.do',
                data: {
                    name: name,
                    owner: owner,
                    startDate: startDate,
                    endDate: endDate,
                    pageNo: pageNo,
                    pageSize: pageSize
                },
                type: 'post',
                dataType: 'json',
                //2.3处理响应 data有两个数据，一个是数组，一个是int
                success: function (data) {
                    // 总条数
                    $("#totalRowsB").text(data.totalRows);//把查询的数据放在totalRowsB的标签里
                    // 遍历数组列表activityList $.each(遍历的数组，回调函数(index遍历的下标,obj循环变量))
                    // 定义字符串，存放市场活动列表数据
                    var htmlStr = "";
                    $.each(data.activityList, function (index, obj) {
                        // obj和this都是取出的数组元素,htmlStr进行字符串拼接
                        htmlStr += "<tr class=\"active\">";
                        htmlStr += "<td><input type=\"checkbox\" style='width: 15px;height: 15px' value=\""+obj.id+"\"/></td>";
                        htmlStr += " <td><a style=\"text-decoration: none; cursor: pointer;\" onclick=\"window.location.href='workbench/activity/ActivityDetail.do?id="+obj.id+"'\">" + obj.name + "</a></td>";
                        htmlStr += "<td>" + obj.owner + "</td>";
                        htmlStr += " <td>" + obj.startDate + "</td>";
                        htmlStr += " <td>" + obj.endDate + "</td>";
                        htmlStr += "</tr>";
                    });
                    // 把拼接好的表格字符串 插入到 显示的<tbody>
                    // text()显示文本信息不能有标签，html()可以有标签
                    $("#tBody").html(htmlStr);


                    //计算页码数
                    var totalPages = data.totalRows / pageSize;
                    if (data.totalRows % pageSize > 0) {
                        totalPages = parseInt(data.totalRows / pageSize) + 1;
                    }
                    // 调用页码翻页函数
                    $("#pagDiv").bs_pagination({
                        currentPage: pageNo,//当前页码数
                        rowsPerPage: pageSize,//每页显示条数
                        totalRows: data.totalRows,//总条数
                        totalPages: totalPages,//总页数
                        visiblePageLinks: 5,//最多显示的卡片数
                        showGoToPage: true,//显示“跳转到部分
                        showRowsPerPage: true,//每页显示条数
                        showRowsInfo: false,//是否显示记录的信息
                        //用户切换页码，触发函数，返回页码之后的pageNo和pageSize
                        onChangePage: function (event, pageObj) {
                            queryActivityByConditionForPage(pageObj.currentPage, pageObj.rowsPerPage);
                            //取消全选按钮
                            $("#chckAll").prop("checked", false);
                        }
                    });
                }
            });
        }

    </script>
</head>
<body>

<!-- 创建市场活动的模态窗口 -->
<div class="modal fade" id="createActivityModal" role="dialog">
    <div class="modal-dialog" role="document" style="width: 85%;">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal">
                    <span aria-hidden="true">×</span>
                </button>
                <h4 class="modal-title" id="myModalLabel1">创建市场活动</h4>
            </div>
            <div class="modal-body">

                <form id="createActivityForm" class="form-horizontal" role="form">

                    <div class="form-group">
                        <label for="create-marketActivityOwner" class="col-sm-2 control-label">所有者<span
                                style="font-size: 15px; color: red;">*</span></label>
                        <div class="col-sm-10" style="width: 300px;">
                            <select class="form-control" id="create-marketActivityOwner">
                                <c:forEach items="${userList}" var="user">
                                    <option value="${user.id}">${user.name}</option>
                                </c:forEach>
                            </select>
                        </div>
                        <label for="create-marketActivityName" class="col-sm-2 control-label">名称<span
                                style="font-size: 15px; color: red;">*</span></label>
                        <div class="col-sm-10" style="width: 300px;">
                            <input type="text" class="form-control" id="create-marketActivityName">
                        </div>
                    </div>

                    <div class="form-group">
                        <label for="create-startTime" class="col-sm-2 control-label">开始日期</label>
                        <div class="col-sm-10" style="width: 300px;">
                            <input type="text" class="form-control my-date" id="create-startTime" readonly>
                        </div>
                        <label for="create-endTime" class="col-sm-2 control-label">结束日期</label>
                        <div class="col-sm-10" style="width: 300px;">
                            <input type="text" class="form-control my-date" id="create-endTime" readonly>
                        </div>
                    </div>
                    <div class="form-group">

                        <label for="create-cost" class="col-sm-2 control-label">成本</label>
                        <div class="col-sm-10" style="width: 300px;">
                            <input type="text" class="form-control" id="create-cost">
                        </div>
                    </div>
                    <div class="form-group">
                        <label for="create-describe" class="col-sm-2 control-label">描述</label>
                        <div class="col-sm-10" style="width: 81%;">
                            <textarea class="form-control" rows="3" id="create-describe"></textarea>
                        </div>
                    </div>

                </form>

            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
                <button type="button" class="btn btn-primary" id="SaveCreateActivityBtn">保存</button>
            </div>
        </div>
    </div>
</div>

<!-- 修改市场活动的模态窗口 -->
<div class="modal fade" id="editActivityModal" role="dialog">
    <div class="modal-dialog" role="document" style="width: 85%;">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal">
                    <span aria-hidden="true">×</span>
                </button>
                <h4 class="modal-title" id="myModalLabel2">修改市场活动</h4>
            </div>
            <div class="modal-body">

                <form class="form-horizontal" role="form">
                    <!--设置一个隐藏标签，用来存放id，供后面修改数据时操作-->
                    <input type="hidden" id="edit-id">
                    <div class="form-group">
                        <label for="edit-marketActivityOwner" class="col-sm-2 control-label">所有者<span
                                style="font-size: 15px; color: red;">*</span></label>
                        <div class="col-sm-10" style="width: 300px;">
                            <select class="form-control" id="edit-marketActivityOwner">
                                <c:forEach items="${userList}" var="user">
                                    <option value="${user.id}">${user.name}</option>
                                </c:forEach>
                            </select>
                        </div>
                        <label for="edit-marketActivityName" class="col-sm-2 control-label">名称<span
                                style="font-size: 15px; color: red;">*</span></label>
                        <div class="col-sm-10" style="width: 300px;">
                            <input type="text" class="form-control" id="edit-marketActivityName">
                        </div>
                    </div>

                    <div class="form-group">
                        <label for="edit-startTime" class="col-sm-2 control-label">开始日期</label>
                        <div class="col-sm-10" style="width: 300px;">
                            <input type="text" class="form-control my-date" id="edit-startTime" readonly>
                        </div>
                        <label for="edit-endTime" class="col-sm-2 control-label">结束日期</label>
                        <div class="col-sm-10" style="width: 300px;">
                            <input type="text" class="form-control my-date" id="edit-endTime" readonly>
                        </div>
                    </div>

                    <div class="form-group">
                        <label for="edit-cost" class="col-sm-2 control-label">成本</label>
                        <div class="col-sm-10" style="width: 300px;">
                            <input type="text" class="form-control" id="edit-cost" value="5,000">
                        </div>
                    </div>

                    <div class="form-group">
                        <label for="edit-describe" class="col-sm-2 control-label">描述</label>
                        <div class="col-sm-10" style="width: 81%;">
                            <textarea class="form-control" rows="3" id="edit-describe"></textarea>
                        </div>
                    </div>

                </form>

            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
                <button type="button" class="btn btn-primary" id="updateSaveBtn">更新</button>
            </div>
        </div>
    </div>
</div>

<!-- 导入市场活动的模态窗口 -->
<div class="modal fade" id="importActivityModal" role="dialog">
    <div class="modal-dialog" role="document" style="width: 85%;">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal">
                    <span aria-hidden="true">×</span>
                </button>
                <h4 class="modal-title" id="myModalLabel">导入市场活动</h4>
            </div>
            <div class="modal-body" style="height: 350px;">
                <div style="position: relative;top: 20px; left: 50px;">
                    请选择要上传的文件：<small style="color: gray;">[仅支持.xls]</small>
                </div>
                <div style="position: relative;top: 40px; left: 50px;">
                    <input type="file" id="activityFile">
                    <br>
                    <a href="file/activity.xls" download="activity-mode.xls" style="text-decoration:none;color: #2a6496" ><b>下载导入文件模板</b></a>
                </div>
                <div style="position: relative; width: 400px; height: 320px; left: 45% ; top: -40px;">
                    <h3>重要提示</h3>
                    <ul>
                        <li>操作仅针对Excel，仅支持后缀名为XLS的文件。</li>
                        <li>给定文件的第一行将视为字段名。</li>
                        <li>请确认您的文件大小不超过5MB。</li>
                        <li>日期值以文本形式保存，必须符合yyyy-MM-dd格式。</li>
                        <li>日期时间以文本形式保存，必须符合yyyy-MM-dd HH:mm:ss的格式。</li>
                        <li>默认情况下，字符编码是UTF-8 (统一码)，请确保您导入的文件使用的是正确的字符编码方式。</li>
                        <li>建议您在导入真实数据之前用测试文件测试文件导入功能。</li>
                    </ul>
                </div>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
                <button id="importActivityBtn" type="button" class="btn btn-primary">导入</button>
            </div>
        </div>
    </div>
</div>


<div>
    <div style="position: relative; left: 10px; top: -10px;">
        <div class="page-header">
            <h3>市场活动列表</h3>
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
                        <input class="form-control" type="text" id="query-name">
                    </div>
                </div>

                <div class="form-group">
                    <div class="input-group">
                        <div class="input-group-addon">所有者</div>
                        <input class="form-control" type="text" id="query-owner">
                    </div>
                </div>


                <div class="form-group">
                    <div class="input-group">
                        <div class="input-group-addon">开始日期</div>
                        <input class="form-control my-date" type="text" id="query-startDate"/>
                    </div>
                </div>
                <div class="form-group">
                    <div class="input-group">
                        <div class="input-group-addon">结束日期</div>
                        <input class="form-control my-date" type="text" id="query-endDate">
                    </div>
                </div>

                <button type="button" class="btn btn-default" id="queryActivityBtn">查询</button>

            </form>
        </div>
        <div class="btn-toolbar" role="toolbar"
             style="background-color: #F7F7F7; height: 50px; position: relative;top: 5px;">
            <div class="btn-group" style="position: relative; top: 18%;">
                <button type="button" class="btn btn-primary" id="createActivityBtn"><span
                        class="glyphicon glyphicon-plus"></span> 创建
                </button>
                <button type="button" class="btn btn-default" id="editActivityBtn"><span
                        class="glyphicon glyphicon-pencil"></span> 修改
                </button>
                <button type="button" class="btn btn-danger" id="deleteBtn"><span
                        class="glyphicon glyphicon-minus"></span> 删除
                </button>
            </div>
            <div class="btn-group" style="position: relative; top: 18%;">
                <button id="importOpenActivityBtn" type="button" class="btn btn-default">
                    <span class="glyphicon glyphicon-import"></span> 上传列表数据（导入）
                </button>
                <button id="exportActivityAllBtn" type="button" class="btn btn-default"><span
                        class="glyphicon glyphicon-export"></span> 下载列表数据（全部）
                </button>
                <button id="exportActivityXzBtn" type="button" class="btn btn-default"><span
                        class="glyphicon glyphicon-export"></span> 下载列表数据（选中）
                </button>
            </div>
            <div class="btn-group" style="position: relative; top: 18%;">
                <button type="button" class="btn btn-default" style="cursor: default;">共<b id="totalRowsB">50</b>条记录
                </button>
            </div>
        </div>
        <div style="position: relative;top: 10px;">
            <table class="table table-hover">
                <thead>
                <tr style="color: #B3B3B3;">
                    <td><input type="checkbox" id="chckAll" style='width: 15px;height: 15px'/></td>
                    <td>名称</td>
                    <td>所有者</td>
                    <td>开始日期</td>
                    <td>结束日期</td>
                </tr>
                </thead>
                <tbody id="tBody">
                <%--                js遍历的时候在这里拼接--%>
                <%--                <tr class="active">--%>
                <%--                    <td><input type="checkbox"/></td>--%>
                <%--                    <td><a style="text-decoration: none; cursor: pointer;"--%>
                <%--                           onclick="window.location.href='detail.jsp';">发传单</a></td>--%>
                <%--                    <td>zhangsan</td>--%>
                <%--                    <td>2020-10-10</td>--%>
                <%--                    <td>2020-10-20</td>--%>
                <%--                </tr>--%>
                </tbody>
            </table>
            <%--下一页上一页--%>
            <div id="pagDiv">
            </div>
        </div>

        <%--        <div style="height: 50px; position: relative;top: 30px;">--%>
        <%--            <div>--%>
        <%--                <button type="button" class="btn btn-default" style="cursor: default;">共<b id="totalRowsB">50</b>条记录--%>
        <%--                </button>--%>
        <%--            </div>--%>
        <%--            <div class="btn-group" style="position: relative;top: -34px; left: 110px;">--%>
        <%--                <button type="button" class="btn btn-default" style="cursor: default;">显示</button>--%>
        <%--                <div class="btn-group">--%>
        <%--                    <button type="button" class="btn btn-default dropdown-toggle" data-toggle="dropdown">--%>
        <%--                        10--%>
        <%--                        <span class="caret"></span>--%>
        <%--                    </button>--%>
        <%--                    <ul class="dropdown-menu" role="menu">--%>
        <%--                        <li><a href="#">20</a></li>--%>
        <%--                        <li><a href="#">30</a></li>--%>
        <%--                    </ul>--%>
        <%--                </div>--%>
        <%--                <button type="button" class="btn btn-default" style="cursor: default;">条/页</button>--%>
        <%--            </div>--%>
        <%--            <div style="position: relative;top: -88px; left: 285px;">--%>
        <%--                <nav>--%>
        <%--                    <ul class="pagination">--%>
        <%--                        <li class="disabled"><a href="#">首页</a></li>--%>
        <%--                        <li class="disabled"><a href="#">上一页</a></li>--%>
        <%--                        <li class="active"><a href="#">1</a></li>--%>
        <%--                        <li><a href="#">2</a></li>--%>
        <%--                        <li><a href="#">3</a></li>--%>
        <%--                        <li><a href="#">4</a></li>--%>
        <%--                        <li><a href="#">5</a></li>--%>
        <%--                        <li><a href="#">下一页</a></li>--%>
        <%--                        <li class="disabled"><a href="#">末页</a></li>--%>
        <%--                    </ul>--%>
        <%--                </nav>--%>
        <%--            </div>--%>
        <%--        </div>--%>

    </div>

</div>
</body>
</html>