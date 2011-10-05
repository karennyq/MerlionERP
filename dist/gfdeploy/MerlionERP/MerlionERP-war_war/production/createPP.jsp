<%-- 
    Document   : createPP
    Created on : Sep 23, 2011, 11:26:45 AM
    Author     : Randy
--%>

<%@page import="java.util.ArrayList"%>
<%@page import="java.util.Calendar"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <%@include file = "../global/global_include.jsp" %>
        <title>Create Sales Forecast</title>
        
        <script type="text/javascript">
            <% Calendar cal = Calendar.getInstance(); %>
            <% int year = cal.get(Calendar.YEAR); %>
            <% int month = cal.get(Calendar.MONTH); %>
            <% String[] monthName = {"January","February","March","April","May","June","July","August","September","October","November","December"}; %>
            
            $(document).ready(function(){
                $('#monthTab').hide();
                $('#workingDaysTab').hide();
                $('#capacityTab').hide();
                $('#utilizationTab').hide();
                $('#plannedDmd').panel('close');
                $('#forecastedTab').hide();
                $('#quantityTab').hide();
                $('#create').hide();
                
                $('#quantity').numberbox({
                    precision:0
                });
                
                $('#year').append('<option value=\'\'></option>');
                <% if (month < 11) { %>
                    <% for (int i=0; i<2; i++) { %>
                        $('#year').append('<option value=\'<%= year + i%>\'><%= year + i%></option>');
                    <% } %>
                <% } else { %>
                    $('#year').append('<option value=\'<%= year + 1%>\'><%= year + 1%></option>');
                <% } %>
            });
            
            $(function() {
                $('#year').change(function() {
                    $('#workingDaysTab').hide();
                    $('#capacityTab').hide();
                    $('#utilizationTab').hide();
                    $('#plannedDmd').panel('close');
                    $('#forecastedTab').hide();
                    $('#quantityTab').hide();
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
                    if ($("#month :selected").val() == "") {
                        $('#workingDaysTab').hide();
                        $('#capacityTab').hide();
                        $('#utilizationTab').hide();
                        $('#plannedDmd').panel('close');
                        $('#forecastedTab').hide();
                        $('#quantityTab').hide();
                        $('#create').hide();
                    } else {
                        $.ajax({
                            type: "POST",
                            url: "../ProductionPlanServlet",
                            data: "action=checkPPDone"
                                + "&year=" + $("#year :selected").val()
                                + "&month=" + $("#month :selected").val(),
                            dataType: "xml",
                            cache: false,
                            success: function(data){
                                if ($(data).find('result').text() == "false") {
                                    $.ajax({
                                        type: "POST",
                                        url: "../ProductionPlanServlet",
                                        data: "action=calculatePPDetails"
                                            + "&year=" + $("#year :selected").val()
                                            + "&month=" + $("#month :selected").val()
                                            + "&listName=" + "createPDList",
                                        dataType: "xml",
                                        cache: false,
                                        success: function(data){
                                            $(data).find('details').each(function(){
                                                $('#working_days').val($(this).find('working_days').text());
                                                $('#capacity').val($(this).find('capacity').text());
                                                $('#utilization').val($(this).find('utilization').text());

                                                if ($(data).find('capacity').text() != "0") {
                                                    $.ajax({
                                                        type: "POST",
                                                        url: "../ProductServlet",
                                                        data: "action=getProducts",
                                                        dataType: "xml",
                                                        cache: false,
                                                        success: function(data){
                                                            $('#product_id>option').remove();
                                                            $('#product_id').append('<option value=\'\'></option>');
                                                            $(data).find('product').each(function(){
                                                                $('#product_id').append('<option value=\'' + $(this).find('product_id').text() + '\'>' + $(this).find('product_name').text() + '</option>');
                                                            });
                                                        }
                                                    });

                                                    $('#addProduct').show();
                                                    $('#display').datagrid('reload');
                                                    $('#plannedDmd').panel('open');
                                                } else {
                                                    alertMsgStr("Create Production Plan", "No capacity. Unable to proceed.", "error");
                                                }
                                            });
                                            
                                            $('#workingDaysTab').show();
                                            $('#capacityTab').show();
                                            $('#utilizationTab').show();
                                        }
                                    });
                                } else {
                                    alertMsgStr("Create Production Plan", "Production Plan done for this period. Please choose another.", "error");
                                }
                            }
                        });
                    }
                });
            });
            
            $(function() {
                $('#product_id').change(function() {
                    if ($("#product_id :selected").val() == "") {
                        $('#forecastedTab').hide();
                        $('#quantityTab').hide();
                    } else {
                        $.ajax({
                            type: "POST",
                            url: "../SalesForecastServlet",
                            data: "action=getForecast"
                                + "&year=" + $("#year :selected").val()
                                + "&month=" + $("#month :selected").val()
                                + "&product_id=" + $("#product_id :selected").val(),
                            dataType: "xml",
                            cache: false,
                            success: function(data){
                                $('#forecastedTab').show();
                                $('#forecasted').val($(data).find('forecasted').text());
                                $('#quantityTab').show();
                                $('#quantity').val("");
                            }
                        });
                    }
                });
            });
            
            function getProductID(value,rowData,rowIndex) {
                return rowData.product.product_id;  
            }
            
            function getProductName(value,rowData,rowIndex) {
                return rowData.product.product_name;  
            }
            
            function addPlannedDmd(){
                var qty = parseInt(Math.round($('#quantity').val()));
                if (qty < 1) {
                    alertMsgStr("Create Production Plan", "Quantity must be greater than 0.", "error");
                } else {
                    $.ajax({
                        type: "POST",
                        url: "../ProductionPlanServlet",
                        data: "action=addPlannedDmd"
                            + "&product_id=" + $("#product_id :selected").val()
                            + "&quantity=" + $('#quantity').val()
                            + "&capacity=" + $('#capacity').val()
                            + "&listName=" + "createPDList",
                        dataType: "xml",
                        cache: false,
                        success: function(data){
                            $(data).find('reply').each(function(){
                                if ($(this).find('message').text() == "false") {
                                    alertMsgStr("Create Production Plan", "Rejected. Over 90% utilization rate.", "error");
                                } else {
                                    $('#utilization').val($(data).find('utilization').text());
                                    $('#create').show();
                                }
                                $('#product_id').val("");
                                $('#forecastedTab').hide();
                                $('#quantityTab').hide();
                                $('#display').datagrid('reload');
                            });    
                        }
                    });
                }
            }
            
            function deleteAction(value,rowData,rowIndex) {
                return '<input type="button" onclick="deletePlannedDmd('+rowIndex+','+rowData.product.product_id+');" value="Delete"/>';  
            }
            
            function deletePlannedDmd(rowIndex,product_id){
                window.parent.$.messager.confirm("Confirm", "Delete Product ID " + product_id + " from Planned Demand?", function(r){
                    if (r){
                        $.ajax({
                            type: "POST",
                            url: "../ProductionPlanServlet",
                            data: "action=deletePlannedDmd"
                                + "&position=" + rowIndex
                                + "&capacity=" + $('#capacity').val()
                                + "&listName=" + "createPDList",
                            dataType: "xml",
                            cache: false,
                            success: function(data){
                                var utilization = parseFloat($(data).find('utilization').text());
                                $('#utilization').val(utilization);
                                if (utilization > 0) {
                                    $('#create').show();
                                } else {
                                    $('#create').hide();
                                }
                                $('#display').datagrid('reload');
                            }
                        });
                    }     
                });
            }
            
            function onClickConfirm(){
                $('#createPP').panel('expand');
                $.ajax({
                    type: "POST",
                    url: "../ProductionPlanServlet",
                    data: "action=createPP"
                        + "&year=" + $("#year :selected").val()
                        + "&month=" + $("#month :selected").val()
                        + "&working_days=" + $('#working_days').val()
                        + "&capacity=" + $('#capacity').val()
                        + "&utilization=" + $('#utilization').val()
                        + "&listName=" + "createPDList",
                    dataType: "json",
                    cache: false,
                    success: function(data){
                        if(data.type!='error') {
                            alertMsg(data);
                            reloadAll();
                            closeTab('Create Production Plan');
                        }
                        alertMsg(data); 
                    }
                });
            }
        </script>
    </head>
    <body>
        <div style="padding:3px 2px;border-bottom:1px solid #ccc"><h2>Create Production Plan</h2></div>  
        <br/>
        <div id="createPP" class="easyui-panel" title="General Details:" style="padding:0px;background:#fafafa;" collapsible="true">
            <table class="tableForms">
                <tr>
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
                <tr id="workingDaysTab">
                    <td class="tableForms_label">Working Days:</td>
                    <td class="tableForms_userInput">
                        <input name="working_days" id="working_days" type="text" disabled />
                    </td>
                </tr>
                <tr id="capacityTab">
                    <td class="tableForms_label">Capacity (Hours):</td>
                    <td class="tableForms_userInput">
                        <input name="capacity" id="capacity" type="text" disabled />
                    </td>
                </tr>
                <tr id="utilizationTab">
                    <td class="tableForms_label">Utilization (%):</td>
                    <td class="tableForms_userInput">
                        <input name="utilization" id="utilization" type="text" disabled />
                    </td>
                </tr>
            </table>
        </div>
        <br/>
        <div id="plannedDmd" class="easyui-panel" title="Planned Demand:" style="padding:0px;background:#fafafa;" collapsible="false"> 
            <div id="tb">
                <table class="tableForms">
                    <tr id="productTab">
                        <td class="tableForms_label">Product:</td>
                        <td class="tableForms_userInput">
                            <select class="easyui-validatebox" name="product_id" id="product_id"></select>
                        </td>
                    </tr>
                    <tr id="forecastedTab">
                        <td class="tableForms_label">Forecasted (Boxes):</td>
                        <td class="tableForms_userInput">
                            <input name="forecasted" id="forecasted" type="text" disabled/>
                        </td>
                    </tr>
                    <tr id="quantityTab">
                        <td class="tableForms_label">Planned Quantity (Boxes):</td>
                        <td class="tableForms_userInput">
                            <input class="easyui-validatebox" name="quantity" id="quantity" type="text" required="true" />
                            &nbsp;&nbsp;&nbsp;&nbsp;
                            <a href="javascript:void(0);" id="addPDBt" class="easyui-linkbutton" onclick="addPlannedDmd()">Add</a>
                        </td>
                    </tr>
                </table>
            </div>
            <div fit="true" style="width:100%;height:200px;">
                <table id="display" class="easyui-datagrid" fit="true"
                       url="../ProductionPlanServlet?action=getPlannedDmdList&listName=createPDList"
                       toolbar="tb"
                       singleSelect="true" fitColumns="true"
                       rownumbers="false">
                    <thead>  
                        <tr> 
                            <th field="product_id" width="10%" formatter="getProductID">Product ID</th>
                            <th field="product_name" width="20%" formatter="getProductName">Product Name</th>
                            <th field="boxes_to_produce" width="20%">Planned Quantity</th>
                            <th field="hours_needed" width="40%">Hours Needed</th>
                            <th field="delete_action" width="10%" formatter="deleteAction"></th>
                        </tr>  
                    </thead>  
                </table>
            </div>
        </div>
        <br/>
        <div class="form_buttons" id="create">
            <input type="submit" onclick="onClickConfirm()" value="Create" />
        </div>
    </body>
</html>