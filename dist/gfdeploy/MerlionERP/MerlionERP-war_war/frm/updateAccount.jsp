<%-- 
    Document   : updateAccount
    Created on : Sep 14, 2011, 10:16:31 PM
    Author     : alyssia
--%>
<%@page import="org.persistence.Account"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <%@include file = "../global/global_include.jsp" %>
        <title>Update Account</title>
        <script type="text/javascript">  
            function confirmMsg(formName){
                window.parent.$.messager.confirm("Update Account", "Proceed with action?", function(r){
                    if(r==true){
                        $(formName).submit();
                        $(formName).panel('expand');
                    }
                });
            }
            
            $(function(){
                $('#increaseCredit').form({                    
                    url:'../AccountServlet?action=updateAccount&confirm=increaseCredit',
                    onSubmit:function(){
                        return $(this).form('validate');
                    },
                    success:function(data){
                        var obj = jQuery.parseJSON(data); 
                        if(obj.type!='error'){
                            $('#increaseCredit').form('clear');
                            getAccountInfo();
                            reloadAll();
                        }
                        alertMsg(obj);
                    }
                });
                
                $('#decreaseCredit').form({
                    url:'../AccountServlet?action=updateAccount&confirm=decreaseCredit',
                    onSubmit:function(){
                        return $(this).form('validate');
                    },
                    success:function(data){
                        var obj = jQuery.parseJSON(data); 
                        if(obj.type!='error'){
                            getAccountInfo();
                            $('#decreaseCredit').form('clear'); 
                            reloadAll();
                        }
                        alertMsg(obj);       
                    }
                });
                               
                $('#addDeposit').form({
                    url:'../AccountServlet?action=updateAccount&confirm=addDeposit',
                    onSubmit:function(){
                        return $(this).form('validate');
                    },
                    success:function(data){
                        var obj = jQuery.parseJSON(data); 
                        if(obj.type!='error'){
                            getAccountInfo();
                            $('#addDeposit').form('clear'); 
                            reloadAll();
                        }
                        alertMsg(obj);
                    }
                });
                
                $('#transferDeposit').form({
                    url:'../AccountServlet?action=updateAccount&confirm=transferDeposit',
                    onSubmit:function(){
                        return $(this).form('validate');
                    },
                    success:function(data){
                        var obj = jQuery.parseJSON(data); 
                        if(obj.type!='error'){
                            getAccountInfo();
                            $('#transferDeposit').form('clear'); 
                            reloadAll();
                        }
                        alertMsg(obj);
                    }
                });
               
                $('#deductRefund').form({
                    url:'../AccountServlet?action=updateAccount&confirm=deductRefund',
                    onSubmit:function(){
                        return $(this).form('validate');
                    },
                    success:function(data){
                        var obj = jQuery.parseJSON(data); 
                        if(obj.type!='error'){
                            getAccountInfo();
                            $('#deductRefund').form('clear'); 
                            reloadAll();
                        }
                        alertMsg(obj);
                    }
                });
            })
                
            function getAccountInfo(){
                $.ajax({
                    type: "POST",
                    url: "../AccountServlet",
                    data: "action=loadPage&content=accountDetails&account_id="+$('#account_id').val(),  
                    dataType: "json",
                    cache: false,
                    success: function(data){
                        $('#account_id').val(data.account_id);
                        $('#account_id1').val(data.account_id);
                        $('#account_id2').val(data.account_id);
                        $('#account_id3').val(data.account_id);
                        $('#account_id4').val(data.account_id);
                        $('#account_id5').val(data.account_id);

                        if (data.accountStatus == '<%=Account.AccountStatus.New%>'){
                            $('#max_credit_limit').html('[Not Yet Accessed]');
                            $('#updateDeposit').panel('close');
                            $('#updateRefund').panel('close');
                            $('#decreaseCreditTab').hide();
                        }else{
                            $('#max_credit_limit').html(data.max_credit_limit);
                            $('#deposit_amt').html(data.deposit_amt);
                            $('#refundable_amt').html(data.refundable_amt);
                            $('#updateDeposit').panel('open');
                            $('#updateRefund').panel('open');
                            $('#decreaseCreditTab').show();
                        }
                    }
                });
            } 
            
            function getCustomerInfo(){
                var inquirer_id= $('#inquirer_id').val();
                $.ajax({
                    type: "POST",
                    url: "../AccountServlet",
                    data: "action=loadPage&content=custDetails&inquirer_id="+inquirer_id,  
                    dataType: "json",
                    cache: false,
                    success: function(data){
                        $('#inquirer_id').val(data.inquirer_id);
                        $('#contact_person').html(data.contact_person);
                        $('#company_add').html(data.company_add);
                        $('#company_name').html(data.company_name);
                        $('#contact_no').html(data.contact_no);
                    }
                });
            } 
            
            function openDd(){
                $('#tt').datagrid('reload');
                $('#dd').dialog('open');
            }
            
            $(document).ready(function(){
                getCustomerInfo();
                if($('#account_id').val()!=null||$('#account_id').val()==""){
                    getAccountInfo();
                }

                $('input:text').numberbox({  
                    precision:2  
                });
            });
            
            // append dropdownlist with Customer's sales order ID'
            $(document).ready(function(){
                $(function(){
                    $('#dd').dialog({modal:true});
                    $('#dd').dialog('close');
                });
                
                $('#tt').datagrid({  
                    onClickRow:function(rowIndex,rowData){  
                        $('#so_id').val(rowData.so_id);
                        $('#so_id_tx').html(rowData.so_id);
                        $('#deposit_requested_hidden').html(rowData.deposit_requested);
                        $('#dd').dialog('close');
                    }   
                }); 
            });
        </script>    
    </head>
    <body>
        <input type="hidden" id="inquirer_id" name="inquirer_id" value="<%=request.getParameter("inquirer_id")%>" />
        <input type="hidden" id="account_id" name="account_id" value="<%=request.getParameter("account_id")%>" />
        <input type="hidden" id="deposit_requested_hidden" name ="deposit_requested_hidden"/>
        <div style="padding:3px 2px;border-bottom:1px solid #ccc"><h2>Update Account</h2></div>
        <br/>
        <div id="salesLeadD" class="easyui-panel" title="Customer Details:" style="padding:0px;background:#fafafa;" collapsible="true" > 
            <table class="tableForms">
                <tr>  
                    <td class="tableForms_label">Customer ID: </td> 
                    <td class="tableForms_userInput" >
                        <%=request.getParameter("inquirer_id")%>
                    </td>
                </tr>
                <tr>
                    <td class="tableForms_label">Customer Name: </td>
                    <td class="tableForms_userInput" id ="contact_person" name="contact_person"></td>
                </tr>
                <tr>
                    <td class="tableForms_label">Company Name </td>
                    <td class="tableForms_userInput" id ="company_name" name="company_name"></td>
                </tr>
                <tr>
                    <td class="tableForms_label">Address: </td>
                    <td class="tableForms_userInput" id ="company_add" name="company_add"></td>
                </tr>
                <tr>
                    <td class="tableForms_label">Contact no: </td>
                    <td class="tableForms_userInput" id ="contact_no" name="contact_no"></td>
                </tr>
            </table>
        </div>
        <br/>
        <div id="acctD" class="easyui-panel" title="Account Details:" style="padding:0px;background:#fafafa;"  
             collapsible="true" > 
            <table class="tableForms">
                <tr>  
                    <td class="tableForms_label">Account ID: </td> 
                    <td class="tableForms_userInput" >
                        <%=request.getParameter("account_id")%>
                    </td>
                </tr>
            </table>
        </div>
        <br/>
        <div id="updateCredit" class="easyui-panel" title="Credit:" style="padding:0px;background:#fafafa;" collapsible="true" > 
            <table class="tableForms">
                <tr>  
                    <td class="tableForms_label">Maximum Credit Limit: </td> 
                    <td class="tableForms_userInput" id ="max_credit_limit" name="max_credit_limit"></td> 
                </tr>
                <tr>
                    <td class="tableForms_label">Increase Credit Limit: </td> 
                    <td class="tableForms_userInput">
                        <form id="increaseCredit" method="post">
                            <input id="account_id1" name="account_id1"  type="hidden" value="" />
                            <input class="easyui-numberbox" id="increase_credit_limit" name="increase_credit_limit" type="text" required="true"/>
                            <input type="button" name ="confirm" onClick="confirmMsg('#increaseCredit');" value="Confirm"/>
                        </form>
                    </td>
                </tr>
                <tr id="decreaseCreditTab">
                    <td class="tableForms_label">Decrease Credit Limit: </td> 
                    <td class="tableForms_userInput">
                        <form id="decreaseCredit" method="post">
                            <input id="account_id2" name="account_id2"  type="hidden" value="" />
                            <input class="easyui-numberbox" id="decrease_credit_limit" name="decrease_credit_limit" type="text" required="true"/>
                            <input type="button" name="confirm" onClick="confirmMsg('#decreaseCredit');" value="Confirm"/>
                        </form>
                    </td>
                </tr>
            </table>
        </div>   
        <br/>
        <div id="updateDeposit" class="easyui-panel" title="Deposit:" style="padding:0px;background:#fafafa;" collapsible="true" > 
            <table class="tableForms">
                <tr>  
                    <td class="tableForms_label">Current Amount: </td> 
                    <td class="tableForms_userInput" id ="deposit_amt" name="deposit_amt"></td> 
                </tr>
                <tr>
                    <td class="tableForms_label">Sales Order ID:</td> 
                    <td class="tableForms_userInput"><input id="inquirer_id" name="inquirer_id"  type="hidden" value=<%=request.getParameter("inquirer_id")%> />
                        <table>
                            <tr>
                                <td>
                                    <div id="so_id_tx" name="so_id_tx">
                                        <%=(request.getParameter("so_id") != null) ? request.getParameter("so_id") : ""%>
                                    </div>                                        
                                </td>
                                <td><a href="javascript:void(0);" class="easyui-linkbutton" iconCls="icon-search" onclick="openDd()"></a></td>
                            </tr>
                        </table>
                    </td>             
                </tr>
                <tr>
                    <td class="tableForms_label">Requested Deposit Amount: </td>
                    <td class="tableForms_userInput" id ="deposit_requested_hidden" name="deposit_requested_hidden"></td>
                </tr>
                <tr>
                    <td class="tableForms_label">Add Deposit: </td> 
                    <td class="tableForms_userInput">
                        <form id="addDeposit" method="post">
                            <input type="hidden" id="account_id3" name="account_id3" value="" />
                            <input type="hidden" id="so_id" name="so_id" value="<%=(request.getParameter("so_id") != null) ? request.getParameter("so_id") : ""%>"/>
                            <input class="easyui-numberbox" name="add_deposit" type="text" required="true"/>
                            <input type="button" name="confirm" onClick="confirmMsg('#addDeposit');" value="Confirm"/>
                        </form>
                    </td> 
                </tr>
                <tr>
                    <td class="tableForms_label">Transfer Deposit: </td> 
                    <td class="tableForms_userInput">
                        <form id="transferDeposit" method="post">
                            <input type="hidden" id="account_id5" name="account_id5" value="" />
                            <input class="easyui-numberbox" name="transfer_deposit" type="text" required="true"/>
                            <input type="button" name="confirm" onClick="confirmMsg('#transferDeposit');" value="Confirm"/>
                        </form>
                    </td> 
                </tr>
            </table>  
        </div>
        <br/>
        <div id="updateRefund" class="easyui-panel" title="Refund:" style="padding:0px;background:#fafafa;"  
             collapsible="true" > 
            <table class="tableForms">
                <tr>  
                    <td class="tableForms_label">Current Amount: </td> 
                    <td class="tableForms_userInput" id ="refundable_amt" name="refundable_amt"></td> 
                </tr> 
                <tr>      
                    <td class="tableForms_label">Deduct Refund: </td> 
                    <td class="tableForms_userInput">
                        <form id="deductRefund" method="post">
                            <input id="account_id4" name="account_id4" type="hidden" value="" />
                            <input class="easyui-numberbox" name="deduct_refund" type="text" required="true"/>
                            <input type="button" name ="confirm" onClick="confirmMsg('#deductRefund');" value="Confirm"/>
                        </form>
                    </td>
                </tr> 
            </table>    
        </div>
        <div id="tb" style="padding:5px;height:auto">  
            <div>
                <table cellpadding="5">
                    <tr>
                        <td>Sales Order ID:</td>
                        <td><input type="text"/></td>
                        <td><a href="#" class="easyui-linkbutton" iconCls="icon-search">Search</a></td>
                    </tr>
                </table>  
            </div>  
        </div>
        <div id="dd" title="Search Sales Order ID" style="width:600px; height:300px">
            <table id="tt" class="easyui-datagrid" fit="true" style="width:100%;"  
                   url="../AccountServlet?action=loadPage&content=getSalesOrderId&inquirer_id=<%=request.getParameter("inquirer_id")%>"
                   toolbar="#tb"  
                   singleSelect="true" fitColumns="true"
                   rownumbers="true">
                <thead>  
                    <tr> 
                        <th field="so_id" width="10px">Sales Order ID</th>
                    </tr>  
                </thead>  
            </table> 
        </div> 
    </body>
</html>