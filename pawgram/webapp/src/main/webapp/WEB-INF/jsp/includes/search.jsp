<div class="row uspaced60">
    <div class="col-md-3"></div>  
    <div class="col-md-6">
      <div id="custom-search-input">
        <label for="validationCustom03" class="text filttext"><spring:message code="search.post"/></label>
        <div class="input-group col-md-12 ">
          <c:choose>
            <c:when test="${empty currentCategory}">
              <form action="<c:url value="/search/" />"  method="get">  
            </c:when>
            <c:otherwise>
            <form action="<c:url value="/search/category/${currentCategory.lowerName}" />" method="get">  
            </c:otherwise>
          </c:choose> 
        
         <input type="hidden" id="latitude_input" name="latitude" /><br>
         <input type="hidden" id="longitude_input" name="longitude" />    
         <input type="text" name="query" class="search-query form-control form-control-md text" placeholder="Search" value="<c:out value="${empty queryText ? '' : queryText}" />" />
          </form>
          <span class="input-group-btn">
            <button class="btn uspacedfa" type="button">
              <i class=" fas fa-search"></i>
            </button>
          </span>
        </div>
      </div>
    </div>
  </div>