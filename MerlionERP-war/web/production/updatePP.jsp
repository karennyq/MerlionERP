<%-- 
    Document   : updatePP
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
        <title>Update Production Plan</title>
        
        <% session.setAttribute("updatePDList" + request.getParameter("mo_id"), new ArrayList()); %>
        <% Calendar cal = Calendar.getInstance(); %>
        <% int year = cal.get(Calendar.YEAR); %>
        <% int month = cal.get(Calendar.MONTH); %>
        <% String[] monthName = {"January","February","March","April","May","June","July","August","September","October","November","December"}; %>
        
        <script type="text/javascript">
            $(document).ready(function(){
                getMODetails();
                
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
            
            function getMODetails(){
                $.ajax({
                    type: "POST",
                    url: "../ProductionPlanServlet",
                    data: "action=getMODetails"
                        + "&mo_id=" + $('#mo_id').val()
                        + "&listName=" + "updatePDList<%= request.getParameter("mo_id")%>",
                    dataType: "json",
                    cache: false,
                    success: function(data){
                        $('#mo_id').val(data.id);
                        $('#year').html(data.year);
                        $('#year_hidden').val(data.year);
                        $('#month').html(data.month);
                        $('#month_hidden').val(data.month);
                        $('#working_days').html(data.working_days);
                        $('#capacity').html(data.capacity);
                        $('#utilization').val(data.utilization);
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
                    alertMsgStr("Update Production Plan", "Quantity must be greater than 0.", "error");
                } else {
                    $.ajax({
                        type: "POST",
                        url: "../ProductionPlanServlet",
                        data: "action=addPlannedDmd"
                            + "&product_id=" + $("#product_id :selected").val()
                            + "&quantity=" + $('#quantity').val()
                            + "&capacity=" + $('#capacity').val()
                            + "&listName=" + "updatePDList<%= request.getParameter("mo_id")%>",
                        dataType: "xml",
                        cache: false,
                        success: function(data){
                            $(data).find('reply').each(function(){
                                if ($(this).find('message').text() == "false") {
                                    alertMsgStr("Update Production Plan", "Rejected. Over 90% utilization rate.", "error");
                                } else {
                                    $('#utilization').val($(data).find('utilization').text());
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
                                + "&listName=" + "updatePDList<%= request.getParameter("mo_id")%>",
                            dataType: "xml",
                            cache: false,
                            success: function(data){
                                var utilization = parseFloat($(data).find('utilization').text());
                                $('#utilization').val(utilization);
                                $('#display').datagrid('reload');
                            }
                        });
                    }     
                });
            }
            
            function onClickConfirm(){
                $('#updatePP').panel('expand');
                $.ajax({
                    type: "POST",
                    url: "../ProductionPlanServlet",
                    data: "action=updatePP"
                        + "&mo_id=" + $('#mo_id').val()
                        + "&listName=" + "updatePDList<%= request.getParameter("mo_id")%>",
                    dataType: "json",
                    cache: false,
                    success: function(data){
                        if(data.type!='error') {
                            alertMsg(data);
                            reloadAll();
                        }
                        alertMsg(data); 
                    }
                });
            }
        </script>
    </head>
    <body>
        <div style="padding:3px 2px;border-bottom:1px solid #ccc"><h2>Update Production Plan</h2></div>  
        <br/>
        <input type="hidden" name="mo_id" id="mo_id" value="<%= request.getParameter("mo_id")%>" />
        <input type="hidden" name="year_hidden" id="year_hidden" value="" />
        <input type="hidden" name="month_hidden" id="month_hidden" value="" />
        <div id="updatePP" class="easyui-panel" title="General Details:" style="padding:0px;background:#fafafa;" collapsible="true">
            <table class="tableForms">
                <tr>
                    <td class="tableForms_label">Year:</td>
                    <td class="tableForms_userInput" name="year" id="year"></td>
                </tr>
                <tr id="monthTab">
                    <td class="tableForms_label">Month:</td>
                    <td class="tableForms_userInput" name="month" id="month"></td>
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
            <div fit="true" style="width:100%;height:200px;">
                <table id="display" class="easyui-datagrid" fit="true"
                       url="../ProductionPlanServlet?action=getPlannedDmdList&listName=updatePDList<%= request.getParameter("mo_id")%>"
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
            <input type="submit" onclick="onClickConfirm()" value="Update" />
            <input type="button" onclick="sfReset()" value="Reset" />
        </div>
    </body>
</html>