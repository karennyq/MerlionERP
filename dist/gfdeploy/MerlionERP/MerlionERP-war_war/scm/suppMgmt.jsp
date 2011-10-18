<%-- 
    Document   : suppMgmt
    Created on : Oct 7, 2011, 12:06:26 PM
    Author     : alyssia
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="util.GVSCM"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Supplier Mgmt</title>
        <%@include file = "../global/global_include.jsp" %>
        <script type="text/javascript">
            function updateAction(value,rowData,rowIndex) {
                return '<input type="button" onclick="goUpdate('+rowData.supplier_id+');" value="Update"/>';  
            }
            
            function goUpdate(supplier_id){
                window.parent.addTab('update_supp'+supplier_id,'Update Supplier: '+supplier_id,'<%=GVSCM.UPDATE_SUPP%>?supplier_id='+supplier_id);
            }
            
            function filterTable(){
                var suppID=$('#supplier_id').val();
                var suppName=$('#supplier_name').val();
                var f_url='../SupplierServlet?action=loadPage&content=table&supplier_id='+suppID+'&supplier_name='+suppName;
                $('#tt').datagrid({url:f_url});     
            }
        </script>
    </head>
    <body>
        <div style="padding:3px 2px;border-bottom:1px solid #ccc"><h2>Supplier Management</h2></div>
        <br/>
        <div id="tb" style="padding:5px;height:auto">  
            <div style="margin-bottom:5px">  
                <a href="javascript:void(0)" onclick="javascript:window.parent.addTab('add_supp','Create Supplier','<%=GVSCM.CREATE_SUPP%>');" class="easyui-linkbutton" iconCls="icon-add" plain="true">Create Supplier</a>   
            </div>  
            <div>
                <table cellpadding="5">
                    <tr>
                        <td>Supplier Id:</td>
                        <td>
                            <input id="supplier_id" name="supplier_id" type="text"/>
                        </td>
                        <td>Supplier Name:</td>
                        <td>
                            <input id="supplier_name" name="supplier_name" type="text"/>
                        </td>
                        <td><a href="#" class="easyui-linkbutton" onclick="filterTable()" iconCls="icon-search"></a></td>
                    </tr>
                </table>
            </div>  
        </div>  
        <div fit="true" style="width:100%; height:500px">
            <table id="tt" class="easyui-datagrid" fit="true" style="width:100%;"  
                   url="../SupplierServlet?action=loadPage&content=table"   
                   toolbar="#tb"
                   singleSelect="true" fitColumns="true" pagination="true"
                   rownumbers="false">  
                <thead>  
                    <tr> 
                        <th field="supplier_id" width="20%">Supplier ID</th>  
                        <th field="supplier_name" width="30%">Supplier Name</th>
                        <th field="supplier_address" width="40%">Supplier Address</th>
                        <th field="update_action" width="20%" formatter="updateAction"></th>
                    </tr>  
                </thead>  
            </table> 
        </div>
    </body>
</html>
