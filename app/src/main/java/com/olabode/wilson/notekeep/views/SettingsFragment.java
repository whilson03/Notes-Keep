package com.olabode.wilson.notekeep.views;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.preference.ListPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;

import com.olabode.wilson.notekeep.R;
import com.olabode.wilson.notekeep.utils.Constants;

import java.util.Objects;

public class SettingsFragment extends PreferenceFragmentCompat {
    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.settings_main, rootKey);
        Objects.requireNonNull(getActivity()).setTitle("Settings");


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        Preference feedBackPreference = findPreference(Constants.SharedPreferenceKeys.FEEDBACK_PREFERENCE_KEY);
        feedBackPreference.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                sendFeedback();
                return true;
            }
        });

        return super.onCreateView(inflater, container, savedInstanceState);

    }


    /**
     * send feedback email
     */
    public void sendFeedback() {
        Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:" + "whilson03@gmail.com"));
        intent.putExtra(Intent.EXTRA_SUBJECT, "feedback from NotesKeep app");
        intent.putExtra(Intent.EXTRA_TEXT, "message");
        startActivity(Intent.createChooser(intent, "Send Email"));
    }


    // The below method sets the Preference Summary as per selected.
    private void setPreferenceSummary(Preference preference, String value) {
        if (preference instanceof ListPreference) {
            // For list preferences, figure out the label of the selected value
            ListPreference listPreference = (ListPreference) preference;
            int prefIndex = listPreference.findIndexOfValue(value);
            if (prefIndex >= 0) {
                // Set the summary to that label
                listPreference.setSummary(listPreference.getEntries()[prefIndex]);
            }
        }
    }

}