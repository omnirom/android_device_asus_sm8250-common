package org.omnirom.device;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;

public class TileActivity extends Activity {
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        Intent intent = new Intent();
        String className = ((ComponentName) getIntent().getParcelableExtra("android.intent.extra.COMPONENT_NAME")).getClassName();
        if (className.equals("org.omnirom.device.FrameRateTileService") ||
                className.equals("org.omnirom.device.GloveModeTileService")) {
            intent.setPackage("org.omnirom.device");
            intent.setAction("org.omnirom.device.DEVICE_SETTING_PAGE");
            startActivity(intent);
        }
        finish();
    }
}
