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
            // 加载首页线索
            queryClueByConditionForPage(1, 10);
            // 创建线索1
            $("#createClueBtn").click(function () {
                // 重置form
                $("#createClueForm").get(0).reset();
                $("#createClueModal").modal("show");
            });
            // 创建线索2
            $("#createSaveClueBtn").click(function () {
                // 收集参数
                var fullname = $.trim($("#create-surname").val());
                var appellation = $("#create-call").val();
                var owner = $("#create-clueOwner").val();
                var company = $.trim($("#create-company").val());
                var job = $.trim($("#create-job").val());
                var email = $.trim($("#create-email").val());
                var phone = $.trim($("#create-phone").val());
                var website = $.trim($("#create-website").val());
                var mphone = $.trim($("#create-mphone").val());
                var state = $("#create-status").val();
                var source = $("#create-source").val();
                var description = $.trim($("#create-describe").val());
                var contactSummary = $.trim($("#create-contactSummary").val());
                var nextContactTime = $.trim($("#create-nextContactTime").val());
                var address = $.trim($("#create-address").val());
                // 表单验证
                if (owner == "") {
                    alert("所有者不能为空");
                    return;
                }
                if (company == "") {
                    alert("公司不能为空");
                    return;
                }
                if (fullname == "") {
                    alert("姓名不能为空");
                    return;
                }

                // 正则表达式验证
                if (email != "") { // 如果邮箱非空开始验证
                    var emailRegExp = /^\w+([-+.]\w+)*@\w+([-.]\w+)*\.\w+([-.]\w+)*$/; // 邮件验证正则表达式
                    if (!emailRegExp.test(email)) {
                        alert("邮箱格式错误");
                        return;
                    }
                }
                if (phone != "") {
                    var phoneRegExp = /0\d{2,3}-\d{7,8}/; // 国内座机电话号码验证："XXX-XXXXXXX"
                    if (!phoneRegExp.test(phone)) {
                        alert("座机号码格式错误");
                        return;
                    }
                }
                if (website != "") {
                    var websiteRegExp = /^(?:(http|https|ftp):\/\/)?((?:[\w-]+\.)+[a-z0-9]+)((?:\/[^/?#]*)+)?(\?[^#]+)?(#.+)?$/i;
                    if (!websiteRegExp.test(website)) {
                        alert("网站格式错误");
                        return;
                    }
                }
                if (mphone != "") {
                    var mphoneRegExp = /^(13[0-9]|14[5|7]|15[0|1|2|3|5|6|7|8|9]|18[0|1|2|3|5|6|7|8|9])\d{8}$/;
                    if (!mphoneRegExp.test(mphone)) {
                        alert("手机号码格式错误");
                        return;
                    }
                }
                // 发送请求
                $.ajax({
                    url: 'workbench/clue/saveCreateClue.do',
                    data: {
                        fullname: fullname,
                        appellation: appellation,
                        owner: owner,
                        company: company,
                        job: job,
                        email: email,
                        phone: phone,
                        website: website,
                        mphone: mphone,
                        state: state,
                        source: source,
                        description: description,
                        contactSummary: contactSummary,
                        nextContactTime: nextContactTime,
                        address: address
                    },
                    type: 'post',
                    dataType: 'json',
                    success: function (data) {
                        if (data.code == "1") {
                            // 关闭模态
                            $("#createClueModal").modal("hide");
                            // 首页
                            queryClueByConditionForPage(1, 10);
                        } else {
                            alert(code.message);
                        }
                    }
                });

            });

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
            // 查询：条件查询
            $("#queryBtn").click(function () {
                queryClueByConditionForPage(1, 10);
            });
            //删除： 删除按钮
            $("#deleteClueBtn").click(function () {
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
                    var id = "";
                    $.each(checkedIds, function () {
                        id += "id=" + this.value + "&";
                    });
                    // 取出&
                    id = id.substr(0, id.length - 1);

                    //5.3发送请求
                    $.ajax({
                        url: 'workbench/clue/deleteClueByIds.do',
                        data: id,
                        type: 'post',
                        //响应
                        dataType: 'json',
                        success: function (data) {
                            if (data.code == '1') {
                                //删除成功
                                //刷新
                                queryClueByConditionForPage(1, 10);
                            } else {
                                alert(data.message);
                            }
                        }
                    });
                }

            });

            // 修改1：查询
            $("#updateClueBtn").click(function () {
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
                        url: 'workbench/clue/queryClueById.do',
                        data: {
                            id: id
                        },
                        type: 'post',
                        // 2处理响应
                        dataType: 'json',
                        success: function (data) {
                            //查询成功
                            // 查询
                            $("#edit-id").val(data.id);
                            $("#edit-surname").val(data.fullname);
                            $("#edit-call").val(data.appellation);
                            $("#edit-clueOwner").val(data.owner);
                            $("#edit-company").val(data.company);
                            $("#edit-job").val(data.job);
                            $("#edit-email").val(data.email);
                            $("#edit-phone").val(data.phone);
                            $("#edit-website").val(data.website);
                            $("#edit-mphone").val(data.mphone);
                            $("#edit-status").val(data.state);
                            $("#edit-source").val(data.source);
                            $("#edit-describe").val(data.description);
                            $("#edit-contactSummary").val(data.contactSummary);
                            $("#edit-nextContactTime").val(data.nextContactTime);
                            $("#edit-address").val(data.address);
                            // 弹出创建市场模态窗口
                            $("#editClueModal").modal("show");
                        }
                    });
                } else {
                    alert("只能选择一条修改");
                    return;
                }
            });
            // 修改2：保存
            $("#updateSaveBtn").click(function () {
                //2.1封装参数
                var id = $("#edit-id").val();
                var fullname = $.trim($("#edit-surname").val());
                var appellation = $("#edit-call").val();
                var owner = $("#edit-clueOwner").val();
                var company = $.trim($("#edit-company").val());
                var job = $.trim($("#edit-job").val());
                var email = $.trim($("#edit-email").val());
                var phone = $.trim($("#edit-phone").val());
                var website = $.trim($("#edit-website").val());
                var mphone = $.trim($("#edit-mphone").val());
                var state = $("#edit-status").val();
                var source = $("#edit-source").val();
                var description = $.trim($("#edit-describe").val());
                var contactSummary = $.trim($("#edit-contactSummary").val());
                var nextContactTime = $.trim($("#edit-nextContactTime").val());
                var address = $.trim($("#edit-address").val());
                //2.2表单验证
                if (owner == "") {
                    alert("所有者不能为空");
                    return;
                }
                if (company == "") {
                    alert("名称不能为空");
                    return;
                }
                if (fullname == "") {
                    alert("姓名不能为空");
                    return;
                }
                // 正则表达式验证
                if (email != "") { // 如果邮箱非空开始验证
                    var emailRegExp = /^\w+([-+.]\w+)*@\w+([-.]\w+)*\.\w+([-.]\w+)*$/; // 邮件验证正则表达式
                    if (!emailRegExp.test(email)) {
                        alert("邮箱格式错误");
                        return;
                    }
                }
                if (phone != "") {
                    var phoneRegExp = /0\d{2,3}-\d{7,8}/; // 国内座机电话号码验证："XXX-XXXXXXX"
                    if (!phoneRegExp.test(phone)) {
                        alert("座机号码格式错误");
                        return;
                    }
                }
                if (website != "") {
                    var websiteRegExp = /^(?:(http|https|ftp):\/\/)?((?:[\w-]+\.)+[a-z0-9]+)((?:\/[^/?#]*)+)?(\?[^#]+)?(#.+)?$/i;
                    if (!websiteRegExp.test(website)) {
                        alert("网站格式错误");
                        return;
                    }
                }
                if (mphone != "") {
                    var mphoneRegExp = /^(13[0-9]|14[5|7]|15[0|1|2|3|5|6|7|8|9]|18[0|1|2|3|5|6|7|8|9])\d{8}$/;
                    if (!mphoneRegExp.test(mphone)) {
                        alert("手机号码格式错误");
                        return;
                    }
                }

                //2.2 发送请求
                $.ajax({
                    url: 'workbench/clue/updateClue.do',
                    data: {
                        id: id,
                        fullname: fullname,
                        appellation: appellation,
                        owner: owner,
                        company: company,
                        job: job,
                        email: email,
                        phone: phone,
                        website: website,
                        mphone: mphone,
                        state: state,
                        source: source,
                        description: description,
                        contactSummary: contactSummary,
                        nextContactTime: nextContactTime,
                        address: address
                    },
                    type: 'post',
                    //做出响应
                    dataType: 'json',
                    success: function (data) {
                        if (data.code == '1') {
                            // 当前页
                            $("#editClueModal").modal("hide");
                            queryClueByConditionForPage($("#pagDiv").bs_pagination('getOption', 'currentPage'),
                                $("#pagDiv").bs_pagination('getOption', 'rowsPerPage'));
                        } else {
                            alert(data.message);
                        }
                    }
                });
            });


        });

        // 查询线索
        function queryClueByConditionForPage(pageNo, pageSize) {
            // 收集参数
            var fullname = $.trim($("#query-fullname").val());
            var company = $.trim($("#query-company").val());
            var phone = $.trim($("#query-phone").val());
            var mphone = $.trim($("#query-mphone").val());
            var source = $("#query-source option:selected").val();
            var owner = $("#query-owner option:selected").val();
            var state = $("#query-state option:selected").val();
            // 发送请求
            $.ajax({
                url: 'workbench/clue/queryClueByCondition.do',
                data: {
                    fullname: fullname,
                    company: company,
                    phone: phone,
                    mphone: mphone,
                    source: source,
                    owner: owner,
                    state: state,
                    pageNo: pageNo,
                    pageSize: pageSize
                },
                type: 'post',
                dataType: 'json',
                success: function (data) {
                    var htmlStr = "";
                    $.each(data.clueList, function (index, obj) {
                        // obj和this都是取出的数组元素,htmlStr进行字符串拼接
                        htmlStr += "<tr class=\"active\">";
                        htmlStr += "<td><input type=\"checkbox\" style='width: 15px;height: 15px' value=\"" + obj.id + "\"/></td>";
                        htmlStr += "<td><a style=\"text-decoration: none; cursor: pointer;\"onclick=\"window.location.href='workbench/clue/selectClueForDetailById.do?id=" + obj.id + "';\">" + obj.fullname + "</a></td>";
                        htmlStr += "<td>" + obj.company + "</td>";
                        htmlStr += "<td>" + obj.phone + "</td>";
                        htmlStr += "<td>" + obj.mphone + "</td>";
                        htmlStr += "<td>" + obj.source + "</td>";
                        htmlStr += " <td>" + obj.owner + "</td>";
                        htmlStr += "<td>" + obj.state + "</td>";
                        htmlStr += " </tr>";
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
                            queryClueByConditionForPage(pageObj.currentPage, pageObj.rowsPerPage);
                        }
                    });
                }
            });
        }


    </script>
