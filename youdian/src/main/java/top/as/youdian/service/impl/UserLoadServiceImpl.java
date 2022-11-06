package top.as.youdian.service.impl;

import top.as.youdian.entity.UserLoad;
import top.as.youdian.dao.UserLoadDao;
import top.as.youdian.service.UserLoadService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * (UserLoad)表服务实现类
 *
 * @author makejava
 * @since 2021-10-28 16:36:36
 */
@Service("userLoadService")
public class UserLoadServiceImpl implements UserLoadService {
    @Resource
    private UserLoadDao userLoadDao;


    @Override
    public List<UserLoad> queryAll(UserLoad userLoad) {
        return this.userLoadDao.queryAll(userLoad);
    }

    /**
     * 新增数据
     *
     * @param userLoad 实例对象
     * @return 实例对象
     */
    @Override
    public UserLoad insert(UserLoad userLoad) {
        this.userLoadDao.insert(userLoad);
        return userLoad;
    }


    /**
     * 通过主键删除数据
     *
     * @param devId 主键
     * @return 是否成功
     */
    @Override
    public boolean deleteById(String devId) {
        return this.userLoadDao.deleteById(devId) > 0;
    }
}