<%-- 
    Document   : updatePL
    Created on : Sep 30, 2011, 10:50:08 AM
    Author     : Randy
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <%@include file = "../global/global_include.jsp" %>
        <title>Update Production Line</title>
        
        <script type="text/javascript">
            $(document).ready(function(){
                if($('#production_line_id').val()!=null||$('#production_line_id').val()==""){
                    getDetails();
                }
            });
            
            function getDetails(){
                $.ajax({
                    type: "POST",
                    url: "../ProductionLineServlet",
                    data: "action=getDetails&production_line_id="+$('#production_line_id').val(),
                    dataType: "json",
                    cache: false,
                    success: function(data){
                        $('#production_line_id').val(data.production_line_id);
                        $('#production_line_name').val(data.production_line_name);
                    }
                });
            }
            
            function onClickConfirm(){
                $('#updatePH').panel('expand');
            }
            
            function phReset(){
                window.parent.$.messager.confirm("Confirm", 'Reset the form?', function(r){
                    if (r){
                        getDetails();
                    }
                });
            }
            
            $(function() {
                $('#ff').form({
                    url:'../ProductionLineServlet?action=updatePL',
                    onSubmit:function() {
                        return $(this).form('validate');
                    },
                    success:function(data) {
                        var obj = jQuery.parseJSON(data); 
                        if(obj.type!='error') {
                            reloadAll();
                        }
                        alertMsg(obj); 
                    }
                });
            });
        </script>
    </head>
    <body>
        <div style="padding:3px 2px;border-bottom:1px solid #ccc"><h2>Update Public Holiday</h2></div>  
        <br/>
        <form id="ff" method="post">
            <input type="hidden" name="production_line_id" id="production_line_id" value=<%= request.getParameter("production_line_id")%> />
            <div id="updatePL" class="easyui-panel" title="General Details:" style="padding:0px;background:#fafafa;" collapsible="true" >
                <table class="tableForms">
                    <tr>
                        <td class="tableForms_label">Production Line ID:</td>
                        <td class="tableForms_userInput">
                            <%= request.getParameter("production_line_id")%>
                        </td>
                    </tr>
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
                <input type="submit" onclick="onClickConfirm()" value="Update" />
                <input type="button" onclick="phReset()" value="Reset" />
            </div> 
        </form>
    </body>
</html>
