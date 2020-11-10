# Requirements specification

## Purpose of the application

The purpose of the application is to allow the user to get an easily understandable and navigable overview of their runs (outdoor, i.e. not on treadmill). Today, a lot of different mobile apps and wearable devices exist, which gathers data for the user about their activities. However, these often require an account, and perhaps even a subscription, and don't necessarily allow for local storage of the data they gather - either because of the limited storage often found in smaller devices, or because of the design of the service. Also, simply because of the form factor of these platforms, they don’t always offer an easily understandable and navigable overview of the data they gather. A desktop application on the other hand, offers a larger platform providing an opportunity to more comfortabely browse and track your runs, when getting home, and locally store the data you collected on your run.

## Users

The application is primarily aimed at single users, for whom the app offers a way to save, track, and browse the data they gather about their running.

## User interface

tba

## Functinality

The user will be able to:
-	Create categories of runs (e.g. “Pavement”, “Trail”, “Track”, “5K”, “10K”, “tempo”. “easy” etc.),
-	Create subcategories to higher categories, e.g. “Surface” = {“Pavement”, “Trail”, “Track”}, “Distance” = {“5K”, “10K”},
-	Save runs by manually inputting data about them, including:
    - Duration,
    - Moment in time (e.g. date),
    - Distance,
    - .gpx file,
    - Avg. cadence,
    - Avg. speed,
    - Applicable user-created categories.
-	Edit data in saved runs,
-   Delete runs,
-   See an overview of any run saved in the application and the data added to that run,
-	Browse runs by user-created categories and open an overview of any run matching selected categories, 
-   See an overview of all runs that match selected user-created categories including: 
    - Number of runs matching selected categories,
    - Total distance of runs matching selected categories (for runs for which user has added .gpx files),
    - Avg. distance of runs matching selected categories (for runs for which user has added .gpx files),
    - Total duration of runs matching selected categories,
    - Avg. duration of runs matching selected categories,
    - Graph of duration/avg. speed per moments in time,
    - Personal best (i.e. fastest run/shortest duration),
    - Map of included routes (provided user has added .gpx files),


## Technological requirements on the application

-   The application must work on the Linux- & OSX desktop,
-   The application will use a database for persistent storage,
-   All app data will be saved locally on device.

## Future development

-   Ability to browse and see an overview of runs on a time axis,
-   Ability to see graph of distance per moment in time in "overview of all runs that match selected user-created categories",
-   Ability to see graph of avg. cadence per run matching selected categories (easily implemented but assumed to be of lesser interest to user, thus beeing put low on the "to-do-list"),
-   Ability to add and see altitude data for a run (tot. ascent, totl descent, and altitude graph of run),
-   Ability to add and see more complete cadence data for a run (graph of cadence during run, e.g. 1min avg.),
-   Inclusion of heart rate data,
-   Make app more general and dynamic no longer focusing on showing data specifically to runs, instead allowing users to use the app to stora data about any kind of activity, track it, and get overviews of relevant data to their activities. Making the app more general could be facilitated by the app logic already depending on user-created categories, (i.e. Activities could be user-created categories and activity-specific categories  (e.g. "Distance") would become subcategories, with their own subcategories etc., to the activity). However, different data can be relevant to different activities, and thus a useful a pivot to activity in general might require tailor made user interfaces for each activity  thus significantly complicating the user interface, the application logic, and the application design in general. Because time and location data often is interesting for runners, activities where such data is also valued coudl be quite easily incorporated, e.g. cycling and outdoor swimming. Then, the application would not support genearl activities though but again be activity specific, evanthough the range of supported activities would have increased.
