# Banten

[![Build Status](https://travis-ci.org/waabox/banten.svg?branch=master)](https://travis-ci.org/waabox/banten)
[![Maven Central](https://img.shields.io/maven-central/v/org.waabox.banten/banten-core.svg)](https://search.maven.org/#search%7Cga%7C1%7Corg.waabox.banten)
[![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](https://opensource.org/licenses/Apache-2.0)

Banten is a modular application framework built on Spring Boot that enables deep transversal cuts of functionality through composable modules.

## Features

- **Module-based architecture** - Compose applications from reusable, self-contained modules
- **Hierarchical contexts** - Each module gets its own isolated Spring MVC context with a shared parent
- **Cross-module communication** - Registry-based APIs for loose coupling between modules
- **Enterprise integrations** - Built-in modules for Hibernate, Shiro, Hazelcast, Elasticsearch, and Camel

## Quick Start

### Maven Dependency

```xml
<dependency>
  <groupId>org.waabox.banten</groupId>
  <artifactId>banten-core</artifactId>
  <version>1.0</version>
</dependency>
```

### Create Your Application

```java
public class MyApplication extends BantenApplication {

  @Override
  protected Bootstrap bootstrap() {
    return new Bootstrap(
      HibernateModule.class,
      WebAppModule.class,
      ShiroModule.class
    );
  }

  @Override
  public void init(final ModuleApiRegistry registry) {
    // Configure modules application-wide
  }

  public static void main(String[] args) {
    SpringApplication.run(MyApplication.class, args);
  }
}
```

## Available Modules

| Module | Artifact | Description |
|--------|----------|-------------|
| Core | `banten-core` | Base framework with module system and context management |
| Web | `banten-web` | Servlet filters, weblets, SiteMesh decorators, menu system |
| Hibernate | `banten-hibernate` | JPA/Hibernate ORM with connection pooling |
| Shiro | `banten-shiro` | Apache Shiro authentication and authorization |
| Login | `banten-login` | Pre-built login and account management |
| Hazelcast | `banten-hazelcast` | Distributed caching and session clustering |
| Elasticsearch | `banten-elasticsearch` | Full-text search with AWS support |
| Camel | `banten-camel` | Apache Camel routing integration |
| Test Support | `banten-test-support` | Testing utilities with EasyMock and HTMLUnit |

## Architecture

Banten creates a two-tier Spring ApplicationContext:

```
┌─────────────────────────────────────┐
│     Parent Context (Shared)         │
│  - Module public configurations     │
│  - Cross-module beans               │
└─────────────────────────────────────┘
          │           │           │
    ┌─────▼───┐ ┌─────▼───┐ ┌─────▼───┐
    │Module A │ │Module B │ │Module C │
    │MVC Ctx  │ │MVC Ctx  │ │MVC Ctx  │
    └─────────┘ └─────────┘ └─────────┘
```

Each `WebModule` receives its own isolated DispatcherServlet and Spring context, while sharing beans through the parent context.

## Creating a Module

```java
public class UserModule extends AbstractModule {

  @Override
  public String getName() {
    return "user";
  }

  @Override
  public Class<?> getPublicConfiguration() {
    return UserConfiguration.class;
  }

  @Override
  public Module getConfigurationApi() {
    return new ConfigurationApi() {
      // Expose configuration for other modules
    };
  }
}
```

For web modules with controllers:

```java
public class UserWebModule extends WebModule {

  @Override
  Class<?> getMvcConfiguration() {
    return UserMvcConfiguration.class;
  }
}
```

## Building from Source

```bash
# Build all modules
mvn clean install

# Build without GPG signing (local development)
mvn clean install -Dgpg.skip=true

# Run the sample application
mvn spring-boot:run -pl banten-sample
```

The sample application demonstrates all modules working together and is available at `http://localhost:8080/` after startup.

## Requirements

- Java 7 or higher
- Maven 3.x

## License

Banten is released under the [Apache License 2.0](https://www.apache.org/licenses/LICENSE-2.0).

## Author

Created by [Emiliano Arango](https://github.com/waabox)
