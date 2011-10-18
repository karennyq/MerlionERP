<%-- 
    Document   : rmMgmt
    Created on : Oct 7, 2011, 12:02:09 AM
    Author     : alyssia
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="util.GVPRODUCTION"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Raw Material Mgmt</title>
        <%@include file = "../global/global_include.jsp" %>
        <script type="text/javascript">
            function updateAction(value,rowData,rowIndex) {
                return '<input type="button" onclick="goUpdate('+rowData.raw_material_id+');" value="Update"/>';  
            }
            
            function goUpdate(raw_material_id){
                window.parent.addTab('update_rm'+raw_material_id,'Update Raw Material: '+raw_material_id,'<%=GVPRODUCTION.UPDATE_RM%>?raw_material_id='+raw_material_id);
            }
            
            function filterTable(){
                var raw_material_id=$('#raw_material_id').val();
                var mat_name=$('#mat_name').val();
                var f_url='../RawMaterialServlet?action=loadPage&content=table&raw_material_id='+raw_material_id+'&mat_name='+mat_name;
                $('#tt').datagrid({url:f_url});     
            }
        </script>
    </head>
    <body>
        <div style="padding:3px 2px;border-bottom:1px solid #ccc"><h2>Raw Material Management</h2></div>
        <br/>
        <div id="tb" style="padding:5px;height:auto">  
            <div style="margin-bottom:5px">  
                <a href="javascript:void(0)" onclick="javascript:window.parent.addTab('add_rm','Create Raw Material','<%=GVPRODUCTION.CREATE_RM%>');" class="easyui-linkbutton" iconCls="icon-add" plain="true">Create Raw Material</a>   
            </div>  
            <div>
                <table cellpadding="5">
                    <tr>
                        <td>Raw Material ID:</td>
                        <td>
                            <input id="raw_material_id" name="raw_material_id" type="text"/>
                        </td>
                        <td>Raw Material Name:</td>
                        <td>
                            <input id="mat_name" name="mat_name" type="text"/>
                        </td>
                        <td><a href="#" class="easyui-linkbutton" onclick="filterTable()" iconCls="icon-search"></a></td>
                    </tr>
                </table>
            </div>  
        </div>  
        <div fit="true" style="width:100%; height:500px">
            <table id="tt" class="easyui-datagrid" fit="true" style="width:100%;"  
                   url="../RawMaterialServlet?action=loadPage&content=table"   
                   toolbar="#tb"
                   singleSelect="true" fitColumns="true" pagination="true"
                   rownumbers="false">  
                <thead>  
                    <tr> 
                        <th field="raw_material_id" width="30%">Raw Material ID</th>  
                        <th field="mat_name" width="30%">Raw Material Name</th>
                        <th field="update_action" width="40%" formatter="updateAction"></th>
                    </tr>  
                </thead>  
            </table> 
        </div>
    </body>
</html>
