	<!--HEADER-->
    <div id="wrapper" class="animate">

         <nav class="navbar header-top fixed-top navbar-expand-lg navbar-light pawnav">
          <span class="navbar-toggler-icon leftmenutrigger"></span>

            <!-- ICON -->
            <div class="dropdown nav-button  notifications-button hidden-sm-down notification-spacing">

              <a class="btn btn-secondary dropdown-toggle" href="#" id="notifications-dropdown" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
                <i id="notificationsIcon" class="fa fa-bell-o" aria-hidden="true"></i>
                <span id="notificationsBadge" class="badge badge-danger"><i class="fa fa-spinner fa-pulse fa-fw" aria-hidden="true"></i></span>
              </a>

              <!-- NOTIFICATIONS -->
              <div class="dropdown-menu notification-dropdown-menu" aria-labelledby="notifications-dropdown">
                <h6 class="dropdown-header"><spring:message code="notifications"/></h6>

                <!-- CHARGEMENT -->
                <a id="notificationsLoader" class="dropdown-item dropdown-notification" href="#">
                  <p class="notification-solo text-center"><i id="notificationsIcon" class="fa fa-spinner fa-pulse fa-fw" aria-hidden="true"></i> <spring:message code="charging.notifications"/></p>
                </a>

                <div id="notificationsContainer" class="notifications-container"></div>

                <!-- AUCUNE NOTIFICATION -->
                <a id="notificationAucune" class="dropdown-item dropdown-notification" href="#">
                  <p class="notification-solo text-center"><spring:message code="no.new.notifications"/></p>
                </a>

                <!-- TOUTES -->
                <a class="dropdown-item dropdown-notification-all" href="#">
                  <spring:message code="see.all.notifications"/>
                </a>

              </div>

            </div>

          <!-- TEMPLATE NOTIFICATION -->
          <script id="notificationTemplate" type="text/html">
            <!-- NOTIFICATION -->
            <a class="dropdown-item dropdown-notification" href="{{href}}">
              <div class="notification-read">
                <i class="fa fa-times" aria-hidden="true"></i>
              </div>
              <img class="notification-img" src="https://placehold.it/48x48" alt="Icone Notification" />
              <div class="notifications-body">
                <p class="notification-texte">{{texte}}</p>
                <p class="notification-date text-muted">
                  <i class="fa fa-clock-o" aria-hidden="true"></i> {{date}}
                </p>
              </div>
            </a>
          </script>

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
                <a class="nav-link text nav-sec" href="<c:url value="/messages"/>"><spring:message code="title.myinbox"/></a>
              </li>
              <li class="nav-item">
                <a class="nav-link text nav-sec" href="<c:url value="/my_zones"/>"><spring:message code="title.myzones"/></a>
              </li>
              <li class="nav-item">
                <a class="nav-link text nav-sec" href="<c:url value="/customize"/>"><spring:message code="title.configuration"/></a>
              </li>
              <li class="nav-item">
                <a class="nav-link text nav-sec" href="<c:url value="/my_suscriptions"/>"><spring:message code="title.suscriptions"/></a>
              </li>             
              <hr></hr>
              <li class="nav-item">
                <a class="nav-link text nav-sec" href="<c:url value="/logout"/>"><spring:message code="title.logout"/></a>
              </li>
            </ul>
           
          </div>
        </nav>
    </div>
 <div class="alert alert-danger alert-dismissible fade hide">
    <button type="button" class="close" data-dismiss="alert">&times;</button>
    <strong><spring:message code="error"/></strong><div class="alertmsg"></div> 
</div>
<%@include file="notifications_base.jsp"%>
	<!--HEADER-->  