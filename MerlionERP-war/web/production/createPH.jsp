<%-- 
    Document   : createPH
    Created on : Sep 17, 2011, 1:09:50 PM
    Author     : Randy
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <%@include file = "../global/global_include.jsp" %>
        <title>Create Public Holiday</title>
        
        <script type="text/javascript">
            /*
            $.fn.datebox.defaults.formatter = function(date) {
                var y = date.getFullYear();
                var m = date.getMonth() + 1;
                var d = date.getDate();
                return (d < 10 ? '0' + d : d) + '/' + (m < 10 ? '0' + m : m) + '/' + y;
            };
            
            $.fn.datebox.defaults.parser = function(s) {
                if (s) {
                    var a = s.split('/');
                    var d = new Number(a[0]);
                    var m = new Number(a[1]);
                    var y = new Number(a[2]);
                    var dd = new Date(y, m - 1, d);
                    return dd;
                } else {
                    return new Date();
                }
            };*/
    
            $(document).ready(function(){              
                $('#nameTab').hide();
                $('#create').hide();
            });
            
            $(function() {
                $("#ph_date").datepicker({
                    dateFormat: 'dd/mm/yy'
                });
            });
            
            function onClickConfirm(){
                $('#createPH').panel('expand');
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
                    url:'../PublicHolidayServlet?action=createPH',
                    onSubmit:function() {
                        return $(this).form('validate');
                    },
                    success:function(data) {
                        var obj = jQuery.parseJSON(data); 
                        if(obj.type!='error') {
                            alertMsg(obj);
                            reloadAll();
                            closeTab('Create Public Holiday');
                        }
                        alertMsg(obj); 
                    }
                });
            });
        </script>
    </head>
    <body>
        <div style="padding:3px 2px;border-bottom:1px solid #ccc"><h2>Create Public Holiday</h2></div>  
        <br/>
        <form id="ff" method="post">
            <div id="createPH" class="easyui-panel" title="General Details:" style="padding:0px;background:#fafafa;" collapsible="true" >
                <table class="tableForms">
                    <tr>
                        <td class="tableForms_label">Date:</td>  
                        <td class="tableForms_userInput">
                            <input class="easyui-validatebox" name="ph_date" id="ph_date" type="text" required="true" /> (dd/mm/yyyy)
                        </td>  
                    </tr>
                    <tr id="nameTab">
                        <td class="tableForms_label">Name:</td>  
                        <td class="tableForms_userInput">
                            <input class="easyui-validatebox" name="ph_name" id="ph_name" type="text" required="true" />
                        </td>  
                    </tr>
                </table>
            </div>
            <br/>
            <div class="form_buttons" id="create">
                <input type="submit" onclick="onClickConfirm()" value="Create" />
            </div> 
        </form>
    </body>
</html>
