<%-- 
    Document   : global
    Created on : Aug 14, 2011, 11:16:05 AM
    Author     : Ken
--%>
<%
    /* Data Attributes */

    String userIdentification = (String) request.getSession().getAttribute("loginUserId").toString();
    if (userIdentification == null) {
        userIdentification = "";
    }

    if (userIdentification.isEmpty()) {
        String redirectURL = "../error.jsp";
        response.sendRedirect(redirectURL);
    }

   


%>
<script type="text/javascript" src="../js/jquery-easyui-1.2/jquery-1.6.min.js"></script>
<script type="text/javascript" src="../js/jquery-easyui-1.2/jquery-ui-1.8.16.custom.min.js"></script>
<script type="text/javascript" src="../js/jquery-easyui-1.2/jquery.easyui.min.js"></script>

<link type="text/css" rel="stylesheet" href="../js/jquery-easyui-1.2/themes/default/easyui.css"/>
<link type="text/css" rel="stylesheet" href="../js/jquery-easyui-1.2/themes/icon.css"/>
<link type="text/css" rel="stylesheet" href="../css/cupertino/jquery-ui-1.8.16.custom.css"/>
<link type="text/css" rel="stylesheet" href="../css/design.css" />

<script type="text/javascript"> 
    
    
    
    
    //for json
    function alertMsg(obj){
        //var obj = jQuery.parseJSON(data);
        window.parent.$.messager.alert(obj.title,obj.content,obj.type);
    }
    
    //for normal string
    function alertMsgStr(title,content,type){
        window.parent.$.messager.alert(title,content,type);
    }
    

    function confirmReset(formId,content){
        window.parent.$.messager.confirm("Confirm", content, function(r){
            if (r){
                $('#'+formId).each(function(){
                    this.reset();
                });
            }
        });
    }
    
    //function closeTab(){
      //  parent.closeTab("<%=request.getParameter("tabTitle")%>");
    //}
    
    function closeTab(tabTitle){
        if(tabTitle==""){
            parent.closeTab("<%=request.getParameter("tabTitle")%>");
        }else{
            parent.closeTab(tabTitle);
        }
        
    }
    
    function comboboxFomatter(row){
        
        if(row.id==0){
            return "&nbsp";
        }else{
            return "<span style='cursor:pointer'>"+row.text+"</span>";
        }
        
    }
    
   /* function reloadAllDataGrid(){
        
        //SysAdmin
        try{parent.emp_ac.$('#tt').datagrid('reload');}
        catch(err){}  
        try{f.role_mgmt.$('#tt').datagrid('reload');}
        catch(err){} 
        try{f.dept_mgmt.$('#tt').datagrid('reload');}
        catch(err){} 
       
        //FRM
        try{f.account_mgmt.$('#tt2').datagrid('reload');}
        catch(err){} 
        
        //CRM
        try{parent.sole_distribution_mgmt.$('#tt').datagrid('reload');}
        catch(err){} 
        try{
            f.customer_mgmt.$('#tt').datagrid('reload');
        }catch(err){}
        try{
            f.sales_lead_mgmt.$('#tt').datagrid('reload');
        }catch(err){}
        try{parent.sales_inquiry_mgmt.$('#tt').datagrid('reload');}
        catch(err){} 
        try{
            parent.sales_quotation_mgmt.$('#tt').datagrid('reload');
        }catch(err){} 
        
    }*/
    
    function reloadAll(){
        var arrFrame=parent.frames;
           
        for( i=0;i<arrFrame.length;i++){
            arrFrame[i].$('#tt').datagrid('reload');
        }
           
    }
</script>
