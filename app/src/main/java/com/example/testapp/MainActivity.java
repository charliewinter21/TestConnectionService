package com.example.testapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.content.ComponentName;
import android.content.Context;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.telecom.PhoneAccount;
import android.telecom.PhoneAccountHandle;
import android.telecom.TelecomManager;
import android.util.Log;
import android.Manifest;

public class MainActivity extends AppCompatActivity {
    private String TAG = "MainActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TelecomManager tm = (TelecomManager) getSystemService(Context.TELECOM_SERVICE);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            Log.v(TAG, "entrando en telecom");
            PhoneAccountHandle phoneAccountHandle = new PhoneAccountHandle(
                    new ComponentName(this.getApplicationContext(), MyConnectionService.class),
                    "example");
            PhoneAccount.Builder builder = PhoneAccount.builder(phoneAccountHandle, "example");
            builder.setCapabilities(PhoneAccount.CAPABILITY_SIM_SUBSCRIPTION);
            builder.setCapabilities(PhoneAccount.CAPABILITY_VOICE_CALLING_AVAILABLE);
            builder.setCapabilities(PhoneAccount.CAPABILITY_SUPPORTS_VOICE_CALLING_INDICATIONS);
            PhoneAccount phoneAccount = builder.build();
            tm.registerPhoneAccount(phoneAccount);
            Bundle extras = new Bundle();
            extras.putParcelable(TelecomManager.EXTRA_OUTGOING_CALL_EXTRAS, phoneAccountHandle);
            extras.putParcelable(TelecomManager.EXTRA_PHONE_ACCOUNT_HANDLE, phoneAccountHandle);
            extras.putBoolean(TelecomManager.EXTRA_START_CALL_WITH_SPEAKERPHONE, true);
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                Log.v(TAG, "permiso no dado");
                ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.CALL_PHONE},101);
                return;
            }
            Log.v(TAG, "iniciando llamada");
            tm.placeCall(Uri.parse("tel:347526"), extras);

            //Direct call
            /*Intent intent = new Intent();
            intent.setData(Uri.parse("tel:347526"));
            startActivity(intent);*/
        }
    }
}