<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>


<head>
	<meta charset="UTF-8">
	<title><spring:message code="pageName"/> - <spring:message code="resetpw"/></title>

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
	      	<c:url value="/login/forget/recover/process" var="postPathName" />
	      	<form:form modelAttribute="recoverForm" action="${postPathName}" method="post">
	          <img src="<c:url value="/resources/img/logo.png"/>" class="img-responsive" alt="" />
	          
			  <div>
			  	<spring:message code="mail" var="mail"/>
	          	<form:input path="mail" type="text" placeholder="${mail}" class="form-control input-lg" />
	          	<form:errors path="mail" element="p" cssClass="form-error"/>
	          </div>

	          <div>
	          	<spring:message code="token" var="token"/>
	         	<form:input path="currentToken" type="text" placeholder="${token}" class="form-control input-lg" />
	         	<form:errors path="currentToken" element="p" cssClass="form-error"/>
	          </div>

	          <div>
	          	<spring:message code="newpw" var="newpw"/>
	          	<form:input path="passwordForm.password" type="password" placeholder="${newpw}" class="form-control input-lg" />
	          	<form:errors path="passwordForm.password" element="p" cssClass="form-error"/>
	          </div> 

	          <div>
	          	<spring:message code="confirmnewpw" var="confpw"/>
	          	<form:input path="passwordForm.repeatPassword" type="password" placeholder="${confpw}" class="form-control input-lg" />
	          	<form:errors path="passwordForm.repeatPassword" element="p" cssClass="form-error"/>
	          </div>         
	          
	          <spring:message code="resetpw" var="resetpw"/>
	          <input type="submit" name="go" class="btn btn-lg btn-primary btn-block" value="${resetpw}"></input> 
	      </form:form>
	      </section>  
	    </div>
	      
	     <div class="col-md-4"></div>
	      

	  </div>  
	  
	</div>
	</div>


</body>	