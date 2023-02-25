package tv.vizbee.demo.atvreceiver.data;

import java.io.Serializable;

public class Video implements Serializable {

    private String guid;
    private String title;
    private String subtitle;
    private String videoURL;
    private String imageURL;
    private boolean isLive;

    public Video() {
    }

    public Video(String guid, String title, String subtitle, String imageURL, String videoURL, Boolean isLive) {
        this.title = title;
        this.subtitle = subtitle;
        this.guid = guid;
        this.videoURL = videoURL;
        this.imageURL = imageURL;
        this.isLive = isLive;
    }

    public String getGuid() {
        return guid;
    }

    public String getTitle() {
        return title;
    }
    public void setTitle(String title) { this.title = title; }

    public String getSubtitle() {
        return subtitle;
    }
    public void setSubtitle(String subtitle) { this.subtitle = subtitle; }

    public String getVideoURL() {
        return videoURL;
    }
    public void setVideoURL(String videoURL) { this.videoURL = videoURL; }

    public String getImageURL() { return imageURL; }
    public void setImageURL(String imageURL) { this.imageURL = imageURL; }

    public boolean isLive() {
        return isLive;
    }
    public void setLive(boolean live) {
        isLive = live;
    }

    @Override
    public String toString() {
        return "Video{" +
                "id=" + guid +
                ", title='" + title + '\'' +
                ", subtitle='" + subtitle + '\'' +
                ", videoUrl='" + videoURL + '\'' +
                ", imageUrl='" + imageURL + '\'' +
                ", isLive='" + isLive + '\'' +
                '}';
    }
}
