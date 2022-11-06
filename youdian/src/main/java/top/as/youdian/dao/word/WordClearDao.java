package top.as.youdian.dao.word;

import top.as.youdian.entity.word.WordClear;
import top.as.youdian.entity.word.WordWIdClear;

import java.util.List;

/**
 * (WordClear)表数据库访问层
 *
 * @author makejava
 * @since 2021-10-23 17:17:25
 */
public interface WordClearDao {

    /**
     * 通过ID查询单条数据
     *
     * @param wId 主键
     * @return 实例对象
     */
    List<WordClear> queryById(String wId);


    /**
     * 通过实体作为筛选条件查询
     *
     * @param wordClear 实例对象
     * @return 对象列表
     */
    List<WordWIdClear> queryAll(WordWIdClear wordClear);

}