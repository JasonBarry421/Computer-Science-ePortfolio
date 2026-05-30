package com.example.EventCalendarMobileApplication.Calendar.View;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceManager;
import androidx.preference.SwitchPreferenceCompat;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NavUtils;

import com.example.EventCalendarMobileApplication.Login.View.LoginActivity;
import com.example.EventCalendarMobileApplication.R;
import com.example.EventCalendarMobileApplication.Session;


public class SettingsActivity extends AppCompatActivity {
    SharedPreferences mPreferences;
    Boolean isDark;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Checks whether Dark Mode was enabled
        mPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        isDark = mPreferences.getBoolean("theme", false);

        setContentView(R.layout.settings_activity);
        if (savedInstanceState == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.settingsFrameLayout, new SettingsFragment())
                    .commit();
        }

        setTitle("Settings");
    }

    @Override
    protected void onResume(){
        super.onResume();

        // Recreates the Screen whenever the Dark Mode Setting Changes
        if (isDark != mPreferences.getBoolean("theme", false)){
            recreate();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            NavUtils.navigateUpFromSameTask(this);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.basic_menu, menu);
        return true;
    }


    public static class SettingsFragment extends PreferenceFragmentCompat {
        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
            setPreferencesFromResource(R.xml.root_preferences, rootKey);

            // Finds and Listens for Theme Preference Click
            SwitchPreferenceCompat themePref = findPreference("theme_pref");
            if (themePref != null) {
                themePref.setOnPreferenceChangeListener((preference, newValue) -> {
                    // Turn on or off night mode
                    if ((Boolean) newValue) {
                        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                    } else {
                        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                    }
                    return true;
                });
            }

            // Finds and Listens for Logout Preference
            Preference logoutPref = findPreference("logout_pref");
            if (logoutPref != null){
                logoutPref.setOnPreferenceClickListener(preference -> {
                    // If Clicked, Disables Current User ID
                    Session.currentUserId = -1;

                    // Goes to Login Screen
                    Intent intent = new Intent(getActivity(), LoginActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);

                    return true;
                });
            }
        }
    }
}