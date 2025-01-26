
CREATE DATABASE sushuodorm;
-- 切换库
use sushuodorm;

CREATE TABLE user (
                      id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '用户ID',
                      create_time DATETIME NOT NULL COMMENT '创建时间',
                      update_time DATETIME NOT NULL COMMENT '更新时间',
                      user_account VARCHAR(255) NOT NULL COMMENT '用户账号',
                      user_password VARCHAR(255) NOT NULL COMMENT '用户密码',
                      union_id VARCHAR(255) COMMENT '联合ID',
                      user_name VARCHAR(255) COMMENT '用户姓名',
                      user_avatar VARCHAR(255) COMMENT '用户头像',
                      user_profile VARCHAR(255) COMMENT '用户简介',
                      user_role VARCHAR(255) COMMENT '用户角色',
                      gender VARCHAR(255) COMMENT '性别',
                      phone VARCHAR(255) COMMENT '电话',
                      room_id VARCHAR(255) COMMENT '房间ID',
                      is_deleted BOOLEAN NOT NULL DEFAULT FALSE COMMENT '逻辑删除标志'
) COMMENT='用户表';

CREATE TABLE room (
                      room_id INT AUTO_INCREMENT PRIMARY KEY COMMENT '寝室号',
                      dorm_name VARCHAR(255) COMMENT '宿舍名称',
                      room_name VARCHAR(255) COMMENT '房间名称',
                      capacity INT COMMENT '房间容量',
                      create_time DATETIME NOT NULL COMMENT '创建时间',
                      update_time DATETIME NOT NULL COMMENT '更新时间',
                      is_deleted INT DEFAULT 0 COMMENT '逻辑删除标志'
) COMMENT='房间表';
CREATE TABLE post (
                      id INT AUTO_INCREMENT PRIMARY KEY COMMENT '帖子ID',
                      date DATETIME COMMENT '帖子日期',
                      title VARCHAR(255) NOT NULL COMMENT '帖子标题',
                      content TEXT NOT NULL COMMENT '帖子内容',
                      author VARCHAR(255) NOT NULL COMMENT '帖子作者',
                      tags VARCHAR(255) NOT NULL COMMENT '帖子标签',
                      likes BIGINT COMMENT '点赞数',
                      favorites BIGINT COMMENT '收藏数',
                      create_time DATETIME NOT NULL COMMENT '创建时间',
                      update_time DATETIME NOT NULL COMMENT '更新时间',
                      is_deleted INT DEFAULT 0 COMMENT '逻辑删除标志'
) COMMENT='帖子表';
CREATE TABLE msg (
                     id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '消息ID',
                     date DATETIME COMMENT '消息日期',
                     content TEXT COMMENT '消息内容',
                     author VARCHAR(255) COMMENT '消息作者',
                     create_time DATETIME NOT NULL COMMENT '创建时间',
                     update_time DATETIME NOT NULL COMMENT '更新时间'
) COMMENT='消息表';

CREATE TABLE mail (
                      id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '邮件ID',
                      user_id BIGINT COMMENT '用户ID',
                      sender_name VARCHAR(255) COMMENT '发件人姓名',
                      email VARCHAR(255) COMMENT '发件人邮箱',
                      title VARCHAR(255) COMMENT '邮件标题',
                      subject TEXT COMMENT '邮件主题',
                      date DATETIME COMMENT '邮件日期',
                      create_time DATETIME NOT NULL COMMENT '创建时间',
                      update_time DATETIME NOT NULL COMMENT '更新时间',
                      is_read BOOLEAN DEFAULT FALSE COMMENT '是否已读',
                      is_deleted BOOLEAN DEFAULT FALSE COMMENT '是否已删除',
                      is_replied BOOLEAN DEFAULT FALSE COMMENT '是否已回复'
) COMMENT='邮件表';

CREATE TABLE likePost (
                          id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '点赞ID',
                          username VARCHAR(255) COMMENT '用户名',
                          post_id BIGINT NOT NULL COMMENT '帖子ID',
                          user_id BIGINT NOT NULL COMMENT '用户ID',
                          create_time DATETIME NOT NULL COMMENT '创建时间',
                          update_time DATETIME NOT NULL COMMENT '更新时间'
) COMMENT='点赞表';

CREATE TABLE likeComment (
                             id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '点赞ID',
                             username VARCHAR(255) COMMENT '用户名',
                             comment_id BIGINT NOT NULL COMMENT '评论ID',
                             user_id BIGINT NOT NULL COMMENT '用户ID',
                             date DATETIME COMMENT '点赞日期',
                             create_time DATE NOT NULL COMMENT '创建时间',
                             update_time DATE NOT NULL COMMENT '更新时间'
) COMMENT='评论点赞表';

CREATE TABLE favorite (
                          id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '收藏ID',
                          post_id BIGINT NOT NULL COMMENT '帖子ID',
                          user_id BIGINT NOT NULL COMMENT '用户ID',
                          username VARCHAR(255) COMMENT '用户名',
                          create_time DATE NOT NULL COMMENT '创建时间',
                          update_time DATETIME NOT NULL COMMENT '更新时间',
                          is_deleted BOOLEAN DEFAULT FALSE COMMENT '是否已删除'
) COMMENT='收藏表';
CREATE TABLE comment (
                         id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '评论ID',
                         content TEXT NOT NULL COMMENT '评论内容',
                         author VARCHAR(255) NOT NULL COMMENT '评论作者',
                         date DATETIME COMMENT '评论日期',
                         likes BIGINT DEFAULT 0 COMMENT '点赞数',
                         post_id BIGINT NOT NULL COMMENT '帖子ID',
                         parent_id BIGINT COMMENT '父评论ID',
                         reply_num BIGINT DEFAULT 0 COMMENT '回复数',
                         create_time DATETIME NOT NULL COMMENT '创建时间',
                         update_time DATETIME NOT NULL COMMENT '更新时间'
) COMMENT='评论表';