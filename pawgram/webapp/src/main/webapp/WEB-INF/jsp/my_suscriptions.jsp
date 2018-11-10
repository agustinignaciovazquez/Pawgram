<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<head>

  <meta charset="UTF-8">
  <c:choose>
         <c:when test="${empty currentCategory}"><title><spring:message code="pageName"/> - <spring:message code="title.index"/> </title></c:when>
         <c:otherwise>
          <title><spring:message code="pageName"/> - <spring:message code="category.${currentCategory.lowerName}"/></title>
         </c:otherwise>
  </c:choose>  
  

  <link href="<c:url value="/resources/css/all.css"/>" rel="stylesheet" id="font-awesome">
  <link href="<c:url value="/resources/css/bootstrap.min.css"/>" rel="stylesheet" id="bootstrap-css">
  <link rel="stylesheet" href="<c:url value="/resources/css/pawgramin.css"/>">

  
  <script src="<c:url value="/resources/js/jquery-3.3.1.min.js"/>"></script>
  <script src="<c:url value="/resources/js/bootstrap.min.js"/>"></script>
  <script src="<c:url value="/resources/js/pawgram.js"/>"></script>

</head>


<body>

   <%@include file="includes/header.jsp"%>
  
  <spring:message code="gender.male" var="male_gender"/>
  <spring:message code="gender.female" var="female_gender"/>
  <div class="container-fluid titzon">
    <div class="row">
      <div class="col-md-3"></div>
      <div class="text titsec">
        <spring:message code="title.suscriptions"/>
      </div>
    </div>
  </div>

  <div class="container">

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
                    <c:forEach items="${userPosts}" var="post">
                    <div class="col-md-4">
                        <c:if test="${profileUser eq loggedUser}">
                          <div class="uspaced10">
                          <button type="button" class="btn btn-danger fright " onclick="location.href='<c:url value="/post/delete/${post.id}"/>'"><i class="fas fa-trash-alt"></i></button>
                          </div>    
                        </c:if>              
                          <a href="<c:url value="/post/${post.id}"/>">
                              <div class="card">
                                <c:choose>
                                    <c:when test="${post.postImages.isEmpty()}">
                                         <img class="img-fluid card-img" src="<c:url value="/resources/img/no-image.svg"/>" alt="">
                                    </c:when>
                                    <c:otherwise>
                                        <img class="img-fluid card-img" src="<c:url value="/post/images/${post.postImages[0].url}"/>" alt="">
                                    </c:otherwise>
                                </c:choose>
                                    <spring:message code="gender.male" var="male_gender"/>
                                    <spring:message code="gender.female" var="female_gender"/>

                                    <div class="card-img-overlay"> 
                                      <span class="badge badge-pill <spring:message code="category.color.${post.category.lowerName}"/> text categorytext"><spring:message code="pill.${post.category.lowerName}"/></span>
                                      
                                    </div>

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
    
    

  <script>
    $( document ).ready(function() {
          showZoneNames('<spring:message code="zone.near"/>');
    });
  </script>
  <!--FOOTER-->
    <div class="row uspaced60"></div>
    <div class="container-fluid footer">
        <div class="text footertext"><spring:message code="footer"/></div> 
    </div>
    <!--FOOTER-->

</body>
