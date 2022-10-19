CREATE DATABASE IF NOT EXISTS `EXSIM_db`;
USE `EXSIM_db`;

CREATE TABLE IF NOT EXISTS `user`
(
    `id`       bigint       not null auto_increment,
    `username` varchar(30) not null,
    `password` varchar(100) default null,
    `email`    varchar(100) default null,
    `status`   tinyint(1)   default 0 comment '0 is frozen account,1 has activiated',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8;


CREATE TABLE IF NOT EXISTS `file`
(
    `id` bigint not null auto_increment,
    `file_name` varchar(100) not null ,
    `file_path` varchar(200) default null ,
    `created_time` datetime not null ,
    `last_modify_time` datetime not null ,
    `last_modify_user_id` bigint not null ,
    `create_author_id` bigint not null ,
    `description` varchar(200) default null,
    `property` tinyint(1) default 0 comment '0 is private,1 is public',
        PRIMARY KEY (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8;


CREATE TABLE IF NOT EXISTS `file_permission`
(
    `id` bigint not null  auto_increment,
    `user_id` bigint not null ,
    `file_id` bigint not null ,
    `permission` tinyint(1) default 0 comment '0 is read only,1 is write and read',
    `last_open_time` datetime default null,
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8;
