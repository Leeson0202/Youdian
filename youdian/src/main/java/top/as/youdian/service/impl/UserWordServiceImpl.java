package top.as.youdian.service.impl;

import top.as.youdian.entity.userWord.UserWord;
import top.as.youdian.dao.UserWordDao;
import top.as.youdian.service.UserWordService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * (UserWord)表服务实现类
 *
 * @author makejava
 * @since 2021-10-31 17:58:43
 */
@Service("userWordService")
public class UserWordServiceImpl implements UserWordService {
    @Resource
    private UserWordDao userWordDao;

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    @Override
    public UserWord queryById(String id) {
        return this.userWordDao.queryById(id);
    }

    /**
     * 查询多条数据
     *
     * @param offset 查询起始位置
     * @param limit  查询条数
     * @return 对象列表
     */
    @Override
    public List<UserWord> queryAllByLimit(int offset, int limit) {
        return this.userWordDao.queryAllByLimit(offset, limit);
    }

    @Override
    public List<UserWord> queryAll(UserWord userWord) {
        return this.userWordDao.queryAll(userWord);
    }

    /**
     * 新增数据
     *
     * @param userWord 实例对象
     * @return 实例对象
     */
    @Override
    public UserWord insert(UserWord userWord) {
        this.userWordDao.insert(userWord);
        return userWord;
    }

    /**
     * 修改数据
     *
     * @param userWord 实例对象
     * @return 实例对象
     */
    @Override
    public UserWord update(UserWord userWord) {
        this.userWordDao.update(userWord);
        return this.queryById(userWord.getId());
    }

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 是否成功
     */
    @Override
    public boolean deleteById(String id) {
        return this.userWordDao.deleteById(id) > 0;
    }
}