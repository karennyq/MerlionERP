<%@page import="org.persistence.SalesOrder"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="util.GVCRM"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Sales Order Mgmt (ALL)</title>
        <%@include file = "../global/global_include.jsp" %>
        <script type="text/javascript">
            $(document).ready(function(){
                $('#atpcheck').combobox({  
                    url:'../SalesOrderServlet?action=getATPCheckEnum',  
                    valueField:'id',  
                    textField:'text',
                    editable:false
                });
                
                $('#creditcheck').combobox({  
                    url:'../SalesOrderServlet?action=getCreditCheckEnum',  
                    valueField:'id',  
                    textField:'text',
                    editable:false
                }); 
                
                $('#status').combobox({  
                    url:'../SalesOrderServlet?action=getStatusEnum',  
                    valueField:'id',  
                    textField:'text',
                    editable:false
                }); 
            });
            
            //filtering the table
            function filterTable(){
                var po_id=$('#po_id').val();
                var so_id=$('#so_id').val();
                var atpCheck=$('#atpcheck').combobox('getValue');
                var status=$('#status').combobox('getValue');
                var creditCheck=$('#creditcheck').combobox('getValue');
                var f_url='../SalesOrderServlet?action=loadPage&content=tableALL&po_id='+po_id+'&so_id='+so_id+'&atpCheck='+atpCheck+'&status='+status+'&creditCheck='+creditCheck;
                $('#tt').datagrid({  
                    url:f_url });
                $('#tt').datagrid('reload');      
            }
            
            function reset(){
                $('#po_id').val("");
                $('#so_id').val("");
                $('#atpcheck').combobox('setValue',0);
                $('#status').combobox('setValue',0);
                $('#creditcheck').combobox('setValue',0);
                filterTable();
            }
            
            function formatPoRef(value, rowData, rowIndex){
                return value.po_reference_no;
            }
        </script>
    </head>
    <body>
        <div style="padding:3px 2px;border-bottom:1px solid #ccc"><h2>Sales Order Management (ALL)</h2></div> 
        <br/>
        <div id="tb" style="padding:5px;height:auto">  
            <!--<div style="margin-bottom:5px">  
                <a href="javascript:void(0)" onclick="javascript:window.parent.addTab('add_sales_order','Create Sales Order','<%=GVCRM.CREATE_SALES_ORDER%>');" class="easyui-linkbutton" iconCls="icon-add" plain="true">Create Sales Order</a>   
            </div>-->  
            <div>
                <table cellpadding="5">
                    <tr>
                        <td>SO ID:</td>
                        <td>
                            <input id="so_id" name="so_id" type="text"/>
                        </td>
                        <td>PO Ref. ID:</td>
                        <td>
                            <input id="po_id" name="po_id" type="text"/>
                        </td>
                        <td>ATP Check:</td>
                        <td>
                            <input id="atpcheck" name="atpcheck" panelHeight="auto" class="easyui-combobox"/>
                        </td>
                    </tr>
                    <tr>
                        <td>Credit Check:</td>
                        <td>
                            <input id="creditcheck" name="creditcheck" panelHeight="auto" class="easyui-combobox"/>
                        </td>
                        <td>Status:</td>
                        <td>
                            <input id="status" name="status" panelHeight="auto" class="easyui-combobox"/>
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
                   url="../SalesOrderServlet?action=loadPage&content=tableALL"   
                   toolbar="#tb"
                   singleSelect="true" fitColumns="true" pagination="true"
                   rownumbers="false">  
                <thead>  
                    <tr> 
                        <th field="so_id" width="10%" sortable="true">SO ID</th>
                        <th field="purchaseOrder" width="20%" formatter="formatPoRef" sortable="true">PO Ref. ID</th>
                        <th field="atpCheck" width="20%" sortable="true">ATP Check</th>
                        <th field="creditCheck" width="20%" sortable="true">Credit Check</th>
                        <th field="deposit_requested" width="20%" sortable="true">Deposit Req.</th>
                        <th field="status" width="10%" sortable="true">Status</th>
                    </tr>  
                </thead>  
            </table> 
        </div>
    </body>
</html>