package tv.vizbee.demo.atvreceiver.cast;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;


import com.google.android.gms.cast.tv.CastReceiverOptions;
import com.google.android.gms.cast.tv.ReceiverOptionsProvider;

import java.util.Collections;

/**
 * Define ReceiverOptionsProvider for the Cast SDK to know CastReceiverOptions.
 * It's crucial to specify custom name space with "urn:x-cast:tv.vizbee.sync" for the connection
 * with Sender to work.
 */
public class CastReceiverOptionsProvider implements ReceiverOptionsProvider {

   private static final String LOG_TAG = CastReceiverOptionsProvider.class.getSimpleName();

   @NonNull
   @Override
   public CastReceiverOptions getOptions(@NonNull Context context) {

      Log.v(LOG_TAG, "getOptions");

      return new CastReceiverOptions.Builder(context)
              .setVersionCode(1)
              .setCustomNamespaces(Collections.singletonList("urn:x-cast:tv.vizbee.sync"))
              .setStatusText("Cast Vizbee Demo App Receiver")
              .build();
   }
}
