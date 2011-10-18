<%-- 
    Document   : createPurchaseOrder
    Created on : Sep 25, 2011, 10:10:46 PM
    Author     : karennyq
--%>

<%@page import="util.GVCRM"%>
<%@page import="org.persistence.Customer"%>
<%@page import="org.persistence.SalesLead"%>
<%@page import="java.util.ArrayList"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>

<% session.setAttribute("createPurchaseOrderLineItemList", new ArrayList()); %>
<%    session.setAttribute("purchaseOrderDiscount", "0.00"); %>

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <%@include file = "../global/global_include.jsp" %>
        <title>Create Purchase Order</title>
        <script type="text/javascript">     
            $(document).ready(function(){
                $('#pdt_qty').numberbox({  
                    min:1,  
                    precision:0
                });
                
                $(function(){
                    $('#dd').dialog({modal:true});
                    $('#dd').dialog('close');
                    $('#poDetails').panel("close");
                    $('#pdtDetails').panel("close");
                    $('#buttons').hide();
                });
                
                $('#tt').datagrid({  
                    onClickRow:function(rowIndex,rowData){  
                        $('#quotation_id_hidden').val(rowData.pre_sale_doc_id);
                        getQuotationInfo();
                        $('#dd').dialog('close');
                    }   
                });
                
                $('#status').combobox({  
                    url:'../PurchaseOrderServlet?action=loadPage&content=statusDropdown',  
                    valueField:'id',  
                    textField:'text',
                    editable:false,
                    required:true,
                    formatter:comboboxFomatter
                });
                
                /*$('#payment_mode').combobox({  
                    url:'../PurchaseOrderServlet?action=loadPage&content=paymentModeDropdown',  
                    valueField:'text',  
                    textField:'text',
                    editable:false,
                    required:true
                });*/
                
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
                            data: "action=updateProductItem&update_qty="+row.quantity +"&listIndex="+index+"&lineItemListSession=createPurchaseOrderLineItemList&discountSession=purchaseOrderDiscount",
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
                    data: "action=getProducts&lineItemListSession=createPurchaseOrderLineItemList&discountSession=purchaseOrderDiscount",
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
                    data: "action=addProduct&product_id="+productId+"&pdt_qty="+pdtQty+"&lineItemListSession=createPurchaseOrderLineItemList&discountSession=purchaseOrderDiscount",
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
                $.ajax({
                    type: "POST",
                    url:"../PurchaseOrderServlet",
                    data: "action=updateDiscount"
                        + "&add_disc=" + $("#add_discount").val()
                        + "&lineItemListSession=" + "createPurchaseOrderLineItemList"
                        + "&discountSession=" + "purchaseOrderDiscount",
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
                            data: "action=removeProductItem&listIndex="+rowIndex+"&lineItemListSession=createPurchaseOrderLineItemList&discountSession=purchaseOrderDiscount",
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
                $('#findQuotation').panel('expand');
                $('#poDetails').panel('expand');
                $('#pdtDetails').panel('expand');
            }

            function reset(){
                $('#quotation_id').html("");
                $('#poDetails').panel("close");
                $('#pdtDetails').panel("close");
                $('#sent_date').val("");
                $('#customer_id').html("");
                $('#company_name').html(""); //change all these to val for textbox
                $('#company_address').html("");
                $('#contact_person').html("");
                $('#contact_no').html("");
                $('#fax_no').html("");
                $('#email').html("");
                $('#cust_type').html("");
                $('#cust_type_hidden').val("");
                $('#expiry_date').html("");
                $('#status').combobox('setValue',0);
                $('#remarks').val("");
                $('#shipping_address').val("");
                $('#buttons').hide();                
            }
            
            function custReset(formId,content){
                window.parent.$.messager.confirm("Confirm", content, function(r){
                    if (r){
                        getQuotationInfo();
                        $('#product_id').val("");
                        $('#pdt_qty').val("");
                        $('add_discount').val("");
                        $('#pdttt').datagrid('reload');
                        $('#po_ref_no').val("");
                        $('#sent_date').val("");
                        $('#payment_mode').combobox('setValue',0);
                        $('#remarks').val("");
                        $('#shipping_address').val("");
                        $('#buttons').show();
                    }
                });
            }
            
            function openDd(){
                $('#tt').datagrid('reload');
                $('#dd').dialog('open');         
            }
            
            function getQuotationInfo(){
                var quotationID= $('#quotation_id_hidden').val();
                $.ajax({
                    type: "POST",
                    url: "../PurchaseOrderServlet",
                    data: "action=loadPage"
                        + "&content=" + "quotationDetailsForPO"
                        + "&quotation_id=" +quotationID
                        + "&lineItemListSession=" + "createPurchaseOrderLineItemList"
                        + "&discountSession=" + "purchaseOrderDiscount",
                    dataType: "json",
                    cache: false,
                    success: function(data){               
                        if(data.type == 'error'){
                            window.parent.$.messager.confirm(data.title, data.content, function(r){
                                if (r){
                                    var str=data.content;
                                    var start = str.indexOf(" ");
                                    str = str.substr(start);
                                    start = str.indexOf(" ", 2);
                                    str = str.substr(start);
                                    start = str.indexOf(" ");
                                    var end = str.indexOf(" ", 1);
                                    var inquirer_id = str.substr(start, end);
                                    window.parent.addTab('convert_customer'+inquirer_id,'Create Customer from Existing Sales Lead (ID: '+inquirer_id+')','<%=GVCRM.CONVERT_CUSTOMER%>?tabTitle=Create+Customer+from+Existing+Sales+Lead+(ID:+'+inquirer_id+')&inquirer_id='+inquirer_id);
                                }
                            });
                            //alertMsg(data);
                            reset();
                        }else{
                            $('#quotation_id').html(quotationID);
                            $('#poDetails').panel("open");
                            $('#pdtDetails').panel("open");
                            $('#buttons').show();
                            $('#customer_id').html(data.inquirer.inquirer_id);
                            $('#company_name').html(data.inquirer.company_name); //change all these to val for textbox
                            $('#company_address').html(data.inquirer.company_add);
                            $('#contact_person').html(data.inquirer.contact_person);
                            $('#contact_no').html(data.inquirer.contact_no);
                            $('#fax_no').html(data.inquirer.fax_no);
                            $('#email').html(data.inquirer.email); 
                            
                            var cust_type = data.inquirer.cust_type;
                            if(cust_type == "<%=Customer.CustomerType.Direct_Sale.name()%>"){
                                $('#cust_type').html("Customer ("+"<%=Customer.CustomerType.Direct_Sale.name().replace('_', ' ')%>"+")");
                                $('#cust_type_hidden').val("<%=Customer.CustomerType.Direct_Sale.name()%>");
                            }else if(cust_type == "<%=Customer.CustomerType.Wholesale.name()%>"){
                                $('#cust_type').html("Customer ("+"<%=Customer.CustomerType.Wholesale.name()%>"+")"); 
                                $('#cust_type_hidden').val("<%=Customer.CustomerType.Wholesale.name()%>");    
                            }
                            
                            var exp_date = new Date(data.expiry_date);
                            exp_date.setHours(0, 0, 0, 0);
                            exp_date.setMonth(exp_date.getMonth(), exp_date.getDate()+1);
                            
                            var currentTime = new Date(); 
                            if(Date.parse(currentTime)<=Date.parse(exp_date)){    
                                $('#expiry_date').html(data.expiry_date);
                            }else if(Date.parse(currentTime)>Date.parse(exp_date)){
                                $('#expiry_date').html("<font color:'red'>"+data.expiry_date+"</font>");
                            }
                            
                            $('#pdttt').datagrid('reload');
                            $('#pdtDetails').panel('collapse');
                            $('#pdtDetails').panel('expand');
                        }
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
                    url:'../PurchaseOrderServlet?action=createPurchaseOrder&lineItemListSession=createPurchaseOrderLineItemList&discountSession=purchaseOrderDiscount',
                    onSubmit:function(){   
                        return $(this).form('validate');
                    },
                    success:function(data){
                        var obj = jQuery.parseJSON(data); 
                        if((obj.type!='error')){
                            $('#ff').form('clear'); 
                            reset();
                            $('#pdttt').datagrid('reload');
                            reloadAll();
                            alertMsg(obj);
                            closeTab("");
                        }
                        alertMsg(obj);
                    }
                });
            });
        </script> 
    </head>
    <body>
        <div style="padding:3px 2px;border-bottom:1px solid #ccc"><h2>Create Purchase Order</h2></div>  
        <br/>
        <form id="ff" method="post">
            <input type="hidden" id="quotation_id_hidden" name="quotation_id_hidden"/>
            <input type="hidden" id="cust_type_hidden" name="cust_type_hidden"/>
            <div id="findQuotation" name="findQuotation" class="easyui-panel" title="Select a Quotation:" style="padding:0px;background:#fafafa;" collapsible="true" > 
                <table class="tableForms"> 
                    <tr>  
                        <td class="tableForms_label">Quotation ID:</td> 
                        <td class="tableForms_userInput">
                            <table>
                                <tr>
                                    <td>
                                        <label id="quotation_id" name="quotation_id"></label>
                                    </td>
                                    <td><a href="javascript:void(0);" class="easyui-linkbutton" iconCls="icon-search" onclick="openDd()"></a></td>
                                </tr>
                            </table>
                        </td>
                    </tr>
                </table>
            </div>
            <br/>
            <div id="poDetails" name="poDetails" class="easyui-panel" title="Purchase Order Details:" style="padding:0px;background:#fafafa;" collapsible="true"> 
                <table class="tableForms"> 
                    <tr>
                        <td class="tableForms_label">Purchase Order No:</td>  
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
                        <td class="tableForms_label">Customer Type:</td>
                        <td class="tableForms_userInput">
                            <label id="cust_type" name="cust_type" ></label>
                        </td>  
                    </tr>
                    <tr>
                        <td class="tableForms_label">Quotation Expiry Date:</td>  
                        <td class="tableForms_userInput">
                            <label id="expiry_date" name="expiry_date"></label>
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
                        <td class="tableForms_label">PO status:</td>  
                        <td class="tableForms_userInput">
                            <input id="status" name="status" panelHeight="auto" class="easyui-combobox"/>
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
                           url="../PurchaseOrderServlet?action=loadPage&content=pdtTable&lineItemListSession=createPurchaseOrderLineItemList&discountSession=purchaseOrderDiscount"
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
                    <br/>
                </div>
            </div>
            <br/>
            <div id="buttons" name="buttons" class="form_buttons" hidden="true">
                <input type="submit" value="Create" onclick="onClickConfirm();"/>
                <input type="button" onclick="custReset('ff','Reset the form?')" value="Reset" />
            </div>
        </form>
        <div id="tb" style="padding:5px;height:auto">  
            <div>
                <table cellpadding="5">
                    <tr>
                        <td>Quotation ID:</td>
                        <td><input type="text"/></td>
                        <td><a href="#" class="easyui-linkbutton" iconCls="icon-search">Search</a></td>
                    </tr>
                </table> 
            </div>  
        </div>
        <div id="dd" title="Search Quotation" style="width:600px; height:300px">
            <table id="tt" class="easyui-datagrid" fit="true" style="width:100%;"  
                   url="../PurchaseOrderServlet?action=loadPage&content=quotationDialog&lineItemListSession=createPurchaseOrderLineItemList&discountSession=purchaseOrderDiscount"   
                   toolbar="#tb"  
                   singleSelect="true"
                   fitColumns="true"
                   rownumbers="true">  
                <thead>  
                    <tr> 
                        <th field="pre_sale_doc_id" width="20%">Quotation ID</th> 
                        <th field="inquirer" width="20%" formatter="formatClientID">Client ID</th> 
                        <th field="company_name" width="20%" formatter="formatCompanyName">Company Name</th>
                        <th field="contact_person" width="20%" formatter="formatContactPerson">Contact Person</th>
                        <th field="request_date" width="20%">Request Date</th>
                    </tr>  
                </thead>  
            </table> 
        </div>
    </body>
</html>
