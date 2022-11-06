drop database if exists youdian;
create database youdian;
use youdian;

#  1. 用户

#  id	 昵称  电话 密码	描述	 邮箱  微信 年级	生日	  创建时间   头像	  背景    手机提醒  手表提醒
# u_id  name  tel  pwd  desc email wx grade birth  c_date  h_url  b_url   pho_rem  wat_rem
create table user
(
    u_id   varchar(50)  not null PRIMARY KEY COMMENT '用户id' default '',
    name   varchar(20)  not null COMMENT '昵称'               default '',
    tel    varchar(20)  not null COMMENT '电话'               default '',
    pwd    varchar(20)  not null COMMENT '密码'               default '',
    `desc` varchar(100) not null COMMENT '描述'               default '',
    email  varchar(100) not null COMMENT '邮箱'               default '',
    wx     varchar(100) not null COMMENT '微信'               default '',
    grade  varchar(20)  not null COMMENT '年级'               default '',
    birth  datetime     not null COMMENT '生日'               default NOW(),
    c_date datetime     not null COMMENT '创建时间'             default NOW(),
    h_url  varchar(300) not null COMMENT '头像url'            default '',
    b_url  varchar(300) not null COMMENT '背景url'            default '',
    unique index index_u_id (u_id)
);

# 2. 登陆记录
#
# 用户id  设备标识  设备类型  token    ip
# u_id   dev_id    tag     token    ip
create table user_load
(
    dev_id varchar(50) not null primary key COMMENT '设备id' default '',
    u_id   varchar(50) not null COMMENT '用户id'             default '',
    tag    smallint    not null comment '设备类型'             default 0,
    token  text        not null COMMENT 'token',
    ip     varchar(26) not null COMMENT 'ip'               default '',
    foreign key (u_id) references user (u_id)
);

# 3.  用户单词库
#
# 标记说明
# tag：0 陌生词库（100个）
# tag: 1 无需再背
# tag: 2 熟悉单词
# tag: 3 收藏->三次全错
# tag: 4 收藏->手动添加
#
# 用户id    单词id   标记      复习次数            难度     添加时间
# u_id      w_id    tag    times_review      difficult   c_date
create table user_word
(
    id           varchar(50) not null primary key COMMENT 'id' default '',
    u_id         varchar(50) not null COMMENT '用户id'           default '',
    w_id         varchar(50) not null COMMENT '单词id'           default '',
    tag          smallint    not null COMMENT '分类标记'           default 0,
    times_review smallint    not null COMMENT '是否复习'           default 0,
    difficult    smallint    not null COMMENT '难度'        default 0,
    c_date       datetime    not null COMMENT '创建时间'           default NOW(),
    foreign key (u_id) references user (u_id)
);

# 4. 时间数据记录
#
# 用户id      日期   A  B  C  D  E  F  G  H  I  J  K  L  当日总时长  当日总个数
# u_id       day    A  B  C  D  E  F  G  H  I  J  K  L  sum_time      sum
create table user_time_record
(
    id       varchar(50) not null primary key COMMENT 'id' default '',
    u_id     varchar(50) not null COMMENT '用户id'           default '',
    day      datetime    not null COMMENT '日期'             default NOW(),
    A        smallint    not null COMMENT '0-2'            default 0,
    B        smallint    not null COMMENT '2-4'            default 0,
    C        smallint    not null COMMENT '4-6'            default 0,
    D        smallint    not null COMMENT '6-8'            default 0,
    E        smallint    not null COMMENT '8-10'           default 0,
    F        smallint    not null COMMENT '10-12'          default 0,
    G        smallint    not null COMMENT '12-14'          default 0,
    H        smallint    not null COMMENT '14-16'          default 0,
    I        smallint    not null COMMENT '16-18'          default 0,
    J        smallint    not null COMMENT '18-20'          default 0,
    K        smallint    not null COMMENT '20-22'          default 0,
    `L`      smallint    not null COMMENT '22-24'          default 0,
    sum_time smallint    not null COMMENT '当日总时间'          default 0,
    sum      smallint    not null COMMENT 'sum'            default 0,
    foreign key (u_id) references user (u_id)
);

