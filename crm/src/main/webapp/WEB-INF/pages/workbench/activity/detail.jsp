<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page isELIgnored="false" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath() + "/";
%>
<html>
<head>
    <base href="<%=basePath%>">
    <meta charset="UTF-8">
    <link href="jquery/bootstrap_3.3.0/css/bootstrap.min.css" type="text/css" rel="stylesheet"/>
    <script type="text/javascript" src="jquery/jquery-1.11.1-min.js"></script>
    <script type="text/javascript" src="jquery/bootstrap_3.3.0/js/bootstrap.min.js"></script>
    <script type="text/javascript">

        //默认情况下取消和保存按钮是隐藏的
        var cancelAndSaveBtnDefault = true;

        $(function () {
            $("#remark").focus(function () {
                if (cancelAndSaveBtnDefault) {
                    //设置remarkDiv的高度为130px
                    $("#remarkDiv").css("height", "130px");
                    //显示
                    $("#cancelAndSaveBtn").show("2000");
                    cancelAndSaveBtnDefault = false;
                }
            });

            $("#cancelBtn").click(function () {
                //显示
                $("#cancelAndSaveBtn").hide();
                //设置remarkDiv的高度为130px
                $("#remarkDiv").css("height", "90px");
                cancelAndSaveBtnDefault = true;
                // 清空文本域中内容
                $("#remark").val("");
            });

            // $(".remarkDiv").mouseover(function () {
            //     $(this).children("div").children("div").show();
            // });
            $("#remarkDivList").on("mouseover", ".remarkDiv", function () {
                $(this).children("div").children("div").show();
            });


            // $(".remarkDiv").mouseout(function () {
            //     $(this).children("div").children("div").hide();
            // });
            $("#remarkDivList").on("mouseout",".remarkDiv",function () {
                $(this).children("div").children("div").hide();
            });



            // $(".myHref").mouseover(function () {
            //     $(this).children("span").css("color", "red");
            // });
            $("#remarkDivList").on("mouseover",".myHref",function () {
                $(this).children("span").css("color","red");
            });


            // $(".myHref").mouseout(function () {
            //     $(this).children("span").css("color", "#E6E6E6");
            // });
            $("#remarkDivList").on("mouseout",".myHref",function () {
                $(this).children("span").css("color","#E6E6E6");
            });


            // 创建备注
            $("#saveBtn").click(function () {
                // 获取参数
                var noteContent = $("#remark").val();
                var activityId = '${activity.id}';// 从request请求域
                if (noteContent == "") {
                    alert("请输入备注内容");
                    return;
                }
                // 发送请求
                $.ajax({
                    url: 'workbench/activity/saveCreateActivityRemark.do',
                    data: {
                        noteContent: noteContent,
                        activityId: activityId
                    },
                    type: 'post',
                    // 处理响应
                    dataType: 'json',
                    success: function (data) {
                        if (data.code = "1") {
                            // 成功
                            // 清空
                            $("#remark").val("");
                            // 生成评论
                            var htmlStr = "";
                            htmlStr += "<div id=\"div_" + data.retDate.id + "\" class=\"remarkDiv\" style=\"height: 60px;\">";
                            htmlStr += "<img title=\"${sessionScope.sessionUser.name}\" src=\"image/user-thumbnail.png\" style=\"width: 34px; height:34px;\">";
                            htmlStr += "<div style=\"position: relative; top: -40px; left: 40px;width: 870px; \">";
                            htmlStr += "<small style=\"color: gray;\">@${sessionScope.sessionUser.name}:" + data.retDate.createTime + "创建</small>";
                            htmlStr += "<p>" + data.retDate.noteContent + "</p>";
                            htmlStr += "<div style=\"position: relative; left: 500px; top: -30px; height: 30px; width: 100px; display: none;\">";
                            htmlStr += "<a class=\"myHref\" name=\"editA\" remarkId=\"" + data.retDate.id + "\" href=\"javascript:void(0);\"><span class=\"glyphicon glyphicon-edit\" style=\"font-size: 20px; color: #E6E6E6;\"></span></a>";
                            htmlStr += "&nbsp;&nbsp;&nbsp;&nbsp;";
                            htmlStr += "<a class=\"myHref\" name=\"deleteA\" remarkId=\"" + data.retDate.id + "\" href=\"javascript:void(0);\"><span class=\"glyphicon glyphicon-remove\" style=\"font-size: 20px; color: #E6E6E6;\"></span></a>";
                            htmlStr += " </div>";
                            htmlStr += " </div>";
                            htmlStr += " </div>";
                            $("#remarkDiv").before(htmlStr); // 以追加的方式增加备注

                        } else {
                            alert(data.message);
                        }

                    }
                });
            });

            // 删除备注
            $("#remarkDivList").on("click", "a[name='deleteA']", function () {
                // 删除确认
                if (!window.confirm("确定删除吗？")){
                    return;
                }
                // 获取id
                var id = $(this).attr("remarkId");// 获取删除按钮的id
                $.ajax({
                    url: "workbench/activity/deleteActivityRemarkById.do",
                    data: {id: id},
                    type: 'post',
                    dataType: 'json',
                    success: function (data) {
                        if (data.code == "1") {
                            //删除成功
                            $("#div_" + id).remove();
                        } else {
                            //删除失败
                            alert(data.message);
                        }
                    }
                });
            });

            // 修改打开模态窗口
            $("#remarkDivList").on("click", "a[name='editA']", function () {
                // 获取活动的id和content
                var id = $(this).attr("remarkId");
                var content = $("#div_" + id + " p").text();
                // 向模态窗口赋值
                $("#edit-id").val(id);
                $("#edit-noteContent").val(content);
                // 打开模态窗口
                $("#editRemarkModal").modal("show");
            });

            // 修改保存
            $("#updateRemarkBtn").click(function () {
                // 获取参数
                var id = $("#edit-id").val();
                var noteContent = $.trim($("#edit-noteContent").val());
                // 表单验证
                if (noteContent == "") {
                    alert("请输入备注内容");
                    return;
                }
                // 转发请求
                $.ajax({
                    url: 'workbench/activity/saveEditActivityRemark.do',
                    data: {id: id, noteContent: noteContent},
                    type: 'post',
                    dataType: 'json',
                    success: function (data) {
                        if (data.code == "1") {
                            $("#editRemarkModal").modal("hide");
                            // 刷新当前的备注数据
                            $("#div_" + id + " p").text(noteContent);
                            $("#div_" + id + " small").text("@${sessionScope.sessionUser.name}:" + data.retDate.editTime + "修改");
                        } else {
                            //失败
                            alert(data.message);
                        }
                    }
                });
            });


        });

    </script>

