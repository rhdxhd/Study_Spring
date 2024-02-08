/**
 * 유기동물 관련 처리 함수 
 */
 

$(document)
.on("change", "#sido", function() { //시도변경시
	animal_sigungu();
	animal_list( 1 );
})

.on("change", "#sigungu", function() { //시군구 변경시
	animal_shelter();
	animal_list( 1 );
})
.on("change", "#shelter", function() { //보호소 변경시
	animal_list( 1 );	
})
.on("change", "#upkind", function() { //축종변경시( 개/고양이/기타 )
	animal_kind();
	animal_list( 1 );	
})
.on("change", "#kind", function() { //품종변경시
	animal_list( 1 );	
})



//품종조회
function animal_kind() {
	$("#kind").remove();
	if( $("#upkind").val()=="") return; //축종 선택한 경우만 품종조회 가능
	
	$.ajax({
		url: "animal/kind",
		data: { upkind: $("#upkind").val() }
		
	}).done(function(response){
		$("#upkind").after( response );
		
	})
}






function animal_type(){
	
//	축종코드
// - 개 : 417000
// - 고양이 : 422400
// - 기타 : 429900

	 var tag=`
		 <select class="form-select w-px200" id="upkind">
			<option value="">축종 선택</option>
			<option value="417000">개</option>
			<option value="422400">고양이</option>
			<option value="429900">기타</option>
		</select>
	 	`;
	 	
	 	$(".animal-top").append( tag );
}







function animal_sigungu() {
	$("#sigungu").remove();
	$("#shelter").remove(); //시군구가 있어야만 보호소도 조회가능
	if( $("sido").val()=="") return; //시도코드가 있는 경우만 시군구 조회
	
	$.ajax({
		url: "animal/sigungu",
		data: { sido:$("#sido").val() }
	}).done(function (response) {
		$("#sido").after( response );
		
	})
	
}






function animal_shelter() {
	$("#shelter").remove();
	
	//시군구코드( 입력 시 데이터 O, 미입력 시 데이터 X )
	if( $("#sigungu").val()=="" ) return;
	$.ajax({
		url: "animal/shelter",
		data: { sido: $("#sido").val(), sigungu: $("#sigungu").val() }
	}).done(function(response) {
		$("#sigungu").after( response )	
	})
	
}
	


