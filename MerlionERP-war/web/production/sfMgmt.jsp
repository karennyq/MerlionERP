<%-- 
    Document   : sfMgmt
    Created on : Sep 17, 2011, 12:17:18 PM
    Author     : Randy
--%>

<%@page import="util.GVPRODUCTION"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Sales Forecast Mgmt</title>
        <%@include file = "../global/global_include.jsp" %>
        <script type="text/javascript">
            function getProductName(value,rowData,rowIndex) {
                return rowData.product.product_name;  
            }
            
            function updateAction(value,rowData,rowIndex) {
                return '<input type="button" onclick="goUpdate('+rowData.sales_forecast_id+');" value="Update"/>';  
            }
            
            function goUpdate(sales_forecast_id){
                window.parent.addTab('update_sf'+sales_forecast_id,'Update Sales Forecast '+sales_forecast_id,'<%=GVPRODUCTION.UPDATE_SF%>?sales_forecast_id='+sales_forecast_id);
            }
        </script>
    </head>
    <body>
        <div style="padding:3px 2px;border-bottom:1px solid #ccc"><h2>Sales Forecast Management</h2></div> 
        <br/>
        <div id="tb" style="padding:5px;height:auto">  
            <div style="margin-bottom:5px">  
                <a href="javascript:void(0)" onclick="javascript:window.parent.addTab('add_sf','Create Sales Forecast','<%=GVPRODUCTION.CREATE_SF%>');" class="easyui-linkbutton" iconCls="icon-add" plain="true">Create Sales Forecast</a>   
            </div>  
            <div>
                <table cellpadding="5">
                    <td>Product:</td>
                    <td><input type="text" /></td>
                    <td>Year:</td>
                    <td><input type="text" /></td>
                    <td>Month:</td>
                    <td><input type="text" /></td>
                    <td><a href="#" class="easyui-linkbutton" iconCls="icon-search"></a></td>
                </table>  
            </div>  
        </div>  
        <div fit="true" style="width:100%; height:500px">
            <table id="tt" class="easyui-datagrid" fit="true" style="width:100%;"  
                   url="../SalesForecastServlet?action=loadTable"   
                   toolbar="#tb"
                   singleSelect="true" fitColumns="true" pagination="true"
                   rownumbers="false">
                <thead>  
                    <tr>
                        <th field="sales_forecast_id" width="5%">ID</th>
                        <th field="year" width="10%">Year</th>
                        <th field="month" width="10%">Month</th>
                        <th field="yoy_growth" width="15%">YoY Growth (%)</th>
                        <th field="product_name" width="30%" formatter="getProductName">Product Name</th>
                        <th field="amt_forecasted" width="20%">Amount Forecasted</th>
                        <th field="update_action" width="10%" formatter="updateAction"></th>
                    </tr>  
                </thead>  
            </table> 
        </div>
    </body>
</html>