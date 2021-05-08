package com.example.killwords_v1;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import screen.ScreenScaleResult;
import screen.ScreenScaleUtil;

import static com.example.killwords_v1.Constant.*;

public class MainMenuView extends SurfaceView implements SurfaceHolder.Callback {

    MainActivity activity;
    Paint paint;
    Bitmap bitmapStart;
    Bitmap bitmapHelp;
    Bitmap bitmapBack;
    Bitmap bitmapOut;

    public MainMenuView(MainActivity activity) {
        super(activity);
        this.activity=activity;
        this.getHolder().addCallback(this);
        paint=new Paint();
        paint.setAntiAlias(true);
        initBitmap();

        activity.playSound(1,-1);
    }

    public void initBitmap() {
        bitmapStart= BitmapFactory.decodeResource(getResources(),R.drawable.butstart);
        bitmapHelp= BitmapFactory.decodeResource(getResources(),R.drawable.buthelp);
        bitmapBack= BitmapFactory.decodeResource(getResources(),R.drawable.back);
        bitmapOut= BitmapFactory.decodeResource(getResources(),R.drawable.butexit);
    }


    public void newonDraw(Canvas canvas) {
        ScreenScaleResult ssr=MainActivity.ssr;
        canvas.save();
        canvas.translate(ssr.lucX,ssr.lucY);
        canvas.scale(ssr.ratio,ssr.ratio);
        canvas.drawBitmap(bitmapBack,BACK_XOFFSET,BACK_YOFFSET,paint);
        canvas.drawBitmap(bitmapStart,BUTTON_START_XOFFSET,BUTTON_START_YOFFSET,paint);
        canvas.drawBitmap(bitmapHelp,BUTTON_HELP_XOFFSET,BUTTON_HELP_YOFFSET,paint);
        canvas.drawBitmap(bitmapOut,BUTTON_OUT_XOFFSET,BUTTON_OUT_YOFFSET,paint);
        canvas.restore();
    }

    @Override
    public boolean onTouchEvent(MotionEvent e) {
        int x=(int)(e.getX());
        int y=(int)(e.getY());
        ScreenScaleResult ssr=MainActivity.ssr;
        int[] ca= ScreenScaleUtil.touchFromTargetToOrigin(x,y,ssr);
        x=ca[0];
        y=ca[1];
        System.out.println("点击位置："+x+"..."+y);
        switch (e.getAction()){
            case MotionEvent.ACTION_DOWN:
                if(x>BUTTON_START_XOFFSET&&x<BUTTON_START_WIDTH+BUTTON_START_XOFFSET
                        &&y>BUTTON_START_YOFFSET&&y<BUTTON_START_YOFFSET+BUTTON_START_HEIGHT)
                {//对开始按钮的监听  点击可是按钮跳到IpView
                    activity.playSound(2, 0);
                    activity.gotoIpView();

                }
                if(x>BUTTON_HELP_XOFFSET&&x<BUTTON_HELP_XOFFSET+BUTTON_HELP_WIDTH
                        &&y>BUTTON_HELP_YOFFSET&&y<BUTTON_HELP_YOFFSET+BUTTON_HELP_HEIGHT)
                {//对帮助按钮的监听
                    activity.playSound(2, 0);
                    activity.hd.sendEmptyMessage(6);

                }

                if(x>BUTTON_OUT_XOFFSET&&x<BUTTON_OUT_XOFFSET+BUTTON_OUT_WIDTH
                        &&y>BUTTON_OUT_YOFFSET&&y<BUTTON_OUT_YOFFSET+BUTTON_OUT_HEIGHT)
                {//对关于按钮的监听
                    activity.playSound(2, 0);
                    System.exit(0);

                }
                break;
        }
        return true;
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        this.repaint();

    }

    private void repaint() {
        SurfaceHolder holder=this.getHolder();
        Canvas canvas=holder.lockCanvas();
        try{
            synchronized (holder){
                newonDraw(canvas);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            if(canvas!=null)
                holder.unlockCanvasAndPost(canvas);
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {

    }
}
