<%@page import="util.GVCRM"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Customer Mgmt</title>
        <%@include file = "../global/global_include.jsp" %>
        <script type="text/javascript"> 
            function getView(){
                var row = $('#tt').datagrid('getSelected');
                if (row){
                }else{
                    window.parent.$.messager.alert("View Customer Detail","Please select a customer's record to update.","info");
                }
            }
            
            function goUpdate(inquirer_id){  
                window.parent.addTab('update_customer'+inquirer_id,'Update Customer: '+inquirer_id,'<%=GVCRM.UPDATE_CUSTOMER%>?inquirer_id='+inquirer_id);
            }

            //on link
            function formatAction(value,rowData,rowIndex){  
                return '<input type="button" onclick="goUpdate('+rowData.inquirer_id+');" value="Update"/>';    
            }  
            
             function reset(){
                $('#inquirer_id').val("");
                $('#company_name').val("");
                filterTable();
            }
            
            function filterTable(){
                var salesLeadID=$('#inquirer_id').val();
                var compName=$('#company_name').val();
                var f_url='../CustomerServlet?action=loadPage&content=table&inquirer_id='+salesLeadID+'&company_name='+compName;
                $('#tt').datagrid({url:f_url});  
                $('#tt').datagrid('load');
            }
            
        </script>
    </head>
    <body>
        <div style="padding:3px 2px;border-bottom:1px solid #ccc"><h2>Customer Management</h2></div> 
        <br/>
        <div id="tb" style="padding:5px;height:auto">  
            <div style="margin-bottom:5px">  
                <a href="javascript:void(0)" onclick="javascript:window.parent.addTab('add_customer','Create Customer','<%=GVCRM.CREATE_CUSTOMER%>?tabTitle=Create+Customer');" class="easyui-linkbutton" iconCls="icon-add" plain="true">Create Customer</a>     
            </div>  
            <div>
                <table cellpadding="5">
                    <tr>
                        <td>Customer ID:</td>
                        <td>
                            <input name="inquirer_id" id="inquirer_id" type="text"/>
                        </td>
                        <td>Company Name:</td>
                        <td>
                            <input name="company_name" id="company_name" type="text"/>
                        </td>
                        <td>
                            <a href="#" class="easyui-linkbutton" onclick="filterTable()" iconCls="icon-search"></a>
                            <a href="#" class="easyui-linkbutton" onclick="reset()">Clear</a>
                        </td>
                    </tr>
                </table>  
            </div>  
        </div>  
        <div fit="true" style="width:100%; height:500px">
            <table id="tt" class="easyui-datagrid" fit="true" style="width:100%;"  
                   url="../CustomerServlet?action=loadPage&content=table"   
                   toolbar="#tb"  
                   singleSelect="true" fitColumns="true" pagination="true"
                   rownumbers="false">  
                <thead>  
                    <tr> 
                        <th field="inquirer_id" width="10%" sortable="true">ID</th>  
                        <th field="company_name" width="50%" sortable="true">Company Name</th>
                        <th field="convert_date" width="30%" sortable="true">Date & Time Created</th> 
                        <th field="action" width="10%" formatter="formatAction"></th>
                    </tr>  
                </thead>  
            </table> 
        </div>
    </body>
</html>
