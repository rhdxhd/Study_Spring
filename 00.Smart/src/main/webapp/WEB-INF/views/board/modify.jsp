<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>     
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>
<h3 class="mb-4">방명록 글수정</h3>

<form method="post" action="update" enctype="multipart/form-data">
<input type="hidden" name="writer" value="${loginInfo.user_id}">
<table class="table tb-row">
<colgroup>
	<col width="180px">
	<col>
</colgroup>
<tr><th>제목</th>
	<td><input type="text"  title="제목" autofocus class="check-empty form-control" value="${vo.title}" name="title"></td>
</tr>
<tr><th>내용</th>
	<td><textarea name="content" title="내용"  class="check-empty form-control" >${vo.content}</textarea></td>
</tr>
<tr><th>첨부파일</th>

	<td><div class="row">
			<div>
				<label>
					<input class="form-control" id="file-multiple" type="file" name="file" multiple>
					<i role="button" class="me-4 fa-solid fa-file-circle-plus fs-2"></i>
				</label>
				
				<!-- 드래그드랍으로 파일첨부 -->
				<div class="form-control py-2 mt-2 file-drag">
					<!-- 첨부된 파일이 없는 경우 -->
					<c:if test="${empty vo.fileList}">
						<div class="py-3 text-center">첨부할 파일을 마우스로 끌어 오세요</div>
					</c:if>
					<!-- 첨부된 파일이 있는 경우 -->
					<c:forEach items="${vo.fileList}" var="f" varStatus="status">
						<div class="file-item d-flex gap-2 my-1">
							<button type="button" class="btn-close small" data-seq="${status.index}"></button>
							<span>${f.filename}</span>
						</div>					
					</c:forEach>
				</div>
				
				<!--  
				<div class="d-flex align-items-center">
					<span class="file-name"></span>
					<i role="button" class="ms-4 file-delete d-none text-danger fs-4 fa-solid fa-trash file-delete"></i>
				</div>
				-->
			</div>
			
		</div>
	</td></tr>
	
</table>
<input type="hidden" name="id" value="${vo.id}">
<input type="hidden" name="curPage" value="${page.curPage}">
<input type="hidden" name="search" value="${page.search}">
<input type="hidden" name="keyword" value="${page.keyword}">
<input type="hidden" name="pageList" value="${page.pageList}">
<input type="hidden" name="remove"> <!-- 삭제한 파일id -->
</form>

<div class="btn-toolbar justify-content-center gap-2">
	<button class="btn btn-primary px-4" id="btn-save">저장</button>
	<button class="btn btn-outline-primary px-4" id="btn-cancel">취소</button>
</div>


<script>
var fileList = new FileList();
//첨부된 파일정보를 FileList 객체에 담기
<c:forEach items="${vo.fileList}" var="f">
fileList.setFile( urlToFile( "${f.filepath}", "${f.filename}" ), ${f.id} )
</c:forEach>
console.log( "fileList> ", fileList )

//문자열이 아닌 File 정보가 담기도록 처리한다
function urlToFile( url, filename ){
	var file;
	$.ajax({
		url: url,
		responseType: "blob",
		async: false,
	}).done(function( response ){
		var blob = new Blob( [response] );
		file = new File( [blob], filename )
	})
	return file; 
}

$("#btn-save").click(function(){
	if( emptyCheck() ){ //입력이 되어 있는 경우만 서브밋
		multipleFileUpload();
		$("[name=remove]").val( fileList.info.remove ) //  1, 2
		$("form").submit();
	}
})
$("#btn-cancel").click(function(){
	$("form").attr("action", "info").submit()
})
</script>


</body>
</html>