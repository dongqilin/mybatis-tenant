# DB_NAME: dongql

CREATE TABLE `user` (
  `uid` bigint(20) NOT NULL,
  `user_name` varchar(30) NOT NULL,
  `gender` tinyint(1) DEFAULT '0' COMMENT '0未知，1男，2女',
  PRIMARY KEY (`uid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `user_permission` (
  `permission_id` bigint(20) NOT NULL,
  `permission_name` varchar(30) NOT NULL,
  `permission_code` varchar(50) NOT NULL,
  `url` varchar(50) DEFAULT NULL,
  `permission_type` tinyint(4) NOT NULL DEFAULT '1' COMMENT '1菜单，2功能',
  PRIMARY KEY (`permission_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

