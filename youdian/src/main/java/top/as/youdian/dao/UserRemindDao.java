package top.as.youdian.dao;

import top.as.youdian.entity.UserRemind;
import org.apache.ibatis.annotations.Param;
import java.util.List;

/**
 * (UserRemind)表数据库访问层
 *
 * @author makejava
 * @since 2021-10-30 13:27:43
 */
public interface UserRemindDao {

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    UserRemind queryById(String id);


    /**
     * 通过实体作为筛选条件查询
     *
     * @param userRemind 实例对象
     * @return 对象列表
     */
    List<UserRemind> queryAll(UserRemind userRemind);

    /**
     * 新增数据
     *
     * @param userRemind 实例对象
     * @return 影响行数
     */
    int insert(UserRemind userRemind);

    /**
     * 修改数据
     *
     * @param userRemind 实例对象
     * @return 影响行数
     */
    int update(UserRemind userRemind);

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 影响行数
     */
    int deleteById(String id);

}