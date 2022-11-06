package top.as.youdian.controller;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import top.as.youdian.tools.ValidateImageCodeUtils;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.awt.image.BufferedImage;

@RestController
public class Home {

    @GetMapping("getCodeImage")
    @ApiOperation(value = "获取验证码")
    @ApiImplicitParam()
    public void getImage(HttpSession session, HttpServletResponse response) throws Exception {
        //生产验证码
        String securityCode = ValidateImageCodeUtils.getSecurityCode();
        BufferedImage image = ValidateImageCodeUtils.createImage(securityCode);
        session.setAttribute("code", securityCode);  // 存入session作用域
        // 响应图片
        ServletOutputStream os = response.getOutputStream();
        ImageIO.write(image, "png", os);
    }


}
