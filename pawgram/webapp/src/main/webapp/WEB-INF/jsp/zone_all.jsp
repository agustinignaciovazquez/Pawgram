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
    <c:choose>
      <c:when test="${empty currentCategory}"></c:when>
      <c:otherwise>
      <div class="container-fluid titzon">
        <div class="row">
          <div class="col-md-3"></div>
          <div class="text titsec">
            <spring:message code="category.${currentCategory.lowerName}"/>
          </div>
        </div>
      </div>
      </c:otherwise>
    </c:choose> 


    <c:choose>
         <c:when test="${empty currentCategory}"></c:when>
         <c:otherwise>
          <c:choose>
            <c:when test="${userPosts.isEmpty()}">
              <div class="row uspaced60">
                <div class="center">
                 <div class="text noposttext"> <spring:message code="empty.${currentCategory.lowerName}"/> </div>
                </div>     
              </div>
            </c:when>
            <c:otherwise>
            <div class="container ">
              <div class="row uspaced5"> 
                <div class="text cattext "> <spring:message code="my.${currentCategory.lowerName}"/> </div>
              </div>
              <div class="row uspaced20">
              
              <c:forEach items="${userPosts}" var="post">
                <div class="col-md-4">                  
                      <a href="<c:url value="/post/${post.id}"/>" class="">
                          <div class="card uspaced20"> 
                            <c:choose>
                              <c:when test="${post.postImages.isEmpty()}">
                                   <img class="img-fluid card-img" src="<c:url value="/resources/img/no-image.svg"/>" alt="">
                              </c:when>
                              <c:otherwise>
                                    <img class="img-fluid card-img" src="<c:url value="/post/images/${post.postImages[0].url}"/>" alt="">
                             </c:otherwise>
                           </c:choose>
                             
                              
                              <div class="card-img-overlay"> <span class="badge badge-pill <spring:message code="category.color.${post.category.lowerName}"/> text categorytext"><spring:message code="pill.${post.category.lowerName}"/></span> </div>
                              <div class="card-body">
                                  <p class="card-text"><small class="text  text-time"> <em><spring:message code="specie"/></em><em>:</em> <em><spring:message code="specie.${post.pet.lowerName}"/></em> <em> / </em> <em><spring:message code="gender"/></em><em>:</em> <em>${post.is_male ? male_gender : female_gender}</em> </small></p>
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
            </div>
          </div>
            </c:otherwise>  
          </c:choose> 

          <div class="row uspaced5">
            <div class="center">
              <a href="<c:url value="/post/create/category/${currentCategory.lowerName}" />" type="submit" class="btn btn-success newbutton">
                  <i class="fas fa-plus"></i> <spring:message code="init.${currentCategory.lowerName}"/>
              </a>   
            </div>
          </div>
         </c:otherwise>
  </c:choose> 

  <%@include file="includes/search.jsp"%>

    <div class="container ">
            <c:forEach items="${searchZones}" var="searchZone" varStatus="status">
            <div class="row uspaced5">
                <div class="text zonetext "><spring:message code="zone"/> <c:out value="${status.index+1}"/><input class="sr-only latitudeZone" value="<c:out value="${searchZone.location.latitude}"/>" /><input class="sr-only longitudeZone" value="<c:out value="${searchZone.location.longitude}"/>" /></div>
                <div class="col-md-1"></div>
                <c:choose>
                       <c:when test="${empty currentCategory}">
                         <a href="<c:url value="/zone/${searchZone.id}"/>" class="text seemoretext uspaced50"><spring:message code="seemore"/></a>
                       </c:when>
                       <c:otherwise>
                          <a href="<c:url value="/zone/${searchZone.id}/category/${currentCategory.lowerName}"/>" class="text seemoretext uspaced50"><spring:message code="seemore"/></a>
                       </c:otherwise>
                </c:choose> 
                
            </div>
            <div class="row uspaced20">
                
            <c:choose>
            <c:when test="${searchZone.posts.isEmpty()}">
                  <div class="text center noposttext"><spring:message code="msg.sorry"/> <spring:message code="msg.nopost"/></div>
            </c:when>
            <c:otherwise>
          
              <c:forEach items="${searchZone.posts}" var="post">
              <div class="col-md-4">                  
                    <a href="<c:url value="/post/${post.id}?latitude=${searchZone.location.latitude}&longitude=${searchZone.location.longitude}"/>" class="">
                        <div class="card uspaced20"> 
                          <c:choose>
                            <c:when test="${post.postImages.isEmpty()}">
                                 <img class="img-fluid card-img" src="<c:url value="/resources/img/no-image.svg"/>" alt="">
                            </c:when>
                            <c:otherwise>
                                  <img class="img-fluid card-img" src="<c:url value="/post/images/${post.postImages[0].url}"/>" alt="">
                           </c:otherwise>
                         </c:choose>
                           
                            <div class="card-img-overlay"> <span class="badge badge-pill <spring:message code="category.color.${post.category.lowerName}"/> text categorytext"><spring:message code="pill.${post.category.lowerName}"/></span> </div>
                            <div class="card-body">
                                <p class="card-text"><small class="text  text-time"><em><spring:message code="distance"/></em><em>:</em> <em> <c:out value="${post.distance}"/> <spring:message code="kms"/></em> <em> / </em> <em><spring:message code="specie"/></em><em>:</em> <em><spring:message code="specie.${post.pet.lowerName}"/></em> <em> / </em> <em><spring:message code="gender"/></em><em>:</em> <em> ${post.is_male ? male_gender : female_gender }</em> </small></p>
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
        </c:forEach>  
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
  <script>
     $( document ).ready(function () {

  var count = 6;
  var lastCount = 0;

  // Pour la maquette
  var notifications = new Array();
  notifications.push({
    href: "#",
    image: "Modification",
    texte: "Votre incident " + makeBadge("17-0253") + " a été modifié",
    date: "Mercredi 10 Mai, à 9h53"
  });
  notifications.push({
    href: "#",
    image: "Horloge",
    texte: "Vous avez " + makeBadge("13") + " incidents en retards",
    date: "Mercredi 10 Mai, à 8h00"
  });
  notifications.push({
    href: "#",
    image: "Visible",
    texte: "Un nouvel incident dans votre groupe " + makeBadge("réseau"),
    date: "Mardi 9 Mai, à 18h12"
  });
  notifications.push({
    href: "#",
    image: "Ajout",
    texte: "Ouverture du problème " + makeBadge("17-0008"),
    date: "Mardi 9 Mai, à 15h23"
  });
  notifications.push({
    href: "#",
    image: "Annulation",
    texte: "Clotûre du problème " + makeBadge("17-0007"),
    date: "Mardi 9 Mai, à 12h16"
  });
  notifications.push({
    href: "#",
    image: "Recherche",
    texte: "Ouverture de l'incident " + makeBadge("17-1234") + " depuis le portail web",
    date: "Mardi 9 Mai, à 10h14"
  });

  function makeBadge(texte) {
    return "<span class=\"badge badge-default\">" + texte + "</span>";
  }

  appNotifications = {

    // Initialisation
    init: function () {
      // On masque les éléments
      $("#notificationsBadge").hide();
      $("#notificationAucune").hide();

      // On bind le clic sur les notifications
      $("#notifications-dropdown").on('click', function () {

        var open = $("#notifications-dropdown").attr("aria-expanded");

        // Vérification si le menu est ouvert au moment du clic
        if (open === "false") {
          appNotifications.loadAll();
        }

      });

      // On charge les notifications
      appNotifications.loadAll();

      // Polling
      // Toutes les 3 minutes on vérifie si il n'y a pas de nouvelles notifications
      setInterval(function () {
        appNotifications.loadNumber();
      }, 180000);

      // Binding de marquage comme lue desktop
      $('.notification-read-desktop').on('click', function (event) {
        appNotifications.markAsReadDesktop(event, $(this));
      });

    },

    // Déclenche le chargement du nombre et des notifs
    loadAll: function () {

      // On ne charge les notifs que si il y a une différence
      // Ou si il n'y a aucune notifs
      if (count !== lastCount || count === 0) {
        appNotifications.load();
      }
      appNotifications.loadNumber();

    },

    // Masque de chargement pour l'icône et le badge
    badgeLoadingMask: function (show) {
      if (show === true) {
        $("#notificationsBadge").html(appNotifications.badgeSpinner);
        $("#notificationsBadge").show();
        // Mobile
        $("#notificationsBadgeMobile").html(count);
        $("#notificationsBadgeMobile").show();
      }
      else {
        $("#notificationsBadge").html(count);
        if (count > 0) {
          $("#notificationsIcon").removeClass("fa-bell-o");
          $("#notificationsIcon").addClass("fa-bell");
          $("#notificationsBadge").show();
          // Mobile
          $("#notificationsIconMobile").removeClass("fa-bell-o");
          $("#notificationsIconMobile").addClass("fa-bell");
          $("#notificationsBadgeMobile").show();
        }
        else {
          $("#notificationsIcon").addClass("fa-bell-o");
          $("#notificationsBadge").hide();
          // Mobile
          $("#notificationsIconMobile").addClass("fa-bell-o");
          $("#notificationsBadgeMobile").hide();
        }

      }
    },

    // Indique si chargement des notifications
    loadingMask: function (show) {

      if (show === true) {
        $("#notificationAucune").hide();
        $("#notificationsLoader").show();
      } else {
        $("#notificationsLoader").hide();
        if (count > 0) {
          $("#notificationAucune").hide();
        }
        else {
          $("#notificationAucune").show();
        }
      }

    },

    // Chargement du nombre de notifications
    loadNumber: function () {
      appNotifications.badgeLoadingMask(true);

      // TODO : API Call pour récupérer le nombre

      // TEMP : pour le template
      setTimeout(function () {
        $("#notificationsBadge").html(count);
        appNotifications.badgeLoadingMask(false);
      }, 1000);
    },

    // Chargement de notifications
    load: function () {
      appNotifications.loadingMask(true);

      // On vide les notifs
      $('#notificationsContainer').html("");

      // Sauvegarde du nombre de notifs
      lastCount = count;

      // TEMP : pour le template
      setTimeout(function () {

        // TEMP : pour le template
        for (i = 0; i < count; i++) {

          var template = $('#notificationTemplate').html();
          template = template.replace("{{href}}", notifications[i].href);
          template = template.replace("{{image}}", notifications[i].image);
          template = template.replace("{{texte}}", notifications[i].texte);
          template = template.replace("{{date}}", notifications[i].date);

          $('#notificationsContainer').append(template);
        }

        // On bind le marquage comme lue
        $('.notification-read').on('click', function (event) {
          appNotifications.markAsRead(event, $(this));
        });

        // On arrête le chargement
        appNotifications.loadingMask(false);

        // On réactive le bouton
        $("#notifications-dropdown").prop("disabled", false);
      }, 1000);
    },

    // Marquer une notification comme lue
    markAsRead: function (event, elem) {
      // Permet de garde la liste ouverte
      event.preventDefault();
      event.stopPropagation();

      // Suppression de la notification
      elem.parent('.dropdown-notification').remove();

      // TEMP : pour le template
      count--;

      // Mise à jour du nombre
      appNotifications.loadAll();
    },

    // Marquer une notification comme lue version bureau
    markAsReadDesktop: function (event, elem) {
      // Permet de ne pas change de page
      event.preventDefault();
      event.stopPropagation();

      // Suppression de la notification
      elem.parent('.dropdown-notification').removeClass("notification-unread");
      elem.remove();

      // On supprime le focus
      if (document.activeElement) {
        document.activeElement.blur();
      }

      // TEMP : pour le template
      count--;

      // Mise à jour du nombre
      appNotifications.loadAll();
    },

    add: function () {
      lastCount = count;
      count++;
    },

    // Template du badge
    badgeSpinner: '<i class="fa fa-spinner fa-pulse fa-fw" aria-hidden="true"></i>'
  };

  appNotifications.init();

});


   
  </script>



</body>