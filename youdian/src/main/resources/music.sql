drop database if exists music;
create database music;
use music;

create table user
(
    u_id     varchar(40) not null PRIMARY KEY COMMENT '用户id',
    usr      varchar(20) not null COMMENT '账户',
    pwd      varchar(20) NOT null COMMENT '密码',
    tel      varchar(20) NOT null COMMENT '电话',
    n_name   varchar(20) NOT null COMMENT '昵称',
    r_name   varchar(20) COMMENT '真实姓名',
    h_url    varchar(300) COMMENT '头像链接',
    gd_id    varchar(40) COMMENT '喜爱歌单id',
    usr_date datetime DEFAULT NOW() COMMENT '创建时间',
    unique index index_u_id (u_id)
);

create table music
(
    m_id   varchar(40) not null PRIMARY KEY COMMENT '音乐id',
    singer varchar(20) COMMENT '歌手',
    m_url  varchar(300) COMMENT '音乐链接',
    gc_url varchar(300) COMMENT '歌词链接',
    f_url  varchar(300) COMMENT '封面链接',
    m_name varchar(20) not null COMMENT '歌名',
    unique index index_m_id (m_id)
);


create table gedan
(
    gd_id   varchar(40) not null PRIMARY KEY COMMENT '歌单id',
    u_id    varchar(40) not null COMMENT '用户id',
    gd_name varchar(20) not null COMMENT '歌单名称',
    gd_fm   varchar(200) COMMENT '歌单封面',
    m_num   int(11) COMMENT '歌曲数量',
    gd_date datetime DEFAULT NOW() COMMENT '创建时间',
    FOREIGN KEY (u_id) REFERENCES user (u_id),
    unique index index_gd_id (gd_id)

);

create table gedan_music
(
    gd_id varchar(40) not null COMMENT '歌单id',
    m_id  varchar(40) not null COMMENT '音乐id',
    FOREIGN KEY (gd_id) REFERENCES gedan (gd_id),
    FOREIGN KEY (m_id) REFERENCES music (m_id),
    key index_gd_m (gd_id, m_id)
);

create table t_load
(
    l_id   varchar(40) not null COMMENT '登录id',
    app_id varchar(40) not null COMMENT '应用id',
    u_id   varchar(40) not null COMMENT '用户id',
    msg    varchar(40) not null COMMENT '随机数',

    FOREIGN KEY (u_id) REFERENCES user (u_id),
    unique index index_l_id (l_id)
);