</head>
<body>

<!-- 创建线索的模态窗口 -->
<div class="modal fade" id="createClueModal" role="dialog">
    <div class="modal-dialog" role="document" style="width: 90%;">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal">
                    <span aria-hidden="true">×</span>
                </button>
                <h4 class="modal-title" id="myModalLabel">创建线索</h4>
            </div>
            <div class="modal-body">
                <form class="form-horizontal" id="createClueForm" role="form">

                    <div class="form-group">
                        <label for="create-clueOwner" class="col-sm-2 control-label">所有者<span
                                style="font-size: 15px; color: red;">*</span></label>
                        <div class="col-sm-10" style="width: 300px;">
                            <select class="form-control" id="create-clueOwner">
                                <option></option>
                                <c:forEach items="${userList}" var="user">
                                    <option value="${user.id}">${user.name}</option>
                                </c:forEach>
                            </select>
                        </div>
                        <label for="create-company" class="col-sm-2 control-label">公司<span
                                style="font-size: 15px; color: red;">*</span></label>
                        <div class="col-sm-10" style="width: 300px;">
                            <input type="text" class="form-control" id="create-company">
                        </div>
                    </div>

                    <div class="form-group">
                        <label for="create-call" class="col-sm-2 control-label">称呼</label>
                        <div class="col-sm-10" style="width: 300px;">
                            <select class="form-control" id="create-call">
                                <option></option>
                                <c:forEach items="${appellationList}" var="appellation">
                                    <option value="${appellation.id}">${appellation.value}</option>
                                </c:forEach>
                            </select>
                        </div>
                        <label for="create-surname" class="col-sm-2 control-label">姓名<span
                                style="font-size: 15px; color: red;">*</span></label>
                        <div class="col-sm-10" style="width: 300px;">
                            <input type="text" class="form-control" id="create-surname">
                        </div>
                    </div>

                    <div class="form-group">
                        <label for="create-job" class="col-sm-2 control-label">职位</label>
                        <div class="col-sm-10" style="width: 300px;">
                            <input type="text" class="form-control" id="create-job">
                        </div>
                        <label for="create-email" class="col-sm-2 control-label">邮箱</label>
                        <div class="col-sm-10" style="width: 300px;">
                            <input type="text" class="form-control" id="create-email">
                        </div>
                    </div>

                    <div class="form-group">
                        <label for="create-phone" class="col-sm-2 control-label">公司座机</label>
                        <div class="col-sm-10" style="width: 300px;">
                            <input type="text" class="form-control" id="create-phone">
                        </div>
                        <label for="create-website" class="col-sm-2 control-label">公司网站</label>
                        <div class="col-sm-10" style="width: 300px;">
                            <input type="text" class="form-control" id="create-website">
                        </div>
                    </div>

                    <div class="form-group">
                        <label for="create-mphone" class="col-sm-2 control-label">手机</label>
                        <div class="col-sm-10" style="width: 300px;">
                            <input type="text" class="form-control" id="create-mphone">
                        </div>
                        <label for="create-status" class="col-sm-2 control-label">线索状态</label>
                        <div class="col-sm-10" style="width: 300px;">
                            <select class="form-control" id="create-status">
                                <option></option>
                                <c:forEach items="${clueStateList}" var="clueState">
                                    <option value="${clueState.id}">${clueState.value}</option>
                                </c:forEach>

                            </select>
                        </div>
                    </div>

                    <div class="form-group">
                        <label for="create-source" class="col-sm-2 control-label">线索来源</label>
                        <div class="col-sm-10" style="width: 300px;">
                            <select class="form-control" id="create-source">
                                <option></option>
                                <c:forEach items="${sourceList}" var="source">
                                    <option value="${source.id}">${source.value}</option>
                                </c:forEach>

                            </select>
                        </div>
                    </div>


                    <div class="form-group">
                        <label for="create-describe" class="col-sm-2 control-label">线索描述</label>
                        <div class="col-sm-10" style="width: 81%;">
                            <textarea class="form-control" rows="3" id="create-describe"></textarea>
                        </div>
                    </div>

                    <div style="height: 1px; width: 103%; background-color: #D5D5D5; left: -13px; position: relative;"></div>

                    <div style="position: relative;top: 15px;">
                        <div class="form-group">
                            <label for="create-nextContactTime" class="col-sm-2 control-label">下次联系时间</label>
                            <div class="col-sm-10" style="width: 300px;">
                                <input type="text" class="form-control my-date" id="create-nextContactTime" readonly>
                            </div>
                        </div>
                        <div class="form-group">
                            <label for="create-contactSummary" class="col-sm-2 control-label">联系纪要</label>
                            <div class="col-sm-10" style="width: 81%;">
                                <textarea class="form-control" rows="3" id="create-contactSummary"></textarea>
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
                <button type="button" class="btn btn-primary" id="createSaveClueBtn">保存</button>
            </div>
        </div>
    </div>
