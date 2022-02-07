package com.adamsoft.moviereview.controller;

import com.adamsoft.moviereview.dto.UploadResultDTO;
import lombok.extern.log4j.Log4j2;
import net.coobird.thumbnailator.Thumbnailator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RestController
@Log4j2
public class UploadController {
    @Value("${com.adamsoft.upload.path}") // application.properties 변수
    private String uploadPath;

    @PostMapping(value = "/uploadajax")
    public ResponseEntity<List<UploadResultDTO>> uploadFile(MultipartFile[] uploadFiles) {
        List<UploadResultDTO> resultDTOList = new ArrayList<>();

        for (MultipartFile uploadFile : uploadFiles) {
            //이미지 파일만 업로드 가능
            if(uploadFile.getContentType().startsWith("image") == false) {
                log.warn("this file is not image type");
                return new ResponseEntity<>(HttpStatus.FORBIDDEN);
            }
            //실제 파일 이름 IE나 Edge는 전체 경로가 들어오므로
            String originalName = uploadFile.getOriginalFilename();
            String fileName = originalName.substring(originalName.lastIndexOf("\\") + 1);

            log.info("fileName: " + fileName);
            String realUploadPath = makeFolder();
            //UUID
            String uuid = UUID.randomUUID().toString();
            String saveName = uploadPath + File.separator + realUploadPath + File.separator + uuid + fileName;
            File saveFile = new File(saveName);
            try {
                uploadFile.transferTo(saveFile);
                //섬네일 파일 이름은 중간에 s_로 시작하도록
                File thumbnailFile = new File(uploadPath + File.separator + realUploadPath + File.separator + "s_" + uuid +fileName );
                //섬네일 생성
                Thumbnailator.createThumbnail(saveFile, thumbnailFile,100,100);

                resultDTOList.add(new UploadResultDTO(fileName, uuid,realUploadPath));
            } catch (IOException e) {
                System.out.println(e.getLocalizedMessage());
                e.printStackTrace();
            }
        }
        System.out.println("뭐지" + resultDTOList.toString());
        return new ResponseEntity<>(resultDTOList, HttpStatus.OK);
    }

    @PostMapping("/removefile")
    public ResponseEntity<Boolean> removeFile(String fileName) {
        String srcFileName = null;
        try {
            srcFileName = URLDecoder.decode(fileName,"UTF-8");
            File file = new File(uploadPath + File.separator + srcFileName);
            boolean result = file.delete();
            File thumbnail = new File(file.getParent(), "s_" + file.getName());
            result = thumbnail.delete();
            return new ResponseEntity<>(result, HttpStatus.OK);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return new ResponseEntity<>(false, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    private String makeFolder() {
        String str = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd"));
        String realUploadPath =  str.replace("//", File.separator);
        File uploadPathDir = new File(uploadPath, realUploadPath);
        if (uploadPathDir.exists() == false) {
            uploadPathDir.mkdirs();
        }
        return realUploadPath;
    }

    @GetMapping("/display")
    public ResponseEntity<byte[]> getFile(String filename, String size) {
        ResponseEntity<byte[]> result = null;
        try {
            log.info("filename: " + URLDecoder.decode(filename,"UTF-8"));

            File file = new File(uploadPath + File.separator + URLDecoder.decode(filename,"UTF-8"));
            log.info("filepath:" + uploadPath + File.separator + URLDecoder.decode(filename,"UTF-8"));
            log.info("file: " + file);

            if(size != null && size.equals("1")){
                file = new File(file.getParent(), file.getName().substring(2));
            }

            HttpHeaders header = new HttpHeaders();
            header.add("Content-Type", Files.probeContentType(file.toPath()));
            result = new ResponseEntity<>(FileCopyUtils.copyToByteArray(file), header, HttpStatus.OK);
        } catch (Exception e) {
            log.error(e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return result;
    }
}

