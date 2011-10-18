<%-- 
    Document   : viewEmployees
    Created on : Aug 31, 2011, 6:26:30 PM
    Author     : Ken
--%>

<%@page import="util.GVSYSTEMADMIN"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Employee Management</title>
        <%@include file = "../global/global_include.jsp" %>
        <script type="text/javascript"> 
            function goUpdate(empId){
                window.parent.addTab('update_emp_'+empId,'Update Employee: '+empId,'<%=GVSYSTEMADMIN.UPDATE_EMP%>?emp_id='+empId);
            }

            //on link
            function formatAction(value,rowData,rowIndex){  
                return '<input type="button" onclick="goUpdate('+rowData.emp_id+');" value="Update"/>'; 
            }  
        
            function formatActionReset(value,rowData,rowIndex){  
                return '<input type="button" onclick="goReset('+rowData.emp_id+');" value="Reset Password"/>'; 
            }  
        
            function goReset(empId){
                window.parent.$.messager.confirm("Confirm", "Reset password?", function(r){
                    if (r){
                        $.ajax({
                            type: "POST",
                            url: "../EmployeeServlet",
                            data: "action=resetPassword&emp_id="+empId,
                            dataType: "json",
                            cache: false,
                            success: function(data){
                                if(data.type!='error'){
                                    $('#tt').datagrid('reload');
                                }
                                alertMsg(data);    
                            }
                        });    
                    }     
                });
            }
        
            function formatActionActive(value,rowData,rowIndex){  
                var active="";
                var inactive="";
                if(rowData.active_status=='Active'){
                    active="checked";
                }else{
                    inactive="checked";
                }
                var content='<INPUT onchange="inActive(\''+value+'\',\''+rowData.emp_id+'\')" TYPE=RADIO NAME="activate_status'+rowData.emp_id+'" VALUE="0"'+active+' >Active<INPUT onchange="inActive(\''+value+'\',\''+rowData.emp_id+'\')" TYPE=RADIO NAME="activate_status'+rowData.emp_id+'" VALUE="1" '+inactive+'>Inactive';
                return content;
            }  
            
            function inActive(val,empId){
                if(val=='Active'){
                    val='Inactive';
                }else{
                    val='Active';
                }
                
                window.parent.$.messager.confirm("Confirm", "Change employee's status?", function(r){
                    if (r){
                        $.ajax({
                            type: "POST",
                            url: "../EmployeeServlet",
                            data: "action=activate&emp_id="+empId+"&status="+val,
                            dataType: "json",
                            cache: false,
                            success: function(data){
                                if(data.type!='error'){
                                    $('#tt').datagrid('reload');
                                }
                                alertMsg(data);    
                            }
                        });    
                    }  else{
                        $('#tt').datagrid('reload');
                    }   
                });
            }
            
            //filtering the table
            function filterTable(){    
                var nric=$('#nric').val();
                var emp_name=$('#emp_name').val();
                var status=$('#active_type').combobox('getValue');
                var f_url='../EmployeeServlet?action=loadPage&content=table&nric='+nric+'&emp_name='+emp_name+'&status='+status;
                $('#tt').datagrid({url:f_url});
                $('#tt').datagrid('load');
            }
             
            $(document).ready(function(){
                $('#active_type').combobox({  
                    url:'../EmployeeServlet?action=loadPage&content=dropdown',  
                    valueField:'id',  
                    textField:'text',
                    editable:false,
                    formatter:comboboxFomatter
                    //required:true
                });
                
                //for debug table loading
                /*$('#tt').datagrid({ 
                    onLoadError:function(){  
                        alert("Table loading failed.");
                    },
                    onLoadSuccess:function(data){  
                        var obj = jQuery.parseJSON(data); 
                        if((obj.type=='error')){
                            alertMsg(obj);
                        }
                    }   
                });  */    
            });
            
            function reset(){
                $('#nric').val("");                
                $('#emp_name').val("");
                var status=$('#active_type').combobox('setValue', 0);

                filterTable();
            }
        </script>
    </head>
    <body>
        <div style="padding:3px 2px;border-bottom:1px solid #ccc"><h2>Employee Management</h2></div> 
        <br/>
        <div id="tb" style="padding:5px;height:auto">  
            <div style="margin-bottom:5px">  
                <a href="javascript:void(0)" onclick="javascript:window.parent.addTab('add_emp','Create Employee','<%=GVSYSTEMADMIN.CREATE_EMP%>?tabTitle=Create+Employee');" class="easyui-linkbutton" iconCls="icon-add" plain="true">Create Employee</a>  
            </div>  
            <div>
                <table cellpadding="5">
                    <tr>
                        <td>Nric/Fin:</td>
                        <td>
                            <input name="nric" id="nric" type="text"/>
                        </td>
                        <td>Employee Name:</td>
                        <td>
                            <input  name="emp_name" id="emp_name" type="text"/>
                        </td>
                        <td>Status:</td>
                        <td>
                            <input id="active_type" name="active_type" panelHeight="auto" class="easyui-combobox"/>
                        </td>
                        <td><a href="#" class="easyui-linkbutton" onclick="filterTable()" iconCls="icon-search"></a>
                            <a href="#" class="easyui-linkbutton" onclick="reset()">Clear</a>
                        </td>
                    </tr>
                </table>  
            </div>  
        </div>  
        <div fit="true" style=" height:500px">
            <table id="tt" class="easyui-datagrid" fit="true" style="width:100%;"  
                   url="../EmployeeServlet?action=loadPage&content=table"   
                   toolbar="#tb"  
                   singleSelect="true" fitColumns="true" pagination="true"
                   rownumbers="false">  
                <thead>  
                    <tr>  
                        <th field="nric" width="15%" sortable="true" >NRIC/FIN</th>
                        <th field="emp_name" width="20%" sortable="true" >Employee Name</th>  
                        <th field="email" width="20%" sortable="true" >Email</th>  
                        <th field="active_status" width="20%" sortable="true" formatter="formatActionActive">Status</th>  
                        <th field="action" align="right" width="10%" formatter="formatAction"></th>  
                        <th field="action1" align="right" width="15%" formatter="formatActionReset"></th>  
                    </tr>  
                </thead>  
            </table> 
        </div>
    </body>
</html>
