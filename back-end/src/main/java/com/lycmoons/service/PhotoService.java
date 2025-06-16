package com.lycmoons.service;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;

public interface PhotoService {
    String storePhoto(MultipartFile[] files) throws IOException;
    String deletePhoto(String photoUrl);
}
