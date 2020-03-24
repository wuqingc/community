## Spring boot 交流社区

### 项目简介

参照 **码问社区** 开发的一个springboot论坛,主要是为了练手.

采用springboot + mybatis,数据库采用H2.

### 项目资料

* [快速搭建](https://spring.io/guides/gs/serving-web-content/)
* [spring参考文档](https://docs.spring.io/spring/docs/5.2.5.BUILD-SNAPSHOT/spring-framework-reference/web.html#spring-web)
* [springboot参考文档](https://docs.spring.io/spring-boot/docs/current/reference/htmlsingle/#boot-documentation)
* [thymeleaf参考文档](https://www.thymeleaf.org/doc/tutorials/3.0/usingthymeleaf.html#dialects-the-standard-dialect)
* [bootstrap组件库](https://v3.bootcss.com/components/#media)
* [maven仓库](https://mvnrepository.com/)

### 工具集合

* [github授权流程](https://developer.github.com/apps/building-oauth-apps/authorizing-oauth-apps/)
* [okhttp](https://square.github.io/okhttp/#license) : 用于github登录验证时发起get/post请求.
* [visual paradigm](https://www.visual-paradigm.com/cn/):绘制需要的类图,时序图
* [flyway管理数据库迁移](https://flywaydb.org/getstarted/firststeps/maven)
* [Lombok工具集成](https://projectlombok.org/features/all)
* [commons-lang3](https://mvnrepository.com/artifact/org.apache.commons/commons-lang3/3.9)

### 数据库

* [H2数据库快速搭建](https://www.h2database.com/html/quickstart.html)
* [集成mybatis](http://mybatis.org/spring-boot-starter/mybatis-spring-boot-autoconfigure/)

### 脚本

* mvn -Dmybatis.generator.overwrite=true mybatis-generator:generate
* mvn flyway:migrate

