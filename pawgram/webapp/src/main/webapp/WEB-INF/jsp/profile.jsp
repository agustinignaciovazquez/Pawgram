<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<head>

  <meta charset="UTF-8">
  <title><spring:message code="pageName"/>-<spring:message code="title.addzone"/></title> 
  

  <link href="<c:url value="/resources/css/all.css"/>" rel="stylesheet" id="font-awesome">
  <link href="<c:url value="/resources/css/bootstrap.min.css"/>" rel="stylesheet" id="bootstrap-css">
  <link rel="stylesheet" href="<c:url value="/resources/css/pawgramin.css"/>">

  
  <script src="<c:url value="/resources/js/jquery-3.3.1.min.js"/>"></script>
  <script src="<c:url value="/resources/js/bootstrap.min.js"/>"></script>
  <script src="<c:url value="/resources/js/pawgram.js"/>"></script>

</head>

<body>

    <%@include file="includes/header.jsp"%>

    <div class="container-fluid profilebanner">
			<a href="">
				<div class="row uspaced5">
					<div class="avatarwrapper avatar">
						<img class="avatar" alt="" src="./img/profilepic2.jpg">
	                	<i class="fas fa-edit editicon"></i>
					</div>
				</div>
			</a>
			<div class="row">
				<div class="text name">Tomas Zorraco</div>
			</div>
			<div class="row">
				<div class="text email">insanecoding@gmail.com</div>
			</div>
			<div class="row bspaced5">
				<div class="text email">(011) 4074-0641</div>
			</div>

		</div>

		<div class="row uspaced60">
			<div class="center">
				<div class="text noposttext"> Aun no tienes ningun post en proceso </div>
			</div>
			
			      
		</div>

		<div class="row uspaced20">
			<div class=" center">
				<button type="submit" class="btn btn-success newbutton">
	    			<i class="fas fa-plus"></i> Iniciar nuevo post
				</button>  	
			</div>
			    
		</div>

		<div class="container uspaced5">

            <div class="row">
              <div class="col-md-6">
                <div class="text zonetext ">Mis Posts</div>
              </div>
              <div class="col-md-6">

                <div class="btn-group dropright fright">
                  <button type="button" class="btn dropdown-toggle" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
                     Filtra por categoria
                  </button>
                  <div class="dropdown-menu">
                    <a class="dropdown-item" href="#">Perdidos</a>
                      <a class="dropdown-item" href="#">Encontrados</a>
                      <a class="dropdown-item" href="#">Adopciones</a>
                      <a class="dropdown-item" href="#">Emergencias</a>
                  </div>
                </div>

              </div>  
            </div>
            <div class="row uspaced20">
                <div class="col-md-4">                  
                    <a href="#post" class="">
                        <div class="card"> 
                            <img class="img-fluid card-img" src="https://s7d1.scene7.com/is/image/PETCO/puppy-090517-dog-featured-355w-200h-d" alt="">
                            <div class="card-img-overlay"> <span class="badge badge-pill badge-danger text categorytext">Perdido</span> </div>
                            <div class="card-body">
                                <p class="card-text"><small class="text  text-time"><em>A 0,7 kms</em> <em> / </em> <em>Especie</em><em>:</em> <em>Perro</em> <em> / </em> <em>Sexo</em><em>:</em> <em>Macho</em> </small></p>
                                <div class="news-title">
                                    <h2 class="text title-small">Tomy se perdio en la concha de tu hermana</h2>
                                </div>
                                <div class="card-exp">
                                    <i class="far fa-eye"> <div class="text seemoretext">Ver Detalles</div></i>
                                </div>
                                
                            </div>
                        </div>
                    </a>
                </div>
                <div class="col-md-4">
                    <a href="#post" class="">
                        <div class="card"> 
                            <img class="img-fluid card-img" src="https://i.ytimg.com/vi/SfLV8hD7zX4/maxresdefault.jpg" alt="">
                            <div class="card-img-overlay"> <span class="badge badge-pill badge-success text categorytext ">Encontrado</span> </div>
                            <div class="card-body">
                                <p class="card-text"><small class="text text-time"><em>A 0,7 kms</em> <em> / </em> <em>Especie</em><em>:</em> <em>Perro</em> <em> / </em> <em>Sexo</em><em>:</em> <em>Macho</em> </small></p>
                                <div class="news-title">
                                    <h2 class="text title-small">Tomy se perdio en la concha de tu hermana</h2>
                                </div>
                                <div class="card-exp">
                                    <i class="far fa-eye"> <div class="text seemoretext">Ver Detalles</div></i>
                                </div>
                                
                            </div>
                        </div>
                    </a>
                </div> 

                <div class="col-md-4">
                    <a href="#post" class="">
                        <div class="card"> 
                            <img class="img-fluid card-img" src="https://i.kinja-img.com/gawker-media/image/upload/s--WFkXeene--/c_scale,f_auto,fl_progressive,q_80,w_800/ol9ceoqxidudap8owlwn.jpg" alt="">
                            <div class="card-img-overlay"> <span class="badge badge-pill badge-danger text categorytext">Perdido</span> </div>
                            <div class="card-body">
                                <p class="card-text"><small class=" text text-time"><em>A 0,7 kms</em> <em> / </em> <em>Especie</em><em>:</em> <em>Perro</em> <em> / </em> <em>Sexo</em><em>:</em> <em>Macho</em> </small></p>
                                <div class="news-title">
                                    <h2 class=" text title-small">Tomy se perdio en la concha de tu hermana</h2>
                                </div>
                                <div class="card-exp">
                                    <i class="far fa-eye"> <div class="text seemoretext">Ver Detalles</div></i>
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
