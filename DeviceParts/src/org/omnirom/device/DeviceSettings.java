/*
* Copyright (C) 2016 The OmniROM Project
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

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Parcel;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.os.UserHandle;
import androidx.preference.PreferenceFragment;
import androidx.preference.ListPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceCategory;
import androidx.preference.PreferenceScreen;
import androidx.preference.SwitchPreference;
import androidx.preference.TwoStatePreference;
import android.provider.Settings;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.util.Log;
import java.util.Map;

public class DeviceSettings extends PreferenceFragment implements
        Preference.OnPreferenceChangeListener {

    public static final String KEY_SETTINGS_PREFIX = "device_setting_";
    public static final String KEY_GLOVE_SWITCH = "glove";

    private static final String KEY_CATEGORY_AUDIO = "audio_category";
    private static final String KEY_CATEGORY_BATTERY = "category_battery";
    private static final String KEY_CATEGORY_MOTOR = "category_motor";
    private static final String KEY_CATEGORY_GAMING = "category_gaming";
    private static final String KEY_CATEGORY_SCREEN = "screen";
    public static final String KEY_GAME_GENIE = "game_toolbar_app";
    private static final String KEY_FRAME_MODE = "frame_mode_key";
    private static final String KEY_FRAME_MODE_ZF7 = "frame_mode_key_zf7";
    private static final String KEY_FRAME_CATEGORY = "frame_mode_main";
    private static final String KEY_FRAME_CATEGORY_ZF7 = "frame_mode_main_zf7";
    public static final String FPS = "fps";

    protected static final String DEFAULT_FPS_VALUE = "60";
    private static final String ACTION_AIR_TRIGGER_OFF = "com.asus.airtriggers.SYSTEMUI_AIR_TRIGGER_OFF";
    private static final String ACTION_AIR_TRIGGER_ON = "com.asus.airtriggers.SYSTEMUI_AIR_TRIGGER_ON";
    private static final String AIRTRIGGER_PACKAGE_NAME = "com.asus.airtriggers";
    private static final String FIELD_AIR_TRIGGER_ENABLE = "air_trigger_enable";
    public static final String KEY_AIRTRIGGER = "grip_sensor_apk";
    public static final String KEY_AIRTRIGGER_PREF = "grip_sensor_pref";
    private static final String TAG = "AirTriggerApkPreferenceController";

    private Airtrigger mAirtrigger;
    private static Preference mAirtriggerPref;
    private static ListPreference mFrameModeRate;
    private static ListPreference mFrameModeRateZF7;
    private static SwitchPreference mGripSensorPreference;
    private static TwoStatePreference mGloveModeSwitch;
    private static Preference mGameGenie;

    protected static final boolean isRog3 = android.os.Build.DEVICE.equals("ASUS_I003_1");
    private static final String SURFACE_FLINGER_SERVICE_KEY = "SurfaceFlinger";
    private static final String SURFACE_COMPOSER_INTERFACE_KEY = "android.ui.ISurfaceComposer";
    private static final int SURFACE_FLINGER_CODE = 1035;
    private static Map<Integer, Integer> fpsMap = Map.of(160, 0, 144, 1, 120, 2, 90, 3, 60, 4);
    private static Map<Integer, Integer> fpsMapZF7 = Map.of(90, 0, 60, 1);
    private static IBinder mSurfaceFlinger;

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.main, rootKey);
        final PreferenceScreen prefScreen = getPreferenceScreen();
        final PreferenceCategory audioCategory =
                (PreferenceCategory) prefScreen.findPreference(KEY_CATEGORY_AUDIO);
        final PreferenceCategory fpsCategory =
                (PreferenceCategory) prefScreen.findPreference(KEY_FRAME_CATEGORY);
        final PreferenceCategory fpsCategoryzf7 =
                (PreferenceCategory) prefScreen.findPreference(KEY_FRAME_CATEGORY_ZF7);
        final PreferenceCategory GamingCategory =
                (PreferenceCategory) prefScreen.findPreference(KEY_CATEGORY_GAMING);
        final PreferenceCategory BatteryCategory =
                (PreferenceCategory) prefScreen.findPreference(KEY_CATEGORY_BATTERY);
        final PreferenceCategory MotorCategory =
                (PreferenceCategory) prefScreen.findPreference(KEY_CATEGORY_MOTOR);

        mFrameModeRate = (ListPreference) findPreference(KEY_FRAME_MODE);
        int framevalue = Settings.System.getInt(getContext().getContentResolver(),
                            FPS, 60);
        mFrameModeRate.setValue(Integer.toString(framevalue));
        mFrameModeRate.setSummary(mFrameModeRate.getEntry());
        mFrameModeRate.setOnPreferenceChangeListener(this);

        mFrameModeRateZF7 = (ListPreference) findPreference(KEY_FRAME_MODE_ZF7);
        int framevaluezf7 = Settings.System.getInt(getContext().getContentResolver(),
                            FPS, 60);
        mFrameModeRateZF7.setValue(Integer.toString(framevaluezf7));
        mFrameModeRateZF7.setSummary(mFrameModeRateZF7.getEntry());
        mFrameModeRateZF7.setOnPreferenceChangeListener(this);

        if (isRog3) {
            prefScreen.removePreference(fpsCategoryzf7);
        } else {
            prefScreen.removePreference(audioCategory);
            prefScreen.removePreference(BatteryCategory);
            prefScreen.removePreference(fpsCategory);
        }

        mGloveModeSwitch = (TwoStatePreference) findPreference(KEY_GLOVE_SWITCH);
        mGloveModeSwitch.setEnabled(GloveModeSwitch.isSupported());
        mGloveModeSwitch.setChecked(GloveModeSwitch.isCurrentlyEnabled(this.getContext()));
        mGloveModeSwitch.setOnPreferenceChangeListener(new GloveModeSwitch(getContext()));

        mGameGenie = findPreference(KEY_GAME_GENIE);
        mGameGenie.setEnabled(GameGenie.isGameGenieExist(this.getContext()));

        mAirtriggerPref = findPreference(KEY_AIRTRIGGER_PREF);
        mAirtriggerPref.setOnPreferenceChangeListener(this);
        if (!isRog3) {
            prefScreen.removePreference(GamingCategory);
        }

        if (isRog3) {
            mGripSensorPreference = (SwitchPreference) findPreference(KEY_AIRTRIGGER);
            mGripSensorPreference.setChecked(Settings.Global.getInt(getContext().getContentResolver(),
            FIELD_AIR_TRIGGER_ENABLE, 0) == 1);
            mGripSensorPreference.setOnPreferenceChangeListener(this);

            // Remove the camera motor category for the Rog3
            prefScreen.removePreference(MotorCategory);
        }

    }

    @Override
    public boolean onPreferenceTreeClick(Preference preference) {
        if (preference == mAirtriggerPref) {
            mAirtrigger.startAirTriggerSettings(this.getContext());
        }
        return super.onPreferenceTreeClick(preference);
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {
        if (preference == mFrameModeRate) {
            int value = Integer.valueOf((String) newValue);
            int index = mFrameModeRate.findIndexOfValue((String) newValue);
            mFrameModeRate.setSummary(mFrameModeRate.getEntries()[index]);
            changeFps(getContext(), value);
            Settings.System.putInt(getContext().getContentResolver(), FPS, value);
        }
        if (preference == mFrameModeRateZF7) {
            int value = Integer.valueOf((String) newValue);
            int index = mFrameModeRateZF7.findIndexOfValue((String) newValue);
            mFrameModeRateZF7.setSummary(mFrameModeRateZF7.getEntries()[index]);
            changeFps(getContext(), value);
            Settings.System.putInt(getContext().getContentResolver(), FPS, value);
        }
        if (preference == mGripSensorPreference) {
            notifySwitchState(((Boolean) newValue).booleanValue());
        }
        return true;
    }

    protected static void changeFps(Context context, int fps) {
        mSurfaceFlinger = ServiceManager.getService(SURFACE_FLINGER_SERVICE_KEY);
        try {
            if (mSurfaceFlinger != null) {
                mSurfaceFlinger = ServiceManager.getService(SURFACE_FLINGER_SERVICE_KEY);
                Parcel data = Parcel.obtain();
                data.writeInterfaceToken(SURFACE_COMPOSER_INTERFACE_KEY);
                if (isRog3) {
                    data.writeInt(fpsMap.getOrDefault(fps, -1));
                } else {
                    data.writeInt(fpsMapZF7.getOrDefault(fps, -1));
                }
                mSurfaceFlinger.transact(SURFACE_FLINGER_CODE, data, null, 0);
                data.recycle();
                if (isRog3) {
                    System.out.println("OmnifpsMap: " + fpsMap +" Omnifps: "+ fps);
                } else {
                    System.out.println("OmnifpsMap: " + fpsMapZF7 +" Omnifps: "+ fps);
                }
            }
        } catch (RemoteException ex) {
               // intentional no-op
        }
            Settings.System.putInt(context.getContentResolver(), DeviceSettings.FPS, fps);
    }

    private void notifySwitchState(boolean z) {
        Log.d(TAG, "notifySwitchState enabled=" + z);
        Intent intent = new Intent();
        intent.setAction(z ? ACTION_AIR_TRIGGER_ON : ACTION_AIR_TRIGGER_OFF);
        intent.setPackage(AIRTRIGGER_PACKAGE_NAME);
        getContext().sendBroadcastAsUser(intent, UserHandle.CURRENT);
    }
}
