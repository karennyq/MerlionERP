<%-- 
    Document   : transactionsMgmt
    Created on : Sep 14, 2011, 3:39:51 PM
    Author     : alyssia
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>View Transactions</title>
        <%@include file = "../global/global_include.jsp" %>
        <script type="text/javascript"> 
            function filterTable(){
                var acctID=$('#account_id').val();
                var trxID=$('#trans_id').val();
                var trxType=$('#trans_type').combobox('getValue');
                var trxNature=$('#transaction_nature').combobox('getValue');
                var f_url='../TransactionServlet?action=loadPage&content=table&account_id='+acctID+'&trans_id='+trxID+'&trans_type='+trxType+'&transaction_nature='+trxNature;
                $('#tt').datagrid({url:f_url});     
            }
           
            $(document).ready(function(){
                filterTable();
               
                $('#trans_type').combobox({  
                    url:'../TransactionServlet?action=loadPage&content=trxTypeDropdown',  
                    valueField:'id',  
                    textField:'text',
                    editable:false,
                    formatter:comboboxFomatter
                });
                
                $('#transaction_nature').combobox({  
                    url:'../TransactionServlet?action=loadPage&content=trxNatureDropdown',  
                    valueField:'id',  
                    textField:'text',
                    editable:false,
                    formatter:comboboxFomatter
                });
            });
            
            function formatAcctType(value,rowData,rowIndex){
                try{
                    return value+" Account";
                }catch(err){
                } 
            }
        </script>
    </head>
    <body>
        <input type="hidden" id="account_id" name="account_id" value="<%=request.getParameter("account_id")%>" />
        <div style="padding:3px 2px;border-bottom:1px solid #ccc"><h2>Transaction Management</h2></div> 
        <br/>
        <div id="tb" style="padding:5px;height:auto">  
            <div>
                <table cellpadding="5">
                    <tr>
                        <td>Transaction ID:</td>
                        <td>
                            <input  name="trans_id" id="trans_id" type="text"/>
                        </td>
                        <td>Account Type:</td>
                        <td>
                            <input id="trans_type" name="trans_type" panelHeight="auto" class="easyui-combobox"/>
                        </td>
                        <td>Transaction Nature:</td>
                        <td>
                            <input id="transaction_nature" name="transaction_nature" panelHeight="auto" class="easyui-combobox"/>
                        </td>
                        <td>
                            <a href="#" class="easyui-linkbutton" onclick="filterTable()" iconCls="icon-search"></a>
                        </td>
                    </tr>
                </table>
            </div>  
        </div>
        <div fit="true" style="width:100%; height:500px">
            <table id="tt" class="easyui-datagrid " fit="true" style="width:100%;"  
                   url="../TransactionServlet?action=loadPage&content=table"   
                   toolbar="#tb"  
                   singleSelect="true" fitColumns="true" pagination="true"
                   rownumbers="false">  
                <thead>  
                    <tr>
                        <th field="trans_id" width="20%">Transaction ID</th>  
                        <th field="amt_involved" width="20%">Amount Involved</th>
                        <th field="trans_date_time" width="20%">Date and Time</th>
                        <th field="trans_type" width="20%" sortable="true" formatter="formatAcctType">Account Type</th> 
                        <th field="transaction_nature" width="20%" sortable="true">Transaction Nature</th> 
                    </tr>  
                </thead>  
            </table> 
        </div>
    </body>
</html>
