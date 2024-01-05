/**
 * 공통 적용 함수 선언
 */
 
$(function(){
	
	if( $(".date").length > 0 ) {
		
		var today = new Date();
		var range = today.getFullYear()-100 + " : " + today.getFullYear();  // "1990 : 2023"; //지금으로부터 100년 전
		
		$.datepicker.setDefaults({
			dateFormat: "yy-mm-dd",
			changeYear: true,
			changeMonth: true,
			showMonthAfterYear: true,
			dayNamesMin: ["일", "월", "화", "수", "목", "금", "토"],
			monthNamesShort : ["1월", "2월" , "3월" , "4월" , "5월" , "6월" ,
							   "7월" , "8월" , "9월" , "10월" , "11월" , "12월"],
			maxDate: today, 				   
			yearRange: range,				   
		})		
		
	}

	$(".date").datepicker();	
	$(".date").attr("readonly", true); //읽기전용으로	
	
	//날짜 선택시 삭제보이게
	$(".date").change(function() {
		$(this).next(".date-delete").css("display", "inline");
	})
	
	//삭제클릭시 날짜 없애기
	$(".date-delete").click(function() {
		$(this).prev(".date").val( "" )
		$(this).css("dispaly", "none");
	})
	
	
	//파일선택시
	$("input#file-single").change(function(){
//		console.log ( $(this) )
	//  console.log ( this.files[0] )
		var _preview = $(this).closest(".file-info").find(".file-preview")
		var _delete = $(this).closest(".file-info").find(".file-delete")
		
	
		var attached = this.files[0];
		if( attached ) {
			console.log( 'name> ' , attached.name )
			//파일크기제한을 두고자 한다면
			if ( rejectFile(attached, $(this)) ) return;
			
			//이미지만 첨부해야한다면
			if( isImage(attached.name) ) {
				_delete.removeClass("d-none") //삭제보이게
				
				if( _preview.length > 0 ) {
					_preview.html("<img>");
					
					var reader = new FileReader();
					reader.readAsDataURL( attached );
					reader.onload = function( e ) {
						//console.log( e.target.result )
						//console.log( this.result )
						_preview.children("img").attr("src", this.result )
					}
				}
			
				}else{
//					_preview.empty(); // _preview.children("img").remove()
//					$(this).val("");
					initFileInfo( $(this) )
				}	
				
			console.log( 'attached> ', $(this).val() )
		}
		
	})
	
		
	$(".file-delete").click(function(){
		//선택했던 파일정보 삭제. 미리보기도 안보이게, 삭제버튼도 안보이게
//		var _info = $(this).closest(".file-info");	
//		_info.find(".file-preview").empty();	
//		_info.find("input[type=file]").val("");
//		$(this).addClass("d-none")	
		initFileInfo( $(this) )
	})	
		

})


//선택했던 파일정보 삭제. 미리보기도 안보이게, 삭제버튼도 안보이게
function initFileInfo(tag) {
	var _info = tag.closest(".file-info");	
		_info.find(".file-preview").empty();	
		_info.find("input[type=file]").val("");
		_info.find(".file-delete").addClass("d-none")	
}


//파일크기제한
function rejectFile( fileInfo, tag ) {
	// 1K=1024, 1M=1024*1024, 1G=1024*1024*1024
	if( fileInfo.size > 1024*1024*10 ) { //10M
		alert("10Mb 를 넘는 파일은 첨부할 수 없습니다")
//		tag.val("");
		//이전에 이미지선택된게 있었다면 미리보기도 없애야 한다.
//		tag.closest(".file-info").find(".file-preview").empty()
//		tag.closest(".file-info").find(".file-delete").addClass("d-none")
		initFileInfo( tag )
		return true;
	}else	
		return false;
}




//이미지파일인지 확인
function isImage ( filename ){
	//abc.png, a.bc.jpg, ...
	var imgs = [ "png", "jpg", "jpeg", "gif", "bmp", "webp" ]
	var ext = filename.substr(filename.lastIndexOf(".")+1) ;
	// substr(start, n개), substring(start, finish)
	return imgs.indexOf(ext) == -1 ? false : true;
}

