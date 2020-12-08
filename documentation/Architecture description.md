# Architecture description

## Structure

The application implements a three level design, in the form that the package _main.ui_ contains the user interface which initializes the application logic, contained in the package _main.domain_. The application logic, in turn, initializes data access objects which handle the persistent storage of the applications data and are contained in the package _main.dao_. The UI is initialized by the class _main.Main_.The entore package structure is illustrated below:

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

The main responsibility for the application logic lies with the Logic class, which is supported by the classes Run, Category, and CategoryAttribute. The Logic class controls the DAO classess and serves the UI, as illlustrated below:


<img src="https://github.com/jrhel/ot-harjoitustyo2020/blob/master/documentation/pictures/package-class%20diagram.jpg">
