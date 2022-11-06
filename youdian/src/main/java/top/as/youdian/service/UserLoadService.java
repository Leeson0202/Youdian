package top.as.youdian.service;

import top.as.youdian.entity.UserLoad;
import java.util.List;

/**
 * (UserLoad)表服务接口
 *
 * @author makejava
 * @since 2021-10-28 16:36:36
 */
public interface UserLoadService {
    // 匹配查找
    List<UserLoad> queryAll(UserLoad userLoad);



    /**
     * 新增数据
     *
     * @param userLoad 实例对象
     * @return 实例对象
     */
    UserLoad insert(UserLoad userLoad);


    /**
     * 通过主键删除数据
     *
     * @param devId 主键
     * @return 是否成功
     */
    boolean deleteById(String devId);

}