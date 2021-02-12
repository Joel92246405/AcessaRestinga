package com.joel.a0800restinga.Uteis;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.joel.a0800restinga.Activities.Inicial;

public class SharedPreferencesActivity extends AppCompatActivity{

        protected static String SP_NAME = SharedPreferencesActivity.class.getCanonicalName();
        protected static String TAG = SP_NAME;
        protected static String SP_KEY_EMAIL = "EMAIL";
        protected static String STATE_EMAIL = "EMAIL";
        protected String userEmail = "";
        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
        }

        @Override
        public void onRestoreInstanceState(Bundle savedInstanceState) {
            // Always call the superclass so it can restore the view hierarchy
            super.onRestoreInstanceState(savedInstanceState);

            // Restore state members from saved instance
            userEmail = savedInstanceState.getString(STATE_EMAIL);
            Log.d(TAG, "userEmail: " + userEmail);
        }

        protected String getTemporarilySavedEmail() {
            SharedPreferences sp = this.getSharedPreferences(SP_NAME, MODE_PRIVATE);
            return sp.getString(SP_KEY_EMAIL, "");
        }

        protected void temporarilySaveEmail(String email) {
            SharedPreferences sp = this.getSharedPreferences(Inicial.SP_NAME, MODE_PRIVATE);
            SharedPreferences.Editor editor = sp.edit();
            editor.putString(Inicial.SP_KEY_EMAIL, email);
            editor.commit();
        }
    }

