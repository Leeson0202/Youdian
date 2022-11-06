package top.as.youdian.tools;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;
import java.util.Date;

// 期望返回时间转换为Long时间戳如
//    /**
//     * 注解，将Date时间转换成Long，单位为毫秒
//     */
//    @JsonSerialize(using = DateToLongSerializer.class)
//    private Date createTime;
//    @JsonSerialize(using = DateToLongSerializer.class)
//    private Date updateTime;
// https://blog.csdn.net/xusha_scnc/article/details/82251534?ops_request_misc=%257B%2522request%255Fid%2522%253A%2522164310735616780271976974%2522%252C%2522scm%2522%253A%252220140713.130102334.pc%255Fall.%2522%257D&request_id=164310735616780271976974&biz_id=0&utm_medium=distribute.pc_search_result.none-task-blog-2~all~first_rank_ecpm_v1~rank_v31_ecpm-2-82251534.pc_search_result_control_group&utm_term=springBoot+Date%E8%BF%94%E5%9B%9E%E6%97%B6%E9%97%B4%E6%88%B3&spm=1018.2226.3001.4187
public class DateToLongSerializer extends JsonSerializer<Date> {
    @Override
    public void serialize(Date date, JsonGenerator jsonGenerator,
                          SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.writeNumber(date.getTime());
    }

}
