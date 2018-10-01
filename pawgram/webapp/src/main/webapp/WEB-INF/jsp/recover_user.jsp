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
	      	<form method="post" role="login">
	          <img src="<c:url value="/resources/img/logo.png"/>" class="img-responsive" alt="" />
	          
			  <div>
			  	<spring:message code="mail" var="mail"/>
	          	<input type="text" placeholder="${mail}" class="form-control input-lg"></input>
	          </div>

	          <div>
	          	<spring:message code="token" var="token"/>
	         	<input path="token" type="text" placeholder="${token}" class="form-control input-lg"></input>
	          </div>

	          <div>
	          	<spring:message code="newpw" var="newpw"/>
	          	<input path="password" type="password" name="j_password" placeholder="${newpw}" class="form-control input-lg"></input>
	          </div> 

	          <div>
	          	<spring:message code="confirmnewpw" var="confpw"/>
	          	<input path="repeatpassword" type="password" name="j_password" placeholder="${confpw}" class="form-control input-lg"></input>
	          </div>         
	          
	          <spring:message code="resetpw" var="resetpw"/>
	          <input type="submit" name="go" class="btn btn-lg btn-primary btn-block" value="${resetpw}"></input> 
	      </form>
	      </section>  
	    </div>
	      
	     <div class="col-md-4"></div>
	      

	  </div>  
	  
	</div>
	</div>


</body>	