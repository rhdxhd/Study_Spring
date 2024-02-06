<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>  
<style>
.popfile {width: 120px; height: 120px;}
</style>


<c:set var="response" value="${list.response.body }"/>
<c:forEach items="${list.response.body.items.item }" var="vo">

<table class = "table tb-list animal">
<colgroup>
	<col width="120px">
	<col width="100px"><col width="60px"> <!-- 성별 -->
	<col width="70px"><col width="160px"> <!-- 나이 -->
	<col width="70px"><col width="120px"> <!-- 체중 -->
	<col width="70px"><col width="160px"> <!-- 색상 -->
	<col width="70px"><col width="110px"> <!-- 접수일자 -->
</colgroup>

<tr>
	<td rowspan="3"><img class ="popfile" src="${vo.popfile }"></td>
	<th>성별</th><td>${vo.sexCd }</td>
	<th>나이</th><td>${vo.age }</td>
	<th>체중</th><td>${vo.weight }</td>
	<th>색상</th><td>${vo.colorCd }</td>
	<th>접수일자</th><td>${vo.happenDt }</td>
</tr>
<tr>
	<th>특징</th><td colspan="9" class="text-start">${vo.specialMark }</td>
</tr>
<tr>
	<th>발견장소</th><td colspan="7" class="text-start">${vo.happenPlace }</td>
	<td colspan="2">${vo.processState }</td> <!-- 상태  -->
</tr>
<tr>
	<td colspan="2">${vo.careNm }</td>
	<td colspan="7">${vo.careAddr }</td>
	<td colspan="2">${vo.careTel }</td>
</tr>
</table>
</c:forEach>



<script>
$(".popfile").click(function() {
	$("#modal-map .modal-body").html( $(this).clone() )
	$("#modal-map img").removeClass("popfile");
	$("#modal-map").removeAttr("style");
	new bootstrap.Modal ( $("#modal-map") ).show();
})




makePage( ${list.response.body.totalCount}, ${list.response.body.pageNo} );
</script>




