# Banten 

[![Build Status](https://travis-ci.org/waabox/banten.svg?branch=master)](https://travis-ci.org/waabox/banten)

<h1>What is Banten?</h1>

Banten is a modular application Framework, based on Spring-Boot. http://projects.spring.io/spring-boot/

Banten focus in perform deep transversals cuts of funcionality called "Module".

<h1>Creating a new Application with Banten!</h1>

The only thing you need to do, is extend the BantenApplication class, providing the modules that you want to use.
This will create the Spring's Application Context.

```java
  public class SampleApplication extends BantenApplication {
    @Override
    protected Bootstrap bootstrap() {
      return new Bootstrap(
        ModuleA.class,
        ModuleB.class
      );
    }
    
    @Override
    public void init(final ModuleApiRegistry registry) {
      // here you should configure modules "application wide"
    }
    
  }
```
Your pom.xml

Yes, we are in maven central! ( http://search.maven.org/#search%7Cga%7C1%7Ccom.avenida.banten ).

The list of deployed artifacts

```xml

  <dependency>
    <groupId>com.avenida.banten</groupId>
    <artifactId>banten-core</artifactId>
    <version>1.0</version>
  </dependency>
  <dependency>
    <groupId>com.avenida.banten</groupId>
    <artifactId>banten-test-support</artifactId>
    <version>1.0</version>
  </dependency>
  <dependency>
    <groupId>com.avenida.banten</groupId>
    <artifactId>banten-hibernate</artifactId>
    <version>1.0</version>
  </dependency>
  <dependency>
    <groupId>com.avenida.banten</groupId>
    <artifactId>banten-hazelcast</artifactId>
    <version>1.0</version>
  </dependency>
  <dependency>
    <groupId>com.avenida.banten</groupId>
    <artifactId>banten-elasticsearch</artifactId>
    <version>1.0</version>
  </dependency>
  <dependency>
    <groupId>com.avenida.banten</groupId>
    <artifactId>banten-camel</artifactId>
    <version>1.0</version>
  </dependency>
  <dependency>
    <groupId>com.avenida.banten</groupId>
    <artifactId>banten-shiro</artifactId>
    <version>1.0</version>
  </dependency>
  <dependency>
    <groupId>com.avenida.banten</groupId>
    <artifactId>banten-web</artifactId>
    <version>1.0</version>
  </dependency>
  <dependency>
    <groupId>com.avenida.banten</groupId>
    <artifactId>banten-login</artifactId>
    <version>1.0</version>
  </dependency>

```
