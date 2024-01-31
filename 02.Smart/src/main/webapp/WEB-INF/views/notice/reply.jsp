<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>  
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>

<h3 class="mb-4">공지글 답글등록</h3>

<form method="post" action="replyInsert" enctype="multipart/form-data">
<input type="hidden" name="writer" value="${loginInfo.user_id }">
<table class="table tb-row">
<colgroup>
    <col width='180px'>
    <col>
</colgroup>
<tr><th>제목</th>
	<td><input type="text" title="제목" autofocus class="check-empty form-control" name="title"></td>
</tr>
<tr><th>내용</th>
	<td><textarea name="content" title="내용" class="check-empty form-control"></textarea></td>
</tr>

<tr><th>첨부파일</th>
	<td><div class="row">
   			 <div class="col-auto d-flex file-info align-items-center">
   			 <label>
       			 <input class="form-control" id="file-single" type="file" name="file">
       			 <i role="button" class="ms-2 fa-solid fa-paperclip fs-3"></i>
       			 
   			 </label>
   			 <div class="ms-4 d-flex align-items-center">
   			 	<span class="file-name"></span>
   			 	<i role="button" class="ms-4 file-delete d-none text-danger fs-4 fa-regular fa-trash-can"></i>
   			 </div>
   		 </div>
	  </div>
	</td>
</tr>
</table>


<input type="hidden" name="curPage" value="${page.curPage }">
<input type="hidden" name="search" value="${page.search }">
<input type="hidden" name="keyword" value="${page.keyword }">
<!-- 원글정보  -->
<input type="hidden" name="root" value="${vo.root }">
<input type="hidden" name="step" value="${vo.step }">
<input type="hidden" name="indent" value="${vo.indent }">
<input type="hidden" name="rid" value="${vo.id }">



</form>


<div class="btn-toolbar justify-content-center gap-2">
    <button class="btn btn-primary px-4" id="btn-save" >저장</button>
    <button class="btn btn-outline-primary px-4" id="btn-cancel">취소</button>
</div>


<script>
$("#btn-save").click(function() {
	if( emptyCheck() ) { //입력이 되어 있는 경우만 서브밋
		$("form").submit()
	}
})

$("#btn-cancel").click(function() {
	location = "info?id={vo.id}&curPage=${page.curPage}&search=${page.search}&keyword=${page.keyword}"
})




</script>




</body>
</html>