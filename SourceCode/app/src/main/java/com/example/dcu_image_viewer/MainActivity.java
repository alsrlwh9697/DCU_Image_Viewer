package com.example.dcu_image_viewer;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private static final int REQUEST_CODE_PERMISSIONS = 101;
    private static final String TAG = "MainActivity";
    private String[] requiredPermissions = new String[]{

            Manifest.permission.READ_MEDIA_IMAGES
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 권한 확인 및 요청
        if (allPermissionsGranted()) {
            Log.d(TAG, "All permissions granted");
            setupGallery();
        } else {
            Log.d(TAG, "Requesting permissions");
            ActivityCompat.requestPermissions(this, requiredPermissions, REQUEST_CODE_PERMISSIONS);
        }
    }

    // 모든 권한이 허용되었는지 확인
    private boolean allPermissionsGranted() {
        for (String permission : requiredPermissions) {
            if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
                Log.d(TAG, "Permission not granted: " + permission);
                return false;
            }
        }
        return true;
    }

    // 권한 요청 결과 처리
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (allPermissionsGranted()) {
                Log.d(TAG, "Permissions granted after request");
                setupGallery();
            } else {
                Log.d(TAG, "Permissions not granted after request");
                Toast.makeText(this, "Permissions not granted", Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }

    // 갤러리 설정
    private void setupGallery() {
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 3)); // 3열 그리드 레이아웃

        List<String> imagePathList = getImagesFromExternalStorage();
        GalleryAdapter galleryAdapter = new GalleryAdapter(this, imagePathList);
        recyclerView.setAdapter(galleryAdapter);
    }

    // 외부 저장소에서 이미지 가져오기
    private List<String> getImagesFromExternalStorage() {
        List<String> imagePathList = new ArrayList<>();
        File directory = new File("/sdcard");
        File[] files = directory.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isFile() && isImageFile(file.getPath())) {
                    imagePathList.add(file.getPath());
                }
            }
        }
        return imagePathList;
    }

    // 파일이 이미지인지 확인
    private boolean isImageFile(String path) {
        String[] imageExtensions = {".jpg", ".jpeg", ".png", ".bmp", ".gif", ".heic"};
        for (String extension : imageExtensions) {
            if (path.toLowerCase().endsWith(extension)) {
                return true;
            }
        }
        return false;
    }
}
