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
		var _name = $(this).closest(".file-info").find(".file-name")
		
	
		var attached = this.files[0];
		console.log( 'attached> ' , attached )
		
		if( attached ) {
			//파일크기제한을 두고자 한다면
			if ( rejectFile(attached, $(this)) ) return;
			
			_name.text(attached.name); //파일명 보이게
			_delete.removeClass("d-none"); //삭제버튼 보이게
			
			
			//이미지만 첨부해야한다면
			if( isImage(attached.name) ) {
				
				if( _preview.length > 0 ) {
					_preview.html("<img>");
					
					var reader = new FileReader();
					reader.readAsDataURL( attached );
					reader.onload = function( e ) {
						//					console.log( e.target.result )
						//					console.log( this.result )
						_preview.children("img").attr("src", this.result )
					}
				}
			
				}else{
//					_preview.empty(); // _preview.children("img").remove()
//					$(this).val("");
					if(  $(this).hasClass("image-only") ) {
						 initFileInfo( $(this) )
					}
					
				}	
				
			console.log( 'file> ', $(this).val() )
		}
		
	})
	
		
		
	$(".file-drag")
	.on("dragover dragleave drop", function(e){
		e.preventDefault(); // 드롭을 허용하기 위해 기본 동작 취소
		
		//드래그 오버시 입력태그에 커서 있을때처럼 적용하기
		if( e.type == "dragover" ) $(this).addClass("drag-over");
		else					   $(this).removeClass("drag-over");
		
	})
	.on("drop", function(e){
		console.log("e>", e)
		console.log("e>", e.originalEvent.dataTransfer.files)
		var files = filterFolder( e.originalEvent.dataTransfer);
		
		$(files).each(function() {
			fileList.setFile( this )
		})
		console.log( 'fileList>' , fileList )
		fileList.showFile(); //끌어온 파일목록 보이게
	})
	;
		
		
	$("body")
	.on("dragover dragleave drop", function(e){
		e.preventDefault();
	});	
		
	$("#file-multiple").on("change", function(){
		var files = this.files;
		$(files).each(function() {
			fileList.setFile( this )
		})
		fileList.showFile();
	})
		
		
})



function multipleFileUpload() {
	//FileList 객체의 files의 파일정보를 input file태그에 넣기
	var transfer = new DataTransfer();
	var files = fileList.getFile();
	if( files.length > 0 ) {
		for(i=0; i <files.length; i++){
			transfer.items.add( files[i] );
		}
		
	} 
	console.log('transfer.files>', transfer.files)
	$("#file-multiple").prop("files", transfer.files)	
	
}




//파일관련처리
function FileList(){
	this.files = [];
	this.setFile = function(file) {
		this.files.push ( file );
	}
	this.getFile = function() {
		return this.files;
	}
	//해당 파일항목 삭제
	//slice(시작, 끝) : 시작위치에서 끝위치-1 까지를 반환, 끝 파라미터 생략가능, 원래 데이터는 그대로 유지됨.
	//splice(시작, 갯수) : 시작 위치에서 지정 갯수만큼 제거. 원래 데이터가 바뀜
	this.removeFile = function( i ) {
		this.files.splice(i,1);
		
	
	}
	
	this.showFile = function() {
		var tag = ""
		if( this.files.length > 0 ) { //파일목록에 파일이 있는 경우
			for(i=0; i<this.files.length; i++) {
				tag += `
					<div class="file-item d flex gap-2">
						<button type="button" class="btn-close small" data-seq="${i}"></button>
						<span>${ this.files[i].name }</span>
					</div>
				`;
				
			}
		}else{
		
		}
		$(".file-drag").html( tag );
	}
}


//폴더 제한하기
function filterFolder( transfer ) {
	var files = [], folder = false;
	for( i=0; i<transfer.items.length; i++ ){
		var entry = transfer.items[i].webkitGetAsEntry();
		// console.log('idx>', i, entry)
		if( entry.isFile ) files.push( transfer.files[i] )
		else				folder = true;
	}
	if( folder ) {
		alert("폴더는 첨부할 수 없습니다");
		
	}	
	return files;
	
}






$(document)
.on("click", ".file-item .btn-close", function(){
	//console.log( 'idx>' , $(this).data("seq") )
	fileList.removeFile( $(this).data("seq") )
	fileList.showFile()
	
})

		
		
		
		
	$(".file-delete").click(function(){
		//선택했던 파일정보 삭제. 미리보기도 안보이게, 삭제버튼도 안보이게
//		var _info = $(this).closest(".file-info");	
//		_info.find(".file-preview").empty();	
//		_info.find("input[type=file]").val("");
//		$(this).addClass("d-none")	
		initFileInfo( $(this) )
	})
		








//선택했던 파일정보 삭제. 미리보기도 안보이게, 삭제버튼도 안보이게
function initFileInfo(tag) {
	var _info = tag.closest(".file-info");	
		_info.find(".file-name").empty();	//선택한 파일명 안보이게
		_info.find(".file-preview").empty();	//미리보기 이미지 안보이게
		_info.find("input[type=file]").val("");    //선택한 파일정보 초기화
		_info.find(".file-delete").addClass("d-none")	 //삭제버튼 안보이게
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










//입력여부확인
function emptyCheck() {
	var ok = true;
	$(".check-empty").each(function(){
		if($(this).val() == "" ) {
			alert( $(this).attr("title") + "입력하세요!" );
			$(this).focus();
			ok = false;
			return ok;
			
		}
		
	})
	return ok;
}