</div>

<!-- 修改线索的模态窗口 -->
<div class="modal fade" id="editClueModal" role="dialog">
    <div class="modal-dialog" role="document" style="width: 90%;">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal">
                    <span aria-hidden="true">×</span>
                </button>
                <h4 class="modal-title">修改线索</h4>
            </div>
            <div class="modal-body">
                <form class="form-horizontal" role="form">
                    <input type="hidden" id="edit-id">
                    <div class="form-group">
                        <label for="edit-clueOwner" class="col-sm-2 control-label">所有者<span
                                style="font-size: 15px; color: red;">*</span></label>
                        <div class="col-sm-10" style="width: 300px;">
                            <select class="form-control" id="edit-clueOwner">
                                <option></option>
                                <c:forEach items="${userList}" var="user">
                                    <option value="${user.id}">${user.name}</option>
                                </c:forEach>
                            </select>
                        </div>
                        <label for="edit-company" class="col-sm-2 control-label">公司<span
                                style="font-size: 15px; color: red;">*</span></label>
                        <div class="col-sm-10" style="width: 300px;">
                            <input type="text" class="form-control" id="edit-company" value="动力节点">
                        </div>
                    </div>

                    <div class="form-group">
                        <label for="edit-call" class="col-sm-2 control-label">称呼</label>
                        <div class="col-sm-10" style="width: 300px;">
                            <select class="form-control" id="edit-call">
                                <option></option>
                                <c:forEach items="${appellationList}" var="appellation">
                                    <option value="${appellation.id}">${appellation.value}</option>
                                </c:forEach>
                            </select>
                        </div>
                        <label for="edit-surname" class="col-sm-2 control-label">姓名<span
                                style="font-size: 15px; color: red;">*</span></label>
                        <div class="col-sm-10" style="width: 300px;">
                            <input type="text" class="form-control" id="edit-surname" value="李四">
                        </div>
                    </div>

                    <div class="form-group">
                        <label for="edit-job" class="col-sm-2 control-label">职位</label>
                        <div class="col-sm-10" style="width: 300px;">
                            <input type="text" class="form-control" id="edit-job" value="CTO">
                        </div>
                        <label for="edit-email" class="col-sm-2 control-label">邮箱</label>
                        <div class="col-sm-10" style="width: 300px;">
                            <input type="text" class="form-control" id="edit-email" value="lisi@bjpowernode.com">
                        </div>
                    </div>

                    <div class="form-group">
                        <label for="edit-phone" class="col-sm-2 control-label">公司座机</label>
                        <div class="col-sm-10" style="width: 300px;">
                            <input type="text" class="form-control" id="edit-phone" value="010-84846003">
                        </div>
                        <label for="edit-website" class="col-sm-2 control-label">公司网站</label>
                        <div class="col-sm-10" style="width: 300px;">
                            <input type="text" class="form-control" id="edit-website"
                                   value="http://www.bjpowernode.com">
                        </div>
                    </div>

                    <div class="form-group">
                        <label for="edit-mphone" class="col-sm-2 control-label">手机</label>
                        <div class="col-sm-10" style="width: 300px;">
                            <input type="text" class="form-control" id="edit-mphone" value="12345678901">
                        </div>
                        <label for="edit-status" class="col-sm-2 control-label">线索状态</label>
                        <div class="col-sm-10" style="width: 300px;">
                            <select class="form-control" id="edit-status">
                                <option></option>
                                <c:forEach items="${clueStateList}" var="clueState">
                                    <option value="${clueState.id}">${clueState.value}</option>
                                </c:forEach>
                            </select>
                        </div>
                    </div>

                    <div class="form-group">
                        <label for="edit-source" class="col-sm-2 control-label">线索来源</label>
                        <div class="col-sm-10" style="width: 300px;">
                            <select class="form-control" id="edit-source">
                                <option></option>
                                <c:forEach items="${sourceList}" var="source">
                                    <option value="${source.id}">${source.value}</option>
                                </c:forEach>
                            </select>
                        </div>
                    </div>

                    <div class="form-group">
                        <label for="edit-describe" class="col-sm-2 control-label">描述</label>
                        <div class="col-sm-10" style="width: 81%;">
                            <textarea class="form-control" rows="3" id="edit-describe">这是一条线索的描述信息</textarea>
                        </div>
                    </div>

                    <div style="height: 1px; width: 103%; background-color: #D5D5D5; left: -13px; position: relative;"></div>

                    <div style="position: relative;top: 15px;">
                        <div class="form-group">
                            <label for="edit-contactSummary" class="col-sm-2 control-label">联系纪要</label>
                            <div class="col-sm-10" style="width: 81%;">
                                <textarea class="form-control" rows="3" id="edit-contactSummary">这个线索即将被转换</textarea>
                            </div>
                        </div>
                        <div class="form-group">
                            <label for="edit-nextContactTime" class="col-sm-2 control-label">下次联系时间</label>
                            <div class="col-sm-10" style="width: 300px;">
                                <input type="text" class="form-control my-date" id="edit-nextContactTime"
                                       value="2017-05-01" readonly>
                            </div>
                        </div>
                    </div>

                    <div style="height: 1px; width: 103%; background-color: #D5D5D5; left: -13px; position: relative; top : 10px;"></div>

                    <div style="position: relative;top: 20px;">
                        <div class="form-group">
                            <label for="edit-address" class="col-sm-2 control-label">详细地址</label>
                            <div class="col-sm-10" style="width: 81%;">
                                <textarea class="form-control" rows="1" id="edit-address">北京大兴区大族企业湾</textarea>
                            </div>
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


