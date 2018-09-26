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
      <div class="row">
        <div class="text zonetext uspaced60 lspaced20"><spring:message code="configuration.change.info"/></div>
      </div>
      
      <hr class="divider">

      <div class="row uspaced20">
        <div class="col-lg-12">
            
          <div class="text"><spring:message code="name"/></div class="text">
          <div class="form-group"> 
            <input type="text" value="Roberto" class="form-control" placeholder="<spring:message code="newname"/>"> 
          </div> 
          <div class="text"><spring:message code="surname"/></div class="text">
          <div class="form-group"> 
            <input type="text" value="Carlos" class="form-control" placeholder="<spring:message code="newsurname"/>"> 
          </div> 
                
        </div>  
      </div>

    </div>


    <div class="container uspaced60">
      <div class="row">
        <div class="text zonetext uspaced60 lspaced20"><spring:message code="changepw"/></div>
      </div>
      <hr class="divider">
      <div class="row uspaced20">
        <div class="col-lg-12">
            
            <div class="text"><spring:message code="currentpw"/></div class="text">
            <div class="form-group pass_show"> 
                    <input type="password" value="faisalkhan@123" class="form-control" placeholder="<spring:message code="currentpw"/>"> 
                </div> 
               <div class="text"><spring:message code="newpw"/></div class="text">
                <div class="form-group pass_show"> 
                    <input type="password" value="faisal.khan@123" class="form-control" placeholder="<spring:message code="newpw"/>"> 
                </div> 
               <div class="text"><spring:message code="confirmnewpw"/></div class="text">
                <div class="form-group pass_show"> 
                    <input type="password" value="faisal.khan@123" class="form-control" placeholder="<spring:message code="confirmnewpw"/>"> 
                </div> 
                
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