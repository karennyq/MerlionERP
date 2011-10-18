<%-- 
    Document   : createSupp
    Created on : Oct 7, 2011, 12:49:13 PM
    Author     : alyssia
--%>

<%@page import="org.persistence.SalesLead"%>
<%@page import="org.persistence.LineItem"%>
<%@page import="java.util.ArrayList"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <% session.setAttribute("createSupplierwithRawMaterial", new ArrayList());%>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <%@include file = "../global/global_include.jsp" %>
        <title>Create Supplier</title>
        <script type="text/javascript"> 
            
            function openRM(){
                $('#tt').datagrid('reload');
                $('#dd').dialog('open');         
            }
            
            $(document).ready(function(){
                $(function(){
                    $('#dd').dialog({modal:true});
                    $('#dd').dialog('close');
                    $('#raw_material_details').hide();
                });
                
                $('#tt').datagrid({  
                    onClickRow:function(rowIndex,rowData){  
                        $('#raw_material_id').html(rowData.raw_material_id);
                        getRawMaterialInfo();
                        $('#raw_material_id_hidden').val(rowData.raw_material_id);
                        $('#mat_name').html(rowData.mat_name);
                        
                        $('#dd').dialog('close');
                    }   
                });
               
                // rm table
                $('#rmtt').datagrid({
                    onBeforeEdit:function(index,row){  
                        row.editing = true;  
                        updateActions();  
                    },  
                    onAfterEdit:function(index,row){  
                        row.editing = false; 
                        $.ajax({
                            type: "POST",
                            url: "../SupplierServlet",
                            data: "action=updateRawMaterialItem"
                                + "&update_leadTime=" + row.lead_time
                                + "&update_lotSize=" + row.lot_size
                                + "&update_shelfLife=" + row.shelf_life
                                + "&update_storageUnit=" + row.storage_unit
                                + "&listIndex=" + index
                                + "&listName=" + "createSupplierwithRawMaterial",
                            dataType: "json",
                            cache: false,
                            success: function(data){
                                if(data.type!='error'){
                                    reloadAll();
                                }
                                alertMsg(data);    
                            }
                        }); 
                        updateActions();  
                    },  
                    onCancelEdit:function(index,row){  
                        row.editing = false;  
                        updateActions();  
                    }
                });
            });
            
            function getRawMaterialInfo(){
                $.ajax({
                    type: "POST",
                    url: "../RawMaterialServlet",
                    data: "action=loadPage"
                        + "&content=" + "rmDetails"
                        + "&raw_material_id=" + $('#raw_material_id_hidden').val(),
                    dataType: "json",
                    cache: false,
                    success: function(data){
                        $('#raw_material_details').show();
                        $('#raw_material_id').val(data.raw_material_id);
                        $('#mat_name').val(data.mat_name);
                    }
                });
            } 
             
            function addRm(){
                var rmId = $('#raw_material_id_hidden').val();
                var leadTime = $('#lead_time').val();
                var shelfLife = $('#shelf_life').val();
                var lotSize = $('#lot_size').val();
                var storageUnit = $('#storage_unit').val();
               
                $.ajax({
                    type: "POST",
                    url: "../SupplierServlet",
                    data: "action=addRMDetails"
                        + "&rmId=" + rmId
                        + "&leadTime=" + leadTime
                        + "&shelfLife=" + shelfLife
                        + "&lotSize=" + lotSize
                        + "&storageUnit=" + storageUnit
                        + "&listName=" + "createSupplierwithRawMaterial",
                    dataType: "json",
                    cache: false,
                    success: function(data){
                        if(data.type!='error'){                            
                            $('#raw_material_id').html("");
                            $('#mat_name').html("");
                            $('#lead_time').val("");
                            $('#shelf_life').val("");
                            $('#lot_size').val("");
                            $('#storage_unit').val("");
                            $('#rmtt').datagrid('reload');  
                            $('#raw_material_details').hide();
                            reloadAll();
                        }else{
                            alertMsg(data);    
                        }
                    }
                });
            }
            
            function cfmReset(formId,content){
                window.parent.$.messager.confirm("Confirm", content, function(r){
                    if (r){
                        $('#raw_material_details').hide();
                        $('#raw_material_id').html("");
                        $.ajax({
                            type: "POST",
                            url:"../SupplierServlet",
                            data: "action=loadPage"
                                + "&content=" + "rmTable"
                                + "&reset=" + "true"
                                + "&listName=" + "createSupplierwithRawMaterial",
                            dataType: "json",
                            cache: false,
                            success: function(data){
                                if(data.type!='error'){                            
                                    $('#supplier_id').val("");
                                    $('#supplier_add').val("");
                                    reloadAll();  
                                }else{
                                    alertMsg(data);    
                                }
                            }
                        });
 
                        $('#'+formId).each(function(){
                            this.reset();
                        });
                    }
                });
            }
            
            function formatMatName(value,rowData,rowIndex){
                try {
                    return (value.mat_name==null)?"":value.mat_name; 
                } catch (err) {
                }
            }
            
            function formatActions(value,row,index){ 
                if (row.editing) {  
                    var s = '<input type="button" onclick="saverow('+index+')" value="Save"/> ';  
                    var c = '<input type="button" onclick="cancelrow('+index+')" value="Cancel"/>';  
                    return s+c;  
                } else {  
                    var e = '<input type="button" onclick="editrow('+index+')" value="Edit"/> ';  
                    var d = '<input type="button" onclick="removeRMItem('+index+')" value="Delete"/>';  
                    return e+d;  
                }  
            }
            
            function updateActions(){  
                var rowcount = $('#rmtt').datagrid('getRows').length;  
                for(var i=0; i<rowcount; i++){ 
                    $('#rmtt').datagrid('updateRow',{  
                        index:i,  
                        row:{actions:''}  
                    });  
                }  
            }  
            
            function editrow(index){  
                $('#rmtt').datagrid('beginEdit', index);  
            }  
             
            function saverow(index){  
                $('#rmtt').datagrid('endEdit', index);  
                 
            }
            
            function cancelrow(index){  
                $('#rmtt').datagrid('cancelEdit', index);  
            }  
            
            function removeRMItem(rowIndex){
                window.parent.$.messager.confirm("Confirm", "Delete the selected item?", function(r){
                    if (r){
                        $.ajax({
                            type: "POST",
                            url: "../SupplierServlet",
                            data: "action=removeRMItem"
                                + "&listIndex=" + rowIndex
                                + "&listName=" + "createSupplierwithRawMaterial",
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
            
            function onClickConfirm(){
                //$('#findInquirer').panel('expand');
                //$('#createInquiry').panel('expand');
                //$('#pdtDetails').panel('expand');
            }
            
            function filterRawMat(){
                var raw_mat_name_search=$('#raw_mat_name_search').val();
                var f_url='../RawMaterialServlet?action=loadPage&content=dialog&raw_mat_name_search='+raw_mat_name_search;
                $('#tt').datagrid({url:f_url});     
            }
            
            $(function(){
                $('#ff').form({
                    url:'../SupplierServlet?action=createSupplier&listName=createSupplierwithRawMaterial',
                    onSubmit:function(){   
                        return $(this).form('validate');
                    },
                    success:function(data){
                        var obj = jQuery.parseJSON(data); 
                        if((obj.type!='error')){
                            $('#ff').form('clear'); 
                            $('#raw_material_details').hide();
                            //$('#raw_material_id').html("");
                            try {
                                reloadAll();
                                alertMsg(obj);
                                closeTab("Create Supplier");
                            } catch(err){  
                            }        
                        }
                        alertMsg(obj);
                    }
                });
            });
        </script>    
    </head>
    <body>
        <div style="padding:3px 2px;border-bottom:1px solid #ccc"><h2>Create Supplier</h2></div>  
        <br/>
        <form id="ff" method="post">

            <div id="createSupplier" name="createSupplier" class="easyui-panel" title="Supplier Details:" style="padding:0px;background:#fafafa;" collapsible="true" >
                <table class="tableForms"> 
                    <tr>  
                        <td class="tableForms_label">Supplier Name: </td> 
                        <td class="tableForms_userInput">
                            <input class="easyui-validatebox" id="supplier_name" name="supplier_name" type="text"/>
                        </td>
                    </tr>
                    <tr>  
                        <td class="tableForms_label">Supplier Address:</td>  
                        <td class="tableForms_userInput"><textarea id="supplier_address" name="supplier_address"></textarea></td>  
                    </tr>
                </table>  
            </div>
            <br/>
            <input type="hidden" id="raw_material_id_hidden" name="raw_material_id_hidden"/>
            <div id="findRawMaterial" name="findRawMaterial" class="easyui-panel" title="Raw Material Details:" style="padding:0px;background:#fafafa;" collapsible="true" > 
                <table class="tableForms"> 
                    <tr>  
                        <td class="tableForms_label">Raw Material ID:</td> 
                        <td class="tableForms_userInput">
                            <table>
                                <tr>
                                    <td>
                                        <label id="raw_material_id" name="raw_material_id"></label>
                                    </td>
                                    <td><a href="javascript:void(0);" class="easyui-linkbutton"  onclick="openRM()" iconCls="icon-search"></a></td>
                                </tr>
                            </table>
                        </td>
                    </tr>
                    <tbody id="raw_material_details" name="raw_material_details" hidden="true">
                        <tr>  
                            <td class="tableForms_label">Raw Material Name:</td>  
                            <td class="tableForms_userInput">
                                <label id="mat_name" name="mat_name"></label>
                            </td>  
                        </tr>
                        <tr>
                            <td class="tableForms_label">Lead Time (Week):</td> 
                            <td class="tableForms_userInput">
                                <input class="easyui-numberbox" id="lead_time" name="lead_time" type="text"/>
                            </td>
                        </tr>
                        <tr>
                            <td class="tableForms_label">Lot Size (Kg):</td> 
                            <td class="tableForms_userInput">
                                <input class="easyui-numberbox" id="lot_size" name="lot_size" type="text"/>
                            </td>
                        </tr>
                        <tr>
                            <td class="tableForms_label">Shelf Life (Month):</td> 
                            <td class="tableForms_userInput">
                                <input class="easyui-numberbox" id="shelf_life" name="shelf_life" type="text"/>
                            </td>
                        </tr>
                        <tr>  
                            <td class="tableForms_label">Storage unit:</td> 
                            <td class="tableForms_userInput">
                                <input class="easyui-numberbox" id="storage_unit" name="storage_unit" type="text"/>
                                &nbsp;&nbsp;&nbsp;&nbsp;
                                <a href="javascript:void(0);" id="addRmBt" class="easyui-linkbutton" onclick="addRm()">Add</a>
                            </td>  
                        </tr>
                    </tbody>
                </table>  
                <div id="rm-add-tt" name="rm-add-tt" fit=true style="height:250px;">
                    <table id="rmtt" name="rmtt" class="easyui-datagrid" fit="true" style="width:100%; height:100px;" 
                           url="../SupplierServlet?action=loadPage&content=rmTable&listName=createSupplierwithRawMaterial"
                           idField="rmItem_id"
                           fitColumns="true">  
                        <thead>  
                            <tr> 
                                <th field="rawMaterial" width="20%" formatter="formatMatName">Raw Material Name</th>
                                <th field="lead_time" editor="{
                                    type:'numberbox',  
                                    options:{  
                                    min:1,  
                                    precision:0,    
                                    required:true }
                                    }" width="15%">Lead Time</th>
                                <th field="lot_size" editor="{
                                    type:'numberbox',  
                                    options:{  
                                    min:1,  
                                    precision:0,    
                                    required:true }
                                    }" width="15%">Lot Size</th>
                                <th field="shelf_life" editor="{
                                    type:'numberbox',  
                                    options:{  
                                    min:1,  
                                    precision:0,    
                                    required:true }
                                    }" width="15%">Shelf Life</th>
                                <th field="storage_unit" editor="{
                                    type:'numberbox',  
                                    options:{  
                                    min:1,  
                                    precision:0,    
                                    required:true }
                                    }" width="15%">Storage Unit</th>
                                <th field="actions" width="20%" formatter="formatActions"></th>
                            </tr>  
                        </thead>
                    </table>
                </div>
            </div>
            <br/>
            <div class="form_buttons">
                <input type="submit" value="Create" onclick="onClickConfirm();"/>
                <input type="button" onclick="cfmReset('ff','Clear the form?')" value="Clear" />
            </div> 
        </form>
        <div id="tb" style="padding:5px;height:auto">  
            <div>
                <table cellpadding="5">
                    <tr>
                        <td>Raw Material Name:</td>
                        <td><input name="raw_mat_name_search" id="raw_mat_name_search" type="text"/></td>
                        <td><a href="#" class="easyui-linkbutton" onclick="filterRawMat()" iconCls="icon-search">Search</a></td>
                    </tr>
                </table>
            </div>  
        </div>
        <div id="dd" title="Search Raw Material" style="width:600px; height:300px">
            <table id="tt" class="easyui-datagrid" fit="true" style="width:100%;"  
                   url="../RawMaterialServlet?action=loadPage&content=rmDetails"   
                   toolbar="#tb"  
                   singleSelect="true"
                   fitColumns="true"
                   rownumbers="true">  
                <thead>  
                    <tr> 
                        <th field="raw_material_id" width="50%">Raw Material ID</th>  
                        <th field="mat_name" width="50%">Raw Material Name</th>
                    </tr>  
                </thead>  
            </table> 
        </div>
    </body>
</html>
