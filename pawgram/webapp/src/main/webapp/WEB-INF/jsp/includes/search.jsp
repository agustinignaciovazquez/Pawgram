<div class="row uspaced60">
    <div class="col-md-3"></div>  
    <div class="col-md-6">
      <div id="custom-search-input">
        <label for="validationCustom03" class="text filttext"><spring:message code="search.post"/></label>

          <c:choose>
            <c:when test="${empty currentCategory}">
              <form id="mysearchformq" action="<c:url value="/search/" />"  method="get">  
            </c:when>
            <c:otherwise>
              <form id="mysearchformq" action="<c:url value="/search/category/${currentCategory.lowerName}" />" method="get">  
            </c:otherwise>
          </c:choose> 
              <c:if test="${not empty searchZone}" >
                <c:set value="${searchZone.location.latitude}" var="slat" />
                <c:set value="${searchZone.location.longitude}" var="slon" />
              </c:if>
              <input type="hidden" id="latitude_input" name="latitude" value="${empty slat ? '' : slat}" />
              <input type="hidden" id="longitude_input" name="longitude" value="${empty slon ? '' : slon}"/>  
              <div class="input-group col-md-12 ">
                <input maxlength="50" type="text" name="query" class="search-query form-control form-control-md text" placeholder="<spring:message code="search"/>" value="<c:out value="${empty queryText ? '' : queryText}" />" />
                 </form>  
                <span class="input-group-btn">
                  <button class="btn uspacedfa" type="button">
                    <i class=" fas fa-search"></i>
                  </button>
                </span>
              </div>  
              <script src="<c:url value="/resources/js/search.js"/>"></script>
              <script>
                $(document).ready(function(){
                  getLocation();//try to retrieve location from browser and put it in the inputs
                  $('#mysearchformq').submit(function(event) {
                     event.preventDefault(); //this will prevent the default submit
                      if($('.search-query').val().length > 3){
                        $(this).unbind('submit').submit(); // continue the submit unbind preventDefault
                      }else{
                         showAlert('.alert-danger:first','<spring:message code="short"/>');  
                      }
                    })
                });
              </script>
      </div>
    </div>
  </div>