<%@page import="java.util.ArrayList"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <%@include file = "../global/global_include.jsp" %>
        <title>Add Sole Distribution</title>
        <script src="http://maps.googleapis.com/maps/api/js?sensor=false&libraries=places"
        type="text/javascript"></script>
        <% session.setAttribute("soleDistributionList", new ArrayList());%>
        <script type="text/javascript">  
            function initialize() {
                var mapOptions = {
                    center: new google.maps.LatLng(-33.8688, 151.2195),
                    zoom: 13,
                    mapTypeId: google.maps.MapTypeId.ROADMAP
                };
                var map = new google.maps.Map(document.getElementById('map_canvas'),
                mapOptions);
 
                var input = document.getElementById('region');
                var options = { 
                    //bounds: defaultBounds, 
                    types: ['geocode'] // <- See here! 
                }; 

                var autocomplete = new google.maps.places.Autocomplete(input,options);
                autocomplete.setTypes('geocode');  
                //autocomplete.bindTo('bounds', map); 
                
                var infowindow = new google.maps.InfoWindow();
                var marker = new google.maps.Marker({
                    map: map
                });
 
                google.maps.event.addListener(autocomplete, 'place_changed', function() {
                    infowindow.close();
                    var place = autocomplete.getPlace();
                    if (place.geometry.viewport) {
                        map.fitBounds(place.geometry.viewport);
                    } else {
                        map.setCenter(place.geometry.location);
                        map.setZoom(17);  // Why 17? Because it looks good.
                    }
 
                    var image = new google.maps.MarkerImage(
                    place.icon,
                    new google.maps.Size(71, 71),
                    new google.maps.Point(0, 0),
                    new google.maps.Point(17, 34),
                    new google.maps.Size(35, 35));
                    marker.setIcon(image);
                    marker.setPosition(place.geometry.location);
 
                    var address = '';
                    if (place.address_components) {
                        address = [(place.address_components[0] &&
                                place.address_components[0].short_name || ''),
                            (place.address_components[1] &&
                                place.address_components[1].short_name || ''),
                            (place.address_components[2] &&
                                place.address_components[2].short_name || '')
                        ].join(' ');
                    }
		  
                    infowindow.setContent('<div><strong>' + place.name + '</strong><br>' + address);
                    infowindow.open(map, marker);
                });
            }
            google.maps.event.addDomListener(window, 'load', initialize);
	  
            function  regionTextChange(){
                var inquirer_id=$('#inquirer_id').val();
                var input = document.getElementById('region').value;
                var geocoder = new google.maps.Geocoder();
                var addressT = '';
                //var formateAdd = '';
                var valid=false;
	  
                geocoder.geocode({ 'address': input }, function (results, status) {
                    if (status == google.maps.GeocoderStatus.OK) {
                        formateAdd=results[0].formatted_address;
                        var addNames = results[0].address_components;
                        for(var i=0; i<addNames.length; i++) {
                            if(addressT!=''){
                                if(addNames[i].long_name!=addNames[i-1].long_name){
                                    addressT=addressT+',';
                                    addressT=addressT+addNames[i].long_name;}
                            }else{
                                addressT=addressT+addNames[i].long_name; 
                            }			      
                        }
                        var typessss='';
                        var addtype = results[0].types;
                        for(var i=0; i<addtype.length; i++) {
                            if (addtype[i] == 'locality') valid=true;
                            if (addtype[i] == 'country') valid=true;
                            if (addtype[i] == 'administrative_area_level_1') valid=true;
                            if (addtype[i] == 'administrative_area_level_2') valid=true;
                            typessss=typessss+' '+addtype[i];
                        } 
                    }
                    
                    if(!valid){
                        if(addressT=="Australia"){
                            $('#region').val("Commonwealth of Australia");
                            regionTextChange();
                        }else{
                            //alert('Xnot correct!  '+ addressT+'  '+' '+formateAdd+' '+typessss);
                            alertMsgStr("Create Sole Distributor","Please enter a valid City, State(Province) or Country.","error");
                        }
                    }else{
                        //alert(addressT);    
                        $('#region').val(addressT);
                        window.parent.$.messager.confirm("Create Sole Distributor", "Add sole distributionship for <span style='color:red'>"+addressT+"</span>?", function(r){
                            if (r){
                                $.ajax({
                                    type: "POST",
                                    url:"../SoleDistributionServlet",
                                    data: "action=addDistribution"
                                        + "&region=" + addressT
                                        + "&inquirer_id=" + inquirer_id,
                                    dataType: "json",
                                    cache: false,
                                    success: function(data){
                                        if(data.type!='error'){                            
                                            $('#region').val("");
                                            $('#soleDistt').datagrid('reload');   
                                        }else{
                                            alertMsg(data);    
                                        }
                                    }
                                });  
                            }
                        });
                    }  
                });
            }    
            
            function formatMap(value,row,index){
                var place=row.region;
                var content="<img src='http://maps.googleapis.com/maps/api/staticmap?center="+place+"&size=250x100&sensor=false&markers=color:blue%7Clabel:%7C"+place+"&maptype=terrain' alt='"+place+"'></image>";
                return content;
            }
            
            function formatDel(value,row,index){
                return '<input type="button" onclick="delDistribution('+index+')" value="Delete"/>';
            }
            
            function delDistribution(index){
                window.parent.$.messager.confirm("Confirm", "Delete the selected landmark?", function(r){
                    if (r){
                        $.ajax({
                            type: "POST",
                            url: "../SoleDistributionServlet",
                            data: "action=removeDistribution&listIndex="+index,
                            dataType: "json",
                            cache: false,
                            success: function(data){
                                if(data.type!='error'){
                                    $('#soleDistt').datagrid('reload');
                                }
                                alertMsg(data);    
                            }
                        });    
                    }     
                });
            }
            
            function getCustomerInfo(){
                $.ajax({
                    type: "POST",
                    url: "../SoleDistributionServlet",
                    data: "action=loadPage"
                        + "&content=details"
                        + "&inquirer_id=" + $('#inquirer_id').val(),
                    dataType: "json",
                    cache: false,
                    success: function(data){
                        $('#inquirer_id').val(data.inquirer_id);
                        $('#company_name').html(data.company_name);
                        $('#contact_person').html(data.contact_person);
                        $('#contact_no').html(data.contact_no);
                        $('#fax_no').html(data.fax_no);
                        $('#email').html(data.email);
                        $('#company_name').html(data.company_name);
                        $('#company_add').html(data.company_add);
                        $('#remarks').html(data.remarks);  
                        $('#country').html(data.country);
                        $('#city').html(data.city);
                        $('#cust_type').html(data.cust_type.replace('_',' '));
                    }
                });
            } 
                
            $(document).ready(function(){
                $('#hideTbody').hide();
                $('#addRegion').panel('close');
                // $('#addRegion').panel('collapse');
                $('#form_buttons').hide();
                $(function(){
                    $('#dd').dialog({  
                        modal:true  
                    });
                    $('#dd').dialog('close');
                });
                $('#tt').datagrid({  
                    onClickRow:function(rowIndex,rowData){ 
                        custReset("ff","");
                        $('#inquirer_id').val(rowData.inquirer_id);
                        
                        getCustomerInfo();
                        $('#inquirer_id_tx').html(rowData.inquirer_id);
                        $('#hideTbody').show();
                        $('#addRegion').panel('open');
                        $('#dd').dialog('close');
                        //$('#addRegion').panel('expand');
                        $('#form_buttons').show();
                    }   
                }); 
            });
                
            function openDd(){
                $('#tt').datagrid('load');
                $('#dd').dialog('open');
            }
            
            function custReset(formId,content){
                if (content==""){
                    //$('#inquirer_details').hide();
                    $('#inquirer_id_tx').html("");
                    $('#inquirer_id').val("");
                    $('#company_name').html("");
                    $('#contact_person').html("");
                    $('#contact_no').html("");
                    $('#fax_no').html("");
                    $('#email').html("");
                    $('#company_name').html("");
                    $('#company_add').html("");
                    $('#remarks').html("");  
                    $('#country').html("");
                    $('#city').html("");
                    $('#cust_type').html("");
                        
                    $.ajax({
                        type: "POST",
                        url:"../SoleDistributionServlet",
                        data: "action=loadPage&content=addDistributorShipTable&reset=true",
                        dataType: "json",
                        cache: false,
                        success: function(data){
                            try{
                                if(data.type!='error'){                            
                                    $('#soleDistt').datagrid('reload');   
                                }else{
                                    alertMsg(data);    
                                }
                            }catch(err){
                            }
                        }
                    });
 
                    $('#'+formId).each(function(){
                        this.reset();
                    });

                }else{
                    window.parent.$.messager.confirm("Confirm", content, function(r){
                        if (r){
                            //$('#inquirer_details').hide();
                            $('#inquirer_id_tx').html("");
                            $('#inquirer_id').val("");
                            $('#company_name').html("");
                            $('#contact_person').html("");
                            $('#contact_no').html("");
                            $('#fax_no').html("");
                            $('#email').html("");
                            $('#company_name').html("");
                            $('#company_add').html("");
                            $('#remarks').html("");  
                            $('#country').html("");
                            $('#city').html("");
                            $('#cust_type').html("");
                        
                            $.ajax({
                                type: "POST",
                                url:"../SoleDistributionServlet",
                                data: "action=loadPage&content=addDistributorShipTable&reset=true",
                                dataType: "json",
                                cache: false,
                                success: function(data){
                                    try{
                                        if(data.type!='error'){                            
                                            $('#soleDistt').datagrid('reload');   
                                        }else{
                                            alertMsg(data);    
                                        }
                                    }catch(err){
                                    }
                                }
                            });
 
                            $('#'+formId).each(function(){
                                this.reset();
                            });
                        }
                    });
                }
            }
            
            $(function(){
                $('#ff').form({
                    url:'../SoleDistributionServlet?action=createSoleDistributor',
                    onSubmit:function(){   
                        return $(this).form('validate');
                    },
                    success:function(data){
                        //alert("asdas");
                        var obj = jQuery.parseJSON(data); 
                        if((obj.type!='error')){
                            custReset('ff',"");
                            reloadAll();
                            alertMsg(obj);
                            closeTab("");
                        }
                        alertMsg(obj);
                    }
                });
            });
            
            function onClickConfirm(){
                $('#updateSD').panel('expand');
                $('#addRoles').panel('expand');
            }
        </script>    
    </head>
    <body>
        <div style="padding:3px 2px;border-bottom:1px solid #ccc"><h2>Add Sole Distributor</h2></div>  
        <br/>
        <form id="ff" method="post">
            <input type="hidden" id="inquirer_id" name="inquirer_id" value="<%=(request.getParameter("inquirer_id") != null) ? request.getParameter("inquirer_id") : ""%>" />
            <div id="updateSD" class="easyui-panel" title="General Details:" style="padding:0px;background:#fafafa;" collapsible="true"> 
                <table class="tableForms"> 
                    <tr>  
                        <td class="tableForms_label">Customer ID:</td> 
                        <td class="tableForms_userInput">
                            <table>
                                <tr>
                                    <td width="80%">
                                        <div id="inquirer_id_tx" name="inquirer_id_tx">
                                            <%=(request.getParameter("inquirer_id") != null) ? request.getParameter("inquirer_id") : ""%>
                                        </div>                               
                                    </td>
                                    <td width="20%"><a href="javascript:void(0);" class="easyui-linkbutton" iconCls="icon-search" onclick="openDd()"></a></td>
                                </tr>
                            </table>
                        </td>
                    </tr>
                    <tbody id="hideTbody" name="hideTbody">
                        <tr>
                            <td class="tableForms_label">Company:</td>  
                            <td class="tableForms_userInput"><label id="company_name" name="company_name"></label></td>  
                        </tr> 
                        <tr>  
                            <td class="tableForms_label">Country:</td>  
                            <td class="tableForms_userInput"><label id="country" name="country"></label></td>  
                        </tr>
                        <tr>
                            <td class="tableForms_label">City:</td>  
                            <td class="tableForms_userInput"><label id="city" name="city"></label></td>  
                        </tr> 
                        <tr>  
                            <td class="tableForms_label">Contact Person:</td>  
                            <td class="tableForms_userInput"><label id="contact_person" name="contact_person"></label></td>  
                        </tr>
                        <tr>
                            <td class="tableForms_label">Contact:</td>  
                            <td class="tableForms_userInput"><label id="contact_no" name="contact_no"></label></td>  
                        </tr> 
                        <tr>  
                            <td class="tableForms_label">Fax No:</td>  
                            <td class="tableForms_userInput"><label id="fax_no" name="fax_no"></label></td>  
                        </tr>
                        <tr>
                            <td class="tableForms_label">Email:</td>  
                            <td class="tableForms_userInput"><label id="email" name="email"></label></td>  
                        </tr> 
                        <tr>  
                            <td class="tableForms_label">Address:</td>  
                            <td class="tableForms_userInput"><label id="company_add" name="company_add"></label></td>  
                        </tr>
                        <tr>
                            <td class="tableForms_label">Customer Type:</td>  
                            <td class="tableForms_userInput"><label id="cust_type" name="cust_type"></label></td> 
                        </tr>
                        <tr>  
                            <td class="tableForms_label">Remarks:</td>  
                            <td class="tableForms_userInput"><label id="remarks" name="remarks"></label></td> 
                        </tr>  
                    </tbody>
                </table> 
            </div>
            <br/>
            <div id="addRegion" class="easyui-panel" title="Add Regions:" style="padding:0px;background:#fafafa;"  collapsible="true">
                <div id="soleDistb" style="padding:5px;height:auto">  
                    <table cellpadding="5"> 
                        <tr>  
                            <td>Region:</td>  
                            <td><input id="region" size="60" type="text" name="region" /></td>
                            <td><a href="#" onClick="regionTextChange();" class="easyui-linkbutton">Add</a>
                                <div id="map_canvas"></div>
                            </td> 
                        </tr> 
                    </table>
                </div>  
                <div fit="true" style="height:300px">
                    <table id="soleDistt" class="easyui-datagrid" fit="true" style="width:100%;"  
                           url="../SoleDistributionServlet?action=loadPage&content=addDistributorShipTable"   
                           toolbar="#soleDistb"  
                           singleSelect="true" fitColumns="true"
                           rownumbers="false">  
                        <thead>  
                            <tr>  
                                <th field="region" width="20%">Region</th>  
                                <th field="map" align="center" width="70%" formatter="formatMap">Map</th>  
                                <th field="action" formatter="formatDel" width="10%">Remove</th> 
                            </tr>  
                        </thead>  
                    </table> 
                </div>
            </div>
            <br/>
            <div class="form_buttons" id="form_buttons">
                <input type="submit" onclick="onClickConfirm();" value="Create"/>
                <input type="button" onclick="custReset('ff','Clear the form?')" value="Clear" />
            </div> 
        </form>
        <div id="tb" style="padding:5px;height:auto">  
            <div>
                <table cellpadding="5">
                    <tr>
                        <td>Customer ID:</td>
                        <td><input type="text"/></td>
                        <td><a href="#" class="easyui-linkbutton" iconCls="icon-search">Search</a></td>
                    </tr>
                </table>  
            </div>  
        </div>
        <div id="dd" title="Search Wholesalers" style="width:600px; height:300px">
            <table id="tt" class="easyui-datagrid" fit="true" style="width:100%;"  
                   url="../SoleDistributionServlet?action=loadPage&content=dialog"   
                   toolbar="#tb"  
                   singleSelect="true"
                   fitColumns="true"
                   rownumbers="true">  
                <thead>  
                    <tr> 
                        <th field="inquirer_id" width="10%">ID</th>  
                        <th field="company_name" width="40%">Company Name</th>
                        <th field="country" width="25%">Country</th>
                        <th field="city" width="25%">City</th>
                    </tr>  
                </thead>  
            </table> 
        </div>
    </body>
</html>
