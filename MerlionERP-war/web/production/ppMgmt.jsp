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
        <title>Production Plan Mgmt</title>
        <%@include file = "../global/global_include.jsp" %>
        <script type="text/javascript">
            function updateAction(value,rowData,rowIndex) {
                return '<input type="button" onclick="goUpdate('+rowData.id+');" value="Update"/>';  
            }
            
            function goUpdate(id){
                window.parent.addTab('update_pp'+id,'Update Production Plan '+id,'<%=GVPRODUCTION.UPDATE_PP%>?id='+id);
            }
            
            function weeklyAction(value,rowData,rowIndex) {
                return '<input type="button" onclick="goWeekly('+rowData.id+');" value="View in Weeks"/>';  
            }
            
            function goWeekly(id){
                window.parent.addTab('weekly_pp'+id,'Weekly Production Plan '+id,'<%=GVPRODUCTION.WEEKLY_PP%>?id='+id);
            }
        </script>
    </head>
    <body>
        <div style="padding:3px 2px;border-bottom:1px solid #ccc"><h2>Production Plan Management</h2></div> 
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
                   url="../ProductionPlanServlet?action=loadTable"   
                   toolbar="#tb"
                   singleSelect="true" fitColumns="true" pagination="true"
                   rownumbers="false">  
                <thead>  
                    <tr> 
                        <th field="id" width="5%">ID</th>
                        <th field="year" width="10%">Year</th>
                        <th field="month" width="10%">Month</th>
                        <th field="working_days" width="10%">Working Days</th>
                        <th field="capacity" width="10%">Capacity</th>
                        <th field="utilization" width="10%">Utilization</th>
                        <th field="ot_capacity" width="10%">OT Capacity</th>
                        <th field="ot_utilization" width="10%">OT Utilization</th>
                        <th field="update_action" width="10%" formatter="updateAction"></th>
                        <th field="weekly_action" width="15%" formatter="weeklyAction"></th>
                    </tr>  
                </thead>  
            </table> 
        </div>
    </body>
</html>
