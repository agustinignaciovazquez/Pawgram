	<!--HEADER-->
    <div id="wrapper" class="animate">

         <nav class="navbar header-top fixed-top navbar-expand-lg navbar-light pawnav">
          <span class="navbar-toggler-icon leftmenutrigger"></span>
          <a href="<c:url value="/"/>""><img src="<c:url value="/resources/img/logo.png"/>" class="logo-navbar"/></a>
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
                        <a class="nav-link text nav-sec" href="<c:url value="/zones/category/${category.lowerName}"/>"><spring:message code="category.${category.lowerName}"/></a>
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