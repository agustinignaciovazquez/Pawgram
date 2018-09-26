<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<head>

	<meta charset="UTF-8">
	<title>Pawgram - Create Post</title>

    <link href="css/all.css" rel="stylesheet"/>
	<link href="css/bootstrap.min.css" rel="stylesheet" id="bootstrap-css"/>
	<link rel="stylesheet" href="css/pawgramin.css"/>

	<script src="js/jquery-3.3.1.min.js"></script>
	<script src="js/bootstrap.min.js"></script>
    <script src="js/pawgram.js"></script>

</head>


<body>
	
	<%@include file="includes/header.jsp"%>  

		<div class="container-fluid titzon">
			<div class="row">
				<div class="col-md-3"></div>
				<div class="text titsec">Crear anuncio</div>
			</div>
		</div>
		<div class=" container step center">
			<div class="row uspaced200">
				<div class="text formtitle center">Selecciona una categoria:</div>
			</div>
			<div class="row uspaced20">
				<div class="container optionblock center ">
					<a href="" class="formoption">
						<div class="container formoption">
							<div class="row">
								<div class="col-md-2">
									<i class=" fas fa-search-plus formicon formiconlost uspaced10"></i>
								</div>
								<div class="col-md-10">
									<div class="text formtext">Perdido</div>
									<div class="text formsubtext">Selecciona esta opcion si perdiste una mascota.</div>
								</div>	
							</div>	
						</div>
					</a>
					<a href="" class="formoption">
						<div class="container formoption">
							<div class="row">
								<div class="col-md-2">
									<i class="fab fa-periscope formicon formiconfound uspaced10"></i>
								</div>
								<div class="col-md-10">
									<div class="text formtext">Encontrado</div>
									<div class="text formsubtext">Selecciona esta opcion si encontraste una mascota.</div>
								</div>	
							</div>	
						</div>
					</a>
					<a href="" class="formoption">
						<div class="container formoption">
							<div class="row">
								<div class="col-md-2">
									<i class="fas fa-home formicon formiconadopt uspaced10"></i>
								</div>
								<div class="col-md-10">
									<div class="text formtext">Adopcion</div>
									<div class="text formsubtext">Selecciona esta opcion si deseas dar en adopcion una mascota.</div>
								</div>	
							</div>	
						</div>
					</a>
					<a href="" class="formoption">
						<div class="container formoption">
							<div class="row">
								<div class="col-md-2">
									<i class="fas fa-hospital formicon formiconhosp uspaced10"></i>
								</div>
								<div class="col-md-10">
									<div class="text formtext">Emergencia</div>
									<div class="text formsubtext">Selecciona esta opcion si tenes una emergencia con tu mascota. </div>
								</div>	
							</div>	
						</div>
					</a>	
				</div>
			</div>
		</div>
		
	<!--FOOTER-->
    <div class="row uspaced60"></div>
    <div class="container-fluid footer">
        <div class="text footertext">© 2018 Todos los derechos reservados pawgram.org</div> 
    </div>
    <!--FOOTER-->




</body>