
package com.n00bware.propmodder;

import android.net.Uri;
import android.os.Bundle;
import android.content.Intent;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.PreferenceActivity;
import android.preference.PreferenceScreen;


public class Credit extends PreferenceActivity implements
        Preference.OnPreferenceChangeListener {

    private static final String JBIRDVEGAS_PREF = "pref_jbirdvegas";
    private static final String REVNUMBERS_PREF = "pref_revnumbers";

    private ListPreference mJBirdVegas;
    private ListPreference mRevNumbers;

    @Override
    public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

        setTitle(R.string.credit_title);
        addPreferencesFromResource(R.xml.credit);
        PreferenceScreen prefSet = getPreferenceScreen();

        mJBirdVegas = (ListPreference) prefSet.findPreference(JBIRDVEGAS_PREF);
        mJBirdVegas.setOnPreferenceChangeListener(this);

        mRevNumbers = (ListPreference) prefSet.findPreference(REVNUMBERS_PREF);
        mRevNumbers.setOnPreferenceChangeListener(this);

    }

    public boolean onPreferenceChange(Preference preference, Object newValue) {
        if (newValue != null) {
            if (preference != null) {
                return launchBrowser(newValue.toString());
            }
        }

        return false;
    }

    private boolean launchBrowser(String urlValue) {
        if (urlValue != null) {
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(urlValue));
            startActivity(browserIntent);
			return true;
        }

        return false;
    }
}
