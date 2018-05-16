package com.zionstudio.seabedmodlling_421;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 主页
 */
public class HomeActivity extends BaseActivity {
    public static final int REQUEST_HEIGHTMAP = 1;
    public static final int REQUEST_GRASS = 2;
    public static final int REQUEST_ROCK = 3;
    private static final int PERMISSIONS_REQUEST_STORAGE = 1;
    @BindView(R.id.btn_heightmap)
    Button mBtnHeight;
    @BindView(R.id.tv_heightmap)
    TextView mTvHeightmap;
    @BindView(R.id.tv_grass)
    TextView mTvGrass;
    @BindView(R.id.btn_grass)
    Button mBtnGrass;
    @BindView(R.id.tv_rock)
    TextView mTvRock;
    @BindView(R.id.btn_rock)
    Button mBtnRock;
    @BindView(R.id.et_max)
    EditText mEtMax;
    @BindView(R.id.et_min)
    EditText mEtMin;
    @BindView(R.id.btn_confirm)
    Button mBtnConfirm;

    private boolean mPermissionGranted = false; //是否授予权限
    private static final String PERMISSIONS_REJECTED_MSG = "需要SD卡读取权限"; //请求权限被拒绝时的提示

    @Override
    public void initData() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            mPermissionGranted = false;
            requestPermission();
        } else {
            mPermissionGranted = true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case PERMISSIONS_REQUEST_STORAGE:
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED
                        && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                    mPermissionGranted = true;
                } else {
                    mPermissionGranted = false;
                    Toast.makeText(this, PERMISSIONS_REJECTED_MSG, Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    @Override
    public void initView() {
//        mBtnHeight.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                openGallery();
//            }
//        });
    }

    private void requestPermission() {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSIONS_REQUEST_STORAGE);
    }

    @Override
    public int getLayoutId() {
        return R.layout.home_activity;
    }


    /**
     * 打开系统相册
     */
    public void openGallery(int requestCode) {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, requestCode);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (mPermissionGranted == false) {
            Toast.makeText(this, PERMISSIONS_REJECTED_MSG, Toast.LENGTH_SHORT).show();
            return;
        }
        if (data != null && data.getData() != null) {
            Uri uri = data.getData();
            switch (requestCode) {
                case REQUEST_HEIGHTMAP:
                    Constant.sHeightmapPath = parseImagePath(uri);
                    mTvHeightmap.setText(Constant.sHeightmapPath);
                    break;
                case REQUEST_GRASS:
                    Constant.sGrassPath = parseImagePath(uri);
                    mTvGrass.setText(Constant.sGrassPath);
                    break;
                case REQUEST_ROCK:
                    Constant.sRockPath = parseImagePath(uri);
                    mTvRock.setText(Constant.sRockPath);
                    break;
            }
        }
    }

    /**
     * 解析uri得到图片的路径
     *
     * @param uri
     */
    private String parseImagePath(Uri uri) {
        String[] filePathColumn = {MediaStore.Images.Media.DATA};
        Cursor cursor = getContentResolver().query(uri, filePathColumn, null, null, null);
        cursor.moveToFirst();
        int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
        String imagePath = cursor.getString(columnIndex);
        cursor.close();
        return imagePath;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    @OnClick({R.id.btn_heightmap, R.id.btn_grass, R.id.btn_rock, R.id.btn_confirm})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_heightmap:
                openGallery(REQUEST_HEIGHTMAP);
                break;
            case R.id.btn_grass:
                openGallery(REQUEST_GRASS);
                break;
            case R.id.btn_rock:
                openGallery(REQUEST_ROCK);
                break;
            case R.id.btn_confirm:
                if (checkValid()) {
                    startActivity(new Intent(this, RenderActivity.class));
                }
                break;
        }
    }

    private boolean checkValid() {
        if (TextUtils.isEmpty(mTvHeightmap.getText().toString())
                || TextUtils.isEmpty(mTvGrass.getText().toString())
                || TextUtils.isEmpty(mTvRock.getText().toString())) {
            Toast.makeText(this, "请选择高度图、草地纹理图与岩石纹理图", Toast.LENGTH_LONG).show();
            return false;
        }
        if (mTvHeightmap.getText().toString().equals("请选择图片") || mTvGrass.getText().toString().equals("请选择图片") || mTvRock.getText().toString().equals("请选择图片")) {
            Toast.makeText(this, "请选择高度图、草地纹理图与岩石纹理图", Toast.LENGTH_LONG).show();
            return false;
        }
        if (TextUtils.isEmpty(mEtMax.getText().toString()) || TextUtils.isEmpty(mEtMin.getText().toString())) {
            Toast.makeText(this, "请输入地形的最大/最小高度", Toast.LENGTH_LONG).show();
            return false;
        }
        try {
            Constant.HIGH_BASE = Float.parseFloat(mEtMin.getText().toString());
            Constant.HIGHEST = Float.parseFloat(mEtMax.getText().toString());
        } catch (NumberFormatException e) {
            Toast.makeText(this, "地形高度请输入十进制数字", Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }
}
