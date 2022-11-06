package top.as.youdian.dao.word;

import top.as.youdian.entity.word.Word;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * (Word)表数据库访问层
 *
 * @author makejava
 * @since 2021-10-20 21:31:59
 */
public interface WordDao {

    /**
     * 通过ID查询单条数据
     *
     * @param wId 主键
     * @return 实例对象
     */
    Word queryById(String wId);

    /**
     * 查询指定行数据
     *
     * @param offset 查询起始位置
     * @param limit  查询条数
     * @return 对象列表
     */
    List<Word> queryAllByLimit(@Param("offset") int offset, @Param("limit") int limit);


    /**
     * 通过实体作为筛选条件查询
     *
     * @param word 实例对象
     * @return 对象列表
     */
    List<Word> queryAll(@Param("word") Word word, @Param("num") int num);

    // 查询单词本中单词数量
    Integer queryNumByTag(String tag);

    /**
     * 新增数据
     *
     * @param word 实例对象
     * @return 影响行数
     */
    int insert(Word word);

    /**
     * 修改数据
     *
     * @param word 实例对象
     * @return 影响行数
     */
    int update(Word word);

    /**
     * 通过主键删除数据
     *
     * @param wId 主键
     * @return 影响行数
     */
    int deleteById(String wId);

}