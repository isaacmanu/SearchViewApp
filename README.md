# TFTStatsApp

A project I started to develop my skills as an Android Developer. The goal was to create an Android app where players of the online competitive game 
'Teamfight Tactics' could access detailed information on games they and others played.

## Overview

Riot Games provides APIs which developers can use to create web or mobile applications that allow users to access 
detailed information on themselves or other users. This app aims to use the APIs provided by Riot Games to give users detailed information on their matches 
played in the Riot Games product 'Teamfight Tactics' (or 'TFT' for short).

## Technologies

The app itself is written entirely in Kotlin targeting Android 12 API level 32 and uses the following technologies:

- [Retrofit](https://square.github.io/retrofit/) to make HTTP GET requests to the server.

- [Coil](https://coil-kt.github.io/coil/) to load and display images from the web.

- [Moshi](https://github.com/square/moshi) to parse JSON responses into Kotlin objects.

- [List Adapter](https://developer.android.com/reference/androidx/recyclerview/widget/ListAdapter)

- [Live Data](https://developer.android.com/topic/libraries/architecture/livedata)

- [Data Binding](https://developer.android.com/topic/libraries/data-binding) to bind UI components to Live Data sources.

- [Coroutines](https://developer.android.com/kotlin/coroutines) to launch network requests off of the main thread.

- MVVM Architecture

The [backend](https://4u2rwv.deta.dev/docs) server is written in Python and hosted by [Deta](https://www.deta.sh/) and uses the following:

- [FastAPI](https://fastapi.tiangolo.com/) framework to build the API.

- [Requests](https://pypi.org/project/requests/) library to make GET requests to the [Riot API](https://developer.riotgames.com/).
