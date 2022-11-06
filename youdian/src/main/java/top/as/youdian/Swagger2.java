package top.as.youdian;


import java.util.*;
import java.util.ArrayList;

import io.swagger.models.parameters.Parameter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;
import top.as.youdian.entity.word.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.net.URL;

import static springfox.documentation.builders.PathSelectors.regex;

@Configuration
@EnableSwagger2
public class Swagger2 {

    @Bean
    public Docket webApiConfig() {
        return new Docket(DocumentationType.SWAGGER_2)
                .groupName("webApi")
                .apiInfo(webApiInfo())
                .ignoredParameterTypes(HttpSession.class, HttpServletRequest.class, HttpServletResponse.class,
                        File.class, FileSystemResource.class, InputStream.class,
//                        WordAudio.class, WordClear.class, WordDef.class, WordPhrase.class, WordSentences.class,WordSimilar.class,
                        OutputStream.class, URI.class, URL.class)
                .select()
                .build();

    }

    private ApiInfo webApiInfo() {
        return new ApiInfoBuilder()
                .title("----- 悠点单词 API文档 -----  *只可填写有  Description 的参数")
                .description("本文档描述了 悠点 微服务接口定义,  只可填写有  Description 的参数")
                .version("1.0")
                .contact(new Contact("Leeson", "http://youdian.asedrfa.top", "asedrfa@163.com"))
                .build();
    }

//    private List<Parameter> getParamerList() {
//        ParameterBuilder tokenPar = new ParameterBuilder();
//        List<Parameter> pars = new ArrayList<>();
//        tokenPar.name("token").description("令牌").modelRef(new ModelRef("String"))
//                .parameterType("header")
//
//    }

}
