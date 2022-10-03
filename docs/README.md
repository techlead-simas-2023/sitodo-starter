# Developing Sitodo From Zero

> A training course for developing a simple todo list Web application using Java
> and Spring Boot framework.

## Description

This is a training course for teaching students on how to develop a Web application
using Java programming language and Spring Boot framework, with focus on applying
test-first approach and continuous integration/continuous deployment (CI/CD).

## Learning Outcomes

By the end of this course, students should be able to:

-  Apply a workflow when using a version control system such as [Git][]
-  Develop a simple Web application using Java programming language and Spring
   Boot framework backed by a relational database.
-  Create tests for verifying the Web application in different levels, such as
   unit test, integration test, and functional test
-  Set up a Continuous Integration/Continuous Deployment (CI/CD) pipeline using
   GitHub Actions (or similar platform such as GitLab CI/CD)

## Target Audience

While the course is designed for fresh graduates with a background in computing,
students coming from a different background can read and attend the course. At
minimum, you need to:

-  Have some experiences in programming, preferably using an object-oriented
   language
-  Understand relational data modeling and SQL operations
-  Know Web design and familiar with the related technologies such as HTML,
   basic JavaScript, and CSS
-  Know basic Linux shell scripting (e.g. `bash`) or its equivalent on Windows
   (e.g. `cmd` or `pwsh`)

## Requirements

To complete this course, you need a computing device that can run the following
development tools:

-  A shell, such as `bash` on Linux or Mac OS, or `cmd`/`pwsh` on Windows
-  Latest long-term support (LTS) version (i.e. Java 17 at the time of writing) of Java Development Kit (JDK),
   preferably from [Adoptium][].
-  [IntelliJ IDEA Community Edition by JetBrains][]
-  [PostgreSQL][] database system version 14

> Note: While you can use other text editors (e.g. Visual Studio Code) or IDE such
> as Eclipse or NetBeans, we might not be able to troubleshoot any problems
> that come up during the course

In addition, you also need to have an account on the following online services:

-  [GitHub][] an online source code repository and project management system
-  [Heroku][] a Platform-as-a-Service (PaaS) for deploying the Web application

## Course Outlines

The course will include:

1. Set up local development environment
2. Initialise up a Spring Boot project
3. Introduction to test-driven development (TDD)
4. Create a continuous integration and deployment pipeline (CI/CD)
5. Exercise a Git workflow such as Feature Branch

There will be some additional challenges for participants, such as:

1. Develop functional test using Selenium
2. Implement basic user authentication using Spring Security
3. Migrate deployment to a different PaaS provider such as [Fly.io][]

You can proceed to the contents for [Day 1](./bootcamp/day-1/init-spring.md).
     
## License

The learning materials in this course are licensed under Creative Commons
Attribution-ShareAlike 4.0 ([CC BY-SA 4.0](./LICENSE)).
You can reuse, modify, distribute, and do pretty much anything with the course
materials as long as it is permitted under the terms of the license.

[Adoptium]: https://adoptium.net/
[Fly.io]: https://fly.io
[Git]: https://git-scm.com/
[GitHub]: https://github.com
[Heroku]: https://www.heroku.com
[IntelliJ IDEA Community Edition by JetBrains]: https://www.jetbrains.com/idea/download/
[PostgreSQL]: https://www.postgresql.org/
