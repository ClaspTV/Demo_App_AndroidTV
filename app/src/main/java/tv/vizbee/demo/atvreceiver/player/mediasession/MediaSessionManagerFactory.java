package tv.vizbee.demo.atvreceiver.player.mediasession;

import android.content.Context;

import com.bitmovin.player.api.Player;

public class MediaSessionManagerFactory {

    public static IMediaSessionManager create(Player bmPlayer,
                                              IMediaSessionCallback mediaSessionCallback,
                                              Context context,
                                              boolean useCompat) {
        if (useCompat) {
            return new BitmovinMediaSessionCompatManager(bmPlayer, mediaSessionCallback, context);
        } else {
            return new BitmovinMediaSessionManager(bmPlayer, mediaSessionCallback, context);
        }
    }
}
