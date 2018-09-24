<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<head>

	<meta charset="UTF-8">
	<title>Pawgram - Emergency</title>

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
          <img src="<c:url value="/resources/img/logo.png"/>" class="logo-navbar"/>
          <button class="navbar-toggler" type="button" data-toggle="collapse" data-target="#navbarText" aria-controls="navbarText"
            aria-expanded="false" aria-label="Toggle navigation">
            <span class="navbar-toggler-icon"></span>
          </button>
          <div class="collapse navbar-collapse" id="navbarText">
            
             <ul class="navbar-nav ml-md-auto d-md-flex">
               <c:forEach items="${categories}" var="category">
                <c:set var="active" value="${category eq currentCategory}"/>
                <li class="nav-item ${active ? 'active' : ''}">
                  <a class="nav-link text nav-sec" href="<c:out value="${category.lowerName}"/>"><spring:message code="category.${category.lowerName}"/></a>
                </li>
              </c:forEach>
            </ul>
            <ul class="navbar-nav animate side-nav">
              <li class="nav-item">
                <a class="nav-link text nav-sec" href="#">Mi perfil</a>
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
				<div class="text titsec"><spring:message code="category.${category.lowerName}"/></div>
			</div>
		</div>

		<div class="row uspaced60">
			<div class="center">
			 <div class="text noposttext"> Aun no tienes ninguna emergencia en proceso </div>
			</div>     
		</div>

		<div class="row uspaced20">
			<div class="center">
				<button type="submit" class="btn btn-success newbutton">
	    			<i class="fas fa-plus"></i> Iniciar nueva emergencia
				</button>  	
			</div>
			    
		</div>

		<div class="container uspaced5">

            <div class="row">
                <div class="text zonetext "> Cerca de Quilmes</div>
            </div>
            <div class="row uspaced20">
                
            <c:choose>
            <c:when test="${searchZone.posts.isEmpty()}">
                  <div class="text center"><spring:message code="msg.sorry"/></div>
                  <div class="text center"><spring:message code="msg.nopost"/></div>
            </c:when>
            <c:otherwise>
          
              <c:forEach items="${searchZone.posts}" var="post">
              <div class="col-md-4">                  
                    <a href="<c:url value="/post/${post.id}"/>" class="">
                        <div class="card"> 
                          <c:choose>
                            <c:when test="${post.postImages.isEmpty()}">
                                 <img class="img-fluid card-img" src="https://upload.wikimedia.org/wikipedia/commons/thumb/a/ac/No_image_available.svg/1024px-No_image_available.svg.png" alt="">
                            </c:when>
                            <c:otherwise>
                                  <img class="img-fluid card-img" src="<c:url value="/post/images/${post.postImages[0]}"/>" alt="">
                           </c:otherwise>
                         </c:choose>
                           

                            <div class="card-img-overlay"> <span class="badge badge-pill <spring:message code="category.color.${post.category.lowerName}"/> text categorytext"><spring:message code="pill.${post.category.lowerName}"/></span> </div>
                            <div class="card-body">
                                <p class="card-text"><small class="text  text-time"><em>Distancia</em><em>:</em> <em> <c:out value="${post.distance}"/> kms</em> <em> / </em> <em>Especie</em><em>:</em> <em><spring:message code="specie.${post.pet.lowerName}"/></em> <em> / </em> <em>Sexo</em><em>:</em> <em>CAMBIAR DESPUES</em> </small></p>
                                <div class="news-title">
                                    <h2 class="text title-small"><c:out value="${post.title}"/></h2>
                                </div>
                                <div class="card-exp">
                                    <i class="far fa-eye"> <div class="text seemoretext">Ver Detalles</div></i>
                                </div>
                                
                            </div>
                        </div>
                    </a>
                </div>  
                          
                
              </c:forEach>
            </c:otherwise>
        </c:choose>

        </div>    
    </div>



		

	<!--FOOTER-->
    <div class="row uspaced60"></div>
    <div class="container-fluid footer">
        <div class="text footertext">© 2018 Todos los derechos reservados pawgram.org</div> 
    </div>
    <!--FOOTER-->




</body>