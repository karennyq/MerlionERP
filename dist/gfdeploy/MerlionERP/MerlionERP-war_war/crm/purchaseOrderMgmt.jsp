<%-- 
    Document   : purchaseOrderMgmt
    Created on : Sep 25, 2011, 10:10:58 PM
    Author     : karennyq
--%>

<%@page import="org.persistence.PurchaseOrder"%>
<%@page import="util.GVCRM"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Purchase Order Mgmt</title>
        <%@include file = "../global/global_include.jsp" %>
        <script type="text/javascript">
            function formatActionUpdate(value,rowData,rowIndex){ 
                return '<input type="button" onclick="goUpdate('+rowData.po_id+');" value="Update"/>'; 
            }
            
            function formatPrice(value,rowData,rowIndex){ 
                return "S$"+value.toFixed(2);
            }
            
            function formatInquirerId(value,rowData,rowIndex){
                try{
                    return (value.inquirer_id==null)?"":value.inquirer_id; 
                }catch(err){
                }
            }
            
            function formatQuotationId(value,rowData,rowIndex){
                try{
                    return (value.pre_sale_doc_id==null)?"":value.pre_sale_doc_id; 
                }catch(err){
                }
            }
            
            function goUpdate(po_id){  
                window.parent.addTab('update_purchase_order_'+po_id,'Update Purchase Order: '+po_id,'<%=GVCRM.UPDATE_PURCHASE_ORDER%>?po_id='+po_id);
            }
            
            //filtering the table
            function filterTable(){
                var po_ref_no=$('#po_ref_no').val();
                var inquirer_id=$('#inquirer_id').val();
                var approved_status=$('#approved_status').combobox('getValue');
                var f_url='../PurchaseOrderServlet?action=loadPage&content=table&po_ref_no='+po_ref_no+'&inquirer_id='+inquirer_id+'&approved_status='+approved_status;
                $('#tt').datagrid({  
                    url:f_url });
                $('#tt').datagrid('load');      
            }
            
            function reset(){
                $('#po_ref_no').val("");
                $('#inquirer_id').val("");
                $('#approved_status').combobox('setValue',0);
                filterTable();
            }
            
            $(document).ready(function(){
                $('#approved_status').combobox({  
                    url:'../PurchaseOrderServlet?action=loadPage&content=approvedStatusDropdown',  
                    valueField:'id',  
                    textField:'text',
                    editable:false
                });
            });
            
            function goConvert(po_id){
                $.ajax({
                    type: "POST",
                    url:"../PurchaseOrderServlet",
                    data: "action=convertToSO&po_id="+po_id,
                    dataType: "json",
                    cache: false,
                    success: function(data){
                        if(data.type!='error'){ 
                            reloadAll();
                            alertMsg(data);  
                        }else{
                            alertMsg(data);    
                        }
                    }
                });
            }
            
            function formatStatus(value, rowData, rowIndex){
                if(value == '<%=PurchaseOrder.Status.Inactive%>'){
                    return "<font color='red'>"+value+"</font>";                    
                }else{
                    return value;
                }
            }
            
            //on link
            function formatActionConvert(value,rowData,rowIndex){  
                if(rowData.approved_status == '<%=PurchaseOrder.ApprovedStatus.Pending.name()%>'){
                    return '<input type="button" onclick="goUpdate('+rowData.po_id+');" value="Update"/>'; 
                }else if(rowData.approved_status == '<%=PurchaseOrder.ApprovedStatus.Approved.name()%>'){
                    return '<input type="button" onclick="goConvert('+rowData.po_id+');" value="Convert to SO"/>'; 
                }else{
                    return '<input type="button" onclick="goUpdate('+rowData.po_id+');" value="Update"/>'; 
                }
            }        
                
            function formatActionUpdate(value,rowData,rowIndex){  
                if(rowData.convert_status=='Converted'){
                    return '<input type="button" disabled="disabled" onclick="goUpdate('+rowData.inquirer_id+');" value="Update"/>';      // return '<a href="javascript:void(0)" onclick="goConvert('+rowData.inquirer_id+');">Convert to Customer</a>';  
                }
                else if(rowData.convert_status=='Not_Converted'){
                    return '<input type="button" onclick="goUpdate('+rowData.inquirer_id+');" value="Update"/>';    
                }
            }
        </script>
    </head>
    <body>
        <div style="padding:3px 2px;border-bottom:1px solid #ccc"><h2>Purchase Order Management</h2></div> 
        <br/>
        <div id="tb" style="padding:5px;height:auto">  
            <div style="margin-bottom:5px">  
                <a href="javascript:void(0)" onclick="javascript:window.parent.addTab('add_purchase_order','Create Purchase Order','<%=GVCRM.CREATE_PURCHASE_ORDER%>?tabTitle=Create+Purchase+Order');" class="easyui-linkbutton" iconCls="icon-add" plain="true">Create Purchase Order</a>
            </div>  
            <div>  
                <table cellpadding="5">
                    <tr>
                        <td>PO Ref. ID:</td>
                        <td><input name="po_ref_no" id="po_ref_no" type="text"/></td>
                        <td>Customer ID:</td>
                        <td><input name="inquirer_id" id="inquirer_id" style="width:100px" type="text"/></td>
                        <td>Status:</td>
                        <td><input name="approved_status" id="approved_status" panelHeight="auto" class="easyui-combobox"/></td>
                        <td>
                            <a href="#" class="easyui-linkbutton" onclick="filterTable()" iconCls="icon-search"></a>  
                            <a href="#" class="easyui-linkbutton" onclick="reset()">Reset</a>
                        </td>
                    </tr>
                </table>
            </div>  
        </div>
        <div fit="true" style="width:100%; height:500px">
            <table id="tt" class="easyui-datagrid" fit="true" style="width:100%;"  
                   url="../PurchaseOrderServlet?action=loadPage&content=table"   
                   toolbar="#tb"  
                   singleSelect="true" fitColumns="true" pagination="true"
                   rownumbers="false">  
                <thead>  
                    <tr> 
                        <th field="po_id" width="5%" sortable="true">ID</th>  
                        <th field="po_reference_no" width="10%" sortable="true">PO Ref. ID</th>  
                        <th field="customer" width="10%" formatter="formatInquirerId" sortable="true">Customer ID</th>
                        <th field="salesQuotation" width="10%" formatter="formatQuotationId" sortable="true">Quotation ID</th>
                        <th field="discounted_total" width="10%" sortable="true" formatter="formatPrice">Disc. Total</th>
                        <th field="received_date" width="15%" sortable="true">Received Date</th>
                        <th field="approved_status" width="15%" sortable="true">Approved status</th>
                        <th field="status" width="10%" sortable="true" formatter="formatStatus">Status</th>
                        <th field="actionConvert" align="right" width="15%" formatter="formatActionConvert"></th>
                    </tr>  
                </thead>  
            </table> 
        </div>
    </body>
</html>
