package com.android.calendar;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.ContentResolver;
import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.provider.CalendarContract;
import android.provider.Settings;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getFragmentManager()
                .beginTransaction()
                .replace(android.R.id.content, new GeneralPreferences())
                .commit();

        ActionBar mActionBar = getSupportActionBar();
        if (mActionBar != null)
            mActionBar.setDisplayHomeAsUpEnabled(true);

        Account[] accounts = AccountManager.get(this).getAccounts();
        if (accounts != null) {
            for (Account acct : accounts) {
                if (ContentResolver.getIsSyncable(acct, CalendarContract.AUTHORITY) > 0) {
                    PreferenceActivity.Header accountHeader = new PreferenceActivity.Header();
                    accountHeader.title = acct.name;
                    accountHeader.fragment =
                            "com.android.calendar.selectcalendars.SelectCalendarsSyncFragment";
                    Bundle args = new Bundle();
                    args.putString(CalendarContract.Calendars.ACCOUNT_NAME, acct.name);
                    args.putString(CalendarContract.Calendars.ACCOUNT_TYPE, acct.type);
                    accountHeader.fragmentArguments = args;
                }
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        } else if (item.getItemId() == R.id.action_add_account) {
            Intent nextIntent = new Intent(Settings.ACTION_ADD_ACCOUNT);
            final String[] array = {"com.android.calendar"};
            nextIntent.putExtra(Settings.EXTRA_AUTHORITIES, array);
            nextIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(nextIntent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.settings_title_bar, menu);
        return true;
    }
}
