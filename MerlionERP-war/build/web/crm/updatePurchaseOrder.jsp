<%-- 
    Document   : createPurchaseOrder
    Created on : Sep 25, 2011, 10:10:46 PM
    Author     : karennyq
--%>
<%@page import="util.GVCRM"%>
<%@page import="org.persistence.Customer"%>
<%@page import="org.persistence.SalesLead"%>
<%@page import="org.persistence.PurchaseOrder"%>

<%@page import="java.util.ArrayList"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <%@include file = "../global/global_include.jsp" %>
        <title>Update Purchase Order</title>
        <script type="text/javascript"> 
            $(document).ready(function(){
                $('#pdt_qty').numberbox({  
                    min:1,  
                    precision:0
                });
                
                $('#po_id_hidden').val('<%=request.getParameter("po_id")%>');
                getPurchaseOrderInfo();
                
                $('#status').combobox({  
                    url:'../PurchaseOrderServlet?action=loadPage&content=statusDropdown',  
                    valueField:'text',  
                    textField:'text',
                    editable:false,
                    required:true,
                    formatter:comboboxFomatter
                });
                
                //ptd table  
                $('#pdttt').datagrid({
                    onBeforeEdit:function(index,row){  
                        row.editing = true;  
                        updateActions();  
                    },  
                    onAfterEdit:function(index,row){  
                        row.editing = false; 
                        $.ajax({
                            type: "POST",
                            url: "../PurchaseOrderServlet",
                            data: "action=updateProductItem&update_qty="+row.quantity +"&listIndex="+index+"&lineItemListSession=updatePurchaseOrderLineItemList"+<%=request.getParameter("po_id")%>+"&discountSession=updatePurchaseOrderDiscount"+<%=request.getParameter("po_id")%>,
                            dataType: "json",
                            cache: false,
                            success: function(data){
                                if(data.type!='error'){
                                    $('#pdttt').datagrid('reload');
                                }
                                alertMsg(data);    
                            }
                        }); 
                        updateActions();  
                    },  
                    onCancelEdit:function(index,row){  
                        row.editing = false;  
                        updateActions(); 
                        $('#pdttt').datagrid('reloadFooter');
                    }
                });
                
                $.ajax({
                    type: "POST",
                    url: "../ProductServlet",
                    data: "action=getProducts&lineItemListSession=updatePurchaseOrderLineItemList"+<%=request.getParameter("po_id")%>+"&discountSession=updatePurchaseOrderDiscount"+<%=request.getParameter("po_id")%>,
                    dataType: "xml",
                    cache: false,
                    success: function(data){
                        $('#product_id').append('<option value=\'\'></option>');
                        $(data).find('product').each(function(){
                            $('#product_id').append('<option value=\'' + $(this).find('product_id').text() + '\'>' + $(this).find('product_name').text() + '</option>');
                        });
                    }
                });
            });
            
            function updateActions(){  
                var rowcount = $('#pdttt').datagrid('getRows').length;  
                for(var i=0; i<rowcount; i++){ 
                    $('#pdttt').datagrid('updateRow',{  
                        index:i,  
                        row:{actions:''}  
                    });  
                }  
            }  
            
            function addPdt(){
                var productId = $('#product_id').val();
                var pdtQty = $('#pdt_qty').val();
                $.ajax({
                    type: "POST",
                    url: "../PurchaseOrderServlet",
                    data: "action=addProduct&product_id="+productId+"&pdt_qty="+pdtQty+"&lineItemListSession=updatePurchaseOrderLineItemList"+<%=request.getParameter("po_id")%>+"&discountSession=updatePurchaseOrderDiscount"+<%=request.getParameter("po_id")%>,
                    dataType: "json",
                    cache: false,
                    success: function(data){
                        if(data.type!='error'){                            
                            $('#product_id').val("");
                            $('#pdt_qty').val("");
                            $('#pdttt').datagrid('reload');    
                        }else{
                            alertMsg(data);    
                        }
                    }
                });
            }
            
            function formatProduct(value,rowData,rowIndex){
                try{
                    return (value.product_name==null)?"":value.product_name; 
                }catch(err){
                }
            }
            
            function add_discount_load(){
                $('#add_discount').numberbox({  
                    min:0.0,  
                    precision:2,  
                    required:true
                });  
            }
            
            function add_discount_change(){
                var add_disc=$("#add_discount").val();
                
                $.ajax({
                    type: "POST",
                    url:"../PurchaseOrderServlet",
                    data: "action=updateDiscount&add_disc="+add_disc+"&lineItemListSession=updatePurchaseOrderLineItemList"+<%=request.getParameter("po_id")%>+"&discountSession=updatePurchaseOrderDiscount"+<%=request.getParameter("po_id")%>,
                    dataType: "json",
                    cache: false,
                    success: function(data){
                        if(data.type!='error'){                            
                            alertMsg(data);  
                            $('#pdttt').datagrid('reload');   
                        }else{
                            alertMsg(data);    
                        }
                    }
                });
            }
            
            function formatTotal(value,row,rowIndex){   
                if((row.bulk_discount=="Sub Total:")||(row.bulk_discount=="Net Total:")){
                    try{
                        value = value.toFixed(2);
                        return "S$"+value; 
                    }catch(err){
                    }
                }else if(row.bulk_discount=="Lead Time:"){
                    if(value ==null){
                        return "";
                    }else{
                        value = value.toFixed(0);
                        return value+" Working Day(s)";
                    }
                }else if((row.bulk_discount=="Add. Discount:")){
                    try{
                        value = value.toFixed(2);
                        var content='<input id="add_discount" size="6" onfocus="add_discount_load()" onChange="add_discount_change()" value="'+value+'" name="add_discount"/>%';
                        return content; 
                    }catch(err){
                    }
                }else{
                    try{
                        value = value.toFixed(2);
                        return value; 
                    }catch(err){
                    } 
                }
            }
            
            function formatBulkDiscount(value,row,rowIndex){
                if((row.bulk_discount=="Sub Total:")||(row.bulk_discount=="Add. Discount:")||(row.bulk_discount=="Net Total:")||(row.bulk_discount=="Lead Time:")){     
                    return value;
                }else{
                    try{
                        value = value.toFixed(2);
                        return value+"%";
                    }catch(err){
                    } 
                }
            }
            
            function formatBasePrice(value,rowData,rowIndex){
                try{
                    value = value.toFixed(2);
                    return "S$"+value;
                }catch(err){   
                } 
            }
            
            function formatUnitPrice(value,rowData,rowIndex){
                try{
                    if(rowData.base_price ==null && rowData.quantity ==null){
                        return "";
                    }else{
                        var unitPrice = rowData.base_price * rowData.quantity
                        unitPrice = unitPrice.toFixed(2);
                        return "S$"+unitPrice;
                    }
                }catch(err){
                } 
            }
            
            function formatLeadTime(value,rowData,rowIndex){
                if(value ==null){
                    return "";
                }else{
                    return value+" Working Day(s)";
                }
            } 
            
            function formatActions(value,row,index){ 
                if((row.bulk_discount=="Sub Total:")||(row.bulk_discount=="Add. Discount:")||(row.bulk_discount=="Net Total:")||(row.bulk_discount=="Lead Time:")){
                    return "";   
                }else if (row.editing){  
                    var s = '<input type="button" onclick="saverow('+index+')" value="Save"/> ';  
                    var c = '<input type="button" onclick="cancelrow('+index+')" value="Cancel"/>';  
                    return s+c;  
                } else {  
                    var e = '<input type="button" onclick="editrow('+index+')" value="Edit"/> ';  
                    var d = '<input type="button" onclick="deletePdtItem('+index+')" value="Delete"/>';  
                    return e+d;  
                }  
            }
            
            function editrow(index){  
                $('#pdttt').datagrid('beginEdit', index);  
            }  
             
            function saverow(index){  
                $('#pdttt').datagrid('endEdit', index);  
            }
            
            function cancelrow(index){  
                $('#pdttt').datagrid('cancelEdit', index);  
            }
            
            function deletePdtItem(rowIndex){
                window.parent.$.messager.confirm("Confirm", "Delete the selected item?", function(r){
                    if (r){
                        $.ajax({
                            type: "POST",
                            url: "../PurchaseOrderServlet",
                            data: "action=removeProductItem&listIndex="+rowIndex+"&lineItemListSession=updatePurchaseOrderLineItemList"+<%=request.getParameter("po_id")%>+"&discountSession=updatePurchaseOrderDiscount"+<%=request.getParameter("po_id")%>,
                            dataType: "json",
                            cache: false,
                            success: function(data){
                                if(data.type!='error'){
                                    $('#pdttt').datagrid('reload');
                                }
                                alertMsg(data);    
                            }
                        });    
                    }     
                });
            }
            
            function onClickConfirm(){
                $('#poDetails').panel('expand');
                $('#pdtDetails').panel('expand');
            }
            
            function custReset(formId,content){
                window.parent.$.messager.confirm("Confirm", content, function(r){
                    if (r){
                        getPurchaseOrderInfo();
                        $('#product_id').val("");
                        $('#pdt_qty').val("");
                        $('add_discount').val("");
                        $('#pdttt').datagrid('reload');
                        $('#buttons').show();
                    }
                });
            }
            
            function getPurchaseOrderInfo(){
                var poID= $('#po_id_hidden').val();
                $.ajax({
                    type: "POST",
                    url: "../PurchaseOrderServlet",
                    data: "action=loadPage&content=purchaseOrderDetails&po_id="+poID+"&lineItemListSession=updatePurchaseOrderLineItemList"+<%=request.getParameter("po_id")%>+"&discountSession=updatePurchaseOrderDiscount"+<%=request.getParameter("po_id")%>,
                    dataType: "json",
                    cache: false,
                    success: function(data){               
                        $('#po_id').html(poID);
                        $('#poDetails').panel("open");
                        $('#pdtDetails').panel("open");
                        $('#buttons').show();
                        $('#customer_id').html(data.customer.inquirer_id);
                        $('#company_name').html(data.customer.company_name); //change all these to val for textbox
                        $('#company_address').html(data.customer.company_add);
                        $('#contact_person').html(data.customer.contact_person);
                        $('#contact_no').html(data.customer.contact_no);
                        $('#fax_no').html(data.customer.fax_no);
                        $('#email').html(data.customer.email); 
                        $('#sent_date').val(data.sent_date);
                        $('#received_date').html(data.received_date);
                        $('#shipping_address').val(data.shipping_Address);
                        $('#remarks').val(data.remarks);
                        $('#po_ref_no').val(data.po_reference_no);
                        $('#status').combobox('select',data.approved_status);
                            
                        var cust_type = data.customer.cust_type;
                        if(cust_type == "<%=Customer.CustomerType.Direct_Sale.name()%>"){
                            $('#cust_type').html("Customer ("+"<%=Customer.CustomerType.Direct_Sale.name().replace('_', ' ')%>"+")");
                            $('#cust_type_hidden').val("<%=Customer.CustomerType.Direct_Sale.name()%>");
                        }else if(cust_type == "<%=Customer.CustomerType.Wholesale.name()%>"){
                            $('#cust_type').html("Customer ("+"<%=Customer.CustomerType.Wholesale.name()%>"+")"); 
                            $('#cust_type_hidden').val("<%=Customer.CustomerType.Wholesale.name()%>");    
                        }
                            
                        $('#pdttt').datagrid('reload');
                        $('#pdtDetails').panel('collapse');
                        $('#pdtDetails').panel('expand');
                    }
                });
            }
           
            function formatClientID(value,rowData,rowIndex){
                return value.inquirer_id;
            }
            
            function formatCompanyName(value,rowData,rowIndex){
                return rowData.inquirer.company_name;
            }
            
            function formatContactPerson(value,rowData,rowIndex){
                return rowData.inquirer.contact_person;            
            }
            
            $(function() {
                $("#sent_date").datepicker({
                    dateFormat: 'dd/mm/yy'
                });            
            });
            
            $(function(){
                $('#ff').form({
                    url:'../PurchaseOrderServlet?action=updatePurchaseOrder&lineItemListSession=updatePurchaseOrderLineItemList<%=request.getParameter("po_id")%>&discountSession=updatePurchaseOrderDiscount<%=request.getParameter("po_id")%>',
                    onSubmit:function(){  
                        return $(this).form('validate');
                    },
                    success:function(data){
                        var obj = jQuery.parseJSON(data); 
                        if((obj.type!='error')){
                            getPurchaseOrderInfo();
                            $('#product_id').val("");
                            $('#pdt_qty').val("");
                            $('add_discount').val("");
                            $('#pdttt').datagrid('reload');
                            $('#buttons').show();                            
                            //var f = window.parent;
                            reloadAll();           
                        }
                        alertMsg(obj);
                    }
                });
            });
        </script> 
    </head>
    <body>
        <div style="padding:3px 2px;border-bottom:1px solid #ccc"><h2>Update Purchase Order</h2></div>  
        <br/>
        <form id="ff" method="post">
            <input type="hidden" id="po_id_hidden" name="po_id_hidden"/>
            <input type="hidden" id="cust_type_hidden" name="cust_type_hidden"/>
            <div id="poDetails" name="poDetails" class="easyui-panel" title="Purchase Order Details:" style="padding:0px;background:#fafafa;" collapsible="true"> 
                <table class="tableForms"> 
                    <tr>
                        <td class="tableForms_label">PO ID:</td>
                        <td><label id="po_id" name="po_id"></label>
                        </td>
                    </tr>
                    <tr>
                        <td class="tableForms_label">PO Reference No:</td>  
                        <td class="tableForms_userInput">
                            <input class="easyui-validatebox" id="po_ref_no" name="po_ref_no" type="text" required="true"/>
                        </td>  
                    </tr>
                    <tr>
                        <td class="tableForms_label">Customer ID:</td>  
                        <td class="tableForms_userInput">
                            <label id="customer_id" name="customer_id"></label>
                        </td>  
                    </tr>
                    <tr>
                        <td class="tableForms_label">Company Name:</td>  
                        <td class="tableForms_userInput">
                            <label id="company_name" name="company_name"></label>
                        </td>  
                    </tr> 
                    <tr>
                        <td class="tableForms_label">Company Address:</td>  
                        <td class="tableForms_userInput">
                            <label id="company_address" name="company_address"></label>
                        </td>  
                    </tr> 
                    <tr>  
                        <td class="tableForms_label">Contact Person:</td>  
                        <td class="tableForms_userInput">
                            <label id="contact_person" name="contact_person"></label>
                        </td>  
                    </tr>
                    <tr>
                        <td class="tableForms_label">Contact:</td>  
                        <td class="tableForms_userInput">
                            <label id="contact_no" name="contact_no"></label>
                        </td>  
                    </tr>
                    <tr>
                        <td class="tableForms_label">Email:</td>  
                        <td class="tableForms_userInput">
                            <label id="email" name="email"></label>
                        </td> 
                    </tr>
                    <tr>  
                        <td class="tableForms_label">Fax No:</td>  
                        <td class="tableForms_userInput">
                            <label id="fax_no" name="fax_no"></label>
                        </td>  
                    </tr>
                    <tr>
                        <td class="tableForms_label">Customer Type:</td>   <!-- set to combo -->
                        <td class="tableForms_userInput">
                            <label id="cust_type" name="cust_type" ></label>
                        </td>  
                    </tr>
                    <!--
                    <tr>  
                        <td class="tableForms_label">Payment Mode:</td>
                        <td class="tableForms_userInput">
                            <input id="payment_mode" name="payment_mode" required="true"></textarea>
                        </td>  
                    </tr>
                    -->
                    <tr>
                        <td class="tableForms_label">Sent Date:</td>
                        <td><input name="sent_date" id="sent_date" type="text" class="easyui-validatebox" required="true" readonly="readonly"/></td>
                    </tr>
                    <tr>
                        <td class="tableForms_label">Received Date:</td>
                        <td><label name="received_date" id="received_date"/></td>
                    </tr>
                    <tr>  
                        <td class="tableForms_label">Shipping Address:</td>  
                        <td class="tableForms_userInput">
                            <textarea id="shipping_address" name="shipping_address" class="easyui-validatebox" required="true"></textarea>
                        </td>  
                    </tr>
                    <tr>  
                        <td class="tableForms_label">Remarks:</td>  
                        <td class="tableForms_userInput"><textarea id="remarks" name="remarks"></textarea></td>  
                    </tr>
                    <tr>  
                        <td class="tableForms_label">Status:</td>  
                        <td class="tableForms_userInput">
                            <input id="status" name="status" panelHeight="auto"/>
                        </td>  
                    </tr>
                </table>  
            </div>
            <br/>
            <div id="pdtDetails" name="pdtDetails" class="easyui-panel" title="Product Details:" style="padding:0px;background:#fafafa;overflow:hidden;" collapsible="true" hidden="true"  >
                <div id="findPdt" style="padding:5px;height:auto">  
                    <div>
                        <table cellpadding="5">
                            <tr>
                                <td>Product:</td>
                                <td><select class="easyui-validatebox" name="product_id" id="product_id"></select></td>
                                <td>Quantity (Boxes):</td>
                                <td><input class="easyui-numberbox" id="pdt_qty" name="pdt_qty" type="text"/></td>
                                <td><a href="javascript:void(0);" id="addPdtBt" class="easyui-linkbutton" onclick="addPdt()">Add</a></td>
                            </tr>
                        </table>
                    </div>
                </div>
                <div id="pdt-add-tt" name="pdt-add-tt" fit=true style="height:250px;">
                    <table id="pdttt" name="pdttt" class="easyui-datagrid" fit="true" style="width:100%; height:100px;" 
                           url='../PurchaseOrderServlet?action=loadPage&content=pdtTable&lineItemListSession=updatePurchaseOrderLineItemList<%=request.getParameter("po_id")%>&discountSession=updatePurchaseOrderDiscount<%=request.getParameter("po_id")%>'
                           fitColumns="true"
                           rownumbers="true"
                           showfooter="true">  
                        <thead>  
                            <tr> 
                                <th field="product" width="20%" formatter="formatProduct">Product Name</th>
                                <th field="base_price" width="10%" formatter="formatBasePrice">Unit Price</th>
                                <th field="quantity" editor="{
                                    type:'numberbox',  
                                    options:{  
                                    min:1,  
                                    precision:0,    
                                    required:true }
                                    }" width="10%">Quantity</th>
                                <th field="bulk_discount" formatter="formatBulkDiscount" width ="15%">Bulk Discount</th>
                                <th field="actual_price" width="15%" formatter="formatTotal">Total</th>
                                <th field="indicative_lead_time" width="20%" formatter="formatLeadTime">Indicative Lead Time</th>
                                <th field="actions" width="10%" formatter="formatActions"></th>
                            </tr>  
                        </thead>
                    </table>
                </div>
            </div>
            <br/>
            <div id="buttons" name="buttons" class="form_buttons" hidden="true">
                <input type="submit" value="Update" onclick="onClickConfirm();"/>
                <input type="button" onclick="custReset('ff','Reset the form?')" value="Reset" />
            </div>
        </form>
    </body>
</html>