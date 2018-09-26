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

</head>


<body>
	
	<%@include file="includes/header.jsp"%>

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
                                   <img src="<c:url value="/post/images/${postImage.url}"/>" class="center img-fluid carrouselimg">
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
                                  <!-- NOT NECESARY <li class="list-inline-item active">
                                    <a id="carousel-selector-0" class="selected" data-slide-to="0" data-target="#myCarousel">
                                        <img src="https://upload.wikimedia.org/wikipedia/commons/thumb/a/ac/No_image_available.svg/1024px-No_image_available.svg.png" class="img-fluid carrouselimglittle">
                                    </a>
                                  </li>!-->
                              </c:when>
                              <c:otherwise>
                                  <c:forEach items="${post.postImages}" var="postImage" varStatus="loop">
                                  <li class="list-inline-item active">
                                    <a id="carousel-selector-${loop.index}" class="${loop.first ? 'selected' : ''}" data-slide-to="${loop.index}" data-target="#myCarousel">
                                        <img src="<c:url value="/post/images/${postImage.url}"/>" class="img-fluid center carrouselimglittle">
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
                <spring:message code="gender.male" var="male_gender"/>
                <spring:message code="gender.female" var="female_gender"/>
                <div class="text postmaininfo uspaced10"><span class="badge badge-pill <spring:message code="category.color.${post.category.lowerName}"/> text postmaininfo"><spring:message code="pill.${post.category.lowerName}"/></span><em class="lspaced10">A <c:out value="${post.distance}"/> kms</em> <em> / </em> <em>Especie</em><em>:</em> <em><spring:message code="specie.${post.pet.lowerName}"/></em> <em> / </em> <em>Sexo</em><em>:</em> <em>${post.is_male ? male_gender : female_gender }</em>
                </div>

                <div class="text postdescription uspaced20"> <c:out value="${post.description}"/></div>

                <div class="uspaced20">
                  <div class="text postcontact"> Lugar del evento: </div>
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
              <c:url value="/post/${post.id}/comment" var="postPath" />
              <form:form modelAttribute="commentsForm" class="comment-form" action="${postPath}" method="post">
                            <div class="form-group">
                                <form:textarea type="text" class="form-control" rows="3" path="parentForm.content" placeholder="Tu comentario" maxlength="512"/>
                                <form:errors path="parentForm.content" element="p" cssClass="form-error"/>
                            </div>
                            <div class="btn-place">
                                <input type="submit" class="btn btn-default post-comment-btn" value="enviar" />
                            </div>
              </form:form>
              <div class="comments-container">
                <c:forEach items="${parentComments}" var="commentFamily" varStatus="status">

                            <div class="comment-and-replies">
                            <div class="parent-comment" id="comment${commentFamily.parentComment.id}">
                                <div class="row">
                                    <div class="col-md-1">
                                        <a href="<c:url value="/profile/${commentFamily.parentComment.author.id}"/>">
                                            <img class="profile-img-circle" src="<c:url value="/profile/${commentFamily.parentComment.author.id}/profilePicture"/>">
                                        </a>
                                    </div>
                                    <div class="col-md-10 parent-name-mail-holder">
                                        <div class="row col-md-12 profile-name-holder">
                                            <a class="profile-name" href="<c:url value="/profile/${commentFamily.parentComment.author.id}"/>"> 
                                                <c:out value="${commentFamily.parentComment.author.name}" />
                                            </a>                              </div>
                                        <div class="row col-md-12">
                                            <a class="creator-mail" href="mailto:<c:out value="${commentFamily.parentComment.author.mail}"/>">
                                                <span class="glyphicon glyphicon-envelope"></span>
                                                <p><c:out value="${commentFamily.parentComment.author.mail}"/></p>
                                            </a>
                                        </div>                    
                                    </div>
                                </div>
                                <div class="row">
                                    <div class="col-md-12 comment-content">
                                        <p>
                                            <c:out value="${commentFamily.parentComment.content}" />
                                        </p>
                                    </div>
                                </div>
                                <div class="row reply-button-holder">
                                    <div class="col-md-4">
                                        <p class="reply-btn">
                                            <span class="glyphicon glyphicon-share-alt"></span>
                                            REPLY
                                        </p>
                                    </div>
                                </div>
                                <div class="row comment-divider">
                                    <div class="col-md-12"></div>
                                </div>
                            </div>

                            <c:forEach items="${commentFamily.childComments}" var="child">
                                <div class="row child-comment" id="comment${child.id}">
                                    <div class="col-md-10 col-md-offset-2">
                                        <div class="row">
                                            <div class="col-md-1">
                                                <a href="<c:url value="/profile/${child.author.id}"/>">
                                                    <img class="profile-img-circle" src="<c:url value="/profile/${child.author.id}/profilePicture"/>">
                                                </a>
                                            </div>
                                            <div class="col-md-10 child-name-mail-holder">
                                                <div class="row col-md-12 profile-name-holder">
                                                    <a class="profile-name" href="<c:url value="/profile/${child.author.id}"/>"> 
                                                        <c:out value="${child.author.name}" />
                                                    </a>
                                                </div>
                                                <div class="row col-md-12">
                                                    <a class="creator-mail" href="mailto:<c:out value="${child.author.mail}"/>">
                                                        <span class="glyphicon glyphicon-envelope"></span>
                                                        <p><c:out value="${child.author.mail}"/></p>
                                                    </a>
                                                </div>                    
                                            </div>
                                        </div>
                                        <div class="row">
                                            <div class="col-md-12 comment-content">
                                                <p>
                                                    <c:out value="${child.content}" />
                                                </p>
                                            </div>
                                        </div>
                                        <div class="row comment-divider">
                                            <div class="col-md-12"></div>
                                        </div>
                                    </div>
                                </div>  
                            </c:forEach>                
                           
                           <div class="row child-comment-row">
                                <div class="col-md-10 col-md-offset-2">
                                    <form:form modelAttribute="commentsForm" id="form${status.index}" class="comment-form reply-comment" action="${postPath}?parentid=${commentFamily.parentComment.id}&index=${status.index}" method="post">
                                        <div class="row">
                                            <div class="col-md-1">
                                                <a href="<c:url value="/profile/${loggedUser.id}"/>">
                                                    <img class="profile-img-circle" src="<c:url value="/profile/${loggedUser.id}/profilePicture"/>">
                                                </a>
                                            </div>
                                            <div class="col-md-10 child-name-mail-holder">
                                                <div class="row col-md-12 profile-name-holder">
                                                    <a class="profile-name" href="<c:url value="/profile/${loggedUser.id}"/>"> 
                                                        <c:out value="${loggedUser.name}" />
                                                    </a>
                                                </div>
                                                <div class="row col-md-12">
                                                    <a class="creator-mail" href="mailto:<c:out value="${loggedUser.mail}"/>">
                                                        <span class="glyphicon glyphicon-envelope"></span>
                                                        <p><c:out value="${loggedUser.mail}"/></p>
                                                    </a>
                                                </div>                    
                                            </div>
                                        </div>  
                                        <div class="form-group comment-form-fields">
                                            <spring:message code="postPage.replyTo" arguments="${commentFamily.parentComment.author.name}" var="replyPlaceholder"/>
                                            <form:textarea type="text" class="form-control" rows="3" path="childForms[${status.index}].content" placeholder="${replyPlaceholder}"  maxlength="512"/>
                                            <form:errors path="childForms[${status.index}].content" element="p" cssClass="form-error"/>
                                        </div>
                                        <div class="btn-place">
                                            <input type="submit" class="btn btn-default post-comment-btn" value="REPLY" />
                                        </div>
                                        <div class="row comment-divider">
                                            <div class="col-md-12"></div>
                                        </div>
                                    </form:form>
                                </div>
                            </div>
                        
                            </div>
                        </c:forEach>
              </div>
              <div class="row uspaced5"></div>        
            </div>
          </div>
          <div class="col-lg-3"></div>
        </div>
        <div class="row uspaced20"></div>


  <!--GOOGLE MAPS-->
  <script>
// Initialize and add the map
function initMap() {
  // The location of Uluru
  var uluru = {lat: <c:out value="${post.location.latitude}"/>, lng: <c:out value="${post.location.longitude}"/>};
  // The map, centered at Uluru
  var map = new google.maps.Map(
      document.getElementById('map'), {zoom: 14, center: uluru});
  // The marker, positioned at Uluru
  var marker = new google.maps.Marker({position: uluru, map: map});
}
  </script>
  <script async defer
  src="https://maps.googleapis.com/maps/api/js?key=AIzaSyAsqLEThGLQ6T4Ayox_K7Em1S4DuAT-wm8&callback=initMap">
  </script>
  <!--GOOGLE MAPS-->  		

	<!--FOOTER-->
    <div class="row uspaced60"></div>
    <div class="container-fluid footer">
        <div class="text footertext">© 2018 Todos los derechos reservados pawgram.org</div> 
    </div>
  <!--FOOTER-->




</body>