</head>
<body>

<!-- 修改市场活动备注的模态窗口 -->
<div class="modal fade" id="editRemarkModal" role="dialog">
    <%-- 备注的id --%>
    <input type="hidden" id="edit-id" value="">
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
                    <div class="form-group">
                        <label for="edit-describe" class="col-sm-2 control-label">内容</label>
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


<!-- 返回按钮 -->
<div style="position: relative; top: 35px; left: 10px;">
    <a href="javascript:void(0);" onclick="window.history.back();"><span class="glyphicon glyphicon-arrow-left"
                                                                         style="font-size: 20px; color: #DDDDDD"></span></a>
</div>

<!-- 大标题 -->
<div style="position: relative; left: 40px; top: -30px;">
    <div class="page-header">
        <h3>市场活动-${activity.name} <small>${requestScope.get("activity").startDate}
            ~ ${requestScope.get("activity").endDate}</small></h3>
    </div>

</div>

<br/>
<br/>
<br/>

<!-- 详细信息 -->
<div style="position: relative; top: -70px;">
    <div style="position: relative; left: 40px; height: 30px;">
        <div style="width: 300px; color: gray;">所有者</div>
        <div style="width: 300px;position: relative; left: 200px; top: -20px;">
            <b>${requestScope.get("activity").owner}</b></div>
        <div style="width: 300px;position: relative; left: 450px; top: -40px; color: gray;">名称</div>
        <div style="width: 300px;position: relative; left: 650px; top: -60px;">
            <b>${requestScope.get("activity").name}</b></div>
        <div style="height: 1px; width: 400px; background: #D5D5D5; position: relative; top: -60px;"></div>
        <div style="height: 1px; width: 400px; background: #D5D5D5; position: relative; top: -60px; left: 450px;"></div>
    </div>

    <div style="position: relative; left: 40px; height: 30px; top: 10px;">
        <div style="width: 300px; color: gray;">开始日期</div>
        <div style="width: 300px;position: relative; left: 200px; top: -20px;">
            <b>${requestScope.get("activity").startDate}</b></div>
        <div style="width: 300px;position: relative; left: 450px; top: -40px; color: gray;">结束日期</div>
        <div style="width: 300px;position: relative; left: 650px; top: -60px;">
            <b>${requestScope.get("activity").endDate}</b></div>
        <div style="height: 1px; width: 400px; background: #D5D5D5; position: relative; top: -60px;"></div>
        <div style="height: 1px; width: 400px; background: #D5D5D5; position: relative; top: -60px; left: 450px;"></div>
    </div>
    <div style="position: relative; left: 40px; height: 30px; top: 20px;">
        <div style="width: 300px; color: gray;">成本</div>
        <div style="width: 300px;position: relative; left: 200px; top: -20px;">
            <b>${requestScope.get("activity").cost}</b></div>
        <div style="height: 1px; width: 400px; background: #D5D5D5; position: relative; top: -20px;"></div>
    </div>
    <div style="position: relative; left: 40px; height: 30px; top: 30px;">
        <div style="width: 300px; color: gray;">创建者</div>
        <div style="width: 500px;position: relative; left: 200px; top: -20px;">
            <b>${requestScope.get("activity").createBy}&nbsp;&nbsp;</b><small
                style="font-size: 10px; color: gray;">${requestScope.get("activity").createTime}</small></div>
        <div style="height: 1px; width: 550px; background: #D5D5D5; position: relative; top: -20px;"></div>
    </div>
    <div style="position: relative; left: 40px; height: 30px; top: 40px;">
        <div style="width: 300px; color: gray;">修改者</div>
        <div style="width: 500px;position: relative; left: 200px; top: -20px;"><b>${requestScope.get("activity").editBy}&nbsp;&nbsp;</b><small
                style="font-size: 10px; color: gray;">${requestScope.get("activity").editTime}</small></div>
        <div style="height: 1px; width: 550px; background: #D5D5D5; position: relative; top: -20px;"></div>
    </div>
    <div style="position: relative; left: 40px; height: 30px; top: 50px;">
        <div style="width: 300px; color: gray;">描述</div>
        <div style="width: 630px;position: relative; left: 200px; top: -20px;">
            <b>
                ${requestScope.get("activity").description}
            </b>
        </div>
        <div style="height: 1px; width: 850px; background: #D5D5D5; position: relative; top: -20px;"></div>
    </div>
