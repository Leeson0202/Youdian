package top.as.youdian.service;

import top.as.youdian.entity.UserRemind;

import java.util.List;

/**
 * (UserRemind)表服务接口
 *
 * @author makejava
 * @since 2021-10-30 13:27:43
 */
public interface UserRemindService {

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    UserRemind queryById(String id);

    // 条件查询
    List<UserRemind> queryAll(UserRemind userRemind);


    /**
     * 新增数据
     *
     * @param userRemind 实例对象
     * @return 实例对象
     */
    UserRemind insert(UserRemind userRemind);

    /**
     * 修改数据
     *
     * @param userRemind 实例对象
     * @return 实例对象
     */
    UserRemind update(UserRemind userRemind);

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 是否成功
     */
    boolean deleteById(String id);

}