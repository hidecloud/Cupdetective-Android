package com.hidecloud.cupdetective;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.graphics.ImageFormat;
import android.hardware.Camera;
import android.hardware.Camera.Parameters;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import android.util.DisplayMetrics;
import android.view.Display;
import android.graphics.BitmapFactory;
import android.widget.ImageView;
import android.widget.Button;


public class MainActivity extends ActionBarActivity {
    SurfaceHolder surfaceHolder;
    SurfaceView surfaceView;
    int screenWidth, screenHeight;
    Camera camera;
    boolean isPreview = false;
    boolean direction = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        WindowManager wm = getWindowManager();
        Display display = wm.getDefaultDisplay();
        DisplayMetrics metrics = new DisplayMetrics();
        // 获取屏幕的宽和高
        display.getMetrics(metrics);
        screenWidth = metrics.widthPixels;
        screenHeight = metrics.heightPixels;

        surfaceView = (SurfaceView) findViewById(R.id.surfaceView);
        final ImageView imageView = (ImageView) findViewById(R.id.imageView);
        imageView.setImageBitmap(BitmapFactory.decodeResource(getResources(),R.drawable.ruler));
        Button button = (Button) findViewById(R.id.changeDirection);

        button.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
            if (direction) {
              imageView.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.rulerop));
              direction = false;
            } else {
              imageView.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.ruler));
              direction = true;
            }
          }
        });

        surfaceHolder = surfaceView.getHolder();
        surfaceHolder.addCallback(new Callback()
        {
            @Override
            public void surfaceChanged(SurfaceHolder holder, int format,
                                       int width, int height)
            {
            }

            @Override
            public void surfaceCreated(SurfaceHolder holder)
            {
                // 打开摄像头
                initCamera();
            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder)
            {
                // 如果camera不为null ,释放摄像头
                if (camera != null)
                {
                    if (isPreview) camera.stopPreview();
                    camera.release();
                    camera = null;
                }
            }
        });
    }

    private void initCamera()
    {
        if (!isPreview)
        {
            // 此处默认打开后置摄像头。
            // 通过传入参数可以打开前置摄像头
            camera = Camera.open(0);  //①
            camera.setDisplayOrientation(90);
        }
        if (camera != null && !isPreview)
        {
            try
            {
                Camera.Parameters parameters = camera.getParameters();
                // 设置预览照片的大小
                parameters.setPreviewSize(screenWidth, screenHeight);
                // 设置预览照片时每秒显示多少帧的最小值和最大值
                parameters.setPreviewFpsRange(4, 10);
                // 设置图片格式
                parameters.setPictureFormat(ImageFormat.JPEG);
                // 设置JPG照片的质量
                parameters.set("jpeg-quality", 85);
                // 设置照片的大小
                parameters.setPictureSize(screenWidth, screenHeight);
                // 设置自动对焦
                parameters.setFocusMode(Parameters.FOCUS_MODE_CONTINUOUS_VIDEO);
                // 通过SurfaceView显示取景画面
                camera.setPreviewDisplay(surfaceHolder);  //②
                // 开始预览
                camera.startPreview();  //③
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
            isPreview = true;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
