<%@page import="util.GVCRM"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Sole Distribution Mgmt</title>
        <%@include file = "../global/global_include.jsp" %>
        <script type="text/javascript"> 
            function goUpdate(inquirer_id){ 
                window.parent.addTab('update_sole_distribution'+inquirer_id,'Manage Sole Distribution (Customer ID: '+inquirer_id+')','<%=GVCRM.UPDATE_SOLE_DISTRIBUTION%>?inquirer_id='+inquirer_id+'&tabTitle=Manage+Sole+Distribution+(CUSTOMER+ID:+'+inquirer_id+')');
            }

            //on link
            function formatAction(value,rowData,rowIndex){  
                return '<input type="button" onclick="goUpdate('+rowData.inquirer_id+');" value="Manage"/>';      
            }
            
            function formatSd(value,rowData,rowIndex){
                var content="";
                $.each(rowData.soleDistribution, function(i, item) {
                    content=content+item.region+"<br>"
                });
                return content;
            }            
            
            function filterTable(){
                var id=$('#f_id').val();
                var name=$('#f_name').val();
                var region=$('#f_region').val();
                var f_url='../SoleDistributionServlet?action=loadPage&content=table&f_id='+id+'&f_name='+name+'&f_region='+region;  
                $('#tt').datagrid({url:f_url});
                $('#tt').datagrid('load');
            }
            
            function reset(){
                $('#f_id').val("");
                $('#f_name').val("");
                $('#f_region').val("");
                filterTable();
            }
        </script>
    </head>
    <body>
        <div style="padding:3px 2px;border-bottom:1px solid #ccc"><h2>Sole Distribution Management</h2></div> 
        <br/>
        <div id="tb" style="padding:5px;height:auto">
            <div style="margin-bottom:5px">  
                <a href="javascript:void(0)" onclick="javascript:window.parent.addTab('add_distributor','Add Sole Distributor','<%=GVCRM.CREATE_SOLE_DISTRIBUTION%>?tabTitle=Add+Sole+Distributor');" class="easyui-linkbutton" iconCls="icon-add" plain="true">Add Sole Distributor</a>     
            </div> 
            <div>
                <table cellpadding="5">
                    <tr>
                        <td>Wholesaler ID:</td>
                        <td>
                            <input id="f_id" style="width:100px" type="text"/>
                        </td>
                        <td>Wholesaler Name:</td>
                        <td>
                            <input id="f_name" style="width:100px" type="text"/>
                        </td>
                        <td>Sole Distribution Region:</td>
                        <td>
                            <input id="f_region" style="width:100px" type="text"/>
                        </td>
                    </tr>
                    <tr>
                        <td> </td>
                        <td> </td>
                        <td> </td>
                        <td> </td>
                        <td> </td>
                        <td>
                            <a href="#" onclick="filterTable()" class="easyui-linkbutton" iconCls="icon-search"></a>
                            <a href="#" class="easyui-linkbutton" onclick="reset()">Clear</a>

                        </td>
                    </tr>
                </table>  
            </div>  
        </div>  
        <div fit="true" style="width:100%; height:500px">
            <table id="tt" class="easyui-datagrid" fit="true" style="width:100%;"  
                   url="../SoleDistributionServlet?action=loadPage&content=table"   
                   toolbar="#tb"  
                   singleSelect="true" fitColumns="true" pagination="true"
                   rownumbers="false">  
                <thead>  
                    <tr> 
                        <th field="inquirer_id" sortable="true" width="10%">ID</th>  
                        <th field="company_name" sortable="true" width="30%">Company Name</th>
                        <th field="convert_date" sortable="true" width="25%">Date & Time Created</th> 
                        <th field="soleDistribution" formatter="formatSd" width="25%">Regions</th>
                        <th field="action" width="10%" formatter="formatAction"></th>
                    </tr>  
                </thead>  
            </table> 
        </div>
    </body>
</html>
