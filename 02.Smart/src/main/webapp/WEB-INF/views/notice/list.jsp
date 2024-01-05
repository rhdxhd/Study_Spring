<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>  
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>
<h3 class="mb-4">공지글목록</h3>


<div class="row mb-2 justify-content-between">
	<div class="col-auto">		
	</div>
	<!-- 관리자로 로그인되어 있는 경우만 -->
	<c:if test="${loginInfo.role =='ADMIN' }">
	<div class="col-auto">
		<button type="button" class="btn btn-primary" onclick="location='register'">글쓰기</button>
	</div>
	</c:if>
</div>


<table class="table tb-list">
<colgroup>
    <col>
    <col width='120px'>
    <col width='140px'>
    <col width='100px'>
</colgroup>
<tr>
	<th>번호</th>
    <th>제목</th>
    <th>작성자</th>
    <th>작성일자</th>
    <th>첨부파일</th>
</tr>

<c:if test="${ empty list }">
<tr><td colspan ="3">공지글이 없습니다</td></tr>
</c:if>

<c:forEach items="${list}" var="vo">
<tr>
	<td>${vo.no }</td>
    <td class ="text-start"><a href="info?id=${vo.id}" class="text-link">${vo.title }</td>
    <td>${vo.name }</td>
    <td>${vo.writedate }</td>
    <td><c:if test="${ !empty vo.filename }"><i class="fa-solid fa-paperclip"></i></c:if></td>
</tr>
</c:forEach>




</table>



</body>
</html>