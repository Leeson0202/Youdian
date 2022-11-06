package top.as.youdian.controller.tools;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.as.youdian.tools.AssertUtil;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Date;

@RestController
public class Downloads {

    @GetMapping("download/{path1}/{path2}/{fileName}")
    @ApiOperation(value = "下载文件")
    @ApiImplicitParam(name = "fileName", value = "文件URI", required = true, dataTypeClass = String.class, paramType = "query")
    public ResponseEntity<FileSystemResource> getFile3(@PathVariable("path1") String path1, @PathVariable("path2") String path2, @PathVariable("fileName") String fileName) throws FileNotFoundException {
        File file = new File(AssertUtil.getRootPath() + path1 + "/" + path2 + "/", fileName);
        if (file.exists()) {
            return export(file);
        }
        System.out.println(file);
        return null;
    }

    @GetMapping("download/{path1}/{path2}/{path3}/{fileName}")
    @ApiOperation(value = "下载文件")
    @ApiImplicitParam(name = "fileName", value = "文件URI", required = true, dataTypeClass = String.class, paramType = "query")
    public ResponseEntity<FileSystemResource> getFile4(@PathVariable("path1") String path1, @PathVariable("path2") String path2, @PathVariable("path3") String path3, @PathVariable("fileName") String fileName) throws FileNotFoundException {
        File file = new File(AssertUtil.getRootPath() + path1 + "/" + path2 + "/" + path3 + "/", fileName);
        if (file.exists()) {
            return export(file);
        }
        System.out.println(file);
        return null;
    }


    public ResponseEntity<FileSystemResource> export(File file) {
        if (file == null) {
            return null;
        }
        HttpHeaders headers = new HttpHeaders();
        headers.add("Cache-Control", "no-cache, no-store, must-revalidate");
        headers.add("Content-Disposition", "attachment; filename=" + file.getName());
        headers.add("Pragma", "no-cache");
        headers.add("Expires", "0");
        headers.add("Last-Modified", new Date().toString());
        headers.add("ETag", String.valueOf(System.currentTimeMillis()));
        return ResponseEntity.ok().headers(headers).contentLength(file.length()).contentType(MediaType.parseMediaType("application/octet-stream")).body(new FileSystemResource(file));
    }
}
