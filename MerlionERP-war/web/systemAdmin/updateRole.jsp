<%-- 
 Document   : updateRole
 Created on : Sep 21, 2011, 4:18:34 PM
 Author     : alyssia
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <%@include file = "../global/global_include.jsp" %>
        <title>Update Department</title>
        <script type="text/javascript">  
            $(function(){
                var roleID= $('#role_id').val();
                $('#updateRole').form({                    
                    url:'../RoleServlet?action=updateRole&confirm=updateRole&role_id='+roleID,
                    onSubmit:function(){
                        return $(this).form('validate');
                    },
                    success:function(data){
                        try{
                            var obj = jQuery.parseJSON(data); 
                        }catch(err){alert(err);}
                        if(obj.type!='error'){
                            var f = window.parent;
                            reloadAll();
                        }
                        alertMsg(obj);
                    }
                });
            });
               
            function getRoleInfo(){
                var roleID= $('#role_id').val();
                $.ajax({
                    type: "POST",
                    url: "../RoleServlet",
                    data: "action=loadPage&content=details&role_id="+roleID,  
                    dataType: "json",
                    cache: false,
                    success: function(data){
                        $('#role_id').val(data.role_id);
                        $('#role_name').val(data.role_name);
                        $('#actt').treegrid('reload');
                        //loadTable(data.role_id);
                    }
                });
            } 
           
            function onClickConfirm(){
                $('#updateRole').panel('expand');
            }  
            
            function confirmReset(formId,content){
                window.parent.$.messager.confirm("Confirm", content, function(r){
                    if (r){
                        $('#'+formId).each(function(){
                            getRoleInfo();
                        });
                    }
                });
            }
            
            $(document).ready(function(){
                if($('#role_id').val()!=null||$('#role_id').val()==""){
                    getRoleInfo();
                }
            });
            
            function formatAction(value,row,index){
                if(row.url!=""){
                    if(row.checked==true)
                        return '<input type="checkbox" name="arCheckBox" value="'+row.id+'" checked /> ';
                    else
                        return '<input type="checkbox" name="arCheckBox" value="'+row.id+'" /> ';
                }
                return "";
            }
        
            /* function loadTable(id){
                
                var f_url="../RoleServlet?action=loadPage&content=updateAccessRights&role_id="+id;   
                $('#actt').treegrid({  
                    url:f_url,
                    treeField:'name'
                });
                //$('#actt').treegrid('reload');
                  
            }*/
        </script>    
    </head>
    <body>
        <div style="padding:3px 2px;border-bottom:1px solid #ccc"><h2>Update Role</h2></div>
        <br/>
        <form id="updateRole" method="post"> 
            <input id="role_id" name="role_id"  type="hidden" value='<%=request.getParameter("role_id")%>' />
            <div id="updateR" class="easyui-panel" title="General Details:" style="padding:0px;background:#fafafa;"  
                 collapsible="true" > 
                <table class="tableForms">
                    <tr>  
                        <td class="tableForms_label">Role ID: </td> 
                        <td class="tableForms_userInput" >
                            <%=request.getParameter("role_id")%>
                        </td>
                    </tr>
                    <tr>  
                        <td class="tableForms_label">Role Name: </td> 
                        <td class="tableForms_userInput">
                            <input class="easyui-validatebox" id="role_name" name="role_name" type="text" required="true"/>
                        </td>
                    </tr>
                </table> 
            </div>
            <br/>
            <div  id="assignAr" class="easyui-panel" title="Assign Access Rights:" style="padding:0px;background:#fafafa;"  
                  collapsible="true" minimizable="false"
                  maximizable="false"> 
                <div fit="true" style="height:250px">
                    <table id="actt" class="easyui-treegrid" fit="true" style="width:100%;"
                           url="../RoleServlet?action=loadPage&content=updateAccessRights&role_id=<%=request.getParameter("role_id")%>"
                           rownumbers="true"
                           idField="id" treeField="name">  
                        <thead>
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
                <input type="submit" onclick="onClickConfirm();" value="Save" />
                <input type="button" onclick="confirmReset('updateRole','Reset the form?')" value="Reset" />
            </div> 
        </form>        
    </body>
</html>
