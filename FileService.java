package com.smartcontact.services;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.smartcontact.entities.Emails;
import com.smartcontact.entities.FileEntity;
import com.smartcontact.repositories.FileRepository;

@Service
public class FileService {

    @Autowired
    private FileRepository fileRepository;

    public List<FileEntity> saveFiles(MultipartFile[] files) throws IOException{
        List<FileEntity> savedFiles = new ArrayList<>();
        for (MultipartFile file : files) {
            FileEntity fileEntity = new FileEntity();
            fileEntity.setFileName(file.getOriginalFilename());
            fileEntity.setFileType(file.getContentType());
            fileEntity.setData(file.getBytes());
            
            savedFiles.add(fileRepository.save(fileEntity));
        }
        return savedFiles;
    }


    public void saveEmail(Emails email){
        FileEntity fileEntity = new FileEntity();
        fileEntity.setEmail(email);
    }
}   
