package top.as.youdian.dao.word;

import top.as.youdian.entity.word.WordDef;

import java.util.List;

/**
 * (WordDef)表数据库访问层
 *
 * @author makejava
 * @since 2021-10-23 17:17:43
 */
public interface WordDefDao {

    /**
     * 通过ID查询单条数据
     *
     * @param wId 主键
     * @return 实例对象
     */
    List<WordDef> queryById(String wId);


    /**
     * 通过实体作为筛选条件查询
     *
     * @param wordDef 实例对象
     * @return 对象列表
     */
    List<WordDef> queryAll(WordDef wordDef);

}