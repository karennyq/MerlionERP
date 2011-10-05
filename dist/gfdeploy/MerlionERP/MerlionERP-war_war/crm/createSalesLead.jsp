<%-- 
    Document   : createSalesLead
    Created on : Sep 7, 2011, 11:13:49 AM
    Author     : Ken
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <%@include file = "../global/global_include.jsp" %>
        <title>Create Sales Lead</title>
        <script type="text/javascript">  
            $(function(){
                $('#ff').form({
                    url:'../SalesLeadServlet?action=createSalesLead',
                    onSubmit:function(){
                        return $(this).form('validate');
                    },
                    success:function(data){
                        var obj = jQuery.parseJSON(data); 
                        if(obj.type!='error'){
                            $('#ff').form('clear');                         
                            try{
                                reloadAll();
                                alertMsg(obj);
                                closeTab("");
                            }catch(err){
                            }
                        }
                        alertMsg(obj);
                    }
                });
            });
            
            $('#nn').numberbox({  
                min:0,  
                precision:0  
            });
            
            function onClickConfirm(){
                $('#createSales').panel('expand');
            }
        </script>    
    </head>
    <body>
        <div style="padding:3px 2px;border-bottom:1px solid #ccc"><h2>Create Sales Lead</h2></div>  
        <br/>
        <form id="ff" method="post"> 
            <div id="createSales" class="easyui-panel" title="General Details:" style="padding:0px;background:#fafafa;" collapsible="true" > 
                <table class="tableForms">  
                    <tr>  
                        <td class="tableForms_label">Company:</td>  
                        <td class="tableForms_userInput"><input class="easyui-validatebox" id="company_name" name="company_name" type="text" required="true"/></td>  
                    </tr>
                    <tr>
                        <td class="tableForms_label">Contact Person:</td>  
                        <td class="tableForms_userInput"><input class="easyui-validatebox" id="contact_person" name="contact_person" type="text" required="true"/></td>  
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
                        <td class="tableForms_userInput"><textarea class="easyui-validatebox" id="company_add" name="company_add"></textarea></td>  
                    </tr>  
                    <tr>
                        <td class="tableForms_label">Remarks:</td>  
                        <td class="tableForms_userInput"><textarea id="remarks" name="remarks"></textarea></td>
                    </tr>
                </table>  
            </div>
            <br/>
            <div class="form_buttons">
                <input type="submit" onclick="onClickConfirm();" value="Create"/>
                <input type="button" onclick="confirmReset('ff','Clear the form?')" value="Clear" />
            </div> 
        </form>
    </body>
</html>
