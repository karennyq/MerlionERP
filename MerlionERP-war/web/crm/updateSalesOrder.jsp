<%-- 
    Document   : updateSalesOrder
    Created on : Sep 30, 2011, 3:44:11 PM
    Author     : karennyq
--%>

<%@page import="org.persistence.SalesLead"%>
<%@page import="org.persistence.LineItem"%>
<%@page import="java.util.ArrayList"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <%@include file = "../global/global_include.jsp" %>
        <title>Update Sales Order</title>
        <script type="text/javascript"> 
            <% session.setAttribute("updateSOLineItemList" + request.getParameter("so_id"), new ArrayList()); %>
            <% session.setAttribute("updateSODiscount" + request.getParameter("so_id"), "0.00"); %>
                
            $(document).ready(function(){                
                getSODetails();
                
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
                
                $('#pdt_qty').numberbox({  
                    min:1,  
                    precision:0
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
                            url: "../SalesOrderServlet",
                            data: "action=updateProductItem"
                                + "&update_qty=" + row.quantity
                                + "&listIndex=" + index
                                + "&listName=" + "updateSOLineItemList<%= request.getParameter("so_id")%>",
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
            });
            
            function getSODetails(){
                $.ajax({
                    type: "POST",
                    url: "../SalesOrderServlet",
                    data: "action=getSODetails"
                        + "&so_id=" + $('#so_id').val()
                        + "&listName=" + "updateSOLineItemList<%= request.getParameter("so_id")%>"
                        + "&discountName=" + "updateSODiscount<%= request.getParameter("so_id")%>",
                    dataType: "json",
                    cache: false,
                    success: function(data){
                        $('#so_id').val(data.so_id);
                        $('#inquirer_id').html(data.purchaseOrder.customer.inquirer_id);
                        $('#company_name').html(data.purchaseOrder.customer.company_name);
                        $('#contact_person').html(data.purchaseOrder.customer.contact_person);
                        $('#contact_no').html(data.purchaseOrder.customer.contact_no);
                        $('#email').html(data.purchaseOrder.customer.email);
                        $('#fax_no').html(data.purchaseOrder.customer.fax_no);
                        if(data.purchaseOrder.customer.convert_status == "<%=SalesLead.ConvertStatus.Not_Converted.name()%>"){
                            $('#cust_type').html("Sales Lead");
                        }else{     
                            var cust_type = data.purchaseOrder.customer.cust_type.replace("_"," ");
                            $('#cust_type').html("Customer ("+cust_type+")");
                        }
                        $.each(data.deliveryOrders, function(i, item) {
                            $('#destination').val(item.destination);
                        });
                        $('#pdttt').datagrid('reload');
                    }
                });
            } 
             
            function addPdt(){
                var productId = $('#product_id').val();
                var pdtQty = $('#pdt_qty').val();
                $.ajax({
                    type: "POST",
                    url: "../SalesOrderServlet",
                    data: "action=addProduct"
                        + "&product_id=" + productId
                        + "&pdt_qty=" + pdtQty
                        + "&listName=" + "updateSOLineItemList<%= request.getParameter("so_id")%>",
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
                    precision:2  
                });  
            }
            
            function add_discount_change(){
                var add_disc=$("#add_discount").val();
                if($("#add_discount").val()==""){
                    $("#add_discount").val("0.00");
                };
                $.ajax({
                    type: "POST",
                    url:"../SalesOrderServlet",
                    data: "action=updateDiscount"
                        + "&add_disc=" + add_disc
                        + "&discountName=" + "updateSODiscount<%= request.getParameter("so_id")%>",
                    dataType: "json",
                    cache: false,
                    success: function(data){
                        if(data.type!='error'){                            
                            alertMsg(data);
                            $('#pdttt').datagrid('reload');   
                        }else{
                            $("#add_discount").val("0.00");
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
                        var content='<input id="add_discount" size="6" onChange="add_discount_change()" onfocus="add_discount_load()"  value="'+value+'" name="add_discount"/>%';
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
                if(value == null){
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
            
            function updateActions(){  
                var rowcount = $('#pdttt').datagrid('getRows').length;  
                for(var i=0; i<rowcount; i++){ 
                    $('#pdttt').datagrid('updateRow',{  
                        index:i,  
                        row:{actions:''}  
                    });  
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
                            url: "../SalesOrderServlet",
                            data: "action=removeProductItem"
                                + "&listIndex=" + rowIndex
                                + "&listName=" + "updateSOLineItemList<%= request.getParameter("so_id")%>",
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
                $('#findClient').panel('expand');
                $('#updateSO').panel('expand');
                $('#pdtDetails').panel('expand');
            }
            
            function reload(){
                window.parent.$.messager.confirm("Confirm", 'Reset the form?', function(r){
                    if (r){
                        $('#product_id').val('');
                        $('#pdt_qty').val('');
                        getSODetails();
                    }
                });
            }
            
            $(function(){
                $('#ff').form({
                    url:'../SalesOrderServlet?action=updateSO&listName=updateSOLineItemList<%= request.getParameter("so_id")%>&discountName=updateSODiscount<%= request.getParameter("so_id")%>',
                    onSubmit:function(){   
                        return $(this).form('validate');
                    },
                    success:function(data){
                        var obj = jQuery.parseJSON(data); 
                        if((obj.type!='error')){
                            reloadAll();
                        }
                        alertMsg(obj);
                    }
                });
            });
        </script>    
    </head>
    <body>
        <div style="padding:3px 2px;border-bottom:1px solid #ccc"><h2>Update Sales Order</h2></div>  
        <br/>
        <form id="ff" method="post">
            <input type="hidden" name="so_id" id="so_id" value="<%= request.getParameter("so_id")%>" />
            <div id="findClient" name="findClient" class="easyui-panel" title="Client Details:" style="padding:0px;background:#fafafa;" collapsible="true" >
                <table class="tableForms"> 
                    <tr>  
                        <td class="tableForms_label">Client ID:</td> 
                        <td class="tableForms_userInput">
                            <label id="inquirer_id" name="inquirer_id"></label>
                        </td>
                    </tr>
                    <tbody id="client_details" name="client_details">
                        <tr>
                            <td class="tableForms_label">Company:</td>  
                            <td class="tableForms_userInput">
                                <label id="company_name" name="company_name"></label>
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
                            <td class="tableForms_label">Client Type:</td>  
                            <td class="tableForms_userInput">
                                <label id="cust_type" name="cust_type"></label>
                            </td>  
                        </tr>
                    </tbody>
                </table>  
            </div>
            <br/>
            <div id="updateSO" name="updateSO" class="easyui-panel" title="Sales Order Details:" style="padding:0px;background:#fafafa;" collapsible="true"> 
                <table class="tableForms"> 
                    <tr>
                        <td class="tableForms_label">Destination:</td>  
                        <td class="tableForms_userInput">
                            <textarea id="destination" name="destination"></textarea>
                        </td>  
                    </tr>
                </table>  
            </div>
            <br/>
            <div id="pdtDetails" name="pdtDetails" class="easyui-panel" title="Product Details:" style="padding:0px;background:#fafafa;" collapsible="true" >
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
                           url="../SalesOrderServlet?action=getProductList&listName=updateSOLineItemList<%= request.getParameter("so_id")%>&discountName=updateSODiscount<%= request.getParameter("so_id")%>"
                           fitColumns="true"
                           rownumbers="true"
                           showfooter="true">  
                        <thead>  
                            <tr> 
                                <th field="product" width="20%" formatter="formatProduct">Product Name</th>
                                <th field="base_price" width="15%" formatter="formatBasePrice">Unit Price</th>
                                <th field="quantity" editor="{
                                    type:'numberbox',  
                                    options:{  
                                    min:1,  
                                    precision:0,    
                                    required:true }
                                    }" width="15%">Quantity</th>
                                <th field="bulk_discount" formatter="formatBulkDiscount" width ="15%">Bulk Discount</th>
                                <th field="actual_price" width="15%" formatter="formatTotal">Total</th>
                                <th field="actions" width="20%" formatter="formatActions"></th>
                            </tr>  
                        </thead>
                    </table>
                </div>
            </div>
            <br/>
            <div class="form_buttons">
                <input type="submit" value="Update" onclick="onClickConfirm();"/>
                <input type="button" onclick="reload()" value="Reset" />
            </div> 
        </form>
    </body>
</html>
