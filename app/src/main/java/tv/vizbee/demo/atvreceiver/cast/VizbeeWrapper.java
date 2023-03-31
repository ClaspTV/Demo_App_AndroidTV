package tv.vizbee.demo.atvreceiver.cast;

import android.app.Application;

import org.json.JSONException;
import org.json.JSONObject;

import tv.vizbee.demo.atvreceiver.ATVVZBDemoApplication;
import tv.vizbee.demo.atvreceiver.cast.applifecycle.VizbeeAppLifecycleAdapter;
import tv.vizbee.screen.api.Vizbee;
import tv.vizbee.screen.api.VizbeeOptions;

public class VizbeeWrapper {

    private static String VIZBEE_APP_ID = "vzb2000001";
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

    public void init(Application application) {

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
        VizbeeAppLifecycleAdapter appLifecycleAdapter =
                ((ATVVZBDemoApplication)application).getAppLifecycleAdapter();
        Vizbee.getInstance().initialize(application,
                VIZBEE_APP_ID,
                new AppAdapter(appLifecycleAdapter),
                vizbeeOptions);
    }

    public VizbeeOptions getVizbeeOptions() {
        return vizbeeOptions;
    }
}
