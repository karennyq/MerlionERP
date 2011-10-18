<%-- 
    Document   : salesInquiryMgmt
    Created on : Sep 18, 2011, 1:10:26 PM
    Author     : karennyq
--%>

<%@page import="util.GVCRM"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Sales Inquiry Mgmt</title>
        <%@include file = "../global/global_include.jsp" %>
        <script type="text/javascript">     
            function formatActionUpdate(value,rowData,rowIndex){ 
                return '<input type="button" onclick="goUpdate('+rowData.pre_sale_doc_id+');" value="Update"/>'; 
            }
            
            function formatInquirerId(value,rowData,rowIndex){
                try{
                    return (value.inquirer_id==null)?"":value.inquirer_id; 
                }catch(err){
                }
            }
            
            function formatCompanyName(value,rowData,rowIndex){
                try{
                    return (rowData.inquirer.company_name==null)?"":rowData.inquirer.company_name; 
                }catch(err){
                }
            }
            
            function formatActionConvert(value,rowData,rowIndex){  
                return '<input type="button" onclick="goConvert('+rowData.pre_sale_doc_id+');" value="Convert to Quotation"/>'; 
            }
            
            function goConvert(pre_sale_doc_id){  
                window.parent.addTab('convert_sales_inquiry_'+pre_sale_doc_id,'Convert Sales Inquiry: '+pre_sale_doc_id,'<%=GVCRM.CONVERT_SALES_INQUIRY%>?pre_sale_doc_id='+pre_sale_doc_id);
            }
            
            function goUpdate(pre_sale_doc_id){  
                window.parent.addTab('update_sales_inquiry_'+pre_sale_doc_id,'Update Sales Inquiry: '+pre_sale_doc_id,'<%=GVCRM.UPDATE_SALES_INQUIRY%>?pre_sale_doc_id='+pre_sale_doc_id);
            }
            
            //filtering the table
            function filterTable(){
                var inquiry_id=$('#inquiry_id').val();
                var inquirer_id=$('#inquirer_id').val();
                var inquiry_priority=$('#inquiry_priority').combobox('getValue');
                var company_name = $('#company_name').val();
                var req_date_1 = $('#req_date_1').val();
                var req_date_2 = $('#req_date_2').val();
                var f_url='../SalesInquiryServlet?action=loadPage&content=table&inquiry_id='+inquiry_id+'&inquirer_id='+inquirer_id+'&inquiry_priority='+inquiry_priority+'&company_name='+company_name+'&req_date_1='+req_date_1+'&req_date_2='+req_date_2+'';
                $('#tt').datagrid({  
                    url:f_url });
                $('#tt').datagrid('load');      
            }
            
            function formatPrice(value,rowData,rowIndex){ 
                return "S$"+value.toFixed(2);
            }
            
            function reset(){
                $('#inquiry_id').val("");
                $('#inquirer_id').val("");
                $('#req_date_1').val("");
                $('#req_date_2').val("");
                $('#inquiry_priority').combobox('setValue',0);
                filterTable();
            }
            
            $(document).ready(function(){
                $('#inquiry_priority').combobox({  
                    url:'../SalesInquiryServlet?action=loadPage&content=priorityDropdown',  
                    valueField:'id',  
                    textField:'text',
                    editable:false
                });
            });
            
            $(function() {
                $("#req_date_1").datepicker({
                    dateFormat: 'dd/mm/yy'
                });            
            });
            
            $(function() {
                $("#req_date_2").datepicker({
                    dateFormat: 'dd/mm/yy'
                });            
            });
        </script>
    </head>
    <body>
        <div style="padding:3px 2px;border-bottom:1px solid #ccc"><h2>Sales Inquiry Management</h2></div> 
        <br/>
        <div id="tb" style="padding:5px;height:auto">  
            <div style="margin-bottom:5px">  
                <a href="javascript:void(0)" onclick="javascript:window.parent.addTab('add_sales_inquiry','Create Sales Inquiry','<%=GVCRM.CREATE_SALES_INQUIRY%>?tabTitle=Create+Sales+Inquiry');" class="easyui-linkbutton" iconCls="icon-add" plain="true">Create Sales Inquiry</a>
            </div>  
            <div>  
                <table cellpadding="5">
                    <tr>
                        <td>Sales Inquiry ID:</td>
                        <td>
                            <input name="inquiry_id" id="inquiry_id" type="text"/>
                        </td>
                        <td>Inquirer ID:</td>
                        <td>
                            <input name="inquirer_id" id="inquirer_id" type="text"/>
                        </td>
                        <td>Company Name:</td>
                        <td>
                            <input name="company_name" id="company_name" type="text"/>
                        </td>
                    </tr>
                    <tr>
                        <td>Priority:</td>
                        <td>  
                            <input id="inquiry_priority" name="inquiry_priority" panelHeight="auto" class="easyui-combobox"/>
                        </td>                        
                        <td>
                            Date Range:
                        </td>
                        <td colspan="2">
                            <input name="req_date_1" id="req_date_1" type="text" readonly="readonly" size="10"/>
                            &emsp;to
                            &emsp;<input name="req_date_2" id="req_date_2" type="text" readonly="readonly" size="10"/>
                        </td>
                        <td align="right">
                            <a href="#" class="easyui-linkbutton" onclick="filterTable()" iconCls="icon-search"></a>
                            <a href="#" class="easyui-linkbutton" onclick="reset()">Clear</a>
                        </td>
                    </tr>
                </table>
            </div>  
        </div>
        <div fit="true" style="width:100%; height:500px">
            <table id="tt" class="easyui-datagrid" fit="true" style="width:100%;"  
                   url="../SalesInquiryServlet?action=loadPage&content=table"   
                   toolbar="#tb"  
                   singleSelect="true" fitColumns="true" pagination="true"
                   rownumbers="false">  
                <thead>  
                    <tr> 
                        <th field="pre_sale_doc_id" width="5%" sortable="true">ID</th>  
                        <th field="inquirer" width="10%" formatter="formatInquirerId" sortable="true">Inquirer ID</th>
                        <th field="company_name" width="15%" formatter="formatCompanyName">Company Name</th>
                        <th field="priority" width="10%" sortable="true">Priority</th>
                        <th field="request_date" width="20%" sortable="true">Request Date</th>
                        <th field="total_price" width="10%" sortable="true" formatter="formatPrice">Total Price</th>  
                        <th field="actionConvert" width="20%" formatter="formatActionConvert"></th>
                        <th field="actionUpdate" align="right" width="10%" formatter="formatActionUpdate"></th>
                    </tr>  
                </thead>  
            </table> 
        </div>
    </body>
</html>
