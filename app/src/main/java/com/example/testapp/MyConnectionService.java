package com.example.testapp;

import android.content.Intent;
import android.telecom.Connection;
import android.telecom.ConnectionRequest;
import android.telecom.ConnectionService;
import android.telecom.DisconnectCause;
import android.telecom.PhoneAccountHandle;
import android.telecom.TelecomManager;
import android.util.Log;
public class MyConnectionService extends ConnectionService {
    public static final String TAG = MyConnectionService.class.getName();
    public Connection mConnection;
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.v(TAG, "On Start");
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public Connection onCreateOutgoingConnection(PhoneAccountHandle connectionManagerPhoneAccount, ConnectionRequest request) {
        Log.v(TAG, String.valueOf(request.getAddress()));
        Connection incomingCallConnection = createConnection(request);
        //incomingCallConnection.setRinging();
        //incomingCallConnection.setActive();
        //incomingCallConnection.setConnectionCapabilities(Connection.CAPABILITY_HOLD);
        incomingCallConnection.setDialing();
        return incomingCallConnection;
    }

    @Override
    public void onCreateOutgoingConnectionFailed(PhoneAccountHandle connectionManagerPhoneAccount, ConnectionRequest request) {
        if (request != null) {
            Log.v(TAG, request.toString());
        }
        super.onCreateOutgoingConnectionFailed(connectionManagerPhoneAccount, request);
    }

    private Connection createConnection(ConnectionRequest request) {
        mConnection = new Connection() {
            @Override
            public void onStateChanged(int state) {
                super.onStateChanged(state);
            }

            @Override
            public void onDisconnect() {
                super.onDisconnect();
                mConnection.setDisconnected(new DisconnectCause(DisconnectCause.CANCELED));
                //mConnectionsAvailableForConference.clear();
                mConnection.destroy();
            }

            @Override
            public void onSeparate() {
                super.onSeparate();
            }

            @Override
            public void onAbort() {
                super.onAbort();
                mConnection.setDisconnected(new DisconnectCause(DisconnectCause.CANCELED));
                mConnection.destroy();
            }

            @Override
            public void onHold() {
                super.onHold();
            }

            @Override
            public void onAnswer() {
                super.onAnswer();
                mConnection.setActive();
            }

            @Override
            public void onReject() {
                super.onReject();
                mConnection.setDisconnected(new DisconnectCause(DisconnectCause.CANCELED));
                mConnection.destroy();

            }
        };
        mConnection.setAddress(request.getAddress(), TelecomManager.PRESENTATION_ALLOWED);
        mConnection.setExtras(request.getExtras());
        return mConnection;
    }
}