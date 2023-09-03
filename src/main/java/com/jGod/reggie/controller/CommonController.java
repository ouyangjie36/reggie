package com.jGod.reggie.controller;

import com.jGod.reggie.common.R;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.UUID;

@RestController
@RequestMapping("/common")
public class CommonController {
    @Value("${reggie.path}")
    private String basePath;

    /**
     * 文件上传
     *
     * @param file
     * @return
     */
    @PostMapping("/upload")
    public R<String> upload(MultipartFile file) {
        File dir = new File(basePath);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        String suffix = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."));
        String fileName = UUID.randomUUID() + suffix;
        try {
            file.transferTo(new File(basePath + fileName));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return R.success(fileName);
    }

    @GetMapping("/download")
    public void download(String name, HttpServletResponse response){
        try {
            File file = new File(basePath+name);
            if(!file.exists()) return;
            FileInputStream fileInputStream = new FileInputStream(file);
            ServletOutputStream servletOutputStream = response.getOutputStream();
            response.setContentType("image/jpeg");
            int len;
            byte[] arr = new byte[1024];
            while((len = fileInputStream.read(arr))!=-1){
                servletOutputStream.write(arr,0,len);
                servletOutputStream.flush();
            }
            servletOutputStream.close();
            fileInputStream.close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
