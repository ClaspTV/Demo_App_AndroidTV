package tv.vizbee.demo.atvreceiver.data;

import java.util.Arrays;
import java.util.List;

public class VideoCatalog {

    public static final String VIDEO_CATEGORY[] = {
            "VOD",
            // LIVE
    };

    public static List<Video> allVideos() {
        return Arrays.asList(
                new Video(
                        "elephants",
                        "Elephants Dream",
                        "2006 ‧ Sci-fi/Short ‧ 11 mins",
                        "https://s3.amazonaws.com/vizbee/images/demoapp/elephants_dream.jpg",
                        "https://commondatastorage.googleapis.com/gtv-videos-bucket/CastVideos/hls/ElephantsDream.m3u8",
                        false
                ),
                new Video(
                        "tears",
                        "Tears of Steel",
                        "2012 ‧ Sci-fi/Short ‧ 12 mins",
                        "https://s3.amazonaws.com/vizbee/images/demoapp/20732e42e9cec9dcf99dc305cb6615e3.jpg",
                        "https://commondatastorage.googleapis.com/gtv-videos-bucket/CastVideos/hls/TearsOfSteel.m3u8",
                        false
                )
//                new Video(
//                        "Akamai Live Stream",
//                        "akamai-live-stream",
//                        "https://cph-p2p-msl.akamaized.net/hls/live/2000341/test/master.m3u8",
//                        "https://chiefit.me/wp-content/uploads/2018/04/Akamai-Logo835x396.jpg",
//                )
//                new Video("Big Buck Bunny",
//                        BIG_BUCK_BUNNY,
//                        "http://commondatastorage.googleapis.com/gtv-videos-bucket/big_buck_bunny_1080p.mp4",
//                        ),
        );
    }
}
