<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>     
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>

<div class="row justify-content-center">
    <div class="col-lg-5">
        <div class="card shadow-lg border-0 rounded-lg mt-5">
            <div class="card-header">
	            <h3 class="text-center font-weight-light my-4">
					<a href="<c:url value='/'/>"><img src="<c:url value='/img/hanul.logo.png'/>"></a>	
				</h3>
				<h4 class="text-center">비밀번호 찾기</h4>
				</div>
            <div class="card-body">
                <form method="post" action="resetPassword">
                    <div class="form-floating mb-3">
                        <input class="form-control" name="user_id" type="text" required placeholder="아이디">
                        <label>아이디</label>
                    </div>
                    <div class="form-floating mb-3">
                        <input class="form-control" name="email" type="text" required placeholder="이메일">
                        <label>이메일</label>
                    </div>
                    <div class="d-flex align-items-center justify-content-between mt-4 mb-0">
	                    <button class="py-3 btn btn-primary px-4">확인</button>
                        <a class="py-3 btn btn-outline-primary  px-4" href="login">취소</a>
                    </div>
                </form>
            </div>
        </div>
    </div>
</div>

</body>
</html>










