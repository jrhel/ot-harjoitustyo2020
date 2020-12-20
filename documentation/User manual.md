# User manual

The application allows the user to store data about their runs, browse their running history, and get overviews of individual runs as well as all runs matching selected user-created categories.

## Launching application

The application can be lauched, after downloading the file [RunView.jar](https://github.com/jrhel/ot-harjoitustyo2020/releases/tag/loppupalautus), with the command line command:

```
java -jar RunView.jar
```

The first thing a user might want to do, is add their running data to the application. However, to get the most out of the application it is recommended that the user adds any category of run that they deem relevant. E.g. many runenrs like to do so called "tempo" runs, "long" runs, and "easy" runs. Some run "5K"s, "10K"s, "half marathons", or even "marathons", and some run even longer. Perhaps you're interesting in seeing what shoes you used when, or "how many miles" you "have on them". Getting a meaningful overview of your data requires the ability to select the data that is relevant to what you want to see. E.g., seeing the data from your "easy" runs with your "marathons" might not be very helpful. Thus, having categories to "tag" your runs with is helpful to later be able to quickli select the ones that youare interesting to see.

To create a new category, simply select "Add a new category", and a new page will pop up allowing you to add any information you deem relevant to your category by entering that information into the field below "Add attribute" and then selecting "Add attribute". From this page you can also make you new category a subcategory by selecting one of the listed "parent categories". Fianlly, give your category a name, e.g. "tempo" or "5K", and select "Save category".

(Graphic illustration will be added when the estetic design of the GUI is finished.)

To add a new Run, simply select "Add a new run". Fill in the date of your run in the form "dd\mm\yyyy", the distance in kilometers, the duration of your run in the form "hh:mm:ss", and the total amount of steps you took on your run. Finally add the file path of a .gpx file for your run, if you wish. You can select any applicable categories from the list on the right. When youre done, simply click "Save run" to add the run to the application.

(Graphic illustration will be added when the estetic design of the GUI is finished.)

To open an overview of a run, simply select it. (Section will be expanded as the user interfece expands.)

To delete a run, simply open the overview of it and click "Delete run".

(Graphic illustration will be added when the estetic design of the GUI is finished.)

To edit the data of a run, simply open the overview of it and click "Edit". A new page will open, similar to when adding a new run. Fill in the page with the data for that run as you would when adding a new one, and click save and the run will be updated with the new data.

(Graphic illustration will be added when the estetic design of the GUI is finished.)