<div>
    <div style="position: relative; left: 10px; top: -10px;">
        <div class="page-header">
            <h3>线索列表</h3>
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
                        <input class="form-control" type="text" id="query-fullname">
                    </div>
                </div>

                <div class="form-group">
                    <div class="input-group">
                        <div class="input-group-addon">公司</div>
                        <input class="form-control" type="text" id="query-company">
                    </div>
                </div>

                <div class="form-group">
                    <div class="input-group">
                        <div class="input-group-addon">公司座机</div>
                        <input class="form-control" type="text" id="query-phone">
                    </div>
                </div>

                <div class="form-group">
                    <div class="input-group">
                        <div class="input-group-addon">线索来源</div>
                        <select class="form-control" id="query-source">
                            <option></option>
                            <c:forEach items="${sourceList}" var="source">
                                <option >${source.value}</option>
                            </c:forEach>
                        </select>
                    </div>
                </div>

                <br>

                <div class="form-group">
                    <div class="input-group">
                        <div class="input-group-addon">所有者</div>
                        <select class="form-control" id="query-owner">
                            <option></option>
                            <c:forEach items="${userList}" var="user">
                                <option >${user.name}</option>
                            </c:forEach>
                        </select>
                    </div>
                </div>


                <div class="form-group">
                    <div class="input-group">
                        <div class="input-group-addon">手机</div>
                        <input class="form-control" type="text" id="query-mphone">
                    </div>
                </div>

                <div class="form-group">
                    <div class="input-group">
                        <div class="input-group-addon">线索状态</div>
                        <select class="form-control" id="query-state">
                            <option></option>
                            <c:forEach items="${clueStateList}" var="clueState">
                                <option >${clueState.value}</option>
                            </c:forEach>
                        </select>
                    </div>
                </div>

                <button type="button" class="btn btn-default" id="queryBtn">查询</button>

            </form>
        </div>
        <div class="btn-toolbar" role="toolbar"
             style="background-color: #F7F7F7; height: 50px; position: relative;top: 40px;">
            <div class="btn-group" style="position: relative; top: 18%;">
                <button type="button" class="btn btn-primary" id="createClueBtn"><span
                        class="glyphicon glyphicon-plus"></span> 创建
                </button>
                <button type="button" class="btn btn-default" id="updateClueBtn"><span
                        class="glyphicon glyphicon-pencil"></span> 修改
                </button>
                <button type="button" class="btn btn-danger" id="deleteClueBtn"><span
                        class="glyphicon glyphicon-minus"></span> 删除
                </button>
            </div>


        </div>
        <div style="position: relative;top: 50px;">
            <table class="table table-hover">
                <thead>
                <tr style="color: #B3B3B3;">
                    <td><input type="checkbox"/></td>
                    <td>名称</td>
                    <td>公司</td>
                    <td>公司座机</td>
                    <td>手机</td>
                    <td>线索来源</td>
                    <td>所有者</td>
                    <td>线索状态</td>
                </tr>
                </thead>
                <tbody id="tBody">

                </tbody>
            </table>
            <%--下一页上一页--%>
            <div id="pagDiv">
            </div>


        </div>

    </div>
</div>
</body>
</html>