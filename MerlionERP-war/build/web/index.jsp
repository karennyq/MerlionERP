<%-- 
    Document   : index
    Created on : Aug 24, 2011, 12:02:37 AM
    Author     : Ken
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html style="height: 100%; overflow-x: hidden; overflow-y: hidden; ">

    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>MerlionERP&trade;</title>
        <script type="text/javascript" src="./js/jquery-easyui-1.2/jquery-1.6.min.js"></script>
        <script type="text/javascript" src="./js/jquery-easyui-1.2/jquery-ui-1.8.16.custom.min.js"></script>
        <script type="text/javascript" src="./js/jquery-easyui-1.2/jquery.easyui.min.js"></script>

        <link type="text/css" rel="stylesheet" href="./js/jquery-easyui-1.2/themes/default/easyui.css"/>
        <link type="text/css" rel="stylesheet" href="./js/jquery-easyui-1.2/themes/icon.css"/>
        <link type="text/css" rel="stylesheet" href="./css/cupertino/jquery-ui-1.8.16.custom.css"/>
        <link type="text/css" rel="stylesheet" href="./css/design.css" />

        <script type="text/javascript"> 
            function alertMsg(obj){
                window.parent.$.messager.alert(obj.title,obj.content,obj.type);
            }
                        
            function refreshCaptcha(id){
                var obj = document.getElementById(id);
                var src = obj.src;
                var pos = src.indexOf('?');
                if (pos >= 0) {
                    src = src.substr(0, pos);   
                }
                var date = new Date();
                obj.src = src + '?v=' + date.getTime();
                return false;
            }
            
            $(function(){
                $('#ff').form({
                    url:"./LoginServlet?action=login",
                    onSubmit:function(){
                        return $(this).form('validate');
                    },
                    success:function(data){
                        var obj = jQuery.parseJSON(data); 
                        if(obj.content=='Success'){
                            $('#ff').form('clear'); 
                            window.open('global/global_layout.jsp',"_self");
                       
                        }else{
                            $('#pwd').val('');
                            $('#verificationExist').attr('value','true');
                            $('#verify').attr({
                                value: '',
                                required: 'true'
                            });
                           
                            $("#login").attr("class", "centered2");
                            
                            $('#verification').show();
                            $('#pp').panel('maximize');
                            
                            refreshCaptcha('captcha');
                            alertMsg(obj);
                        }
                    }
        
                });
            });
            
           /* function popup(url) 
            {
                params  = 'width='+screen.width;
                params += ', height='+screen.height;
                params += ', top=0, left=0'
                params += ', fullscreen=yes';
                params+=',directories=0, \
                location=0, \
                menubar=0, \
                resizable=1, \
                scrollbars=1, \
                status=0, \
                toolbar=0'
                closeMe();
                newwin=window.open(url,'mainwin', params);
                if (window.focus) {newwin.focus()}
                return false;
            }
            
            function closeMe()
            {
                var win=window.open("","_self");
                win.close();
            }*/
            
            
            $(document).ready(function(){
                $('#verification').hide();
            }); 
        </script>
    </head>
    <body style="background:#E0ECF9">
        <div name="login" id="login" class="centered">
            <span style=" color: #000000; letter-spacing: -2px; font-family: Arial,Helvetica,sans-serif; font-style: normal; font-variant: normal; font-weight: normal; font-size: 28px; line-height: 28px; font-size-adjust: none; font-stretch: normal">MERLION</span>
            <span style=" color: #ff0000; letter-spacing: -1px; font-family: Arial,Helvetica,sans-serif; font-style: normal; font-variant: normal; font-weight: bold; font-size: 28px; line-height: 28px; font-size-adjust: none; font-stretch: normal">ERP <span style="font-size: 14px; position: absolute; color: #000000; top: -5px; left: 175px">&trade;</span></span>
            <div id="pp" class="easyui-panel" title="Log In" fit="true" style=" overflow:hidden; width:100%;padding:10px;background:#fafafa;"  
                 closable="false"  
                 collapsible="false" maximizable="false">
                <form id="ff" method="post"> 
                    <table>
                        <tr>
                            <td colspan="3">Please enter your username and password to continue.</td>
                        </tr>
                        <tr>
                            <td></td>
                            <td></td>
                            <td></td>
                        </tr>
                        <tr>
                            <td></td>
                            <td></td>
                            <td></td>
                        </tr>
                        <tr>  
                            <td>Username:</td>  
                            <td colspan="2"><input style="width:98%" size="30" class="easyui-validatebox" name="username" type="text" required="true" /></td>  
                        </tr>  
                        <tr>  
                            <td>Password:</td>  
                            <td colspan="2"><input style="width:98%" size="30" class="easyui-validatebox" id="pwd" name="pwd" type="password" required="true" /></td>  
                        </tr>  
                        <tr>
                            <td><input type="hidden" id="verificationExist" name="verificationExist" value="false"/></td>
                            <td></td>
                            <td></td>
                        </tr>
                        <tbody id="verification" name="verification" hidden="true">
                            <tr>
                                <td></td>
                                <td style="width:9px" ><img id="captcha" name="captcha" src="Captcha.jpg"/>
                                </td>
                                <td style="text-align: left"><a href="javascript:void(0);" class="easyui-linkbutton" plain="true" iconCls="icon-reload" onclick="return refreshCaptcha('captcha');"></a>
                                </td>
                            </tr>
                            <tr>
                                <td>Verification Code:</td>
                                <td colspan="2"><input style="width:98%" size="30" class="easyui-validatebox" type="text" id="verify" name="verify" /></td>
                            </tr>
                        </tbody>
                        <tr>     
                            <td style="text-align: left"><a href="javascript:void(0);" class="easyui-linkbutton" plain="true" onclick="window.open('global/global_layout.jsp','_self');">&ensp;&ensp;&ensp;</a></td>
                            <td></td>
                            <td style="text-align: right"><input type="submit" value="Login" /></td>  
                        </tr>  
                    </table>  
                </form>
            </div>
        </div>
    </body>
</html>

