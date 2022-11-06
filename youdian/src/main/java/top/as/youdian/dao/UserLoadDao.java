package top.as.youdian.dao;

import top.as.youdian.entity.UserLoad;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * (UserLoad)表数据库访问层
 *
 * @author makejava
 * @since 2021-10-28 16:36:36
 */
public interface UserLoadDao {


    /**
     * 通过实体作为筛选条件查询
     *
     * @param userLoad 实例对象
     * @return 对象列表
     */
    List<UserLoad> queryAll(UserLoad userLoad);

    /**
     * 新增数据
     *
     * @param userLoad 实例对象
     * @return 影响行数
     */
    int insert(UserLoad userLoad);


    /**
     * 通过主键删除数据
     *
     * @param devId 主键
     * @return 影响行数
     */
    int deleteById(String devId);

}