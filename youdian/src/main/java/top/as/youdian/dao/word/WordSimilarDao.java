package top.as.youdian.dao.word;

import top.as.youdian.entity.word.WordSimilar;

import java.util.List;

/**
 * (WordSimilar)表数据库访问层
 *
 * @author makejava
 * @since 2021-10-24 12:44:34
 */
public interface WordSimilarDao {

    /**
     * 通过ID查询单条数据
     *
     * @param wId 主键
     * @return 实例对象
     */
    List<WordSimilar> queryById(String wId);

    /**
     * 通过实体作为筛选条件查询
     *
     * @param wordSimilar 实例对象
     * @return 对象列表
     */
    List<WordSimilar> queryAll(WordSimilar wordSimilar);


}