package com.example.dcu_image_viewer;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;

public class ImageDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_detail);

        ImageView imageView = findViewById(R.id.imageView);

        Intent intent = getIntent();
        String imagePath = intent.getStringExtra("imagePath");
        if (imagePath != null) {
            Glide.with(this).load(Uri.parse("file://" + imagePath)).into(imageView);
        }
    }
}
