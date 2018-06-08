# MybatisGenerator

自定义注释生成器，将数据库字段备注写入生成的实体类中。

## 用法：
1. 配置 `src/main/resources/ generatorConfig.properties`
```
mybatis.generator.outputDirectory=target
## 是否删除之前生成的代码
delete.previous=true
## 上一次生成包的根路径
previous.package.firstName=com

## -------------- 数据库连接 --------------##
mybatis.generator.jdbcURL=jdbc:oracle:thin:@10.211.55.3:1521:ORCL
mybatis.generator.jdbcDriver=oracle.jdbc.driver.OracleDriver
mybatis.generator.jdbcUserId=xx
mybatis.generator.jdbcPassword=xx

## -------------- 输出包 --------------##
## 基础包
package.base=com.zuoban.xx
## dao 包名
package.suffix.dao=dao
## mapper.xml 包名
package.suffix.mapper=mapper
## entity 包名
package.suffix.entity=entity

## -------------- 要生成的表名多个用,分隔 --------------##
mybatis.generator.tableName= table1, table2

# 是否使用lombok 插件， 若为true 生成的实体类会添加@Data注解并去除getter,setter
lombok=true
```

2. 运行 `main/java/App.java`
