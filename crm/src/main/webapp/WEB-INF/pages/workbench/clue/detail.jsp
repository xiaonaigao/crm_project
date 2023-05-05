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
                    $("#remark").val("");
                    cancelAndSaveBtnDefault = false;
                }
            });

            $("#cancelBtn").click(function () {
                //显示
                $("#cancelAndSaveBtn").hide();
                //设置remarkDiv的高度为130px
                $("#remarkDiv").css("height", "90px");
                cancelAndSaveBtnDefault = true;
            });

            $("#remarkDivList").on("mouseover", ".remarkDiv", function () {
                $(this).children("div").children("div").show();
            });

            $("#remarkDivList").on("mouseout", ".remarkDiv", function () {
                $(this).children("div").children("div").hide();
            });

            $("#remarkDivList").on("mouseover", ".myHref", function () {
                $(this).children("span").css("color", "red");
            });

            $("#remarkDivList").on("mouseout", ".myHref", function () {
                $(this).children("span").css("color", "#E6E6E6");
            });

            /**
             * 创建线索评论
             */
            $("#saveClueRemarkBtn").click(function () {
                // 获取参数
                var clueId = '${clue.id}';
                var noteContent = $("#remark").val();
                // 表单验证
                if (noteContent == "") {
                    alert("输入备注内容");
                    return;
                }
                // 发送请求
                $.ajax({
                    url: 'workbench/clue/saveCreateClueRemark.do',
                    data: {clueId: clueId, noteContent: noteContent},
                    type: 'post',
                    dataType: 'json',
                    success: function (data) {
                        if (data.code == "1") {
                            // 清空
                            $("#remark").val("");
                            // 成功
                            var htmlStr = "";
                            htmlStr += "<div id=\"div_" + data.retDate.id + "\" class=\"remarkDiv\" style=\"height: 60px;\">";
                            htmlStr += "<img title=\"${sessionUser.name}\" src=\"image/user-thumbnail.png\" style=\"width: 35px; height:35px;\">";
                            htmlStr += "<div style=\"position: relative; top: -40px; left: 40px;width: 870px; \">";
                            htmlStr += "<small style=\"color: gray;\">@${sessionUser.name}:" + data.retDate.createTime + "创建</small>";
                            htmlStr += "<p>" + data.retDate.noteContent + "</p>";
                            htmlStr += "<div style=\"position: relative; left: 500px; top: -30px; height: 30px; width: 100px; display: none;\">";
                            htmlStr += "	<a class=\"myHref\" name=\"editA\" remarkId=\"" + data.retDate.id + "\" href=\"javascript:void(0);\"><span class=\"glyphicon glyphicon-edit\" style=\"font-size: 20px; color: #E6E6E6;\"></span></a>";
                            htmlStr += "&nbsp;&nbsp;&nbsp;&nbsp;";
                            htmlStr += "<a class=\"myHref\" name=\"deleteA\" remarkId=\"" + data.retDate.id + "\" href=\"javascript:void(0);\"><span class=\"glyphicon glyphicon-remove\" style=\"font-size: 20px; color: #E6E6E6;\"></span></a>";
                            htmlStr += "</div>";
                            htmlStr += "</div>";
                            htmlStr += "</div>";
                            $("#remarkDiv").before(htmlStr);
                        } else {
                            // 失败
                            alert(data.message);
                        }
                    }

                });
            });

            /**
             * 删除线索
             */
            $("#remarkDivList").on("click", "a[name='deleteA']", function () {
                if (!window.confirm("确认删除吗？")) {
                    return;
                }
                //获取参数
                var id = $(this).attr("remarkId");
                $.ajax({
                    url: 'workbench/clue/deleteClueRemarkById.do',
                    data: {id: id},
                    type: 'post',
                    dataType: 'json',
                    success: function (data) {
                        if (data.code == "1") {
                            $("#div_" + id).remove();
                        } else {
                            alert(data.message);
                        }
                    }
                });

            });
            /**
             * 修改评论
             */
            $("#remarkDivList").on("click", "a[name='editA']", function () {
                // 收集参数
                var id = $(this).attr("remarkId");
                var content = $("#div_" + id + " p").text();
                // 赋值
                $("#edit-id").val(id);
                $("#edit-noteContent").val(content);
                $("#editRemarkModal").modal("show");
            });
            $("#updateRemarkBtn").click(function () {
                // 收集参数
                var id = $("#edit-id").val();
                var noteContent = $("#edit-noteContent").val();
                // 表单验证
                if (noteContent == "") {
                    alert("请输入修改的备注");
                    return;
                }
                $.ajax({
                    url: 'workbench/clue/editClueRemarkById.do',
                    data: {id: id, noteContent: noteContent},
                    type: 'post',
                    dataType: 'json',
                    success: function (data) {
                        if (data.code == "1") {
                            // 成功
                            $("#div_" + id + " p").text(noteContent);
                            $("#div_" + id + " small").text("@${sessionScope.sessionUser.name}:" + data.retDate.editTime + "修改");
                            $("#editRemarkModal").modal("hide");
                        } else {
                            //失败
                            alert(data.message);
                        }
                    }
                });
            });
            /**
             * 关联市场活动
             */
            // 1给绑定市场活动按钮添加单击事件
            $("#boundActivityBtn").click(function () {
                // 初始化
                $("#searchActivityTxt").val(""); //清空搜索框
                $("#tBody").html(""); // 清空之前显示的数据
                $("#checkAll").prop("checked", false); // 全选取消
                // 显示模态窗口
                $("#boundModal").modal("show");
            });

            // 2搜索框添加单击事件,在键盘的键被按下的时候,接下来触发
            $("#searchActivityTxt").keyup(function () {
                // 获取参数
                var clueId = '${clue.id}';
                var activityName = $("#searchActivityTxt").val();
                // 发送请求
                $.ajax({
                    url: 'workbench/clue/queryActivityForDetailByNameAndClueId.do',
                    data: {clueId: clueId, activityName: activityName},
                    type: 'post',
                    dataType: 'json',
                    success: function (data) {
                        var htmlStr = "";
                        $.each(data, function (index, obj) {
                            htmlStr += "<tr>";
                            htmlStr += "<td><input type=\"checkbox\" value=\"" + obj.id + "\"/></td>";
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

            // 给全选按钮添加事件实现全选（全选按钮在线索数据被查出来之前已经生成了，所以直接给固有元素全选按钮添加事件即可）
            $("#checkAll").click(function () {
                // 如果全选按钮选中，则列表中所有按钮都选中（操作tBody下面的所有子标签input，设置为当前（this）全选按钮的状态）
                $("#tBody input[type='checkbox']").prop("checked", this.checked);
            });

            // 当线索标签不是全选时取消全选按钮
            $("#tBody").on("click", "input[type='checkbox']", function () {
                // 设置全选标签状态，如果当前所有标签数和选中标签数相等，则全选，否则不全选
                $("#checkAll").prop("checked",
                    $("#tBody input[type='checkbox']").size() == $("#tBody input[type='checkbox']:checked").size());
            });

            // 3 点击关联
            $("#saveBoundBtn").click(function () {
                // 收集参数
                // 获取所有选中的checkbox
                var checkids = $("#tBody input[type='checkbox']:checked");
                var clueId = '${clue.id}';
                // 表单验证
                if (checkids.size() == 0) {
                    alert("输入要关联的市场活动");
                    return;
                }
                // 拼接参数
                var ids = "";
                $.each(checkids, function () {// activityId=xxxx&activityId=xxxx&....&activityId=xxxx&
                    ids += "activityId=" + this.value + "&";
                });
                ids += "clueId=" + clueId;
                // 发送请求
                $.ajax({
                    url: 'workbench/clue/saveBound.do',
                    data: ids,
                    type: 'post',
                    dataType: 'json',
                    success: function (data) {
                        if (data.code == "1") {
                            // 成功
                            $("#boundModal").modal("hide");
                            var htmlStr = "";
                            $.each(data.retDate, function (index, obj) {
                                htmlStr += "<tr id=\"tr_" + obj.id + "\">";
                                htmlStr += "<td>" + obj.name + "</td>";
                                htmlStr += "<td>" + obj.startDate + "</td>";
                                htmlStr += "<td>" + obj.endDate + "</td>";
                                htmlStr += "<td>" + obj.owner + "</td>";
                                htmlStr += "<td><a href=\"javascript:void(0);\" activityId=\"" + obj.id + "\"  style=\"text-decoration: none;\"><span class=\"glyphicon glyphicon-remove\"></span>解除关联</a></td>";
                                htmlStr += "</tr>";
                            });
                            $("#relationTBody").append(htmlStr);
                        } else {
                            alert(data.message);
                        }
                    }
                });

            });

            /**
             * 解除关联
             */
            $("#relationTBody").on("click", "a", function () {
                // 收集参数
                var activityId = $(this).attr("activityId"); // 市场活动id
                var clueId = "${clue.id}"; // 线索id
                // 确定
                if (!window.confirm("确定解除绑定吗？")){
                    return;
                }
                // 发送请求
                $.ajax({
                    url:'workbench/clue/deleteBound.do',
                    data:{activityId:activityId,clueId:clueId},
                    type:'post',
                    dataType:'json',
                    success:function (data) {
                        if (data.code=="1"){
                            //成功
                            $("#tr_"+activityId).remove();
                        }else{
                            alert(data.message);
                        }
                    }

                });
            });

            /**
             * 线索转换
             */
            $("#convertClueBtn").click(function () {
                window.location.href="workbench/clue/toConvert.do?id=${clue.id}";
            });
        });

    </script>

</head>
<body>

<!-- 关联市场活动的模态窗口 -->
<div class="modal fade" id="boundModal" role="dialog">
    <div class="modal-dialog" role="document" style="width: 80%;">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal">
                    <span aria-hidden="true">×</span>
                </button>
                <h4 class="modal-title">关联市场活动</h4>
            </div>
            <div class="modal-body">
                <div class="btn-group" style="position: relative; top: 18%; left: 8px;">
                    <form class="form-inline" role="form">
                        <div class="form-group has-feedback">
                            <input type="text" id="searchActivityTxt" class="form-control" style="width: 300px;"
                                   placeholder="请输入市场活动名称，支持模糊查询">
                            <span class="glyphicon glyphicon-search form-control-feedback"></span>
                        </div>
                    </form>
                </div>
                <table id="activityTable" class="table table-hover" style="width: 900px; position: relative;top: 10px;">
                    <thead>
                    <tr style="color: #B3B3B3;">
                        <td><input type="checkbox" id="checkAll"/></td>
                        <td>名称</td>
                        <td>开始日期</td>
                        <td>结束日期</td>
                        <td>所有者</td>
                        <td></td>
                    </tr>
                    </thead>
                    <tbody id="tBody">

                    </tbody>
                </table>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-default" data-dismiss="modal">取消</button>
                <button type="button" class="btn btn-primary" id="saveBoundBtn">关联</button>
            </div>
        </div>
    </div>
</div>

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
        <h3>${clue.fullname} <small>${clue.company}</small></h3>
    </div>
    <div style="position: relative; height: 50px; width: 500px;  top: -72px; left: 700px;">
        <button type="button" class="btn btn-default" id="convertClueBtn" ;><span
                class="glyphicon glyphicon-retweet"></span> 转换
        </button>

    </div>
</div>

<br/>
<br/>
<br/>

<!-- 详细信息 -->
<div style="position: relative; top: -70px;">
    <div style="position: relative; left: 40px; height: 30px;">
        <div style="width: 300px; color: gray;">名称</div>
        <div style="width: 300px;position: relative; left: 200px; top: -20px;">
            <b>${clue.fullname}${clue.appellation}</b></div>
        <div style="width: 300px;position: relative; left: 450px; top: -40px; color: gray;">所有者</div>
        <div style="width: 300px;position: relative; left: 650px; top: -60px;"><b>${clue.owner}</b></div>
        <div style="height: 1px; width: 400px; background: #D5D5D5; position: relative; top: -60px;"></div>
        <div style="height: 1px; width: 400px; background: #D5D5D5; position: relative; top: -60px; left: 450px;"></div>
    </div>
    <div style="position: relative; left: 40px; height: 30px; top: 10px;">
        <div style="width: 300px; color: gray;">公司</div>
        <div style="width: 300px;position: relative; left: 200px; top: -20px;"><b>${clue.company}</b></div>
        <div style="width: 300px;position: relative; left: 450px; top: -40px; color: gray;">职位</div>
        <div style="width: 300px;position: relative; left: 650px; top: -60px;"><b>${clue.job}</b></div>
        <div style="height: 1px; width: 400px; background: #D5D5D5; position: relative; top: -60px;"></div>
        <div style="height: 1px; width: 400px; background: #D5D5D5; position: relative; top: -60px; left: 450px;"></div>
    </div>
    <div style="position: relative; left: 40px; height: 30px; top: 20px;">
        <div style="width: 300px; color: gray;">邮箱</div>
        <div style="width: 300px;position: relative; left: 200px; top: -20px;"><b>${clue.email}</b></div>
        <div style="width: 300px;position: relative; left: 450px; top: -40px; color: gray;">公司座机</div>
        <div style="width: 300px;position: relative; left: 650px; top: -60px;"><b>${clue.phone}</b></div>
        <div style="height: 1px; width: 400px; background: #D5D5D5; position: relative; top: -60px;"></div>
        <div style="height: 1px; width: 400px; background: #D5D5D5; position: relative; top: -60px; left: 450px;"></div>
    </div>
    <div style="position: relative; left: 40px; height: 30px; top: 40px;">
        <div style="width: 300px; color: gray;">线索状态</div>
        <div style="width: 300px;position: relative; left: 200px; top: -20px;"><b>${clue.state}</b></div>
        <div style="width: 300px;position: relative; left: 450px; top: -40px; color: gray;">线索来源</div>
        <div style="width: 300px;position: relative; left: 650px; top: -60px;"><b>${clue.source}</b></div>
        <div style="height: 1px; width: 400px; background: #D5D5D5; position: relative; top: -60px;"></div>
        <div style="height: 1px; width: 400px; background: #D5D5D5; position: relative; top: -60px; left: 450px;"></div>
    </div>
    <div style="position: relative; left: 40px; height: 30px; top: 50px;">
        <div style="width: 300px; color: gray;">创建者</div>
        <div style="width: 500px;position: relative; left: 200px; top: -20px;"><b>${clue.createBy}&nbsp;&nbsp;</b><small
                style="font-size: 10px; color: gray;">${clue.createTime}</small></div>
        <div style="height: 1px; width: 550px; background: #D5D5D5; position: relative; top: -20px;"></div>
    </div>
    <div style="position: relative; left: 40px; height: 30px; top: 60px;">
        <div style="width: 300px; color: gray;">修改者</div>
        <div style="width: 500px;position: relative; left: 200px; top: -20px;"><b>${clue.editBy}&nbsp;&nbsp;</b><small
                style="font-size: 10px; color: gray;">${clue.editTime}</small></div>
        <div style="height: 1px; width: 550px; background: #D5D5D5; position: relative; top: -20px;"></div>
    </div>
    <div style="position: relative; left: 40px; height: 30px; top: 70px;">
        <div style="width: 300px; color: gray;">描述</div>
        <div style="width: 630px;position: relative; left: 200px; top: -20px;">
            <b>
                ${clue.description}
            </b>
        </div>
        <div style="height: 1px; width: 850px; background: #D5D5D5; position: relative; top: -20px;"></div>
    </div>
    <div style="position: relative; left: 40px; height: 30px; top: 80px;">
        <div style="width: 300px; color: gray;">联系纪要</div>
        <div style="width: 630px;position: relative; left: 200px; top: -20px;">
            <b>
                ${clue.contactSummary}
            </b>
        </div>
        <div style="height: 1px; width: 850px; background: #D5D5D5; position: relative; top: -20px;"></div>
    </div>
    <div style="position: relative; left: 40px; height: 30px; top: 90px;">
        <div style="width: 300px; color: gray;">下次联系时间</div>
        <div style="width: 300px;position: relative; left: 200px; top: -20px;"><b>${clue.nextContactTime}</b></div>
        <div style="height: 1px; width: 400px; background: #D5D5D5; position: relative; top: -20px; "></div>
    </div>
    <div style="position: relative; left: 40px; height: 30px; top: 100px;">
        <div style="width: 300px; color: gray;">详细地址</div>
        <div style="width: 630px;position: relative; left: 200px; top: -20px;">
            <b>
                ${clue.address}
            </b>
        </div>
        <div style="height: 1px; width: 850px; background: #D5D5D5; position: relative; top: -20px;"></div>
    </div>
</div>

<!-- 备注 -->
<div id="remarkDivList" style="position: relative; top: 40px; left: 40px;">
    <div class="page-header">
        <h4>备注</h4>
    </div>


    <!-- 备注2 -->
    <c:forEach items="${clueRemarkList}" var="remark">
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
                <button type="button" class="btn btn-primary" id="saveClueRemarkBtn">保存</button>
            </p>
        </form>
    </div>
</div>

<!-- 市场活动 -->
<div>
    <div style="position: relative; top: 60px; left: 40px;">
        <div class="page-header">
            <h4>市场活动</h4>
        </div>
        <div style="position: relative;top: 0px;">
            <table class="table table-hover" style="width: 900px;">
                <thead>
                <tr style="color: #B3B3B3;">
                    <td>名称</td>
                    <td>开始日期</td>
                    <td>结束日期</td>
                    <td>所有者</td>
                    <td></td>
                </tr>
                </thead>
                <tbody id="relationTBody">
                <c:forEach items="${activityList}" var="activity">
                    <tr id="tr_${activity.id}">
                        <td >${activity.name}</td>
                        <td>${activity.startDate}</td>
                        <td>${activity.endDate}</td>
                        <td>${activity.owner}</td>
                        <td><a href="javascript:void(0);" activityId="${activity.id}"
                               style="text-decoration: none;"><span
                                class="glyphicon glyphicon-remove"></span>解除关联</a></td>
                    </tr>
                </c:forEach>
                </tbody>
            </table>
        </div>

        <div>
            <a href="javascript:void(0);" id="boundActivityBtn"
               style="text-decoration: none;"><span class="glyphicon glyphicon-plus"></span>关联市场活动</a>
        </div>
    </div>
</div>


<div style="height: 200px;"></div>
</body>
</html>