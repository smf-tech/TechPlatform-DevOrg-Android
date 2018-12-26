# TechPlatform-DevOrg-Android

## Model View Presenter architecture

- View
It contains only the UI and it does not contain any logic or knowledge of the data displayed.
In typical implementations the view components in MVP exports an interface that is used by the Presenter.
The presenter uses these interface methods to manipulate the view.

- Presenter
The presenter triggers the business logic and tells the view when to update.
It therefore interacts with the model and fetches and transforms data from the model to update the view.
The presenter should not have, if possible, a dependency to the Android SDK.

- Model
Contains a data provider and the code to fetch and update the data.
This part of MVP updates the database or communicate with a web server.