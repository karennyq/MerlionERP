<%-- 
    Document   : createRole
    Created on : Sep 21, 2011, 3:41:43 PM
    Author     : alyssia
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Create Role</title>
        <%@include file = "../global/global_include.jsp" %>
        <script type="text/javascript"> 
            $(function(){
                $('#createRole').form({
                    url:'../RoleServlet?action=createRole',
                    onSubmit:function(){   
                        return $(this).form('validate');
                    },
                    success:function(data){
                        var obj = jQuery.parseJSON(data); 
                        if((obj.type!='error')){
                            $('#createRole').form('clear'); 
                            reloadAll();
                            alertMsg(obj);
                            closeTab("");
                        }
                        alertMsg(obj);
                    }
                });
            });
            
            function onClickConfirm(){
                $('#createRole').panel('expand');
            }
            
            $(document).ready(function(){
                $.ajax({
                    type: "POST",
                    url: "../DepartmentServlet",
                    data: "action=getDepts",
                    dataType: "xml",
                    cache: false,
                    success: function(data){
                        $('#department_id').append('<option value=\'\'></option>');
                        $(data).find('department').each(function(){
                            $('#department_id').append('<option value=\''+$(this).find('department_id').text()+'\'>' + $(this).find('department_name').text() + '</option>');
                        });
                    }
                });
            });
            
            function formatAction(value,row,index){
                if(row.url!=""){
                    return '<input type="checkbox" name="arCheckBox" value="'+row.id+'" /> ';
                }
                return "";
            }
        </script>
    </head>
    <body>
        <div style="padding:3px 2px;border-bottom:1px solid #ccc"><h2>Create Role</h2></div>  
        <br/>
        <form id="createRole" method="post"> 
            <div id="createRole" class="easyui-panel" title="Role:" style="padding:0px;background:#fafafa;"  
                  collapsible="true" minimizable="false"
                  maximizable="false">  
                <table class="tableForms">  
                    <tr>  
                        <td class="tableForms_label">Role Name:</td>  
                        <td class="tableForms_userInput"><input class="easyui-validatebox" name="role_name"  type="text" required="true"/></td> 
                    </tr>  
                </table>  
            </div>  
            <br/>
            <div  id="assignDept" class="easyui-panel" title="Department:" style="padding:0px;background:#fafafa;"  
                  collapsible="true" minimizable="false"
                  maximizable="false">  
                <table class="tableForms">  
                    <tr>
                        <td class="tableForms_label">Department:</td>  
                        <td class="tableForms_userInput">
                            <select class="easyui-validatebox" name="department_id" id="department_id" required="true"></select>
                        </td>
                    </tr> 
                </table>  
            </div> 
            <br/>
            <div  id="assignAr" class="easyui-panel" title="Assign Access Rights:" style="padding:0px;background:#fafafa;"  
                  collapsible="true" minimizable="false"
                  maximizable="false"> 
                <div fit="true" style="height:250px">
                    <table class="easyui-treegrid" fit="true" style="width:100%;"
                           url="../RoleServlet?action=loadPage&content=accessRights"
                           rownumbers="true"
                           idField="id" treeField="name">  
                        <thead frozen="false">
                            <tr>
                                <th field="name" width="500px">Access Rights</th>
                                <th field="Action" formatter="formatAction" width="100">Manage</th>
                            </tr>
                        </thead>
                    </table> 
                </div>
            </div>
            <br/>
            <div class="form_buttons">
                <input type="submit" onclick="onClickConfirm();" value="Create"/>
                <input type="button" onclick="confirmReset('createRole','Clear the form?')" value="Clear" />
            </div> 
        </form>
    </body>
</html>
