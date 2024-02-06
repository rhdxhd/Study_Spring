<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>  

<select class="form-select w-px200" id="sido">
	<c:forEach items="${list.response.body.items.item }" var="vo">
	<option value="${vo.orgCd }">${vo.orgdownNm }</option>
	</c:forEach>
</select>

<script>
$("#sido").change(function() {
	animal_list( 1 );
	
})


</script>