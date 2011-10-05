<%-- 
    Document   : createPL
    Created on : Sep 30, 2011, 10:36:55 AM
    Author     : Randy
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <%@include file = "../global/global_include.jsp" %>
        <title>Create Production Line</title>
        
        <script type="text/javascript">
            function onClickConfirm(){
                $('#createPL').panel('expand');
            }
            
            $(function() {
                $('#ff').form({
                    url:'../ProductionLineServlet?action=createPL',
                    onSubmit:function() {
                        return $(this).form('validate');
                    },
                    success:function(data) {
                        var obj = jQuery.parseJSON(data); 
                        if(obj.type!='error') {
                            alertMsg(obj);
                            reloadAll();
                            closeTab('Create Production Line');
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
            <div id="createPL" class="easyui-panel" title="General Details:" style="padding:0px;background:#fafafa;" collapsible="true" >
                <table class="tableForms">
                    <tr>
                        <td class="tableForms_label">Production Line Name:</td>  
                        <td class="tableForms_userInput">
                            <input class="easyui-validatebox" name="production_line_name" id="production_line_name" type="text" required="true" />
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
