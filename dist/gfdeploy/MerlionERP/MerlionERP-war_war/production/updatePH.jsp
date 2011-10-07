<%-- 
    Document   : updatePH
    Created on : Sep 17, 2011, 10:55:29 PM
    Author     : Randy
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <%@include file = "../global/global_include.jsp" %>
        <title>Update Public Holiday</title>
        
        <script type="text/javascript">
            $(function() {
                $("#ph_date").datepicker({
                    dateFormat: 'dd/mm/yy'
                });
            });
            
            $(document).ready(function(){
                if($('#pub_holi_id').val()!=null||$('#pub_holi_id').val()==""){
                    getDetails();
                }
            });
            
            function getDetails(){
                var pub_holi_id = $('#pub_holi_id').val();
                $.ajax({
                    type: "POST",
                    url: "../PublicHolidayServlet",
                    data: "action=getDetails&pub_holi_id="+pub_holi_id,
                    dataType: "json",
                    cache: false,
                    success: function(data){
                        $('#pub_holi_id').val(data.pub_holi_id);
                        $('#ph_date').html(data.ph_date);
                        $('#ph_name').val(data.ph_name);
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
                $('#ph_date').change(function() {
                    $.ajax({
                        type: "POST",
                        url: "../PublicHolidayServlet",
                        data: "action=validatePHDate&ph_date=" + $('#ph_date').val(),
                        dataType: "xml",
                        cache: false,
                        success: function(data){
                            if ($(data).find('result').text() == "true") {
                                $('#nameTab').show();
                                $('#create').show();
                            } else {
                                $('#nameTab').hide();
                                $('#create').hide();
                                alertMsgStr("Create Public Holiday", $(data).find('result').text(), "error");
                            }
                        },
                        error: function() {
                            alert("Unexpected System Error!");
                        }
                    });
                });
            });
            
            $(function() {
                $('#ff').form({
                    url:'../PublicHolidayServlet?action=updatePH',
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
            <input type="hidden" name="pub_holi_id" id="pub_holi_id" value="<%= request.getParameter("pub_holi_id")%>" />
            <div id="updatePH" class="easyui-panel" title="General Details:" style="padding:0px;background:#fafafa;" collapsible="true" >
                <table class="tableForms">
                    <tr>
                        <td class="tableForms_label">Public Holiday ID:</td>
                        <td class="tableForms_userInput">
                            <%= request.getParameter("pub_holi_id")%>
                        </td>
                    </tr>
                    <tr>
                        <td class="tableForms_label">Date:</td>  
                        <td class="tableForms_userInput" name="ph_date" id="ph_date"></td>
                    </tr>
                    <tr>
                        <td class="tableForms_label">Name:</td>  
                        <td class="tableForms_userInput">
                            <input class="easyui-validatebox" name="ph_name" id="ph_name" type="text" required="true" />
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
