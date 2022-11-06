package top.as.youdian.service;

import top.as.youdian.entity.UserTimeRecord;

import javax.xml.crypto.Data;
import java.util.Date;
import java.util.List;

/**
 * (UserTimeRecord)表服务接口
 *
 * @author makejava
 * @since 2021-11-01 16:46:49
 */
public interface UserTimeRecordService {

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    UserTimeRecord queryById(String id);
    List<UserTimeRecord> queryByUIdAfterDay(String uId,Date day);
    UserTimeRecord queryByDay(Date day);
    List<UserTimeRecord> queryAll(UserTimeRecord userTimeRecord);

    /**
     * 查询多条数据
     *
     * @param offset 查询起始位置
     * @param limit 查询条数
     * @return 对象列表
     */
    List<UserTimeRecord> queryAllByLimit(int offset, int limit);

    /**
     * 新增数据
     *
     * @param userTimeRecord 实例对象
     * @return 实例对象
     */
    UserTimeRecord insert(UserTimeRecord userTimeRecord);

    /**
     * 修改数据
     *
     * @param userTimeRecord 实例对象
     * @return 实例对象
     */
    UserTimeRecord update(UserTimeRecord userTimeRecord);

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 是否成功
     */
    boolean deleteById(String id);

}