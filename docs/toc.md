# Table of Contents

## Course Setup

1. Install Git
   1. Reminder about newlines, i.e. `git config --global auto.crlf input`,
      and install EditorConfig
2. Install Java
   1. Ensure JAVA_HOME and PATH configured properly
3. Install IDE (IntelliJ)
4. Set up GitHub account
5. Set up Heroku / Fly.io account
6. At the end, verify all tools have been installed, e.g. `git --version`

## Project Scaffolding

1. Use [Spring Initializr](https://start.spring.io/)
2. Initialise Git repository in local development environment
3. Create a new online Git repository on GitHub
4. Update README.md
5. Create a first commit
6. Set up a new Git remote from local repository to online repository on GitHub
7. Push local Git history to the online repository

## Implement Landing Page (Hello, World)

1. Introduction to test-driven development (TDD)
2. Inside-out TDD part 1: pure unit test
   1. Create an HTML page for the landing page with custom greeting from GET request
   2. Create a unit test for the controller class
3. Inside-out TDD part 2: unit test in mocked environment using `@MockMvc`
   and `@SpringBootTest`
   1. Create a unit test with mock for the controller class
4. Inside-out TDD part 3: functional test with Selenium (Optional)

## Quality Assurance & Test Automation

1. Deploy the app manually using Heroku or Fly.io CLI tool
2. Automate test suite execution using GitHub Actions on GitHub
3. Automate deployment using GitHub Actions on GitHub
4. Code quality analysis using GitHub Actions on GitHub (Optional)

## Implement Todo List

1. Implement data persistence layer
   1. Create model class using JPA
   2. Create migration script using Liquibase
2. Implement business logic layer
   1. Create a service class
   2. Write a unit test for service class
3. Implement presentation layer
   1. Create a controller class
   2. Write a unit test for controller class
   3. Create a Web page using HTML and Thymeleaf template syntax

## Extra Challenge: Personalised Todo List with Authentication

<!-- 
    -- Notes --
    Security in Spring: 
    https://stackoverflow.com/questions/42148257/unit-testing-methods-using-principal-from-spring-secucrity?noredirect=1&lq=1
-->

1. Define user authentication model
2. Update database schema via database migration
3. Secure the existing todo list behind authentication

## Extra Challenge: PostgreSQL Database

1. Install PostgreSQL on development and deployment environments
   1. Local installation options: system-wide (using installer) or container
      (Docker)
   2. Installation options on PaaS: Heroku Addon (Heroku) or `flyctl` (Fly.io)
