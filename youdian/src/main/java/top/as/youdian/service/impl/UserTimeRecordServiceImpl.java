package top.as.youdian.service.impl;

import top.as.youdian.entity.UserTimeRecord;
import top.as.youdian.dao.UserTimeRecordDao;
import top.as.youdian.service.UserTimeRecordService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * (UserTimeRecord)表服务实现类
 *
 * @author makejava
 * @since 2021-11-01 16:46:49
 */
@Service("userTimeRecordService")
public class UserTimeRecordServiceImpl implements UserTimeRecordService {
    @Resource
    private UserTimeRecordDao userTimeRecordDao;

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    @Override
    public UserTimeRecord queryById(String id) {
        return this.userTimeRecordDao.queryById(id);
    }

    @Override
    public List<UserTimeRecord> queryByUIdAfterDay(String uId,Date day) {
        return this.userTimeRecordDao.queryByUIdAfterDay(uId,day);
    }

    @Override
    public UserTimeRecord queryByDay(Date day) {
        return this.userTimeRecordDao.queryByDay(day);
    }

    // 查询所有
    @Override
    public List<UserTimeRecord> queryAll(UserTimeRecord userTimeRecord) {
        return this.userTimeRecordDao.queryAll(userTimeRecord);
    }

    /**
     * 查询多条数据
     *
     * @param offset 查询起始位置
     * @param limit  查询条数
     * @return 对象列表
     */
    @Override
    public List<UserTimeRecord> queryAllByLimit(int offset, int limit) {
        return this.userTimeRecordDao.queryAllByLimit(offset, limit);
    }

    /**
     * 新增数据
     *
     * @param userTimeRecord 实例对象
     * @return 实例对象
     */
    @Override
    public UserTimeRecord insert(UserTimeRecord userTimeRecord) {
        this.userTimeRecordDao.insert(userTimeRecord);
        return userTimeRecord;
    }

    /**
     * 修改数据
     *
     * @param userTimeRecord 实例对象
     * @return 实例对象
     */
    @Override
    public UserTimeRecord update(UserTimeRecord userTimeRecord) {
        this.userTimeRecordDao.update(userTimeRecord);
        return this.queryById(userTimeRecord.getId());
    }

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 是否成功
     */
    @Override
    public boolean deleteById(String id) {
        return this.userTimeRecordDao.deleteById(id) > 0;
    }
}