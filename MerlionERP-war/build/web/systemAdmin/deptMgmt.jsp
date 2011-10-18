<%-- 
    Document   : deptMgmt
    Created on : Sep 20, 2011, 12:58:35 PM
    Author     : alyssia
--%>

<%@page import="util.GVSYSTEMADMIN"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Department Management</title>
        <%@include file = "../global/global_include.jsp" %>
        <script type="text/javascript"> 
            function goUpdate(departmentID){
                window.parent.addTab('update_dept','Update Department: ' + departmentID,'<%=GVSYSTEMADMIN.UPDATE_DEPT%>?department_id='+departmentID);
            }
            
            function formatAction(value,rowData,rowIndex){     
                return '<input type="button" onclick="goUpdate('+rowData.department_id+');" value="Update"/>';   
            }  
                       
            function filterTable(){
                var deptID=$('#department_id').val();
                var deptName=$('#department_name').val();
                var f_url='../DepartmentServlet?action=loadPage&content=table&department_id='+deptID+'&department_name='+deptName;
                $('#tt').datagrid({url:f_url});     
            }
            
            function deleteDept(deptID,rowIndex){    
                window.parent.$.messager.confirm("Confirm", "Delete department " + deptID + "?", function(r){
                    if (r){
                        $.ajax({
                            type: "POST",
                            url: "../DepartmentServlet",
                            data: "action=deleteDepartment&department_id="+deptID,
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
                return '<input type="button" onclick="deleteDept('+rowData.department_id+');" value="Delete"/>'; 
            }
            
            function reset(){
                $('#department_id').val("");                
                $('#department_name').val("");
                filterTable();
            }
        </script>
    </head>
    <body>
        <div style="padding:3px 2px;border-bottom:1px solid #ccc"><h2>Department Management</h2></div> 
        <br/>
        <div id="tb" style="padding:5px;height:auto">  
            <div style="margin-bottom:5px">  
                <a href="javascript:void(0)" onclick="javascript:window.parent.addTab('add_dept','Create Department','<%=GVSYSTEMADMIN.CREATE_DEPT%>?tabTitle=Create+Department');" class="easyui-linkbutton" iconCls="icon-add" plain="true">Create Department</a>  
            </div>  
            <div>
                <table cellpadding="5">
                    <tr>
                        <td>Department ID:</td>
                        <td>
                            <input name="department_id" id="department_id" type="text"/>
                        </td>
                        <td>Department Name:</td>
                        <td>
                            <input name="department_name" id="department_name" type="text"/>
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
                   url="../DepartmentServlet?action=loadPage&content=table"   
                   toolbar="#tb"  
                   singleSelect="true" fitColumns="true" pagination="true"
                   rownumbers="false">  
                <thead>  
                    <tr>  
                        <th field="department_id" width="20%" >Department ID</th>
                        <th field="department_name" width="60%" >Department Name</th>  
                        <th field="action" align="right" width="10%" formatter="formatAction"></th> 
                        <th field="action2" align="right" width="10" formatter="formatAction2"></th>
                    </tr>  
                </thead>  
            </table> 
        </div>
    </body>
</html>
