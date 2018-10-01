<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<!DOCTYPE html>

<head>

	<meta charset="UTF-8">
	<title><spring:message code="pageName"/> - <spring:message code="title.createpost"/></title>

		<link href="<c:url value="/resources/css/all.css"/>" rel="stylesheet" id="font-awesome">
  <link href="<c:url value="/resources/css/bootstrap.min.css"/>" rel="stylesheet" id="bootstrap-css">
  <link rel="stylesheet" href="<c:url value="/resources/css/pawgramin.css"/>">

  
  <script src="<c:url value="/resources/js/jquery-3.3.1.min.js"/>"></script>
  <script src="<c:url value="/resources/js/bootstrap.min.js"/>"></script>
  <script src="<c:url value="/resources/js/pawgram.js"/>"></script>

   

</head>


<body>
	
	<%@include file="includes/header.jsp"%>   
	<c:url value="/post/create/category/${currentCategory}/process" var="postPath"/>
	
		<div class="container-fluid titzon">
			<div class="row">
				<div class="col-md-3"></div>
				<div class="text titsec"><spring:message code="createpost"/></div>
			</div>
		</div>
		<form:form modelAttribute="uploadForm" action="${postPath}" method="post" enctype="multipart/form-data">
		<div class="row">

			<div class="col-md-2"></div>
			<div class="col-md-3">
				<div class="fistcontainer">
					<div class="row uspaced60">
						<div class="text formtitle center"><spring:message code="select.specie"/></div>
					</div>
					<div class="row uspaced20">
						<div class="container optionblock2 radio-group ">
								<form:errors path="pet" cssClass="form-error" element="p"/>
								<c:forEach items="${pets}" var="pet">
									<div class="container formoption uspaced50 bspaced50 radio" data-value="<c:out value="${pet}"/>">
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
						<div class="text formtitle center"><spring:message code="select.gender"/></div>
					</div>
					<div class="row uspaced20">
						
						<div class="container optionblock2 radio-group ">
							<form:errors path="is_male" cssClass="form-error" element="p"/>
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
        	<div class="text formtitle center"><spring:message code="select.place"/></div>
        </div>
		<div class="container">
            <div class="row">
                <input id="searchInput" onkeypress="return noenter()" class="controls" type="text" placeholder="<spring:message code="enter.location"/>">
                <div id="map"></div>
                <!--<ul id="geoData">
                    <li>Direccion: <span id="location"></span></li>
                    <li>Codigo Postal: <span id="postal_code"></span></li>
                    <li>Pais: <span id="country"></span></li>
                    <li>Latitud: <span id="lat"></span></li>
                    <li>Longitud: <span id="lon"></span></li>
                </ul>-->
                <form:errors path="latitude" cssClass="form-error" element="p"/>
                <form:hidden path="latitude" class="form-control" id="lat" name="latitude"  />
                <form:hidden path="longitude" class="form-control" id="lon" name="longitude" />
            </div>
            
        </div>
		
        <div class="row uspaced60">
        	<div class="text formtitle center"><spring:message code="complete.data"/></div>
        </div>
        <div class="row">
        		<div class=" myformcontainer center">
				    <div class="form-area">  
				        	<div class=" text subformtitle uspaced20">
				        		<spring:message code="post.title"/>
				        	</div>
				    		<div class="form-group">
				    			<spring:message code="write.title" var="writetitle"/>
								<form:input path="title" type="text" class="form-control" id="name" name="name" placeholder="${writetitle}"  />
								<form:errors path="title" cssClass="form-error" element="p"/>
							</div>
							<div class=" text subformtitle">
				        		<spring:message code="post.date"/>
				        	</div>
							<div class="form-group">
								<form:input path="event_date" type="date" class="form-control" id="date" name="date"  />
								<form:errors path="event_date" cssClass="form-error" element="p"/>
							</div>
							<div class=" text subformtitle">
				        		<spring:message code="post.phone"/>
				        	</div>
				        	
							<div class="form-group">
								<form:input path="contact_phone" type="text" class="form-control" id="mobile" name="mobile" placeholder=""  />
								<form:errors path="contact_phone" cssClass="form-error" element="p"/>
							</div>
							<div class=" text subformtitle">
				        		<spring:message code="post.description"/>
				        	</div>
				        	<spring:message code="write.description" var="writedesc"/>
				            <div class="form-group">
				            <form:textarea path="description" class="form-control" type="textarea" id="message" placeholder="${writedesc}" maxlength="140" rows="7"></form:textarea>
				            <form:errors path="description" cssClass="form-error" element="p"/>
				               <!--<span class="help-block"><p id="characterLeft" class="help-block ">llegaste al limite</p></span>-->                    
				            </div>
				            <div class=" text subformtitle">
				        		<spring:message code="post.image"/>
				        	</div>
				            <!--<input type="file" name="files" /><br/>

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
				            </div>!-->
				            <c:forEach items="${uploadForm.images}" varStatus="status">
										<div class="col-md-3 image-div"> 
											<form:input class="image-input" type="file" path="images[${status.index}].file" accept="image/*"/>
											<form:label path="images[${status.index}].file">
												<div class="preview-container">
													<span class="add-img-text">
														<button  type="button"><i class="fa fa-plus"></i></button>
													</span>
													<button  type="button"><i class="fa fa-minus"></i></button>
												</div>
											</form:label>
											<form:errors path="images[${status.index}].file" cssClass="form-error" element="p"/>
										</div>
							</c:forEach>
				            

				    </div>  
        	</div>
        </div>



        <div class="row">
        	 <spring:message code="createpost" var="createpost"/>
        	<div class="center">
        		<input id="submit" type="submit" name="submit" class="btn btn-lg btn-success pull-right" value="${createpost}"></input>
        	</div>
        	
        </div>
    </form:form>
    <script>

    	var index = 0;

    	$(document).ready(function(){
    		$('image-div').each(function(index){
    			if(index > 0){
    				
    				//$( this ).css("display", "none");
    			}
    		});
    	});
    var latitude = parseFloat(document.getElementById('lat').value);
   	var longitude = parseFloat(document.getElementById('lon').value);
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