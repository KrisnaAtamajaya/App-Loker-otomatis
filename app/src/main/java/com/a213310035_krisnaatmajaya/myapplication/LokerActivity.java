package com.a213310035_krisnaatmajaya.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LokerActivity extends AppCompatActivity {

    private ImageView loker1;
    private ImageView loker2;
    private ImageView loker3;
    private ImageView loker4;

    private String topicLoker1;
    private String topicLoker2;
    private String topicLoker3;
    private String topicLoker4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loker);

        loker1 = findViewById(R.id.loker1);
        loker2 = findViewById(R.id.loker2);
        loker3 = findViewById(R.id.loker3);
        loker4 = findViewById(R.id.loker4);

        loker1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openMainActivity(topicLoker1);
            }
        });

        loker2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openMainActivity(topicLoker2);
            }
        });

        loker3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openMainActivity(topicLoker3);
            }
        });

        loker4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openMainActivity(topicLoker4);
            }
        });

        topicLoker1 = MqttConfig.getTopicForLocker("loker1");
        topicLoker2 = MqttConfig.getTopicForLocker("loker2");
        topicLoker3 = MqttConfig.getTopicForLocker("loker3");
        topicLoker4 = MqttConfig.getTopicForLocker("loker4");
    }

    private void openMainActivity(String topic) {
        DatabaseReference lokerStatusRef = FirebaseDatabase.getInstance().getReference("loker_status").child(topic);
        lokerStatusRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String status = dataSnapshot.getValue(String.class);
                if (status != null && status.equals("terisi")) {
                    Toast.makeText(LokerActivity.this, "This locker is already occupied", Toast.LENGTH_SHORT).show();
                } else {
                    lokerStatusRef.setValue("terisi");
                    Intent intent = new Intent(LokerActivity.this, MainActivity.class);
                    intent.putExtra("topic", topic);
                    startActivity(intent);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle onCancelled event
            }
        });
    }
}
