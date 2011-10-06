  
<%@page import="java.util.HashMap"%>
<%@page import="util.GVCRM"%>
<%@page import="util.GVFRM"%>
<%@page import="util.GVSYSTEMADMIN"%>
<%@page import="util.GVPRODUCTION"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">
<html>
    <head>
        <%
            /* Data Attributes*/
            String userIdentification = request.getSession().getAttribute("loginUserId").toString();
            if (userIdentification == null) {
                userIdentification = "";
            }

            if (userIdentification.isEmpty()) {
                String redirectURL = "../index.jsp";
                response.sendRedirect(redirectURL);
            }

            HashMap hm = (HashMap) session.getAttribute("acessRightsHm");
        %>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
        <title>MerlionERP&trade;</title>        
        <script type="text/javascript" src="../js/jquery-easyui-1.2/jquery-1.6.min.js"></script>
        <script type="text/javascript" src="../js/jquery-easyui-1.2/jquery-ui-1.8.16.custom.min.js"></script>
        <script type="text/javascript" src="../js/jquery-easyui-1.2/jquery.easyui.min.js"></script>
        <link type="text/css" rel="stylesheet" href="../js/jquery-easyui-1.2/themes/default/easyui.css"/>
        <link type="text/css" rel="stylesheet" href="../js/jquery-easyui-1.2/themes/icon.css"/>
        <link type="text/css" rel="stylesheet" href="../css/cupertino/jquery-ui-1.8.16.custom.css"/>
        <link type="text/css" rel="stylesheet" href="../css/design.css" />
        <script type="text/javascript">
            var tabNo=0;
            $(function(){
                $('#tt').tabs({
                    tools:[{
                            iconCls:'icon-closeAll',
                            handler: function(){
                                window.parent.$.messager.confirm("Close Tabs", "Close All Tabs?", function(r){
                                    if (r==true){
                                    
                                        var t = $('#tt');  
                                        var tabs = t.tabs('tabs');  
                                        //var no=tabs.tabs.length
                                        for(var i=0; i<tabNo;i++){
                                            //alert(i);
                                            //closeTab();
                                            $('#tt').tabs('close', tabs[1].panel('options').title);
                                        }
                                    }
                                });
                            }
                        }]
                });
            });

            function alertMsg(obj){
                //var obj = jQuery.parseJSON(data);
                $.messager.alert(obj.title,obj.content,obj.type);
            }

            function getEmpInfo(){
                var empID= '<%=session.getAttribute("loginUserId")%>';
                //alert(empID);
                $.ajax({
                    type: "POST",
                    url: "../EmployeeServlet",
                    data: "action=loadPage&content=details&emp_id="+empID,
                    dataType: "json",
                    cache: false,
                    success: function(data){
                        $('#emp_id').html(data.emp_id);
                        $('#emp_name_menu').html(data.emp_name);
                        $('#emp_name').html(data.emp_name);
                        $('#nric').html(data.nric);
                        //$('#email').val(data.email);
                        //$('#tt').datagrid('reload');
                    }
                });
            } 

            function addTab(iframeId,title,url){  
                if ($('#tt').tabs('exists', title)){  
                    $('#tt').tabs('select', title);  
                } else {  
                    var content = '<iframe id='+iframeId+' name='+iframeId+' scrolling="auto" frameborder="0"  src="'+url+'" style="width:100%;height:100%; background:#ffffff;"></iframe>';  
                    $('#tt').tabs('add',{  
                        title:title,  
                        content:content,  
                        closable:true  
                    }); 
                    tabNo=tabNo+1;
                }  
            }  
            
            function addHomeTab(iframeId,title,url){  
                if ($('#tt').tabs('exists', title)){  
                    $('#tt').tabs('select', title);  
                } else {  
                    var content = '<iframe id='+iframeId+' name='+iframeId+' scrolling="auto" frameborder="0"  src="'+url+'" style="width:100%;height:100%; background:#ffffff;"></iframe>';  
                    $('#tt').tabs('add',{  
                        title:title,  
                        content:content,  
                        closable:false  
                    });  
                }  
            }  
            
            function closeTab(title){
                try{
                    $('#tt').tabs('close', title);
                    tabNo=tabNo-1;
                }
                catch(err){
                }
            } 
            
            function checkSession() {
                $.ajax({
                    type: "POST",
                    url: "../LoginServlet",
                    data: "action=checkSessionTimeOut",
                    dataType: "json",
                    cache: false,
                    success: function(data){
                        // window.close();
                        if(data.type=='warning'){
                            window.parent.$.messager.confirm("Session Time Out", "Your Session has been expired, click ok to go back to login, cancel to close.", function(r){
                                if (r==true){
                                    window.open('../index.jsp',"_self");
                                }else{
                                    window.close();
                                }
                            });
                        }else{
                            setTimeout("checkSession()", 65000);
                        }  
                    }
                });
            }
            
            $(document).ready(function(){
                checkSession();

                $(function(){
                    $('#dd').dialog({  
                        modal:true  
                    });
                    $('#dd').dialog('close');
                });
                    
                $('#ff').form({
                    url:'../EmployeeServlet?action=changePassword&emp_id=<%=session.getAttribute("loginUserId")%>',
                    onSubmit:function(){
                        return $(this).form('validate');
                    },
                    success:function(data){
                        var obj = jQuery.parseJSON(data); 
                        if(obj.type!='error'){
                            $('#ff').form('clear'); 
                        }
                        alertMsg(obj);    
                    }
                });
                getEmpInfo();
                //
                $('#demotree').tree('collapseAll');
                
                //
                $('#demotree').hide();
                if($('#mntree').html().trim()==""){
                    $('#demotree').show();   
                }
                
                setTimeout("addHomeTab('workspace','Workspace','../common/workspace.jsp')",1000);
            });
                
            //$('#layout').layout('collapse','east');  
            
            function showWorkSpace(){
                addHomeTab('workspace','Workspace','../common/workspace.jsp');
            }
            
            function logout(){
                window.parent.$.messager.confirm("Confirm", "Are you sure you want to logout?", function(r){
                    if (r){
                        $.ajax({
                            type: "POST",
                            url: "../LoginServlet",
                            data: "action=logOut",
                            dataType: "json",
                            cache: false,
                            success: function(data){
                                window.open('../index.jsp',"_self");
                                //window.close();
                                //$('#email').val(data.email);
                                //$('#tt').datagrid('reload');
                            }
                        });
                    }
                });
            }
            
            function showChangePassword(){
                $('#ff').form('clear'); 
                $('#dd').dialog('open');
            }
        </script>
    </head>
    <body id="layout" name="layout" class="easyui-layout layoutBody"  style="text-align:left;">
        <div region="north" border="false" style="height:40px; width:100%; text-align:left; background:#E0ECF9;">  
            <div style="position: absolute;  bottom: 0px; padding:1px; width:100%; border:0px solid #D2E0F2"> 
                &nbsp;&nbsp;
                <span style=" color: #000000; letter-spacing: -2px; font-family: Arial,Helvetica,sans-serif; font-style: normal; font-variant: normal; font-weight: normal; font-size: 28px; line-height: 28px; font-size-adjust: none; font-stretch: normal">MERLION</span>
                <span style=" color: #ff0000; letter-spacing: -1px; font-family: Arial,Helvetica,sans-serif; font-style: normal; font-variant: normal; font-weight: bold; font-size: 28px; line-height: 28px; font-size-adjust: none; font-stretch: normal">ERP</span>
                <span style="position: absolute;  top: 1px; left: 183px">&trade;</span>

                <span style="position: absolute;  right: 5px;">
                    <a href="#" class="easyui-menubutton" menu="#mm1" iconCls="icon-user"><label id="emp_name_menu" name="emp_name_menu"></label></a> 
                    <a  href="#" class="easyui-linkbutton" plain="true" onclick="logout()" iconCls="icon-logout">Logout</a>  
                    <a  href="#" class="easyui-menubutton" menu="#mm3" iconCls="icon-help">Help</a>  
                </span>

            </div>
            <div id="mm1" style="width:150px;"> 
                <div iconCls="icon-layout"><a herf="#" onclick="showWorkSpace()">Workspace</a></div>
                <div iconCls="icon-gears"><a herf="#" onclick="showChangePassword()">Change Password</a></div>  
            </div>  
            <div id="mm3" style="width:100px;">  
                <div>Help</div>  
                <div>About</div>  
            </div>  
        </div>
        <div region="west" title="&nbsp;Menu" style="width:260px; padding:5px;"> 
            
            <ul id="demotree" name="mntree" class="easyui-tree">
                <li iconCls="icon-window">
                    <span>Initial Login</span>  
                    <ul>  
                        <li iconCls="icon-box"><span><a href="javascript:void(0)" onclick="addTab('dept_mgmt','Department Management','<%=GVSYSTEMADMIN.DEPT_MGMT%>');">Department Management</a></span></li>  
                        <li iconCls="icon-box"><span><a href="javascript:void(0)" onclick="addTab('role_mgmt','Role Management','<%=GVSYSTEMADMIN.ROLE_MGMT%>');">Role Management</a></span></li>  
                        <li iconCls="icon-box"><span><a href="javascript:void(0)" onclick="addTab('emp_ac','Employee Accounts','<%=GVSYSTEMADMIN.EMP_MGMT%>');">Employee Management</a></span></li>  
                    </ul>  
                </li> 
            </ul>
              
            
            <ul id="mntree" name="mntree" class="easyui-tree"> 

                <%if (hm.get("0").equals("true")) {%>
                <li iconCls="icon-window">
                    <span>System Admin Management</span>  
                    <ul>  
                        <%if (hm.get("01").equals("true")) {%>
                        <li iconCls="icon-box"><span><a href="javascript:void(0)" onclick="addTab('dept_mgmt','Department Management','<%=GVSYSTEMADMIN.DEPT_MGMT%>');">Department Management</a></span></li>  
                        <%}%>
                        <%if (hm.get("02").equals("true")) {%>
                        <li iconCls="icon-box"><span><a href="javascript:void(0)" onclick="addTab('role_mgmt','Role Management','<%=GVSYSTEMADMIN.ROLE_MGMT%>');">Role Management</a></span></li>  
                        <%}%>
                        <%if (hm.get("00").equals("true")) {%>
                        <li iconCls="icon-box"><span><a href="javascript:void(0)" onclick="addTab('emp_ac','Employee Accounts','<%=GVSYSTEMADMIN.EMP_MGMT%>');">Employee Management</a></span></li>  
                        <%}%>
                    </ul>  
                </li> 
                <%}%>
                <%if (hm.get("1").equals("true")) {%>
                <li iconCls="icon-window">  
                    <span>Customer Relationship Management</span>  
                    <ul>  
                        <%if (hm.get("10").equals("true")) {%>
                        <li iconCls="icon-window">
                            <span>Sales Lead and Customer</span>
                            <ul>  
                                <%if (hm.get("100").equals("true")) {%>
                                <li iconCls="icon-box"><span><a  href="javascript:void(0)" onclick="addTab('sales_lead_mgmt','Sales Leads Management','<%=GVCRM.SALES_LEAD_MGMT%>');">Sales Leads Management</a></span></li>  
                                <%}%><%if (hm.get("101").equals("true")) {%>
                                <li iconCls="icon-box"><span><a   href="javascript:void(0)" onclick="addTab('customer_mgmt','Customers Management','<%=GVCRM.CUSTOMER_MGMT%>');">Customers Management</a></span></li>  

                                <%}%><%if (hm.get("102").equals("true")) {%>
                                <li iconCls="icon-box"><span><a   href="javascript:void(0)" onclick="addTab('sole_distribution_mgmt','Sole Distribution Management','<%=GVCRM.SOLE_DISTRIBUTION_MGMT%>');">Sole Distribution Management</a></span></li>  
                                <%}%><%if (hm.get("103").equals("true")) {%>
                                <li iconCls="icon-box"><span><a href="javascript:void(0)" onclick="addTab('transfer_customer_mgmt','Transfer Customers','<%=GVCRM.CUSTOMER_TRSF_MGMT%>');">Customers Transfer Management</a></span></li> 
                                <%}%>
                            </ul>
                        </li>
                        <%}%>
                        <%if (hm.get("11").equals("true")) {%>
                        <li iconCls="icon-window">
                            <span>Sales Order Processing</span>
                            <ul>  
                                <%if (hm.get("110").equals("true")) {%>
                                <li iconCls="icon-box"><span><a href="javascript:void(0)" onclick="addTab('sales_inquiry_mgmt','Sales Inquiry Management','<%=GVCRM.SALES_INQUIRY_MGMT%>');">Sales Inquiry Management</a></span></li>
                                <%}%><%if (hm.get("111").equals("true")) {%>
                                <li iconCls="icon-box"><span><a href="javascript:void(0)" onclick="addTab('sales_quotation_mgmt','Sales Quotation Management','<%=GVCRM.SALES_QUOTATION_MGMT%>');">Sales Quotation Management</a></span></li>  
                                <%}%><%if (hm.get("112").equals("true")) {%>
                                <li iconCls="icon-box"><span><a href="javascript:void(0)" onclick="addTab('purchase_order_mgmt','Purchase Order Management','<%=GVCRM.PURCHASE_ORDER_MGMT%>');">Purchase Order Management</a></span></li>  
                                <%}%><%if (hm.get("113").equals("true")) {%>
                                <li iconCls="icon-box"><span><a href="javascript:void(0)" onclick="addTab('sales_order_mgmt','Sales Order Management','<%=GVCRM.SALES_ORDER_MGMT%>');">Sales Order Management</a></span></li>  
                                <%}%><%if (hm.get("114").equals("true")) {%>
                                <li iconCls="icon-box"><span><a href="javascript:void(0)" onclick="addTab('delivery_order_mgmt','Delivery Order Management','<%=GVCRM.DELIVERY_ORDER_MGMT%>');">Delivery Order Management</a></span></li>  
                                <%}%>
                                <!--<li iconCls="icon-box"><span><a href="javascript:void(0)" onclick="addTab('sales_inquiry_mgmt_all','Sales Inquiry Management (ALL)','<%=GVCRM.SALES_INQUIRY_MGMT_ALL%>');">Sales Inquiry Management (ALL)</a></span></li>
                                <li iconCls="icon-box"><span><a href="javascript:void(0)" onclick="addTab('sales_quotation_mgmt_all','Sales Quotation Management (ALL)','<%=GVCRM.SALES_QUOTATION_MGMT_ALL%>');">Sales Quotation Management (ALL)</a></span></li> 
                                <li iconCls="icon-box"><span><a href="javascript:void(0)" onclick="addTab('purchase_order_mgmt_all','Purchase Order Management (ALL)','<%=GVCRM.PURCHASE_ORDER_MGMT_ALL%>');">Purchase Order Management (ALL)</a></span></li> 
                                <li iconCls="icon-box"><span><a href="javascript:void(0)" onclick="addTab('sales_order_mgmt_all','Sales Order Management (ALL)','<%=GVCRM.SALES_ORDER_MGMT_ALL%>');">Sales Order Management (ALL)</a></span></li>  
                                -->

                            </ul>
                        </li> 
                        <%}%>
                    </ul>  
                </li>
                <%}%>
                <%if (hm.get("2").equals("true")) {%>
                <li iconCls="icon-window">  
                    <span>Finance Resource Management</span>  
                    <ul>  
                        <%if (hm.get("20").equals("true")) {%>
                        <li iconCls="icon-box"><span><a href="javascript:void(0)" onclick="addTab('account_mgmt','Account Management','<%=GVFRM.ACCT_MGMT%>');">Account Management</a></span></li>    
                        <%}%>
                    </ul>  
                </li>
                <%}%>
                <%if (hm.get("3").equals("true")) {%>
                <li iconCls="icon-window">  
                    <span>Production Management</span>  
                    <ul>
                        <%if (hm.get("30").equals("true")) {%>
                        <li iconCls="icon-box"><span><a href="javascript:void(0)" onclick="addTab('ph_mgmt','Public Holiday Management','<%=GVPRODUCTION.PH_MGMT%>');">Public Holiday Management</a></span></li>
                        <%}%> <%if (hm.get("31").equals("true")) {%>
                        <li iconCls="icon-box"><span><a href="javascript:void(0)" onclick="addTab('sf_mgmt','Sales Forecast Management','<%=GVPRODUCTION.SF_MGMT%>');">Sales Forecast Management</a></span></li>
                        <%}%><%if (hm.get("32").equals("true")) {%>
                        <li iconCls="icon-box"><span><a href="javascript:void(0)" onclick="addTab('pp_mgmt','Production Plan Management','<%=GVPRODUCTION.PP_MGMT%>');">Production Plan Management</a></span></li> 
                        <%}%><%if (hm.get("33").equals("true")) {%>
                        <li iconCls="icon-box"><span><a href="javascript:void(0)" onclick="addTab('pl_mgmt','Production Line Management','<%=GVPRODUCTION.PL_MGMT%>');">Production Line Management</a></span></li>
                        <%}%>
                    </ul>  
                </li>
                <%}%>
            </ul> 
        </div>  
        <div border="false" region="center" style="padding:0px; overflow:hidden; ">        
            <div id="tt" class="easyui-tabs" fit="true" border="flase" style="overflow:hidden; background:#E0ECF9;" plain="true">
                <!--<div title="Workspace">  
                    <iframe id="workspace" name="workspace" scrolling="auto" frameborder="0"  src="../common/workspace.jsp" style="width:100%;height:100%; background:#ffffff;"></iframe>
                </div> -->
            </div> 
        </div>
        <div region="east" title="Messeges" style="width:200px;"></div>
        <div id="dd" title="Change Password" style="width:380px; height:250px; padding:0px;background:#fafafa;">
            <form id="ff" method="post">
                <table class="tableForms"> 
                    <tr>
                        <td class="tableForms_label">Employee ID:</td>  
                        <td class="tableForms_userInput"><label id="emp_id" name="emp_id"></label></td>  
                    </tr>
                    <tr>
                        <td class="tableForms_label">NRIC/FIN:</td>  
                        <td class="tableForms_userInput"><label id="nric" name="nric"></label></td>  
                    </tr>
                    <tr>
                        <td class="tableForms_label">Name:</td>  
                        <td class="tableForms_userInput"><label id="emp_name" name="emp_name"></label></td>  
                    </tr>
                    <tr>
                        <td class="tableForms_label">Old Password:</td>  
                        <td class="tableForms_userInput"><input type="password" id="old_password" name ="old_password" required="true"/></td>  
                    </tr>
                    <tr>
                        <td class="tableForms_label">New Password:</td>  
                        <td class="tableForms_userInput"><input type="password" id="new_password" name ="new_password" required="true"/></td>  
                    </tr>
                    <tr>
                        <td class="tableForms_label">Confirmation of Password:</td>  
                        <td class="tableForms_userInput"><input type="password" id="confirm_password" name ="confirm_password" required="true" validType="remote['../EmployeeServlet?action=validPassword','confirm_password','new_password']" invalidMessage="wdwwww"/></td>  
                    </tr>
                </table> 
                <div class="form_buttons">
                    <input type="submit" value="Change"/>
                    <input type= "reset" value="Clear" />
                </div> 
            </form>
        </div>
    </body> 
</html>