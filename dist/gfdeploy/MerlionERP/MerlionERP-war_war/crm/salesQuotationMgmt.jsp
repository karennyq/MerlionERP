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
        <title>Sales Quotation Mgmt</title>
        <%@include file = "../global/global_include.jsp" %>
        <script type="text/javascript">      
            function formatActionUpdate(value,rowData,rowIndex){ 
                return '<input type="button" onclick="goUpdate('+rowData.pre_sale_doc_id+');" value="Update"/>'; 
            }
            
            function goUpdate(pre_sale_doc_id){  
                window.parent.addTab('update_sales_quotation_'+pre_sale_doc_id,'Update Sales Quotation: '+pre_sale_doc_id,'<%=GVCRM.UPDATE_SALES_QUOTATION%>?pre_sale_doc_id='+pre_sale_doc_id);
            }
            
            function formatPrice(value,rowData,rowIndex){ 
                return "S$"+value.toFixed(2);
            }
            
            function formatActionQuotationReport(value,rowData,rowIndex){ 
                var Content=   '<a herf="#" onclick="generateQuotationReport('+rowData.pre_sale_doc_id+',\'.pdf\');"><img src="../css/images/pdf.gif" width="16" height="16" alt="pdf"/></a>';       
                return Content;   
            }
            
            function formatActionPOReport(value,rowData,rowIndex){ 
                var Content=  '<a herf="#" onclick="generatePOReport('+rowData.pre_sale_doc_id+',\'.pdf\');" ><img src="../css/images/pdf.gif" width="16" height="16" alt="pdf"/></a>'; 
                var content2= '&nbsp;<a herf="#" onclick="generatePOReport('+rowData.pre_sale_doc_id+',\'.rtf\');" ><img src="../css/images/words.gif" width="16" height="16" alt="words"/></a>'; 
                return Content+content2;    
            }
            
            function generateQuotationReport(pre_sale_doc_id, fileType){  
                var win = $.messager.progress({
                    title:'Please Wait.',
                    msg:'Generating report...',
                    interval:'2000'
                });
                window.open("../ReportServlet?action=produceQuotationReport&pre_sale_doc_id="+pre_sale_doc_id+"&fileType="+fileType,"report");
                checkReport(pre_sale_doc_id);
            }
            
            function generatePOReport(pre_sale_doc_id,fileType){  
                var win = $.messager.progress({
                    title:'Please Wait.',
                    msg:'Generating report...',
                    interval:'2000'
                });
                window.open("../ReportServlet?action=producePOReport&pre_sale_doc_id="+pre_sale_doc_id+"&fileType="+fileType,"report");
                checkReport(pre_sale_doc_id);
            }
            
            function checkReport(pre_sale_doc_id) {
                $.ajax({
                    type: "POST",
                    url: "../LoginServlet",
                    data: "action=checkReport&pre_sale_doc_id="+pre_sale_doc_id,
                    dataType: "json",
                    cache: false,
                    success: function(data){
                        // window.close();
                        if(data.content=='done'){
                            $.messager.progress('bar').progressbar('setValue', 100);  
                            $.messager.progress('close');
                        }else{
                            var value =  $.messager.progress('bar').progressbar('getValue');  
                            if (value < 100){  
                                value += Math.floor(Math.random() * 5);  
                                $.messager.progress('bar').progressbar('setValue', value);  
                            }  
                            setTimeout("checkReport("+pre_sale_doc_id+")", 500);
                        }     
                    }
                });
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
                var f_url='../SalesQuotationServlet?action=loadTable&quotation_id='+quotation_id+'&inquirer_id='+inquirer_id+'&company_name='+company_name+'&exp_date_1='+exp_date_1+'&exp_date_2='+exp_date_2+'';
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
            <div style="margin-bottom:5px">  
                <a href="javascript:void(0)" onclick="javascript:window.parent.addTab('add_sales_quotation','Create Sales Quotation','<%=GVCRM.CREATE_SALES_QUOTATION%>?tabTitle=Create+Sales+Quotation');" class="easyui-linkbutton" iconCls="icon-add" plain="true">Create Sales Quotation</a>
            </div>  
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
                   url="../SalesQuotationServlet?action=loadTable" 
                   toolbar="#tb"
                   singleSelect="true" fitColumns="true" pagination="true"
                   rownumbers="false">  
                <thead>  
                    <tr> 
                        <th field="pre_sale_doc_id" width="10%" sortable="true">Quotation ID</th>  
                        <th field="inquirer" width="10%" formatter="formatInquirerId" sortable="true">Client ID</th>
                        <th field="company_name" width="20%" formatter="formatCompanyName" sortable="true">Company Name</th>
                        <th field="expiry_date" width="20%" sortable="true">Expired Date</th> 
                        <th field="discounted_total" width="10%" sortable="true" formatter="formatPrice">Disc. Price</th> 
                        <th field="actionQuotationReport" align="center" width="10%" formatter="formatActionQuotationReport">Quotation</th>
                        <th field="actionPOReport" align="center" width="10%" formatter="formatActionPOReport">PO Template</th>
                        <th field="actionUpdate" align="center" width="10%" formatter="formatActionUpdate"></th>
                    </tr>  
                </thead>  
            </table> 
        </div>
        <iframe src="" height="0" width="0" name="report"></iframe>
    </body>
</html>
