<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>

<head>

	<meta charset="UTF-8">
	<title><spring:message code="Pawgram - Configuration"/></title>

  <link href="<c:url value="/resources/css/all.css"/>" rel="stylesheet" id="font-awesome">
	<link href="<c:url value="/resources/css/bootstrap.min.css"/>" rel="stylesheet" id="bootstrap-css">
	<link rel="stylesheet" href="<c:url value="/resources/css/pawgramin.css"/>">

	
  <script src="<c:url value="/resources/js/jquery-3.3.1.min.js"/>"></script>
	<script src="<c:url value="/resources/js/bootstrap.min.js"/>"></script>
  <script src="<c:url value="/resources/js/pawgram.js"/>"></script>

  <script src="https://maps.googleapis.com/maps/api/js?libraries=places&callback=initMap" async defer></script>

</head>


<body>
	
	<!--HEADER-->
    <div id="wrapper" class="animate">

         <nav class="navbar header-top fixed-top navbar-expand-lg navbar-light pawnav">
          <span class="navbar-toggler-icon leftmenutrigger"></span>
          <img src="img/logo.png" class="logo-navbar"/>
          <button class="navbar-toggler" type="button" data-toggle="collapse" data-target="#navbarText" aria-controls="navbarText"
            aria-expanded="false" aria-div class="text"="Toggle navigation">
            <span class="navbar-toggler-icon"></span>
          </button>
          <div class="collapse navbar-collapse" id="navbarText">
             <ul class="navbar-nav ml-md-auto d-md-flex">
              <li class="nav-item">
                <a class="nav-link pawlink text nav-sec" href="#">Busquedas
                  <span class="sr-only">(current)</span>
                </a>
              </li>
              <li class="nav-item">
                <a class="nav-link text nav-sec" href="#">Adopciones</a>
              </li>
              <li class="nav-item">
                <a class="nav-link text nav-sec" href="#">Emergencias</a>
              </li>
            </ul>
            <ul class="navbar-nav animate side-nav">
              <li class="nav-item">
                <a class="nav-link text nav-sec" href="#">Mi perfil
                  <span class="sr-only">(current)</span>
                </a>
              </li>
              <li class="nav-item">
                <a class="nav-link text nav-sec" href="#">Mis Zonas</a>
              </li>
              <li class="nav-item">
                <a class="nav-link text nav-sec" href="#">Configuracion</a>
              </li>             
              <hr></hr>
              <li class="nav-item">
                <a class="nav-link text nav-sec" href="#">Cerrar Sesion</a>
              </li>
            </ul>
           
          </div>
        </nav>
    </div>

	<!--HEADER-->  

		<div class="container-fluid titzon">
			<div class="row">
				<div class="col-md-3"></div>
				<div class="text titsec">Configuracion</div>
			</div>
		</div>

		<div class="container">
      <div class="row">
        <div class="text zonetext uspaced60 lspaced20">Cambiar Informacion Personal</div>
      </div>
      
      <hr class="divider">

      <div class="row uspaced20">
        <div class="col-lg-12">
            
          <div class="text">Nombre</div class="text">
          <div class="form-group"> 
            <input type="text" value="Roberto" class="form-control" placeholder="Nuevo Nombre"> 
          </div> 
          <div class="text">Apellido</div class="text">
          <div class="form-group"> 
            <input type="text" value="Carlos" class="form-control" placeholder="Nuevo apellido"> 
          </div> 
                
        </div>  
      </div>

    </div>


    <div class="container uspaced60">
      <div class="row">
        <div class="text zonetext uspaced60 lspaced20">Cambiar Contraseña</div>
      </div>
      <hr class="divider">
      <div class="row uspaced20">
        <div class="col-lg-12">
            
            <div class="text">Contraseña actual</div class="text">
            <div class="form-group pass_show"> 
                    <input type="password" value="faisalkhan@123" class="form-control" placeholder="Current Password"> 
                </div> 
               <div class="text">Nueva Contraseña</div class="text">
                <div class="form-group pass_show"> 
                    <input type="password" value="faisal.khan@123" class="form-control" placeholder="New Password"> 
                </div> 
               <div class="text">Confirmar Nueva Contraseña</div class="text">
                <div class="form-group pass_show"> 
                    <input type="password" value="faisal.khan@123" class="form-control" placeholder="Confirm Password"> 
                </div> 
                
        </div>  
      </div>
    </div>

	<!--FOOTER-->
    <div class="row uspaced60"></div>
    <div class="container-fluid footer">
        <div class="text footertext">© 2018 Todos los derechos reservados pawgram.org</div> 
    </div>
    <!--FOOTER-->




</body>