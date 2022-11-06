package top.as.youdian.controller;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.models.auth.In;
import lombok.SneakyThrows;
import org.springframework.web.multipart.MultipartFile;
import top.as.youdian.entity.*;
import top.as.youdian.entity.user.User;
import top.as.youdian.entity.user.UserDetail;
import top.as.youdian.entity.user.UserIndex;
import top.as.youdian.entity.userWord.UserWord;
import top.as.youdian.entity.userWord.UserWordDetail;
import top.as.youdian.exception.MyException;
import top.as.youdian.service.*;
import org.springframework.web.bind.annotation.*;
import top.as.youdian.service.word.WordService;
import top.as.youdian.tools.AssertUtil;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.*;

import com.alibaba.fastjson.JSON;
import top.as.youdian.tools.IpUtil;
import top.as.youdian.tools.UpLoadFileUtil;
import top.as.youdian.tools.token.TokenUtils;


/**
 * (User)表控制层
 *
 * @author makejava
 * @since 2021-10-27 23:10:51
 */
//@Transactional
@RestController
@RequestMapping("user")
public class UserController {
    /**
     * 服务对象
     */
    @Resource
    private UserService userService;
    @Resource
    private UserLoadService userLoadService;
    @Resource
    private UserRemindService userRemindService;
    @Resource
    private UserWordService userWordService;
    @Resource
    private WordService wordService;
    @Resource
    private UserTimeRecordService userTimeRecordService;


