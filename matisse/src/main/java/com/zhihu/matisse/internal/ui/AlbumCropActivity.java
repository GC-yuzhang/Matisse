/*
 * Copyright 2017 Zhihu Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.zhihu.matisse.internal.ui;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.zhihu.matisse.R;
import com.zhihu.matisse.internal.entity.SelectionSpec;
import com.zhihu.matisse.internal.ui.widget.CropImageView;
import com.zhihu.matisse.internal.utils.FileHelper;
import java.io.File;
import java.util.List;

import static com.zhihu.matisse.ui.MatisseActivity.EXTRA_RESULT_CROP_PATH;
import static com.zhihu.matisse.ui.MatisseActivity.EXTRA_RESULT_CROP_URI;
import static com.zhihu.matisse.ui.MatisseActivity.EXTRA_RESULT_SELECTION;

public class AlbumCropActivity extends AppCompatActivity implements CropImageView.OnBitmapSaveCompleteListener {
    private CropImageView mCropImageView;
    private boolean mIsSaveRectangle;
    private int mOutputX;
    private int mOutputY;

    private final SelectionSpec mSelectionSpec = SelectionSpec.getInstance();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (!SelectionSpec.getInstance().hasInited) {
            setResult(RESULT_CANCELED);
            finish();
            return;
        }

        List<Uri> uris = getIntent().getParcelableArrayListExtra(EXTRA_RESULT_SELECTION);
        if (uris == null || uris.isEmpty()) {
            setResult(RESULT_CANCELED);
            finish();
            return;
        }

        Uri uri = uris.get(0);

        setContentView(R.layout.activity_album_crop);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.mipmap.icon_back);
        toolbar.setNavigationOnClickListener(view -> finish());

        mCropImageView = (CropImageView) findViewById(R.id.cv_crop_image);
        mCropImageView.setOnBitmapSaveCompleteListener(this);

        TextView saveButton = (TextView) findViewById(R.id.btn_ok);
        saveButton.setOnClickListener(view -> mCropImageView.saveBitmapToFile(
            FileHelper.getInstance().getCropCacheFolder(AlbumCropActivity.this),
            mOutputX,
            mOutputY,
            mIsSaveRectangle
        ));

        //获取需要的参数
        mOutputX = mSelectionSpec.cropOutputWidth;
        mOutputY = mSelectionSpec.cropOutputHeight;
        mIsSaveRectangle = mSelectionSpec.cropToCircle;

        mCropImageView.setFocusStyle(mSelectionSpec.focusStyle());
        mCropImageView.setFocusWidth(mSelectionSpec.focusWidth);
        mCropImageView.setFocusHeight(mSelectionSpec.focusHeight);
        mCropImageView.setImageURI(uri);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onBitmapSaveSuccess(File file) {
        Intent intent = new Intent();
        intent.putExtra(EXTRA_RESULT_CROP_URI, Uri.fromFile(file));
        intent.putExtra(EXTRA_RESULT_CROP_PATH, file.getPath());
        setResult(RESULT_OK, intent);
        finish();
    }

    @Override
    public void onBitmapSaveError(File file) {
        setResult(RESULT_CANCELED);
        finish();
    }
}
