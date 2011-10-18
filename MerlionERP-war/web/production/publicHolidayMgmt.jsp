<%-- 
    Document   : phMgmt
    Created on : Sep 17, 2011, 12:16:49 PM
    Author     : Randy
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="util.GVPRODUCTION"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Public Holiday Mgmt</title>
        <%@include file = "../global/global_include.jsp" %>
        <script type="text/javascript">
            function updateAction(value,rowData,rowIndex) {
                return '<input type="button" onclick="goUpdate('+rowData.pub_holi_id+');" value="Update"/>';  
            }
            
            function goUpdate(pub_holi_id){
                window.parent.addTab('update_ph'+pub_holi_id,'Update Public Holiday '+pub_holi_id,'<%=GVPRODUCTION.UPDATE_PH%>?pub_holi_id='+pub_holi_id);
            }
            
            function deleteAction(value,rowData,rowIndex) {
                return '<input type="button" onclick="goDelete('+rowData.pub_holi_id+');" value="Delete"/>';  
            }
            
            function goDelete(pub_holi_id){
                window.parent.$.messager.confirm("Confirm", 'Delete Public Holiday ' + pub_holi_id + '?', function(r){
                    if (r){
                        $.ajax({
                            url: '../PublicHolidayServlet',
                            data: 'action=deletePH&pub_holi_id=' + pub_holi_id,
                            dataType: 'json',
                            cache: false,
                            success: function(data) {
                                alertMsg(data);
                                $('#tt').datagrid('reload');
                            }
                        });
                    }
                });
            }
        </script>
    </head>
    <body>
        <div style="padding:3px 2px;border-bottom:1px solid #ccc"><h2>Public Holiday Management</h2></div> 
        <br/>
        <div id="tb" style="padding:5px;height:auto">  
            <div style="margin-bottom:5px">  
                <a href="javascript:void(0)" onclick="javascript:window.parent.addTab('add_ph','Create Public Holiday','<%=GVPRODUCTION.CREATE_PH%>');" class="easyui-linkbutton" iconCls="icon-add" plain="true">Create Public Holiday</a>   
            </div>  
            <div>
                <table cellpadding="5">
                    <tr>
                        <td>Public Holiday Date:</td>
                        <td>
                            <input type="text"/>
                        </td>
                        <td>Public Holiday Name:</td>
                        <td>
                            <input  type="text"/>
                        </td>
                        <td><a href="#" class="easyui-linkbutton" iconCls="icon-search"></a></td>
                    </tr>
                </table>
            </div>  
        </div>  
        <div fit="true" style="width:100%; height:500px">
            <table id="tt" class="easyui-datagrid" fit="true" style="width:100%;"  
                   url="../PublicHolidayServlet?action=loadTable"   
                   toolbar="#tb"
                   singleSelect="true" fitColumns="true" pagination="true"
                   rownumbers="false">  
                <thead>  
                    <tr> 
                        <th field="pub_holi_id" width="5%">ID</th>  
                        <th field="ph_date" width="15%">Public Holiday Date</th>
                        <th field="ph_name" width="60%">Public Holiday Name</th>
                        <th field="update_action" width="10%" formatter="updateAction"></th>
                        <th field="delete_action" width="10%" formatter="deleteAction"></th>
                    </tr>  
                </thead>  
            </table> 
        </div>
    </body>
</html>
