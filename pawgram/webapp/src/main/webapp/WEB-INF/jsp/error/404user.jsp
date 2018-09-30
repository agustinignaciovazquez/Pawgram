<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<html>
<head>
    <link rel="stylesheet" href="<c:url value="/resources/css/errors.css"/>">
</head>
<body>
    <div class="cover">
    	<h1><spring:message code="error.404user.cover"/> <small><spring:message code="error404"/></small></h1>
    	<p class="lead">
            <spring:message code="error.404user.lead"/>
    	</p>
    </div>
</body>
</html>