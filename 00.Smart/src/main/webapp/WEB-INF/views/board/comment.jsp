<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>     

<div id="comment-regist" class="row my-4 justify-content-center">
	<div class="w-pct80 justify-content-between d-flex pb-2 h-px40">
		<span>댓글작성 [ <span class="writing">0</span> / 200 ]</span>
		<a class="btn btn-outline-primary btn-sm d-none btn-register">댓글등록</a>
	</div>
	<div class="comment w-pct80">
		<div class="form-control py-3 text-center guide">
		${empty loginInfo ? "댓글을 입력하려면 여기를 클릭후 로그인하세요" : "댓글을 입력하세요"}
		</div>
	</div>
</div>

<div id="comment-list" class="row justify-content-center mx-0">
</div>

<script>
commentList();

//댓글목록조회
function commentList(){
	$.ajax({
		url: "comment/list/${vo.id}",
		//url: "<c:url value='/hr/list'/>",
	}).done(function( response ){
		//console.log( response );
		$("#comment-list").html( response )
	})
}

$(".btn-register").click(function(){
	$.ajax({
		url: "comment/register",
		data: { board_id: ${vo.id}
				, content: $("#comment-regist textarea").val()
				, writer: "${loginInfo.user_id}" 
			  },
	}).done(function( response ){
		if( response ){
			alert("댓글이 등록되었습니다");
			initComment();
			commentList();
		}else{
			alert("댓글이 등록 실패ㅠㅠ");			
		}
	})
})

//댓글등록 부분 초기화
function initComment(){
	$("#comment-regist .writing").text(0);
	$(".btn-register").addClass("d-none");
	$("#comment-regist .comment").html(
	`
	<div class="form-control py-3 text-center guide">
		${empty loginInfo ? "댓글을 입력하려면 여기를 클릭후 로그인하세요" : "댓글을 입력하세요"}
		</div>
	` );
}

$("#comment-regist .comment").click(function(){
	if( ${empty loginInfo} ){
		if( confirm("로그인 하시겠습니까?") ){
			$("form").attr("action", "<c:url value='/member/login'/>").submit();
		}
	}else{
		//클릭시마다 textarea가 추가되지 않도록 guide가 있을때만 추가
		if( $(this).children( ".guide" ).length == 1 ){
			$(this).append( "<textarea class='form-control'></textarea>" );
			$(this).children( "textarea" ).focus();
			$(this).children( ".guide" ).remove();
		}
	}
})

$(document)
.on("keyup", "#comment-regist textarea", function(){
// 	var input = $(this).val();
// 	if( input.length > 200 ){
// 		alert("최대 200자까지 입력할 수 있습니다");
// 		input = input.substr(0,200);
// 		$(this).val( input );
// 	}
	var input = writing( $(this) );
	
	$("#comment-regist .writing").text( input.length );
	if( input.length > 0 )	$(".btn-register").removeClass( "d-none" )
	else 					$(".btn-register").addClass( "d-none" )
	
}).on("contextmenu", "#comment-regist textarea", function(e){
	e.preventDefault(); //textarea 에 우클릭 방지
	
}).on("keyup", "#comment-list textarea", function(){
	var input = writing( $(this) );
	$(this).closest(".comment").find(".writing").text( input.length );
	
})

function writing( tag ){
	var input = tag.val();
	if( input.length > 200 ){
		alert("최대 200자까지 입력할 수 있습니다");
		input = input.substr(0,200);
		tag.val( input );
	}
	return input;
}


</script>
<style>
.comment textarea { height: 90px }
.h-px40 { height: 40px }
</style>
















