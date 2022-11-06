package top.as.youdian.service;

import top.as.youdian.entity.userWord.UserWord;
import java.util.List;

/**
 * (UserWord)表服务接口
 *
 * @author makejava
 * @since 2021-10-31 17:58:43
 */
public interface UserWordService {

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    UserWord queryById(String id);

    /**
     * 查询多条数据
     *
     * @param offset 查询起始位置
     * @param limit 查询条数
     * @return 对象列表
     */
    List<UserWord> queryAllByLimit(int offset, int limit);
    List<UserWord> queryAll(UserWord userWord);

    /**
     * 新增数据
     *
     * @param userWord 实例对象
     * @return 实例对象
     */
    UserWord insert(UserWord userWord);

    /**
     * 修改数据
     *
     * @param userWord 实例对象
     * @return 实例对象
     */
    UserWord update(UserWord userWord);

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 是否成功
     */
    boolean deleteById(String id);

}