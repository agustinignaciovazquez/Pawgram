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

    <c:url value="/my_zones/create/process" var="postPath" />
    <form:form modelAttribute="searchZoneForm" class="comment-form" action="${postPath}" method="post">
    <div class="row uspaced15">
    	<div class="col-md-3"></div>
        <div class="col-md-6">
            <input id="searchInput" onkeypress="return noenter()" class="controls" type="text" placeholder="Enter a location">
            <form:errors path="latitude" element="p" cssClass="form-error"/>
            <div id="map"></div>
            <form:hidden path="latitude" class="form-control" id="lat" name="latitude"  />
            <form:hidden path="longitude" class="form-control" id="lon" name="longitude" />
        <!--<ul id="geoData">
            	<li>Direccion: <span id="location"></span></li>
                <li>Codigo Postal: <span id="postal_code"></span></li>
                <li>Pais: <span id="country"></span></li>
                <li>Latitud: <span id="lat"></span></li>
                <li>Longitud: <span id="lon"></span></li>
            </ul>-->
        </div>    
    </div>


	<div class="row uspaced5">
		<div class="col-md-3"></div>
		<div class="col-md-6">
			<div class="text zonetext zoneel1 rspaced2"><spring:message code="select.range"/></div>
		</div>
		<div class="col-md-3"></div>      
    </div>

    <div class="row">
    	<div class="range range-danger center">
    		<spring:message code="km" var="kmmsg" />
            <form:input path="range" type="range" name="range" min="1" max="15" onchange="rangeDanger.value=value + '${kmmsg}'" />
            <output id="rangeDanger"> <c:out value="${searchZoneForm.range}"/> <spring:message code="km"/></output>
        </div>
    </div>

    <div class="row uspaced60">
    	 <spring:message code="savezone" var="savezone"/>
       	<div class="center">
        	<input id="submit" type="submit" name="submit" class="btn btn-lg btn-success" value="${savezone}"></input>
        </div>
        	
    </div>

    </form:form>

    <script>    
   function initMap() {
   	
 
	latitude = -34.6031933;
	longitude = -58.3677853;

    var map = new google.maps.Map(document.getElementById('map'), {
      center: {lat: latitude, lng: longitude},
      zoom: 14
    });

    var input = document.getElementById('searchInput');
    map.controls[google.maps.ControlPosition.TOP_LEFT].push(input);

    var autocomplete = new google.maps.places.Autocomplete(input);
    autocomplete.bindTo('bounds', map);

    var infowindow = new google.maps.InfoWindow();
   
    var marker = new google.maps.Marker({
        map: map,
        anchorPoint: new google.maps.Point(0, -29)
    });
	

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
    	var cityCircle = new google.maps.Circle({
            strokeColor: '#FF0000',
            strokeOpacity: 0.8,
            strokeWeight: 2,
            fillColor: '#FF0000',
            fillOpacity: 0.35,
            map: map,
            center: place.geometry.location,
            radius: range.value*1000
        });
 
        document.getElementById('lat').value  = place.geometry.location.lat();
        document.getElementById('lon').value  = place.geometry.location.lng();

    });

    }
    </script>	

    <script async defer
 	 src="https://maps.googleapis.com/maps/api/js?libraries=places&key=AIzaSyAsqLEThGLQ6T4Ayox_K7Em1S4DuAT-wm8&callback=initMap">
 	 </script>


    <!--FOOTER-->
    <div class="row uspaced60"></div>
    <div class="container-fluid footer">
        <div class="text footertext"><spring:message code="footer"/></div> 
    </div>
    <!--FOOTER-->




</body>