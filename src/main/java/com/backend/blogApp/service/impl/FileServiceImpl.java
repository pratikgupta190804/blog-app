package com.backend.blogApp.service.impl;

import com.backend.blogApp.service.FileService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class FileServiceImpl implements FileService {
    @Override
    public String uploadImage(MultipartFile file, String path) throws IOException {
        //file name
        String name = file.getOriginalFilename();
        //path create
        //ex - abc.png

        String randomUUID = UUID.randomUUID().toString();
        String fileName = randomUUID.concat(name.substring(name.lastIndexOf(".")));

        // full path
        String filePath = path + File.separator + fileName;

        //check path
        File f = new File(path);
        if(!f.exists()){
            f.mkdir();
        }

        //copy content
        Files.copy(file.getInputStream(), Paths.get(filePath));
        return fileName;
    }

    @Override
    public InputStream getResource(String path, String fileName) throws FileNotFoundException {
        String filePath = path + File.separator + fileName;
        InputStream is = new FileInputStream(filePath);
        return is;
    }
}
