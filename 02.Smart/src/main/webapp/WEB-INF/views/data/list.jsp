<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>     
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>
<h3 class="mb-4">공공데이터 목록</h3>

<ul class="nav nav-pills justify-content-center my-3">
  <li class="nav-item">
    <a class="nav-link active" href="#">약국조회</a>
  </li>
  <li class="nav-item">
    <a class="nav-link" href="#">유기동물조회</a>
  </li>
  <li class="nav-item">
    <a class="nav-link" href="#">기타조회</a>
  </li>
  <li class="nav-item">
    <a class="nav-link" href="#">기타조회</a>
  </li>
</ul>

<div class="row mb-2 justify-content-between">
	<div class="col-auto animal-top">
	</div>
	<div class="col-auto">
		<select id="pageList" class="form-select">
			<c:forEach var="i" begin="1" end="5">
			<option value="${10 *i}">${10 *i}개씩</option>
			</c:forEach>
		</select>
	</div>
</div>	
<div id="data-list"></div>

<script>

$("ul.nav-pills li").click(function(){
	$("ul.nav-pills li a").removeClass("active");
	$(this).children("a").addClass("active");
	var idx = $(this).index();
	if( idx==0 ){
		pharmacy_list( 1 );
	}else if( idx==1 ){
		animal_list( 1 );
	}
})

$(document)
.on("click", ".pagination li a", function(){
	pharmacy_list( $(this).data("page") );
})

$("#pageList").change(function(){
	page.pageList = $(this).val()
	pharmacy_list( 1 );
})

$(function(){
	$("ul.nav-pills li").eq(0).trigger("click"); //클릭이벤트 강제발생시키기
})

//약국목록조회
function pharmacy_list( curPage ){
	var table
	= `<table class="table tb-list pharmacy">
		<colgroup><col width="300px"><col width="160px"><col></colgroup>
		<thead><tr><th>약국명</th><th>전화번호</th><th>주소</th></tr></thead>
		<tbody></tbody>
		</table>`;
	$("#data-list").html( table );
	
	$.ajax({
		url: "pharmacy",
		data: { pageNo: curPage, numOfRows: page.pageList }
	}).done(function( response ){
		//console.log( response.response.body )
		respose = response.response.body;
		console.log( respose )
		
		table = "";
		$( respose.items.item ).each(function(){
			table += `<tr><td>\${this.yadmNm}</td>
						  <td>\${this.telno ? this.telno : "-"}</td>
						  <td class="text-start">\${this.addr}</td>
					  </tr>` ;
		})
		$("#data-list .pharmacy tbody").html( table );
		
		makePage( respose.totalCount , curPage);
	})
	
}

var page = { pageList:10, blockPage:10 };


//유기동물목록조회
function animal_list( pageNo ){
}
</script>















</body>
</html>