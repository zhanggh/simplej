<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xmlns:tx="http://www.springframework.org/schema/tx"
       xsi:schemaLocation="http://www.springframework.org/schema/beans 
                        http://www.springframework.org/schema/beans/spring-beans.xsd 
                        http://www.springframework.org/schema/tx
                        http://www.springframework.org/schema/tx/spring-tx.xsd
                        ">


    <!-- 数据源 -->
    <bean id="sqlSessionFactory" class="org.mybatis.spring.SqlSessionFactoryBean">
        <property name="dataSource" ref="dataSource"/>
        <property name="mapperLocations" value="classpath*:/mappers/**/*.xml"/>
        <property name="plugins">
            <array>
                <bean id="mybatisSqlInterceptor" class="com.haven.simplej.db.sql.interceptor.MybatisSqlInterceptor"/>
            </array>
        </property>
    </bean>

    <bean class="org.mybatis.spring.mapper.MapperScannerConfigurer">
        <property name="basePackage" value="${package}.${business}.mapper"/>
        <property name="sqlSessionFactoryBeanName" value="sqlSessionFactory"/>
        <property name="annotationClass" value="org.springframework.stereotype.Repository" />
    </bean>

    <bean id="sqlSession" class="org.mybatis.spring.SqlSessionTemplate"  scope="prototype">
        <constructor-arg index="0" ref="sqlSessionFactory" />
    </bean>

    <!-- transaction support, spring boot 工程已经自动开启事务注解了
    <bean id="transactionManager" class="org.springframework.jdbc.datasource.DataSourceTransactionManager">
        <property name="dataSource" ref="dataSource" />
    </bean>
    <tx:annotation-driven transaction-manager="transactionManager" />
    -->
</beans>
