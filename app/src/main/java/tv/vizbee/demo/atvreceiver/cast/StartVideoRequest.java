package tv.vizbee.demo.atvreceiver.cast;

import tv.vizbee.screen.api.messages.VideoInfo;

public class StartVideoRequest {
    public VideoInfo videoInfo;
    public long position;
    public StartVideoRequest(VideoInfo videoInfo , long position) {
        this.videoInfo = videoInfo;
        this.position = position;
    }
}