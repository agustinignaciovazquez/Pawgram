<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<html>
<head>
    <link rel="stylesheet" href="<c:url value="/resources/css/errors.css"/>">
</head>
<body>
    <div class="cover">
    	<h1><spring:message code="error.500.cover"/> <small><spring:message code="error500"/></small></h1>
    	<p class="lead">
            <spring:message code="error.500.lead"/>
    	</p>
    </div>
</body>
</html>