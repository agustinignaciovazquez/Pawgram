<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<head>

	<meta charset="UTF-8">
	<title>Pawgram - Post</title>

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
                  <li class="nav-item">
                    <a class="nav-link text nav-sec" href="<c:url value="/zones/category/${category.lowerName}"/>"><spring:message code="category.${category.lowerName}"/></a>
                  </li>
              </c:forEach>
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
       <!-- main slider carousel -->
        <div class="row uspaced20">
            <div class="col-lg-1"></div>
            <div class="col-lg-5" id="slider">
              <div class="container postcontent">  
                      <div id="myCarousel" class="carousel slide">
                          <!-- main slider carousel items -->
                          <div class="carousel-inner">
                            <c:choose>
                              <c:when test="${post.postImages.isEmpty()}">
                                  <div class="active item carousel-item" data-slide-number="0">
                                   <img src="https://upload.wikimedia.org/wikipedia/commons/thumb/a/ac/No_image_available.svg/1024px-No_image_available.svg.png" class="center img-fluid carrouselimg">
                                  </div>
                              </c:when>
                              <c:otherwise>
                                  <c:forEach items="${post.postImages}" var="postImage" varStatus="loop">
                                    <div class="${loop.first ? 'active' : ''} item carousel-item" data-slide-number="${loop.index}">
                                   <img src="https://upload.wikimedia.org/wikipedia/commons/thumb/a/ac/No_image_available.svg/1024px-No_image_available.svg.png" class="center img-fluid carrouselimg">
                                  </div>
                                </c:forEach>
                             </c:otherwise>
                           </c:choose>
                             
                              

                              <a class="carousel-control-prev" href="#myCarousel" role="button" data-slide="prev">
                                <span class="carousel-control-prev-icon" aria-hidden="true"></span>
                              </a>
                              <a class="carousel-control-next" href="#myCarousel" role="button" data-slide="next">
                                <span class="carousel-control-next-icon" aria-hidden="true"></span>
                              </a>

                          </div>
                          <!-- main slider carousel nav controls -->


                          <ul class="carousel-indicators uspaced20">
                             
                              <c:choose>
                              <c:when test="${post.postImages.isEmpty()}">
                                  <li class="list-inline-item active">
                                    <a id="carousel-selector-0" class="selected" data-slide-to="0" data-target="#myCarousel">
                                        <img src="https://upload.wikimedia.org/wikipedia/commons/thumb/a/ac/No_image_available.svg/1024px-No_image_available.svg.png" class="img-fluid">
                                    </a>
                                  </li>
                              </c:when>
                              <c:otherwise>
                                  <c:forEach items="${post.postImages}" var="postImage" varStatus="loop">
                                  <li class="list-inline-item active">
                                    <a id="carousel-selector-${loop.index}" class="${loop.first ? 'selected' : ''}" data-slide-to="${loop.index}" data-target="#myCarousel">
                                        <img src="<c:url value="/post/images/${postImage.url}"/>" class="img-fluid">
                                    </a>
                                  </li>
                                </c:forEach>
                             </c:otherwise>
                           </c:choose>

                          </ul>
                  </div>
                  <div class="row uspaced20"></div>
                </div>  
            </div>
            <div class="col-lg-5">
              <div class="container postcontent">
                
                <div class="text posttitle"><c:out value="${post.title}"/></div>
                
                <div class="text postdate uspaced10"> El evento ocurrio el <c:out value="${post.event_date}"/></div>
                
                <div class="text postmaininfo uspaced10"><span class="badge badge-pill <spring:message code="category.color.${post.category.lowerName}"/> text postmaininfo"><spring:message code="pill.${post.category.lowerName}"/></span><em class="lspaced10">A <c:out value="${post.distance}"/> kms</em> <em> / </em> <em>Especie</em><em>:</em> <em><spring:message code="specie.${post.pet.lowerName}"/></em> <em> / </em> <em>Sexo</em><em>:</em> <em>${post.is_male ? 'MACHO' : 'HEMBRA'}</em>
                </div>

                <div class="text postdescription uspaced20"> <c:out value="${post.description}"/></div>

                <div class="uspaced20">
                  <div class="text postcontact"> Lugar del evento: </div>
                  <input id="searchInput" class="controls" type="text" placeholder="Enter a location">
                   <div id="map"></div>
                          <!--<ul id="geoData">
                              <li>Direccion: <span id="location"></span></li>
                              <li>Codigo Postal: <span id="postal_code"></span></li>
                              <li>Pais: <span id="country"></span></li>
                              <li>Latitud: <span id="lat"></span></li>
                              <li>Longitud: <span id="lon"></span></li>
                          </ul>-->
                </div>

                <div class="text postcontact uspaced20"> Datos de contacto:</div>
                <div class="text postcontactinfo"> Nombre: <c:out value="${post.owner.name} ${post.owner.surname}"/> </div>
                <div class="text postcontactinfo"> Telefono: <c:out value="${post.contact_phone}"/> </div>
                <div class="text postcontactinfo"> E-mail: <c:out value="${post.owner.mail}"/></div>



                <div class="row uspaced20"></div>

             </div>
            <div class="col-lg-1"></div>
          </div>
        </div>

        <div class="row uspaced20">
          <div class="col-lg-3"></div>
          <div class="col-lg-6">
            <div class="container postcontent">
              <div class="text postcontact">Comentarios:</div>
              <div class="comments-container">
                <ul id="comments-list" class="comments-list">
                  <li>
                    <div class="comment-main-level">
                      <!-- Avatar -->
                      <div class="comment-avatar"><img src="http://i9.photobucket.com/albums/a88/creaticode/avatar_1_zps8e1c80cd.jpg" alt=""></div>
                      <!-- Contenedor del Comentario -->
                      <div class="comment-box">
                        <div class="comment-head">
                          <h6 class="comment-name by-author"><a href="http://creaticode.com/blog">Agustin Ortiz</a></h6>
                          <span>hace 20 minutos</span>
                          <i class="fa fa-reply"></i>
                          <i class="fa fa-heart"></i>
                        </div>
                        <div class="comment-content">
                          Lorem ipsum dolor sit amet, consectetur adipisicing elit. Velit omnis animi et iure laudantium vitae, praesentium optio, sapiente distinctio illo?
                        </div>
                      </div>
                    </div>
                    <!-- Respuestas de los comentarios -->
                    <ul class="comments-list reply-list">
                      <li>
                        <!-- Avatar -->
                        <div class="comment-avatar"><img src="http://i9.photobucket.com/albums/a88/creaticode/avatar_2_zps7de12f8b.jpg" alt=""></div>
                        <!-- Contenedor del Comentario -->
                        <div class="comment-box">
                          <div class="comment-head">
                            <h6 class="comment-name"><a href="http://creaticode.com/blog">Lorena Rojero</a></h6>
                            <span>hace 10 minutos</span>
                            <i class="fa fa-reply"></i>
                            <i class="fa fa-heart"></i>
                          </div>
                          <div class="comment-content">
                            Lorem ipsum dolor sit amet, consectetur adipisicing elit. Velit omnis animi et iure laudantium vitae, praesentium optio, sapiente distinctio illo?
                          </div>
                        </div>
                      </li>

                      <li>
                        <!-- Avatar -->
                        <div class="comment-avatar"><img src="http://i9.photobucket.com/albums/a88/creaticode/avatar_1_zps8e1c80cd.jpg" alt=""></div>
                        <!-- Contenedor del Comentario -->
                        <div class="comment-box">
                          <div class="comment-head">
                            <h6 class="comment-name by-author"><a href="http://creaticode.com/blog">Agustin Ortiz</a></h6>
                            <span>hace 10 minutos</span>
                            <i class="fa fa-reply"></i>
                            <i class="fa fa-heart"></i>
                          </div>
                          <div class="comment-content">
                            Lorem ipsum dolor sit amet, consectetur adipisicing elit. Velit omnis animi et iure laudantium vitae, praesentium optio, sapiente distinctio illo?
                          </div>
                        </div>
                      </li>
                    </ul>
                  </li>

                  <li>
                    <div class="comment-main-level">
                      <!-- Avatar -->
                      <div class="comment-avatar"><img src="http://i9.photobucket.com/albums/a88/creaticode/avatar_2_zps7de12f8b.jpg" alt=""></div>
                      <!-- Contenedor del Comentario -->
                      <div class="comment-box">
                        <div class="comment-head">
                          <h6 class="comment-name"><a href="http://creaticode.com/blog">Lorena Rojero</a></h6>
                          <span>hace 10 minutos</span>
                          <i class="fa fa-reply"></i>
                          <i class="fa fa-heart"></i>
                        </div>
                        <div class="comment-content">
                          Lorem ipsum dolor sit amet, consectetur adipisicing elit. Velit omnis animi et iure laudantium vitae, praesentium optio, sapiente distinctio illo?
                        </div>
                      </div>
                    </div>
                  </li>
                </ul>
              </div>
              <div class="row uspaced5"></div>        
            </div>
          </div>
          <div class="col-lg-3"></div>
        </div>
        <div class="row uspaced20"></div>


  
  
    		

	<!--FOOTER-->
    <div class="row uspaced60"></div>
    <div class="container-fluid footer">
        <div class="text footertext">© 2018 Todos los derechos reservados pawgram.org</div> 
    </div>
  <!--FOOTER-->




</body>