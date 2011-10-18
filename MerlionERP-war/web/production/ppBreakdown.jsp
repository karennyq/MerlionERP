<%-- 
    Document   : weeklyPPMgmt
    Created on : Oct 16, 2011, 2:10:43 PM
    Author     : Randy
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="util.GVPRODUCTION"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Monthly Production Plan Breakdown</title>
        <%@include file = "../global/global_include.jsp" %>
        <script type="text/javascript">
            function dailyAction(value,rowData,rowIndex) {
                return '<input type="button" onclick="goDaily('+rowData.id+');" value="View Daily"/>';  
            }
            
            function goDaily(wo_id){
                $('#dailyTable').datagrid({  
                    url:'../ProductionPlanServlet?action=loadDailyTable&wo_id=' + wo_id
                });
                $('#dailyTable').datagrid('reload');
            }
            
            function updateAction(value,rowData,rowIndex) {
                return '<input type="button" onclick="goUpdate('+rowData.id+');" value="Update"/>';  
            }
            
            function goUpdate(do_id){
                window.parent.addTab('update_dpp'+do_id,'Update Daily Production Plan: '+do_id,'<%=GVPRODUCTION.UPDATE_DPP%>?do_id='+do_id);
            }
        </script>
    </head>
    <body>
        <div style="padding:3px 2px;border-bottom:1px solid #ccc"><h2>Weekly Breakdown</h2></div> 
        <br/>
        <div fit="true" style="width:100%; height:190px">
            <table id="weeklyTable" class="easyui-datagrid" fit="true" style="width:100%;"  
                   url="../ProductionPlanServlet?action=loadWeeklyTable&mo_id=<%= request.getParameter("mo_id")%>"   
                   singleSelect="true" fitColumns="true" pagination="false"
                   rownumbers="false">  
                <thead>  
                    <tr> 
                        <th field="id" width="10%">ID</th>
                        <th field="week" width="10%">Week</th>
                        <th field="working_days" width="10%">Working Days</th>
                        <th field="capacity" width="10%">Capacity</th>
                        <th field="utilization" width="10%">Utilization</th>
                        <th field="ot_capacity" width="10%">OT Capacity</th>
                        <th field="ot_utilization" width="10%">OT Utilization</th>
                        <th field="daily_action" width="30%" formatter="dailyAction"></th>
                    </tr>  
                </thead>  
            </table> 
        </div>
        <div style="padding:3px 2px;border-bottom:1px solid #ccc"><h2>Daily Breakdown</h2></div> 
        <br/>
        <div fit="true" style="width:100%; height:190px">
            <table id="dailyTable" class="easyui-datagrid" fit="true" style="width:100%;"
                   singleSelect="true" fitColumns="true" pagination="false"
                   rownumbers="false">  
                <thead>  
                    <tr>
                        <th field="id" width="10%">ID</th>
                        <th field="day" width="10%">Day</th>
                        <th field="working_days" width="10%">Working Days</th>
                        <th field="capacity" width="10%">Capacity</th>
                        <th field="utilization" width="10%">Utilization</th>
                        <th field="ot_capacity" width="10%">OT Capacity</th>
                        <th field="ot_utilization" width="10%">OT Utilization</th>
                        <th field="update_action" width="30%" formatter="updateAction"></th>
                    </tr>
                </thead>  
            </table> 
        </div>
    </body>
</html>
