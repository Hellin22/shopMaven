package com.shop.service;

import lombok.extern.java.Log;
import org.springframework.stereotype.Service;
import java.io.File;
import java.io.FileOutputStream;
import java.util.UUID;

@Service
@Log
public class FileService {

    public String uploadFile(String uploadPath, String originalFileName, byte[] fileData) throws Exception{
        UUID uuid = UUID.randomUUID();
        // 서로 다른 객체임을 구분하기 위해 uuid 사용 -> 중복될 일이 거의 없어 파일명 중복이 거의 발생하지 않음.

        String extension = originalFileName.substring(originalFileName.lastIndexOf("."));
        // .을 기준으로 확장자명을 추출 (example.png -> .png)

        String savedFileName = uuid.toString() + extension;
        String fileUploadFullUrl = uploadPath + "/" + savedFileName;
        // uuid 값과 조합하고 저장할 위치 선정

        FileOutputStream fos = new FileOutputStream(fileUploadFullUrl);
        // 바이트단위 출력을 내보내는 클래스, 생성자로 파일이 저장될 위치와 이름을 넘겨주어 파일에 쓸 파일 출력 스트림 만듦
        fos.write(fileData); // fileData를 파일 출력 스트림에 입력
        fos.close();
        return savedFileName; // 업로드된 파일 이름 반환
    }

    public void deleteFile(String filePath) throws Exception{
        File deleteFile = new File(filePath); // 파일이 저장된 경로로 파일 객체 생성
        if(deleteFile.exists()) {
            deleteFile.delete(); // 존재한다면 삭제
            log.info("파일을 삭제하였습니다.");
        } else {
            log.info("파일이 존재하지 않습니다.");
        }
    }

}