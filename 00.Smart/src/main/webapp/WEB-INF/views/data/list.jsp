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
    <a class="nav-link active" >약국조회</a>
  </li>
  <li class="nav-item">
    <a class="nav-link"  >유기동물조회</a>
  </li>
  <li class="nav-item">
    <a class="nav-link"  >기타조회</a>
  </li>
  <li class="nav-item">
    <a class="nav-link" >기타조회</a>
  </li>
</ul>

<div class="row mb-2 justify-content-between">
	<div class="d-flex gap-2 col-auto animal-top">
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

<jsp:include page="/WEB-INF/views/include/loading.jsp"/>
<jsp:include page="/WEB-INF/views/include/modal.jsp"/>

<script src="<c:url value='/js/animal.js'/>"></script>
<script type="text/javascript" 
	src="//dapi.kakao.com/v2/maps/sdk.js?appkey=fbee557ea83a719aee6aecf5309271c1"></script>

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
	if( $("table.pharmacy").length>0 )
		pharmacy_list( $(this).data("page") );
	else
		animal_list( $(this).data("page") );
})
.on("click", ".map", function(){
	//XPos, YPos 가 있는 경우만 지도표시 가능 
	if( $(this).data("x") != "undefined" && $(this).data("y") != "undefined"  ){
		showKakaoMap( $(this) );
	}else{
		alert("위경도가 없어 위치를 지도에 표시할 수 없습니다")
	}
})

function showKakaoMap( tag ){
	
	$("#map").remove();
	$("#modal-map").after(`<div id="map" style="width:668px;height:700px;"></div>`);
	
	var xy = new kakao.maps.LatLng( tag.data("y"), tag.data("x") );
	var container =  document.getElementById('map'); //지도를 담을 영역의 DOM 레퍼런스
	var options = { //지도를 생성할 때 필요한 기본 옵션
		center: xy, //지도의 중심좌표.
		level: 4 //지도의 레벨(확대, 축소 정도)
	};
	var map = new kakao.maps.Map(container, options); //지도 생성 및 객체 리턴

	// 마커를 생성합니다
	var marker = new kakao.maps.Marker({
	    position: xy
	});
	// 마커가 지도 위에 표시되도록 설정합니다
	marker.setMap(map);

	// 인포윈도우를 생성합니다
	var name = tag.text();
	var infowindow = new kakao.maps.InfoWindow({
	    position : xy, 
	    content : `<div style="padding:5px;" class="text-danger fw-bold">\${name}</div>`
	});
	// 마커 위에 인포윈도우를 표시합니다. 두번째 파라미터인 marker를 넣어주지 않으면 지도 위에 표시됩니다
	infowindow.open(map, marker); 

	//모달태그 body로 map 태그를 옮기기
	$("#modal-map .modal-body").html( $("#map") );
	//모달태그가 보이게 하기
	new bootstrap.Modal( $("#modal-map") ).show();

}

$("#pageList").change(function(){
	page.pageList = $(this).val()
	
	if( $("table.pharmacy").length > 0 )
		pharmacy_list( 1 );
	else
		animal_list( 1 );
})

$(function(){
	$("ul.nav-pills li").eq(1).trigger("click"); //클릭이벤트 강제발생시키기
})

//약국목록조회
function pharmacy_list( curPage ){
	$(".animal-top").empty();
	
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
			table += `<tr><td><a class="map text-link" data-x="\${this.XPos}" data-y="\${this.YPos}">\${this.yadmNm}</a></td>
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
function animal_list( curPage ){
	$(".loading").removeClass("d-none");
// 	$(".loading").show() ;
	
	//시도조회
	if( $("#sido").length==0 ) animal_sido();
	
	var animal = {};
	animal.pageNo = curPage;
	animal.numOfRows = page.pageList;
	animal.sido = $("#sido").length==1 ? $("#sido").val() : "";
	animal.sigungu = $("#sigungu").length==1 ? $("#sigungu").val() : "";
	animal.shelter = $("#shelter").length==1 ? $("#shelter").val() : "";
	animal.upkind = $("#upkind").length==1 ? $("#upkind").val() : "";
	animal.kind = $("#kind").length==1 ? $("#kind").val() : "";
	
	$.ajax({
		url: "animal/list",
		//data: { pageNo: curPage, numOfRows: page.pageList }
		data: JSON.stringify( animal ),
		type: 'post',
		contentType: 'application/json'
		
	}).done(function( response ){
		$("#data-list").html( response )
		
		setTimeout(function(){ $(".loading").addClass("d-none") }, 1000);
// 		setTimeout(function(){ $(".loading").hide() }, 1000);
	})
	
}

//유기동물시도조회
function animal_sido(){
	$.ajax({
		url: "animal/sido"
	}).done(function(response){
		$(".animal-top").prepend( response )
		animal_type();
	})
}

</script>















</body>
</html>