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
            // 1加载首页数据
            loadAllTran(1, 10);
            // 2搜索按钮
            $("#searchTranBtn").click(function () {
                loadAllTran(1, 10);
            });
            // 3创建按钮
            $("#createTranBtn").click(function () {
                window.location.href='workbench/transaction/saveIndex.do';
            });
            
            // 4删除按钮
            $("#deleteTranBtn").click(function () {
                // 收集参数checkbox
                var ids = $("#tBody input[type='checkbox']:checked");
                // 删除为空
                if (ids.size()==0){
                    alert("请选择删除的事件");
                    return;
                }
                if (window.confirm("确定删除吗？")){
                    // 点击确认
                    // 遍历数据 this就是选择的对象 this.value就是值
                    // 把tranids=xxx&tranIds=xxx
                    var tranIds="";
                    $.each(ids,function () {
                        tranIds+="tranIds="+this.value+"&";
                    });
                    // 删除最后的&
                    tranIds = tranIds.substr(0,tranIds.length-1);

                    // 发送请求
                    $.ajax({
                        url:'workbench/transaction/deleteTran.do',
                        data:tranIds,
                        type:'post',
                        dataType: 'json',
                        success:function (data) {
                            if (data.code=='1'){
                                // 删除成功--当前页
                                loadAllTran($("#pagDiv").bs_pagination('getOption', 'currentPage'),
                                    $("#pagDiv").bs_pagination('getOption', 'rowsPerPage'));
                            }else{
                                alert(code.message);
                            }
                        }
                    });
                }
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

            // 5.修改
            $("#editTranBtn").click(function () {
                // 收集选择的checkBox
                var checkedIds = $("#tBody input[type='checkbox']:checked");
                if (checkedIds.size()==0){
                    alert("请选择修改的交易");
                    return;
                }
                // 选择一个交易
                if (checkedIds.size()==1){
                   var tranId = checkedIds[0].value;
                   window.location.href='workbench/transaction/searchTranForId.do?tranId='+tranId;
                }else{
                    alert("只能修改1条交易");
                    return;
                }
            });


            

        });

        // 搜索交易--主页展示
        function loadAllTran(pageNo, pageSize) {
            // 收集查询参数
            var owner = $.trim($("#query-owner").val());
            var name =$.trim($("#query-name").val());
            var customerName = $.trim($("#query-customername").val());
            var stage = $("#query-stage option:selected").val();
            var type = $("#query-type option:selected").val();
            var source = $("#query-source option:selected").val();
            var contactName = $.trim($("#query-contactname").val());
            // 发送请求
            $.ajax({
                url: 'workbench/transaction/transactionListIndex.do',
                data: {
                    pageNo: pageNo,
                    pageSize: pageSize,
                    owner:owner,
                    name:name,
                    customerName:customerName,
                    stage:stage,
                    type:type,
                    source:source,
                    contactName:contactName
                },
                type: 'post',
                dataType: 'json',
                success: function (data) {
                    var htmlStr = "";
                    $.each(data.tranAllList, function (index, obj) {
                        htmlStr += "<tr>";
                        htmlStr += "<td><input type=\"checkbox\" style='width: 15px;height: 15px' value=\"" + obj.id + "\"/></td>";
                        htmlStr += "<td><a style=\"text-decoration: none; cursor: pointer;\"onclick=\"window.location.href='workbench/transaction/queryTranFortranId.do?tranId="+obj.id+"';\">" + obj.name + "</a></td>";
                        htmlStr += "<td>" + obj.customerId + "</td>";
                        htmlStr += "<td>" + obj.stage + "</td>";
                        htmlStr += " <td>" + obj.type + "</td>";
                        htmlStr += " <td>" + obj.owner + "</td>";
                        htmlStr += " <td>" + obj.source + "</td>";
                        htmlStr += " <td>" + obj.contactsId + "</td>";
                        htmlStr += " </tr>";
                    });
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
                            loadAllTran(pageObj.currentPage, pageObj.rowsPerPage);
                        }
                    });
                }

            });
        }

    </script>
</head>
<body>


<div>
    <div style="position: relative; left: 10px; top: -10px;">
        <div class="page-header">
            <h3>交易列表</h3>
        </div>
    </div>
</div>

<div style="position: relative; top: -20px; left: 0px; width: 100%; height: 100%;">

    <div style="width: 100%; position: absolute;top: 5px; left: 10px;">

        <div class="btn-toolbar" role="toolbar" style="height: 80px;">
            <form class="form-inline" role="form" style="position: relative;top: 8%; left: 5px;">

                <div class="form-group">
                    <div class="input-group">
                        <div class="input-group-addon">所有者</div>
                        <input class="form-control" type="text" id="query-owner">
                    </div>
                </div>

                <div class="form-group">
                    <div class="input-group">
                        <div class="input-group-addon">名称</div>
                        <input class="form-control" type="text" id="query-name">
                    </div>
                </div>

                <div class="form-group">
                    <div class="input-group">
                        <div class="input-group-addon">客户名称</div>
                        <input class="form-control" type="text" id="query-customername">
                    </div>
                </div>

                <br>

                <div class="form-group">
                    <div class="input-group">
                        <div class="input-group-addon">阶段</div>
                        <select class="form-control" id="query-stage">
                            <option></option>
                            <c:forEach items="${stageList}" var="stage">
                                <option >${stage.value}</option>
                            </c:forEach>
                        </select>
                    </div>
                </div>

                <div class="form-group">
                    <div class="input-group">
                        <div class="input-group-addon">类型</div>
                        <select class="form-control" id="query-type">
                            <option></option>
                            <c:forEach items="${transactionTypeList}" var="transactionType">
                                <option >${transactionType.value}</option>
                            </c:forEach>
                        </select>
                    </div>
                </div>

                <div class="form-group">
                    <div class="input-group">
                        <div class="input-group-addon">来源</div>
                        <select class="form-control" id="query-source">
                            <option></option>
                            <c:forEach items="${sourceList}" var="source">
                                <option >${source.value}</option>
                            </c:forEach>
                        </select>
                    </div>
                </div>

                <div class="form-group">
                    <div class="input-group">
                        <div class="input-group-addon">联系人名称</div>
                        <input class="form-control" type="text" id="query-contactname">
                    </div>
                </div>

                <button type="button" class="btn btn-default" id="searchTranBtn">查询</button>

            </form>
        </div>
        <div class="btn-toolbar" role="toolbar"
             style="background-color: #F7F7F7; height: 50px; position: relative;top: 10px;">
            <div class="btn-group" style="position: relative; top: 18%;">
                <button type="button" class="btn btn-primary" id="createTranBtn"><span
                        class="glyphicon glyphicon-plus"></span> 创建
                </button>
                <button type="button" class="btn btn-default" id="editTranBtn"><span
                        class="glyphicon glyphicon-pencil"></span> 修改
                </button>
                <button type="button" class="btn btn-danger" id="deleteTranBtn"><span class="glyphicon glyphicon-minus"></span> 删除</button>
            </div>


        </div>
        <div style="position: relative;top: 10px;">
            <table class="table table-hover">
                <thead>
                <tr style="color: #B3B3B3;">
                    <td><input id="chckAll" type="checkbox"/></td>
                    <td>名称</td>
                    <td>客户名称</td>
                    <td>阶段</td>
                    <td>类型</td>
                    <td>所有者</td>
                    <td>来源</td>
                    <td>联系人名称</td>
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



