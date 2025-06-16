package com.lycmoons.service.impl;
import com.lycmoons.service.PhotoService;
import com.lycmoons.util.Convertor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class PhotoServiceImpl implements PhotoService {
    @Value("${spring.upload-path}")
    String uploadPath;

    @Value("${spring.upload-directory}")
    String uploadDirectory;

    @Value("${spring.serverip}")
    String serverIp;

    @Override
    public String storePhoto(MultipartFile[] files) throws IOException {
        List<String> photoUrls = new ArrayList<>();
        for (MultipartFile file : files) {
            String fileName = file.getOriginalFilename();
            if(fileName!=null){
                String suffix = fileName.substring(fileName.lastIndexOf(".")); // 获取后缀名
                String newFileName = UUID.randomUUID().toString() + suffix;
                File dest = new File(uploadPath, newFileName);
                file.transferTo(dest); // 保存文件
                photoUrls.add(Convertor.mergePhotoUrl(serverIp,"8080", uploadDirectory, newFileName));
            }
        }
        return Convertor.mergePhotoUrls(photoUrls);
    }

    @Override
    public String deletePhoto(String photoUrl) {
        String prefix = "http://localhost:8080/" + uploadDirectory + "/";
        if (!photoUrl.startsWith(prefix)) {
            return "图片URL无效";
        }

        String fileName = photoUrl.substring(prefix.length());
        String filePath = uploadPath + "/" + fileName;
        File file = new File(filePath);
        if (file.exists() && file.isFile()) {
            boolean deleted = file.delete();
            if (deleted) {
                return null;
            } else {
                return "图片删除失败";
            }
        } else {
            return "图片不存在";
        }
    }
}
