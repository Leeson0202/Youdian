package top.as.youdian.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import top.as.youdian.tools.DateToLongSerializer;

import java.util.Date;
import java.io.Serializable;

/**
 * (UserTimeRecord)实体类
 *
 * @author makejava
 * @since 2021-11-01 16:46:49
 */
@Data
@ApiModel("UserTimeRecord 用户背单词记录实体类")
public class UserTimeRecord implements Serializable {
    private static final long serialVersionUID = 157704316655175504L;
    /**
    * id
    */
    @ApiModelProperty("id")
    private String id;
    /**
    * 用户id
    */@ApiModelProperty("用户id")
    @JsonProperty("uId")
    private String uId;
    /**
    * 日期
    */@ApiModelProperty("日期")
    @JsonSerialize(using = DateToLongSerializer.class)
    private Date day;
    /**
    * 0-2
    */@ApiModelProperty("0-2")
    private Integer a;
    /**
    * 2-4
    */@ApiModelProperty("2-4")
    private Integer b;
    /**
    * 4-6
    */@ApiModelProperty("4-6")
    private Integer c;
    /**
    * 6-8
    */@ApiModelProperty("6-8")
    private Integer d;
    /**
    * 8-10
    */@ApiModelProperty("8-10")
    private Integer e;
    /**
    * 10-12
    */@ApiModelProperty("10-12")
    private Integer f;
    /**
    * 12-14
    */@ApiModelProperty("12-14")
    private Integer g;
    /**
    * 14-16
    */@ApiModelProperty("14-16")
    private Integer h;
    /**
    * 16-18
    */@ApiModelProperty("16-18")
    private Integer i;
    /**
    * 18-20
    */@ApiModelProperty("18-20")
    private Integer j;
    /**
    * 20-22
    */@ApiModelProperty("20-22")
    private Integer k;
    /**
    * 22-24
    */@ApiModelProperty("22-24")
    private Integer l;
    /**
    * 当日总时间
    */@ApiModelProperty("当日总时间")
    @JsonProperty("sumTime")
    private Integer sumTime;
    /**
    * sum
    */@ApiModelProperty("当日总的背单词数量")
    private Integer sum;



}