<%-- 
    Document   : rolesMgmt
    Created on : Sep 20, 2011, 1:29:34 PM
    Author     : alyssia
--%>

<%@page import="util.GVSYSTEMADMIN"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Role Management</title>
        <%@include file = "../global/global_include.jsp" %>
        <script type="text/javascript"> 
            function goUpdate(roleID){               
                window.parent.addTab('update_role'+roleID,'Update Role: ' + roleID,'<%=GVSYSTEMADMIN.UPDATE_ROLE%>?role_id='+roleID);
            }
            
            function formatAction(value,rowData,rowIndex){      
                return '<input type="button" onclick="goUpdate('+rowData.role_id+');" value="Update"/>';   
            }
            
            function filterTable(){    
                var roleID=$('#role_id').val();
                var roleName=$('#role_name').val();
                var f_url='../RoleServlet?action=loadPage&content=table&role_id='+roleID+'&role_name='+roleName;
                $('#tt').datagrid({url:f_url});     
            }
           
            function deleteRole(roleID,rowIndex){
                window.parent.$.messager.confirm("Confirm", "Delete role " + roleID + "?", function(r){
                    if (r){
                        $.ajax({
                            type: "POST",
                            url: "../RoleServlet",
                            data: "action=deleteRole&role_id="+roleID,
                            dataType: "json",
                            cache: false,
                            success: function(data){
                                if(data.type!='error'){
                                    $('#tt').datagrid('reload');
                                    var f = window.parent;
                                    try{
                                        f.role_mgmt.$('#tt').datagrid('reload');
                                    }
                                    catch(err){
                                    }
                                }
                                alertMsg(data);    
                            }
                        });    
                    }     
                });
            }
            
            
            function formatAction2(value,rowData,rowIndex){  
                return '<input type="button" onclick="deleteRole('+rowData.role_id+');" value="Delete"/>'; 
            }  
            
            function reset(){
                $('#role_id').val("");                
                $('#role_name').val("");
                filterTable();
            }
        </script>
    </head>
    <body>
        <div style="padding:3px 2px;border-bottom:1px solid #ccc"><h2>Roles Management</h2></div> 
        <br/>
        <div id="tb" style="padding:5px;height:auto">  
            <div style="margin-bottom:5px">  
                <a href="javascript:void(0)" onclick="javascript:window.parent.addTab('add_role','Create Role','<%=GVSYSTEMADMIN.CREATE_ROLE%>?tabTitle=Create+Role');" class="easyui-linkbutton" iconCls="icon-add" plain="true">Create Role</a>  
            </div> 
            <div>
                <table cellpadding="5">
                    <tr>
                        <td>Role ID:</td>
                        <td>
                            <input  name="role_id" id="role_id" type="text"/>
                        </td>
                        <td>Role Name:</td>
                        <td>
                            <input  name="role_name" id="role_name" type="text"/>
                        </td>
                        <td><a href="#" class="easyui-linkbutton" onclick="filterTable()" iconCls="icon-search"></a>
                            <a href="#" class="easyui-linkbutton" onclick="reset()">Clear</a>
                        </td>
                    </tr>
                </table>  
            </div>  
        </div>  
        <div fit="true" style="width:100%; height:500px">
            <table id="tt" class="easyui-datagrid" fit="true" style="width:100%;"  
                   url="../RoleServlet?action=loadPage&content=table"   
                   toolbar="#tb"  
                   singleSelect="true" fitColumns="true" pagination="true"
                   rownumbers="false">  
                <thead>  
                    <tr> 
                        <th field="role_id" width="20px">Role ID</th>  
                        <th field="role_name" width="40%">Role Name</th>
                        <th field="action" align="right" width="20" formatter="formatAction"></th>  
                        <th field="action2" align="right" width="20" formatter="formatAction2"></th> 
                    </tr>  
                </thead>  
            </table> 
        </div>
    </body>
</html>
