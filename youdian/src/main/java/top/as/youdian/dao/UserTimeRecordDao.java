package top.as.youdian.dao;

import top.as.youdian.entity.UserTimeRecord;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

/**
 * (UserTimeRecord)表数据库访问层
 *
 * @author makejava
 * @since 2021-11-01 16:46:49
 */
public interface UserTimeRecordDao {

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    UserTimeRecord queryById(String id);
    List<UserTimeRecord> queryByUIdAfterDay(String uId,Date day);
    UserTimeRecord queryByDay(Date day);

    /**
     * 查询指定行数据
     *
     * @param offset 查询起始位置
     * @param limit 查询条数
     * @return 对象列表
     */
    List<UserTimeRecord> queryAllByLimit(@Param("offset") int offset, @Param("limit") int limit);


    /**
     * 通过实体作为筛选条件查询
     *
     * @param userTimeRecord 实例对象
     * @return 对象列表
     */
    List<UserTimeRecord> queryAll(UserTimeRecord userTimeRecord);

    /**
     * 新增数据
     *
     * @param userTimeRecord 实例对象
     * @return 影响行数
     */
    int insert(UserTimeRecord userTimeRecord);

    /**
     * 修改数据
     *
     * @param userTimeRecord 实例对象
     * @return 影响行数
     */
    int update(UserTimeRecord userTimeRecord);

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 影响行数
     */
    int deleteById(String id);

}