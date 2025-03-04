package com.smartcontact.services.impl;

import java.io.IOException;


import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import com.cloudinary.*;
import com.cloudinary.utils.ObjectUtils;
import com.smartcontact.helpers.AppConstants;
import com.smartcontact.services.ImageService;

@Service
public class ImageServiceImpl implements ImageService{

    

    private Cloudinary cloudinary;
    public ImageServiceImpl(Cloudinary cloudinary) {
        this.cloudinary = cloudinary;
    }

    @Override
    public String uploadImage(MultipartFile contactImage, String filename) {
        //upload image on server of cloudinary

        

        try {
            byte[] data = new byte[contactImage.getInputStream().available()];
            contactImage.getInputStream().read(data);
            cloudinary.uploader().upload(data, ObjectUtils.asMap(
                "public_id",filename));

            return this.getUrlFromPublicId(filename);

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

        
    }

    @Override
    public String getUrlFromPublicId(String publicid) {
        
        return cloudinary.url().transformation(
            new Transformation<>()
            .width(AppConstants.CONTACT_IMAGE_WIDTH)
            .height(AppConstants.CONTACT_IMAGE_HEIGHT)
            .crop(AppConstants.CONTACT_IMAGE_CROP)
        
        ).generate(publicid);
    }


    
}
