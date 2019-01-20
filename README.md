# cos

## 介绍
---
- 腾讯云存储COS API的spring/springboot 支持,提供临时密钥解决方案方便使用,按系统用户id设计上传下载权限,可使用默认设计,按用户id限定上传文件夹,下载不限
- 支持单独使用spring配置
- 支持使用springboot配置并提供起步依赖和自动配置,引入依赖即可
- 提供示例代码供参考

## 源代码说明
---
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

#### 编程式配置
```JAVA
@Configuration
class CosConfigKeyPolicyDefault {

    @Bean
    public CosServerProperties cosServerProperties() {
        CosServerProperties serverProperties = new CosServerProperties();
        //读取配置属性,设置属性
        return serverProperties;
    }
    
    @Bean
    public CosTmpKeyProperties cosTmpKeyProperties(CosServerProperties cosServerProperties){
        CosTmpKeyProperties cosTmpKeyProperties = new CosTmpKeyProperties();
        cosTmpKeyProperties.setServerProperties(cosServerProperties);
        //读取配置属性,设置属性
        return cosTmpKeyProperties;
    }
    
    @Bean
    public CosTmpTokenCreator tokenCreator(CosTmpKeyProperties keyProperties){
        CosTmpTokenCreator tokenCreator = new CosTmpTokenCreator();
        tokenCreator.setKeyProperties(keyProperties);
        return tokenCreator;
    }
    
    @Bean
    public ServletRegistrationBean cosTmpKeyServlet(CosTmpTokenCreator creator) {
		  CosTmpKeyServlet cosTmpKeyServlet = new CosTmpKeyServlet();
		  cosTmpKeyServlet.setCreator(creator);
		  return new ServletRegistrationBean(cosTmpKeyServlet, creator.getServerInfo().getKeyApplyPath());
    }

}
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
