package com.lycmoons.util;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;


public class Convertor {

    // 以空格作为分隔符对 photo_url 进行分割
    public static List<String> convertPhotoUrl(String photoUrl){
        if (photoUrl == null || photoUrl.trim().isEmpty()) {
            return List.of(); // 如果为空或空字符串，返回空列表
        }
        return List.of(photoUrl.split("\\+\\+\\+"));
    }

    // Date 类型转为使用标准时间字符串
    public static String convertDate(Date date){
        if (date == null) {
            return ""; // 如果日期为空，返回空字符串
        }
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        return dateFormat.format(date);
    }

    // 将 String 类型数据转化为 Date 类型数据
    public static Date convertDateString(String date) throws ParseException {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        return dateFormat.parse(date);
    }

    // 拼接图片的 url
    public static String mergePhotoUrl(String serverIp, String port, String directory, String fileName){
        if (serverIp == null || directory == null || port == null) {
            return null;
        }
        else {
            return "http://" + serverIp + ":" + port + "/" + directory + "/" + fileName;
        }
    }

    // 组合多个图片的 url
    public static String mergePhotoUrls(List<String> photoUrls){
        if (photoUrls == null || photoUrls.isEmpty()) {
            return null;
        }
        StringBuilder stringBuilder = new StringBuilder(photoUrls.get(0));
        for (int i = 1; i < photoUrls.size(); i++) {
            stringBuilder.append("+++").append(photoUrls.get(i));
        }
        return stringBuilder.toString();
    }
}
