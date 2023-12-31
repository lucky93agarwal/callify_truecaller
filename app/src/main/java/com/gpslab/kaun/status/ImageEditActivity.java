package com.gpslab.kaun.status;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.os.Bundle;
import com.gpslab.kaun.imageeditengine.BaseFragment;
import com.gpslab.kaun.ImageEditor;
import com.gpslab.kaun.R;
import com.gpslab.kaun.imageeditengine.CropFragment;
import com.gpslab.kaun.imageeditengine.FragmentUtil;

public class ImageEditActivity extends BaseImageEditActivity
        implements PhotoEditorFragment.OnFragmentInteractionListener,
        CropFragment.OnFragmentInteractionListener{
    private Rect cropRect;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_edit);

        String imagePath = getIntent().getStringExtra(ImageEditor.EXTRA_IMAGE_PATH);
        if (imagePath != null) {
            FragmentUtil.addFragment(this, R.id.fragment_container,
                    PhotoEditorFragment.newInstance(imagePath));
        }
    }
    @Override public void onCropClicked(Bitmap bitmap) {
        FragmentUtil.replaceFragment(this, R.id.fragment_container,
                CropFragment.newInstance(bitmap, cropRect));
    }

    @Override public void onDoneClicked(String imagePath) {

        Intent intent = new Intent();
        intent.putExtra(ImageEditor.EXTRA_EDITED_PATH, imagePath);
        setResult(Activity.RESULT_OK, intent);
        finish();
    }

    @Override public void onImageCropped(Bitmap bitmap, Rect cropRect) {
        this.cropRect = cropRect;
        PhotoEditorFragment photoEditorFragment =
                (PhotoEditorFragment) FragmentUtil.getFragmentByTag(this,
                        PhotoEditorFragment.class.getSimpleName());
        if (photoEditorFragment != null) {
            photoEditorFragment.setImageWithRect(cropRect);
            photoEditorFragment.reset();
            FragmentUtil.removeFragment(this,
                    (BaseFragment) FragmentUtil.getFragmentByTag(this, CropFragment.class.getSimpleName()));
        }
    }

    @Override public void onCancelCrop() {
        FragmentUtil.removeFragment(this,
                (BaseFragment) FragmentUtil.getFragmentByTag(this, CropFragment.class.getSimpleName()));
    }

    @Override public void onBackPressed() {
        super.onBackPressed();
    }
}