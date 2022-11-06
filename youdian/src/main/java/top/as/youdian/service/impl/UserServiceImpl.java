package top.as.youdian.service.impl;

import top.as.youdian.dao.UserLoadDao;
import top.as.youdian.entity.UserRemind;
import top.as.youdian.entity.user.User;
import top.as.youdian.dao.UserDao;
import top.as.youdian.entity.UserLoad;
import top.as.youdian.exception.MyException;
import top.as.youdian.service.UserRemindService;
import top.as.youdian.service.UserService;
import org.springframework.stereotype.Service;
import top.as.youdian.tools.AssertUtil;
import top.as.youdian.tools.token.TokenUtils;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * (User)表服务实现类
 *
 * @author makejava
 * @since 2021-10-27 23:10:51
 */
@Service("userService")
public class UserServiceImpl implements UserService {
    @Resource
    private UserDao userDao;
    @Resource
    private UserLoadDao userLoadDao;
    @Resource
    private UserRemindService userRemindService;

    /**
     * 通过ID查询单条数据
     *
     * @param uId 主键
     * @return 实例对象
     */
    @Override
    public User queryById(String uId) {
        return this.userDao.queryById(uId);
    }

    // 查询单个 tel
    @Override
    public User queryByTel(String tel) {
        return this.userDao.queryByTel(tel);
    }

    /**
     * 查询多条数据
     *
     * @param offset 查询起始位置
     * @param limit  查询条数
     * @return 对象列表
     */
    @Override
    public List<User> queryAllByLimit(int offset, int limit) {
        return this.userDao.queryAllByLimit(offset, limit);
    }

    /**
     * 新增数据
     *
     * @param user 实例对象
     * @return 实例对象
     */
    @Override
    public User insert(User user) {
        this.userDao.insert(user);
        Map<String, Object> params = new HashMap<>();
        params.put("tag", "0");
        params.put("uId", user.getUId());
        this.userDao.callUserWord("0", user.getUId());
        return user;
    }

    /**
     * 修改数据
     *
     * @param user 实例对象
     * @return 实例对象
     */
    @Override
    public User update(User user) {
        this.userDao.update(user);
        return this.queryById(user.getUId());
    }

    /**
     * 通过主键删除数据
     *
     * @param uId 主键
     * @return 是否成功
     */
    @Override
    public boolean deleteById(String uId) {
        return this.userDao.deleteById(uId) > 0;
    }

    @Override
    public String load(String tel, String pwd, String devId) {
        return null;
    }

    @Override
    public String isLoad(String token) {

        String token1 = TokenUtils.checkToken(token);  //翻译
        if ("S".equals(token1) || "F".equals(token1) || "G".equals(token1)) {
            // 删除token
            UserLoad userLoad = new UserLoad();
            userLoad.setToken(token);
            List<UserLoad> userLoads = userLoadDao.queryAll(userLoad);
            if (userLoads.size() > 0) {
                userLoadDao.deleteById(userLoads.get(0).getDevId());
            }
            AssertUtil.isTrue("F".equals(token1), 420);
            AssertUtil.isTrue("S".equals(token1), 421);
            AssertUtil.isTrue("G".equals(token1), 422);
        }
        // 查找 获取uId
        String[] split = token1.split("::");
        if (split.length < 2) return token1;
        return split[1];
    }

    /**
     * @param tag
     * @param uId
     */
    @Override
    public void callUserWord(String tag, String uId) {
        if (tag == null) {
            // 获取tag类型
            UserRemind userRemind = new UserRemind();
            userRemind.setUId(uId);
            List<UserRemind> userReminds = userRemindService.queryAll(userRemind);
            if (userReminds.size() == 0) {
                throw new MyException(500, "用户记录不存在");
            }
            userRemind = userReminds.get(0);
            userDao.callUserWord(userRemind.getTag(), uId);
        }
        else
            userDao.callUserWord(tag, uId);
    }

}