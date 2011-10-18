<%-- 
    Document   : createSF
    Created on : Sep 18, 2011, 10:22:57 PM
    Author     : Randy
--%>

<%@page import="java.util.Calendar"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <%@include file = "../global/global_include.jsp" %>
        <title>Create Sales Forecast</title>
        <script type="text/javascript">
            $(document).ready(function(){
                $.ajax({
                    type: "POST",
                    url: "../ProductServlet",
                    data: "action=getProducts",
                    dataType: "xml",
                    cache: false,
                    success: function(data){
                        $('#product_id').append('<option value=\'\'></option>');
                        $(data).find('product').each(function(){
                            $('#product_id').append('<option value=\'' + $(this).find('product_id').text() + '\'>' + $(this).find('product_name').text() + '</option>');
                        });
                    }
                });
                
                $('#prevTab').hide();
                $('#yearTab').hide();
                $('#monthTab').hide();
                $('#baseTab').hide();
                $('#yoyTab').hide();
                $('#load').hide();
                $('#forecastTab').hide();
                $('#create').hide();
                
                $('#yoy').numberbox({
                    precision:0
                });
            });
            
            <% Calendar cal = Calendar.getInstance(); %>
            <% int year = cal.get(Calendar.YEAR); %>
            <% int month = cal.get(Calendar.MONTH); %>
            <% String[] monthName = {"January","February","March","April","May","June","July","August","September","October","November","December"}; %>
            
            $(function() {
                $('#product_id').change(function() {
                    $('#monthTab').hide();
                    $('#baseTab').hide();
                    $('#yoyTab').hide();
                    $('#yoy').val('');
                    $('#forecastTab').hide();
                    $('#create').hide();
                    if ($("#product_id :selected").val() == "") {
                        $('#prevTab').hide();
                        $('#yearTab').hide();
                    } else {
                        $.ajax({
                            type: "POST",
                            url: "../SalesForecastServlet",
                            data: "action=getLastDone&product_id=" + $('#product_id').val(),
                            dataType: "xml",
                            cache: false,
                            success: function(data){
                                $('#last_done').html($(data).find('last_done').text());
                            },
                            error: function() {
                                alert("Unexpected System Error!");
                            }
                        });
                        
                        $('#prevTab').show();
                        $('#yearTab').show();
                        $('#year>option').remove();
                        $('#year').append('<option value=\'\'></option>');
                        <% if (month < 11) { %>
                            <% for (int i=0; i<2; i++) { %>
                                $('#year').append('<option value=\'<%= year + i%>\'><%= year + i%></option>');
                            <% } %>
                        <% } else { %>
                            $('#year').append('<option value=\'<%= year + 1%>\'><%= year + 1%></option>');
                        <% } %>
                    }
                });
            });
            
            $(function() {
                $('#year').change(function() {
                    $('#baseTab').hide();
                    $('#yoyTab').hide();
                    $('#yoy').val('');
                    $('#forecastTab').hide();
                    $('#create').hide();
                    if ($("#year :selected").val() == "") {
                        $('#monthTab').hide();
                    } else {
                        $('#monthTab').show();
                        $('#month>option').remove();
                        $('#month').append('<option value=\'\'></option>');
                        if ($("#year :selected").val() == <%= year %>) {
                            <% for (int i=(month+1); i<12; i++) { %>
                                $('#month').append('<option value=\'<%= i+1 %>\'><%= monthName[i] %></option>');
                            <% } %>
                        } else if ($("#year :selected").text() == <%= year+1 %>) {
                            <% for (int i=0; i<(month); i++) { %>
                                $('#month').append('<option value=\'<%= i+1 %>\'><%= monthName[i] %></option>');
                            <% } %>
                        }
                    }
                });
            });
            
            $(function() {
                $('#month').change(function() {
                    $('#yoyTab').hide();
                    $('#yoy').val('');
                    $('#forecastTab').hide();
                    $('#create').hide();
                    if ($("#month :selected").val() == "") {
                        $('#baseTab').hide();
                    } else {
                        $.ajax({
                            type: "POST",
                            url: "../SalesForecastServlet",
                            data: "action=checkSFDone&product_id=" + $('#product_id').val() + "&year=" + $("#year :selected").val() + "&month=" + $("#month :selected").val(),
                            dataType: "xml",
                            cache: false,
                            success: function(data){
                                if ($(data).find('result').text() == "false") {
                                    $.ajax({
                                        type: "POST",
                                        url: "../SalesForecastServlet",
                                        data: "action=getBase&product_id=" + $('#product_id').val() + "&year=" + $("#year :selected").val() + "&month=" + $("#month :selected").val(),
                                        dataType: "xml",
                                        cache: false,
                                        success: function(data){
                                            $('#base').val($(data).find('base').text());
                                            $('#baseTab').show();
                                            if ($(data).find('base').text() == "0") {
                                                alertMsgStr("Create Sales Forecast", "No past sales record. Unable to proceed with Sales Forecast.", "error");
                                            } else {
                                                $('#yoyTab').show();
                                            }
                                        },
                                        error: function() {
                                            alert("Unexpected System Error!");
                                        }
                                    });
                                } else {
                                    alertMsgStr("Create Sales Forecast", "Sales Forecast done for this period. Please choose another.", "error");
                                }
                            },
                            error: function() {
                                alert("Unexpected System Error!");
                            }
                        });
                    }
                });
            });
            
            $(function() {
                $('#yoy').change(function() {
                    $('#load').show();
                    $('#forecastTab').show();
                    var base = parseInt($('#base').val());
                    var yoy = parseInt(Math.round($('#yoy').val()));
                    $('#forecast').val(base + base * yoy / 100);
                    $('#amt_forecasted').val($('#forecast').val());
                    $('#load').fadeOut(750);
                    if (yoy < -100) {
                        alertMsgStr("Create Sales Forecast", "Sales Forecast cannot be negative.", "error");
                        $('#create').hide();
                    } else {
                        $('#create').show();
                    }
                });
            });

            function onClickConfirm(){
                $('#createSF').panel('expand');
            }
            
            $(function() {
                $('#ff').form({
                    url:'../SalesForecastServlet?action=createSF',
                    onSubmit:function() {
                        return $(this).form('validate');
                    },
                    success:function(data) {
                        var obj = jQuery.parseJSON(data); 
                        if(obj.type!='error') {
                            alertMsg(obj);
                            reloadAll();
                            closeTab('Create Sales Forecast');
                        }
                        alertMsg(obj); 
                    }
                });
            });
        </script>
    </head>
    <body>
        <div style="padding:3px 2px;border-bottom:1px solid #ccc"><h2>Create Sales Forecast</h2></div>  
        <br/>
        <form id="ff" method="post">
            <div id="createSF" class="easyui-panel" title="General Details:" style="padding:0px;background:#fafafa;" collapsible="true" >
                <table class="tableForms">
                    <tr>
                        <td class="tableForms_label">Product:</td>  
                        <td class="tableForms_userInput">
                            <select class="easyui-validatebox" name="product_id" id="product_id" required="true"></select>
                        </td>
                    </tr>
                    <tr id="prevTab">
                        <td class="tableForms_label">Last Done Forecast:</td>
                        <td class="tableForms_userInput" name="last_done" id="last_done"></td>
                    </tr>
                    <tr id="yearTab">
                        <td class="tableForms_label">Select Year:</td>
                        <td class="tableForms_userInput">
                            <select class="easyui-validatebox" name="year" id="year" required="true"></select>
                        </td>
                    </tr>
                    <tr id="monthTab">
                        <td class="tableForms_label">Select Month:</td>
                        <td class="tableForms_userInput">
                            <select class="easyui-validatebox" name="month" id="month" required="true"></select>
                        </td>
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
            <div class="form_buttons" id="create">
                <input type="submit" onclick="onClickConfirm()" value="Create" />
            </div> 
        </form>
    </body>
</html>