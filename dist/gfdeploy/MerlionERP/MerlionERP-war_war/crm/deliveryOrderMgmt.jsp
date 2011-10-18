<%-- 
    Document   : DeliveryOrderMgmt
    Created on : Sep 30, 2011, 2:38:15 AM
    Author     : karennyq
--%>

<%@page import="org.persistence.DeliveryOrder"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="util.GVCRM"%>

<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Delivery Order Mgmt</title>
        <%@include file = "../global/global_include.jsp" %>
        <script type="text/javascript">
            $(document).ready(function(){
                $('#status').combobox({  
                    url:'../DeliveryOrderServlet?action=loadPage&content=statusDropdown',  
                    valueField:'id',  
                    textField:'text',
                    editable:false,
                    formatter:comboboxFomatter
                });
            });
            
            //filtering the table
            function filterTable(){
                var do_id=$('#do_id').val();
                var so_id=$('#so_id').val();
                var status=$('#status').combobox('getValue');
                var f_url='../DeliveryOrderServlet?action=loadPage&content=table&do_id='+do_id+'&so_id='+so_id+'&status='+status;
                $('#tt').datagrid({  
                    url:f_url });
                $('#tt').datagrid('reload');      
            }
            
            function reset(){
                $('#do_id').val("");
                $('#so_id').val("");
                $('#status').combobox('setValue',0);
                filterTable();
            }
            
            function formatActionUpdate(value,rowData,rowIndex){ 
                return '<input type="button" onclick="goUpdate('+rowData.do_id+');" value="Update"/>'; 
            }
            
            function goUpdate(do_id){  
                window.parent.addTab('update_delivery_order_'+do_id,'Update Delivery Order '+do_id,'<%=GVCRM.UPDATE_DELIVERY_ORDER%>?do_id='+do_id);
            }
            
            function formatSoId(value, rowData, rowIndex){
                return value.so_id;
            }
            
            function formatStatus(value, rowData, rowIndex){
                if(value == '<%=DeliveryOrder.Status.Inactive%>'){
                    return "<font color='red'>"+value+"</font>";                    
                }else{
                    return value;
                }
            }
        </script>
    </head>
    <body>
        <div style="padding:3px 2px;border-bottom:1px solid #ccc"><h2>Delivery Order Management</h2></div> 
        <br/>
        <div id="tb" style="padding:5px;height:auto">  
            <!--
            <div style="margin-bottom:5px">  
                <a href="javascript:void(0)" onclick="javascript:window.parent.addTab('add_sales_order','Create Sales Order','<%=GVCRM.CREATE_SALES_ORDER%>');" class="easyui-linkbutton" iconCls="icon-add" plain="true">Create Sales Order</a>   
            </div>
            -->
            <div>
                <table cellpadding="5">
                    <tr>
                        <td>Delivery Order ID:</td>
                        <td><input id="do_id" name="do_id" type="text"/></td>
                        <td>SO ID:</td>
                        <td><input id="so_id" name="so_id" type="text"/></td>
                        <td>Status:</td>
                        <td><input id="status" name="status" panelHeight="auto" class="easyui-combobox"/></td>
                        <td colspan="2" align="right">
                            <a href="#" class="easyui-linkbutton" onclick="filterTable()" iconCls="icon-search"></a>
                            <a href="#" class="easyui-linkbutton" onclick="reset()">Clear</a>
                        </td>
                    </tr>
                </table>
            </div>  
        </div>  
        <div fit="true" style="width:100%; height:500px">
            <table id="tt" class="easyui-datagrid" fit="true" style="width:100%;"  
                   url="../DeliveryOrderServlet?action=loadPage&content=table"   
                   toolbar="#tb"
                   singleSelect="true" fitColumns="true" pagination="true"
                   rownumbers="false">  
                <thead>  
                    <tr> 
                        <th field="delivery_order_id" width="25%" sortable="true">DO ID</th>
                        <th field="salesOrder" width="25%" sortable="true" formatter="formatSoId">SO ID</th>
                        <th field="deliveryStatus" width="25%" sortable="true">Delivery Status</th>
                        <th field="status" width="25%" sortable="true" formatter="formatStatus">Status</th>
                        <!--<th field="actionUpdate" align="right" width="25%" formatter="formatActionUpdate"></th>-->
                    </tr>  
                </thead>  
            </table> 
        </div>
    </body>
</html>
