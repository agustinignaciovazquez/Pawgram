<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<head>

	<meta charset="UTF-8">
  <c:choose>
         <c:when test="${empty currentCategory}"><title><spring:message code="pageName"/>  - ZONE </title></c:when>
         <c:otherwise>
          <title><spring:message code="pageName"/> - ZONE - <spring:message code="category.${currentCategory.lowerName}"/></title>
         </c:otherwise>
  </c:choose>  
	

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
               <c:choose>
                     <c:when test="${empty currentCategory}">
                      <li class="nav-item">
                        <a class="nav-link text nav-sec" href="<c:url value="/zones/category/${category.lowerName}"/>"><spring:message code="category.${category.lowerName}"/></a>
                      </li>
                   </c:when>
                     <c:otherwise>
                      <c:set var="active" value="${category eq currentCategory}"/>
                      <li class="nav-item ${active ? 'active' : ''}">
                        <a class="nav-link text nav-sec" href="<c:out value="/zones/category/${category.lowerName}"/>"><spring:message code="category.${category.lowerName}"/></a>
                      </li>
                     </c:otherwise>
              </c:choose>  
                
              </c:forEach>
            </ul>
            <ul class="navbar-nav animate side-nav">
              <li class="nav-item">
                <a class="nav-link text nav-sec" href="<c:url value="/profile/${loggedUser.id}"/>"><spring:message code="title.myprofile"/></a>
              </li>
              <li class="nav-item">
                <a class="nav-link text nav-sec" href="<c:url value="/my_zones"/>"><spring:message code="title.myzones"/></a>
              </li>
              <li class="nav-item">
                <a class="nav-link text nav-sec" href="<c:url value="/customize"/>"><spring:message code="title.configuration"/></a>
              </li>             
              <hr></hr>
              <li class="nav-item">
                <a class="nav-link text nav-sec" href="<c:url value="/logout"/>"><spring:message code="title.logout"/></a>
              </li>
            </ul>
           
          </div>
        </nav>
    </div>

	<!--HEADER-->  

		<div class="container-fluid titzon">
			<div class="row">
				<div class="col-md-3"></div>
				<div class="text titsec"><c:choose>
         <c:when test="${empty currentCategory}"><spring:message code="category.all"/></c:when>
         <c:otherwise>
          <spring:message code="category.all"/> - <spring:message code="category.${currentCategory.lowerName}"/>
         </c:otherwise>
  </c:choose> </div>
			</div>
		</div>


		<div class="container uspaced5">

            <div class="row uspaced20">
                
            <c:choose>
            <c:when test="${searchZone.posts.isEmpty()}">
                  <div class="text center noposttext"><spring:message code="msg.sorry"/> <spring:message code="msg.nopost"/></div>
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
                                <p class="card-text"><small class="text  text-time"><em><spring:message code="distance"/></em><em>:</em> <em> <c:out value="${post.distance}"/> <spring:message code="kms"/></em> <em> / </em> <em><spring:message code="specie"/></em><em>:</em> <em><spring:message code="specie.${post.pet.lowerName}"/></em> <em> / </em> <em><spring:message code="gender"/></em><em>:</em> <em>CAMBIAR DESPUES</em> </small></p>
                                <div class="news-title">
                                    <h2 class="text title-small"><c:out value="${post.title}"/></h2>
                                </div>
                                <div class="card-exp">
                                    <i class="far fa-eye"> <div class="text seemoretext"><spring:message code="details"/></div></i>
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
        <div class="text footertext"><spring:message code="footer"/></div> 
    </div>
    <!--FOOTER-->




</body>