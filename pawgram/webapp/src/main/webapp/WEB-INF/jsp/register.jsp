<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<head>
	<meta charset="UTF-8">
	<title>Pawgram - Register</title>

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
	          	<form:input type="text" path="name" placeholder="Name" class="form-control input-lg"/>
				<form:errors path="name" cssClass="formError" element="p" />
	          </div>

	          <div>
	          	<form:input type="text" path="surname" placeholder="Surname" class="form-control input-lg"/>
				<form:errors path="surname" cssClass="formError" element="p" />
	          </div>

	          <div>
	          	<form:input type="text" path="mail" placeholder="Email" class="form-control input-lg"/>
				<form:errors path="mail" cssClass="formError" element="p" />
	          </div>

	          <div>
	          	<form:input type="password" path="password" placeholder="Password" class="form-control input-lg"/>
				<form:errors path="password" cssClass="formError" element="p" />
	          </div>

	          <div>
	          	<form:input type="repeatPassword" path="repeatPassword" placeholder="Repeat Password" class="form-control input-lg"/>
				<form:errors path="repeatPassword" cssClass="formError" element="p" />
	          </div>
	          
	          <input type="submit" name="go" class="btn btn-lg btn-primary btn-block" value="Register"></input>
	          
	        </form:form>
	      </section>  
	    </div>
	      
	     <div class="col-md-4"></div>
	      

	  </div>  
	  
	</div>
	</div>


</body>	