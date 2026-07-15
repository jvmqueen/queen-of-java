# Queen Programming Language
<img alt="queenlang-logo" src="http://www.amihaiemil.com/images/queenlang.png" width="100" height="90"/>

[![We recommend IntelliJ IDEA](http://amihaiemil.github.io/images/intellij-idea-recommend.svg)](https://www.jetbrains.com/idea/)
  [![DevOps By Rultor.com](http://www.rultor.com/b/jvmqueen/queen-of-java)](http://www.rultor.com/p/jvmqueen/queen-of-java)

Queen-to-Java Transpiler (Queen made out of Java), version `0.1.0`.

## Why a transpiler?

Given that Queen is so similar in syntax to Java, a transpiler made the most sense for an MVP. Future versions will most likely be directly
compiled into JVM bytecode.

## How to use it?

Download the latest version of ``queen-of-java-x.y.z.jar`` from [here](https://github.com/jvmqueen/queen-of-java/packages) (download the latest from ``Assets``).
Rename it to ``queenc.jar``, just for easier usage. ``queenc`` itself is written in Java 11, therefore you need to have at least Java 11 installed, in order to use it.

To get started, just run:
```shell
$ java -jar queenc.jar

queenc version: x.y.z
usage: queenc
 -cm,--createMaven <arg>   Create a template Maven project, configured for
                           Queen. The argument is the path of the parent
                           dir, which will be automatically prefixed by
                           the Java user.home property. For example, the
                           input /projects/queen-project, will create the
                           project under ~/projects/queen-project.
 -h,--help                 Print this help message.
 -o,--output <arg>         Output path. Defaults to
                           -p/target/generated-sources/queen/java.
 -p,--project <arg>        Path to the parent directory of an existing
                           Queen project directory to compile.
 -v,--version              Print the version of queenc.
```

Don't forget, you can write an alias for ``java -jar queenc.jar`` to shorten it to something like ``queenc -h``.

## Hello World

Put the following code into ``EntryPoint.queen``:

```java
package org.queenlang.helloworld;

/**
 * Entry point of our Queen application.
 * @author amihaiemil (amihaiemil@gmail.com)
 * @version $Id$
 * @since 0.0.1
 */
public final implementation EntryPoint {

    public static void main(String[] args) {
        System.out.println("Queen says Hello World!");
    }
}
```

And execute it like this:

```bash
$ java -jar queenc.jar --project . --output . && java ./org/queenlang/helloworld/EntryPoint.java
$ ... logging from queenc ... 
$ Queen says Hello World!
```

The first command transpiles the Queen project (in this case, only the file ``EntryPoint.queen``) from the current directory into the same directory,
while the second command simply executes the created Java class which is created in a directory structure respecting the declared package (``org.queenlang.helloworld``). 

## Semantic Validation and Symbol Resolution

``queenc`` translates your Queen code into Java. The generated Java code will always be syntactically correct. It will also resolve the imports declared in a file, perform semantic validation and symbol resolution, so any transpilation should ultimately result in Java code which is both syntactically and semantically correct. The syntax of Queen is very similar to Java 8, therefore the generated Java code will always be at least Java 8.

However, keep in mind that this process is not yet fully implemented (we're still working on it). While the generated Java code will always be syntactically correct, it might happen that the Java compiler still outputs some semantic or symbol resolution errors. This complicated validation process is also one of the reasons for choosing to use transpilation in the MVP rather than direct compilation - we want to have the Java compiler as a second safety net until we are sure that the semantic validation is 100% correctly implemented.

## JDK

Queen does not have its own utility library or development kit yet.
Therefore, we still rely on the JDK for things such as ``System``, ``String`` and other utilities from Java SE. 

You can safely use classes from the ``java.lang`` package without having to explicitly import them.
Any other class from the JDK will also work fine - however, if it's not in the ``java.lang`` package, it will have to be imported (for example, classes from the
``java.util`` package).

## Contribute

To develop ``queen-of-java`` you will need at least Java 11 and Maven installed. We recommend IntelliJ IDEA as IDE.

If you found a bug or would simply like to suggest a feature request, just open an Issue here on Github. Always open an Issue before opening any Pull Request. Changes should always be discussed before they are implemented. Always make sure the ``maven`` build passes before opening a PR:

```bash
$ mvn clean install
```

## Maven Settings

This project depends on jars from Queen's Github Packages server. This server
requires authentication so, in order for Maven to be able to fetch dependencies,
you need to specify your credentials in Maven's ``settings.xml``:

```xml
<settings>
    ...
    <servers>
        <server>
            <id>github</id>
            <username>yourGithubUsername</username>
            <password>your_github_token</password>
        </server>
    </servers>
</settings>
```

Make sure you [generate](https://github.com/settings/tokens) a token with the appropriate
permissions. The ``settings.xml`` file usually resides on your computer at ``${user.home}/.m2/settings.xml``.
If the file is not there, you can create it.
