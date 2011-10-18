<%-- 
    Document   : updateRM
    Created on : Oct 7, 2011, 1:48:07 AM
    Author     : alyssia
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <%@include file = "../global/global_include.jsp" %>
        <title>Update Raw Material</title>
        
        <script type="text/javascript">
            $(document).ready(function(){
                if($('#raw_material_id').val()!=null||$('#raw_material_id').val()==""){
                    getDetails();
                }
            });
            
            function getDetails(){
                var raw_material_id= $('#raw_material_id').val();
                $.ajax({
                    type: "POST",
                    url: "../RawMaterialServlet",
                    data: "action=getRMDetails&raw_material_id="+raw_material_id,
                    dataType: "json",
                    cache: false,
                    success: function(data){
                        $('#raw_material_id').val(data.raw_material_id);
                        $('#mat_name').val(data.mat_name);
                    }
                });
            }
            
            function onClickConfirm(){
                $('#updateRM').panel('expand');
            }
            
            function rmReset(formId,content){
                window.parent.$.messager.confirm("Confirm", content, function(r){
                    if (r){
                        $('#'+formId).each(function(){
                            getDetails();
                        });
                    }
                });
            }
            
            function deleteSupplier(supplierID,rowIndex){
                var raw_material_id= $('#raw_material_id').val();
                window.parent.$.messager.confirm("Confirm", "Delete the selected item?", function(r){
                    if (r){
                        $.ajax({
                            type: "POST",
                            url: "../SupplierServlet",
                            data: "action=deleteSupplier&supplier_id="+supplierID+"&raw_material_id="+raw_material_id,
                            dataType: "json",
                            cache: false,
                            success: function(data){
                                if(data.type!='error'){
                                    reloadAll();
                                }
                                alertMsg(data);              
                            }
                        });    
                    }     
                });
            }
           
            function formatAction(value,rowData,rowIndex){  
                return '<input type="button" onclick="deleteSupplier('+rowData.supplier_id+');" value="Delete"/>'; 
            }  
            
            $(function() {
                $('#ff').form({
                    url:'../RawMaterialServlet?action=updateRM',
                    onSubmit:function() {
                        return $(this).form('validate');
                    },
                    success:function(data) {
                        var obj = jQuery.parseJSON(data); 
                        if(obj.type!='error') {
                            reloadAll();
                        }
                        alertMsg(obj); 
                    }
                });
            });
        </script>
    </head>
    <body>
        <div style="padding:3px 2px;border-bottom:1px solid #ccc"><h2>Update Raw Material</h2></div>  
        <br/>
        <form id="ff" method="post">
            <input type="hidden" name="raw_material_id" id="raw_material_id" value=<%= request.getParameter("raw_material_id")%> />
            <div id="updateRM" class="easyui-panel" title="General Details:" style="padding:0px;background:#fafafa;" collapsible="true" >
                <table class="tableForms">
                    <tr>
                        <td class="tableForms_label">Raw Material ID:</td>
                        <td class="tableForms_userInput">
                            <%= request.getParameter("raw_material_id")%>
                        </td>
                    </tr>
                    <tr>
                        <td class="tableForms_label">Raw Material Name:</td>  
                        <td class="tableForms_userInput">
                           <input class="easyui-validatebox" id="mat_name" name="mat_name" type="text" required="true" />
                           <input type="submit" onclick="onClickConfirm()" value="Update" />
                           <input type="button" onclick="rmReset('updateRM','Reset the form?')" value="Reset" />
                        </td>  
                    </tr>
                </table>
            </div>
            <br/>
          <div id="supplierDetails" class="easyui-panel" title="Supplier Details:" style="height:250px;background:#fafafa;"  
             collapsible="true" > 
            <%--  <div id="tb" style="padding:5px;height:auto"> 
              <div style="margin-bottom:5px">  
                    <a href="javascript:void(0)" onclick="javascript:window.parent.addTab('add_role','Create Role','<%=GVSYSTEMADMIN.CREATE_ROLE%>?tabTitle=Create+Role&department_id=<%=request.getParameter("department_id")%>');" class="easyui-linkbutton" iconCls="icon-add" plain="true">Create Role</a>  
                </div>
            </div>--%>
            <table id="tt" class="easyui-datagrid" fit="true" style="width:100%;height:100px;"  
                   url="../SupplierServlet?action=retrieveSuppliers&raw_material_id=<%=request.getParameter("raw_material_id")%>"   
                   toolbar="#tb"  
                   singleSelect="true" fitColumns="true" pagination="true"
                   rownumbers="false" > 
                <thead>
                    <tr> 
                        <th field="supplier_id" width="20%">Supplier ID</th>
                        <th field="supplier_name" width="30%">Supplier Name</th>
                        <th field="supplier_address" width="30%">Supplier Address</th>
                        <th field="action" align="right" width="20%" formatter="formatAction"></th>  
                    </tr>  
                </thead>  
            </table> 
        </div>
       </form>
    </body>
</html>

