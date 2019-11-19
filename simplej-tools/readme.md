java项目开发组件，提供如下组件：
1.通用的基础工具，如：流控器、加解密/签名验签、脚本解析工具（freemark/velocity/groovy）、日期工具、xml工具等等
2.通用的代码生成器（simplej-codegen）,通过指定数据库表，反向生成一套完整的spring-boot项目，包含了基本的表增删改查功能，从controller、service到dao
3.支持分表分库功能，通过简单的	@RepositorySharding实现分库功能,更多请看README



更新子模块版本号
mvn versions:set -DnewVersion=1.1
mvn versions:update-child-modules 