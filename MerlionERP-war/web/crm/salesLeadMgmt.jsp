<%@page import="util.GVCRM"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Sales Lead Mgmt</title>
        <%@include file = "../global/global_include.jsp" %>
        <script type="text/javascript"> 
            function goConvert(inquirer_id){
                window.parent.addTab('convert_customer'+inquirer_id,'Create Customer from Existing Sales Lead (ID: '+inquirer_id+')','<%=GVCRM.CONVERT_CUSTOMER%>?inquirer_id='+inquirer_id+'&tabTitle=Create+Customer+from+Existing+Sales+Lead+(ID:+'+inquirer_id+')');
            }
            
            function goUpdate(inquirer_id){   
                window.parent.addTab('update_customer'+inquirer_id,'Update Sales Lead: '+inquirer_id,'<%=GVCRM.UPDATE_SALES_LEAD%>?inquirer_id='+inquirer_id);
            }

            //on link
            function formatActionConvert(value,rowData,rowIndex){  
                if(rowData.convert_status=='Converted'){
                    return '<input type="button" disabled="disabled" value="Convert to Customer"/>';  
                }
                else if(rowData.convert_status=='Not_Converted'){
                    return '<input type="button" onclick="goConvert('+rowData.inquirer_id+');" value="Convert to Customer"/>'; 
                }
            }
            
            function formatActionUpdate(value,rowData,rowIndex){      
                if(rowData.convert_status=='Converted'){
                    return '<input type="button" disabled="disabled" onclick="goUpdate('+rowData.inquirer_id+');" value="Update"/>';      // return '<a href="javascript:void(0)" onclick="goConvert('+rowData.inquirer_id+');">Convert to Customer</a>';  
                }
                else if(rowData.convert_status=='Not_Converted'){
                    return '<input type="button" onclick="goUpdate('+rowData.inquirer_id+');" value="Update"/>';    
                }
            }
            
            function filterTable(){
                var salesLeadID=$('#inquirer_id').val();
                var compName=$('#company_name').val();
                var f_url='../SalesLeadServlet?action=loadPage&content=table&inquirer_id='+salesLeadID+'&company_name='+compName;
                $('#tt').datagrid({url:f_url});     
            }
            
            function reset(){
                $('#inquirer_id').val("");
                $('#company_name').val("");
                filterTable();
            }
            
        </script>
    </head>
    <body>
        <div style="padding:3px 2px;border-bottom:1px solid #ccc"><h2>Sales Leads Management</h2></div> 
        <br/>
        <div id="tb" style="padding:5px;height:auto">  
            <div style="margin-bottom:5px">  
                <a href="javascript:void(0)" onclick="javascript:window.parent.addTab('add_sales_lead','Create Sales Lead','<%=GVCRM.CREATE_SALES_LEAD%>?tabTitle=Create+Sales+Lead');" class="easyui-linkbutton" iconCls="icon-add" plain="true">Create Sales Lead</a>  
            </div>  
            <div>
                <table cellpadding="5">
                    <tr>
                        <td>Sales Lead ID:</td>
                        <td><input name="inquirer_id" id="inquirer_id" type="text"/></td>
                        <td>Company Name:</td>
                        <td><input name="company_name" id="company_name" type="text"/></td>
                        <td>
                            <a href="#" class="easyui-linkbutton" iconCls="icon-search" onClick="filterTable()"></a>
                            <a href="#" class="easyui-linkbutton" onclick="reset()">Clear</a>
                        </td>
                    </tr>
                </table>  
            </div>  
        </div>  
        <div fit="true" style="width:100%; height:500px">
            <table id="tt" class="easyui-datagrid" fit="true" style="width:100%;"  
                   url="../SalesLeadServlet?action=loadPage&content=table"   
                   toolbar="#tb"  
                   singleSelect="true" fitColumns="true" pagination="true"
                   rownumbers="false">  
                <thead>  
                    <tr> 
                        <th field="inquirer_id" width="3%" sortable="true">ID</th>  
                        <th field="company_name" width="25%" sortable="true">Company Name</th>
                        <th field="convert_status" width="20%" sortable="true">Conversion</th>
                        <th field="create_date_time" width="22%" sortable="true">Date & Time Created</th> 
                        <th field="actionConvert" width="20%" formatter="formatActionConvert"></th>
                        <th field="actionUpdate" width="10%" formatter="formatActionUpdate"></th>
                    </tr>  
                </thead>  
            </table> 
        </div>
    </body>
</html>
