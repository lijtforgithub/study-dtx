CREATE TABLE `user` (
                        `id` int(10) unsigned NOT NULL AUTO_INCREMENT COMMENT 'ID',
                        `name` varchar(20) NOT NULL DEFAULT '' COMMENT '名称',
                        PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;