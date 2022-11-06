package top.as.youdian.service.word;

import top.as.youdian.entity.word.WordClear;
import top.as.youdian.entity.word.WordWIdClear;

import java.util.List;

/**
 * (WordClear)表服务接口
 *
 * @author makejava
 * @since 2021-10-24 13:40:06
 */
public interface WordClearService {

    /**
     * 通过ID查询单条数据
     *
     * @param wId 主键
     * @return 实例对象
     */
    List<WordClear> queryById(String wId);

    List<WordWIdClear> queryAll(WordWIdClear wordClear);


}