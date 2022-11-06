package top.as.youdian.dao;

import top.as.youdian.entity.userWord.UserWord;
import org.apache.ibatis.annotations.Param;
import java.util.List;

/**
 * (UserWord)表数据库访问层
 *
 * @author makejava
 * @since 2021-10-31 17:58:43
 */
public interface UserWordDao {

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    UserWord queryById(String id);

    /**
     * 查询指定行数据
     *
     * @param offset 查询起始位置
     * @param limit 查询条数
     * @return 对象列表
     */
    List<UserWord> queryAllByLimit(@Param("offset") int offset, @Param("limit") int limit);


    /**
     * 通过实体作为筛选条件查询
     *
     * @param userWord 实例对象
     * @return 对象列表
     */
    List<UserWord> queryAll(UserWord userWord);

    /**
     * 新增数据
     *
     * @param userWord 实例对象
     * @return 影响行数
     */
    int insert(UserWord userWord);

    /**
     * 修改数据
     *
     * @param userWord 实例对象
     * @return 影响行数
     */
    int update(UserWord userWord);

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 影响行数
     */
    int deleteById(String id);

}