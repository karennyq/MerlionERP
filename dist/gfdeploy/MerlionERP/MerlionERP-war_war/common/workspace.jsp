<%-- 
    Document   : workspace
    Created on : Aug 23, 2011, 11:48:03 PM
    Author     : Ken
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
        <title>Workspace</title>
        <%@include file = "../global/global_include.jsp" %>
        <script type="text/javascript" src="../js/jquery-easyui-portal/jquery.portal.js"></script>
        <link rel="stylesheet" type="text/css" href="../js/jquery-easyui-portal/portal.css">
        <script type="text/javascript">
            $(document).ready(function(){
                $(function(){
                    $('#pp').portal({
                        border:false
                    });
                });
                
                window.onbeforeunload = function(){
                    //alert("save state!");
                };
                
                $('#cc').calendar({  
                    width:180,  
                    height:150,  
                    current:new Date()  
                });  
            });
            
            function add(){
                for(var i=0; i<3; i++){
                    var p = $('<div/>').appendTo('body');
                    p.panel({
                        title:'Title'+i,
                        content:'<div style="padding:5px;">Content'+(i+1)+'</div>',
                        height:100,
                        closable:true,
                        collapsible:true
                    });
                    $('#pp').portal('add', {
                        panel:p,
                        columnIndex:i
                    });
                }
                $('#pp').portal('resize');
            }
            
            function remove(){
                $('#pp').portal('remove',$('#pgrid'));
                $('#pp').portal('resize');
            }
            
            //$(window).unload( function () { alert("save state"); } );
            </script>
    </head>
    <body>
        <div id="pp" class="target" fit="true" style="overflow:hidden; width:100%;height:600px">
            <div>
                <div title="Clock" style="text-align:center;background:#f3eeaf;height:150px;padding:5px;">
                    <object classid="clsid:D27CDB6E-AE6D-11cf-96B8-444553540000" codebase="http://download.macromedia.com/pub/shockwave/cabs/flash/swflash.cab#version=6,0,29,0" width="100" height="100">
                        <param name="movie" value="http://www.respectsoft.com/onlineclock/analog.swf">
                        <param name=quality value=high>
                        <param name="wmode" value="transparent">
                        <embed src="http://www.respectsoft.com/onlineclock/analog.swf" width="100" height="100" quality=high pluginspage="http://www.macromedia.com/shockwave/download/index.cgi?P1_Prod_Version=ShockwaveFlash" type="application/x-shockwave-flash" wmode="transparent"/>
                    </object>
                </div>
                <div title="Calendar" collapsible="true" closable="true" style="text-align:center;height:200px;padding:5px;">
                    <div id="cc" style="width:180px;height:180px;"></div> 
                </div>
            </div>
            <div>
                <div title="Searching" iconCls="icon-search" closable="true" style="height:100px;padding:10px;">
                    <input>
                    <a href="#" class="easyui-linkbutton">Go</a>
                    <a href="#" class="easyui-linkbutton">Search</a>
                </div>
                <div title="Graph" closable="true" style="height:200px;text-align:center;">
                    <img height="160" src="http://knol.google.com/k/-/-/3mudqpof935ww/ip4n5y/web-graph.png"/>
                </div>
            </div>
        </div>
    </body>
</html>
