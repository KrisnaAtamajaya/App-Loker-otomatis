// MainActivity.java
package com.a213310035_krisnaatmajaya.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.MqttPersistenceException;

public class MainActivity extends AppCompatActivity {

    private TextView statusText;
    private Button unlockButton;
    private Button lockButton;
    private Button signoutButton;

    private MqttClient mqttClient;
    private String topic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        statusText = findViewById(R.id.status_text);
        unlockButton = findViewById(R.id.unlock_button);
        lockButton = findViewById(R.id.lock_button);
        signoutButton = findViewById(R.id.signout_button);

        topic = getIntent().getStringExtra("topic");
        connectToMqttBroker();

        unlockButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mqttClient != null && mqttClient.isConnected()) {
                    MqttMessage message = new MqttMessage();
                    message.setPayload("unlock".getBytes());
                    try {
                        mqttClient.publish(topic, message);
                        statusText.setText("Unlock message sent");
                    } catch (MqttPersistenceException e) {
                        e.printStackTrace();
                    } catch (MqttException e) {
                        e.printStackTrace();
                    }
                } else {
                    Toast.makeText(MainActivity.this, "Not connected to MQTT broker", Toast.LENGTH_SHORT).show();
                }
            }
        });

        lockButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mqttClient != null && mqttClient.isConnected()) {
                    MqttMessage message = new MqttMessage();
                    message.setPayload("lock".getBytes());
                    try {
                        mqttClient.publish(topic, message);
                        statusText.setText("Lock message sent");
                    } catch (MqttPersistenceException e) {
                        e.printStackTrace();
                    } catch (MqttException e) {
                        e.printStackTrace();
                    }
                } else {
                    Toast.makeText(MainActivity.this, "Not connected to MQTT broker", Toast.LENGTH_SHORT).show();
                }
            }
        });


        signoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseReference lokerStatusRef = FirebaseDatabase.getInstance().getReference("loker_status").child(topic);
                lokerStatusRef.setValue("kosong", new DatabaseReference.CompletionListener() {
                    @Override
                    public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                        if (databaseError != null) {
                            // Handle error
                        } else {
                            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    }
                });
            }
        });
    }

    private void connectToMqttBroker() {
        try {
            mqttClient = new MqttClient(MqttConfig.getBrokerUrl(), MqttConfig.getClientId(), MqttConfig.getMemoryPersistence());
            mqttClient.connect(MqttConfig.getMqttConnectOptions());
            mqttClient.setCallback(new MqttCallback() {
                @Override
                public void connectionLost(Throwable throwable) {
                    Toast.makeText(MainActivity.this, "Connection lost", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void messageArrived(String s, MqttMessage mqttMessage) throws Exception {
                    // Do nothing
                }

                @Override
                public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {
                    // Do nothing
                }
            });
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mqttClient != null && mqttClient.isConnected()) {
            try {
                mqttClient.disconnect();
            } catch (MqttException e) {
                e.printStackTrace();
            }
        }
    }




    @Override
    public void onBackPressed() {
        if (mqttClient != null && mqttClient.isConnected()) {
            Toast.makeText(this, "Sign out terlebih dahulu jika ingin keluar", Toast.LENGTH_SHORT).show();
        } else {
            super.onBackPressed();
        }
    }




}
