# Spigot's Classloader Hell

### Description

There is a libraryL, both plugins need this library to function.

PluginA exposes an API which includes a method with a parameter type of libraryL.

PluginB has a soft-dependency on pluginA and uses its API to call the method.
If pluginA is disabled, pluginB will do something else that also requires libraryL.

### Gradle

The gradle build tool is used to define depenencies for every java project.

#### pluginB-bukkit
Spigot and pluginA are available on the classpath when the server runs, 
therefore they are declared as `compileOnly` which makes them only required at compile time.

#### pluginA-bukkit & pluginB-bukkit
LibraryL must be shaded both in pluginA and pluginB because they require this and need to work standalone.
This dependency is therefore declared as `implementation` which makes it available at both compile time and run time.

#### pluginA-api
The pluginA-api declares its libraryL dependency as `api` from the java-libary gradle plugin. 
This will make any project that depends on pluginA-api also depend on libraryL, which is a recommended practice.
This also means that pluginA-api can be published to a repository without any form of shading.

### Without relocation

Compiling both plugins **without** relocation and running them on the same server will produce the following error.

```
java.lang.LinkageError: loader constraint violation: 
loader 'pluginA-1.0-SNAPSHOT.jar' @beef4a wants to load class com.jorisg.classloaderhell.library.LibraryObject. 
A different class with the same name was previously loaded by 'pluginB-1.0-SNAPSHOT.jar' @4f9e4b1b. 
(
com.jorisg.classloaderhell.library.LibraryObject is in unnamed module of loader 'pluginB-1.0-SNAPSHOT.jar' @4f9e4b1b, 
parent loader java.net.URLClassLoader @3b764bce
)
```

As you can see, the same class is loaded by two different classloaders (and also from different locations, its respective jar).
Unfortunately, although these classes are identical, they cannot be cast to each other.

### With relocation

Compiling both plugins **with** relocation and running them on the same server will produce the following error.

```
java.lang.NoSuchMethodError: 'void com.jorisg.classloaderhell.plugina.api.PluginAManager.soSomething(com.jorisg.classloaderhell.pluginB.library.LibraryObject)'
```

Because of the relocation, pluginA requires the parameter type to be `com.jorisg.classloaderhell.pluginA.library.LibraryObject`
but it receives the type `com.jorisg.classloaderhell.pluginB.library.LibraryObject`.

