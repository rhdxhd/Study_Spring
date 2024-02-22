<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%> 
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%> 
    
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>
<h3 class="mb-4">방명록 글정보</h3>

<table class="table tb-row">
<colgroup>
	<col width="180px">
	<col>
	<col width="120px">
	<col width="120px">
	<col width="100px">
	<col width="100px">
</colgroup>
<tr><th>제목</th>
	<td colspan="5">${vo.title }</td>
</tr>
<tr><th>작성자</th><td>${vo.name }</td>
	<th>작성일자</th><td>${vo.writedate }</td>
	<th>조회수</th><td>${vo.readcnt }</td>
</tr>
<tr><th>내용</th>
	<td colspan="5">${fn : replace( vo.content, crlf, "<br>" ) }</td>
</tr>
<tr><th>첨부파일</th>
	<td colspan="5">
		<c:forEach items="${vo.fileList}" var="f">
		<div class="row py-1">
			<div class="col-auto">
				<span class="file-name">${f.filename}</span> 
				<i role="button" data-file="${f.id}" class="file-download ms-4 fa-solid fa-file-arrow-down fs-2"></i> 
			</div>
		</div>
		</c:forEach>
	</td>
</tr>
</table>

<div class="btn-toolbar justify-content-center gap-2">
	<button class="btn btn-primary" id="btn-list">목록으로</button>
	
	<!-- 작성자로 로그인한 경우만 수정/삭제 보이게 -->
	<c:if test="${loginInfo.user_id eq vo.writer}">
	<button class="btn btn-primary" id="btn-modify">정보수정</button>
	<button class="btn btn-primary" id="btn-delete">정보삭제</button>
	</c:if>
</div>

<form method="post">
<input type="hidden" name="id" value="${vo.id}">
<input type="hidden" name="curPage" value="${page.curPage}">
<input type="hidden" name="search" value="${page.search}">
<input type="hidden" name="keyword" value="${page.keyword}">
<input type="hidden" name="pageList" value="${page.pageList}">
<input type="hidden" name="url" value="board/info">
</form>

<jsp:include page="comment.jsp"/>

<script>
$(".file-download").click(function(){
	//1.
	//location = "download?id=" + $(this).data("file")
	//2.
	//var file = $(this).data("file")
	//location = `download?id=\${file}`
	//3.
	$("[name=id]").val( $(this).data("file") )
	$("form").attr("action", "download").submit();
})

$("#btn-list, #btn-modify, #btn-delete").click(function(){
	var id = $(this).attr("id");
	id  = id.substr( id.indexOf("-")+1 );
	$("form").attr("action", id);
	if( id == "delete" ){
		if( confirm("정말 삭제하시겠습니까?") ){
			$("form").submit();
		}
	}else 
		$("form").submit();
})
</script>


</body>
</html>