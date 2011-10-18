<%-- 
    Document   : ppMgmt
    Created on : Sep 21, 2011, 2:54:23 PM
    Author     : Randy
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="util.GVPRODUCTION"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Monthly Production Plan Mgmt</title>
        <%@include file = "../global/global_include.jsp" %>
        <script type="text/javascript">
            function action(value,rowData,rowIndex) {
                return '<input type="button" onclick="goUpdate('+rowData.id+');" value="Update"/>' + '<input type="button" onclick="goBreakdown('+rowData.id+');" value="Breakdown"/>';  
            }
            
            function goUpdate(mo_id){
                $.ajax({
                    type: "POST",
                    url: "../ProductionPlanServlet",
                    data: "action=checkMonthAndYear&" +
                        "mo_id=" + mo_id,
                    dataType: "xml",
                    cache: false,
                    success: function(data){
                        if ($(data).find('result').text() == 'fail') {
                            alertMsgStr("Production Plan Management", "Only future production plans can be updated from the monthly level. Please update at the daily level.", "error");
                        } else {
                            window.parent.addTab('update_mpp'+mo_id,'Update Monthly Production Plan: '+mo_id,'<%=GVPRODUCTION.UPDATE_MPP%>?mo_id='+mo_id);
                        }
                    }
                });
            }
            
            function goBreakdown(mo_id){
                window.parent.addTab('breakdown_pp'+mo_id,'Monthly Production Plan Breakdown: '+mo_id,'<%=GVPRODUCTION.BREAKDOWN_PP%>?mo_id='+mo_id);
            }
        </script>
    </head>
    <body>
        <div style="padding:3px 2px;border-bottom:1px solid #ccc"><h2>Monthly Production Plan Management</h2></div> 
        <br/>
        <div id="tb" style="padding:5px;height:auto">  
            <div style="margin-bottom:5px">  
                <a href="javascript:void(0)" onclick="javascript:window.parent.addTab('add_pp','Create Production Plan','<%=GVPRODUCTION.CREATE_PP%>');" class="easyui-linkbutton" iconCls="icon-add" plain="true">Create Production Plan</a>   
            </div>  
            <div>  
                <table cellpadding="5">
                    <td>Year:</td>
                    <td><input type="text" /></td>
                    <td>Month:</td>
                    <td><input type="text" /></td>
                    <td><a href="#" class="easyui-linkbutton" iconCls="icon-search"></a></td>
                </table>
            </div>  
        </div>  
        <div fit="true" style="width:100%; height:500px">
            <table id="tt" class="easyui-datagrid" fit="true" style="width:100%;"  
                   url="../ProductionPlanServlet?action=loadMonthlyTable"   
                   toolbar="#tb"
                   singleSelect="true" fitColumns="true" pagination="true"
                   rownumbers="false">  
                <thead>  
                    <tr> 
                        <th field="id" width="10%">ID</th>
                        <th field="year" width="10%">Year</th>
                        <th field="month" width="10%">Month</th>
                        <th field="working_days" width="10%">Working Days</th>
                        <th field="capacity" width="10%">Capacity</th>
                        <th field="utilization" width="10%">Utilization</th>
                        <th field="ot_capacity" width="10%">OT Capacity</th>
                        <th field="ot_utilization" width="10%">OT Utilization</th>
                        <th field="do_action" width="20%" formatter="action"></th>
                    </tr>  
                </thead>  
            </table> 
        </div>
    </body>
</html>