    @ApiOperation(value = "3.5 获取今日计划")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "token", paramType = "header", required = true, dataTypeClass = String.class),
    })
    @PostMapping("getplan")
    public Map<String, Object> getplan(HttpServletRequest request) {
        // 1. 验证token
        String uId = userService.isLoad(request.getHeader("token"));
        // 今日新词，待复习，累计学习(今日已学习)，今日效率，记忆单词
        // 2. 获取计划
        UserRemind userRemind = new UserRemind();
        userRemind.setUId(uId);
        List<UserRemind> userReminds = userRemindService.queryAll(userRemind);
        if (userReminds.size() == 0) { // 重新创建
            userRemind.setId(UUID.randomUUID().toString());
            userRemind.setUId(uId);
            userRemind.setTag("CET4");
            userRemind.setPhoRem(1);
            userRemind.setWatRem(1);
            userRemind.setCircWay(0);
            userRemind.setDayTime(0);
            userRemind.setList(5);
            userRemind.setNumOfList(10);
            userRemindService.insert(userRemind);
        }
        // 今日计划
        Integer today = userReminds.get(0).getList() * userReminds.get(0).getNumOfList();
        // 3. 今日记录和总的记录 已背
        UserTimeRecord userTimeRecord = new UserTimeRecord();
        userTimeRecord.setUId(uId);
        List<UserTimeRecord> userTimeRecords = userTimeRecordService.queryAll(userTimeRecord);
        Integer sumWord = 0; // 记过了 总的单词（累计学习）
        Integer sumTime = 0;
        for (UserTimeRecord timeRecord : userTimeRecords) {
            sumWord += timeRecord.getSum();
            sumTime += timeRecord.getSumTime();
        }
        // 获取今天的
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        cal.set(Calendar.HOUR_OF_DAY, 0);  // 时
        cal.set(Calendar.MINUTE, 0);  // 分
        cal.set(Calendar.SECOND, 0);  // 秒
        cal.set(Calendar.MILLISECOND, 0);  // 毫秒
        // 设置时间
        userTimeRecord.setDay(cal.getTime());
        // 查询数据库
        userTimeRecords = userTimeRecordService.queryAll(userTimeRecord);
        // 4. 计算今日效率
        Integer todayed = 0;
        Integer todayTime = 0;
        Double efficiency = 0.0;
        if (userTimeRecords.size() != 0) {  // 今天有记录
            todayed = userTimeRecords.get(0).getSum(); //今天已经完成了
            todayTime = userTimeRecords.get(0).getSumTime(); //今天已经完成了时间
            efficiency = 3600* todayed / (double) todayTime;  // 效率
        }
        today -= todayed; // 今日新的剩余
        // 设置日期为昨天的
        cal.add(Calendar.DAY_OF_MONTH, -1);
        userTimeRecords = userTimeRecordService.queryAll(userTimeRecord); // 查询昨天的记录
        Integer toReviewed = 0;
        if (userTimeRecords.size() != 0)
            toReviewed = userTimeRecords.get(0).getSum();

        // 添加
        HashMap<String, Object> returnMap = new HashMap<>();
        returnMap.put("sumWord", sumWord);
        returnMap.put("today", today);
        returnMap.put("todayed", todayed);
        returnMap.put("sumTime", sumTime);
        returnMap.put("efficiency", efficiency);
        returnMap.put("toReviewed", toReviewed);
        return AssertUtil.returnMap(200, "获取今日计划成功", returnMap);
    }

    @ApiOperation(value = "3.4 获取背诵记录")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "token", paramType = "header", required = true, dataTypeClass = String.class),
    })
    @PostMapping("getrecord")
    public Map<String, Object> getrecord(HttpServletRequest request) {
        // 1. 验证token
        String uId = userService.isLoad(request.getHeader("token"));
        // 2. 获取 前14天的数据
        Date date = new Date();  // 当天时间
        Calendar cal = Calendar.getInstance();
        //获取前面的时间用-负号
        cal.setTime(date);
        cal.add(Calendar.DAY_OF_MONTH, -7);  // 7天内
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);

        // 计算 a,b,c... 每天记忆单词用时  每小时记忆单词量 比上周效率变化
        // 定义变量

        // 七天的总天数
        List<UserTimeRecord> userTimeRecords = this.userTimeRecordService.queryByUIdAfterDay(uId, cal.getTime());
        HashMap<String, Integer> map = this.getmap(userTimeRecords);
        Integer length1 = userTimeRecords.size();
        // 如果 0-7天为0
        if (length1 == 0 || map.get("AverageWords") == 0 || map.get("AverageTime") == 0) {
            map.put("AverageWords", 0);
            map.put("AverageTime", 0);
            map.put("Efficiency", -100); // 效率+-
            return AssertUtil.returnMap(200, "获取记录成功", map);
        }
        // 14天
        cal.add(Calendar.DAY_OF_MONTH, -7);  // 14 内的
        List<UserTimeRecord> userTimeRecords_14 = this.userTimeRecordService.queryByUIdAfterDay(uId, cal.getTime());
        HashMap<String, Integer> map1 = this.getmap(userTimeRecords_14);

        Integer lastTime = map1.get("AverageTime") - map.get("AverageTime"); // 前7-14天的总时间 s
        Integer lastWord = map1.get("AverageWords") - map.get("AverageWords"); // 前7-14 天的总个数

        // 计算7天内的 单词/h 和 h/每天
        double averageWords = map.get("AverageWords") / (map.get("AverageTime") / 3600.0); // 前0-7 天的效率=总个数/ 总时间   =（每小时平均多少个单词）
        map.put("AverageWords", (int) averageWords);
        map.put("AverageTime", map.get("AverageTime") / length1);  // 每天用时 = 总时间 / 总的天数

        // 如果7-14 为 0 
        double averageWords1 = 0;
        if (lastTime != 0 && lastWord != 0) {  // 不为0 时计算 word/h
            averageWords1 = lastWord / (lastTime / 3600.0);  // 前7-14 天的效率=总个数/ 总时间   =（每小时平均多少个单词）
        }


        // 计算效率
        double i = 100.0 * (map.get("AverageWords") - averageWords1) / map.get("AverageWords");
        map.put("Efficiency", (int) i); // 效率+-
        return AssertUtil.returnMap(200, "获取记录成功", map);
    }

    public HashMap<String, Integer> getmap(List<UserTimeRecord> userTimeRecords) {
        HashMap<String, Integer> map = new HashMap<>();
        map.put("A", 0);
        map.put("B", 0);
        map.put("C", 0);
        map.put("D", 0);
        map.put("E", 0);
        map.put("F", 0);
        map.put("G", 0);
        map.put("H", 0);
        map.put("I", 0);
        map.put("J", 0);
        map.put("K", 0);
        map.put("L", 0);
        map.put("AverageWords", 0);
        map.put("AverageTime", 0);
        for (UserTimeRecord userTimeRecord : userTimeRecords) {
            map.put("A", userTimeRecord.getA() + map.get("A"));
            map.put("B", userTimeRecord.getB() + map.get("B"));
            map.put("C", userTimeRecord.getC() + map.get("C"));
            map.put("D", userTimeRecord.getD() + map.get("D"));
            map.put("E", userTimeRecord.getE() + map.get("E"));
            map.put("F", userTimeRecord.getF() + map.get("F"));
            map.put("G", userTimeRecord.getG() + map.get("G"));
            map.put("H", userTimeRecord.getH() + map.get("H"));
            map.put("I", userTimeRecord.getI() + map.get("I"));
            map.put("J", userTimeRecord.getJ() + map.get("J"));
            map.put("K", userTimeRecord.getK() + map.get("K"));
            map.put("L", userTimeRecord.getL() + map.get("L"));
            map.put("AverageWords", userTimeRecord.getSum() + map.get("AverageWords"));
            map.put("AverageTime", userTimeRecord.getSumTime() + map.get("AverageTime"));

        }
        return map;
    }

    @SneakyThrows
    @ApiOperation(value = "1.5.3 修改用户背景图片")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "token", paramType = "header", required = true, dataTypeClass = String.class),
            @ApiImplicitParam(name = "file", value = "头像 png/jpg", required = true, dataType = "__file", paramType = "form"),
    })
    @PostMapping("changeback")
    public Map<String, Object> changeback(@RequestParam("file") MultipartFile file, HttpServletRequest request) {
        // 1. 验证token
        String uId = userService.isLoad(request.getHeader("token"));
        // 2. 上传图片
        String bUrl = UpLoadFileUtil.uploadImg(file, "users/img/");
        // 3. 判断该用户是否存在
        User user = userService.queryById(uId);
        AssertUtil.isTrue(user == null, 500);
        // 4. 修改头像
        user.setBUrl(bUrl);
        User update = userService.update(user);
        AssertUtil.isTrue(update == null, 500);

        return AssertUtil.returnMap(200, "头像上传成功", update);
    }


    @SneakyThrows
    @ApiOperation(value = "1.5.2 修改用户头像")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "token", paramType = "header", required = true, dataTypeClass = String.class),
            @ApiImplicitParam(name = "file", value = "头像 png/jpg", required = true, dataType = "__file", paramType = "form"),
    })
    @PostMapping("changehead")
    public Map<String, Object> changehead(@RequestParam("file") MultipartFile file, HttpServletRequest request) {
        // 1. 验证token
        String uId = userService.isLoad(request.getHeader("token"));
        // 2. 上传图片
        String hUrl = UpLoadFileUtil.uploadImg(file, "users/img/");
        // 3. 判断该用户是否存在
        User user = userService.queryById(uId);
        AssertUtil.isTrue(user == null, 500);
        // 4. 修改头像
        user.setHUrl(hUrl);
        User update = userService.update(user);
        AssertUtil.isTrue(update == null, 500);

        return AssertUtil.returnMap(200, "头像上传成功", update);
    }


    @ApiOperation(value = "3.2 提交背诵数据")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "token", paramType = "header", required = true, dataTypeClass = String.class),
    })
    @PostMapping("postdata")
    public Map<String, Object> postdata(@RequestBody Map<String, Object> map, HttpServletRequest request) {
        // 1. 验证token
        String uId = userService.isLoad(request.getHeader("token"));
        // 2. 获取
        Object word_record = map.get("word_record");
        Object time_record = map.get("time_record");
        List<UserWord> word_records = JSON.parseArray(JSON.toJSONString(word_record), UserWord.class);
        List<UserTimeRecord> time_records = JSON.parseArray(JSON.toJSONString(time_record), UserTimeRecord.class);
        map = null;
        word_record = null;
        time_record = null;
        // 组装 写入数据库
        // 1. 先插入 每日记录
        if (time_records != null) {
            for (UserTimeRecord timeRecord : time_records) {
                // 判断是否为整时
                Calendar cal = Calendar.getInstance();
                cal.setTime(timeRecord.getDay());
                cal.set(Calendar.HOUR_OF_DAY, 0);  // 时
                cal.set(Calendar.MINUTE, 0);  // 分
                cal.set(Calendar.SECOND, 0);  // 秒
                cal.set(Calendar.MILLISECOND, 0);  // 毫秒
                if (cal.getTime().getTime() != timeRecord.getDay().getTime())
                    return AssertUtil.returnMap(401, "上传非凌晨时间");
                // 判断是否存在 通过用户id和时间
                Date date1 = new Date(timeRecord.getDay().getTime());
                UserTimeRecord userTimeRecord1 = new UserTimeRecord();
                userTimeRecord1.setUId(uId);
                userTimeRecord1.setDay(date1);
                List<UserTimeRecord> userTimeRecords = this.userTimeRecordService.queryAll(userTimeRecord1);
                if (userTimeRecords.size() > 0) {
                    // update
                    UserTimeRecord userTimeRecord = userTimeRecords.get(0);
                    userTimeRecord.setSumTime(userTimeRecord.getSumTime() + timeRecord.getSumTime());  // 计算总时间
                    this.userTimeRecordService.update(userTimeRecord); //更新数据
                } else {
                    // insert
                    //开始就有 day sumTime 只需要加入uId Id 其他默认未0
                    timeRecord.setUId(uId);
                    timeRecord.setId(UUID.randomUUID().toString());
                    timeRecord.setDay(new Date(timeRecord.getDay().getTime()));
                    this.userTimeRecordService.insert(timeRecord);// 插入数据
                }
            }
        }
        // 2. 再插入单词记录
        if (word_records != null) {
            for (UserWord wordRecord : word_records) {
                // 1. 更新用户记录数据库
                // 获取时间
                Date cDate = (Date) wordRecord.getCDate().clone();
                // 解析时间 查询记录表
                Calendar cal = Calendar.getInstance();
                cal.setTime(cDate);
                cal.set(Calendar.HOUR_OF_DAY, 0);  // 时
                cal.set(Calendar.MINUTE, 0);  // 分
                cal.set(Calendar.SECOND, 0);  // 秒
                cal.set(Calendar.MILLISECOND, 0);  // 毫秒
                Date date = cal.getTime();
                // 查找当天是否有时间记录
                UserTimeRecord userTimeRecord1 = new UserTimeRecord();
                userTimeRecord1.setUId(uId);
                userTimeRecord1.setDay(date);
                List<UserTimeRecord> userTimeRecords = this.userTimeRecordService.queryAll(userTimeRecord1);
                if (userTimeRecords.size() == 0)
                    return AssertUtil.returnMap(401, "上传的单词没有该天的时间记录");
                // 检测用户单词是否存在
                UserTimeRecord userTimeRecord = userTimeRecords.get(0);
                int hours = wordRecord.getCDate().getHours();
                if (hours < 2)
                    userTimeRecord.setA(userTimeRecord.getA() + 1);
                else if (hours < 4)
                    userTimeRecord.setB(userTimeRecord.getB() + 1);
                else if (hours < 6)
                    userTimeRecord.setC(userTimeRecord.getC() + 1);
                else if (hours < 8)
                    userTimeRecord.setD(userTimeRecord.getD() + 1);
                else if (hours < 10)
                    userTimeRecord.setE(userTimeRecord.getE() + 1);
                else if (hours < 12)
                    userTimeRecord.setF(userTimeRecord.getF() + 1);
                else if (hours < 14)
                    userTimeRecord.setG(userTimeRecord.getG() + 1);
                else if (hours < 16)
                    userTimeRecord.setH(userTimeRecord.getH() + 1);
                else if (hours < 18)
                    userTimeRecord.setI(userTimeRecord.getI() + 1);
                else if (hours < 20)
                    userTimeRecord.setG(userTimeRecord.getJ() + 1);
                else if (hours < 22)
                    userTimeRecord.setK(userTimeRecord.getK() + 1);
                else
                    userTimeRecord.setL(userTimeRecord.getL() + 1);
                userTimeRecord.setSum(userTimeRecord.getSum() + 1);

                // 修改数据
                this.userTimeRecordService.update(userTimeRecord);

                // 2. 更新用户单词数据库
                this.userWordService.update(wordRecord);
            }
        }
        // 更新陌生单词
        userService.callUserWord(null, uId);
        return AssertUtil.returnMap(200, "数据上传成功");
    }

    @PostConstruct
    public void setProperties() {
        System.setProperty("druid.mysql.usePingMethod", "false");
    }


    @ApiOperation(value = "3.3 获取个人单词库")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "token", paramType = "header", required = true, dataTypeClass = String.class),
            @ApiImplicitParam(name = "tag", value = "单词类型（3/4收藏？2无需再背?）", paramType = "query", required = true, dataTypeClass = Integer.class),
    })
    @PostMapping("getmylist")
    public Map<String, Object> getmylist(Integer tag, HttpServletRequest request) {
        String uId = userService.isLoad(request.getHeader("token"));
        // 1. 创建userWord 查询类
        UserWord userWord = new UserWord();
        userWord.setUId(uId);
        userWord.setTag(tag);  //  表示陌生单词库
        // 2. 查询单词list
        List<UserWord> userWords = this.userWordService.queryAll(userWord); // 获取单词list
        // 3. 添加详细的单词list
        for (UserWord uword : userWords) {  // 开始添加
            uword.setWord(this.wordService.getElements(uword.getWId()));  // 获取 word信息
        }
        return AssertUtil.returnMap(200, "获取成功", userWords);
    }

    @ApiOperation(value = "3.1 获取陌生单词列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "token", paramType = "header", required = true, dataTypeClass = String.class),
    })
    @PostMapping("getlist")
    public Map<String, Object> getlist(HttpServletRequest request) {
        String uId = userService.isLoad(request.getHeader("token"));
        UserRemind userRemind = new UserRemind();
        userRemind.setUId(uId);
        UserWord userWord = new UserWord();
        userWord.setUId(uId);
        userWord.setTag(0);  // 0 表示陌生单词库
        List<UserRemind> userReminds = userRemindService.queryAll(userRemind);
        if (userReminds.size() == 0)
            AssertUtil.returnMap(500, "单词提醒信息未设置");
        List<UserWord> userWords = this.userWordService.queryAll(userWord); // 获取单词list
        AssertUtil.isTrue(userWords.size() == 0, 500, "陌生单词未初始化");
        Integer num = userReminds.get(0).getNumOfList() * userReminds.get(0).getList(); // 总的数量list
        ArrayList<UserWordDetail> userWordDetails = new ArrayList<>();
        for (UserWord uword : userWords) {  // 开始添加
            uword.setWord(this.wordService.getElements(uword.getWId()));  // 获取 word信息
            userWordDetails.add(JSON.parseObject(JSON.toJSONString(uword), UserWordDetail.class));
            if (num == userWordDetails.size()) break;
        }
        return AssertUtil.returnMap(200, "获取成功", userWordDetails);
    }


    // 获取个人偏好设置
    @ApiOperation(value = "2.2 获取个人偏好")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "token", paramType = "header", required = true, dataTypeClass = String.class),
    })
    @PostMapping("getsetelement")
    public Map<String, Object> getsetelement(HttpServletRequest request) {
        String uId = userService.isLoad(request.getHeader("token"));
        UserRemind userRemind = new UserRemind();
        userRemind.setUId(uId);
        List<UserRemind> userReminds = this.userRemindService.queryAll(userRemind);
        AssertUtil.isTrue(userReminds.size() == 0, 500, "获取失败");
        return AssertUtil.returnMap(200, "获取成功", userReminds.get(0));
    }

    @ApiOperation(value = "2.1 修改个人偏好")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "token", paramType = "header", required = true, dataTypeClass = String.class),
            @ApiImplicitParam(name = "tag", value = "单词本", paramType = "query", required = true, dataTypeClass = String.class),
            @ApiImplicitParam(name = "phoRem", value = "手机提醒", paramType = "query", required = true, dataTypeClass = String.class),
            @ApiImplicitParam(name = "watRem", value = "手表提醒", paramType = "query", required = true, dataTypeClass = String.class),
            @ApiImplicitParam(name = "circWay", value = "循环方式", paramType = "query", required = true, dataTypeClass = String.class),
            @ApiImplicitParam(name = "dayTime", value = "记忆天数", paramType = "query", required = false, dataTypeClass = String.class),
            @ApiImplicitParam(name = "list", value = "几个list", paramType = "query", required = false, dataTypeClass = String.class),
            @ApiImplicitParam(name = "numOfList", value = "一个List的单词数", paramType = "query", required = false, dataTypeClass = String.class)
    })
    @PostMapping("setelement")
    public Map<String, Object> update(UserRemind userRemind, HttpServletRequest request) {
        String uId = userService.isLoad(request.getHeader("token"));
        userRemind.setUId(uId);
        // 查找 remind
        UserRemind userRemind1 = new UserRemind();
        userRemind1.setUId(uId);
        List<UserRemind> userReminds = userRemindService.queryAll(userRemind1);
        if (userReminds.size() == 0) {
            // 创建一个
            userRemind.setId(UUID.randomUUID().toString());
            if (userRemind.getTag() == null) userRemind.setTag("");
            if (userRemind.getPhoRem() == null) userRemind.setPhoRem(1);
            if (userRemind.getWatRem() == null) userRemind.setWatRem(1);
            if (userRemind.getCircWay() == null) userRemind.setCircWay(0);
            if (userRemind.getDayTime() == null) userRemind.setDayTime(0);
            if (userRemind.getList() == null) userRemind.setList(5);
            if (userRemind.getNumOfList() == null) userRemind.setNumOfList(10);
            AssertUtil.isTrue(this.userRemindService.insert(userRemind) == null, 500);
        } else {
            userRemind.setId(userReminds.get(0).getId());
            AssertUtil.isTrue(this.userRemindService.update(userRemind) == null, 500);
        }
        // 如果 tag改了 更新 陌生单词库
        if (userRemind.getTag() != null) {
            // 1. 删除原来的数据
            UserWord userWord = new UserWord();
            userWord.setUId(uId);  // 用户Id
            userWord.setTag(0); // 陌生单词库
            List<UserWord> userWords = userWordService.queryAll(userWord);
            for (UserWord word : userWords) {
                userWordService.deleteById(word.getId());// 删除
            }
            // 2. 插入
            userService.callUserWord(userRemind.getTag(), uId);

        }
        return AssertUtil.returnMap(200, "修改成功");
    }


    @ApiOperation(value = "1.5.1 修改用户基本信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "token", required = true, dataTypeClass = String.class, paramType = "header")
    })
    @PostMapping("/update")
    public Map<String, Object> update(User user, HttpServletRequest request) {
        String uId = userService.isLoad(request.getHeader("token"));

        user.setUId(uId);
        user.setCDate(null);
        user.setWx(null);
        user.setTel(null);
        user.setHUrl(null);
        user.setBUrl(null);
        this.userService.update(user);
        return AssertUtil.returnMap(200, "修改成功");
    }


    @ApiOperation(value = "1.3 找回密码")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "tel", value = "tel", paramType = "query", required = true, dataTypeClass = String.class),
            @ApiImplicitParam(name = "pwd", value = "密码", paramType = "query", required = true, dataTypeClass = String.class),
            @ApiImplicitParam(name = "code", value = "验证码", paramType = "query", required = true, dataTypeClass = String.class),
    })
    @PostMapping("find")
    public Map<String, Object> find(String tel, String pwd, String code, HttpSession session) {
        AssertUtil.strIsEmpty(new String[]{tel, pwd, code});  // 判空

        // 获得 session中的 验证码code
        String sessioncode = (String) session.getAttribute("code");
        AssertUtil.strIsEmpty(sessioncode, 402); // 对比code

        session.removeAttribute("code");// 删除 验证码 code
        AssertUtil.isTrue(!sessioncode.equalsIgnoreCase(code), 402);  // 对比code
        // 查询用户
        User user = this.userService.queryByTel(tel);
        AssertUtil.isTrue(user == null, 410);  // 对比code
        // 设置密码
        user.setPwd(pwd);
        User update = this.userService.update(user);
        AssertUtil.isTrue(update == null, 500);  // 判空

        return AssertUtil.returnMap(200, "找回成功");
    }


    @ApiOperation(value = "1.4.2 获取用户主页信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "token", required = true, dataTypeClass = String.class, paramType = "header"),
            @ApiImplicitParam(name = "uId", value = "用户Id", required = true, dataTypeClass = String.class, paramType = "query"),
    })
    @PostMapping("getuserindex")
    public Map<String, Object> getuserIndex(String uId, HttpServletRequest request) {
        userService.isLoad(request.getHeader("token"));

        return AssertUtil.returnMap(200, "获取成功", JSON.parseObject(JSON.toJSONString(this.userService.queryById(uId)), UserIndex.class));
    }


    @ApiOperation(value = "1.4.1 获取用户详细信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "token", required = true, dataTypeClass = String.class, paramType = "header"),
    })
    @PostMapping("getuserdetail")
    public Map<String, Object> getuserdetail(HttpServletRequest request) {
        String uId = userService.isLoad(request.getHeader("token"));
        User user = this.userService.queryById(uId);
        return AssertUtil.returnMap(200, "获取成功", JSON.parseObject(JSON.toJSONString(user), UserDetail.class));
    }


    @ApiOperation(value = "1.2 用户注册")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "tel", value = "手机号", required = true, dataTypeClass = String.class, paramType = "query"),
            @ApiImplicitParam(name = "pwd", value = "密码", required = true, dataTypeClass = String.class, paramType = "query"),
            @ApiImplicitParam(name = "code", value = "验证码", required = true, dataTypeClass = String.class, paramType = "query"),
    })
    @PostMapping("regist")
    public Map<String, Object> regist(String tel, String pwd, String code, HttpSession session) {
        // 获得 session中的 验证码code
        String sessioncode;
        sessioncode = (String) session.getAttribute("code");
        AssertUtil.isTrue(sessioncode == null, 402);
        session.removeAttribute("code");// 删除 验证码 code
        AssertUtil.isTrue(!sessioncode.equalsIgnoreCase(code), 402);  // 对比code
        // 0. 判断是否注册过
        User user = new User();
        user.setTel(tel);
        User user1 = userService.queryByTel(tel);
        if (user1 != null && user1.getTel().equals(tel))
            return AssertUtil.returnMap(412, "用户已注册");
        // 1. user 表
        String uId = UUID.randomUUID().toString();
        user.setPwd(pwd);
        user.setUId(uId);
        AssertUtil.isTrue(this.userService.insert(user) == null, 500); // 3. 初始化用户单词表（直接random）

        // 2. user_remind表
        UserRemind userRemind = new UserRemind();
        userRemind.setId(UUID.randomUUID().toString());
        userRemind.setUId(uId);
        userRemind.setTag("CET4");
        userRemind.setPhoRem(1);
        userRemind.setWatRem(1);
        userRemind.setCircWay(0);
        userRemind.setDayTime(0);
        userRemind.setList(5);
        userRemind.setNumOfList(10);
        AssertUtil.isTrue(this.userRemindService.insert(userRemind) == null, 500);
        return AssertUtil.returnMap(200, "注册成功");
    }


    @ApiOperation(value = "1.1.1 手机登录")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "tel", value = "手机号", required = true, dataTypeClass = String.class, paramType = "query"),
            @ApiImplicitParam(name = "pwd", value = "密码", required = true, dataTypeClass = String.class, paramType = "query"),
            @ApiImplicitParam(name = "devId", value = "设备标识", required = true, dataTypeClass = String.class, paramType = "query"),
    })
    @PostMapping("load")
    public Map<String, Object> load(@RequestParam("tel") String tel, @RequestParam("pwd") String pwd, @RequestParam("devId") String devId, HttpServletRequest request) {
        // 1. 判断是否为空
        AssertUtil.strIsEmpty(new String[]{tel, pwd, devId}, 400); // 为空
        // 2. 验证账户密码
        User user = this.userService.queryByTel(tel);
        AssertUtil.isTrue(user == null, 410);
        if (!AssertUtil.strEqualsStr(user.getPwd(), pwd)) {// 登录失败
            throw new MyException(411);
        }
        String uId = user.getUId();
        // 2.1 验证是否已经登录
        UserLoad userLoad = new UserLoad();
        userLoad.setTag(devId.charAt(0));
        // 查 uId
        userLoad.setUId(uId);
        List<UserLoad> userLoads = userLoadService.queryAll(userLoad);
        if (userLoads.size() > 0) {
            userLoadService.deleteById(userLoads.get(0).getDevId());  // 删除
        }
        userLoad.setUId(null);

        // 查 devId
        userLoad.setDevId(devId);
        userLoads = userLoadService.queryAll(userLoad);
        //该设备有登录记录 删除登陆记录
        if (userLoads.size() > 0) {
            userLoadService.deleteById(userLoads.get(0).getDevId());  // 删除
        }
        // 3. 生成token
        String token = TokenUtils.getToken(devId + "::" + uId);

        // 4. 添加到load
        userLoad.setUId(uId);
        userLoad.setToken(token);
        String ipAddress = IpUtil.getIpAddr(request);
        userLoad.setIp(ipAddress);
        // 5. load 写入数据库
        if (userLoadService.insert(userLoad) == null) {
            throw new MyException(500);
        }
        Map<String, Object> tokenMap = new HashMap<>();
        tokenMap.put("token", token);
        return AssertUtil.returnMap(200, "登录成功", tokenMap);
    }


    @ApiOperation(value = "1.1.2 手表登录、Web登录")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "devId", value = "设备标识", required = false, dataTypeClass = String.class, paramType = "query"),
            @ApiImplicitParam(name = "token", value = "token", required = false, dataTypeClass = String.class, paramType = "header"),
    })
    @PostMapping("wload")
    public Map<String, Object> wload(String devId, HttpServletRequest request) {
        Map<String, Object> tokenMap = new HashMap<>();
        // 1. 判断是否有devId
        if (devId == null || devId.isEmpty()) {//走token
            String token = request.getHeader("token");
            if (token.isEmpty())
                throw new MyException(400); // devId token都为空
            // 有 token，获取token
            userService.isLoad(token);  //是否有效
            devId = TokenUtils.checkToken(token);

            // 查数据库
            UserLoad userLoad = new UserLoad();
            userLoad.setDevId(devId);
            userLoad.setTag(devId.charAt(0));
            List<UserLoad> userLoads = userLoadService.queryAll(userLoad);
            AssertUtil.isTrue(userLoads.size() == 0, 101); // 没有记录
            token = userLoads.get(0).getToken();
            // 判断token是否有效
            userService.isLoad(token);
            tokenMap.put("token", token);
            return AssertUtil.returnMap(200, "登录成功", tokenMap);
        }
        // 第一次 给服务器发送信息 devId
        String token = TokenUtils.getToken(devId);
        tokenMap.put("token", token);
        return AssertUtil.returnMap(101, "未登录", tokenMap);
    }


    @ApiOperation(value = "1.1.3 手机扫码登录")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "devId", value = "设备标识", required = true, dataTypeClass = String.class, paramType = "query"),
            @ApiImplicitParam(name = "token", value = "token", required = true, dataTypeClass = String.class, paramType = "header"),
    })
    @PostMapping("pload")
    public Map<String, Object> pload(@RequestParam("devId") String devId, HttpServletRequest request) {
        // 1. 获取用户信息
        String token = request.getHeader("token");
        // 2. 判断是否登录
        String uId = userService.isLoad(token);
        // 3. 判断是否已经有登录
        UserLoad userLoad = new UserLoad();
        userLoad.setTag(devId.charAt(0));
        // 查 uId + Tag
        userLoad.setUId(uId);
        List<UserLoad> userLoads = userLoadService.queryAll(userLoad);

        if (userLoads.size() > 0) {
            userLoadService.deleteById(userLoads.get(0).getDevId());  // 删除
        }
        userLoad.setUId(null);

        // 查 devId + Tag
        userLoad.setDevId(devId);
        userLoads = userLoadService.queryAll(userLoad);
        //该设备有登录记录 删除登陆记录
        if (userLoads.size() > 0) {
            userLoadService.deleteById(userLoads.get(0).getDevId());  // 删除
        }

        // 4. 添加UserLoad
        // 设置 token
        token = TokenUtils.getToken(devId + "::" + uId);
        userLoad.setUId(uId);
        userLoad.setIp(IpUtil.getIpAddr(request));
        userLoad.setToken(token);
        UserLoad insert = userLoadService.insert(userLoad);
        AssertUtil.isTrue(insert == null, 500);

        return AssertUtil.returnMap(200, "登录成功");

    }

}