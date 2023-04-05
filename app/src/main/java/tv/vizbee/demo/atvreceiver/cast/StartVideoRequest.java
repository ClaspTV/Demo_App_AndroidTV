package tv.vizbee.demo.atvreceiver.cast;

import tv.vizbee.screen.api.messages.VideoInfo;

/**
 * This class encapsulates the start video request coming from mobile to internally save and process
 * later.
 */
public class StartVideoRequest {
    public VideoInfo videoInfo;
    public long position;
    public StartVideoRequest(VideoInfo videoInfo , long position) {
        this.videoInfo = videoInfo;
        this.position = position;
    }
}