# Vizbee Android TV Demo App

This sample shows how to develop a Vizbee Cast enabled Android TV app.

## Setup Instructions for your Android TV app
1. Use an existing Chromecast Receiver or use Vizbee hosted Chromecast Receiver.
   1. For this Demo App, Vizbee hosted the Receiver with app ID 7620440E.
2. Follow the steps for setting up the Cast Developer Console.
   1. Add the package name of your Android TV app in [Cast Developer Console](https://cast.google.com/publish) to associate it with your Cast App ID.
      1. This is already done for Demo App. Refer the picture below.
      ![Add Android TV Package Name to Cast Console](https://vzb-origin.s3.amazonaws.com/images/integration/androidtv/CastConsoleDemoPackageName.png)
   2. Add the cast serial number (Settings -> System -> Cast -> Serial number) of your Android TV to [Cast Developer Console](https://cast.google.com/publish) to test during development. Refer to the picture below.
      ![Add Cast Serial Number To Cast Console](https://vzb-origin.s3.amazonaws.com/images/integration/androidtv/AddCastSerialNumberToCastConsole.png)
   
3. Follow the steps for setting up your sender app for Android TV cast support.
   1. Once you have integrated Vizbee into your sender app, you can declare its readiness to cast 
      to Android TV by setting the androidReceiverCompatible flag on LaunchOptions to true.
      ```
      class CastOptionsProvider : OptionsProvider {
          override fun getCastOptions(context: Context?): CastOptions {
              val launchOptions: LaunchOptions = Builder()
                               .setAndroidReceiverCompatible(true)
                               .build()
              return CastOptions.Builder()
                              .setLaunchOptions(launchOptions)
                              ...
                              .build()
         }
      }
4. Follow the [Integration Steps](#integration-steps) mentioned below.
5. You should now be able to launch your Android TV Receiver using a sender.
6. If you are unable to launch the Android TV Receiver or have any other issues follow the [Troubleshooting Guide](https://console.vizbee.tv/app/vzb2000001/develop/guides/firetv-androidtv-troubleshooting-snippets).

## Integration Steps
Look for the block comments with text "[BEGIN] Vizbee Integration" and "[END] Vizbee Integration" in the code for an easy understanding of the integration.

### Build Setup
1. Add the Vizbee repository to your Android TV receiver app’s root [build.gradle](/build.gradle).
2. Add Vizbee SDK dependency to your app module's [build.gradle](/app/build.gradle).

### Manifest Setup
1. Add the required intent filters to the app's [AndroidManifest.xml](/app/src/main/AndroidManifest.xml).
2. Create and implement the [ReceiverOptionsProvider](/app/src/main/java/tv/vizbee/demo/atvreceiver/cast/CastReceiverOptionsProvider.java) and refer it from [AndroidManifest.xml](/app/src/main/AndroidManifest.xml).
3. Handle the load intent in your activity responsible for loading video. In Demo App, it is [VideoPlayerActivity](/app/src/main/java/tv/vizbee/demo/atvreceiver/ui/VideoPlayerActivity.java).

### Code Setup
1. Copy the files under [cast package](/app/src/main/java/tv/vizbee/demo/atvreceiver/cast) to your app under an appropriate package.

### SDK Initialisation
1. In your [application](/app/src/main/java/tv/vizbee/demo/atvreceiver/ATVVZBDemoApplication.java) class, initialise Vizbee Android TV Receiver SDK via [VizbeeWrapper](/app/src/main/java/tv/vizbee/demo/atvreceiver/cast/VizbeeWrapper.java) helper file.

### App Adapter
1. Implement `onStart()` and `onEvent()` methods of [AppAdapter](app/src/main/java/tv/vizbee/demo/atvreceiver/cast/AppAdapter.java).
2. Implement `onStart()` to handle a start video request sent by the Mobile app.
3. Implement `onEvent()` to handle a sign in request sent by the Mobile app to automatically sign in the user from the Mobile app.

### Player Adapter
1. If your app has a standard MediaSession/MediaSessionCompat tied to your video player, use [BMMediaSessionCompatPlayerAdapter](app/src/main/java/tv/vizbee/demo/atvreceiver/cast/bitmovinplayer/BMMediaSessionCompatPlayerAdapter.java) and modify the implementation as per your app's video player logic.
2. If your app has a non-standard interface to your video player, use one of [BMVizbeePlayerAdapter](app/src/main/java/tv/vizbee/demo/atvreceiver/cast/bitmovinplayer/BMVizbeePlayerAdapter.java) and [ExoVizbeePlayerAdapter](app/src/main/java/tv/vizbee/demo/atvreceiver/cast/bitmovinplayer/BMVizbeePlayerAdapter.java) as per your app's video player.
3. Based on your choice of Player Adapter, remove the other adapters from your code.
4. Call `setPlayerAdapter()` before the start of every video playback.
5. Call `resetPlayerAdapter()` when the video stops, fails, ends or if the containing Fragment or Activity is stopped or destroyed.
6. Refer to [BitMovinPlayerFragment](app/src/main/java/tv/vizbee/demo/atvreceiver/player/BitMovinPlayerFragment.java) or [ExoPlayerFragment](app/src/main/java/tv/vizbee/demo/atvreceiver/player/ExoPlayerFragment.java) for the examples of `setPlayerAdapter()` and `resetPlayerAdapter()`.


## Documentation
* [Vizbee Android TV Overview and Developer Guide](https://console.vizbee.tv/app/vzb2000001/develop/guides/firetv-androidtv-sdk)
* [Code Snippets](https://console.vizbee.tv/app/vzb2000001/develop/guides/firetv-androidtv-snippets)
* [Troubleshooting](https://console.vizbee.tv/app/vzb2000001/develop/guides/firetv-androidtv-troubleshooting-snippets)
