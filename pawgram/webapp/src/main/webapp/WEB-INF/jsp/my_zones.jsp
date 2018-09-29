<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>


<head>

  <meta charset="UTF-8">
  <title><spring:message code="pageName"/>-<spring:message code="title.myzones"/></title> 
  

  <link href="<c:url value="/resources/css/all.css"/>" rel="stylesheet" id="font-awesome">
  <link href="<c:url value="/resources/css/bootstrap.min.css"/>" rel="stylesheet" id="bootstrap-css">
  <link rel="stylesheet" href="<c:url value="/resources/css/pawgramin.css"/>">

  
  <script src="<c:url value="/resources/js/jquery-3.3.1.min.js"/>"></script>
  <script src="<c:url value="/resources/js/popper.js"/>"></script>
  <script src="<c:url value="/resources/js/bootstrap.min.js"/>"></script>
  <script src="<c:url value="/resources/js/pawgram.js"/>"></script>
  <script>
    var initmaps = [];
  </script>
</head>


<body>
  
  <%@include file="includes/header.jsp"%>
  

		<div class="container-fluid titzon">
			<div class="row">
				<div class="col-md-3"></div>
				<div class="text titsec">Mis Zonas</div>
			</div>
		</div>

		<div class="row uspaced60">
			<div class="center">
			 <div class="text noposttext"> Aun no tienes ninguna zona </div>
			</div>     
		</div>

		<div class="row uspaced20">
			<div class=" center">
				<button type="submit" class="btn btn-success newbutton">
	    			<i class="fas fa-plus"></i> Agregar nueva zona
				</button>  	
			</div>
			    
		</div>
      <div class="container uspaced60">
        <c:forEach items="${searchZones}" var="searchZone" varStatus="status">
        
        <div class="row uspaced5">
            <div class="text zonetext zoneel1 rspaced2">Zona <c:out value="${searchZone.id}"/></div>
            <div class="lspaced20"></div>
            <button type="button" class="btn btn-danger btn-circle uspaced4" onclick="location.href='<c:url value="/my_zones/delete/${searchZone.id}"/>'"><i class="fas fa-trash-alt"></i></button>
        </div>
        <div class="row uspaced20">
          <div class="col-md-12">
            <div class=" zonecard">
              <div class="container">
                <div class="row">
                   <div id="<c:out value="map${status.index}"/>" class="mapround"></div>
        
                </div>
                <div class="row uspaced10">
                  <div class="col-lg-4">
                    <div class="zonewrapper">
                      <div class="text zonetitle"> Zona: </div>
                      <div class="text zonedata">&nbsp <c:out value="${status.index}"/></div>
                    </div>  
                  </div>
                  <div class="col-lg-3">
                    <div class="zonewrapper">
                      <div class="text zonetitle"> Latitud: </div>
                      <div class="text zonedata">&nbsp <c:out value="${searchZone.location.latitude}"/></div>
                    </div>
                  </div>
                  <div class="col-lg-3">
                    <div class="zonewrapper">
                      <div class="text zonetitle"> Longitud: </div>
                      <div class="text zonedata">&nbsp  <c:out value="${searchZone.location.longitude}"/></div>
                    </div>
                  </div>
                  <div class="col-lg-2">
                    <div class="zonewrapper">
                      <div class="text zonetitle"> Rango: </div>
                      <div class="text zonedata"><c:out value="${searchZone.range}"/> km</div>
                    </div>
                  </div>
                </div>
                <div class="bspaced2">         
                </div>
              </div>
            </div>
          </div>
        </div>
        <!--GOOGLE MAPS-->
        
          <script>

        // Initialize and add the map
            function <c:out value="initMap${status.index}"/>() {
              // Create the map.
              var map = new google.maps.Map(document.getElementById('<c:out value="map${status.index}"/>'), {
                zoom: 12,
                center: {lat: <c:out value="${searchZone.location.latitude}"/>, lng: <c:out value="${searchZone.location.longitude}"/>},
                mapTypeId: 'terrain'
              });

              var cityCircle = new google.maps.Circle({
                strokeColor: '#FF0000',
                strokeOpacity: 0.8,
                strokeWeight: 2,
                fillColor: '#FF0000',
                fillOpacity: 0.35,
                map: map,
                center: {lat: <c:out value="${searchZone.location.latitude}"/>, lng: <c:out value="${searchZone.location.longitude}"/>},
                radius: <c:out value="${searchZone.range}"/>
              });
            
          }
          initmaps.push(<c:out value="initMap${status.index}"/>);
          </script>
          
          <!--GOOGLE MAPS--> 
        </c:forEach>
    </div>


  <script>
    function initMap() {
      for (i = 0; i < initmaps.length; i++) {
            initmaps[i]();
      }  
    }
  </script>
  <script async defer
          src="https://maps.googleapis.com/maps/api/js?key=AIzaSyAsqLEThGLQ6T4Ayox_K7Em1S4DuAT-wm8&callback=initMap">
  </script>
  <!--FOOTER-->
    <div class="row uspaced60"></div>
    <div class="container-fluid footer">
        <div class="text footertext"><spring:message code="footer"/></div> 
    </div>
   <!--FOOTER-->




</body>
