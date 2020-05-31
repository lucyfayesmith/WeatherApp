# WeatherApp

This is a simple weather app for android devices which has been created for Android Programming class (Course ID: T560013101). 
The requirements for this project was to include at least 3 of following: Fragments, Service, Threading, Web service
call, External library, Database, Broadcast Receivers, Content providers, Adapters. 

##Project Requirements: 

By creating a weather app, we were able to encorporate content providers, adapters, webservice calls and databases. The user interface
incorporates the use of adapters with the implementation of the Daily and Hourly recycler views. This adapter is the bridge between
the components of the UI and the weather data. To retrieve the relevant weather information this app makes API calls (web service calls)
to [OpenWeatherMap] https://openweathermap.org/api along with the current user location (CUL). 

There is also the encoporation of a widget to accompany the main app that displays the weather based on the CUL. By creating the widget 
we have been able to include content providers into the structure of our app and the use of background processing.

In order to store the users desired locations for weather data retrieval we have implemented a local database using ROOM.

##Installation

Due to the nature of emulators, upon first installation of this app it is neccessary to first open google maps to accept to any neccessary
location restrications. 

This app has been designed to adapt to all screensizes, with a minimum SDK of 24 (Nougat). 


