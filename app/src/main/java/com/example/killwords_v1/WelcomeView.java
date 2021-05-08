package com.example.killwords_v1;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;


import screen.ScreenScaleResult;

public class WelcomeView extends SurfaceView implements SurfaceHolder.Callback {
    MainActivity activity;
    Paint paint;            //画笔
    int currentAlpha=0;     //不透明程度

    int screenWidth=320;    //宽度
    int screenHeight=480;   //高度
   // int sleepSpan=20;       //动画时延ms

    Bitmap logo;            //logo图片
    int currentX;
    int currentY;

    public WelcomeView(MainActivity activity) {
        super(activity);
        this.activity=activity;
        this.getHolder().addCallback(this);   // 设置生命周期回调接口的实现者
        paint =new Paint();                   // 创建画笔
        paint.setAntiAlias(true);             // 打开抗锯齿

        logo= BitmapFactory.decodeResource(activity.getResources(),R.drawable.welcome);


    }


    public void newonDraw(Canvas canvas) {
        //全黑背景
        paint.setColor(Color.WHITE);
        paint.setAlpha(255);
        //canvas.drawRect(0,0,screenWidth,screenHeight,paint);

        paint.setAlpha(currentAlpha);
        ScreenScaleResult ssr=MainActivity.ssr;
        canvas.save();
        canvas.translate(ssr.lucX,ssr.lucY);
        canvas.scale(ssr.ratio,ssr.ratio);
        canvas.drawBitmap(logo,currentX,currentY,paint);
        canvas.restore();
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        new Thread(){
            public void run() {
                //计算位置
                currentX = screenWidth / 2 - logo.getWidth() / 2;
                currentY = screenHeight / 2 - logo.getHeight() / 2;
                //动态更改图片透明度
                for (int i = 255; i > -10; i = i - 10) {
                    currentAlpha = i;
                    if (currentAlpha < 0) {
                        currentAlpha = 0;
                    }
                    SurfaceHolder myholder = WelcomeView.this.getHolder();
                    Canvas canvas = myholder.lockCanvas();    // 获取画布
                    try {
                        synchronized (myholder) {
                            newonDraw(canvas);                  //绘制welcome
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        if (canvas != null) {                       //画布不为空的时候，解锁画布
                            myholder.unlockCanvasAndPost(canvas);
                        }
                    }

                }
            activity.hd.sendEmptyMessage(8);        //发送信息跳转界面
            }
        }.start();      //开启线程
    }
    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {

    }
}
