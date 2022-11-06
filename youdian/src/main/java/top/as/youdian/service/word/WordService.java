package top.as.youdian.service.word;

import top.as.youdian.entity.word.Word;

import java.util.List;

/**
 * (Word)表服务接口
 *
 * @author makejava
 * @since 2021-10-20 21:31:59
 */
public interface WordService {

    /**
     * 通过实体作为筛选条件查询
     *
     * @param word 实例对象
     * @return 对象列表
     */
    List<Word> queryAll(Word word, int num);

    // 查询单词本中单词数量
    Integer queryNumByTag(String tag);

    /**
     * 通过ID查询单条数据
     *
     * @param wId 主键
     * @return 实例对象
     */
    Word queryById(String wId);

    /**
     * 查询多条数据
     *
     * @param offset 查询起始位置
     * @param limit  查询条数
     * @return 对象列表
     */
    List<Word> queryAllByLimit(int offset, int limit);

    /**
     * 新增数据
     *
     * @param word 实例对象
     * @return 实例对象
     */
    Word insert(Word word);

    /**
     * 修改数据
     *
     * @param word 实例对象
     * @return 实例对象
     */
    Word update(Word word);

    /**
     * 通过主键删除数据
     *
     * @param wId 主键
     * @return 是否成功
     */
    boolean deleteById(String wId);

    List<Word> getElements(List<Word> words);

    Word getElements(String wId);


}