<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <%@include file = "../global/global_include.jsp" %>
        <title>Create Customer</title>
        <script type="text/javascript">  
            $(function(){
                $('#ff').form({
                    url:'../CustomerServlet?action=updateCustomer',
                    onSubmit:function(){
                        return $(this).form('validate');
                    },
                    success:function(data){
                        var obj = jQuery.parseJSON(data); 
                        if(obj.type!='error'){
                            reloadAll();
                        }
                        alertMsg(obj);
                    }
                });
            });
            
            $('#nn').numberbox({  
                min:0,  
                precision:0  
            });
            
            function getCustomerInfo(){
                $.ajax({
                    type: "POST",
                    url: "../CustomerServlet",
                    data: "action=loadPage"
                        + "&content=" + "details"
                        + "&inquirer_id=" + $('#inquirer_id').val(),
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
                if($('#inquirer_id').val()!=null&&$('#inquirer_id').val()!=""){
                    getCustomerInfo();
                }
                
                $('#cust_type').combobox({  
                    url:'../CustomerServlet?action=loadPage&content=dropdown',  
                    valueField:'id',  
                    textField:'text',
                    editable:false,
                    required:true,
                    formatter:comboboxFomatter
                });
            });
            
            function custReset(formId,content){
                window.parent.$.messager.confirm("Confirm", content, function(r){
                    if (r){
                        $('#'+formId).each(function(){
                            getCustomerInfo();
                        });
                    }
                });
            }
            
            function onClickConfirm(){
                $('#updateCust').panel('expand');
            }
        </script>    
    </head>
    <body>
        <div style="padding:3px 2px;border-bottom:1px solid #ccc"><h2>Update Customer (ID:  <%=(request.getParameter("inquirer_id") != null) ? request.getParameter("inquirer_id") : ""%>)</h2></div>  
        <br/>
        <form id="ff" method="post">
            <input type="hidden" id="inquirer_id" name="inquirer_id" value="<%=(request.getParameter("inquirer_id") != null) ? request.getParameter("inquirer_id") : ""%>"/>
            <div id="updateCust" class="easyui-panel" title="General Details:" style="padding:0px;background:#fafafa;" collapsible="true" > 
                <table class="tableForms"> 
                    <tr>  
                        <td class="tableForms_label">Customer ID:</td> 
                        <td class="tableForms_userInput">
                            <div id="inquirer_id_tx" name="inquirer_id_tx">
                                <%=(request.getParameter("inquirer_id") != null) ? request.getParameter("inquirer_id") : ""%>
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
                <input type="submit" onclick="onClickConfirm();" value="Save"/>
                <input type="button" onclick="custReset('ff','Reset the form?')" value="Reset" />
            </div> 
        </form>
    </body>
</html>
