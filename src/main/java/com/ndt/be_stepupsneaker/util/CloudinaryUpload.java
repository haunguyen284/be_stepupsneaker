package com.ndt.be_stepupsneaker.util;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.ndt.be_stepupsneaker.infrastructure.exception.ApiException;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Map;

@Service
@Log4j2
public class CloudinaryUpload {
    @Autowired
    private Cloudinary cloudinary;

    public String upload(String imageBase64){
        String imageCloudinary = "";
        if (imageBase64.isEmpty()) {
            return imageCloudinary;
        }
        try {
            Map result = cloudinary.uploader().upload(imageBase64, ObjectUtils.asMap("phash", true));
            imageCloudinary = (String) result.get("secure_url");
        } catch (IOException e) {
            log.error(e.getMessage());
        }
        return imageCloudinary;
    }
}
