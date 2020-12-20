# Requirements specification

## Purpose of the application

The purpose of the application is to allow the user to save data gathered during their runs*, and be able to browse it later and get an easily understandable and navigable overview of their running history. Today, a lot of different mobile apps and wearable devices exist, which gather data for the user about their activities. However, these often require an account, perhaps even a subscription, and don't necessarily allow for local storage of the data they gather - either because of the limited storage often found in smaller devices, or by design of the service. Also, simply because of the form factor of these platforms, they don’t always offer an easily understandable and navigable overview of the data they gather. A desktop application on the other hand, offers a larger platform providing an opportunity to more comfortabely browse and track your runs at home, and locally store the data you collect on your runs.

*The application is primarily meant for outdoor running, i.e. not on treadmill.

## Users

The application is primarily aimed at single users, for whom the app offers a way to save, track, and browse the data they gather about their running.

## User interface

The main user interface consists of three pages:.

The "front page" (below), showing the user a list and a map of all their runs, as well as a list of categories they have saved to the application. The front page also contains some buttons for further interaction:
<img src="https://github.com/jrhel/ot-harjoitustyo2020/blob/master/documentation/pictures/frontPage.jpg">

Pressing the "Add a new run" button further opens a form, for entering the relevant information for saving a new run to the application.
Pressing the "Add a new category" button further opens a form, for entering the relevant information for saving a new category to the application.

The "run overview" (below), showing the user some detailed metrics and information regarding their run, a list of any categories they have tagged it with, a map of their run (if they have provided a .gpx file for it), and buttons for editing their run or deleting it from the application:
<img src="https://github.com/jrhel/ot-harjoitustyo2020/blob/master/documentation/pictures/runPage.jpg">

The "category overview" (below), showing an overview of all runs matching selected categories:
<img src="https://github.com/jrhel/ot-harjoitustyo2020/blob/master/documentation/pictures/overviewPage.jpg">

## Functionality

The front page allows the user to:
-   open a form for entering data for a new run and saving it in the application,
-   browse all runs they have saved to the application (sorted by date), and open an overview of any run,
-   see a map of all their runs, for which they have provided a .gpx file,
-   open a form for entering information pertaining to a new category and saving it in the application, 
-   browse all categories they have saved to the application, and open an overview of any single category or combination of categories,
-   reset the application, so that it contains no data.

The run overview allows the user to:
-   see some detailed metrics and information regarding their run, including the date and distance and duration of the run, the avg. speed and avg. cadence during the run, 
-   see all teh categories the run has been tagged with,
-   buttons for editing and deleting the run,
-   see a map of the run (if they have provided a .gpx file for it),
-   the file path for where the .gpx file is located in the local file system.

The category overview allows the user to:
-   see some aggregated data regarding their shosen categories, including how many runs match the overview includes, their combined distance and duration, and the avg. duration adn avg. speed and avg. cadence of the included runs,
-   some detailed metrics and information regarding their best* personal run, including the date and distance and duration of that run, the avg. speed and avg. cadence during that run, and all the categories that it has been tagged with,
-   see a graph of the durations of the included runs over time,
-   see a map of all included runs (for which a .gpx file has been provided to the appication),
-   see a list of all the included runs, allowing to open an overview of any one of them by clicking on it.

* Pest personal run is measured by fastest avg speed.

## Technological requirements on the application

-   The application must work on the Linux- & OSX desktop,
-   The application will use a database for persistent storage,
-   All app data will be saved locally on device.

## Future development

-   Ability to browse and see an overview of runs on a time axis,
-   Ability to see graph of distance per moment in time in "overview of all runs that match selected user-created categories",
-   Ability to see graph of avg. cadence per run matching selected categories,
-   Ability to see graph of avg. speed per run, projected on a graph of distance per run, matching selected categories,
-   Ability to add and see altitude data for a run (tot. ascent, tot. descent, and altitude graph of run),
-   Ability to add and see more complete cadence data for a run (graph of cadence during run, e.g. 1 min avg.),
-   Inclusion of heart rate data,
-   Make app more general and dynamic no longer focusing on showing data specifically to runs, instead allowing users to use the app to stora data about any kind of activity, track it, and get overviews of relevant data to their activities. Making the app more general could be facilitated by the app logic already depending on user-created categories, (i.e. Activities could be user-created categories and activity-specific categories  (e.g. "Distance") would become subcategories, with their own subcategories etc., to the activity). However, different data can be relevant to different activities, and thus a useful a pivot to activity in general might require tailor made user interfaces for each activity  thus significantly complicating the user interface, the application logic, and the application design in general. Because time and location data often is interesting for runners, activities where such data is also valued coudl be quite easily incorporated, e.g. cycling and outdoor swimming. Then, the application would not support genearl activities though but again be activity specific, evanthough the range of supported activities would have increased.
