package tv.vizbee.demo.atvreceiver.player.mediasession;

public interface IMediaSessionCallback {
    void onPlay();
    void onPause();
    void onSeekTo(long pos);
    void onStop();
}
