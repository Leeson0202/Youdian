package top.as.youdian.dao.word;

import top.as.youdian.entity.word.WordSentences;

import java.util.List;

/**
 * (WordSentences)表数据库访问层
 *
 * @author makejava
 * @since 2021-10-23 17:18:12
 */
public interface WordSentencesDao {

    /**
     * 通过ID查询单条数据
     *
     * @param wId 主键
     * @return 实例对象
     */
    List<WordSentences> queryById(String wId);


    /**
     * 通过实体作为筛选条件查询
     *
     * @param wordSentences 实例对象
     * @return 对象列表
     */
    List<WordSentences> queryAll(WordSentences wordSentences);

}