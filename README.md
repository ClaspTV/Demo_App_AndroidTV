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
   
3. Follow the steps for setting up your sender app for Cast Connect support.
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
4. You should now be able to launch your Android TV Receiver using a sender.
5. If you are unable to launch the Android TV Receiver or have any other issues follow the [Troubleshooting Guide](https://console.vizbee.tv/app/vzb2000001/develop/guides/firetv-androidtv-troubleshooting-snippets).

## Documentation
* [Vizbee Android TV Overview and Developer Guide](https://console.vizbee.tv/app/vzb2000001/develop/guides/firetv-androidtv-sdk)
* [Code Snippets](https://console.vizbee.tv/app/vzb2000001/develop/guides/firetv-androidtv-snippets)
* [Troubleshooting](https://console.vizbee.tv/app/vzb2000001/develop/guides/firetv-androidtv-troubleshooting-snippets)
