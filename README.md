# TERASOLUNA Server Framework for Java (5.x) Common Library

The common library of TERASOLUNA Server Framework for Java (5.x) is a library of useful and unobtrusive common functionalities.

[![Build Status](https://travis-ci.org/terasolunaorg/terasoluna-gfw.svg?branch=5.4.x)](https://travis-ci.org/terasolunaorg/terasoluna-gfw)
[![Coverage Status](https://coveralls.io/repos/terasolunaorg/terasoluna-gfw/badge.svg?branch=5.4.x&service=github)](https://coveralls.io/github/terasolunaorg/terasoluna-gfw?branch=5.4.x)
[![License](http://img.shields.io/:license-apache-brightgreen.svg)](http://www.apache.org/licenses/LICENSE-2.0.html)

## Projects of Common Library

All modules included in TERASOLUNA Server Framework for Java (5.x) Common Library are listed up in the [development guideline](https://github.com/terasolunaorg/guideline/blob/5.4.x/source_en/Overview/FrameworkStack.rst#building-blocks-of-common-library).

## Getting Started

### Using as a part of Template Blank Project

In order to start using the common libaries, start with downloading Template Blank Project. Blank project already contains dependecies defined for TERASOLUNA Server Framework for Java (5.x) Common Library. Template Blank Projects are available from the following links:

* https://github.com/terasolunaorg/terasoluna-gfw-web-multi-blank (Recommended)
* https://github.com/terasolunaorg/terasoluna-gfw-web-blank

### Using maven

Common Library can be downloaded using maven through the following settings in pom.xml file. 

Two types of settings are possible. 

1. Using parent
2. Without using parent

Irrespective of above two ways, first define the repositories in the pom file.

(The below is required in the both the type of settings.)

``` xml
<repositories>
    <repository>
        <releases>
            <enabled>true</enabled>
        </releases>
        <snapshots>
            <enabled>false</enabled>
        </snapshots>
        <id>terasoluna-gfw-releases</id>
        <url>http://repo.terasoluna.org/nexus/content/repositories/terasoluna-gfw-releases/</url>
    </repository>
</repositories>
```

#### Using parent

Define parent project in pom file

``` xml
<parent>
  <groupId>org.terasoluna.gfw</groupId>
  <artifactId>terasoluna-gfw-parent</artifactId>
  <version>5.4.1.RELEASE</version>
</parent>
```

After adding above, add the following dependency definitions. 
(Only the required ones.)

``` xml
<dependency>
    <groupId>org.terasoluna.gfw</groupId>
    <artifactId>terasoluna-gfw-web-dependencies</artifactId>
    <type>pom</type>
</dependency>

<!-- OPTIONAL -->
<dependency>
    <groupId>org.terasoluna.gfw</groupId>
    <artifactId>terasoluna-gfw-web-jsp-dependencies</artifactId>
    <type>pom</type>
</dependency>

<!-- OPTIONAL -->
<dependency>
    <groupId>org.terasoluna.gfw</groupId>
    <artifactId>terasoluna-gfw-jodatime-dependencies</artifactId>
    <type>pom</type>
</dependency>

<!-- OPTIONAL -->
<dependency>
    <groupId>org.terasoluna.gfw</groupId>
    <artifactId>terasoluna-gfw-string</artifactId>
</dependency>

<!-- OPTIONAL -->
<dependency>
    <groupId>org.terasoluna.gfw</groupId>
    <artifactId>terasoluna-gfw-codepoints</artifactId>
</dependency>

<!-- OPTIONAL -->
<dependency>
    <groupId>org.terasoluna.gfw.codepoints</groupId>
    <artifactId>terasoluna-gfw-codepoints-jisx0201</artifactId>
</dependency>

<!-- OPTIONAL -->
<dependency>
    <groupId>org.terasoluna.gfw.codepoints</groupId>
    <artifactId>terasoluna-gfw-codepoints-jisx0208</artifactId>
</dependency>

<!-- OPTIONAL -->
<dependency>
    <groupId>org.terasoluna.gfw.codepoints</groupId>
    <artifactId>terasoluna-gfw-codepoints-jisx0208kanji</artifactId>
</dependency>

<!-- OPTIONAL -->
<dependency>
    <groupId>org.terasoluna.gfw.codepoints</groupId>
    <artifactId>terasoluna-gfw-codepoints-jisx0213kanji</artifactId>
</dependency>

<!-- OPTIONAL -->
<dependency>
    <groupId>org.terasoluna.gfw</groupId>
    <artifactId>terasoluna-gfw-validator</artifactId>
</dependency>

<!-- OPTIONAL -->
<dependency>
    <groupId>org.terasoluna.gfw</groupId>
    <artifactId>terasoluna-gfw-recommended-dependencies</artifactId>
    <type>pom</type>
</dependency>

<!-- OPTIONAL -->
<dependency>
    <groupId>org.terasoluna.gfw</groupId>
    <artifactId>terasoluna-gfw-recommended-web-dependencies</artifactId>
    <type>pom</type>
</dependency>

<!-- If Spring Security is to be used -->
<dependency>
    <groupId>org.terasoluna.gfw</groupId>
    <artifactId>terasoluna-gfw-security-web-dependencies</artifactId>
    <type>pom</type>
</dependency>
<dependency>
    <groupId>org.terasoluna.gfw</groupId>
    <artifactId>terasoluna-gfw-security-core-dependencies</artifactId>
    <type>pom</type>
</dependency>

<!-- If MyBatis3 is to be used -->
<dependency>
    <groupId>org.terasoluna.gfw</groupId>
    <artifactId>terasoluna-gfw-mybatis3-dependencies</artifactId>
    <type>pom</type>
</dependency>

<!-- If JPA is to be used -->
<dependency>
    <groupId>org.terasoluna.gfw</groupId>
    <artifactId>terasoluna-gfw-jpa-dependencies</artifactId>
    <type>pom</type>
</dependency>
```

There is no need of settings related to version. 

Plugins are also already set in parent.

#### Without using parent

Add the following dependency definitions. 

(Only the required ones.)

``` xml
<dependency>
    <groupId>org.terasoluna.gfw</groupId>
    <artifactId>terasoluna-gfw-web-dependencies</artifactId>
    <version>5.4.1.RELEASE</version>
    <type>pom</type>
</dependency>

<!-- OPTIONAL -->
<dependency>
    <groupId>org.terasoluna.gfw</groupId>
    <artifactId>terasoluna-gfw-web-jsp-dependencies</artifactId>
    <version>5.4.1.RELEASE</version>
    <type>pom</type>
</dependency>

<!-- OPTIONAL -->
<dependency>
    <groupId>org.terasoluna.gfw</groupId>
    <artifactId>terasoluna-gfw-jodatime-dependencies</artifactId>
    <version>5.4.1.RELEASE</version>
    <type>pom</type>
</dependency>

<!-- OPTIONAL -->
<dependency>
    <groupId>org.terasoluna.gfw</groupId>
    <artifactId>terasoluna-gfw-string</artifactId>
    <version>5.4.1.RELEASE</version>
</dependency>

<!-- OPTIONAL -->
<dependency>
    <groupId>org.terasoluna.gfw</groupId>
    <artifactId>terasoluna-gfw-codepoints</artifactId>
    <version>5.4.1.RELEASE</version>
</dependency>

<!-- OPTIONAL -->
<dependency>
    <groupId>org.terasoluna.gfw.codepoints</groupId>
    <artifactId>terasoluna-gfw-codepoints-jisx0201</artifactId>
    <version>5.4.1.RELEASE</version>
</dependency>

<!-- OPTIONAL -->
<dependency>
    <groupId>org.terasoluna.gfw.codepoints</groupId>
    <artifactId>terasoluna-gfw-codepoints-jisx0208</artifactId>
    <version>5.4.1.RELEASE</version>
</dependency>

<!-- OPTIONAL -->
<dependency>
    <groupId>org.terasoluna.gfw.codepoints</groupId>
    <artifactId>terasoluna-gfw-codepoints-jisx0208kanji</artifactId>
    <version>5.4.1.RELEASE</version>
</dependency>

<!-- OPTIONAL -->
<dependency>
    <groupId>org.terasoluna.gfw.codepoints</groupId>
    <artifactId>terasoluna-gfw-codepoints-jisx0213kanji</artifactId>
    <version>5.4.1.RELEASE</version>
</dependency>

<!-- OPTIONAL -->
<dependency>
    <groupId>org.terasoluna.gfw</groupId>
    <artifactId>terasoluna-gfw-validator</artifactId>
    <version>5.4.1.RELEASE</version>
</dependency>

<!-- OPTIONAL -->
<dependency>
    <groupId>org.terasoluna.gfw</groupId>
    <artifactId>terasoluna-gfw-recommended-dependencies</artifactId>
    <version>5.4.1.RELEASE</version>
    <type>pom</type>
</dependency>

<!-- OPTIONAL -->
<dependency>
    <groupId>org.terasoluna.gfw</groupId>
    <artifactId>terasoluna-gfw-recommended-web-dependencies</artifactId>
    <version>5.4.1.RELEASE</version>
    <type>pom</type>
</dependency>

<!-- If Spring Security is to be used -->
<dependency>
    <groupId>org.terasoluna.gfw</groupId>
    <artifactId>terasoluna-gfw-security-web-dependencies</artifactId>
    <version>5.4.1.RELEASE</version>
    <type>pom</type>
</dependency>
<dependency>
    <groupId>org.terasoluna.gfw</groupId>
    <artifactId>terasoluna-gfw-security-core-dependencies</artifactId>
    <version>5.4.1.RELEASE</version>
    <type>pom</type>
</dependency>

<!-- If MyBatis3 is to be used -->
<dependency>
    <groupId>org.terasoluna.gfw</groupId>
    <artifactId>terasoluna-gfw-mybatis3-dependencies</artifactId>
    <version>5.4.1.RELEASE</version>
    <type>pom</type>
</dependency>

<!-- If JPA is to be used -->
<dependency>
    <groupId>org.terasoluna.gfw</groupId>
    <artifactId>terasoluna-gfw-jpa-dependencies</artifactId>
    <version>5.4.1.RELEASE</version>
    <type>pom</type>
</dependency>
```

## How to contribute

**Contributing (bug report, pull request, any comments etc.) is welcome !!** Please see the [contributing guideline](CONTRIBUTING.md) for details.



## TERASOLUNA Global Framework

The common library of TERASOLUNA Global Framework is maintained under [1.0.x](https://github.com/terasolunaorg/terasoluna-gfw/tree/1.0.x) branch.

## License
The TERASOLUNA Server Framework for Java (5.x) is released under Apache  License, Version 2.0.
