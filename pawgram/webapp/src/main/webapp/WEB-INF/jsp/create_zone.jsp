<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<head>

  <meta charset="UTF-8">
  <title><spring:message code="pageName"/>-<spring:message code="title.addzone"/></title> 
  

  <link href="<c:url value="/resources/css/all.css"/>" rel="stylesheet" id="font-awesome">
  <link href="<c:url value="/resources/css/bootstrap.min.css"/>" rel="stylesheet" id="bootstrap-css">
  <link rel="stylesheet" href="<c:url value="/resources/css/pawgramin.css"/>">

  
  <script src="<c:url value="/resources/js/jquery-3.3.1.min.js"/>"></script>
  <script src="<c:url value="/resources/js/bootstrap.min.js"/>"></script>
  <script src="<c:url value="/resources/js/pawgram.js"/>"></script>

</head>

<body>
  
    <%@include file="includes/header.jsp"%>
    
	<div class="container-fluid titzon">
	 	<div class="row">
			<div class="col-md-3"></div>
			<div class="text titsec"><spring:message code="add.zone"/></div>
		</div>
	</div>

	<div class="row uspaced5">
		<div class="col-md-3"></div>
        <div class="text zonetext zoneel1 rspaced2"><spring:message code="select.place"/></div>
    </div>

    <div class="row">
    	<div class="col-md-3"></div>
        <input id="searchInput" class="controls" type="text" placeholder="Enter a location">
        <div id="map" class="mapround"></div>
        <!--<ul id="geoData">
            	<li>Direccion: <span id="location"></span></li>
                <li>Codigo Postal: <span id="postal_code"></span></li>
                <li>Pais: <span id="country"></span></li>
                <li>Latitud: <span id="lat"></span></li>
                <li>Longitud: <span id="lon"></span></li>
            </ul>-->
    </div>


	<div class="row uspaced20">
		<div class="col-md-3"></div>
		<div class="col-md-6">
			<div class="text zonetext zoneel1 rspaced2"><spring:message code="select.range"/></div>
		</div>
		<div class="col-md-3"></div>      
    </div>

    <div class="row">
    	<div class="range range-danger center">
            <input type="range" name="range" min="1" max="15" value="5" onchange="rangeDanger.value=value + ' <spring:message code="km"/>' ">
            <output id="rangeDanger">5 <spring:message code="km"/></output>
        </div>
    </div>

    <div class="row uspaced60">
    	 <spring:message code="savezone" var="savezone"/>
       	<div class="center">
        	<input id="submit" type="submit" name="submit" class="btn btn-lg btn-success pull-right" value="${savezone}"></input>
        </div>
        	
    </div>
    

    <script>
    	function initMap() {
   	
		   	if(isNaN(latitude) || isNaN(longitude)){
		   		latitude = -34.6031933;
		   		longitude = -58.3677853;
		   	}else{
		   		var uluru = {lat: latitude, lng: longitude};
		   	}
		    var map = new google.maps.Map(document.getElementById('map'), {
		      center: {lat: latitude, lng: longitude},
		      zoom: 13
		    });
		    var input = document.getElementById('searchInput');
		    map.controls[google.maps.ControlPosition.TOP_LEFT].push(input);

		    var autocomplete = new google.maps.places.Autocomplete(input);
		    autocomplete.bindTo('bounds', map);

		    var infowindow = new google.maps.InfoWindow();
		    if (typeof uluru !== 'undefined') {
		    	var marker = new google.maps.Marker({position: uluru, map: map});
		    }else{
			    var marker = new google.maps.Marker({
			        map: map,
			        anchorPoint: new google.maps.Point(0, -29)
			    });
			}

		    autocomplete.addListener('place_changed', function() {
		        infowindow.close();
		        marker.setVisible(false);
		        var place = autocomplete.getPlace();
		        if (!place.geometry) {
		            window.alert("Autocomplete's returned place contains no geometry");
		            return;
		        }
		  
		        // If the place has a geometry, then present it on a map.
		        if (place.geometry.viewport) {
		            map.fitBounds(place.geometry.viewport);
		        } else {
		            map.setCenter(place.geometry.location);
		            map.setZoom(17);
		        }
		        marker.setIcon(({
		            url: place.icon,
		            size: new google.maps.Size(71, 71),
		            origin: new google.maps.Point(0, 0),
		            anchor: new google.maps.Point(17, 34),
		            scaledSize: new google.maps.Size(35, 35)
		        }));
		        marker.setPosition(place.geometry.location);
		        marker.setVisible(true);
		    
		        var address = '';
		        if (place.address_components) {
		            address = [
		              (place.address_components[0] && place.address_components[0].short_name || ''),
		              (place.address_components[1] && place.address_components[1].short_name || ''),
		              (place.address_components[2] && place.address_components[2].short_name || '')
		            ].join(' ');
		        }
		    
		        infowindow.setContent('<div><strong>' + place.name + '</strong><br>' + address);
		        infowindow.open(map, marker);
		      
		        //Location details
		       /* for (var i = 0; i < place.address_components.length; i++) {
		            if(place.address_components[i].types[0] == 'postal_code'){
		                document.getElementById('postal_code').innerHTML = place.address_components[i].long_name;
		            }
		            if(place.address_components[i].types[0] == 'country'){
		                document.getElementById('country').innerHTML = place.address_components[i].long_name;
		            }
		        }*/
		        //document.getElementById('location').innerHTML = place.formatted_address;
		        document.getElementById('lat').value  = place.geometry.location.lat();
		        document.getElementById('lon').value  = place.geometry.location.lng();

		    });
   	</script> 	

    <script async defer src="https://maps.googleapis.com/maps/api/js?key=key=AIzaSyAsqLEThGLQ6T4Ayox_K7Em1S4DuAT-wm8AIzaSyAsqLEThGLQ6T4Ayox_K7Em1S4DuAT-wm8&callback=initMap">
    </script>


    <!--FOOTER-->
    <div class="row uspaced60"></div>
    <div class="container-fluid footer">
        <div class="text footertext"><spring:message code="footer"/></div> 
    </div>
    <!--FOOTER-->




</body>