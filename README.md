# Vizbee Android TV Demo App

This sample shows how to develop a Vizbee Cast enabled Android TV app.

## Setup Instructions
1. Use an existing Web Receiver or use Vizbee hosted Web Receiver
2. Follow the steps for [setting up the Cast Developer Console](https://gist.github.com/vizbee/4922c3527a5bfbb364f3db338cedaca4#failed_to_launch_nativeapp_instead_launching_cc).
3. Follow the steps for setting up your sender app for Cast Connect support.
   1. Once you have integrated Vizbee into your sender app, you can declare its readiness to cast 
      to Android TV by setting the androidReceiverCompatible flag on LaunchOptions to true.
      `class CastOptionsProvider : OptionsProvider {
            override fun getCastOptions(context: Context?): CastOptions {
            val launchOptions: LaunchOptions = Builder()
                               .setAndroidReceiverCompatible(true)
                               .build()
            return CastOptions.Builder()
                              .setLaunchOptions(launchOptions)
                              ...
                              .build()
           }
      }`
4. You should now be able to launch your Android TV Receiver using a sender.
5. If you are unable to launch the Android TV Receiver or have any other issues follow the [Troubleshooting Guide](https://console.vizbee.tv/app/vzb2000001/develop/guides/firetv-androidtv-troubleshooting-snippets).

## Documentation
* [Vizbee Android TV Overview and Developer Guide](https://console.vizbee.tv/app/vzb2000001/develop/guides/firetv-androidtv-sdk)
* [Code Snippets](https://console.vizbee.tv/app/vzb2000001/develop/guides/firetv-androidtv-snippets)
* [Troubleshooting](https://console.vizbee.tv/app/vzb2000001/develop/guides/firetv-androidtv-troubleshooting-snippets)