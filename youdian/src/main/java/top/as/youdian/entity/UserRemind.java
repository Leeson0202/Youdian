package top.as.youdian.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * (UserRemind)实体类
 *
 * @author makejava
 * @since 2021-10-30 13:27:43
 */
@Data
@ApiModel("UserRemind 用户提醒设置 实体类")
public class UserRemind implements Serializable {
    private static final long serialVersionUID = 959094748800348613L;
    /**
    * id
    */
    @ApiModelProperty("记录id")
    private String id;
    /**
    * 用户id
    */@ApiModelProperty("用户id")
    @JsonProperty("uId")
    private String uId;
    /**
    * 单词本
    */@ApiModelProperty("单词本类型：高中、ETC4")
    private String tag;
    /**
    * 手机提醒
    */@ApiModelProperty("手机提醒0/1")
    @JsonProperty("phoRem")
    private Integer phoRem;
    /**
    * 手表提醒
    */@ApiModelProperty("手表提醒0/1")
    @JsonProperty("watRem")
    private Integer watRem;
    /**
    * 天数
    */@ApiModelProperty("天数")
    @JsonProperty("dayTime")
    private Integer dayTime;
    /**
    * 多少个list
    */@ApiModelProperty("多少个list")
    private Integer list;
    /**
    * 每个list单词数量
    */@ApiModelProperty("每个list单词数量")
    @JsonProperty("numOfList")
    private Integer numOfList;
    /**
    * 循环方式
    */@ApiModelProperty("循环方式")
    @JsonProperty("circWay")
    private Integer circWay;


}