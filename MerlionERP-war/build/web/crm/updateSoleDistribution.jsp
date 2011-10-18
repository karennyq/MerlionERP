<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <%@include file = "../global/global_include.jsp" %>
        <title>Manage Sole Distribution</title>
        <script src="http://maps.googleapis.com/maps/api/js?sensor=false&libraries=places"
        type="text/javascript"></script>
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
                        $('#region').val(addressT);
                        window.parent.$.messager.confirm("Create Sole Distributor", "Add sole distributionship for <span style='color:red'>"+addressT+"</span>?", function(r){
                            if (r){
                                $.ajax({
                                    type: "POST",
                                    url:"../SoleDistributionServlet",
                                    data: "action=addUpdateDistribution&region="+addressT+"&inquirer_id="+inquirer_id,
                                    dataType: "json",
                                    cache: false,
                                    success: function(data){
                                        if(data.type!='error'){                            
                                            $('#region').val("");
                                            $('#soleDistt').datagrid('reload');
                                            try{parent.sole_distribution_mgmt.$('#tt').datagrid('reload');}
                                            catch(err){} 
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
           
            function getCustomerInfo(){
                $.ajax({
                    type: "POST",
                    url: "../SoleDistributionServlet",
                    data: "action=loadPage&content=details&inquirer_id="+$('#inquirer_id').val(),
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
                        $('#cust_type').html(data.cust_type.replace('_',' '))
                    }
                });
            }
            
            $(document).ready(function(){
                if($('#inquirer_id').val()!=null||$('#inquirer_id').val()==""){
                    getCustomerInfo();
                }
            });
            
            function formatDel(value,row,index){
                return '<input type="button" onclick="delDistribution('+index+','+row.sole_dis_id+')" value="Delete"/>';
            }
            
            function delDistribution(index,id){
                if(index==0){
                    window.parent.$.messager.confirm("Confirm", "Delete the selected landmark? <span style='color: red'>By Removing This Region The Wholesaler Will No Longer Be A Sole Distributor.</span>", function(r){
                        if (r){
                            $.ajax({
                                type: "POST",
                                url: "../SoleDistributionServlet",
                                data: "action=removeUpdateDistribution&sole_dis_id="+id,
                                dataType: "json",
                                cache: false,
                                success: function(data){
                                    if(data.type!='error'){
                                        $('#soleDistt').datagrid('reload');
                                        try{parent.sole_distribution_mgmt.$('#tt').datagrid('reload');}
                                        catch(err){} 
                                    }
                                    alertMsg(data);    
                                }
                            });    
                        }    
                    }); 
                }else{
                    window.parent.$.messager.confirm("Confirm", "Delete the selected landmark?", function(r){
                        if (r){
                            $.ajax({
                                type: "POST",
                                url: "../SoleDistributionServlet",
                                data: "action=removeUpdateDistribution&sole_dis_id="+id,
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
            }
        </script>    
    </head>
    <body>
        <div style="padding:3px 2px;border-bottom:1px solid #ccc"><h2>Manage Sole Distribution (Customer ID:  <%=(request.getParameter("inquirer_id") != null) ? request.getParameter("inquirer_id") : ""%>)</h2></div>  
        <br/>
        <form id="ff" method="post">
            <input type="hidden" id="inquirer_id" name="inquirer_id" value="<%=(request.getParameter("inquirer_id") != null) ? request.getParameter("inquirer_id") : ""%>" />
            <div id="updateSD" class="easyui-panel" title="General Details:" style="padding:0px;background:#fafafa;" collapsible="true" > 
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
                        <td  class="tableForms_userInput"><label id="contact_no" name="contact_no"></label></td>  
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
                </table> 
            </div>
            <br/>
            <div id="addRegion" class="easyui-panel" title="Add Regions:" style="padding:0px;background:#fafafa;"  collapsible="true">
                <div id="soleDistb" style="padding:5px;height:auto">  
                    <table cellpadding="5"> 
                        <tr>  
                            <td>Region:</td>  
                            <td><input id="region" size="60" type="text" name="region"/></td>
                            <td>
                                <a href="#" onClick="regionTextChange();" class="easyui-linkbutton">Add</a>
                                <div id="map_canvas"></div> 
                            </td> 
                        </tr> 

                    </table>

                </div>  

                <div fit="true" style="height:400px">
                    <table id="soleDistt" class="easyui-datagrid" fit="true" style="width:100%;"  
                           url="../SoleDistributionServlet?action=loadPage&content=updateDistributorShipTable&inquirer_id=<%=(request.getParameter("inquirer_id"))%>"   
                           toolbar="#soleDistb"  
                           singleSelect="true" fitColumns="true"
                           rownumbers="false" paging="true">  
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
        </form>
    </body>
</html>
