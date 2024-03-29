/*
Vita plus
# 版本号:Version 1.0.00 --- 2020-02-14
 初始化创建

*/
/*
查看建数据库的语句
show create database MiniBlog;
查看建表的语句
SHOW CREATE TABLE MB_User;
修改数据库密码
update user set authentication_string=PASSWORD('bd3366x,') where User='root';
alter user 'root'@'localhost' identified by 'vita2019';
flush privileges;
*/
-- ------------------------------------------------------------------------------------------------
-- 创建数据库
/*
character 指定数据库存储字符串的默认字符集；
collate 指定数据库的默认校对规则，用来比较字符串的方式，解决排序和字符分组的问题；
*/
CREATE DATABASE IF NOT EXISTS Vita_OnlineRestaurant
	DEFAULT CHARACTER SET utf8
	DEFAULT COLLATE utf8_general_ci;
-- 跳转到指定数据库下
USE Vita_OnlineRestaurant;
-- 商品表
DROP TABLE IF EXISTS V_Product;
CREATE TABLE V_Product (
	productId INT UNSIGNED UNIQUE NOT NULL AUTO_INCREMENT COMMENT'[默认]商品编号：唯一、非空',
	productName VARCHAR(50) UNIQUE NOT NULL COMMENT'商品名称：唯一、非空',
	productTypeId INT UNSIGNED COMMENT'商品类别编号',
	productAttributeTypeId INT UNSIGNED COMMENT'商品属性编号',
	productPrice DECIMAL(10,1) NOT NULL COMMENT'商品单价：非空',
	productStockFlag TINYINT NOT NULL COMMENT'商品库存标识（0/无库存限制, 1/有库存限制）：非空',
	productStock INT UNSIGNED COMMENT'商品库存',
	productSalesVolume INT UNSIGNED NOT NULL DEFAULT 0 COMMENT'商品销售总量：非空',
	productIsShow TINYINT NOT NULL COMMENT'商品上架标识（0/未上架, 1/已上架）：非空',
	productImgName VARCHAR(100) NOT NULL COMMENT'商品图片：非空',
	productInfo VARCHAR(200) COMMENT'商品描述',
	productGrade SMALLINT UNSIGNED COMMENT'[弃用]商品优先级：非空，其为用于商品排序的首要标准',
	isDelete TINYINT DEFAULT 0 NOT NULL COMMENT'删除标识（0/未删除, 1/已删除）：非空',
	createDateTime DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT'创建时间：非空',
	updateDateTime DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT'修改时间：非空',
	PRIMARY KEY (productId)
)ENGINE=INNODB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT'商品表';
INSERT V_Product(productName, productTypeId, productPrice, productStockFlag, productIsShow, productImgName, productGrade)
VALUES ('牛腩面', 1, 25.5, 0, 0, 'default.gif', 50), ('牛肉面', 1, 15.5, 0, 0, 'default.gif', 50), ('鸡蛋炒面', 1, 15.5, 0, 0, 'default.gif', 50);
-- 商品类型表
DROP TABLE IF EXISTS V_ProductType;
CREATE TABLE V_ProductType (
	productTypeId INT UNSIGNED UNIQUE NOT NULL AUTO_INCREMENT COMMENT'[默认]商品类型编号：唯一、非空',
	productTypeName VARCHAR(30) UNIQUE NOT NULL COMMENT'商品类型名称：唯一、非空',
	productTypeGrade SMALLINT UNSIGNED NOT NULL COMMENT'商品类型的优先级：非空，其为用于类型排序的首要标准',
	isShow SMALLINT DEFAULT 0 NOT NULL COMMENT'是否上架标识：非空, 0/否, 1/是',
	PRIMARY KEY (productTypeId)
)ENGINE=INNODB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT'商品类型表';
INSERT V_ProductType(productTypeName, productTypeGrade) VALUES('主食', 50), ('炒菜', 50), ('小吃', 50), ('汤类', 50);
-- 商品属性类型表
DROP TABLE IF EXISTS V_ProductAttributeType;
CREATE TABLE V_ProductAttributeType (
	productAttributeTypeId INT UNSIGNED UNIQUE NOT NULL AUTO_INCREMENT COMMENT'[默认]商品属性类的编号：唯一、非空',
	productAttributeTypeName VARCHAR(30) UNIQUE NOT NULL COMMENT'商品属性类的名称：唯一、非空',
	PRIMARY KEY (productAttributeTypeId)
)ENGINE=INNODB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT'商品属性类型表';
INSERT V_ProductAttributeType(productAttributeTypeName) VALUES('菜品'),('酒水温度');
-- 商品属性值表
DROP TABLE IF EXISTS V_ProductAttributeValue;
CREATE TABLE V_ProductAttributeValue (
	productAttributeValueId INT UNSIGNED UNIQUE NOT NULL AUTO_INCREMENT COMMENT'[默认]商品属性类型值的编号：唯一、非空',
	productAttributeTypeId INT UNSIGNED NOT NULL COMMENT'所属商品属性类型的编号：非空',
	productAttributeValueName VARCHAR(30) NOT NULL COMMENT'商品属性类型值的名称：非空',
	PRIMARY KEY (productAttributeValueId)
)ENGINE=INNODB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT'商品属性类型值表';
INSERT INTO V_ProductAttributeValue(productAttributeTypeId, productAttributeValueName) VALUES(1, '清淡'), (1, '正常'), (1, '咸辣'), (2, '常温'), (2, '冰冻');
-- 商品图片表
DROP TABLE IF EXISTS V_ProductImg;
CREATE TABLE V_ProductImg (
	productImgId INT UNSIGNED UNIQUE NOT NULL AUTO_INCREMENT COMMENT'[默认]商品图片的编号：唯一、非空',
	productImgName VARCHAR(100) NOT NULL COMMENT'商品图片的名称：非空',
	PRIMARY KEY (productImgId)
)ENGINE=INNODB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT'商品图片表';
-- 商品排序表
DROP TABLE IF EXISTS V_ProductSort;
CREATE TABLE V_ProductSort (
	productSortId INT UNSIGNED UNIQUE NOT NULL AUTO_INCREMENT COMMENT'排序编号',
	productId INT UNSIGNED NOT NULL COMMENT'商品编号',
	productGrade SMALLINT UNSIGNED NOT NULL COMMENT'排序值'
) ENGINE=INNODB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT'商品排序表';
-- 品类排序表
DROP TABLE IF EXISTS V_ProductTypeSort;
CREATE TABLE V_ProductTypeSort (
	productTypeSortId INT UNSIGNED UNIQUE NOT NULL AUTO_INCREMENT COMMENT'排序编号',
	productTypeId INT UNSIGNED NOT NULL COMMENT'品类编号',
	productTypeGrade SMALLINT UNSIGNED NOT NULL COMMENT'排序值'
) ENGINE=INNODB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT'品类排序表';
-- 餐桌表
DROP TABLE IF EXISTS V_DiningRoom;
CREATE TABLE V_DiningRoom (
	diningRoomId INT UNSIGNED UNIQUE NOT NULL AUTO_INCREMENT COMMENT'[默认]餐桌编号：唯一、非空',
	diningRoomName VARCHAR(10) UNIQUE NOT NULL COMMENT'餐桌名称：唯一、非空',
	diningRoomGrade SMALLINT UNSIGNED NOT NULL COMMENT'排序的首要标准',
	diningRoomStatus TINYINT NOT NULL COMMENT'餐桌状态（0/未使用, 1/使用中, 2/清理中）：非空',
	currentBillNumber VARCHAR(30) COMMENT'当前正在进行的账单编号',
	diningRoomInfo VARCHAR(150) COMMENT'就餐位的相关信息',
	PRIMARY KEY (diningRoomId)
)ENGINE=INNODB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT'餐桌表';
INSERT INTO V_DiningRoom(diningRoomName, diningRoomGrade, diningRoomStatus, diningRoomInfo)
VALUES ('一号桌', 0, 0, '标配20人、空调、卫生间');
-- 购物车表（每个餐桌只能有一个购物车）
DROP TABLE IF EXISTS V_ShoppingCart;
CREATE TABLE V_ShoppingCart (
	shoppingCartId INT UNSIGNED UNIQUE NOT NULL AUTO_INCREMENT COMMENT'[默认]购物车编号：唯一、非空',
	shoppingCartOwnerId INT UNSIGNED UNIQUE NOT NULL COMMENT'购物车拥有者编号：非空',
	shoppingCartStatus TINYINT NOT NULL DEFAULT 0 COMMENT'购物车状态（0/使用中, 1/提交中）：非空',
	shoppingCartPresetMinute SMALLINT UNSIGNED DEFAULT 0 NOT NULL COMMENT'购物车的预定时间：非空，其代表顾客希望几分钟后上菜',
	PRIMARY KEY (shoppingCartId)
)ENGINE=INNODB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT'购物车表';
-- 购物车商品条目表
DROP TABLE IF EXISTS V_ShoppingCartItem;
CREATE TABLE V_ShoppingCartItem (
	shoppingCartItemId INT UNSIGNED UNIQUE NOT NULL AUTO_INCREMENT COMMENT'[默认]购物车编号：唯一、非空',
	productId INT UNSIGNED COMMENT'商品编号',
	productImgName VARCHAR(100) NOT NULL COMMENT'商品图片：非空',
	productName VARCHAR(60) NOT NULL COMMENT'商品名称：非空',
	productQuantity SMALLINT UNSIGNED NOT NULL COMMENT'商品数量：非空',
	productRemarks VARCHAR(40) COMMENT'商品项备注',
	PRIMARY KEY (shoppingCartItemId)
)ENGINE=INNODB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT'购物车商品条目表';
-- 账单表
DROP TABLE IF EXISTS V_Bill;
CREATE TABLE V_Bill (
	billId INT UNSIGNED UNIQUE NOT NULL AUTO_INCREMENT COMMENT'[默认]账单ID：唯一、非空',
	billNumber VARCHAR(30) UNIQUE NOT NULL COMMENT'账单编号：唯一、非空',
	billOwnerTypeFlag TINYINT NOT NULL DEFAULT 0 COMMENT'[默认]归属者类型（0/餐桌、1/其它）：非空',
	billOwnerId INT UNSIGNED NOT NULL COMMENT'账单归属者编号：非空',
	billOwnerName VARCHAR(100) COMMENT'账单所属就餐位的名称',
	billCustomerName VARCHAR(50) COMMENT'账单归属的客户名称',
	billCustomerNumber SMALLINT UNSIGNED COMMENT'顾客人数',
	billOrderQuantity SMALLINT UNSIGNED NOT NULL DEFAULT 0 COMMENT'订单总数：非空',
	billTotalAmount DECIMAL(11,1) DEFAULT 0 NOT NULL COMMENT'账单总金额：非空',
	billReceivedAmount DECIMAL(11,1) COMMENT'账单实收金额（null/未结账, 0/零收入账单, 其它/已结账的非零收入账单）',
	billReceivedDateTime DATETIME COMMENT'账单结账时间',
	billRecentHandlerName VARCHAR(20) COMMENT'账单最新的处理人名称：非空',
	billRecentHandlerId INT UNSIGNED COMMENT'账单最新的处理人编号：非空',
	billStartDateTime DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT'账单开始时间：非空',
	billEndDateTime DATETIME COMMENT'账单结束时间',
	billRemarks VARCHAR(100) COMMENT'账单备注',
	updateDateTime DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT'修改时间：非空',
	PRIMARY KEY (billId)
)ENGINE=INNODB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT'账单表';
-- 订单表
DROP TABLE IF EXISTS V_Order;
CREATE TABLE V_Order (
	orderId INT UNSIGNED UNIQUE NOT NULL AUTO_INCREMENT COMMENT'[默认]订单编号：唯一、非空',
	billNumber VARCHAR(30) NOT NULL COMMENT'所属账单编号：唯一、非空',
	orderProductItemQuantity SMALLINT UNSIGNED NOT NULL DEFAULT 0 COMMENT'商品项总数：非空',
	orderTypeFlag TINYINT NOT NULL COMMENT'订单类别标识（0/堂食, 1/打包, 2/外卖）：非空',
	orderPresetTime VARCHAR(30) NOT NULL COMMENT'订单预定时间：非空',
	orderInitiatorFlag TINYINT NOT NULL COMMENT'订单发起人标识（0/商家, 1/顾客）：非空',
	orderCreateDateTime DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT'[默认]订单创建时间：非空',
	PRIMARY KEY (orderId)
)ENGINE=INNODB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT'订单表';
-- 订单详情表/商品条目表
DROP TABLE IF EXISTS V_OrderProductItem;
CREATE TABLE V_OrderProductItem (
	orderProductItemId INT UNSIGNED UNIQUE NOT NULL AUTO_INCREMENT COMMENT'[默认]商品条目编号：唯一、非空',
	orderId INT UNSIGNED NOT NULL COMMENT'所属订单编号',
	ownerId INT UNSIGNED NULL COMMENT'餐桌编号',
	orderProductId INT UNSIGNED COMMENT'商品编号',
	orderProductName VARCHAR(60) NOT NULL COMMENT'商品名称：非空',
	orderProductImg VARCHAR(100) NOT NULL COMMENT'商品图片：非空',
	orderProductQuantity SMALLINT UNSIGNED NOT NULL COMMENT'商品数量：非空',
	orderProductPrice DECIMAL(10,1) NOT NULL COMMENT'商品单价：非空',
	orderProductItemStatusFlag TINYINT NOT NULL DEFAULT 0 COMMENT'商品无效标识（0/下单修改, 1/库存不足, 2/备货中, 3/已完成）：非空',
	orderProductItemStatusDesc VARCHAR(100) COMMENT'商品无效说明',
	orderProductRemarks VARCHAR(40) COMMENT'商品项备注',
	PRIMARY KEY (orderProductItemId)
)ENGINE=INNODB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT'订单详情表';
-- 客户就餐时间类型
DROP TABLE IF EXISTS V_PresetTime;
CREATE TABLE V_PresetTime (
	presetTimeId SMALLINT UNSIGNED UNIQUE NOT NULL AUTO_INCREMENT COMMENT'[默认]类型编号：唯一、非空',
	presetTimeName VARCHAR(20) UNIQUE NOT NULL COMMENT'类型名称：非空',
	presetTimeMinute SMALLINT UNIQUE NOT NULL COMMENT'该类型所代表的分钟值',
	PRIMARY KEY (presetTimeId)
)ENGINE=INNODB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT'客户就餐时间类型';
-- 客户消息类型表
DROP TABLE IF EXISTS V_CustomerMessageType;
CREATE TABLE V_CustomerMessageType (
	customerMessageTypeId INT UNSIGNED UNIQUE NOT NULL AUTO_INCREMENT COMMENT'[默认]客户消息类型编号：唯一、非空',
	customerMessageTypeName VARCHAR(20) UNIQUE NOT NULL COMMENT'消息类型的名称：唯一、非空',
	PRIMARY KEY (customerMessageTypeId)
)ENGINE=INNODB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT'客户消息类型表';
INSERT INTO V_CustomerMessageType(customerMessageTypeName) VALUES('呼叫商家'), ('预开发票'), ('催促上菜');
-- 客户消息表
DROP TABLE IF EXISTS V_CustomerMessage;
CREATE TABLE V_CustomerMessage (
	customerMessageId INT UNSIGNED UNIQUE NOT NULL AUTO_INCREMENT COMMENT'[默认]消息的主键：唯一、非空',
	diningRoomId INT UNSIGNED NOT NULL COMMENT'餐桌编号：非空',
	customerMessageTypeId INT NOT NULL COMMENT'消息类型的编号',
	customerMessageTypeName VARCHAR(20) NOT NULL COMMENT'消息类型的名称',
	customerMessageValue VARCHAR(100) COMMENT'该条消息的值',
	customerMessageStatus TINYINT DEFAULT 0 COMMENT'0/未读 1/已读 2/完成',
	createDateTime DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT'创建时间',
	PRIMARY KEY (customerMessageId)
)ENGINE=INNODB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT'客户消息表';
-- 商家账号表
DROP TABLE IF EXISTS V_MerchantUser;
CREATE TABLE V_MerchantUser (
	merchantUserId INT UNSIGNED UNIQUE NOT NULL AUTO_INCREMENT COMMENT'[默认]账户主键：唯一、非空',
	merchantUserGrade INT UNSIGNED NOT NULL DEFAULT 1 COMMENT'账户等级：非空',
	merchantUserName VARCHAR(20) NOT NULL COMMENT'账户名称：非空',
	merchantUserAccount VARCHAR(10) UNIQUE NOT NULL COMMENT'账户的登录名：唯一、非空',
	merchantUserPassword VARCHAR(20) NOT NULL COMMENT'账户登录密码：非空',
	createDateTime DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT'创建时间',
	lastModifiedDateTime DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT'最近修改时间',
	PRIMARY KEY (merchantUserId)
)ENGINE=INNODB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT'商家账号表';	
-- 就餐位预定表
DROP TABLE IF EXISTS V_DiningRoomReservation;
CREATE TABLE V_DiningRoomReservation (
	reservationId INT UNSIGNED UNIQUE NOT NULL AUTO_INCREMENT COMMENT'[默认]主键',
	diningRoomId INT UNSIGNED NOT NULL COMMENT'就餐位编号',
	customerName VARCHAR(30) NOT NULL COMMENT'顾客名称',
	reservationInfo VARCHAR(200) COMMENT'预定信息',
	diningDate VARCHAR(15) NOT NULL COMMENT'就餐日期(如：2020-01-01)',
	diningTime VARCHAR(5) NOT NULL COMMENT'餐点(如：午餐、晚餐、全天)',
	reservationStatus TINYINT DEFAULT 0 COMMENT'0/正常, 1/标注到就餐位, 4/删除',
	createDateTime DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT'创建时间',
	updateDateTime DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT'修改时间',
	PRIMARY KEY (reservationId)
)ENGINE=INNODB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT'就餐位预定表';
-- 就餐位与访问Key的映射表
DROP TABLE IF EXISTS V_DiningRoomPosition;
CREATE TABLE V_DiningRoomPosition (
	positionId INT UNSIGNED UNIQUE NOT NULL AUTO_INCREMENT COMMENT'[默认]主键',
	diningRoomId INT UNSIGNED NOT NULL COMMENT'就餐位编号',
	positionKey VARCHAR(20) NOT NULL COMMENT'URL映射的key值',
	requestQRCodeImgName VARCHAR(50) COMMENT'对应的二维码图片名称',
	createDateTime DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT'创建时间',
	updateDateTime DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT'修改时间',
	PRIMARY KEY (positionId)
)ENGINE=INNODB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT'就餐位URL映射表';

