# ExpirationDateTracker

#### Help you to track expiration date for your food and get notified whenever any of them turned expired.


## Tech Stack

This project uses [feature package architecture].
The project features uses MVVM as software design patter for presentation layer.


## Libraries

- Application entirely written in [Kotlin](https://kotlinlang.org)
- Asynchronous processing using [Coroutines](https://kotlin.github.io/kotlinx.coroutines/)
- Uses [Hilt](https://developer.android.com/training/dependency-injection/hilt-android) for dependency injection
- BarCode Scanner [Library](https://github.com/journeyapps/zxing-android-embedded).
- Uses [Rrtrofit](https://square.github.io/retrofit/) for Http Requests.
- Uses Jetpack [Room](https://developer.android.com/training/data-storage/room)  as local database.
- Uses [DataBinding](https://developer.android.com/topic/libraries/data-binding/start) for ui binding.
- Uses Jetpack [Data Store](https://developer.android.com/topic/libraries/architecture/datastore) for saving light weight Key-Value data.
- Uses Jetpack [WorkManager](https://developer.android.com/topic/libraries/architecture/workmanager) for Periodic jobs.
- Uses [JUnit4](https://developer.android.com/training/testing/junit-rules) for unit tests.

## API keys

You need to supply API / client keys for the service the app uses.

- [BARCODE LOOKUP](https://www.barcodelookup.com/api)

Once you obtain the key, you can set them in your `~/local.properties`:
````

BARCODE_LOOKUP_KEY = "YOUR_API_KEY_GOES_HERE"
````

## Application flow

<img src="https://user-images.githubusercontent.com/61784191/150703788-144426a7-f2d1-4043-885c-cf1b0d836525.jpg" width="200"> <img src="https://user-images.githubusercontent.com/61784191/150703790-5f50b521-0c34-49cc-95df-870c9731b031.jpg" width="200"> <img src="https://user-images.githubusercontent.com/61784191/150703791-f0700a4d-e8ee-411e-aa62-5207744d9159.jpg" width="200"> <img src="https://user-images.githubusercontent.com/61784191/150703793-0ef1e99a-c95d-4646-a402-f8674d0ba4e0.jpg" width="200">


https://user-images.githubusercontent.com/61784191/150703795-b95b6230-f0ea-467c-856f-008fd554c586.mp4


