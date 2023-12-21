<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>  
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>
	<a href="<c:url value='/'/>">홈으로</a>
	<hr>
	<h3>회원가입</h3>
	
	<form method="post" action="joinRequest">
	<table border="1">
	<tr><th>성명</th>
		<td><input type="text" name="name"></td>
	</tr>
	<tr><th>성별</th>
		<td>
			<input type="radio" name="gender" id="male" value="남">
			<label for="male">남</label>			
			
			<label><input type="radio" name="gender" value="여" checked>여</label>
		</td>
	</tr>
	
	<tr><th>이메일</th>
		<td><input type="text" name="email"></td>
	</tr>
	
	<tr><th>나이</th>
		<td><input type="text" name="age"></td>
	</tr>
	
	
	</table>
	<input type="submit" value="회원가입(HttpServletRequest)">
	<input type="submit" value="회원가입(@RequestParam)" onclick="action='joinParam'">
	<input type="submit" value="회원가입(DataObject)" onclick="action='joinData'">
	<input type="submit" value="회원가입(@PathVariable)" onclick="go_submit(this.form)">
	</form>
	
	<script>
	function go_submit(f){
		//action='joinPath'
		f.action = 'joinPaht/' + f.name.vlue
		+ '/' + f.gender.value + '/' + f.email.value + '/' + f.age.value
		
	}
	
	</script>
	
</body>
</html>