<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>     
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>
<h3 class="mb-4">신규사원등록</h3>

<form method="post" action="insert">
<table class="table tb-row">
<colgroup>
	<col width="180px">
	<col>
</colgroup>

<tr><th>사원명</th>
	<td>
		<div class="row">
			<div class="col-auto">
				<input class="form-control" type="text" name="last_name" placeholder="성">
			</div>
			<div class="col-auto">
				<input class="form-control" type="text" name="first_name" placeholder="명">
			</div>
		</div>
	</td>
</tr>
<tr><th>이메일</th>
	<td><div class="row">
			<div class="col-auto">
				<input class="form-control" type="text" name="email">
			</div>
		</div>
	</td>
</tr>
<tr><th>전화번호</th>
	<td><div class="row">
			<div class="col-auto">
				<input class="form-control" type="text" name="phone_number">
			</div>
		</div>	
	</td>
</tr>
<tr><th>입사일자</th>
	<td>
		<div class="row">
			<div class="col-auto">
				<input class="form-control date" type="text" name="hire_date">
			</div>
		</div>	
	</td>
</tr>
<tr><th>급여</th>
	<td><div class="row">
			<div class="col-auto">
				<input class="form-control" type="text" name="salary">
			</div>
		</div>	
	</td>
</tr>
<tr><th>부서</th>
	<td><div class="row">
			<div class="col-auto">
				<select name="department_id" class="form-select">
					<option value="-1">소속없음</option>
					<c:forEach items="${departments}" var="d">
					<option value="${d.department_id }" >${d.department_name }</option>
					</c:forEach>
				</select>
			</div>
		</div>	
	</td>
</tr>
<tr><th>업무</th>
	<td><div class="row">
			<div class="col-auto">
				<select name="job_id" class="form-select">
					<c:forEach items="${jobs}" var="j">
					<option value="${j.job_id }" >${j.job_title }</option>
					</c:forEach>
				</select>
			</div>
		</div>
	
	</td>
</tr>

<tr><th>매니저</th>
	<td><div class="row">
			<div class="col-auto">
				<select name="manager_id" class="form-select">
					<option value="-1">매니저없음</option>
					<c:forEach items="${managers}" var="m">
					<option value="${m.employee_id }" >${m.name }</option>
					</c:forEach>
				</select>
			</div>
		</div>
	
	</td>
</tr>

</table>

</form>

<div class="btn-toolbar justify-content-center gap-2">
	<button class="btn btn-primary" onclick="$('form').submit()">저장</button>
	<button class="btn btn-outline-primary" id="btn-cancel">취소</button>
</div>

<script type="text/javascript">
$(function(){
	$(".date").val( $.datepicker.formatDate("yy-mm-dd", new Date()))
})

//$("#btn-cancel").click(function(){
$("#btn-cancel").on('click', function(){
	location = "list"
})



</script>

</body>
</html>