<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>


<head>
	<meta charset="UTF-8">
	<title><spring:message code="pageName"/> - <spring:message code="register"/></title>

	<link href="<c:url value="/resources/css/bootstrap.min.css"/>" rel="stylesheet" id="bootstrap-css">
	<link rel="stylesheet" href="<c:url value="/resources/css/pawgram.css"/>">

	<script src="<c:url value="/resources/js/pawgram.js"/>"></script>
	<script src="<c:url value="/resources/js/bootstrap.min.js"/>"></script>
	<script src="<c:url value="/resources/js/jquery-3.3.1.min.js"/>"></script>
	
</head>

<body>
	<div class="v-center">

	<div class="container">
	  
	  <div class="row" id="pwd-container">
	    <div class="col-md-4"></div>
	    
	    <div class="col-md-4">
	      <section class="login-form">
	        <c:url value="/register/process" var="postPath"/>
			<form:form modelAttribute="registerForm" action="${postPath}" method="post" role="login">
	          <img src="<c:url value="/resources/img/logo.png"/>" class="img-responsive" alt="" />

	          <div>
	          	<spring:message code="name" var="name"/>
	          	<form:input type="text" path="name" placeholder="${name}" class="form-control input-lg"/>
				<form:errors path="name" cssClass="formError" element="p" />
	          </div>

	          <div>
	          	<spring:message code="surname" var="surname"/>
	          	<form:input type="text" path="surname" placeholder="${surname}" class="form-control input-lg"/>
				<form:errors path="surname" cssClass="formError" element="p" />
	          </div>

	          <div>
	          	<spring:message code="mail" var="mail"/>
	          	<form:input type="text" path="mail" placeholder="${mail}" class="form-control input-lg"/>
				<form:errors path="mail" cssClass="formError" element="p" />
	          </div>

	          <div>
	          	<spring:message code="password" var="password"/>
	          	<form:input type="password" path="passwordForm.password" placeholder="${password}" class="form-control input-lg"/>
				<form:errors path="passwordForm.password" cssClass="formError" element="p" />
	          </div>

	          <div>
	          	<spring:message code="confirmpw" var="confirmpw"/>
	          	<form:input type="password" path="passwordForm.repeatPassword" placeholder="${confirmpw}" class="form-control input-lg"/>
				<form:errors path="passwordForm.repeatPassword" cssClass="formError" element="p" />
	          </div>
	          
	          <spring:message code="register" var="register"/>
	          <input type="submit" name="go" class="btn btn-lg btn-primary btn-block" value=${register}></input>
	          
	        </form:form>
	      </section>  
	    </div>
	      
	     <div class="col-md-4"></div>
	      

	  </div>  
	  
	</div>
	</div>


</body>	