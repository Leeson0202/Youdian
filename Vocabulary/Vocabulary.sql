drop database vocabulary;
create database vocabulary;
use vocabulary;

create table word
(
    w_id      varchar(50)  not null PRIMARY KEY COMMENT '单词id' default '',
    spell     varchar(100) not null COMMENT '拼写'               default '',
    tag       varchar(100) COMMENT '单词标签'                      default '',
    href      varchar(300) COMMENT '单词链接'                      default '',
    difficult smallint COMMENT '难度' default 0,
    unique index index_w_id (w_id)
);

# 词性+翻译  list 拼接
create table word_clear
(
    w_id     varchar(50) not null COMMENT '单词id' default '',
    clearfix text        not null COMMENT '词性+翻译',
    FOREIGN KEY (w_id) REFERENCES word (w_id)
);

# 变型  list 拼接
create table word_def
(
    w_id     varchar(50) not null COMMENT '单词id'  default '',
    tag      varchar(20) not null COMMENT '变型tag' default '',
    tag_list text        not null COMMENT '例子',
    FOREIGN KEY (w_id) REFERENCES word (w_id)
);

# 短语
create table word_phrase
(
    w_id    varchar(50)  not null COMMENT '单词id' default '',
    `key`   varchar(300) not null COMMENT '短语拼写' default '',
    `value` varchar(300) not null COMMENT '短语翻译' default '',
    FOREIGN KEY (w_id) REFERENCES word (w_id)
);

# 例句
create table word_sentences
(
    w_id    varchar(50) not null COMMENT '单词id' default '',
    `key`   text        not null COMMENT '句子英文',
    `value` text        not null COMMENT '句子翻译',
    FOREIGN KEY (w_id) REFERENCES word (w_id)
);

# 发音
create table word_audio
(
    w_id         varchar(50)  not null COMMENT '单词id' default '',
    `tag`        varchar(20)  not null COMMENT '发音类型' default '',
    `tag_detail` varchar(100) not null COMMENT '音标'   default '',
    `url`        varchar(300) COMMENT '语音链接'          default '',
    FOREIGN KEY (w_id) REFERENCES word (w_id)
);

# 相似词
create table word_similar
(
    w_id       varchar(50)  not null COMMENT '单词id' default '',
    `tag`      varchar(20)  not null COMMENT '相似类型' default '',
    `tag_list` varchar(200) not null COMMENT '例子'   default '',
    FOREIGN KEY (w_id) REFERENCES word (w_id)
);







