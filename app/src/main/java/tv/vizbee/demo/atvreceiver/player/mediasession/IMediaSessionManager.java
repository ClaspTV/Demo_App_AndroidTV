package tv.vizbee.demo.atvreceiver.player.mediasession;

import tv.vizbee.demo.atvreceiver.data.Video;

public interface IMediaSessionManager {
    void create();
    void updateMetadata(Video video);
    void updatePlaybackState();
    void destroy();
    Object getMediaSessionObject();
}
