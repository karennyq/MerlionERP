<%-- 
    Document   : createDept
    Created on : Sep 20, 2011, 1:25:38 PM
    Author     : alyssia
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Create Department</title>
        <%@include file = "../global/global_include.jsp" %>
        <script type="text/javascript"> 
            $(function(){
                $('#ff').form({
                    url:'../DepartmentServlet?action=createDept',
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
                $('#addDept').panel('expand');
            }
        </script>
    </head>
    <body>
        <div style="padding:3px 2px;border-bottom:1px solid #ccc"><h2>Create Department</h2></div>  
        <br/>
        <form id="ff" method="post"> 
            <div  id="addDept" class="easyui-panel" title="General Details:" style="padding:0px;background:#fafafa;"  
                  collapsible="true" minimizable="false"
                  maximizable="false">  
                <table class="tableForms">  
                    <tr>  
                        <td class="tableForms_label">Department Name:</td>  
                        <td class="tableForms_userInput"><input class="easyui-validatebox" name="department_name"  type="text" required="true"/></td> 
                    </tr>  
                </table>  
            </div>  
            <br/>
            <div class="form_buttons">
                <input type="submit" onclick="onClickConfirm();" value="Create"/>
                <input type="button" onclick="confirmReset('ff','Clear the form?')" value="Clear" />
            </div> 
        </form>
    </body>
</html>
