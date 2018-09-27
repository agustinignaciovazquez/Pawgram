<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<head>

	<meta charset="UTF-8">
	<title><spring:message code="pageName"/> - <spring:message code="title.post"/></title>

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
                
                <div class="text postdate uspaced10"> <spring:message code="post.ocurred"/> <c:out value="${post.event_date}"/></div>
                <spring:message code="gender.male" var="male_gender"/>
                <spring:message code="gender.female" var="female_gender"/>
                <div class="text postmaininfo uspaced10"><span class="badge badge-pill <spring:message code="category.color.${post.category.lowerName}"/> text postmaininfo"><spring:message code="pill.${post.category.lowerName}"/></span><em class="lspaced10">A <c:out value="${post.distance}"/> kms</em> <em> / </em> <em>Especie</em><em>:</em> <em><spring:message code="specie.${post.pet.lowerName}"/></em> <em> / </em> <em>Sexo</em><em>:</em> <em>${post.is_male ? male_gender : female_gender }</em>
                </div>

                <div class="text postdescription uspaced20"> <c:out value="${post.description}"/></div>

                <div class="uspaced20">
                  <div class="text postcontact"> <spring:message code="post.place"/> </div>
                   <div id="map"></div>
                          <!--<ul id="geoData">
                              <li>Direccion: <span id="location"></span></li>
                              <li>Codigo Postal: <span id="postal_code"></span></li>
                              <li>Pais: <span id="country"></span></li>
                              <li>Latitud: <span id="lat"></span></li>
                              <li>Longitud: <span id="lon"></span></li>
                          </ul>-->
                </div>
               
                <div class="text postcontact uspaced20"> <spring:message code="post.contact"/> </div>
                <div class="text postcontactinfo"> <spring:message code="post.name"/> <c:out value="${post.owner.name} ${post.owner.surname}"/> </div>
                <div class="text postcontactinfo"> <spring:message code="post.phonee"/> <c:out value="${post.contact_phone}"/> </div>
                <div class="text postcontactinfo"> <spring:message code="post.mail"/> <c:out value="${post.owner.mail}"/></div>


                <div class="row uspaced20"></div>

             </div>
            <div class="col-lg-1"></div>
          </div>
        </div>

        <div class="row uspaced20">
          <div class="col-lg-3"></div>
          <div class="col-lg-6">
            <div class="container postcontent">
              <div class="text postcontact"><spring:message code="post.comentaries"/></div>

              <c:url value="/post/${post.id}/comment" var="postPath" />
              <form:form modelAttribute="commentsForm" class="comment-form" action="${postPath}" method="post">
                <div class="form-group">
                  <spring:message code="your.comment" var="yourcomment"/>
                  <form:textarea type="text" class="form-control" rows="3" path="parentForm.content" placeholder="${yourcomment}" maxlength="512"/>
                  <form:errors path="parentForm.content" element="p" cssClass="form-error"/>
                </div>
                 <spring:message code="comment.comm" var="comment"/>
                <div class="btn-place">
                  <input type="submit" class="btn btn-default post-comment-btn" value="${comment}" />
                </div>
              </form:form>
               
              <div class="row uspaced60"></div>  

              <div class="comments-container ">
                <ul id="comments-list" class="comments-list uspaced60 ">
                  <c:forEach items="${parentComments}" var="commentFamily" varStatus="status">
                  <li>
                    <div class="comment-main-level" id="comment${commentFamily.parentComment.id}">
                      <!-- Avatar -->
                      <div class="comment-avatar"><img src="<c:url value="/profile/images/${commentFamily.parentComment.author.profile_img_url}"/>" alt=""></div>
                      <!-- Contenedor del Comentario -->
                      <div class="comment-box">
                        <div class="comment-head">
                          <h6 class="comment-name ${post.owner eq commentFamily.parentComment.author ? 'by-author' : 'duki'}"><a href="<c:url value="/profile/${commentFamily.parentComment.author.id}"/>"> <c:out value="${commentFamily.parentComment.author.name} ${commentFamily.parentComment.author.surname}" /></a></h6>
                          <span><c:out value="${commentFamily.parentComment.commentDate}"/></span>
                          
                          <!--

                            <span>hace 10 minutos</span>
                            <i class="fa fa-reply"></i>
                            <i class="fa fa-heart"></i>

                           -->

                        </div>
                        <div class="comment-content">
                          <c:out value="${commentFamily.parentComment.content}" />
                        </div>
                      </div>
                    </div>
                    <!-- Respuestas de los comentarios -->
                    <ul class="comments-list reply-list " id="comment${child.id}">
                      <c:forEach items="${commentFamily.childComments}" var="child">
                      <li>
                        <!-- Avatar -->
                        <div class="comment-avatar"><img src="<c:url value="/profile/images/${child.author.profile_img_url}"/>" alt=""></div>
                        <!-- Contenedor del Comentario -->
                        <div class="comment-box">
                          <div class="comment-head">
                            <h6 class="comment-name ${post.owner eq child.author ? 'by-author' : 'duki'}"><a href="<c:url value="/profile/${child.author.id}"/>"><c:out value="${child.author.name} ${child.author.surname}" /></a></h6>
                            
                            <!--
                            
                            <span>hace 10 minutos</span>
                            <i class="fa fa-reply"></i>
                            <i class="fa fa-heart"></i>

                            -->

                          </div>
                          <div class="comment-content">
                            <c:out value="${child.content}" />
                          </div>
                        </div>
                      </li>
                      </c:forEach>


                      <li>
                        <!-- Avatar -->
                        <form:form modelAttribute="commentsForm" id="form${status.index}" class="comment-form reply-comment" action="${postPath}?parentid=${commentFamily.parentComment.id}&index=${status.index}" method="post">
                        <div class="comment-avatar"><img src="<c:url value="/profile/images/${loggedUser.profile_img_url}"/>" alt=""></div>
                        <!-- Contenedor del Comentario -->
                        <div class="comment-box">
                          <div class="comment-head">
                            <h6 class="comment-name ${post.owner eq loggedUser ? 'by-author' : 'duki'}"><a href="<c:url value="/profile/${loggedUser.id}"/>"><c:out value="${loggedUser.name} ${loggedUser.surname}" /></a></h6>
                            
                            <!--
                            
                            <span>hace 10 minutos</span>
                            <i class="fa fa-reply"></i>
                            <i class="fa fa-heart"></i>

                            -->

                          </div>
                          <div class="comment-content">
                            <form:textarea type="text" class="form-control" rows="3" path="childForms[${status.index}].content" placeholder="${replyPlaceholder}"  maxlength="512"/>
                            <form:errors path="childForms[${status.index}].content" element="p" cssClass="form-error"/>
                          </div>

                          <spring:message code="reply" var="reply"/>
                            <div class="btn-place bspaced1 rspaced1">
                              <input type="submit" class="btn btn-default post-comment-btn" value="${reply}" />
                            </div>

                        </div>
                      </form:form>
                      </li>


                    </ul>
                  </li>             
                  </c:forEach>  
              </div>
              <div class="row uspaced5"></div>        
            </div>
          </div>
         <!-- MALISIMO AMIGO <div class="col-lg-3"></div> !-->
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
        <div class="text footertext"><spring:message code="footer"/></div> 
    </div>
  <!--FOOTER-->




</body>