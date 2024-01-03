package kr.co.middle.common;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

public class CommonService {

	//프로필, 공지사항, 방명록
	public String fileUpload(String category, MultipartFile file, HttpServletRequest request) {
		// 물리적 저장 : d://app//upload/profile/2024/01/03/abc.jpg
		// http://localhost:80/file/profile/2024/01/03/abc.jpg
		// http://localhost:80/file/board/2024/01/03/abc.jpg
		String upload = "d://app/upload/" + category 
						+ new SimpleDateFormat("/yyyy/MM/dd/").format( new Date() );
		//폴더가 있는지 확인후 없으면 폴더 만들기
		File folder = new File( upload );
		if ( ! folder.exists() ) folder.mkdirs();
		//업로드하는 파일명을 고유한 아이디로 저장하기
		String ext = StringUtils.getFilenameExtension(file.getOriginalFilename()); //jpg
		String filename = UUID.randomUUID().toString() + "." + ext; // fda67h-adf8lh.jpg
		
		try {
			//파일을 해당 폴더에 저장하기
			file.transferTo(new File(upload, filename));
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		// upload: d://app//upload/profile/2024/01/03
		
		// 물리적 저장 : d://app//upload  /profile/2024/01/03/abc.jpg
		// DB에 저장될 파일명 http://localhost:80/file   /profile/2024/01/03/abc.jpg
		return upload.replace( "d://app//upload", fileURL(request) ) + filename;
		
		
	}

	
	public String fileURL(HttpServletRequest request) {
		StringBuffer url = new StringBuffer( " http://");
		url.append( request.getServerName() ).append( ":" )  //http://localhost:
		   .append( request.getServerPort() ).append( "/file" );  //http://localhost:80/file
		
		return url.toString();
		
	}
	
	
}
