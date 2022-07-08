/*
* Copyright (C) 2017 The OmniROM Project
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
package org.omnirom.device.rgb;

import android.graphics.Color;
import android.os.Bundle;
import androidx.preference.ListPreference;
import androidx.preference.PreferenceFragment;
import androidx.preference.Preference;
import androidx.preference.TwoStatePreference;
import android.provider.Settings;
import android.util.Log;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.omnirom.device.Utils;
import org.omnirom.device.R;
import org.omnirom.omnilib.preference.SystemSettingsColorSelectPreference;

public class RGBSettings extends PreferenceFragment implements
        Preference.OnPreferenceChangeListener {

    private static final String TAG = "RGBSettingsController";

    public static final String KEY_APPLY_SWITCH = "apply_settings";
    public static final String KEY_RGB_LED_SWITCH = "rgb_led";
    public static final String KEY_MODE_SWITCH = "mode_led";
    public static final String KEY_LED_ON = "led_on";
    public static final String RED_LED = "red_led";
    public static final String GREEN_LED = "green_led";
    public static final String BLUE_LED = "blue_led";
    private static final String LOW_COLOR_PREF = "battery_light_low_color";
    private static final String KEY_FRAME_MODE = "scenario_key";
    public static final String FPS = "scenario";

    private static ListPreference mFrameModeRate;
    private static TwoStatePreference mRgbLedSwitch;
    private static SystemSettingsColorSelectPreference mLowColorPref;

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.rgb_settings, rootKey);

        mRgbLedSwitch = (TwoStatePreference) findPreference(KEY_RGB_LED_SWITCH);
        mRgbLedSwitch.setChecked(Settings.System.getInt(getContext().getContentResolver(),
                KEY_RGB_LED_SWITCH, 0) == 1);

        mLowColorPref = (SystemSettingsColorSelectPreference) findPreference(LOW_COLOR_PREF);
        mLowColorPref.setDefaultValue(0);
        mLowColorPref.setOnPreferenceChangeListener(this);

        mFrameModeRate = (ListPreference) findPreference(KEY_FRAME_MODE);
        int framevalue = Settings.System.getInt(getContext().getContentResolver(),
                            FPS, 0);
        mFrameModeRate.setValue(Integer.toString(framevalue));
        mFrameModeRate.setSummary(mFrameModeRate.getEntry());
        mFrameModeRate.setOnPreferenceChangeListener(this);
    }

    @Override
    public boolean onPreferenceTreeClick(Preference preference) {
        if (preference == mRgbLedSwitch) {
            Settings.System.putInt(getContext().getContentResolver(), KEY_RGB_LED_SWITCH, mRgbLedSwitch.isChecked() ? 1 : 0);
            boolean enabled = Settings.System.getInt(getContext().getContentResolver(), KEY_RGB_LED_SWITCH, 0) != 0;
            setLedsEnabled(KEY_RGB_LED_SWITCH, enabled);
            setLedsEnabled(KEY_APPLY_SWITCH, enabled);
            setLedsEnabled(KEY_LED_ON, enabled);
            return true;
        }
        return super.onPreferenceTreeClick(preference);
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {
        if (preference == mLowColorPref) {
            int color = (Integer) newValue;
            setGlobalValue(RED_LED, Color.red(color));
            setGlobalValue(GREEN_LED, Color.green(color));
            setGlobalValue(BLUE_LED, Color.blue(color));
            Settings.System.putInt(getContext().getContentResolver(), RED_LED, Color.red(color));
            Settings.System.putInt(getContext().getContentResolver(), GREEN_LED, Color.green(color));
            Settings.System.putInt(getContext().getContentResolver(), BLUE_LED, Color.blue(color));
        }
        if (preference == mFrameModeRate) {
            int value = Integer.valueOf((String) newValue);
            int index = mFrameModeRate.findIndexOfValue((String) newValue);
            mFrameModeRate.setSummary(mFrameModeRate.getEntries()[index]);
            setGlobalValue(KEY_MODE_SWITCH, value);
            Settings.System.putInt(getContext().getContentResolver(), FPS, value);
        }
        return true;
    }

    private void setGlobalValue(String key, int values) {
        Utils.writeValue(getLedsFile(key), Integer.toString(values));
    }

    private void setLedsEnabled(String key, boolean enabled) {
        Utils.writeValue(getLedsFile(key), enabled ? "1" : "0");
    }

    public static String getLedsFile(String key) {
        switch(key) {
            case KEY_APPLY_SWITCH:
                return "/sys/class/leds/aura_sync/apply";
            case KEY_MODE_SWITCH:
                return "/sys/class/leds/aura_sync/mode";
            case KEY_RGB_LED_SWITCH:
                return "/sys/class/leds/aura_sync/led_on";
            case KEY_LED_ON:
                return "/sys/class/leds/aura_sync/CSCmode";
            case RED_LED:
                return "/sys/class/leds/aura_sync/red_pwm";
            case GREEN_LED:
                return "/sys/class/leds/aura_sync/green_pwm";
            case BLUE_LED:
                return "/sys/class/leds/aura_sync/blue_pwm";
        }
        return null;
    }
}
