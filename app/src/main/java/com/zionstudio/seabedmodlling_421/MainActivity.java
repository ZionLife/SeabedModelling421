package com.zionstudio.seabedmodlling_421;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends Activity {
    //屏幕对应的宽度和高度
    static float WIDTH;
    static float HEIGHT;
    public static String mImagePath;
    float obsX; //障碍物X坐标
    float obsY; //障碍物Y坐标，对应在Gl坐标系中是Z坐标
    float startX;
    float startY;
    float endX;
    float endY;
    String seperateChar = " ";
    @BindView(R.id.sfv)
    MySurfaceView sfv;
    @BindView(R.id.btn_open)
    Button btnOpen;
    @BindView(R.id.et_obstacleX)
    EditText etObstacleX;
    @BindView(R.id.et_obstacleY)
    EditText etObstacleY;
    @BindView(R.id.et_startX)
    EditText etStartX;
    @BindView(R.id.et_startY)
    EditText etStartY;
    @BindView(R.id.et_endX)
    EditText etEndX;
    @BindView(R.id.et_endY)
    EditText etEndY;
    @BindView(R.id.btn_confirm)
    Button btnConfirm;
    @BindView(R.id.btn_cancel)
    Button btnCancel;
    @BindView(R.id.rl)
    LinearLayout rl;
    @BindView(R.id.mv)
    MapView mv;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //设置为全屏
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        //获得系统的宽度以及高度
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        if (dm.widthPixels > dm.heightPixels) {
            WIDTH = dm.widthPixels;
            HEIGHT = dm.heightPixels;
        } else {
            WIDTH = dm.heightPixels;
            HEIGHT = dm.widthPixels;
        }
        //设置为横屏模式
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        initData();
        initView();
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);
        sfv.setMapView(mv);
        initListener();
//        AStar2.doSearch();
    }

    private void initData() {
//        final Uri uri = Uri.parse(getIntent().getStringExtra("imageUri"));
//        parseImagePath(uri);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        sfv = null;
//        Constant.reset();
    }

    private void initListener() {
        btnOpen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openInputView();
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeInputParamsView();
            }
        });

        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirm();
            }
        });
    }

    private void confirm() {
        String obsXStr = etObstacleX.getText().toString();
        String obsYStr = etObstacleY.getText().toString();
        String startXStr = etStartX.getText().toString();
        String startYStr = etStartY.getText().toString();
        String endXStr = etEndX.getText().toString();
        String endYStr = etEndY.getText().toString();

        if (TextUtils.isEmpty(obsXStr) || TextUtils.isEmpty(obsYStr) || TextUtils.isEmpty(startXStr)
                || TextUtils.isEmpty(startYStr) || TextUtils.isEmpty(endXStr) || TextUtils.isEmpty(endYStr)) {
            Utils.makeToastLong("请输入完整数据");
            return;
        }
        obsX = Float.parseFloat(obsXStr);
        obsY = Float.parseFloat(obsYStr);
        startX = Float.parseFloat(startXStr);
        startY = Float.parseFloat(startYStr);
        endX = Float.parseFloat(endXStr);
        endY = Float.parseFloat(endYStr);
//        checkOutOfBounds();
        setCubeCoordinate();
        rl.setVisibility(View.GONE);
    }

    /**
     * 解析uri得到图片的路径
     *
     * @param uri
     */
    private void parseImagePath(Uri uri) {
        String[] filePathColumn = {MediaStore.Images.Media.DATA};
        Cursor cursor = getContentResolver().query(uri, filePathColumn, null, null, null);
        cursor.moveToFirst();
        int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
        mImagePath = cursor.getString(columnIndex);
        cursor.close();
    }

    private void setCubeCoordinate() {
        Cube.x = obsX;
        Cube.z = obsY;
        sfv.clear();
    }

    private void checkOutOfBounds() {
        if (!(checkX(obsX) && checkX(startX) && checkX(endX)
                && checkY(obsY) && checkY(startY) && checkY(endY))) {
            String msg = "X∈[" + Constant.minX + " ," + Constant.maxX + "]"
                    + "，Y∈[" + Constant.minY + ", " + Constant.maxY + "]";
            Utils.makeToastLong(msg);
        }
//        if (!(checkY(obsY) && checkY(startY) && checkY(endY))) {
//
//        }
    }

    private boolean checkX(float num) {
        if (num < Constant.minX || num > Constant.maxX) {
            return false;
        } else {
            return true;
        }
    }

    private boolean checkY(float num) {
        if (num < Constant.minY || num > Constant.maxY) {
            return false;
        } else {
            return true;
        }
    }

    private void initView() {
//        sfv = new MySurfaceView(this);
//        sfv.requestFocus();//获取焦点
//        sfv.setFocusableInTouchMode(true);//设置为可触控
    }

    public void openInputView() {
        if (rl.getVisibility() == View.GONE) {
            rl.setVisibility(View.VISIBLE);
        } else {
            closeInputParamsView();
        }

    }

    public void closeInputParamsView() {
        rl.setVisibility(View.GONE);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (sfv != null) {
            sfv.onResume();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (sfv != null) {
            sfv.onPause();
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (rl.getVisibility() != View.GONE) {
                rl.setVisibility(View.GONE);
                return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }
}