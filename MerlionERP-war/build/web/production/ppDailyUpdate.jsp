<%-- 
    Document   : dailyPPUpdate
    Created on : Oct 16, 2011, 3:44:11 PM
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
        <title>Update Daily Production Plan</title>
        
        <% session.setAttribute("updateDPDList" + request.getParameter("do_id"), new ArrayList()); %>
        
        <script type="text/javascript">
            $(document).ready(function(){
                getDODetails();
                
                $('#quantity').numberbox({
                    precision:0
                });
                
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
                
                $('#forecastedTab').hide();
                $('#quantityTab').hide();
            });
            
            function getDODetails(){
                $.ajax({
                    type: "POST",
                    url: "../ProductionPlanServlet",
                    data: "action=getDODetails"
                        + "&do_id=" + $('#do_id').val()
                        + "&listName=" + "updateDPDList<%= request.getParameter("do_id")%>",
                    dataType: "json",
                    cache: false,
                    success: function(data){
                        $('#do_id').val(data.id);
                        $('#date').html(data.day);
                        $('#working_days').html(data.working_days);
                        $('#capacity').html(data.capacity);
                        $('#capacity_hidden').val(data.capacity);
                        $('#utilization').val(data.utilization);
                        $('#year_hidden').val(data.weeklyOverview.monthlyOverview.year);
                        $('#month_hidden').val(data.weeklyOverview.monthlyOverview.month);
                        $('#display').datagrid('reload');
                    }
                });
            }
            
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
                                + "&year=" + $('#year_hidden').val()
                                + "&month=" + $('#month_hidden').val()
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
                    alertMsgStr("Update Daily Production Plan", "Quantity must be greater than 0.", "error");
                } else {
                    $.ajax({
                        type: "POST",
                        url: "../ProductionPlanServlet",
                        data: "action=addPlannedDmd"
                            + "&product_id=" + $("#product_id :selected").val()
                            + "&quantity=" + $('#quantity').val()
                            + "&capacity=" + $('#capacity_hidden').val()
                            + "&listName=" + "updateDPDList<%= request.getParameter("do_id")%>",
                        dataType: "xml",
                        cache: false,
                        success: function(data){
                            $(data).find('reply').each(function(){
                                if ($(this).find('message').text() == "false") {
                                    alertMsgStr("Update Daily Production Plan", "Rejected. Over 90% utilization rate.", "error");
                                } else {
                                    $('#utilization').val($(data).find('utilization').text());
                                    $('#update').show();
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
                                + "&capacity=" + $('#capacity_hidden').val()
                                + "&listName=" + "updateDPDList<%= request.getParameter("do_id")%>",
                            dataType: "xml",
                            cache: false,
                            success: function(data){
                                var utilization = parseFloat($(data).find('utilization').text());
                                $('#utilization').val(utilization);
                                if (utilization > 0) {
                                    $('#update').show();
                                } else {
                                    $('#update').hide();
                                }
                                $('#display').datagrid('reload');
                            }
                        });
                    }     
                });
            }
            
            function onClickConfirm(){
                $('#updateDPP').panel('expand');
                $.ajax({
                    type: "POST",
                    url: "../ProductionPlanServlet",
                    data: "action=updateDPP"
                        + "&do_id=" + $('#do_id').val()
                        + "&utilization=" + $('#utilization').val()
                        + "&listName=" + "updateDPDList<%= request.getParameter("do_id")%>",
                    dataType: "json",
                    cache: false,
                    success: function(data){
                        if(data.type!='error') {
                            reloadAll();
                        }
                        alertMsg(data); 
                    }
                });
            }
            
            function ppReset(){
                window.parent.$.messager.confirm("Confirm", 'Reset the form?', function(r){
                    if (r){
                        getDODetails();
                        $('#update').show();
                    }
                });
            }
        </script>
    </head>
    <body>
        <div style="padding:3px 2px;border-bottom:1px solid #ccc"><h2>Update Daily Production Plan</h2></div>  
        <br/>
        <input type="hidden" name="do_id" id="do_id" value="<%= request.getParameter("do_id")%>" />
        <input type="hidden" name="year_hidden" id="year_hidden" value="" />
        <input type="hidden" name="month_hidden" id="month_hidden" value="" />
        <input type="hidden" name="capacity_hidden" id="capacity_hidden" value="" />
        <div id="updateDPP" class="easyui-panel" title="General Details:" style="padding:0px;background:#fafafa;" collapsible="true">
            <table class="tableForms">
                <tr>
                    <td class="tableForms_label">Date:</td>
                    <td class="tableForms_userInput" name="date" id="date"></td>
                </tr>
                <tr id="workingDaysTab">
                    <td class="tableForms_label">Working Days:</td>
                    <td class="tableForms_userInput" name="working_days" id="working_days"></td>
                </tr>
                <tr id="capacityTab">
                    <td class="tableForms_label">Capacity (Hours):</td>
                    <td class="tableForms_userInput" name="capacity" id="capacity"></td>
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
            <div fit="true" style="width:100%;height:250px;">
                <table id="display" class="easyui-datagrid" fit="true"
                       url="../ProductionPlanServlet?action=getPlannedDmdList&listName=updateDPDList<%= request.getParameter("do_id")%>"
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
            <input type="submit" onclick="onClickConfirm()" value="Update" id="update" name="update" />
            <input type="button" onclick="ppReset()" value="Reset" />
        </div>
    </body>
</html>