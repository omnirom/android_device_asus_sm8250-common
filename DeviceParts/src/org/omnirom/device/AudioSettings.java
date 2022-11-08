/*
* Copyright (C) 2023 The OmniROM Project
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

import android.content.pm.PackageManager;
import android.os.Bundle;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.Preference;
import androidx.preference.PreferenceScreen;
import androidx.preference.TwoStatePreference;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;

public class AudioSettings extends PreferenceFragmentCompat implements
        Preference.OnPreferenceChangeListener {

    private static final String TAG = "AudioWizardControllerOmni";

    private static final String KEY_AUDIOWIZARD = "audiowizard_entry";
    private static final String KEY_OUTDOOR_MODE = "outdoor_mode";
    private static final String PACKAGE_NAME = "com.asus.audiowizard";
    private static final String SETTINGS_KEY_AUDIO_OUTDOOR_MODE = "audio_outdoor_mode";
    private static final String SETTINGS_KEY_AUDIO_SUMMARY_KEY = "settings_key_audio_wizard_summary";
    private static final int SETTINGS_AUDIO_WIZARD_OUTDOOR_MODE_OFF = 0;
    private static final int SETTINGS_AUDIO_WIZARD_OUTDOOR_MODE_ON = 1;

    private TwoStatePreference mOutdoorMode;
    private Preference mWizard;

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.audio_mode, rootKey);
        final PreferenceScreen prefScreen = getPreferenceScreen();

        mOutdoorMode = (TwoStatePreference) findPreference(KEY_OUTDOOR_MODE);
        mOutdoorMode.setChecked(Settings.System.getInt(getContext().getContentResolver(),
            SETTINGS_KEY_AUDIO_OUTDOOR_MODE,
            SETTINGS_AUDIO_WIZARD_OUTDOOR_MODE_OFF)==
            SETTINGS_AUDIO_WIZARD_OUTDOOR_MODE_ON);

        mWizard = prefScreen.findPreference(KEY_AUDIOWIZARD);
        mWizard.setEnabled(isEnable());
        updateState();

    }

    @Override
    public boolean onPreferenceTreeClick(Preference preference) {
        if (preference == mOutdoorMode) {
            Settings.System.putInt(getContext().getContentResolver(),
                SETTINGS_KEY_AUDIO_OUTDOOR_MODE, mOutdoorMode.isChecked() ?
                SETTINGS_AUDIO_WIZARD_OUTDOOR_MODE_ON :
                SETTINGS_AUDIO_WIZARD_OUTDOOR_MODE_OFF);
            updateState();
            return true;
        }
        return super.onPreferenceTreeClick(preference);
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {
        return true;
    }

    private void updateState() {
        String summaryString = getSummaryString();
        Log.i(TAG, "updateState summary = " + summaryString);
        if (!TextUtils.isEmpty(summaryString)) {
            mWizard.setSummary(summaryString);
        } else {
            mWizard.setSummary("");
        }
    }

    private boolean isEnable() {
        try {
            boolean UIenable = getContext().getPackageManager().getApplicationInfo(PACKAGE_NAME, 0).enabled;
            Log.d(TAG, " UIenable = " + UIenable);
            return UIenable;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return false;
        }
    }

    private String getSummaryString() {
        return Settings.System.getString(getContext().getContentResolver(), SETTINGS_KEY_AUDIO_SUMMARY_KEY);
    }
}
