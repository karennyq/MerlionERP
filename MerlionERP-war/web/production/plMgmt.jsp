<%-- 
    Document   : plMgmt
    Created on : Sep 30, 2011, 10:21:22 AM
    Author     : Randy
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="util.GVPRODUCTION"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Production Line Mgmt</title>
        <%@include file = "../global/global_include.jsp" %>
        <script type="text/javascript">
            function updateAction(value,rowData,rowIndex) {
                return '<input type="button" onclick="goUpdate('+rowData.production_line_id+');" value="Update"/>';  
            }
            
            function goUpdate(production_line_id){
                window.parent.addTab('update_pl'+production_line_id,'Update Production Line '+production_line_id,'<%=GVPRODUCTION.UPDATE_PL%>?production_line_id='+production_line_id);
            }
        </script>
    </head>
    <body>
        <div style="padding:3px 2px;border-bottom:1px solid #ccc"><h2>Production Line Management</h2></div>
        <br/>
        <div id="tb" style="padding:5px;height:auto">  
            <div style="margin-bottom:5px">  
                <a href="javascript:void(0)" onclick="javascript:window.parent.addTab('add_pl','Create Production Line','<%=GVPRODUCTION.CREATE_PL%>');" class="easyui-linkbutton" iconCls="icon-add" plain="true">Create Production Line</a>   
            </div>  
            <div>
                <table cellpadding="5">
                    <tr>
                        <td>Production Line Name:</td>
                        <td>
                            <input id="production_line_name" name="production_line_name" type="text"/>
                        </td>
                        <td><a href="#" class="easyui-linkbutton" iconCls="icon-search"></a></td>
                    </tr>
                </table>
            </div>  
        </div>  
        <div fit="true" style="width:100%; height:500px">
            <table id="tt" class="easyui-datagrid" fit="true" style="width:100%;"  
                   url="../ProductionLineServlet?action=loadTable"   
                   toolbar="#tb"
                   singleSelect="true" fitColumns="true" pagination="true"
                   rownumbers="false">  
                <thead>  
                    <tr> 
                        <th field="production_line_id" width="45%">Production Line ID</th>  
                        <th field="production_line_name" width="45%">Production Line Name</th>
                        <th field="update_action" width="10%" formatter="updateAction"></th>
                    </tr>  
                </thead>  
            </table> 
        </div>
    </body>
</html>