</div>

<!-- 备注 -->
<div id="remarkDivList" style="position: relative; top: 30px; left: 40px;">
    <div class="page-header">
        <h4>备注</h4>
    </div>

    <!-- 备注2 -->
    <c:forEach items="${activityRemarksList}" var="remark">
        <div id="div_${remark.id}" class="remarkDiv" style="height: 60px;">
            <img title="${remark.createBy}" src="image/user-thumbnail.png" style="width: 35px; height:35px;">
            <div style="position: relative; top: -40px; left: 40px;width: 870px; ">
                <small style="color: gray;">@${remark.editFlag=='1'?remark.editBy:remark.createBy}:${remark.editFlag=='1'?remark.editTime:remark.createTime}${remark.editFlag=='1'?'修改':'创建'}</small>
                <p>${remark.noteContent}</p>
                <div style="position: relative; left: 500px; top: -30px; height: 30px; width: 100px; display: none;">
                    <a class="myHref" name="editA" remarkId="${remark.id}" href="javascript:void(0);"><span
                            class="glyphicon glyphicon-edit" style="font-size: 20px; color: #E6E6E6;"></span></a>
                    &nbsp;&nbsp;&nbsp;&nbsp;
                    <a class="myHref" name="deleteA" remarkId="${remark.id}" href="javascript:void(0);"><span
                            class="glyphicon glyphicon-remove" style="font-size: 20px; color: #E6E6E6;"></span></a>
                </div>
            </div>
        </div>
    </c:forEach>
    <div id="remarkDiv" style="background-color: #E6E6E6; width: 870px; height: 90px;">
        <form role="form" style="position: relative;top: 10px; left: 10px;">
            <textarea id="remark" class="form-control" style="width: 850px; resize : none;" rows="2"
                      placeholder="添加备注..."></textarea>
            <p id="cancelAndSaveBtn" style="position: relative;left: 737px; top: 10px; display: none;">
                <button id="cancelBtn" type="button" class="btn btn-default">取消</button>
                <button id="saveBtn" type="button" class="btn btn-primary">保存</button>
            </p>
        </form>
    </div>
</div>
<div style="height: 200px;"></div>
</body>
</html>