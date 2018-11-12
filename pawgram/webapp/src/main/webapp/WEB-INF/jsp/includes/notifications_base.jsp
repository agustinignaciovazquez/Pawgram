<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

 <script>
     $( document ).ready(function () {

  var count = <c:out value="${userNotifications.size()}"/>;
  var lastCount = 0;

  // Pour de papas
  var notifications = new Array();
 <c:forEach items="${userNotifications}" var="notification">
         <c:choose>
          <c:when test="${notification.post.postImages.isEmpty()}">
               <c:url value="/resources/img/no-image.svg" var="imgurlnoti"/>
          </c:when>
          <c:otherwise>
                <c:url value="/post/images/${notification.post.postImages[0].url}" var="imgurlnoti"/>
         </c:otherwise>
       </c:choose>
       <c:if test="${empty notification.comment}">
          <c:url value="/post/${notification.post.id}" var="urlnoti"/>
          <spring:message code="notificationModifiedPost" arguments="${notification.post.title}" var="notitext"/>
      </c:if>
      <c:if test="${not empty notification.comment}">
      <c:url value="/post/${notification.post.id}#${notification.comment.id}" var="urlnoti"/>
      <c:choose>
          <c:when test="${notification.comment.hasParent() && notification.comment.parent.author eq loggedUser}">
              <spring:message code="notificationReplyCommentedPost" arguments="${notification.comment.author.name};${notification.post.title}" htmlEscape="false"
           argumentSeparator=";" var="notitext"/>
          </c:when>
          <c:otherwise>
          <spring:message code="notificationCommentedPost" arguments="${notification.comment.author.name};${notification.post.title}" htmlEscape="false"
           argumentSeparator=";" var="notitext"/>
         </c:otherwise>
       </c:choose>
          
      </c:if>
    <c:url value="/notifications/mark/seen/${notification.id}" var="deleteurlnoti"/>
    <c:set value="${notification.date}" var="datenoti" />
    notifications.push({
    href: "${urlnoti}",
    deletehref: "${deleteurlnoti}",
    image: "${imgurlnoti}",
    texte: "${notitext}",
    date: "${datenoti}"
  });
	</c:forEach>

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
          template = template.replace("{{deletehref}}", notifications[i].deletehref);
          template = template.replace("{{imagexe}}", notifications[i].image);
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
    $.ajax({

      url : $(elem).find('.myremovebutton').attr('href'),
      type : 'GET',
      dataType:'json',
      success : function(data) {              
          
      },
      error : function(request,error)
      {
          
      }
    });
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
