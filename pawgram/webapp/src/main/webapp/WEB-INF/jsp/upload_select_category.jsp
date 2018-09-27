<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<head>

	<meta charset="UTF-8">
	<title><spring:message code="pageName"/> - <spring:message code="title.createpost"/></title>

	<link href="<c:url value="/resources/css/all.css"/>" rel="stylesheet" id="font-awesome">
  <link href="<c:url value="/resources/css/bootstrap.min.css"/>" rel="stylesheet" id="bootstrap-css">
  <link rel="stylesheet" href="<c:url value="/resources/css/pawgramin.css"/>">

  
  <script src="<c:url value="/resources/js/jquery-3.3.1.min.js"/>"></script>
  <script src="<c:url value="/resources/js/bootstrap.min.js"/>"></script>
  <script src="<c:url value="/resources/js/pawgram.js"/>"></script>

</head>


<body>
	
	<%@include file="includes/header.jsp"%>  

		<div class="container-fluid titzon">
			<div class="row">
				<div class="col-md-3"></div>
				<div class="text titsec"><spring:message code="createpost"/></div>
			</div>
		</div>
		<div class=" container step center">
			<div class="row uspaced200">
				<div class="text formtitle center"><spring:message code="select.category"/></div>
			</div>
			<div class="row uspaced20">
				<div class="container optionblock center ">
					<c:forEach items="${categories}" var="category">
	             
	                     <a href="<c:url value="/post/create/category/${category.lowerName}"/>" class="formoption">
							<div class="container formoption">
								<div class="row">
									<div class="col-md-2">
										<i class="<spring:message code="category.icon.${category.lowerName}"/> formicon uspaced10"></i>
									</div>
									<div class="col-md-10">
										<div class="text formtext"><spring:message code="category.${category.lowerName}"/></div>
										<div class="text formsubtext"><spring:message code="select.${category.lowerName}"/></div>
									</div>	
								</div>	
							</div>
						</a>
	              	</c:forEach>
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