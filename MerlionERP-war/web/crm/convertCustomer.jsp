<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <%@include file = "../global/global_include.jsp" %>
        <title>Create Customer from Existing Sales Lead</title>
        <script type="text/javascript">  
            $(function(){
                $('#ff').form({
                    url:'../CustomerServlet?action=convertCustomer',
                    onSubmit:function(){
                        return $(this).form('validate');
                    },
                    success:function(data){
                        var obj = jQuery.parseJSON(data); 
                        if(obj.type!='error'){
                            $('#emp_id_tx').html('');
                            $('#emp_name_tx').html('');
                            $('#tt').datagrid('reload');
                            reloadAll();
                            alertMsg(obj);
                            closeTab("");
                        }         
                        alertMsg(obj);
                    }
                });
            });
            
            $('#nn').numberbox({  
                min:0,  
                precision:0  
            });
            
            function getSalesLeadInfo(){
                var salesLeadID= $('#inquirer_id').val();
                $.ajax({
                    type: "POST",
                    url: "../SalesLeadServlet",
                    data: "action=loadPage&content=details&inquirer_id="+salesLeadID,
                    dataType: "json",
                    cache: false,
                    success: function(data){
                        $('#inquirer_id').val(data.inquirer_id);
                        $('#company_name').val(data.company_name);
                        $('#contact_person').val(data.contact_person);
                        $('#contact_no').val(data.contact_no);
                        $('#fax_no').val(data.fax_no);
                        $('#email').val(data.email);
                        $('#company_name').val(data.company_name);
                        $('#company_add').val(data.company_add);
                        $('#remarks').val(data.remarks);  
                        $('#country').val(data.country);
                        $('#city').val(data.city);
                        $('#cust_type').combobox('select',data.cust_type.replace('_',' '))

                    }
                });
            } 
                       
            $(document).ready(function(){
                if($('#inquirer_id').val()!=null||$('#inquirer_id').val()==""){
                    getSalesLeadInfo();
                }
                
                $('#cust_type').combobox({  
                    url:'../CustomerServlet?action=loadPage&content=dropdown',  
                    valueField:'text',  
                    textField:'text',
                    editable:false,
                    required:true,
                    formatter:comboboxFomatter
                });
                
                $(function(){
                    $('#dd').dialog({modal:true});
                    $('#dd').dialog('close');
                });
                
                $('#tt').datagrid({  
                    onClickRow:function(rowIndex,rowData){  
                        $('#emp_id').val(rowData.emp_id);
                        $('#emp_name').val(rowData.emp_name);
                        $('#emp_id_tx').html(rowData.emp_id);
                        $('#emp_name_tx').html(rowData.emp_name);   
                        $('#dd').dialog('close');
                    }   
                });     
            });
            
            function searchSalesExName(){
                var emp_name=$('#salesEx_name').val();
                var f_url='../EmployeeServlet?action=loadPage&content=dialog&emp_name='+emp_name+'';
                $('#tt').datagrid({  
                    url:f_url });   
                $('#tt').datagrid('load');
            }
            
            function reset(){
                $('#salesEx_name').val("");
                searchSalesExName();
            }
            
            function openDd(){
                $('#tt').datagrid('reload');
                $('#dd').dialog('open');
            }
            
            function salesReset(formId,content){
                window.parent.$.messager.confirm("Confirm", content, function(r){
                    if (r){
                        $('#'+formId).each(function(){
                            getSalesLeadInfo();
                        });
                        $('#emp_id_tx').html("");
                        $('#emp_name_tx').html("");
                        
                    }
                });
            }
            
            function onClickConfirm(){
                $('#convertCust').panel('expand');
            }
        </script>    
    </head>
    <body>
        <div style="padding:3px 2px;border-bottom:1px solid #ccc"><h2>Create Customer from Existing Sales Lead  (ID: <%=(request.getParameter("inquirer_id") != null) ? request.getParameter("inquirer_id") : ""%>)</h2></div>  
        <br/>
        <form id="ff" method="post">
            <input type="hidden" id="inquirer_id" name="inquirer_id" value="<%=(request.getParameter("inquirer_id") != null) ? request.getParameter("inquirer_id") : ""%>"/>
            <input type="hidden" id="emp_name" name="emp_name" value="<%=(request.getParameter("emp_name") != null) ? request.getParameter("emp_name") : ""%>"/>
            <input type="hidden" id="emp_id" name="emp_id" value="<%=(request.getParameter("emp_id") != null) ? request.getParameter("emp_id") : ""%>"/>
            <div  id="convertCust" class="easyui-panel" title="General Details:" style="padding:0px;background:#fafafa;" collapsible="true" > 
                <table class="tableForms"> 
                    <tr>  
                        <td class="tableForms_label">Sales Lead ID:</td> 
                        <td class="tableForms_userInput">
                            <div id="inquirer_id_tx" name="inquirer_id_tx">
                                <%=(request.getParameter("inquirer_id") != null) ? request.getParameter("inquirer_id") : ""%>
                            </div>                                
                        </td>
                    </tr>
                    <tr>  
                        <td class="tableForms_label">Sales Executive ID:</td> 
                        <td class="tableForms_userInput">
                            <table>  
                                <tr>
                                    <td width="80%">
                                        <div id="emp_id_tx" name="emp_id_tx">
                                            <%=(request.getParameter("emp_id") != null) ? request.getParameter("emp_id") : ""%>
                                        </div>
                                    </td>
                                    <td width="20%"><a href="javascript:void(0);" class="easyui-linkbutton" iconCls="icon-search" onclick="openDd()"></a></td>
                                </tr>
                            </table> 
                        </td>
                    </tr>    
                    <tr>  
                        <td class="tableForms_label">Sales Executive Name:</td> 
                        <td class="tableForms_userInput">
                            <div id="emp_name_tx" name="emp_name_tx">
                                <%=(request.getParameter("emp_name") != null) ? request.getParameter("emp_name") : ""%>
                            </div>                                
                        </td>
                    </tr>
                    <tr>
                        <td class="tableForms_label">Company:</td>  
                        <td class="tableForms_userInput"><input class="easyui-validatebox" id="company_name" name="company_name" type="text" required="true"/></td>  
                    </tr> 
                    <tr>  
                        <td class="tableForms_label">Country:</td>  
                        <td class="tableForms_userInput"><input class="easyui-validatebox" id="country" name="country" type="text" required="true"/></td>  
                    </tr>
                    <tr>
                        <td class="tableForms_label">City:</td>  
                        <td class="tableForms_userInput"><input class="easyui-validatebox" id="city" name="city" type="text" required="true"/></td>  
                    </tr> 
                    <tr>  
                        <td class="tableForms_label">Contact Person:</td>  
                        <td class="tableForms_userInput"><input class="easyui-validatebox" id="contact_person" name="contact_person" type="text" required="true"/></td>  
                    </tr>
                    <tr>
                        <td class="tableForms_label">Contact:</td>  
                        <td class="tableForms_userInput"><input class="easyui-numberbox" id="contact_no" name="contact_no" required="true"/></td>  
                    </tr> 
                    <tr>  
                        <td class="tableForms_label">Fax No:</td>  
                        <td class="tableForms_userInput"><input class="easyui-numberbox" id="fax_no" name="fax_no"/></td>  
                    </tr>
                    <tr>
                        <td class="tableForms_label">Email:</td>  
                        <td class="tableForms_userInput"><input class="easyui-validatebox" id="email" name="email" type="text" required="true" validtype="email"/></td>  
                    </tr> 
                    <tr>  
                        <td class="tableForms_label">Address:</td>  
                        <td class="tableForms_userInput"><textarea class="easyui-validatebox" id="company_add" name="company_add" required="true"></textarea></td>
                    </tr>
                    <tr>
                        <td class="tableForms_label">Customer Type:</td>  
                        <td class="tableForms_userInput">
                            <select id="cust_type" name="cust_type" panelHeight="auto" class="easyui-combobox"></select>
                        </td>
                    </tr>
                    <tr>  
                        <td class="tableForms_label">Remarks:</td>  
                        <td class="tableForms_userInput"><textarea id="remarks" name="remarks"></textarea></td> 
                    </tr>  
                </table>  
            </div>
            <br/>
            <div class="form_buttons">
                <input id="btc" name="btc" type="submit" onclick="onClickConfirm();" value="Convert"/>
                <input id="btr" name="btr" type="button" onclick="salesReset('ff','Do you want to reset the form?')" value="Reset" />
            </div> 
        </form>
        <div id="tb" style="padding:5px;height:auto">  
            <div>
                <table cellpadding="5">
                    <tr>
                        <td>Sales Executive Name:</td>
                        <td><input name="salesEx_name" id="salesEx_name" type="text"/></td>
                        <td>
                            <a href="#" class="easyui-linkbutton" onclick="searchSalesExName()" iconCls="icon-search"></a>
                            <a href="#" class="easyui-linkbutton" onclick="reset()">Clear</a>
                        </td>
                    </tr>
                </table>
            </div>  
        </div>
        <div id="dd" title="Search Sales Executives" style="width:600px; height:300px">
            <table id="tt" class="easyui-datagrid" fit="true" style="width:100%;"  
                   url="../EmployeeServlet?action=loadPage&content=dialog"   
                   toolbar="#tb"  
                   singleSelect="true" fitColumns="true"
                   rownumbers="false"
                   pagination="true">  
                <thead>  
                    <tr> 
                        <th field="emp_id" width="30%" sortable="true">Sales Executive ID</th>  
                        <th field="emp_name" width="70%" sortable="true">Sales Executive Name</th>
                    </tr>  
                </thead>  
            </table> 
        </div>                
    </body>
</html>
