# RunView

This is a course project, for the course in Software Engineering (fi: "Ohjelmistotekniikka", TKT20002) at the University of Helsinki, which should result in an application that allows the user to store relevant data regarding their running, browse and get an overview of their running history.


## Documentation

[Requirements specification](https://github.com/jrhel/ot-harjoitustyo2020/blob/master/documentation/Requirements%20specification.md)

[Record of working hours](https://github.com/jrhel/ot-harjoitustyo2020/blob/master/documentation/Record%20of%20working%20hours.md)

[Architecture description](https://github.com/jrhel/ot-harjoitustyo2020/blob/master/documentation/arcitecture.md)

## Command line Commands

### Testing

Tests are run with the command:

```
mvn test
```

A report of the test coverage is generated with the command:

```
mvn jacoco:report
```

The report can be accessed and browsed by opening the file _target/site/jacoco/index.html_ in your browser

### Checkstyle

Checks, as defined by the file [checkstyle.xml](https://github.com/jrhel/ot-harjoitustyo2020/blob/master/otRunView/checkstyle.xml), are run with the command:

```
 mvn jxr:jxr checkstyle:checkstyle
```

An overview of all errors can be accessed by opening the file _target/site/checkstyle.html_ in your browser






