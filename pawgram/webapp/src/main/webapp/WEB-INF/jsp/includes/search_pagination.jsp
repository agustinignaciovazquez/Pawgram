
	<ul class="pagination pagination-large">

	<!--	chevron left-->
	<c:if test="${currentPage > 1}">
		<li><a href="<c:out value="?query=${param['query']}&latitude=${param['latitude']}&longitude=${param['longitude']}&page=${currentPage-1}"/>" class="page-link"> «</a></li>
	</c:if>
	<!--	first page-->
	<c:if test="${currentPage > 3}">
		<li><a href="<c:out value="?query=${param['query']}&latitude=${param['latitude']}&longitude=${param['longitude']}&page=1"/>" class="page-link"><c:out value="1"/></a></li>
		<li><a class="page-link disabled"><c:out value="..."/></a></li>
	</c:if>
	
	<c:set var="pageBegin" value="${currentPage > 3 ? currentPage - 2 : 1}"/>
	<c:set var="pageEnd" value="${currentPage + 2 < totalPages ? currentPage + 2 : totalPages}"/>
	
	<c:forEach var="i" begin="${pageBegin}" end="${pageEnd}">
		<c:choose>
			<c:when test="${i == currentPage}">
				<li class="active"><a class="page-link"><c:out value="${i}"/></a></li>
			</c:when>
			<c:otherwise>
				<li><a href="<c:out value="?query=${param['query']}&latitude=${param['latitude']}&longitude=${param['longitude']}&page=${i}"/>" class="page-link"><c:out value="${i}"/></a></li>
			</c:otherwise>
		</c:choose>
	</c:forEach>
	
	<!--	last page-->
	<c:if test="${currentPage < totalPages - 2}">
		<li><a class="page-link disabled"><c:out value="..."/></a></li>
		<li><a href="<c:out value="?query=${param['query']}&latitude=${param['latitude']}&longitude=${param['longitude']}&page=${totalPages}"/>" class="page-link"><c:out value="${totalPages}"/></a></li>
	</c:if>
	<!--	chevron right-->
	<c:if test="${currentPage < totalPages}">
		<li><a href="<c:out value="?query=${param['query']}&latitude=${param['latitude']}&longitude=${param['longitude']}&page=${currentPage+1}"/>" class="page-link">»</a></li>
	</c:if>
	</ul>
