package top.as.youdian.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.as.youdian.entity.user.UserDetail;
import top.as.youdian.entity.UserRemind;
import top.as.youdian.entity.UserTimeRecord;
import top.as.youdian.entity.userWord.UserWord;

@RestController
@RequestMapping("Model")
public class ModelEX {
    // UserDetail
    @GetMapping("/UserDetail")
    public UserDetail User() {
        return new UserDetail();
    }

    // UserWord
    @GetMapping("/UserWord")
    public UserWord UserWord() {
        return new UserWord();
    }
    // UserRemind
    @GetMapping("/UserRemind")
    public UserRemind UserRemind() {
        return new UserRemind();
    }

    // UserTimeRecord
    @GetMapping("/UserTimeRecord")
    public UserTimeRecord UserTimeRecord() {
        return new UserTimeRecord();
    }

}
