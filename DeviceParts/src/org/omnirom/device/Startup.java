/*
* Copyright (C) 2013 The OmniROM Project
*
* This program is free software: you can redistribute it and/or modify
* it under the terms of the GNU General Public License as published by
* the Free Software Foundation, either version 2 of the License, or
* (at your option) any later version.
*
* This program is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY; without even the implied warranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
* GNU General Public License for more details.
*
* You should have received a copy of the GNU General Public License
* along with this program. If not, see <http://www.gnu.org/licenses/>.
*
*/
package org.omnirom.device;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import androidx.preference.PreferenceManager;
import android.os.UserHandle;
import android.provider.Settings;
import android.text.TextUtils;

public class Startup extends BroadcastReceiver {
    private static final String ASUS_GAMEMODE = "asus_gamemode";

    private static void restore(String file, boolean enabled) {
        if (file == null) {
            return;
        }
        Utils.writeValue(file, enabled ? "1" : "0");
    }

    private static void restore(String file, String value) {
        if (file == null) {
            return;
        }
        Utils.writeValue(file, value);
    }

    private static String getGestureFile(String key) {
        return GestureSettings.getGestureFile(key);
    }

    private void maybeImportOldSettings(Context context) {
        boolean imported = Settings.System.getInt(context.getContentResolver(), "omni_device_setting_imported", 0) != 0;
        if (!imported) {
            SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(context);
            boolean enabled = sharedPrefs.getBoolean(DeviceSettings.KEY_GLOVE_SWITCH, false);
            Settings.System.putInt(context.getContentResolver(), GloveModeSwitch.SETTINGS_KEY, enabled ? 1 : 0);

            Settings.System.putInt(context.getContentResolver(), "omni_device_setting_imported", 1);
        }
    }

    @Override
    public void onReceive(final Context context, final Intent bootintent) {
        maybeImportOldSettings(context);
        restoreAfterUserSwitch(context);
        if (DeviceSettings.isRog3) {
            context.startServiceAsUser(new Intent(context, GripSensorServiceMain.class),
                UserHandle.CURRENT);
        }
    }

    public static void restoreAfterUserSwitch(Context context) {

        // E Gesture
        String mapping = GestureSettings.DEVICE_GESTURE_MAPPING_0;
        String value = Settings.System.getString(context.getContentResolver(), mapping);
        if (TextUtils.isEmpty(value)) {
            value = AppSelectListPreference.DISABLED_ENTRY;
            Settings.System.putString(context.getContentResolver(), mapping, value);
        }
        boolean enabled = !value.equals(AppSelectListPreference.DISABLED_ENTRY);
        restore(GestureSettings.getGestureFile(GestureSettings.KEY_E_APP), enabled);

        // M Gesture
        mapping = GestureSettings.DEVICE_GESTURE_MAPPING_1;
        value = Settings.System.getString(context.getContentResolver(), mapping);
        if (TextUtils.isEmpty(value)) {
            value = AppSelectListPreference.DISABLED_ENTRY;
            Settings.System.putString(context.getContentResolver(), mapping, value);
        }
        enabled = !value.equals(AppSelectListPreference.DISABLED_ENTRY);
        restore(GestureSettings.getGestureFile(GestureSettings.KEY_M_APP), enabled);

        // S Gesture
        mapping = GestureSettings.DEVICE_GESTURE_MAPPING_2;
        value = Settings.System.getString(context.getContentResolver(), mapping);
        if (TextUtils.isEmpty(value)) {
            value = AppSelectListPreference.DISABLED_ENTRY;
            Settings.System.putString(context.getContentResolver(), mapping, value);
        }
        enabled = !TextUtils.isEmpty(value) && !value.equals(AppSelectListPreference.DISABLED_ENTRY);
        restore(GestureSettings.getGestureFile(GestureSettings.KEY_S_APP), enabled);

        // V Gesture
        mapping = GestureSettings.DEVICE_GESTURE_MAPPING_3;
        value = Settings.System.getString(context.getContentResolver(), mapping);
        if (TextUtils.isEmpty(value)) {
            value = AppSelectListPreference.DISABLED_ENTRY;
            Settings.System.putString(context.getContentResolver(), mapping, value);
        }
        enabled = !value.equals(AppSelectListPreference.DISABLED_ENTRY);
        restore(GestureSettings.getGestureFile(GestureSettings.KEY_V_APP), enabled);

        // W Gesture
        mapping = GestureSettings.DEVICE_GESTURE_MAPPING_4;
        value = Settings.System.getString(context.getContentResolver(), mapping);
        if (TextUtils.isEmpty(value)) {
            value = AppSelectListPreference.DISABLED_ENTRY;
            Settings.System.putString(context.getContentResolver(), mapping, value);
        }
        enabled = !value.equals(AppSelectListPreference.DISABLED_ENTRY);
        restore(GestureSettings.getGestureFile(GestureSettings.KEY_W_APP), enabled);

        // Z Gesture
        mapping = GestureSettings.DEVICE_GESTURE_MAPPING_5;
        value = Settings.System.getString(context.getContentResolver(), mapping);
        if (TextUtils.isEmpty(value)) {
            value = AppSelectListPreference.DISABLED_ENTRY;
            Settings.System.putString(context.getContentResolver(), mapping, value);
        }
        enabled = !value.equals(AppSelectListPreference.DISABLED_ENTRY);
        restore(GestureSettings.getGestureFile(GestureSettings.KEY_Z_APP), enabled);

        enabled = Settings.System.getInt(context.getContentResolver(), GloveModeSwitch.SETTINGS_KEY, 0) != 0;
        if (enabled) {
            restore(GloveModeSwitch.getFile(), enabled);
        }

        boolean enabledGesture = Settings.System.getInt(context.getContentResolver(), GestureSettings.SETTINGS_GESTURE_KEY, 0) != 0;
        if (enabledGesture) {
            restore(GestureSettings.getFile(), enabledGesture);
        }

        String valueExtra = Settings.System.getString(context.getContentResolver(), GestureSettings.SETTINGS_ZENMOTION_KEY);
        if (!TextUtils.isEmpty(valueExtra)) {
             Utils.writeLine(GestureSettings.OFFSCREEN_PATH, valueExtra);
        }

        value = Settings.System.getString(context.getContentResolver(), DeviceSettings.FPS);
        if (TextUtils.isEmpty(value)) {
            value = DeviceSettings.DEFAULT_FPS_VALUE;
            Settings.System.putString(context.getContentResolver(), DeviceSettings.FPS, value);
            DeviceSettings.changeFps(context, Integer.valueOf(value));
        } else {
        DeviceSettings.changeFps(context, Integer.valueOf(value));
        }

        if (DeviceSettings.isRog3) {
            value = Settings.Global.getString(context.getContentResolver(), ASUS_GAMEMODE);
            if (TextUtils.isEmpty(value)) {
                value = "0";
                Settings.Global.putString(context.getContentResolver(), ASUS_GAMEMODE, value);
            }
        }
    }
}
