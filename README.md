# TERASOLUNA Server Framework for Java (5.x) Common Library

The common library of TERASOLUNA Server Framework for Java (5.x) is a library of useful and obstrusive common functionalities.

[![Build Status](https://travis-ci.org/terasolunaorg/terasoluna-gfw.png?branch=master)](https://travis-ci.org/terasolunaorg/terasoluna-gfw)

## Projects of Common Library

The following project are included in TERASOLUNA Server Framework for Java (5.x) Common Library.

<table>
  <tr>
    <th>SR. NO.</th><th>Project Name</th><th>Description</th><th>Contains java code</th><th>Functionalities</th>
  </tr>
  <tr>
    <td>1</td>
    <td>terasoluna-gfw-common</td>
    <td>Functionality that can be used in general and not just related to the web</td>
    <td>Yes</td>
    <td>
    <pre>
* Comprehensive Common Exception Handling Mechanism
  * General exception class (designed as per the needs of this mechanism)
  * Exception Logger
  * Exception Codes
  * Interceptor to output exception log
* Improvised System Time handling mechanism (using java.util.Date, java.sql.Date, java.sql.Time, java.sql.Timestamp)
* Codelist functionality
* Improvised Message handling mechasim
* Query excaping utilities for SQL, JPQL
* Sequencer classes
     </pre>
    </td>
  </tr>
  <tr>
    <td>2</td><td>terasoluna-gfw-jodatime</td><td>Maven dependency definition and functionality using Joda Time</td><td>Yes</td><td><pre>* Improvised System Time handling mechanism (using org.joda.time.DateTime)</pre></td>
  </tr>
  <tr>
    <td>3</td>
    <td>terasoluna-gfw-web</td><td>Functionalities that will be useful while developing a web application</td>
    <td>Yes</td>
    <td>
    <pre>
* Transaction Token Mechanism (Mechanism to prevent double submit)
* Common Exception Handler
* Interceptor to load Codelist
* Download View
* Group of Servlet filters to output log of information in MDC
  * Servlet filter parent class
  * Servlet filter to output tracking Id
  * Servlet filter to clear MDC
* Group of EL functions
  * Counter-measure for Cross-Site-Scripting
  * URL encoding functionality
  * Creating query parameters from JavaBean
* JSP tag to display pagination
* JSP tag to display output messages after request processes
    </pre>
    </td>
  </tr>
  <tr>
    <td>4</td><td>terasoluna-gfw-mybatis3</td><td>Maven dependency definition for MyBatis3</td><td>No</td><td><pre>* Dependency definition for MyBatis3</pre></td>
  </tr>
  <tr>
    <td>5</td><td>terasoluna-gfw-jpa</td><td>Maven dependency definition for JPA</td><td>No</td><td><pre>* Dependency definition for JPA</pre></td>
  </tr>
  <tr>
    <td>6</td><td>terasoluna-gfw-mybatis2<br>(<strong>NOT RECOMMENDED</strong>)</td><td>Maven dependency definition for MyBatis2</td><td>No</td><td><pre>* Dependency definition for MyBatis2</pre></td>
  </tr>
  <tr>
    <td>7</td><td>terasoluna-gfw-security-core</td><td>Maven dependency definition for using spring-security (other than web)</td><td>No</td><td><pre>* Dependency definition for Spring Security (other than web)</pre></td>
  </tr>
  <tr>
    <td>8</td>
    <td>terasoluna-gfw-security-web</td>
    <td>Maven dependency definition for using spring-security (web related) and components that extend spring-security</td>
    <td>yes</td>
    <td>
    <pre>
* Servlet filter to output the authenticated username in log
* Redirect handler to counter-measure open redirect vulnerablibility
* CSRF counter-measure
    </pre>
    </td>
  </tr>
</table>

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
    <repository>
        <releases>
            <enabled>true</enabled>
        </releases>
        <snapshots>
            <enabled>false</enabled>
        </snapshots>
        <id>terasoluna-gfw-3rdparty</id>
        <url>http://repo.terasoluna.org/nexus/content/repositories/terasoluna-gfw-3rdparty/</url>
    </repository>
</repositories>
```

#### Using parent

Define parent project in pom file

``` xml
<parent>
  <groupId>org.terasoluna.gfw</groupId>
  <artifactId>terasoluna-gfw-parent</artifactId>
  <version>5.0.0.RELEASE</version>
</parent>
```

After adding above, add the following dependency definitions. 
(Only the required ones.)

``` xml
<dependency>
    <groupId>org.terasoluna.gfw</groupId>
    <artifactId>terasoluna-gfw-web</artifactId>
</dependency>
<!-- OPTIONAL -->
<dependency>
    <groupId>org.terasoluna.gfw</groupId>
    <artifactId>terasoluna-gfw-jodatime</artifactId>
    <type>pom</type>
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
</dependency

<!-- If Spring Security is to be used -->
<dependency>
    <groupId>org.terasoluna.gfw</groupId>
    <artifactId>terasoluna-gfw-security-web</artifactId>
</dependency>

<!-- If MyBatis3 is to be used -->
<dependency>
    <groupId>org.terasoluna.gfw</groupId>
    <artifactId>terasoluna-gfw-mybatis3</artifactId>
</dependency>

<!-- If JPA is to be used -->
<dependency>
    <groupId>org.terasoluna.gfw</groupId>
    <artifactId>terasoluna-gfw-jpa</artifactId>
</dependency>

<!-- If MyBatis2 is to be used (NOT RECOMMENDED) -->
<dependency>
    <groupId>org.terasoluna.gfw</groupId>
    <artifactId>terasoluna-gfw-mybatis2</artifactId>
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
    <artifactId>terasoluna-gfw-web</artifactId>
</dependency>

<!-- OPTIONAL -->
<dependency>
    <groupId>org.terasoluna.gfw</groupId>
    <artifactId>terasoluna-gfw-recommended-dependencies</artifactId>
    <version>5.0.0.RELEASE</version>
    <type>pom</type>
</dependency>

<!-- OPTIONAL -->
<dependency>
    <groupId>org.terasoluna.gfw</groupId>
    <artifactId>terasoluna-gfw-recommended-web-dependencies</artifactId>
    <version>5.0.0.RELEASE</version>
    <type>pom</type>
</dependency

<!-- If Spring Security is to be used -->
<dependency>
    <groupId>org.terasoluna.gfw</groupId>
    <artifactId>terasoluna-gfw-security-web</artifactId>
    <version>5.0.0.RELEASE</version>
</dependency>

<!-- If MyBatis3 is to be used -->
<dependency>
    <groupId>org.terasoluna.gfw</groupId>
    <artifactId>terasoluna-gfw-mybatis3</artifactId>
    <version>5.0.0.RELEASE</version>
</dependency>

<!-- If JPA is to be used -->
<dependency>
    <groupId>org.terasoluna.gfw</groupId>
    <artifactId>terasoluna-gfw-jpa</artifactId>
    <version>5.0.0.RELEASE</version>
</dependency>

<!-- If MyBatis2 is to be used (NOT RECOMMENDED) -->
<dependency>
    <groupId>org.terasoluna.gfw</groupId>
    <artifactId>terasoluna-gfw-mybatis2</artifactId>
    <version>5.0.0.RELEASE</version>
</dependency>
```

## TERASOLUNA Global Framework

The common library of TERASOLUNA Global Framework is maintained under [1.0.x](https://github.com/terasolunaorg/terasoluna-gfw/tree/1.0.x) branch.

## License
The TERASOLUNA Server Framework for Java (5.x) is released under Apache  License, Version 2.0.
