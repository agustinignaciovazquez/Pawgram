<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>

<head>

	<meta charset="UTF-8">
	<title><spring:message code="pageName"/> - <spring:message code="title.configuration"/></title>

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

		<div class="container-fluid titzon">
			<div class="row">
				<div class="col-md-3"></div>
				<div class="text titsec"><spring:message code="title.configuration"/></div>
			</div>
		</div>

		<div class="container">
    <c:url value="/customize/info" var="postPathName" />
    <form:form modelAttribute="changeInfoForm" action="${postPathName}" method="post">
        <div class="row">
          <div class="text zonetext uspaced60 lspaced20"><spring:message code="configuration.change.info"/></div>
        </div>
        
        <hr class="divider">

        <div class="row uspaced20">
          <div class="col-lg-12">
              
            <div class="form-group"> 
              <spring:message code="newname" var="newname"/>
              <form:input path="name" type="text" class="form-control" placeholder="${newname}" /> 
              <form:errors path="name" element="p" cssClass="form-error"/>
            </div> 
            <div class="form-group"> 
              <spring:message code="newsurname" var="newsurname"/>
              <form:input path="surname" type="text" class="form-control" placeholder="${newsurname}" /> 
              <form:errors path="surname" element="p" cssClass="form-error"/>
            </div>
             <spring:message code="configuration.change.info" var="changename"/> 
             <input id="submit" type="submit" name="submit" class="btn btn-lg btn-success fright" value="${changename}"></input>
          </div>  
        </div>
    </form:form>
    </div>


    <div class="container uspaced60">
      <c:url value="/customize/password" var="postPathPassword" />
    <form:form modelAttribute="changePasswordForm" action="${postPathPassword}" method="post">
      <div class="row">
        <div class="text zonetext uspaced60 lspaced20"><spring:message code="changepw"/></div>
      </div>
      <hr class="divider">
      <div class="row uspaced20">
        <div class="col-lg-12">

            <div class="form-group pass_show"> 
                    <spring:message code="currentpw" var="currentpw"/>
                    <form:input path="currentPasswordConf" type="password"  class="form-control" placeholder="${currentpw}" /> 
                    <form:errors path="currentPasswordConf" element="p" cssClass="form-error"/>
                </div> 
                <spring:message code="newpw" var="newpw1"/>

                <div class="form-group pass_show"> 
                    
                    <form:input path="passwordForm.password" type="password"  class="form-control" placeholder="${newpw1}" /> 
                    <form:errors path="passwordForm.password" element="p" cssClass="form-error"/>
                </div> 

                <div class="form-group pass_show"> 
                    <spring:message code="confirmnewpw" var="confirmnewpw"/>
                    <form:input path="passwordForm.repeatPassword" type="password"  class="form-control" placeholder="${confirmnewpw}" /> 
                    <form:errors path="passwordForm.repeatPassword" element="p" cssClass="form-error"/>
                </div> 
                <spring:message code="changepw" var="changepw"/>
                <input id="submit" type="submit" name="submit" class="btn btn-lg btn-success fright " value="${changepw}"></input>
        </div>  
      </div>
    </form:form>
    </div>

    <div class="container uspaced60">
      <c:url value="/customize/profilePicture" var="postPathPicture" />
      <form:form modelAttribute="changeProfilePictureForm" action="${postPathPicture}" method="post"  enctype="multipart/form-data">
      <div class="row">
        <spring:message code="changepic" var="changepic"/>
        <div class="text zonetext uspaced60 lspaced20">${changepic}</div>
      </div>
      <hr class="divider">
      <div class="row uspaced20">
        <img class="configimg" src="<c:url value="/profile/images/${profileUser.profile_img_url}"/>" />
      </div>
      <div class="row uspaced10">
        <div class="col-lg-12">
          <form:input class="image-input" type="file" path="profilePicture" accept="image/*"/>
          <form:errors path="profilePicture" element="p" cssClass="form-error"/>
        </div>            
      </div>
      <div class="row uspaced20">
        <div class="col-lg-12">
          <input id="submit" type="submit" name="submit" class="btn btn-lg btn-success fright" value="${changepic}"></input>
        </div>
        
      </div> 
        
    </form:form>
    </div>


	<!--FOOTER-->
    <div class="row uspaced60"></div>
    <div class="container-fluid footer">
        <div class="text footertext"><spring:message code="footer"/></div> 
    </div>
    <!--FOOTER-->




</body>