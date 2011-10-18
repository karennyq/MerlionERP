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
        <title>Sales Quotation Mgmt (ALL)</title>
        <%@include file = "../global/global_include.jsp" %>
        <script type="text/javascript">      
            function formatActionUpdate(value,rowData,rowIndex){ 
                return '<input type="button" onclick="goUpdate('+rowData.pre_sale_doc_id+');" value="Update"/>'; 
            }
            
            function goUpdate(pre_sale_doc_id){  
                window.parent.addTab('update_sales_quotation_'+pre_sale_doc_id,'Update Sales Quotation '+pre_sale_doc_id,'<%=GVCRM.UPDATE_SALES_QUOTATION%>?pre_sale_doc_id='+pre_sale_doc_id);
            }
            
            $(function() {
                $("#exp_date_1").datepicker({
                    dateFormat: 'dd/mm/yy'
                });            
            });
            
            $(function() {
                $("#exp_date_2").datepicker({
                    dateFormat: 'dd/mm/yy'
                });            
            });
            
            //filtering the table
            function filterTable(){
                var quotation_id=$('#quotation_id').val();
                var inquirer_id=$('#client_id').val();
                var company_name = $('#company_name').val();
                var exp_date_1 = $('#exp_date_1').val();
                var exp_date_2 = $('#exp_date_2').val();
                var f_url='../SalesQuotationServlet?action=loadPage&content=tableALL&quotation_id='+quotation_id+'&inquirer_id='+inquirer_id+'&company_name='+company_name+'&exp_date_1='+exp_date_1+'&exp_date_2='+exp_date_2+'';
                $('#tt').datagrid({  
                    url:f_url });
                $('#tt').datagrid('load');      
            }
            
            function reset(){
                $('#quotation_id').val("");
                $('#client_id').val("");
                $('#company_name').val("");
                $('#exp_date_1').val("");
                $('#exp_date_2').val("");
                filterTable();  
            }
            
            function formatCompanyName(value,rowData,rowIndex){
                try{
                    return (rowData.inquirer.company_name==null)?"":rowData.inquirer.company_name; 
                }catch(err){
                }
            }
            
            function formatInquirerId(value,rowData,rowIndex){
                try{
                    return (value.inquirer_id==null)?"":value.inquirer_id; 
                }catch(err){
                }
            }
        </script>
    </head>
    <body>
        <div style="padding:3px 2px;border-bottom:1px solid #ccc"><h2>Sales Quotation Management</h2></div> 
        <br/>
        <div id="tb" style="padding:5px;height:auto">  
            <div>  
                <table cellpadding="5">
                    <tr>
                        <td>Sales Quotation ID:</td>
                        <td>
                            <input  name="quotation_id" id="quotation_id" type="text"/>
                        </td>
                        <td>Client ID:</td>
                        <td>
                            <input  name="client_id" id="client_id" type="text"/>
                        </td>
                        <td>Company Name:</td>
                        <td>
                            <input  name="company_name" id="company_name" type="text"/>
                        </td>
                    </tr>
                    <tr>
                        <td>Expired Date Range:</td>
                        <td colspan="3">
                            <input name="exp_date_1" id="exp_date_1" type="text" readonly="readonly" size="10"/>
                            &emsp;to
                            &emsp;<input name="exp_date_2" id="exp_date_2" type="text" readonly="readonly" size="10"/> 
                        </td>
                        <td colspan="2" align="right">
                            <a href="#" class="easyui-linkbutton" onclick="filterTable()" iconCls="icon-search"></a>
                            <a href="#" class="easyui-linkbutton" onclick="reset()">Reset</a> 
                        </td>
                    </tr>
                </table>
            </div>  
        </div>
        <div fit="true" style="width:100%; height:500px">
            <table id="tt" class="easyui-datagrid" fit="true" style="width:100%;"  
                   url="../SalesQuotationServlet?action=loadPage&content=tableALL"   
                   toolbar="#tb"  
                   singleSelect="true" fitColumns="true" pagination="true"
                   rownumbers="false">  
                <thead>  
                    <tr> 
                        <th field="pre_sale_doc_id" width="25%" sortable="true">Quotation ID</th>  
                        <th field="inquirer" width="25%" formatter="formatInquirerId" sortable="true">Client ID</th>
                        <th field="company_name" width="25%" formatter="formatCompanyName" sortable="true">Company Name</th>
                        <th field="expiry_date" width="25%" sortable="true">Expired Date</th>
                    </tr>  
                </thead>  
            </table> 
        </div>
    </body>
</html>
