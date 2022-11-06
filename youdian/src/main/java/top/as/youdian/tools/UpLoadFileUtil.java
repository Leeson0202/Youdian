package top.as.youdian.tools;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.UUID;

@Component
public class UpLoadFileUtil {


    // 上传图片 返回相对地址   tempPath :  user/files/img/
    public static String uploadImg(MultipartFile img, String tempPath) throws Exception {
        // 1. 判断文件是否为空
        AssertUtil.isTrue(img.isEmpty(), 401);
        // 2. 判断文件格式
        String oldfileName = img.getOriginalFilename();
        // 3. 判断格式是否正确
//        AssertUtil.isTrue(!(fileName.endsWith(".jpg") || fileName.endsWith(".png")), "文件格式错误");
        // 4. 修改文件名字（防止相同）
        String fileName = UpLoadFileUtil.transFileName(oldfileName);
        // 5. 获取相对位置地址
        String path = UpLoadFileUtil.getPath(tempPath);
        String filePath = path + fileName;

        // 5. 判断文件夹是否存在
        File file1 = new File(path);
        if (!file1.isDirectory()) {
            // 创建该文件夹
            file1.mkdir();
        }

        // 6. 保存文件
        img.transferTo(new File(filePath));

        // 7. 拼接为 url
        //   String hUrl = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + "/uploadFile/" + format + newName;
        String hUrl = "/download/" + tempPath + fileName;

        return hUrl;
    }

    // 上传文件 返回相对地址  tempPath :  user/files/img/
    public static String uploadFile(MultipartFile file, String tempPath) throws Exception {
        // 1. 判断文件是否为空
        AssertUtil.isTrue(file.isEmpty(), 401);
        // 2. 判断文件格式
        String oldfileName = file.getOriginalFilename();
        // 3. 判断格式是否正确
//        AssertUtil.isTrue(!(fileName.endsWith(".jpg") || fileName.endsWith(".png")), "文件格式错误");
        // 4. 修改文件名字（防止相同）
        String fileName = UpLoadFileUtil.transFileName(oldfileName);
        // 5. 获取相对位置地址
        String path = UpLoadFileUtil.getPath(tempPath);
        String filePath = path + fileName;

        // 8. 保存文件
        file.transferTo(new File(filePath));

        // 9. 拼接为 url
        //   String hUrl = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + "/uploadFile/" + format + newName;
        String hUrl = "/download/" + tempPath + fileName;

        return hUrl;
    }

    private static String getPath(String tempPath) throws FileNotFoundException {
        String path = AssertUtil.getRootPath() + tempPath;
        //  判断文件夹是否存在
        File file1 = new File(path);
        if (!file1.isDirectory() || file1.getParentFile().isDirectory()) {
            file1.getParentFile().mkdir();
            // 创建该文件夹
            file1.mkdir();
        }
        return path;
    }

    private static String transFileName(String oldFileName) {
        return UUID.randomUUID().toString() + oldFileName.substring(oldFileName.indexOf("."));
    }
}
