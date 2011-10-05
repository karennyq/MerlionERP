<%@page import="util.GVCRM"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Customer Transfer Mgmt</title>
        <%@include file = "../global/global_include.jsp" %>
        <script type="text/javascript">            
            function formatActionCHK(value,rowData,rowIndex){  
                var content='<INPUT  TYPE="checkbox" value="'+rowData.inquirer_id+'" NAME="selected_inquirer_id">';
                return content;
            }  
            
            $(function(){
                $('#ff').form({
                    url:'../CustomerServlet?action=updateCustSE',
                    onSubmit:function(){
                        return $(this).form('validate');
                    },
                    success:function(data){
                        var obj = jQuery.parseJSON(data); 
                        if(obj.type!='error'){
                            $('#ff').form('clear'); 
                            $('#sctt').datagrid('reload');
                            var f = window.parent;               
                        }
                        alertMsg(obj);
                    }
                });
            });
            
            $(document).ready(function(){
                $(function(){
                    $('#sedd1').dialog({modal:true});
                    $('#sedd1').dialog('close');
                });
                
                $('#sett1').datagrid({  
                    onClickRow:function(rowIndex,rowData){  
                        $('#emp1_id').val(rowData.emp_id);
                        $('#emp1_id_tx').html(rowData.emp_id);
                        $('#sedd1').dialog('close');
                        var f_url= '../CustomerServlet?action=loadPage&content=tableTRSF&emp_id='+$('#emp1_id').val();
                        $('#sctt').datagrid({url:f_url});
                        $('#sctt').datagrid('reload');
                    }   
                }); 
                
                $(function(){
                    $('#sedd2').dialog({modal:true});
                    $('#sedd2').dialog('close');
                });
                
                $('#sett2').datagrid({  
                    onClickRow:function(rowIndex,rowData){  
                        $('#emp2_id').val(rowData.emp_id);
                        $('#emp2_id_tx').html(rowData.emp_id);
                        $('#sedd2').dialog('close');
                    }   
                }); 
            });
            
            function openDd1(){
                $('#sett1').datagrid('reload');
                $('#sedd1').dialog('open');
            }
            
            function openDd2(){
                $('#sett2').datagrid('reload');
                $('#sedd2').dialog('open');
            }
            
            function custTMReset(formId,content){
                window.parent.$.messager.confirm("Confirm", content, function(r){
                    if (r){
                        $('#'+formId).each(function(){
                            $('#emp1_id').val('');
                            $('#emp1_id_tx').html('');
                            $('#emp2_id').val('');
                            $('#emp2_id_tx').html('');
                            this.reset();
                        });
                    }
                });
            }
        </script>
    </head>
    <body>
        <div style="padding:3px 2px;border-bottom:1px solid #ccc"><h2>Customer Transfer Management</h2></div> 
        <br/>
        <form id="ff" method="post">
            <input type="hidden" id="emp1_id" name="emp1_id"/>
            <input type="hidden" id="emp2_id" name="emp2_id"/>
            <div id="se" class="easyui-panel" title="Sale Executive:" style="padding:0px;background:#fafafa;" collapsible="true" > 
                <table class="tableForms"> 
                    <tr>  
                        <td class="tableForms_label">Sale Executive (From):</td> 
                        <td class="tableForms_userInput">
                            <table>
                                <tr>
                                    <td width="80%"><div id="emp1_id_tx" name="emp1_id_tx"></div></td>
                                    <td width="20%"><a href="javascript:void(0);" class="easyui-linkbutton" iconCls="icon-search" onclick="openDd1()"></a></td>
                                </tr>
                            </table>
                        </td>
                    </tr>
                    <tr>  
                        <td class="tableForms_label">Sale Executive (To):</td> 
                        <td class="tableForms_userInput">
                            <table>
                                <tr>
                                    <td width="80%"><div id="emp2_id_tx" name="emp2_id_tx"></div></td>
                                    <td width="20%"><a href="javascript:void(0);" class="easyui-linkbutton" iconCls="icon-search" onclick="openDd2()"></a></td>
                                </tr>
                            </table>
                        </td>
                    </tr>
                </table> 
            </div>
            <br/>
            <div id="custDd" class="easyui-panel" title="Customer Table:" style="padding:0px;background:#fafafa;"  
                 collapsible="true" > 
                <div id="scdd" title="Search Customer" style="width:100%; height:400px">
                    <table id="sctt" class="easyui-datagrid" fit="true" style="width:100%;"  
                           url="../CustomerServlet?action=loadPage&content=tableTRSF"   
                           toolbar="#tb"  
                           singleSelect="false" fitColumns="true" pagination="true"
                           rownumbers="false">  
                        <thead>  
                            <tr> 
                                <th field="inquirer_id" width="20%">ID</th>  
                                <th field="company_name" width="50%">Company Name</th>
                                <th field="convert_date" width="30%">Date & Time Created</th>
                                <th field="chk_status" width="20%" sortable="true"  formatter="formatActionCHK"></th> 
                            </tr>  
                        </thead>  
                    </table> 
                </div>
            </div>                    
            <br/>
            <div class="form_buttons">
                <input type="submit" value="Transfer"/>
                <input type="button" onclick="custTMReset('ff','Reset the form?')" value="Reset" />
            </div> 
        </form>
        <div id="sedd1" title="Search Employee" style="width:600px; height:300px">
            <table id="sett1" class="easyui-datagrid" fit="true" style="width:100%;"  
                   url="../EmployeeServlet?action=loadPage&content=dialog"   
                   singleSelect="true" fitColumns="true" pagination="true"
                   rownumbers="false">  
                <thead>  
                    <tr> 
                        <th field="emp_id" width="20%">Employee ID</th>  
                        <th field="emp_name" width="40%">Name</th>
                        <th field="email" width="40%">Email</th> 
                    </tr>  
                </thead>  
            </table> 
        </div>
        <div id="sedd2" title="Search Employee" style="width:600px; height:300px">
            <table id="sett2" class="easyui-datagrid" fit="true" style="width:100%;"  
                   url="../EmployeeServlet?action=loadPage&content=dialog"  
                   singleSelect="true" fitColumns="true" pagination="true"
                   rownumbers="false">  
                <thead>  
                    <tr> 
                        <th field="emp_id" width="20%">Employee ID</th>  
                        <th field="emp_name" width="40%">Name</th>
                        <th field="email" width="40%">Email</th>
                    </tr>  
                </thead>  
            </table> 
        </div>
    </body>
</html>
