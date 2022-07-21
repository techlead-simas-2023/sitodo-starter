# Sitodo

[![pipeline status](https://gitlab.com/addianto/sitodo/badges/main/pipeline.svg)](https://gitlab.com/addianto/sitodo/-/commits/main)
[![coverage report](https://gitlab.com/addianto/sitodo/badges/main/coverage.svg)](https://gitlab.com/addianto/sitodo/-/commits/main)

A basic todo app for teaching basic Web programming, Git workflows, and CI/CD.
Heavily inspired by the running example in "Test-Driven Development with Python"
book by Harry Percival.

## Getting Started

This project uses Java 17 and Spring Boot framework. The test suite comprises
unit and functional test suites. The functional tests are run using Selenium on
Firefox browser.

To run the whole test suite, execute:

```shell
mvn test
```

> To run a select test suite, e.g. unit or functional test, add `-Dgroups`
> parameter. For example, to run only the unit test suite, execute
> `mvn test -Dgroups=unit`.  Similarly, to run only the functional test suite,
> execute `mvn test -Dgroups=e2e`.

To build an executable Spring Boot application, execute:

```shell
mvn package -DskipTests
```

> The `-DskipTests` option lets `package` task to build the app into executable
> JAR file without running all test suites. If the option was omitted, then
> all test suites will run, thus increasing the duration of build process,
> especially the functional test suite that run much longer than the unit test
> suite.

The JAR file will be generated at [`./target`](./target) directory. To run it,
execute:

```shell
java -jar sitodo.jar
```

You can customise the configuration by providing an `application.properties`
file in the same directory as the executable JAR file. See the built-in
configuration in the [source code](./src/main/resources/application.properties).

## Running Example

See the running example based on the main branch at [Heroku](https://sitodo-example.herokuapp.com).

## License

This project is licensed under the terms of the [MIT license](./LICENSE).
