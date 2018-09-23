<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>

<head>
	<meta charset="UTF-8">
	<title>Pawgram - Login</title>

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
	      	<c:url value="/login" var="loginUrl" />
	      	<c:url value="/register" var="registerUrl" />
			<form  method="post" action="${loginUrl}" method="post" enctype="application/x-www-form-urlencoded" role="login">
	          <img src="<c:url value="/resources/img/logo.png"/>" class="img-responsive" alt="" />
	          
			  <div>
	          	<input type="text" name="j_username" placeholder="Email" class="form-control input-lg"/>
	          </div>

	          <div>
	          	<input type="password" name="j_password" placeholder="Password" class="form-control input-lg"/>
	          </div>         
	          <div>
				<label><input name="j_rememberme" type="checkbox"/> <spring:message code="remember_me"/> Recuerdame</label>
			</div>
	          <input type="submit" name="go" class="btn btn-lg btn-primary btn-block" value="Sign in"></input>
	          <div>
	            <a href="${registerUrl}">Create account</a> or <a href="#">reset password</a>
	          </div>
	        </form>
	      </section>  
	    </div>
	      

	     <div class="col-md-4"></div>
	      

	  </div>  
	  
	</div>
	</div>


</body>	