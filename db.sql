CREATE TABLE `tk_city` (
  `CityID` smallint(3) unsigned NOT NULL AUTO_INCREMENT,
  `CityName` varchar(50) NOT NULL DEFAULT '',
  `ZipCode` int(6) unsigned NOT NULL DEFAULT '0',
  `ProvinceID` tinyint(2) unsigned NOT NULL DEFAULT '0',
  `letter` char(1) NOT NULL DEFAULT '',
  PRIMARY KEY (`CityID`),
  KEY `letter` (`letter`)
) ENGINE=MyISAM AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;

INSERT INTO tk_city VALUES (1,'Seoul',156880,1,'S');
INSERT INTO tk_city VALUES (2,'Pusan',47100,2,'P');
INSERT INTO tk_city VALUES (3,'Taegu',42919,3,'T');
INSERT INTO tk_city VALUES (4,'Kwangju',500867,4,'K');