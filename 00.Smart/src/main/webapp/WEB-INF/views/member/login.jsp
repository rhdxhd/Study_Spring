<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>     
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
<style>
#naver { background: url("<c:url value='/img/naver.png'/>") center/contain no-repeat #03c75a; }
#kakao { background: url("<c:url value='/img/kakao.png'/>") center/contain no-repeat #FEE500; }
</style>
</head>
<body>

<div class="row justify-content-center">
    <div class="col-lg-5">
        <div class="card shadow-lg border-0 rounded-lg mt-5">
            <div class="card-header">
	            <h3 class="text-center font-weight-light my-4">
					<a href="<c:url value='/'/>"><img src="<c:url value='/img/hanul.logo.png'/>"></a>	
				</h3></div>
            <div class="card-body">
                <form method="post" action="smartLogin">
                    <div class="form-floating mb-3">
                        <input class="form-control" name="user_id" type="text" placeholder="아이디">
                        <label>아이디</label>
                    </div>
                    <div class="form-floating mb-3">
                        <input class="form-control" name="user_pw" type="password" placeholder="비밀번호">
                        <label>비밀번호</label>
                    </div>
                    <button class="py-3 btn btn-primary form-control">로그인</button>
                    <!--  
                    <div class="form-check mb-3">
                        <input class="form-check-input" id="inputRememberPassword" type="checkbox" value="">
                        <label class="form-check-label" for="inputRememberPassword">Remember Password</label>
                    </div>
                    -->
                </form>
                    <hr>
                    <div class="d-flex gap-3">
                    	<input type="button" class="form-control p-3" id="naver">
                    	<input type="button" class="form-control p-3" id="kakao">
                    </div>
                    <div class="d-flex align-items-center justify-content-between mt-4 mb-0">
                        <a class="small" href="join">회원가입</a>
                        <a class="small" href="findPassword">비밀번호찾기</a>
                    </div>
            </div>
        </div>
    </div>
</div>

<script>
$(function(){
	if( ${! empty fail} ){
		alert("아이디나 비밀번호가 일치하지 않습니다");
	}
})

$("#naver, #kakao").click(function(){
// 	console.log( $(this).attr("id") + "Login" )
	location =  $(this).attr("id") + "Login";
})
	
</script>

</body>
</html>






