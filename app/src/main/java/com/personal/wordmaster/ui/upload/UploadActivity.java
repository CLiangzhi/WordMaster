package com.personal.wordmaster.ui.upload;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;

import com.personal.wordmaster.R;
import com.personal.wordmaster.base.BaseActivity;
import com.personal.wordmaster.viewmodel.UploadViewModel;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class UploadActivity extends BaseActivity {

    private static final int REQUEST_CAMERA = 1;
    private static final int REQUEST_ALBUM = 2;

    private UploadViewModel viewModel;
    private TextView tvImageCount;
    private TextView tvStatus;
    private EditText etBookName;
    private final List<Bitmap> selectedBitmaps = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload);

        viewModel = new ViewModelProvider(this).get(UploadViewModel.class);

        tvImageCount = findViewById(R.id.tv_image_count);
        tvStatus = findViewById(R.id.tv_status);
        etBookName = findViewById(R.id.et_book_name);

        findViewById(R.id.btn_take_photo).setOnClickListener(v -> {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            if (intent.resolveActivity(getPackageManager()) != null) {
                startActivityForResult(intent, REQUEST_CAMERA);
            }
        });

        findViewById(R.id.btn_select_album).setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("image/*");
            intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
            startActivityForResult(intent, REQUEST_ALBUM);
        });

        findViewById(R.id.btn_start_parse).setOnClickListener(v -> startParsing());

        observeViewModel();
    }

    private void observeViewModel() {
        viewModel.status.observe(this, msg -> tvStatus.setText(msg));
        viewModel.success.observe(this, success -> {
            if (Boolean.TRUE.equals(success)) {
                finish();
            }
        });
        viewModel.error.observe(this, msg -> tvStatus.setText("错误: " + msg));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK || data == null) return;

        try {
            if (requestCode == REQUEST_CAMERA && data.getExtras() != null) {
                Bitmap bmp = (Bitmap) data.getExtras().get("data");
                if (bmp != null) {
                    selectedBitmaps.add(bmp);
                    updateImageCount();
                }
            } else if (requestCode == REQUEST_ALBUM) {
                if (data.getClipData() != null) {
                    // 多张图片
                    int count = data.getClipData().getItemCount();
                    for (int i = 0; i < count; i++) {
                        Uri uri = data.getClipData().getItemAt(i).getUri();
                        Bitmap bmp = loadBitmapFromUri(uri);
                        if (bmp != null) selectedBitmaps.add(bmp);
                    }
                } else if (data.getData() != null) {
                    // 单张图片
                    Bitmap bmp = loadBitmapFromUri(data.getData());
                    if (bmp != null) selectedBitmaps.add(bmp);
                }
                updateImageCount();
            }
        } catch (Exception e) {
            tvStatus.setText("读取图片失败: " + e.getMessage());
        }
    }

    private Bitmap loadBitmapFromUri(Uri uri) {
        try {
            InputStream is = getContentResolver().openInputStream(uri);
            Bitmap bmp = BitmapFactory.decodeStream(is);
            is.close();
            return bmp;
        } catch (Exception e) {
            return null;
        }
    }

    private void updateImageCount() {
        tvImageCount.setText(String.valueOf(selectedBitmaps.size()));
    }

    private void startParsing() {
        if (selectedBitmaps.isEmpty()) {
            tvStatus.setText("请先拍照或在相册中选择图片");
            return;
        }
        String name = etBookName.getText().toString().trim();
        if (name.isEmpty()) {
            name = "词书 " + System.currentTimeMillis() % 100000;
        }
        viewModel.parseImages(new ArrayList<>(selectedBitmaps), name);
    }
}
