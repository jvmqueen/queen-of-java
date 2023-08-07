# Queen Programming Language
<img alt="queenlang-logo" src="http://www.amihaiemil.com/images/queenlang.png" width="100" height="90"/>

[![Managed By Self XDSD](https://self-xdsd.com/b/mbself.svg)](https://self-xdsd.com/p/jvmqueen/queen-of-java?provider=github) 
[![DevOps By Rultor.com](http://www.rultor.com/b/jvmqueen/queen-of-java)](http://www.rultor.com/p/jvmqueen/queen-of-java)
[![We recommend IntelliJ IDEA](http://amihaiemil.github.io/images/intellij-idea-recommend.svg)](https://www.jetbrains.com/idea/)

Queen-to-Java Transpiler (Queen made out of Java), version ``not-yet-released``.

## Why a transpiler?

Given that Queen is so similar in syntax to Java, a transpiler made the most sense for an MVP. Future versions will most likely be directly
compiled into JVM bytecode.

## How to use it?

Download the latest version of ``queenc`` from [here](https://github.com/jvmqueen/queen-of-java/packages) (download the latest from ``Assets``).
``queenc`` itself is written in Java 11, therefore you need to have at least Java 11 installed, in order to use it.

To print ``help``:

```bash
$ java -jar queenc.jar -h
usage: queenc
 -cp,--classpath <arg>   Classpath(s) to look for user-defined classes.
                         Defaults to current dir.
 -f,--files <arg>        Queen files to transpile.
 -h,--help               Print help message.
 -o,--output <arg>       Output path. Defaults to current dir.
 -v,--version            Print the version of queenc.
 -verbose                Print verbose logs.
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
$ java -jar queenc.jar -f EntryPoint.queen && java ./org/queenlang/helloworld/EntryPoint.java
$ Queen says Hello World!
```

The first command transpiles the Queen file into a Java file, while the second command simply executes it. 

## Semantic Validation and Symbol Resolution

``queenc`` translates your Queen code into Java. The generated Java code will always be syntactically correct. It will also resolve the imports declared in a file, perform semantic validation and symbol resolution, so any transpilation should ultimately result in Java code which is both syntactically and semantically correct. The syntax of Queen starts from Java 8, therefore the generated Java code will always be at least Java 8.

However, keep in mind that this process is not yet fully implemented (we're still working on it). While the generated Java code will always be syntactically correct, it might happen that the Java compiler still outputs some semantic or symbol resolution errors. This complicated validation process is also one of the reasons for choosing to use transpilation in the MVP rather than direct compilation - we want to have the Java compiler as a second safety net until we are sure that the semantic validation is 100% correctly implemented.

## JDK

Queen does not have its own utility library or development kit yet.
Therefore, we still rely on the JDK for things such as ``System``, ``String`` and other utilities from Java SE. 

You can safely use classes from the ``java.lang`` package without having to explicitly import them.
Any other class from the JDK will also work fine - however, if it's not in the ``java.lang`` package, it will have to be imported, for example classes from the
``java.util`` package.

## Contribute

To develop ``queen-of-java`` you will need at least Java 11 and Maven installed. We recommend IntelliJ IDEA as IDE.

If you found a bug or would simply like to suggest a feature request, just open an Issue here on Github. Always open an Issue before opening any Pull Request. Changes should always be discussed before they are implemented. Always make sure the ``maven`` build passes before opening a PR:

```bash
$ mvn clean install
```
