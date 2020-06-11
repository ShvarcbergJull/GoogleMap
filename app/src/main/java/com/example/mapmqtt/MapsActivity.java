package com.example.mapmqtt;

import androidx.fragment.app.FragmentActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttMessageListener;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.MqttTopic;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    String broker = "tcp://192.168.1.8:1883";
    String topicName = "Observatories/";
    MqttClient client;
    String msg = "";
    MapsActivity activity = this;
    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        textView = findViewById(R.id.tit);
        try {
            client = new MqttClient(broker, "Android", new MemoryPersistence());
            client.connect();
            //client.subscribe(topicName + "#", 1); -v1

//            IMqttToken token = client.subscribeWithResponse(topicName + "obs1", 1);
//            token.setActionCallback(new IMqttActionListener() {
//                @Override
//                public void onSuccess(IMqttToken asyncActionToken) {
//                    Log.d("testmur", "ok");
//                }
//
//                @Override
//                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
//
//                }
//            });
//
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        mMap.addMarker(new MarkerOptions().position(new LatLng(52.2, 104.3)).title("ISTP"));
        mMap.addMarker(new MarkerOptions().position(new LatLng(51.8, 103.1)).title("TORY"));
        mMap.addMarker(new MarkerOptions().position(new LatLng(70.0, 88.0)).title("NORI"));
        mMap.addMarker(new MarkerOptions().position(new LatLng(51.8, 104.9)).title("LIST"));
        mMap.addMarker(new MarkerOptions().position(new LatLng(51.6, 100.9)).title("MOND"));
        mMap.addMarker(new MarkerOptions().position(new LatLng(52.9, 103.3)).title("ORDA"));
        mMap.addMarker(new MarkerOptions().position(new LatLng(53.1, 106.8)).title("SARM"));
        mMap.addMarker(new MarkerOptions().position(new LatLng(53.3, 107.7)).title("UZUR"));
        mMap.addMarker(new MarkerOptions().position(new LatLng(53.3, 108.7)).title("MKSM"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(52.2, 104.3)));

        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(final Marker marker) {
//                MqttMessage message = new MqttMessage((marker.getTitle()).getBytes());
//                message.setQos(1);
//                message.setRetained(true);
//                MqttTopic topic = client.getTopic("ObservatoryOf/");
//                try {
//                    topic.publish(message);
//                } catch (MqttException e) {
//                    e.printStackTrace();
//                }
                try {
                    client.unsubscribe(topicName + "#");
                    client.subscribe(topicName + marker.getTitle());
                } catch (MqttException e) {
                    e.printStackTrace();
                }
                return false;
            }
        });

        client.setCallback(new MqttCallback() {
            @Override
            public void connectionLost(Throwable cause) {
                return;
            }

            @Override
            public void messageArrived(String topic, MqttMessage message) throws Exception {
                Log.d("testmur", "" + message);
                textView.setText(message.toString());
            }

            @Override
            public void deliveryComplete(IMqttDeliveryToken token) {
                return;
            }
        });
    }
}