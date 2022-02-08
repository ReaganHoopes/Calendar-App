# Calendar-App

This project is an android app that allows users to:
- Sign up and log in
- The app allows you to make a schedule of events with the addition of location information. 

How to use:
- Open the app in android studio and run using the Pixel 2 emulator.
- Sign up/Log in.
- Select the FAB and you can place a marker on the map for your event location. If the event doesn't have a location just press save.
- Here you can input a title, date, start/end time, and description. The app will give error messages if any required information is missing (descriptions not required).
- You will then be redirected to the main screen where your event will be added to the list (in future runs the event will be in the list in order of when it occurs).
- You can then click on events to view informaiton, view the location or edit.
- For editing you can drag the marker to a new location and then make changes to other information regarding the event. You can also delete the event when needed.

Other functionality:
- The app checks inputs for email and password and supplies user with helpful error messages
- The app data is persistent
- Monetization capability using ads

Technologies utilized:
- Firebase for user auth
- Firestore for saving data to the cloud
- Google maps for location functionality

Languages used:
- Java
