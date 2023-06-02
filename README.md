Filmroll
=========

An Android app to browse films.

The app uses the [Movie Database's API](https://developer.themoviedb.org/docs) to get
information about the films.

Features
---------

- Search for films
- Display a film's details
- List recommended films depending on the selected one

Configuring
------------

Create the `gradle.properties` file in your user's `.gradle/` directory if it
doesn't exist, located for instance at `~/.gradle/gradle.properties`.

In this file, write the line
```gradle
TMDBApiToken="XXXX"
```
where `XXXX` is yout TMDB API token.

This allows to store the secret token out of the project, and not commit it.

Compiling the app
-----------------

Use the `gradlew` script corresponding to your operating system.

On Unix-likes:

```shell
./gradlew
```

Architecture
------------

The application internal architecture is structured according to the Android
recommendations.

```
                  ┌───────────┐
                  │           │
                  │   Logic   │
                  │           │
                  └─────┬─────┘
                        │
                 ┌──────▼──────┐
                 │             │
                 │  ViewModel  │
                 │             │
                 └──────┬──────┘
                        │
                 ┌──────▼─────┐
                 │            │
                 │ Repository │
                 │            │
                 └──────┬─────┘
                        │
                ┌───────┴───────┐
                │               │
         ┌──────▼─────┐    ┌────▼────┐
         │            │    │         │
         │  Database  │    │   API   │
         │            │    │         │
         └────────────┘    └─────────┘
```

License
-------

This project is distributed under the GPL v3 license, or any later version.
