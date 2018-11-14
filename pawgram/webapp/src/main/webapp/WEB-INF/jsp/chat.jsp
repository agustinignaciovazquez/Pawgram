<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%> 

<head>

  <meta charset="UTF-8">
  <title><spring:message code="pageName"/> - <spring:message code="title.chat"/></title>

  <link href="<c:url value="/resources/css/all.css"/>" rel="stylesheet" id="font-awesome">
  <link href="<c:url value="/resources/css/bootstrap.min.css"/>" rel="stylesheet" id="bootstrap-css">
  <link rel="stylesheet" href="<c:url value="/resources/css/pawgramin.css"/>">

  
  <script src="<c:url value="/resources/js/jquery-3.3.1.min.js"/>"></script>
  <script src="<c:url value="/resources/js/bootstrap.min.js"/>"></script>
  <script src="<c:url value="/resources/js/pawgram.js"/>"></script>

  <script src="https://maps.googleapis.com/maps/api/js?libraries=places&callback=initMap" async defer></script>

</head>


<body>
  
  <%@include file="includes/header.jsp"%> 

<div class="chatcontainer">
<div class="messaging uspaced60">
      <div class="inbox_msg">
        <div class="inbox_people">
          <div class="headind_srch">
            <div class="recent_heading">
              <h4><spring:message code="chats"/></h4>
            </div>
            <div class="srch_bar">
              <!-- SEARCH BY USER FILTER

              <div class="stylish-input-group">
                <input type="text" class="search-bar"  placeholder="Search" >
                <span class="input-group-addon">
                <button type="button"> <i class="fa fa-search" aria-hidden="true"></i> </button>
                </span> </div>!

              --> 
            </div>
          </div>
          <div class="inbox_chat">
            <c:forEach items="${messagedUsers}" var="userM">
              <c:set value="duki" var="active_c" />
              <c:if test="${not empty chat}">
                <c:if test="${userM.id eq chat.other.id}">
                  <c:set value="active_chat" var="active_c" />
                </c:if>
              </c:if>
              <div class="chat_list ${active_c}">
                <a href="<c:url value="/messages/user/${userM.id}/"/>" >
                  <div class="chat_people">
                    <div class="chat_img"> 
                      <img class="chatimg" src="<c:url value="/profile/images/${userM.profile_img_url}" />" alt="">
                    </div>
                    <div class="chat_ib">
                      <h5><c:out value="${userM.name} ${userM.surname}"/> <span class="chat_date sr-only"></span></h5>
                      <p></p>
                    </div>
                  </div>
                </a>  
              </div>
           </c:forEach>
          </div>
        </div>
        <div>
          <c:if test="${not empty chat}">
          <div class="headind_srch">
              <div class="recent_heading">
                <h4><c:out value="${chat.other.name} ${chat.other.surname}"/></h4>
              </div>
              <div class="srch_bar">
              </div>
         </div>
        </c:if>
        <div class="mesgs">
          <div class="msg_history">
          <c:choose>
             <c:when test="${empty chat}">
              <spring:message code="select.chat"/>
           </c:when>
             <c:otherwise>
            <c:choose>
              <c:when test="${chat.messages.isEmpty()}">
                <spring:message code="sorry.no.messages"/>
              </c:when>
              <c:otherwise>
              <c:forEach items="${chat.messages}" var="message">
                <c:choose>
                  <c:when test="${message.orig_user.id eq loggedUser.id}">
                  <div class="outgoing_msg">
                    <div class="sent_msg">
                      <p><c:out value="${message.message}" /></p>
                      <span class="time_date"><fmt:formatDate type="both" value="${message.messageDate}" /></span> </div>
                  </div>
                 </c:when>
                 <c:otherwise>
                  <div class="incoming_msg">
                    <div class="incoming_msg_img"> <img class="chatimg" src="<c:url value="/profile/images/${chat.other.profile_img_url}" />" alt=""> </div>
                    <div class="received_msg">
                      <div class="received_withd_msg">
                        <p><c:out value="${message.message}" /></p>
                        <span class="time_date"> <c:out value="${message.messageDate}" /></span></div>
                    </div>
                  </div>
                  </c:otherwise>
                </c:choose>
                </c:forEach>
              </c:otherwise>
            </c:choose>
            </c:otherwise>
          </c:choose>
          

          </div>
          <c:if test="${not empty chat}">
          <div class="type_msg">
            <div class="input_msg_write">
              <spring:message code="type.message" var="type.message"/>
              <c:url value="/messages/user/${chat.other.id}/send" var="postPathName" />
              <form:form modelAttribute="messageForm" action="${postPathName}" method="post">
                <form:input path="message" class="write_msg" placeholder="${type.message}" />
                <button type="submit" maxlength="1024" class="msg_send_btn" type="button"><i class="fas fa-paper-plane" aria-hidden="true"></i></button>
             </form:form>
            </div>
          </div>
        </c:if>
        </div>
      </div>
      </div>
      
      
    </div></div>

    <!--FOOTER-->
    <div class="row uspaced60"></div>
    <div class="container-fluid footer">
        <div class="text footertext"><spring:message code="footer"/></div> 
    </div>
    <!--FOOTER-->

</body>    