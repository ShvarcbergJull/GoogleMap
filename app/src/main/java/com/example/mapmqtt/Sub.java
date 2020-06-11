package com.example.mapmqtt;

import android.util.Log;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttMessage;

public class Sub implements MqttCallback {
    private MqttClient client;
    private MapsActivity activity;

//    public Sub (MqttClient client, MapsActivity activity) {
//        this.client = client;
//        this.client.setCallback(this);
//        this.activity = activity;
//    }

    @Override
    public void messageArrived(String topic, MqttMessage message) throws Exception {
        this.activity.msg = "" + message;
        Log.d("testmur", "" + message);
    }

    @Override
    public void connectionLost(Throwable cause) {

    }

    @Override
    public void deliveryComplete(IMqttDeliveryToken token) {

    }
}
