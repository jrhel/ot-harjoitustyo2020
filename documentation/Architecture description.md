# Architecture description

## Structure

The application implements a three level design, in the form that the package _main.ui_ contains the user interface which initializes the application logic, contained in the package _main.domain_. The application logic, in turn, initializes data access objects which handle the persistent storage of the applications data and are contained in the package _main.dao_. The UI is initialized by the class _main.Main_. The main package structure is illustrated below:

<img src="https://github.com/jrhel/ot-harjoitustyo2020/blob/master/documentation/pictures/package%20structure.jpg">

More specifically, the class main.Main launches the class _mai.ui.GUI_, which in turn initiates an instance of the class _main.ui.stage.MainStage_ from which user interaction is possible.

## User interface

The main user interface consists of three pages (see below).
- The "front page" (_main.ui.stage.MainStage_), showing the user a list and a map of all their runs, as well as a list of categories they have saved to the application. The front page also contains some buttons for further interaction, 
- The "run overview", showing the user some detailed metrics and information regarding their run, a list of any categories they have tagged it with, a map of their run (if they have provided a .gpx file for it), and buttons for editing their run or deleting it from the application, 
- The "category overview", showing an overview of all runs matching selected categories.

Additionally, the user interface can display two forms for user input, popups with messages to the user in specific cases.
All pages and popups use their own Stage objects, which are initated, closed, or updated depending on the requirements of the situation. These are contained in the package _main.ui.stage_. 

The map functionality of the user interface is contained in the package _main.ui.map_. Within this package, the class _RunMap_ functions as the packages internal logic. _RunMap_ takes the application logic and a list of .gpx file paths as parameters, and creates a [JXMapViewer](https://github.com/msteiger/jxmapviewer2) object which gets wrapped in a SwingNode object which gets wrapped in StackPane object to be usable for the main GUI, implemented with JavaFX. The package _main.ui.map_ also contains a package _main.ui.map.painters_ which contains functionality that [JXMapViewer](https://github.com/msteiger/jxmapviewer2) requires to work. The contents of the package _main.ui.map.painters_, i.e. the classes RoutePainter, SelectionPainter, and SelectionAdapter, **IS NOT ORIGNIAL CODE TO THIS PROJECT. Those classes have been borrowed in order to get [JXMapViewer](https://github.com/msteiger/jxmapviewer2) to work, similarly to classses that have been imported to support other parts of the application. Credit for the classes RoutePainter, SelectionPainter, and SelectionAdapter belongs to the creators of [JXMapViewer](https://github.com/msteiger/jxmapviewer2).**

## Application logic

The main responsibility for the application logic lies with the _Logic_ class, which is supported by the classes _Run_, _Category_, and _CategoryAttribute_, in addition to the class _AggregatedRunData_. The _Logic_ class controls the DAO classess and serves the UI. Below is a full class diagram for the application:

<img src="https://github.com/jrhel/ot-harjoitustyo2020/blob/master/documentation/pictures/package_ClassDiagram.jpg">

## Persistent storage of application data

The classes _CategoryDAO_, _CategoryAttributeDAO_, _RunDAO_, and _RunCategoryDAO_ in the package _main.dao_ implement the DAO interface and are responsible for all database operations. The application uses the H2 database engine, handled by these classes, and observes the [DAO design pattern](https://en.wikipedia.org/wiki/Data_access_object).

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
    
The lowermost constructor could also be used in the future to give paralell users paralell database tables without risk of accessing each others data. Since the application is aimed at single users, this feature is however not yet supported nor a priority. The potential of the implementation is though worth mentioning.

## Main user functionality

Below, how the application implements some main user functionality in the form of sequence diagrams:

### Creation of a new category:

<img src="">
