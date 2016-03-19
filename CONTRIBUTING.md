# How to contribute Development of the terasoluna-gfw

This document describes how to contribute the terasoluna-gfw updates.

The terasoluna-gfw is the common library of TERASOLUNA Server Framework for Java (5.x), and is Java projects for [Maven](https://maven.apache.org/).
About running Maven, refer to the [Building a Project with Maven](https://maven.apache.org/run-maven/index.html).

Contribution procedures are follows:


## Create a new issue

Please create a new issue from [here](https://github.com/terasolunaorg/terasoluna-gfw/issues/new) for contributing(bug report, improvement or new content), and get an issue number(tracking id).

> **Note: Supported language**
>
> English only.

* Please write the contribution overview into the title area.
* Please write the contribution detail into the comment area.

 e.g.)
 ```
 ## Description
 In Comment on org.terasoluna.gfw.web.codelist.CodeListInterceptor, there is a mistake in the below sentence.

 `"Interceptor class for setting codelist in attribute of {@link HttpServletRequrst}"`

 ## Possible Solutions
 Modifying to `"Interceptor class for setting codelist in attribute of {@link HttpServletRequest}"`

 ## Affects Version/s
 * 5.0.0.RELEASE
 * 1.0.2.RELEASE

 ## Fix Version/s
 (To be written later by project member)

 ## Issue Links
 * https://github.com/terasolunaorg/terasoluna-gfw/issues/999
 ```

## Fork a repository

Please fork the `terasolunaorg/terasoluna-gfw` into your account repository of GitHub.

* Click a "Fork" button on GitHub web user interface.


## Clone a repository

Please clone a forked repository into your local machine.


e.g.)

```
git clone https://github.com/{your account}/terasoluna-gfw.git
```


## Create a work branch

Please create a work branch on the master branch into your local repository.

> **Note: Recommended work branch name**
>
> issues/{issue number}_{short description}

e.g.)

```
git checkout master
git checkout -b issues/999_typo-in-CodeListInterceptor
```


## Modify the terasoluna-gfw

Please modify the terasoluna-gfw for contributing.



## Build projects and execute tests

Please build all projects and execute all tests using the [Maven](https://maven.apache.org/).

**For Linux or Mac**

```
cd {root directory of your local repository}
sh ./mvn-build-all.sh
```

**For Windows**

```
cd {root directory of your local repository}
mvn-build-all.bat
```

If a maven build result has failed, please modify a source code again.


## Commit a modification

Please commit a modification.

> **Note: Commit comment format**
>
> "{modification overview} #{issue number}"

> **Note: Supported language**
>
> English only.

e.g.)

```
git commit -a -m "Fixes typos in CodeListInterceptor #999"
```


## Push a work branch

Please push a work branch to the GitHub.

e.g.)

```
git push origin issues/999_typo-in-CodeListInterceptor
```


## Create a pull request

Please create a pull request via GitHub web user interface.
For details, please refer to the [GitHub document-Creating a pull request-](https://help.github.com/articles/creating-a-pull-request/).

> **Note: Supported language**
>
> English only.

* Please write the modification overview into the title area and add the #{issue number} at its end. (Default is commit comment or work branch name)
* Please write the modification detail into the comment area. (If needed)
* Please include the issue number(`#{issue number}` format) to track a modification into the comment area.

e.g.)

| Area | Content |
| ----- | --------- |
| Title | Fixes typos in CodeListInterceptor #999 |
| Comment | Please review #999 . |
