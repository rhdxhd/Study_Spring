<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>     
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>     

<!-- 댓글이 없는 경우 -->
<c:if test="${empty list}">
	<div class="py-3 text-center">
		<div class="fs-5">등록된 댓글이 없습니다</div>
		<div>첫번째 댓글을 남겨주세요</div>
	</div>
</c:if>	
<!-- 댓글이 있는 경우 -->
<c:forEach items="${list}" var="vo">
	<div class="comment w-pct80 py-3 border-bottom" data-id="${vo.id}">
		<div class="d-flex justify-content-between">
			<div class="d-flex align-items-center mb-2">
				<span class="me-2">
					<c:if test="${empty vo.profile}">
						<i class="font-profile fa-regular fa-user"></i>
					</c:if>
					<c:if test="${not empty vo.profile}">
						<img class="profile" src="${vo.profile}">
					</c:if>
				</span>
				<span>${vo.name} [ ${vo.writedate} ]</span>
			</div>
			<c:if test="${vo.writer eq loginInfo.user_id}">
			<div>
				<span class="me-4">댓글수정 [ <span class="writing">${fn: length(vo.content) }</span> / 200 ]</span>
				<a class="btn btn-outline-info btn-sm btn-modify-save">수정</a>
				<a class="btn btn-outline-danger btn-sm btn-delete-cancel">삭제</a>
			</div>
			</c:if>
		</div>
		
		<div class="content">${fn: replace( fn: replace(vo.content, lf, "<br>"), crlf, "<br>") }</div>
		<div class="hidden d-none"></div>
	</div>
</c:forEach>

<script>
//수정/저장
$(".btn-modify-save").click(function(){
	var _comment = $(this).closest(".comment");
	//수정
	if( $(this).text()=="수정" ){
		modifyStatus( _comment );
	}else{
		$.ajax({
			url: "comment/update",
			data: { id: _comment.data("id"), content: _comment.find("textarea").val() }
		}).done(function(response){
			console.log( response );
			alert( response.message );
			if( response.success ){
				//변경대상인 댓글의 내용만 반영
				_comment.find(".hidden").text( response.content );
				infoStatus( _comment );  //변경내용만 화면에 반영하는 경우
			}
			//commentList();  //목록을 다시 조회해오는 경우
		})
	}
})

//삭제/취소
$(".btn-delete-cancel").click(function(){
	var _comment = $(this).closest(".comment");
	//취소
	if( $(this).text()=="취소" ){
		infoStatus( _comment );
	}else{
		if( confirm("정말 댓글을 삭제하시겠습니까?") ){
			$.ajax({
				url: "comment/delete",
				data: { id: _comment.data("id") }
			}).done(function( response ){
				if(response.success){
					 // 화면에서 해당 댓글 삭제
					_comment.remove();
				}else{
					alert("댓글이 삭제되지 않았습니다");
				}
			})
		}
	}
})

//수정상태
function modifyStatus( _comment ){
	//버튼은 저장/취소
	_comment.find(".btn-modify-save").text("저장")
			.removeClass("btn-outline-info").addClass("btn-primary");	
	_comment.find(".btn-delete-cancel").text("취소")
			.removeClass("btn-outline-danger").addClass("btn-secondary");
	//내용은 textarea에 보이게
	var _content = _comment.find(".content");
	var content = _content.html().replace(/<br>/g, "\n");
	_content.html(  `<textarea class="form-control">\${content}</textarea>`  );
	_comment.find(".writing").text( content.length );
	_comment.find(".hidden").html( `\${content}` );  //취소시 처리를 위한 정보
}

//정보상태
function infoStatus( _comment ){
	//버튼은 수정/삭제
	_comment.find(".btn-modify-save").text("수정")
			.removeClass("btn-primary").addClass("btn-outline-info");	
	_comment.find(".btn-delete-cancel").text("삭제")
			.removeClass("btn-secondary").addClass("btn-outline-danger");	
	//textarea의 내용이 텍스트로 보이게
	var _content = _comment.find(".content");
	//var content = _content.find("textarea").val().replace(/\n/g, "<br>");
	var content = _comment.find(".hidden").html().replace(/\n/g, "<br>");
	_content.html( content );
	_comment.find(".writing").text( _comment.find(".hidden").text().length )
	_comment.find(".hidden").empty();
}

</script>























	
	