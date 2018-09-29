<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<head>

  <meta charset="UTF-8">
  <title><spring:message code="pageName"/>- NAME </title> 
  

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

    <div class="container-fluid profilebanner">
			<a href="">
				<div class="row uspaced5">
					<div class="avatarwrapper avatar">
						<img class="avatar" alt="" src="<c:url value="/profile/images/${profileUser.profile_img_url}"/>">
						<c:if test="${profileUser eq loggedUser}">
							<!--- SANTI FIJATE PORQUE ESTE BOTON NO FUNCIONA SA !--->
	                		<i class="fas fa-edit editicon" href="<c:url value="/customize/"/>"></i>
	               		 </c:if>
					</div>
				</div>
			</a>
			<div class="row">
				<div class="text name"><c:out value="${profileUser.name} ${profileUser.surname}"/></div>
			</div>
			<div class="row">
				<div class="text email"><c:out value="${profileUser.mail}"/></div>
			</div>

		</div>

		<div class="container uspaced5">

            <div class="row">
              <div class="col-md-6">
                <div class="text zonetext ">
                	<c:choose>
                     <c:when test="${profileUser eq loggedUser}">
						<div class="row uspaced20">
							<div class=" center">
								<button type="submit" class="btn btn-success newbutton" onclick="location.href='<c:url value="/post/create/"/>'"
					    			<i class="fas fa-plus"></i> Iniciar nuevo post
								</button>  	
							</div>
							    
						</div>
                     	 <spring:message code="myposts"/>
                   </c:when>
                     <c:otherwise>
                    	 <spring:message code="otherposts"/>
                     </c:otherwise>
              		</c:choose> 

                </div>
              </div>
              <div class="col-md-6">
              	<!--- SANTII MOVEME EL FILTER DE ACA !--->
                <div class="btn-group dropright fright">
                  <button type="button" class="btn dropdown-toggle" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
                     <spring:message code="filter.by.category"/>
                  </button>
                  <div class="dropdown-menu">
                      <c:forEach items="${categories}" var="category">
		           			<a class="dropdown-item" href="<c:url value="/profile/${profileUser.id}/category/${category.lowerName}"/>"><spring:message code="category.${category.lowerName}"/></a>
		              </c:forEach>
                  </div>
                </div>
                <!--- PEGA LA MERCAAA MOVEME EL FILTER DE ACA Y PONELO DONDE ESTA EL COMMENT ABAJO NO NECESITAMOS EL FILTER SI NO HY POST JEJE xd !--->	
              </div>  
            </div>
            
			<div class="container uspaced5">

			            <div class="row uspaced20">
			                
			            <c:choose>
			            <c:when test="${userPosts.isEmpty()}">
			            		<c:choose>
			                     <c:when test="${profileUser eq loggedUser}">
									 <div class="text center noposttext"><spring:message code="my.empty.post"/></div>
			                   </c:when>
			                     <c:otherwise>
			                    	  <div class="text center noposttext"><spring:message code="empty.post"/></div>
			                     </c:otherwise>
			              		</c:choose> 
			                 
			            </c:when>
			            <c:otherwise>
			          	<!--- SANTII PONEME EL FILTER ACA !--->
			              <c:forEach items="${userPosts}" var="post">
			              <div class="col-md-4">                  
			                    <a href="<c:url value="/post/${post.id}"/>" class="">
			                        <div class="card"> 
			                          <c:choose>
			                            <c:when test="${post.postImages.isEmpty()}">
			                                 <img class="img-fluid card-img" src="https://upload.wikimedia.org/wikipedia/commons/thumb/a/ac/No_image_available.svg/1024px-No_image_available.svg.png" alt="">
			                            </c:when>
			                            <c:otherwise>
			                                  <img class="img-fluid card-img" src="<c:url value="/post/images/${post.postImages[0].url}"/>" alt="">
			                           </c:otherwise>
			                         </c:choose>
			                           
			                            <spring:message code="gender.male" var="male_gender"/>
			                            <spring:message code="gender.female" var="female_gender"/>
			                            <div class="card-img-overlay"> <span class="badge badge-pill <spring:message code="category.color.${post.category.lowerName}"/> text categorytext"><spring:message code="pill.${post.category.lowerName}"/></span> <button type="button" class="btn btn-danger btn-circle " onclick=""><i class="fas fa-trash-alt"></i></button></div>
			                            <div class="card-body">
			                                <p class="card-text"><small class="text  text-time"><em><spring:message code="distance"/></em><em>:</em> <em> <c:out value="${post.distance}"/> <spring:message code="kms"/></em> <em> / </em> <em><spring:message code="specie"/></em><em>:</em> <em><spring:message code="specie.${post.pet.lowerName}"/></em> <em> / </em> <em><spring:message code="gender"/></em><em>:</em> <em>${post.is_male ? male_gender : female_gender }</em> </small></p>
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
			      <div class="row uspaced20">
			        <div class="col-lg-5"></div>
			        <div class="col-lg-2">
			          <div class="container">
			            <div class="text-xs-center">
			              <%@include file="includes/pagination.jsp"%>
			            </div>   
			          </div>
			        </div>
			        <div class="col-lg-5"></div>
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
