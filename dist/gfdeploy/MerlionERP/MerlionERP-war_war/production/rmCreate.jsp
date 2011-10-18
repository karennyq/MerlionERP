<%-- 
    Document   : createRM.jsp
    Created on : Oct 6, 2011, 11:45:11 PM
    Author     : alyssia
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <%@include file = "../global/global_include.jsp" %>
        <title>Create Raw Material</title>
        
        <script type="text/javascript">
            function onClickConfirm(){
                $('#createRM').panel('expand');
            }
            
            $(function() {
                $('#ff').form({
                    url:'../RawMaterialServlet?action=createRM',
                    onSubmit:function() {
                        return $(this).form('validate');
                    },
                    success:function(data) {
                        var obj = jQuery.parseJSON(data); 
                        if(obj.type!='error') {
                            alertMsg(obj);
                            reloadAll();
                            closeTab('Create Raw Material');
                        }
                        alertMsg(obj); 
                    }
                });
            });
        </script>
    </head>
    <body>
        <div style="padding:3px 2px;border-bottom:1px solid #ccc"><h2>Create Production Line</h2></div>  
        <br/>
        <form id="ff" method="post">
            <div id="createRM" class="easyui-panel" title="General Details:" style="padding:0px;background:#fafafa;" collapsible="true" >
                <table class="tableForms">
                    <tr>
                        <td class="tableForms_label">Raw Material Name:</td>  
                        <td class="tableForms_userInput">
                            <input class="easyui-validatebox" name="mat_name" id="mat_name" type="text" required="true" />
                        </td>  
                    </tr>
                </table>
            </div>
            <br/>
            <div class="form_buttons">
                <input type="submit" onclick="onClickConfirm()" value="Create" />
            </div> 
        </form>
    </body>
</html>
