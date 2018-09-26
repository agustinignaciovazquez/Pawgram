<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<!DOCTYPE html>

<head>

	<meta charset="UTF-8">
	<title>Pawgram - Create Post</title>

		<link href="<c:url value="/resources/css/all.css"/>" rel="stylesheet" id="font-awesome">
  <link href="<c:url value="/resources/css/bootstrap.min.css"/>" rel="stylesheet" id="bootstrap-css">
  <link rel="stylesheet" href="<c:url value="/resources/css/pawgramin.css"/>">

  
  <script src="<c:url value="/resources/js/jquery-3.3.1.min.js"/>"></script>
  <script src="<c:url value="/resources/js/bootstrap.min.js"/>"></script>
  <script src="<c:url value="/resources/js/pawgram.js"/>"></script>

   

</head>


<body>
	
	<%@include file="includes/header.jsp"%>   
	<c:url value="/post/create/category/${currentCategory}" var="postPath"/>
	
		<div class="container-fluid titzon">
			<div class="row">
				<div class="col-md-3"></div>
				<div class="text titsec">Crear anuncio</div>
			</div>
		</div>
		<form:form modelAttribute="uploadForm" action="${postPath}" method="post" enctype="multipart/form-data">
		<div class="row">

			<div class="col-md-2"></div>
			<div class="col-md-3">
				<div class="fistcontainer">
					<div class="row uspaced60">
						<div class="text formtitle center">Selecciona una especie:</div>
					</div>
					<div class="row uspaced20">
						<div class="container optionblock2 radio-group ">
								
								<c:forEach items="${pets}" var="pet">
									<div class="container formoption uspaced50 bspaced50 radio" data-value="<c:out value="${pet.lowerName}"/>">
										<div class="row">
											<div class="col-md-3">
												<img src="<c:url value="/resources/img/${pet.lowerName}.jpg"/>" class="icon">
											</div>
											<div class="col-md-9">
												<div class="text formtext"><spring:message code="specie.${pet.lowerName}"/></div>
											</div>	
										</div>	
									</div>
								</c:forEach>
								
								<form:hidden path="pet" id="radio-value" name="radio-value" />
						</div>
					</div>
				</div>
			</div>
			<div class="col-md-2"></div>
			<div clas="col-md-3">
				<div class=" container step">
					<div class="row uspaced60">
						<div class="text formtitle center">Selecciona un sexo:</div>
					</div>
					<div class="row uspaced20">
						
						<div class="container optionblock2 radio-group ">
								<div class="container formoption uspaced50 bspaced50 radio" data-value="true"> 
									<div class="row">
										<div class="col-md-3">
											<i class="fas fa-mars formicon formiconmale "></i>
										</div>
										<div class="col-md-9">
											<div class="text formtext"><spring:message code="gender.male" /></div>
										</div>	
									</div>	
								</div>
								<div class="container formoption  uspaced50 bspaced50 radio" data-value="false">
									<div class="row">
										<div class="col-md-3">
											<i class="fas fa-venus formicon formiconfemale "></i>
										</div>
										<div class="col-md-9">
											<div class="text formtext"><spring:message code="gender.female"/></div>
										</div>	
									</div>	
								</div>
								<form:hidden path="is_male"  id="radio-value" name="radio-value" />
						</div>
					</div>
				</div>
			</div>
		</div>

		 <div class="row uspaced60">
        	<div class="text formtitle center">Selecciona el lugar del evento:</div>
        </div>
		<div class="container">
            <div class="row">
                <input id="searchInput" class="controls" type="text" placeholder="Enter a location">
                <div id="map"></div>
                <!--<ul id="geoData">
                    <li>Direccion: <span id="location"></span></li>
                    <li>Codigo Postal: <span id="postal_code"></span></li>
                    <li>Pais: <span id="country"></span></li>
                    <li>Latitud: <span id="lat"></span></li>
                    <li>Longitud: <span id="lon"></span></li>
                </ul>-->
                <form:hidden path="latitude" class="form-control" id="lat" name="latitude"  />
                <form:hidden path="longitude" class="form-control" id="lon" name="longitude" />
            </div>
            
        </div>
		
        <div class="row uspaced60">
        	<div class="text formtitle center">Completa los datos:</div>
        </div>
        <div class="row">
        		<div class=" myformcontainer center">
				    <div class="form-area">  
				        <form role="form">
				        	<div class=" text subformtitle uspaced20">
				        		Nombre de tu mascota:
				        	</div>
				    		<div class="form-group">
								<form:input path="title" type="text" class="form-control" id="name" name="name" placeholder="eg: Tomy"  />
								<form:errors path="title" cssClass="form-error" element="p"/>
							</div>
							<div class=" text subformtitle">
				        		Fecha del evento:
				        	</div>
							<div class="form-group">
								<form:input path="event_date" type="date" class="form-control" id="date" name="date"  />
								<form:errors path="event_date" cssClass="form-error" element="p"/>
							</div>
							<div class=" text subformtitle">
				        		Titulo:
				        	</div>
							<div class="form-group">
								<form:input path="contact_phone" type="text" class="form-control" id="mobile" name="mobile" placeholder="Escribe tu titulo aqui..."  />
								<form:errors path="contact_phone" cssClass="form-error" element="p"/>
							</div>
							<div class=" text subformtitle">
				        		Descripcion:
				        	</div>
				            <div class="form-group">
				            <form:textarea path="description" class="form-control" type="textarea" id="message" placeholder="Escribe tu descripcion aqui..." maxlength="140" rows="7"></form:textarea>
				            <form:errors path="description" cssClass="form-error" element="p"/>
				               <!--<span class="help-block"><p id="characterLeft" class="help-block ">llegaste al limite</p></span>-->                    
				            </div>
				            <div class=" text subformtitle">
				        		Selecciona al menos una imagen:
				        	</div>
				            <input type="file" name="files" /><br/>

				            <div class="row uspaced50">
				            	<div class="lspaced15">
				            		<button  type="button"><i class="fa fa-plus"></i></button>	
				            	</div>
				            </div>

				            <div class="row uspaced50">
				            	<div class="col-md-10">
				            		<input type="file" name="files" /><br/>
				            	</div>
				            	 <button  type="button"><i class="fa fa-minus"></i></button>
				            </div>
				            
				            

				        </form>
				    </div>  
        	</div>
        </div>



        <div class="row">
        	<div class="center">
        		<button id="submit" name="submit" class="btn btn-lg btn-success pull-right">Crear anuncio</button>
        	</div>
        	
        </div>
    </form:form>
    <script>
    	
   function initMap() {
    var map = new google.maps.Map(document.getElementById('map'), {
      center: {lat: -33.8688, lng: 151.2195},
      zoom: 13
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

    }
    </script>
     <script async defer
 	 src="https://maps.googleapis.com/maps/api/js?libraries=places&key=AIzaSyAsqLEThGLQ6T4Ayox_K7Em1S4DuAT-wm8&callback=initMap">
 	 </script>

	<!--FOOTER-->
    <div class="row uspaced60"></div>
    <div class="container-fluid footer">
        <div class="text footertext">© 2018 Todos los derechos reservados pawgram.org</div> 
    </div>
    <!--FOOTER-->




</body>