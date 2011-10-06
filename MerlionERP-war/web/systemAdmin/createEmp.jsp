<%-- 
    Document   : createAccount
    Created on : Aug 31, 2011, 10:19:51 PM
    Author     : Ken
--%>

<%@page import="java.util.ArrayList"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <% session.setAttribute("createEmployeeRoleList", new ArrayList()); %>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Create Employee</title>
        <%@include file = "../global/global_include.jsp" %>
        <script type="text/javascript"> 
            $(function(){
                $('#ff').form({
                    url:'../EmployeeServlet?action=createEmployee',
                    onSubmit:function(){   
                        return $(this).form('validate');
                    },
                    success:function(data){
                        var obj = jQuery.parseJSON(data); 
                        if((obj.type!='error')){
                            $('#ff').form('clear'); 
                            reloadAll();     
                            alertMsg(obj);
                            closeTab("");
                        }
                        alertMsg(obj);
                    }
                });
            });
    
            function onClickConfirm(){           
                $('#addEmployee').panel('expand');
                $('#addRoles').panel('expand');
            }
            
            $(document).ready(function(){
                $('#deptcc').combobox({  
                    url:'../EmployeeServlet?action=loadPage&content=deptDropdown',  
                    valueField:'id',  
                    textField:'text',
                    editable:false,
                    formatter:comboboxFomatter//******
                    //required:true
                });
                
                $('#rolecc').combobox({  
                    valueField:'id',  
                    textField:'text',
                    editable:false,
                    formatter:comboboxFomatter
                    //required:true
                });
                    
                $('#deptcc').combobox({  
                    onSelect:function(r){  
                        $('#rolecc').combobox('clear');
                        getRoles();
                    }   
                });
                
            });
            
            function getRoles(){
                $.ajax({
                    type: "POST",
                    url: "../EmployeeServlet",
                    data: "action=loadPage&content=deptDropdown",
                    dataType: "json",
                    cache: false,
                    success: function(data){
                        $('#deptcc').combobox('loadData',data);
                    }
                });  
            
                var dId=$('#deptcc').combobox('getValue');
  
                if(dId!=""){
                    $.ajax({
                        type: "POST",
                        url: "../EmployeeServlet",
                        data: "action=loadPage&content=roleDropdown&department_id="+dId,
                        dataType: "json",
                        cache: false,
                        success: function(data){
                            $('#rolecc').combobox('loadData',data);
                        }
                    });             
                }else{
                    $('#rolecc').combobox('clear');
                }    
            }
            
            function addRole(){
                var roleId = $('#rolecc').combobox('getValue');;
                $.ajax({
                    type: "POST",
                    url: "../EmployeeServlet",
                    data: "action=addRole&role_id="+roleId,
                    dataType: "json",
                    cache: false,
                    success: function(data){
                        if(data.type!='error'){    
                            $('#deptcc').combobox('select','');
                            $('#rolecc').combobox('clear');
                            $('#deptcc').combobox('reload');
                            $('#tt').datagrid('reload');
                        }else{
                            alertMsg(data);    
                        }
                    }
                });
            }
            
            function deleteRole(rowIndex){
                window.parent.$.messager.confirm("Confirm", "Delete the selected item?", function(r){
                    if (r){
                        $.ajax({
                            type: "POST",
                            url: "../EmployeeServlet",
                            data: "action=removeRole&listIndex="+rowIndex,
                            dataType: "json",
                            cache: false,
                            success: function(data){
                                if(data.type!='error'){
                                    $('#tt').datagrid('reload');
                                }
                                alertMsg(data);    
                            }
                        });    
                    }     
                });
            }
            
            function empReset(formId,content){
                window.parent.$.messager.confirm("Confirm", content, function(r){
                    if (r){           
                        $.ajax({
                            type: "POST",
                            url:"../EmployeeServlet",
                            data: "action=loadPage&content=roleTable&reset=true",
                            dataType: "json",
                            cache: false,
                            success: function(data){
                                if(data.type!='error'){                                     
                                    $('#deptcc').combobox('select','');
                                    $('#rolecc').combobox('clear');
                                    $('#deptcc').combobox('reload');
                                    $('#tt').datagrid('reload');   
                                }else{
                                    alertMsg(data);    
                                }
                            }
                        });
 
                        $('#'+formId).each(function(){
                            this.reset();
                        });
                    }
                });
            }
            
            function formatDept(value,rowData,rowIndex){  
                return rowData.department.department_name; 
            } 
            
            function formatActionDel(value,rowData,rowIndex){  
                return '<input type="button" onclick="deleteRole('+rowIndex+');" value="Delete"/>'; 
            } 
        </script>
    </head>
    <body>
        <div style="padding:3px 2px;border-bottom:1px solid #ccc"><h2>Create Employee</h2></div>  
        <br/>
        <form id="ff" method="post"> 
            <div  id="addEmployee" class="easyui-panel" title="General Details:" style="padding:0px;background:#fafafa;"  
                  collapsible="true" minimizable="false"
                  maximizable="false">  
                <table class="tableForms">  
                    <tr>  
                        <td class="tableForms_label">Name:</td>  
                        <td class="tableForms_userInput"><input class="easyui-validatebox" name="emp_name"  type="text" required="true"/></td> 
                    </tr>  
                    <tr>
                        <td class="tableForms_label">NRIC/FIN:</td>  
                        <td class="tableForms_userInput"><input class="easyui-validatebox" name="nric"  type="text" required="true"/></td> 
                    </tr>
                    <tr>  
                        <td class="tableForms_label">Email:</td>  
                        <td class="tableForms_userInput"><input class="easyui-validatebox" name="email" type="text" required="true" validType="email"/></td> 
                    </tr>
                </table>  
            </div>  
            <br/>
            <div id="addRoles" class="easyui-panel" title="Add Roles:" style="padding:0px;background:#fafafa;" collapsible="true">
                <div id="tb" style="padding:5px;height:auto">  
                    <div>
                        <table cellpadding="5">
                            <tr>
                                <td>Department:</td>
                                <td><select id="deptcc" panelHeight="auto"  name="deptcc"></select></td>
                                <td>Roles:</td>
                                <td><select id="rolecc" panelHeight="auto" name="rolecc"></select></td>
                                <td><a href="#" class="easyui-linkbutton" onclick="addRole()">Add</a></td>
                            </tr>
                        </table>  
                    </div>  
                </div>  
                <div fit="true" style="height:200px">
                    <table id="tt" class="easyui-datagrid" fit="true" style="width:100%;"  
                           url="../EmployeeServlet?action=loadPage&content=roleTable"   
                           toolbar="#tb"  
                           singleSelect="true" fitColumns="true"
                           rownumbers="true">  
                        <thead>  
                            <tr>  
                                <th field="role_id" width="10%"  >Role ID</th>  
                                <th field="role_name" width="40%" >Role Name</th>  
                                <th field="department" formatter="formatDept" width="30%" >Department</th> 
                                <th field="actionDel" width="20%" formatter="formatActionDel" ></th> 
                            </tr>  
                        </thead>  
                    </table> 
                </div>
            </div>
            <br/>
            <div class="form_buttons">
                <input type="submit" onclick="onClickConfirm();" value="Create"/>
                <input type="button" onclick="empReset('ff','Clear the form?')" value="Clear" />
            </div> 
        </form>
    </body>
</html>
