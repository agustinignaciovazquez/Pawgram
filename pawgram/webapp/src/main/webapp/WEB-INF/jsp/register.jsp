<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<html>
<head>
<link rel="stylesheet" href="<c:url value="/css/style.css"/>" />
</head>
<body>
<h2>Register</h2>
<c:url value="/register/process" var="postPath"/>
<form:form modelAttribute="registerForm" action="${postPath}" method="post">
<div>
<form:label path="name">Name: </form:label>
<form:input type="text" path="name"/>
<form:errors path="name" cssClass="formError" element="p"/>
</div>
<div>
<form:label path="surname">Surname: </form:label>
<form:input type="text" path="surname"/>
<form:errors path="surname" cssClass="formError" element="p"/>
</div>
<div>
<form:label path="mail">E-Mail: </form:label>
<form:input type="text" path="mail"/>
<form:errors path="mail" cssClass="formError" element="p"/>
</div>
<div>
<form:label path="password">Password: </form:label>
<form:input type="password" path="password" />
<form:errors path="password" cssClass="formError" element="p"/>
</div>
<div>
<form:label path="repeatPassword">Repeat password: </form:label>
<form:input type="password" path="repeatPassword"/>
<form:errors path="repeatPassword" cssClass="formError" element="p"/>
</div>
<div>
<input type="submit" value="Register!"/>
</div>
</form:form>
</body>
</html>