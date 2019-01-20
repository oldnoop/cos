## 介绍

* 腾讯云存储COS API的spring/springboot 支持,提供临时密钥解决方案方便使用,按系统用户id设计上传下载权限,可使用默认设计,按用户id限定上传文件夹,下载不限
* 支持单独使用spring配置
* 支持使用springboot配置并提供起步依赖和自动配置,引入依赖即可
* 提供示例代码供参考

## 源代码说明
- 下载 git clone https://github.com/oldnoop/cos.git
- cos-spring 实现spring的集成
- cos-spring-boot-autoconfigure 实现springboot的自动配置
- cos-spring-boot-starter 提供springboot的起步依赖
- cos-spring-boot-example 提供springboot集成的使用示例

## 使用

### 单独使用spring

#### 引入依赖
```POM.XML
<dependency>
  <groupId>com.oldnoop</groupId>
  <artifactId>cos-spring</artifactId>
  <version>1.1.0</version>
  <!--如有log的jar冲突,排除-->
  <!--
  <exclusions>
    <exclusion>
      <groupId>org.slf4j</groupId>
      <artifactId>slf4j-log4j12</artifactId>
    </exclusion>
  </exclusions>
  -->
</dependency>
```

#### 编写资源配置文件
coskey.properties
参考cos-spring的jar包中的/META-INF/cos/coskey.properties

#### 配置servlet
例如web.xml中的配置
```web.xml
<servlet>
	<servlet-name>cos-servlet</servlet-name>
	<servlet-class>com.oldnoop.cos.tmpkey.CosTmpKeyServlet</servlet-class>
	<!-- 配置文件,默认为classpath:coskey.properties或coskey.xml,该方式优先 -->
	<init-param>
		<param-name>cosConfigLocation</param-name>
		<param-value>classpath:coskey.properties</param-value>
	</init-param>
</servlet>
<servlet-mapping>
	<servlet-name>cos-servlet</servlet-name>
	<!--临时密钥的请求路径 -->
	<url-pattern>/coskey</url-pattern>
</servlet-mapping>
```

### 使用springboot

#### 引入依赖
```POM.XML
<!-- 引入cos的起步依赖 -->
<dependency>
	<groupId>com.oldnoop</groupId>
	<artifactId>cos-spring-boot-starter</artifactId>
	<version>1.1.0</version>
</dependency>
```

#### 编写资源配置文件
参考cos-spring-boot-example中的src/main/resources/application.properties