# 5. 提醒设置
#
# 用户id  单词本 手机提醒  手表提醒     天数    几个list  每个List单词数量  循环方式
# u_id    tag  pho_rem  wat_rem   day_time  list    num_of_list    circ_way
create table user_remind
(
    id          varchar(50) not null primary key COMMENT 'id' default '',
    u_id        varchar(50) not null COMMENT '用户id'           default '',
    tag         varchar(20) not null comment '单词本'            default '',
    pho_rem     boolean     not null COMMENT '手机提醒'           default 0,
    wat_rem     boolean     not null COMMENT '手表提醒'           default 0,
    day_time    int comment '天数'                              default 0,
    list        int comment '多少个list'                         default 0,
    num_of_list int comment '每个list单词数量'                      default 0,
    circ_way    smallint comment '循环方式'                       default 0,
    foreign key (u_id) references user (u_id)
);



# 6. 朋友圈（说说）
#
# 朋友圈 id  用户id   文字内容(500字）  获赞数    收藏数     评论
# c_id       u_id     contents       like     coll    comments
create table circle
(
    c_id       varchar(50)  not null PRIMARY KEY COMMENT '朋友圈id' default '',
    u_id       varchar(50)  not null COMMENT '用户id'              default '',
    contents   varchar(500) not null COMMENT '内容'                default '',
    `like`     int          not null comment '获赞数'               default 0,
    `coll`     int          not null comment '收藏数'               default 0,
    `comments` int          not null comment '评论数'               default 0,
    foreign key (u_id) references user (u_id)
);

# 7. 朋友圈图片内容
#
# 朋友圈id	图片链接
# c_id      photo_url
create table c_photo
(
    id        varchar(50)  not null primary key COMMENT 'id' default '',
    c_id      varchar(50)  not null COMMENT '朋友圈id'          default '',
    photo_url varchar(300) not null comment '图片链接'           default '',
    foreign key (c_id) references circle (c_id)
);

# 8. 赞
#
# 朋友圈id  赞用户id
# c_id      u_id
create table c_like
(
    id   varchar(50) not null primary key COMMENT 'id' default '',
    c_id varchar(50) not null COMMENT '朋友圈id'          default '',
    u_id varchar(50) not null COMMENT '用户id'           default '',
    foreign key (c_id) references circle (c_id),
    foreign key (u_id) references user (u_id)
);

# 9. 收藏
#
# 朋友圈id   收藏用户id
# c_id        u_id
create table c_fav
(
    id   varchar(50) not null primary key COMMENT 'id' default '',
    c_id varchar(50) not null COMMENT '朋友圈id'          default '',
    u_id varchar(50) not null COMMENT '用户id'           default '',
    foreign key (c_id) references circle (c_id),
    foreign key (u_id) references user (u_id)
);

# 10. 评论
#
# 朋友圈id  评论用户id   评论内容    评论时间
# c_id       u_id     comments  comment_date
create table c_comments
(
    id            varchar(50)  not null primary key COMMENT 'id' default '',
    c_id          varchar(50)  not null COMMENT '朋友圈id'          default '',
    u_id          varchar(50)  not null COMMENT '用户id'           default '',
    comments      varchar(200) not null comment '评论内容'           default '',
    comments_date datetime     not null comment '评论时间'           default NOW(),
    foreign key (c_id) references circle (c_id),
    foreign key (u_id) references user (u_id)
);

# 11 关注
# 用户id  被关注id 时间
create table follow
(
    id          varchar(50) not null primary key COMMENT 'id' default '',
    u_id        varchar(50) not null COMMENT '用户id'           default '',
    follow_id   varchar(50) not null COMMENT '被关注用户id'        default '',
    follow_date datetime    not null comment '关注时间'           default NOW()
);

drop procedure if exists update_user_word;
DELIMITER $$
create procedure update_user_word(
    in tag varchar(100), # 单词类型
    in uId varchar(50) # 用户id
)
begin
    declare count int;
    # 查找数量 用户单词库
    select count(*)
    from youdian.user_word as uword
    where uword.tag = 0 # tag = 0 表示陌生词库
      and u_id = uId
    into count;
    set count = 100 - count;

    if count > 0 then
        insert youdian.user_word(id, u_id, w_id, tag, times_review, difficult, c_date)
        select UUID(), uId, word.w_id, 0, 0, 0, NOW()
        from vocabulary.word as word
        where word.tag like concat('%', tag, '%')
          and word.w_id not in ( # 不重复
            select uword.w_id
            from youdian.user_word as uword
            where u_id = uId)
        order by rand()
        LIMIT count;

    END if;
end $$
DELIMITER ;



