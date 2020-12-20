# Architecture description

## Structure

The application implements a three level design, in the form that the package _main.ui_ contains the user interface which initializes the application logic, contained in the package _main.domain_. The application logic, in turn, initializes data access objects which handle the persistent storage of the applications data and are contained in the package _main.dao_. The UI is initialized by the class _main.Main_. The main package structure is illustrated below:

<img src="https://github.com/jrhel/ot-harjoitustyo2020/blob/master/documentation/pictures/package%20structure.jpg">

## User interface

The user interface contains four pages (so far).
- A main page giving an overview of all data saved in the application, and user options for further functionality of the application,
- A page for opening a more detailed overview of specif runs saved in the application,
- A page for adding new runs to the application,
- A page for adding new user categories to the application.

Below is an illustration of the user interface (mostly implemented):

<img src="https://github.com/jrhel/ot-harjoitustyo2020/blob/master/documentation/pictures/draft_UI.jpg">

A page for seeing an overview of all runs that match selected user-created categories is also in the works.

## Application logic

The main responsibility for the application logic lies with the _Logic_ class, which is supported by the classes _Run_, _Category_, and _CategoryAttribute_, in addition to the class _AggregatedRunData_. The _Logic_ class controls the DAO classess and serves the UI, as illlustrated below:

<img src="https://github.com/jrhel/ot-harjoitustyo2020/blob/master/documentation/pictures/package-class%20diagram.jpg">

## Persistent storage of application data

The classes _CategoryDAO_, _CategoryAttributeDAO_, _RunDAO_, and _RunCategoryDAO_ in th apackage _main.dao_ are responsible for all database operations.
The application uses the H2 database engine, handled by these classes, and observes the [DAO design pattern](https://en.wikipedia.org/wiki/Data_access_object).

In order for JUnit tests not to interfere with the users data, 1) the applications DAO classes implement a dynamic naming scheme for the database tables they use while retaining the relationship between the DAOs and thus the intactness of the database, 2) JUnit tests use the same data access objects as the application, but initialize the application logic with a different constructor passing on different names for the DAOs to use for their database tables.
Thus the application and JUnit tests use the same application logic and the same database, but parallel database tables instead of the same ones. The uppermost constructor is the constructor used by the application, and the lowermost constructer is used by JUnit tests:

    public Logic() {
        this.runDao = new RunDAO("Run");
        this.catDao = new CategoryDAO("Category");
        this.catAttributeDao = new CategoryAttributeDAO("CategoryAttribute", "Category");
        this.runCatDao = new RunCategoryDAO("RunCategory", "Run", "Category");
        this.ensureDataBaseExists();
    }
    
    public Logic(String runTableName, String categoryTableName, String runCategoryTableName, String categoryAttributeTableName) {
        this.runDao = new RunDAO(runTableName);
        this.catDao = new CategoryDAO(categoryTableName);
        this.catAttributeDao = new CategoryAttributeDAO(categoryAttributeTableName, categoryTableName);
        this.runCatDao = new RunCategoryDAO(runCategoryTableName, runTableName, categoryTableName);
        this.ensureDataBaseExists();
    }
    
The lowermost constructor could also be used in the future to give paralell users paralell database tables without risk of accessing each others data.

## Main user functionality

Below, how the application implements some main user functionality in the form of sequence diagrams:

### Creation of a new category:

<img src="https://github.com/jrhel/ot-harjoitustyo2020/blob/master/documentation/pictures/SD_createCategory.jpg">
