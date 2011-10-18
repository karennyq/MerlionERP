<%-- 
    Document   : updateDept
    Created on : Sep 20, 2011, 2:23:06 PM
    Author     : alyssia
--%>

<%@page import="util.GVSYSTEMADMIN"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <%@include file = "../global/global_include.jsp" %>
        <title>Update Department</title>
        <script type="text/javascript">  
            $(function(){
                var departmentID= $('#department_id').val();
                $('#updateDept').form({                    
                    url:'../DepartmentServlet?action=update&confirm=updateDept&department_id='+departmentID,
                    onSubmit:function(){
                        return $(this).form('validate');
                    },
                    success:function(data){
                        var obj = jQuery.parseJSON(data); 
                        if(obj.type!='error'&&(obj.type!='warning')){
                            reloadAll();
                        }
                        alertMsg(obj);
                    }
                });
            })
           
            function goUpdate(roleID){
                window.parent.addTab('update_role'+roleID,'Update Role: (ID:  ' + roleID + ')','<%=GVSYSTEMADMIN.UPDATE_ROLE%>?role_id='+roleID);
            }
            
            function formatAction(value,rowData,rowIndex){  
                return '<input type="button" onclick="goUpdate('+rowData.role_id+');" value="Update"/>';   
            } 
           
            function deleteRole(roleID,rowIndex){
                window.parent.$.messager.confirm("Confirm", "Delete the selected item?", function(r){
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
           
            function getDeptInfo(){
                var deptID= $('#department_id').val();
                $.ajax({
                    type: "POST",
                    url: "../DepartmentServlet",
                    data: "action=loadPage&content=details&department_id="+deptID,  
                    dataType: "json",
                    cache: false,
                    success: function(data){
                        $('#department_id').val(data.department_id);
                        $('#department_name').val(data.department_name);
                    }
                });
            } 
            
            function onClickConfirm(){
                $('#updateDept').panel('expand');
            }
            
            function confirmReset(formId,content){
                window.parent.$.messager.confirm("Confirm", content, function(r){
                    if (r){
                        $('#'+formId).each(function(){
                            getDeptInfo();
                        });
                    }
                });
            }
            
            $(document).ready(function(){
                if($('#department_id').val()!=null||$('#department_id').val()==""){
                    getDeptInfo();
                }
            });
        </script>    
    </head>
    <body>
        <div style="padding:3px 2px;border-bottom:1px solid #ccc"><h2>Update Department</h2></div> 
        <br/>
        <div id="updateD" class="easyui-panel" title="Department Details:" style="padding:0px;background:#fafafa;"  
             collapsible="true" > 
            <table class="tableForms">
                <tr>  
                    <td class="tableForms_label">Department ID: </td> 
                    <td class="tableForms_userInput">
                        <%=request.getParameter("department_id")%>
                    </td>
                </tr>
                <tr>    
                    <td class="tableForms_label">Department Name: </td> 
                    <td class="tableForms_userInput">
                        <form id="updateDept" method="post">
                            <input id="department_id" name="department_id"  type="hidden" value=<%=request.getParameter("department_id")%> />  
                            <input class="easyui-validatebox" id="department_name" name="department_name" type="text" required="true"/>
                            <input type="submit" onclick="onClickConfirm();" value="Update" />
                            <input type="button" onclick="confirmReset('updateDept','Reset the form?')" value="Reset"/> 
                        </form>
                    </td>
                </tr>
            </table> 
        </div>
        <br/>
        <div id="updateR" class="easyui-panel" title="Role Details:" style="height:250px;background:#fafafa;"  
             collapsible="true" > 
            <div id="tb" style="padding:5px;height:auto"> 
                <div style="margin-bottom:5px">  
                    <a href="javascript:void(0)" onclick="javascript:window.parent.addTab('add_role','Create Role','<%=GVSYSTEMADMIN.CREATE_ROLE%>?tabTitle=Create+Role&department_id=<%=request.getParameter("department_id")%>');" class="easyui-linkbutton" iconCls="icon-add" plain="true">Create Role</a>  
                </div>
            </div>
            <table id="tt" class="easyui-datagrid" fit="true" style="width:100%;height:100px;"  
                   url="../RoleServlet?action=retrieveRoles&department_id=<%=request.getParameter("department_id")%>"   
                   toolbar="#tb"  
                   singleSelect="true" fitColumns="true" pagination="true"
                   rownumbers="true" > 
                <thead>
                    <tr> 
                        <th field="role_id" width="40%">Role ID</th>
                        <th field="role_name" width="40%">Role Name</th>
                        <th field="action" align="right" width="10%" formatter="formatAction"></th>  
                        <th field="action2" align="right" width="10%" formatter="formatAction2"></th> 
                    </tr>  
                </thead>  
            </table> 
        </div>
    </body>
</html>