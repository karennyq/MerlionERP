<%-- 
    Document   : account management 
    Created on : Sep 12, 2011, 5:24:43 PM
    Author     : alyssia
--%>

<%@page import="util.GVFRM"%>
<%@page import="org.persistence.Account"%>
<%@page import="java.io.PrintWriter"%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Account Mgmt</title>
        <%@include file = "../global/global_include.jsp" %>
        <script type="text/javascript"> 
            function getView(){
                var row = $('#tt').datagrid('getSelected');
                if (row){
                    
                }else{
                    window.parent.$.messager.alert("View Credit Detail","Please select a credit's record to update.","info");
                }
            }
            
            function goView(accountID,inquirerID){
                window.parent.addTab('view_transactions','View Transaction: (ID: '+inquirerID+ ')','<%=GVFRM.VIEW_TRX%>?account_id='+accountID+'&inquirer_id='+inquirerID);
            }
            
            function goUpdate(accountID, inquirerID){
                window.parent.addTab('update_account'+inquirerID,'Update Account: ' + inquirerID,'<%=GVFRM.UPDATE_ACCT%>?account_id='+accountID+'&inquirer_id='+inquirerID);
            }
            
            function formatAction(value,rowData,rowIndex){      
                return '<input type="button" onclick="goView('+rowData.account_id+','+rowData.customer.inquirer_id+');" value="View Transaction"/>';  
            }
            
            function formatAction2(value,rowData,rowIndex){      
                return '<input type="button" onclick="goUpdate('+rowData.account_id+','+rowData.customer.inquirer_id+');" value="Update"/>';  
            }
            
            function formatAction3(value,rowData,rowIndex){      
                return value.inquirer_id;
            }
            
            function viewAll(accountID){
                var custID=$('#inquirer_id').val();
                var f_url='../AccountServlet?action=loadPage&content=viewAll&inquirer_id='+custID; 
                $('#tt').datagrid({url:f_url});      
            }     
                                  
            function filterTable(){
                var custID=$('#inquirer_id').val();
                var accountStatus=$('#accountStatus').combobox('getValue');
                var f_url='../AccountServlet?action=loadPage&content=table&inquirer_id='+custID+'&accountStatus='+accountStatus;
                $('#tt').datagrid({url:f_url});     
            }
            
            <%-- Accountstatus dropdownlist --%>
            $(document).ready(function(){
                $('#accountStatus').combobox({  
                    url:'../AccountServlet?action=loadPage&content=acctStatusDropdown',  
                    valueField:'id',  
                    textField:'text',
                    editable:false,
                    formatter:comboboxFomatter
                });
            });                
        </script>
    </head>
    <body>
        <div style="padding:3px 2px;border-bottom:1px solid #ccc"><h2>Account Management</h2></div> 
        <br/>
        <div id="tb" style="padding:5px;height:auto">  
            <div>
                <table cellpadding="5">
                    <tr>
                        <td colspan="5">
                            <input type="button" name ="viewMaxCredit" onClick="viewAll('');" value="View All Exceeding Credit Limit"/>
                        </td>
                    </tr>
                    <tr>
                        <td>Customer ID:</td>
                        <td><input  name="inquirer_id" id="inquirer_id" type="text"/></td>
                        <td>Account Status:</td>
                        <td><input id="accountStatus" name="accountStatus" panelHeight="auto" class="easyui-combobox"/></td>
                        <td><a href="#" class="easyui-linkbutton" onclick="filterTable()" iconCls="icon-search"></a></td>
                    </tr>
                </table>
            </div>  
        </div>  
        <div fit="true" style="width:100%; height:500px">
            <table id="tt" class="easyui-datagrid" fit="true" style="width:100%;"  
                   url="../AccountServlet?action=loadPage&content=table"   
                   toolbar="#tb"  
                   singleSelect="true" fitColumns="true" pagination="true"
                   rownumbers="false">  
                <thead>  
                    <tr> 
                        <th field="customer" formatter="formatAction3" width="10%">Customer ID</th>  
                        <th field="credit_amt" width="10%">Credit</th>
                        <th field="max_credit_limit" width="15%">Max Credit Limit</th>
                        <th field="deposit_amt" width="15%">Deposited Amount</th> 
                        <th field="refundable_amt" width="15%">Refundable Amount</th> 
                        <th field="action" align="right" width="20%" formatter="formatAction"></th>  
                        <th field="action2" align="right" width="15%" formatter="formatAction2"></th>
                    </tr>
                </thead>  
            </table>
        </div>
    </body>
</html>
