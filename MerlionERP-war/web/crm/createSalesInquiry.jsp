<%-- 
    Document   : createSalesInquiry
    Created on : Sep 18, 2011, 10:23:58 PM
    Author     : karennyq
--%>

<%@page import="org.persistence.SalesLead"%>
<%@page import="org.persistence.LineItem"%>
<%@page import="java.util.ArrayList"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <% session.setAttribute("createInquiryLineItemList", new ArrayList());%>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <%@include file = "../global/global_include.jsp" %>
        <title>Create Sales Inquiry</title>
        <script type="text/javascript"> 
            $(document).ready(function(){
                $(function(){
                    $('#dd').dialog({modal:true});
                    $('#dd').dialog('close');
                });
                
                $('#tt').datagrid({  
                    onClickRow:function(rowIndex,rowData){  
                        $('#inquirer_id').val(rowData.inquirer_id);
                        $('#inquirer_id_hidden').val(rowData.inquirer_id);
                        getSalesLeadInfo();
                        $('#dd').dialog('close');
                    }   
                });
                
                $('#inquiry_source').combobox({  
                    url:'../SalesInquiryServlet?action=loadPage&content=inquirySourceDropdown',  
                    valueField:'text',  
                    textField:'text',
                    editable:false,
                    required:true,
                    formatter:comboboxFomatter
                });
                
                $('#priority').combobox({  
                    url:'../SalesInquiryServlet?action=loadPage&content=priorityDropdown',  
                    valueField:'text',  
                    textField:'text',
                    editable:false,
                    required:true,
                    formatter:comboboxFomatter
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
                            url: "../SalesInquiryServlet",
                            data: "action=updateProductItem"
                                + "&update_qty=" + row.quantity
                                + "&listIndex=" + index
                                + "&listName=" + "createInquiryLineItemList",
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
                    }
                });
            });
            
            function openDd(){
                $('#tt').datagrid('reload');
                $('#dd').dialog('open');         
            }
            
            function getSalesLeadInfo(){
                $.ajax({
                    type: "POST",
                    url: "../SalesLeadServlet",
                    data: "action=loadPage"
                        + "&content=" + "inquirerDetails"
                        + "&inquirer_id=" + $('#inquirer_id_hidden').val(),
                    dataType: "json",
                    cache: false,
                    success: function(data){
                        $('#inquirer_details').show();
                        $('#inquirer_id').html(data.inquirer_id);
                        $('#inquirer_id_hidden').val(data.inquirer_id);
                        $('#company_name').html(data.company_name);
                        $('#contact_person').html(data.contact_person);
                        $('#contact_no').html(data.contact_no);
                        $('#fax_no').html(data.fax_no);
                        $('#email').html(data.email);
                        var cust_type = data.cust_type.replace("_"," ");
                        if(data.convert_status == "<%=SalesLead.ConvertStatus.Not_Converted.name()%>"){
                            $('#cust_type').html("Sales Lead ("+cust_type+")");
                        }else{     
                            $('#cust_type').html("Customer ("+cust_type+")");
                        }
                    }
                });
            } 
             
            function addPdt(){
                var productId = $('#product_id').val();
                var pdtQty = $('#pdt_qty').val();
                $.ajax({
                    type: "POST",
                    url: "../SalesInquiryServlet",
                    data: "action=addProduct"
                        + "&product_id=" + productId
                        + "&pdt_qty=" + pdtQty
                        + "&listName=" + "createInquiryLineItemList",
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
            
            function custReset(formId,content){
                window.parent.$.messager.confirm("Confirm", content, function(r){
                    if (r){
                        $('#inquirer_details').hide();
                        $('#inquirer_id').html("");
                        $.ajax({
                            type: "POST",
                            url:"../SalesInquiryServlet",
                            data: "action=loadPage"
                                + "&content=" + "pdtTable"
                                + "&reset=" + "true"
                                + "&listName=" + "createInquiryLineItemList",
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
 
                        $('#'+formId).each(function(){
                            this.reset();
                        });
                    }
                });
            }
            
            function formatProduct(value,rowData,rowIndex){
                try {
                    return (value.product_name==null)?"":value.product_name; 
                } catch (err) {
                }
            }
            
            function formatTotal(value,rowData,rowIndex){
                try {
                    value = value.toFixed(2);
                    return "S$"+value; 
                } catch (err) {
                }
            }
            
            function formatBasePrice(value,rowData,rowIndex){
                try{
                    value = value.toFixed(2);
                    return "S$"+value;
                } catch(err){
                } 
            }
            
            //pdt table!!!!
            function txtBoxOption(){
                var option={
                    type:'numberbox',  
                    options:{  
                        min:1,  
                        precision:2,    
                        required:true }
                };
                return option;
            }
            
            function formatActions(value,row,index){ 
                if (row.quantity=="Grand Total:") {
                    return "";
                } else if (row.editing) {  
                    var s = '<input type="button" onclick="saverow('+index+')" value="Save"/> ';  
                    var c = '<input type="button" onclick="cancelrow('+index+')" value="Cancel"/>';  
                    return s+c;  
                } else {  
                    var e = '<input type="button" onclick="editrow('+index+')" value="Edit"/> ';  
                    var d = '<input type="button" onclick="deletePdtItem('+index+')" value="Delete"/>';  
                    return e+d;  
                }  
            }
            
            function formatBulkDiscount(value,row,rowIndex){
                try{
                    value = value.toFixed(2);
                    return value+"%";
                }catch(err){
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
                            url: "../SalesInquiryServlet",
                            data: "action=removeProductItem"
                                + "&listIndex=" + rowIndex
                                + "&listName=" + "createInquiryLineItemList",
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
                $('#findInquirer').panel('expand');
                $('#createInquiry').panel('expand');
                $('#pdtDetails').panel('expand');
            }
            
            $(function(){
                $('#ff').form({
                    url:'../SalesInquiryServlet?action=createInquiry&listName=createInquiryLineItemList',
                    onSubmit:function(){   
                        return $(this).form('validate');
                    },
                    success:function(data){
                        var obj = jQuery.parseJSON(data); 
                        if((obj.type!='error')){
                            $('#ff').form('clear'); 
                            $('#inquirer_details').hide();
                            $('#inquirer_id').html("");
                            $('#pdttt').datagrid('reload');
                            try {
                                reloadAll();
                                alertMsg(obj);
                                closeTab("");
                            } catch(err){  
                            }        
                        }
                        alertMsg(obj);
                    }
                });
            });
        </script>    
    </head>
    <body>
        <div style="padding:3px 2px;border-bottom:1px solid #ccc"><h2>Create Sales Inquiry</h2></div>  
        <br/>
        <form id="ff" method="post">
            <input type="hidden" id="inquirer_id_hidden" name="inquirer_id_hidden"/>
            <div id="findInquirer" name="findInquirer" class="easyui-panel" title="Inquirer Details:" style="padding:0px;background:#fafafa;" collapsible="true" > 
                <table class="tableForms"> 
                    <tr>  
                        <td class="tableForms_label">Inquirer ID:</td> 
                        <td class="tableForms_userInput">
                            <table>
                                <tr>
                                    <td>
                                        <label id="inquirer_id" name="inquirer_id"></label>
                                    </td>
                                    <td><a href="javascript:void(0);" class="easyui-linkbutton" iconCls="icon-search" onclick="openDd()"></a></td>
                                </tr>
                            </table>
                        </td>
                    </tr>
                    <tbody id="inquirer_details" name="inquirer_details" hidden="true">
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
                            <td class="tableForms_label">Inquirer Type:</td>  
                            <td class="tableForms_userInput">
                                <label id="cust_type" name="cust_type"></label>
                            </td>  
                        </tr>
                    </tbody>
                </table>  
            </div>
            <br/>
            <div id="createInquiry" name="createInquiry" class="easyui-panel" title="Inquiry Details:" style="padding:0px;background:#fafafa;" collapsible="true" >
                <table class="tableForms"> 
                    <tr>
                        <td class="tableForms_label">Inquiry Source:</td>  
                        <td class="tableForms_userInput">
                            <input id="inquiry_source" name="inquiry_source" panelHeight="auto" class="easyui-combobox"/>
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
                                <td>Quantity (Boxes):</td>
                                <td><input class="easyui-numberbox" id="pdt_qty" name="pdt_qty" type="text"/></td>
                                <td><a href="javascript:void(0);" id="addPdtBt" class="easyui-linkbutton" onclick="addPdt()">Add</a></td>
                            </tr>
                        </table>
                    </div>
                </div>
                <div id="pdt-add-tt" name="pdt-add-tt" fit=true style="height:250px;">
                    <table id="pdttt" name="pdttt" class="easyui-datagrid" fit="true" style="width:100%; height:100px;" 
                           url="../SalesInquiryServlet?action=loadPage&content=pdtTable&listName=createInquiryLineItemList"
                           idField="lineItem_id"
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
                <input type="submit" value="Create" onclick="onClickConfirm();"/>
                <input type="button" onclick="custReset('ff','Clear the form?')" value="Clear" />
            </div> 
        </form>
        <div id="tb" style="padding:5px;height:auto">  
            <div>
                <table cellpadding="5">
                    <tr>
                        <td>Inquirer ID:</td>
                        <td><input type="text"/></td>
                        <td><a href="#" class="easyui-linkbutton" iconCls="icon-search">Search</a></td>
                    </tr>
                </table>
            </div>  
        </div>
        <div id="dd" title="Search Inquirer" style="width:600px; height:300px">
            <table id="tt" class="easyui-datagrid" fit="true" style="width:100%;"  
                   url="../SalesLeadServlet?action=loadPage&content=inquirerDialog"   
                   toolbar="#tb"  
                   singleSelect="true"
                   fitColumns="true"
                   rownumbers="true">  
                <thead>  
                    <tr> 
                        <th field="inquirer_id" width="20%">Inquirer ID</th>  
                        <th field="company_name" width="20%">Company Name</th>
                        <th field="contact_person" width="20%">Contact Person</th>
                        <th field="country" width="20%">Country</th>
                        <th field="city" width="20%">City</th>
                    </tr>  
                </thead>  
            </table> 
        </div>
    </body>
</html>
