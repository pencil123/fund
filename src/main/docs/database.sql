 CREATE TABLE `record` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `fund_code` varchar(20) DEFAULT NULL,
  `dwjz` decimal(10,4) DEFAULT NULL,
  `ljjz` decimal(10,4) DEFAULT NULL,
  `jzzzl` decimal(10,4) DEFAULT NULL,
  `fsrq` date DEFAULT NULL,
  `avg_week` decimal(10,4) DEFAULT NULL,
  `avg_month` decimal(10,4) DEFAULT NULL,
  `avg3month` decimal(10,4) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uniqu` (`fund_code`,`fsrq`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 |


create table `fund` (
    fund_code varchar(20) not null primary key,
    fund_name varchar(40) not null default "",
    create_datetime datetime NOT NULL DEFAULT CURRENT_TIMESTAMP()
);