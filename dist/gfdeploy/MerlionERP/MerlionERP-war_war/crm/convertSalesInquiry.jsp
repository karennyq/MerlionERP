<%-- 
    Document   : convertSalesInquiry
    Created on : Sep 30, 2011, 3:56:09 PM
    Author     : Randy
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
        <title>Update Sales Quotation</title>
        <script type="text/javascript"> 
            <% session.setAttribute("convertInquiryLineItemList", new ArrayList()); %>
            <% session.setAttribute("convertInquiryDiscount", "0.00"); %>

            $(document).ready(function(){
                $('#quotation_source').combobox({
                    url:'../SalesQuotationServlet?action=loadPage&content=inquirySourceDropdown',
                    valueField:'text',
                    textField:'text',
                    editable:false,
                    required:true,
                    formatter:comboboxFomatter              
                });
                
                $('#priority').combobox({  
                    url:'../SalesQuotationServlet?action=loadPage&content=priorityDropdown',  
                    valueField:'text',  
                    textField:'text',
                    editable:false,
                    required:true,
                    formatter:comboboxFomatter
                });
                
                $(function(){
                    if($('#pre_sale_doc_id').val()!=null||$('#pre_sale_doc_id').val()==""){
                        getSalesInquiryDetails();
                    }
                })
                
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
                    min:0,  
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
                            url: "../SalesQuotationServlet",
                            data: "action=updateProductItem"
                                + "&update_qty=" + row.quantity
                                + "&listIndex=" + index
                                + "&listName=" + "convertInquiryLineItemList",
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
                    }  });
            });
            
            function getSalesInquiryDetails(){
                $.ajax({
                    type: "POST",
                    url: "../SalesInquiryServlet",
                    data: "action=getSalesInquiryDetails"
                        + "&pre_sale_doc_id=" + $('#pre_sale_doc_id').val()
                        + "&reset=" + "true"
                        + "&listName=" + "convertInquiryLineItemList"
                        + "&discountName=" + "convertInquiryDiscount",
                    dataType: "json",
                    cache: false,
                    success: function(data){
                        $('#pre_sale_doc_id').val(data.pre_sale_doc_id);
                        $('#client_id_hidden').val(data.inquirer.inquirer_id);
                        $('#inquirer_id').html(data.inquirer.inquirer_id);
                        $('#company_name').html(data.inquirer.company_name);
                        $('#contact_person').html(data.inquirer.contact_person);
                        $('#contact_no').html(data.inquirer.contact_no);
                        $('#email').html(data.inquirer.email);
                        $('#fax_no').html(data.inquirer.fax_no);
                        if(data.inquirer.convert_status == "<%=SalesLead.ConvertStatus.Not_Converted.name()%>"){
                            $('#cust_type').html("Sales Lead");
                        }else{     
                            var cust_type = data.inquirer.cust_type.replace("_"," ");
                            $('#cust_type').html("Customer ("+cust_type+")");
                        }
                        $('#quotation_source').combobox('select', data.inquiry_source);
                        $('#priority').combobox('select', data.priority);
                        $('#remarks').val(data.remarks);
                        $('#pdttt').datagrid('reload');
                    }
                });
            } 
             
            function addPdt(){
                $.ajax({
                    type: "POST",
                    url: "../SalesQuotationServlet",
                    data: "action=addProduct"
                        + "&product_id=" + $('#product_id').val()
                        + "&pdt_qty=" + $('#pdt_qty').val()
                        + "&listName=" + "convertInquiryLineItemList",
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
                    url:"../SalesQuotationServlet",
                    data: "action=updateDiscount"
                        + "&add_disc=" + add_disc
                        + "&discountName=" + "convertInquiryDiscount",
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
                            url: "../SalesQuotationServlet",
                            data: "action=removeProductItem"
                                + "&listIndex=" + rowIndex
                                + "&listName=" + "convertInquiryLineItemList",
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
                $('#convertInquiry').panel('expand');
                $('#pdtDetails').panel('expand');
            }
            
            function reload(){
                window.parent.$.messager.confirm("Confirm", 'Reset the form?', function(r){
                    if (r){
                        getSalesInquiryDetails();
                    }
                });
            }
            
            $(function(){
                $('#ff').form({
                    url:'../SalesQuotationServlet?action=createQuotationFromInquiry&listName=convertInquiryLineItemList&discountName=convertInquiryDiscount',
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
        <div style="padding:3px 2px;border-bottom:1px solid #ccc"><h2>Create Sales Quotation from existing Sales Inquiry (ID: <%=request.getParameter("pre_sale_doc_id")%>)</h2></div>
        <br/>
        <form id="ff" method="post">
            <input type="hidden" name="pre_sale_doc_id" id="pre_sale_doc_id" value=<%= request.getParameter("pre_sale_doc_id")%> />
            <input type="hidden" name="client_id_hidden" id="client_id_hidden" value="" />
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
            <div id="convertInquiry" name="convertInquiry" class="easyui-panel" title="Inquiry Details:" style="padding:0px;background:#fafafa;" collapsible="true"> 
                <table class="tableForms"> 
                    <tr>
                        <td class="tableForms_label">Inquiry Source:</td>  
                        <td class="tableForms_userInput">
                            <input id="quotation_source" name="quotation_source" panelHeight="auto" class="easyui-combobox"/>
                        </td>
                    </tr> 
                    <tr>  
                        <td class="tableForms_label">Priority:</td>  
                        <td class="tableForms_userInput">
                            <input id="priority" name="priority" panelHeight="auto"/>
                        </td> 
                    </tr>
                    <tr>  
                        <td class="tableForms_label">Remarks:</td>  
                        <td class="tableForms_userInput"><textarea id="remarks" name="remarks"></textarea></td>  
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
                                <td>Quantity (boxes):</td>
                                <td><input class="easyui-numberbox" id="pdt_qty" name="pdt_qty" type="text"/></td>
                                <td><a href="javascript:void(0);" id="addPdtBt" class="easyui-linkbutton" onclick="addPdt()">Add</a></td>
                            </tr>
                        </table>
                    </div>
                </div>
                <div id="pdt-add-tt" name="pdt-add-tt" fit=true style="height:250px;">
                    <table id="pdttt" name="pdttt" class="easyui-datagrid" fit="true" style="width:100%; height:100px;" 
                           url="../SalesQuotationServlet?action=getProductList&listName=convertInquiryLineItemList&discountName=convertInquiryDiscount"
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
                <input type="submit" value="Convert" onclick="onClickConfirm();"/>
                <input type="button" onclick="reload()" value="Reset" />
            </div> 
        </form>
    </body>
</html>
