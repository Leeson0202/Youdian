package top.as.youdian.service.impl;

import top.as.youdian.entity.UserRemind;
import top.as.youdian.dao.UserRemindDao;
import top.as.youdian.service.UserRemindService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * (UserRemind)表服务实现类
 *
 * @author makejava
 * @since 2021-10-30 13:27:43
 */
@Service("userRemindService")
public class UserRemindServiceImpl implements UserRemindService {
    @Resource
    private UserRemindDao userRemindDao;

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    @Override
    public UserRemind queryById(String id) {
        return this.userRemindDao.queryById(id);
    }

    @Override
    public List<UserRemind> queryAll(UserRemind userRemind) {
        return this.userRemindDao.queryAll(userRemind);
    }


    /**
     * 新增数据
     *
     * @param userRemind 实例对象
     * @return 实例对象
     */
    @Override
    public UserRemind insert(UserRemind userRemind) {
        this.userRemindDao.insert(userRemind);
        return userRemind;
    }

    /**
     * 修改数据
     *
     * @param userRemind 实例对象
     * @return 实例对象
     */
    @Override
    public UserRemind update(UserRemind userRemind) {
        this.userRemindDao.update(userRemind);
        return this.queryById(userRemind.getId());
    }

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 是否成功
     */
    @Override
    public boolean deleteById(String id) {
        return this.userRemindDao.deleteById(id) > 0;
    }
}