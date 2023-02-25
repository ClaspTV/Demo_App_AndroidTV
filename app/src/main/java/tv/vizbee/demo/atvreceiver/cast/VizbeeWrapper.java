package tv.vizbee.demo.atvreceiver.cast;

import android.app.Application;

import org.json.JSONException;
import org.json.JSONObject;

import tv.vizbee.screen.api.Vizbee;
import tv.vizbee.screen.api.VizbeeOptions;

import tv.vizbee.demo.atvreceiver.ui.MainActivity;

public class VizbeeWrapper {

    private static volatile VizbeeWrapper sInstance;

    private VizbeeOptions vizbeeOptions;

    private VizbeeWrapper() {
    }

    public static VizbeeWrapper getInstance() {

        if (null == sInstance) {
            synchronized (VizbeeWrapper.class) {
                if (null == sInstance) {
                    sInstance = new VizbeeWrapper();
                }
            }
        }

        return sInstance;
    }

    public void init(Application application, MainActivity mainActivity) {

        Vizbee.getInstance().enableVerboseLogging();

        // optional VizbeeOptions
        JSONObject attributes = new JSONObject();
        try {
            attributes.put("TEST_CUSTOM_KEY", "TEST_CUSTOM_VALUE");
        } catch (JSONException e) {
        }
        vizbeeOptions = new VizbeeOptions.Builder()
                .setCustomMetricsAttributes(attributes)
                .build();
        Vizbee.getInstance().initialize(application,
                "vzb2000001",
                new AppAdapter(mainActivity),
                vizbeeOptions);
    }

    public VizbeeOptions getVizbeeOptions() {
        return vizbeeOptions;
    }
}
