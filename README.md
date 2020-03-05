# Using Spring in A Server-Side Function on PCC

DISCLAIMER: This document only describes the current workflow for using Spring within PCC and does not necessarily
reflect how this process may change in future releases of PCC or Spring.

## Guide

Using Spring in a server-side function in PCC is possible, but certain aspects may seem unintuitive and there are limitations. 

One source of issues is that dependencies on PCC's classpath will be visible to your code. PCC itself uses Spring on the
server, so if your function uses a different version of Spring than PCC has on its classpath, there may be conflicts.
You may have conflicts involving more than just Spring; logging implementations or any other dependencies you use may
conflict with those provided by PCC. To solve this issue, you should develop against the same version of Spring that PCC
uses and do not bundle your Spring dependencies in your jar; this will allow your application to pick up and use the
Spring version already on PCC's classpath. `In general, it is easier to bundle as few dependencies as possible; you
should onlybundle dependencies that are not already on the PCC classpath.` You can see what is on the classpath using
the gfsh command `status server --name=XYZ` where XYZ is replaced with your server's name.

### Recommended Way to Start a Spring ApplicationContext

This is the simplest way to create a Spring ApplicationContext and inject beans on a PCC server. It will allow you to
use Autowiring and maintain a single ApplicationContext across multiple calls that can be shared between functions.
However, the ApplicationContext cannot be shared between jars.
1.	1.	Ensure that your function class implements org.apache.geode.cache.execute.Function and that all classes that
define beans, including your function class, extend LazyWiringDeclarableSupport from Spring Data GemFire. Not extending
this class will result in your beans not being registered with the ApplicationContext. 
2.	Annotate your configuration class as you want it. This can be your function class or any other.
3.	Inside your function’s `execute()` method, register your configuration class with
`SpringContextBootstrappingInitializer`, set the bean ClassLoader to the same ClassLoader as your configuration class,
and called init() on a new instance of SpringContextBootstrappingInitializer with a new `Properties` object. This can be
accomplished with the below code:
        ```
        SpringContextBootstrappingInitializer.register(MyConfig.class);
        SpringContextBootstrappingInitializer.setBeanClassLoader(MyConfig.class.getClassLoader());
        new SpringContextBootstrappingInitializer().init(new Properties()); 
      ```
4.	Build your jar bundling spring-data-gemfire inside. Unfortunately, the version of Spring Data GemFire that is based
off of the correct Spring version for PCC is incompatible with the current version of PCC itself and, as such, using it
will yield the following exception: `java.lang.NoClassDefFoundError: com/gemstone/gemfire/cache/Declarable`. To resolve
this, you will need to use a newer version of Spring Data GemFire; at the time of writing this, using Spring Data
GemFire 2.0.0.RELEASE works. This may change in future releases of PCC.
5.	You may now access the Spring `ApplicationContext` with `SpringContextBootstrappingInitializer.getApplicationContext()`
and beans should be created and injected as expected.
6.	Note that multiple calls to the function initializing the ApplicationContext will result in an exception:
`java.lang.IllegalStateException: A Spring ApplicationContext has already been initialized`. Therefore, it is advisable
to have a separate function specifically for initializing the `ApplicationContext`.

### Other Ways to Use Spring

The above method is the most convenient and least limited way to use Spring on a PCC server, however, there are other
ways. The below sections are effectively made obsolete by the above method but may still be of use in some cases. Not
using the recommended method has at least one major drawback; Autowiring is not supported. 

#### Using a Spring Utility

The simplest Spring use case is to use some utility class from Spring that does not require an `ApplicationContext`.
This can be done easily as long as the utility you want is part of a dependency on PCC's classpath. All you have to do is
use the utility as you normally would, and when building your jar, do not embed dependencies already on the classpath of
the PCC server. If the utility you want to use comes from a Spring library not present on PCC, such as SpringUtils from
the Spring Data GemFire project, you may still use it, but you will need to pack the necessary dependency into your jar.

#### Starting a Spring ApplicationContext

A more interesting use case is to create an `ApplicationContext` and define beans. To do this, annotate your function
class (or whatever class you want to act as your configuration) with `@Configuration` and create your context in the
`execute()` method, passing in your configuration class. If you have beans defined outside of your configuration class,
you may need to enable `@ComponentScan` and point it at the package(s) containing your bean definitions. Due to how
class loading works inside PCC, you may get an error saying that your configuration class cannot be found. To solve
this issue, override getClassLoader() on the ApplicationContext and return the ClassLoader used to load your
configuration class, like this:
```
context = new AnnotationConfigApplicationContext(MyConfiguration.class) {
   @Override
   public ClassLoader getClassLoader() {
      return MyConfiguration.class.getClassLoader();
   }
};
```
Now you can use your `ApplicationContext`! Unfortunately, injecting beans using `@Autowired` or `@Resource` doesn’t
work; you will have to access beans directly from the context by doing something like: `context.getBean(“BeanName”)`.
Beans are still injected into other, dependent, beans.If you want to use Autowiring, use the recommended method
described earlier in this document.

#### Using Spring Boot

You may wish to use Spring Boot, which is possible. First, you will have to use a version of Spring Boot that is
compatible with the version of Spring on PCC. Annotate your function class (or whatever class you want to hold your
configuration) with `@SpringBootApplication`, then create your context using `SpringApplication` or
`SpringApplicationBuilder` and passing in your configuration class; now you have an `ApplicationContext` without
having to mess with ClassLoaders. Because PCC does not have Spring Boot on the classpath, you will have to pack it into
your jar. You may get an error like “No auto configuration classes found in META-INF/spring.factories. If you are using
a custom packaging, make sure that file is correct”. The solution may seem counter-intuitive; remove
spring-boot-autoconfigure from your jar. As mentioned before, make sure to use a version of Spring Boot that uses the
same Spring Framework version as PCC has on its classpath, and only bundle the dependencies that you need that PCC
doesn’t already have on its classpath, as redefining a dependency with a different version than PCC uses may cause
conflicts. You can see what is on the classpath using the gfsh command `status server --name=XYZ` where XYZ is replaced
with your server's name.

#### Persisting an ApplicationContext Across Function Calls and Sharing it Between Functions

The best way to do this is using the recommended method of starting an `ApplicationContext`, but if you want to store
the `ApplicationContext` yourself, you can achieve this by storing your context in a static variable either in your
function class or in a separate holder class.

#### Sharing an ApplicationContext Between Jars

Because of how class loading works in PCC, you cannot share a single `ApplicationContext` between multiple jars reliably.
The jars will see different instances of the ApplicationContext. Attempting to share beans between multiple jars may
appear to work under some circumstances, but this may introduce several strange issues that make the results unpredictable.
As such, it is recommended to keep all server-side code that needs to access the same beans in a single jar file.

### Limitations

*	You must use the same version of Spring as PCC has on the classpath. You can see what is on the classpath using the
gfsh command `status server --name=XYZ` where XYZ is replaced with your server's name.
*	Multiple jars cannot properly share a single `ApplicationContext`.
*	Autowiring only works when using the recommended method.

### Example Code

This example project consists of three modules: function1, function2, and holder.

* function1 uses `SpringContextBootstrappingInitializer` to create and persist an `ApplicationContext`. 
* function2 uses a bean declared in the configuration class.
* holder is only relevant if using one of the non-recommended methods. It will create and store an `ApplicatioContext`.
holder has code for using Spring with and without Spring Boot; currently, the Spring Boot code is commented out. The
Spring Boot dependency is also commented out in the pom.xml.
 
