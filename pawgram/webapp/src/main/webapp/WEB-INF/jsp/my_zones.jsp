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
        <div class="row">
            <div class="text zonetext zoneel1 rspaced2">Zona 1</div>
            <div class="lspaced20"></div>
            <button type="button" class="btn btn-danger btn-circle uspaced4"><i class="fas fa-trash-alt"></i></button>
        </div>
        <div class="row uspaced20">
          <div class="col-md-12">
            <div class=" zonecard">
              <div class="container">
                <div class="row">
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
                <div class="row uspaced10">
                  <div class="col-lg-4">
                    <div class="zonewrapper">
                      <div class="text zonetitle"> Zona: </div>
                      <div class="text zonedata">&nbsp Quilmes</div>
                    </div>  
                  </div>
                  <div class="col-lg-3">
                    <div class="zonewrapper">
                      <div class="text zonetitle"> Latitud: </div>
                      <div class="text zonedata">&nbsp 9.912301</div>
                    </div>
                  </div>
                  <div class="col-lg-3">
                    <div class="zonewrapper">
                      <div class="text zonetitle"> Longitud: </div>
                      <div class="text zonedata">&nbsp  53.131455</div>
                    </div>
                  </div>
                  <div class="col-lg-2">
                    <div class="zonewrapper">
                      <div class="text zonetitle"> Rango: </div>
                      <div class="text zonedata">&nbsp 10km</div>
                    </div>
                  </div>
                </div>
                <div class="bspaced2">         
                </div>
              </div>
            </div>
          </div>
        </div>
    </div>


  <!--FOOTER-->
    <div class="row uspaced60"></div>
    <div class="container-fluid footer">
        <div class="text footertext"><spring:message code="footer"/></div> 
    </div>
   <!--FOOTER-->




</body>
