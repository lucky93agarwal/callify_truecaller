package com.gpslab.kaun.Service;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.gpslab.kaun.model.Constants;

import org.eclipse.paho.android.service.MqttService;

import java.net.URISyntaxException;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;

public class MqttMessageService extends MqttService {
    public Socket mSocket;
    public SharedPreferences sharedPreferences;
    public SharedPreferences.Editor edit;
    @Override
    public void onCreate() {
        super.onCreate();
        sharedPreferences = getSharedPreferences("data", Context.MODE_PRIVATE);
        edit = sharedPreferences.edit();
        {
            try {
                mSocket = IO.socket(Constants.CHAT_SERVER_URL);
            } catch (URISyntaxException e) {
                throw new RuntimeException(e);
            }
        }
        mSocket.on(Socket.EVENT_CONNECT, onConnect);
        mSocket.connect();

    }
    private Emitter.Listener onConnect = new Emitter.Listener() {
        @Override
        public void call(Object... args) {

            Log.d("WalletlsjWallet", "onConnect 1 = ");

            Log.d("WalletlsjWallet", "emit = "+sharedPreferences.getString("mobile",""));
            mSocket.emit("user_connected", sharedPreferences.getString("mobile",""));

        }
    };
}
