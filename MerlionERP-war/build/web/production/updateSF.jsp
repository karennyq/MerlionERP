<%-- 
    Document   : updateSF
    Created on : Sep 21, 2011, 11:15:23 AM
    Author     : Randy
--%>

<%@page import="java.util.Calendar"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <%@include file = "../global/global_include.jsp" %>
        <title>Update Sales Forecast</title>
        
        <script type="text/javascript">
            $(document).ready(function(){
                if($('#sales_forecast_id').val()!=null||$('#sale_forecast_id').val()==""){
                    getDetails();
                }
                
                $('#load').hide();
                
                $('#yoy').numberbox({
                    precision:0
                });
            });
            
            function getDetails(){
                $.ajax({
                    type: "POST",
                    url: "../SalesForecastServlet",
                    data: "action=getDetails&sales_forecast_id=" + $('#sales_forecast_id').val(),
                    dataType: "xml",
                    cache: false,
                    success: function(data){
                        $(data).find('detail').each(function(){
                            $('#sales_forecast_id').val($(this).find('sales_forecast_id').text());
                            $('#product_name').html($(this).find('product_name').text());
                            $('#year').html($(this).find('year').text());
                            $('#month').html($(this).find('month').text());
                            $('#base').val($(this).find('base').text());
                            $('#yoy').val($(this).find('yoy').text());
                            $('#forecast').val($(this).find('amt_forecasted').text());
                            $('#amt_forecasted').val($(this).find('amt_forecasted').text());
                        }); 
                    }
                });
            }
            
            function onClickConfirm(){
                $('#updateSF').panel('expand');
            }
            
            function sfReset(){
                window.parent.$.messager.confirm("Confirm", 'Reset the form?', function(r){
                    if (r){
                        getDetails();
                    }
                });
            }
            
            $(function() {
                $('#yoy').change(function() {
                    $('#load').show();
                    var base = parseInt($('#base').val());
                    var yoy = parseInt(Math.round($('#yoy').val()));
                    $('#forecast').val(base + base * yoy / 100);
                    $('#amt_forecasted').val($('#forecast').val());
                    $('#load').fadeOut(750);
                });
            });
            
            $(function() {
                $('#ff').form({
                    url:'../SalesForecastServlet?action=updateSF',
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
        <div style="padding:3px 2px;border-bottom:1px solid #ccc"><h2>Update Sales Forecast</h2></div>  
        <br/>
        <form id="ff" method="post">
            <input type="hidden" name="sales_forecast_id" id="sales_forecast_id" value=<%= request.getParameter("sales_forecast_id")%> />
            <div id="updateSF" class="easyui-panel" title="General Details:" style="padding:0px;background:#fafafa;" collapsible="true" >
                <table class="tableForms">
                    <tr>
                        <td class="tableForms_label">Sales Forecast ID:</td>  
                        <td class="tableForms_userInput">
                            <%= request.getParameter("sales_forecast_id")%>
                        </td>  
                    </tr>
                    <tr>
                        <td class="tableForms_label">Product:</td>  
                        <td class="tableForms_userInput" name="product_name" id="product_name"></td>
                    </tr>
                    <tr id="yearTab">
                        <td class="tableForms_label">Selected Year:</td>
                        <td class="tableForms_userInput" name="year" id="year"></td>
                    </tr>
                    <tr id="monthTab">
                        <td class="tableForms_label">Selected Month:</td>
                        <td class="tableForms_userInput" name="month" id="month"></td>
                    </tr>
                    <tr id="baseTab">
                        <td class="tableForms_label">Base (Boxes):</td>
                        <td class="tableForms_userInput">
                            <input name="base" id="base" type="text" disabled />
                        </td>
                    </tr>
                    <tr id="yoyTab">
                        <td class="tableForms_label">YoY Growth (%):</td>
                        <td class="tableForms_userInput">
                            <input class="easyui-validatebox" name="yoy" id="yoy" type="text" required="true" />
                            (Rounded to whole numbers)
                        </td>
                    </tr>
                    <tr id="forecastTab">
                        <td class="tableForms_label">Sales Forecast (Boxes):</td>
                        <td class="tableForms_userInput">
                            <input name="forecast" id="forecast" type="text" disabled />
                            <img src="../css/images/ajax-loader.gif" width="16" height="16" id="load"/>
                            (Rounded to whole numbers)
                            <input type="hidden" name="amt_forecasted" id="amt_forecasted" value=""/>
                        </td>
                    </tr>
                </table>
            </div>
            <br/>
            <div class="form_buttons">
                <input type="submit" onclick="onClickConfirm()" value="Update" />
                <input type="button" onclick="sfReset()" value="Reset" />
            </div> 
        </form>
    </body>
</html>
