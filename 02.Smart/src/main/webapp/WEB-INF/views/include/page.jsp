<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>  


<nav aria-label="Page navigation ">
  <ul class="pagination mt-4 justify-content-center">
    <li class="page-item"><a class="page-link" href="#">Previous</a></li>
    
    
    
  	 <c:if test="${page.curBlock gt 1 }"> 
     <li class="page-item"><a class="page-link" onclick="go_page(1)"><i class="fa-solid fa-angles-left"></i></a></li>
     <li class="page-item"><a class="page-link" onclick="go_page(${page.beginPage-page.blockPage})"><i class="fa-solid fa-angle-right"></i></a></li>
    </c:if>
    
    
    <c:forEach var="no" begin="${page.beginPage }" end="${page.endPage }" step="1">
    <c:if test="${no eq page.curPage}">
	    <li class="page-item"><a class="page-link active" href="#">${no}</a></li>
	</c:if>
    <c:if test="${no ne page.curPage}">
   		<li class="page-item"><a class="page-link" onclick="go_page(${no})">${no}</a></li>
    </c:if>
    
    </c:forEach>
    
    <c:if test="${page.curBlock < page.totalBlock }"> <!-- 마지막블럭이 아닌 경우 보이게 -->
    <li class="page-item"><a class="page-link" onclick="go_page(${page.endPage+1})"><i class="fa-solid fa-angle-right"></i></a></li>
    <li class="page-item"><a class="page-link" onclick="go_page(${page.totalPage})"><i class="fa-solid fa-angles-right"></i></a></li>
    </c:if>
  </ul>
</nav>


<script>
function go_page( no ) {
	$("[name=curPage]").val(no);
	$("form").submit();
	
}


</script>

