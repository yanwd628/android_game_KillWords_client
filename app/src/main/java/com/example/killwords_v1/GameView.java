package com.example.killwords_v1;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.io.IOException;

import screen.ScreenScaleResult;
import screen.ScreenScaleUtil;

import static com.example.killwords_v1.Constant.*;

public class GameView extends SurfaceView implements SurfaceHolder.Callback {

    MainActivity activity;
    Paint paint;
    Paint paintwords;
    Paint paintin;
    GameViewDrawThread viewdraw;
    static Bitmap iback; //背景图片
    static Bitmap ikey; //键盘图片
    static Bitmap[][] keys;
    Words words;
    int currid=0;
    StringBuffer sb=new StringBuffer();


    public GameView(MainActivity activity) {
        super(activity);
        this.activity=activity;
        this.getHolder().addCallback(this);
        paint=new Paint();
        paint.setAntiAlias(true);
        paintwords=new Paint();
        paintwords.setAntiAlias(true);
        paintin=new Paint();
        paintin.setAntiAlias(true);
        words=new Words();
    }
    public static void initBitmap(Resources r) //加载图片方法
    {
        iback = BitmapFactory.decodeResource(r, R.drawable.gamebg);  //背景图
        ikey=BitmapFactory.decodeResource(r,R.drawable.keyboard);
        keys=KeyLoadUtil.splitPic(4,7,ikey,KEY_WIDTH,KEY_HEIGHT);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode==4){
            try
            {
                activity.ca.dout.writeUTF("<#EXIT#>");
                return true;
            } catch (IOException e1)
            {
                e1.printStackTrace();
            }
        }
       return false;
    }

    @Override
    public boolean onTouchEvent(MotionEvent e) {
        int x=(int) (e.getX());
        int y=(int) (e.getY());
        ScreenScaleResult ssr=MainActivity.ssr;
        int[] ca= ScreenScaleUtil.touchFromTargetToOrigin(x,y,ssr);
        x=ca[0];
        y=ca[1];
        //System.out.println("游戏界面点击位置："+x+"^"+y);
        //监听键盘
        if(x>(KET_FIRST_X+KEY_INTERAL*0)&&x<(KET_FIRST_X+KEY_INTERAL*0+KEY_WIDTH)&&y>(KET_FIRST_Y)&&y<(KET_FIRST_Y+KEY_HEIGHT)){
            System.out.println("点击了Q");kill('q');
        }if(x>(KET_FIRST_X+KEY_INTERAL*1)&&x<(KET_FIRST_X+KEY_INTERAL*1+KEY_WIDTH)&&y>(KET_FIRST_Y)&&y<(KET_FIRST_Y+KEY_HEIGHT)){
            System.out.println("点击了W");kill('w');
        }if(x>(KET_FIRST_X+KEY_INTERAL*2)&&x<(KET_FIRST_X+KEY_INTERAL*2+KEY_WIDTH)&&y>(KET_FIRST_Y)&&y<(KET_FIRST_Y+KEY_HEIGHT)){
            System.out.println("点击了E");kill('e');
        }if(x>(KET_FIRST_X+KEY_INTERAL*3)&&x<(KET_FIRST_X+KEY_INTERAL*3+KEY_WIDTH)&&y>(KET_FIRST_Y)&&y<(KET_FIRST_Y+KEY_HEIGHT)){
            System.out.println("点击了R");kill('r');
        }if(x>(KET_FIRST_X+KEY_INTERAL*4)&&x<(KET_FIRST_X+KEY_INTERAL*4+KEY_WIDTH)&&y>(KET_FIRST_Y)&&y<(KET_FIRST_Y+KEY_HEIGHT)){
            System.out.println("点击了T");kill('t');
        }if(x>(KET_FIRST_X+KEY_INTERAL*5)&&x<(KET_FIRST_X+KEY_INTERAL*5+KEY_WIDTH)&&y>(KET_FIRST_Y)&&y<(KET_FIRST_Y+KEY_HEIGHT)){
            System.out.println("点击了Y");kill('y');
        }if(x>(KET_FIRST_X+KEY_INTERAL*6)&&x<(KET_FIRST_X+KEY_INTERAL*6+KEY_WIDTH)&&y>(KET_FIRST_Y)&&y<(KET_FIRST_Y+KEY_HEIGHT)){
            System.out.println("点击了U");kill('u');
        }if(x>(KET_FIRST_X+KEY_INTERAL*7)&&x<(KET_FIRST_X+KEY_INTERAL*7+KEY_WIDTH)&&y>(KET_FIRST_Y)&&y<(KET_FIRST_Y+KEY_HEIGHT)){
            System.out.println("点击了I");kill('i');
        }if(x>(KET_FIRST_X+KEY_INTERAL*8)&&x<(KET_FIRST_X+KEY_INTERAL*8+KEY_WIDTH)&&y>(KET_FIRST_Y)&&y<(KET_FIRST_Y+KEY_HEIGHT)){
            System.out.println("点击了O");kill('o');
        }if(x>(KET_FIRST_X+KEY_INTERAL*9)&&x<(KET_FIRST_X+KEY_INTERAL*9+KEY_WIDTH)&&y>(KET_FIRST_Y)&&y<(KET_FIRST_Y+KEY_HEIGHT)){
            System.out.println("点击了P");kill('p');
        }if(x>(KET_SECOND_X+KEY_INTERAL*0)&&x<(KET_SECOND_X+KEY_INTERAL*0+KEY_WIDTH)&&y>(KET_SECOND_Y)&&y<(KET_SECOND_Y+KEY_HEIGHT)){
            System.out.println("点击了A");kill('a');
        }if(x>(KET_SECOND_X+KEY_INTERAL*1)&&x<(KET_SECOND_X+KEY_INTERAL*1+KEY_WIDTH)&&y>(KET_SECOND_Y)&&y<(KET_SECOND_Y+KEY_HEIGHT)){
            System.out.println("点击了S");kill('s');
        }if(x>(KET_SECOND_X+KEY_INTERAL*2)&&x<(KET_SECOND_X+KEY_INTERAL*2+KEY_WIDTH)&&y>(KET_SECOND_Y)&&y<(KET_SECOND_Y+KEY_HEIGHT)){
            System.out.println("点击了D");kill('d');
        }if(x>(KET_SECOND_X+KEY_INTERAL*3)&&x<(KET_SECOND_X+KEY_INTERAL*3+KEY_WIDTH)&&y>(KET_SECOND_Y)&&y<(KET_SECOND_Y+KEY_HEIGHT)){
            System.out.println("点击了F");kill('f');
        }if(x>(KET_SECOND_X+KEY_INTERAL*4)&&x<(KET_SECOND_X+KEY_INTERAL*4+KEY_WIDTH)&&y>(KET_SECOND_Y)&&y<(KET_SECOND_Y+KEY_HEIGHT)){
            System.out.println("点击了G");kill('g');
        }if(x>(KET_SECOND_X+KEY_INTERAL*5)&&x<(KET_SECOND_X+KEY_INTERAL*5+KEY_WIDTH)&&y>(KET_SECOND_Y)&&y<(KET_SECOND_Y+KEY_HEIGHT)){
            System.out.println("点击了H");kill('h');
        }if(x>(KET_SECOND_X+KEY_INTERAL*6)&&x<(KET_SECOND_X+KEY_INTERAL*6+KEY_WIDTH)&&y>(KET_SECOND_Y)&&y<(KET_SECOND_Y+KEY_HEIGHT)){
            System.out.println("点击了J");kill('j');
        }if(x>(KET_SECOND_X+KEY_INTERAL*7)&&x<(KET_SECOND_X+KEY_INTERAL*7+KEY_WIDTH)&&y>(KET_SECOND_Y)&&y<(KET_SECOND_Y+KEY_HEIGHT)){
            System.out.println("点击了K");kill('k');
        }if(x>(KET_SECOND_X+KEY_INTERAL*8)&&x<(KET_SECOND_X+KEY_INTERAL*8+KEY_WIDTH)&&y>(KET_SECOND_Y)&&y<(KET_SECOND_Y+KEY_HEIGHT)) {
            System.out.println("点击了L");kill('l');
        }if(x>(KET_THIRD_X+KEY_INTERAL*0)&&x<(KET_THIRD_X+KEY_INTERAL*0+KEY_WIDTH)&&y>(KET_THIRD_Y)&&y<(KET_THIRD_Y+KEY_HEIGHT)) {
            System.out.println("点击了Z");kill('z');
        }if(x>(KET_THIRD_X+KEY_INTERAL*1)&&x<(KET_THIRD_X+KEY_INTERAL*1+KEY_WIDTH)&&y>(KET_THIRD_Y)&&y<(KET_THIRD_Y+KEY_HEIGHT)) {
            System.out.println("点击了X");kill('x');
        }if(x>(KET_THIRD_X+KEY_INTERAL*2)&&x<(KET_THIRD_X+KEY_INTERAL*2+KEY_WIDTH)&&y>(KET_THIRD_Y)&&y<(KET_THIRD_Y+KEY_HEIGHT)) {
            System.out.println("点击了C");kill('c');
        }if(x>(KET_THIRD_X+KEY_INTERAL*3)&&x<(KET_THIRD_X+KEY_INTERAL*3+KEY_WIDTH)&&y>(KET_THIRD_Y)&&y<(KET_THIRD_Y+KEY_HEIGHT)) {
            System.out.println("点击了V");kill('v');
        }if(x>(KET_THIRD_X+KEY_INTERAL*4)&&x<(KET_THIRD_X+KEY_INTERAL*4+KEY_WIDTH)&&y>(KET_THIRD_Y)&&y<(KET_THIRD_Y+KEY_HEIGHT)) {
            System.out.println("点击了B");kill('b');
        }if(x>(KET_THIRD_X+KEY_INTERAL*5)&&x<(KET_THIRD_X+KEY_INTERAL*5+KEY_WIDTH)&&y>(KET_THIRD_Y)&&y<(KET_THIRD_Y+KEY_HEIGHT)) {
            System.out.println("点击了N");kill('n');
        }if(x>(KET_THIRD_X+KEY_INTERAL*6)&&x<(KET_THIRD_X+KEY_INTERAL*6+KEY_WIDTH)&&y>(KET_THIRD_Y)&&y<(KET_THIRD_Y+KEY_HEIGHT)) {
            System.out.println("点击了M");kill('m');
        }if(x>(KET_THIRD_X+KEY_INTERAL*7)&&x<(KET_THIRD_X+KEY_INTERAL*7+KEY_WIDTH)&&y>(KET_THIRD_Y)&&y<(KET_THIRD_Y+KEY_HEIGHT)) {
            System.out.println("点击了删除");
            if(sb.length()!=0){
                sb.deleteCharAt(sb.length()-1);
            }
        }

        return false;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        ScreenScaleResult ssr=MainActivity.ssr;
        canvas.save();
        canvas.translate(ssr.lucX,ssr.lucY);
        canvas.scale(ssr.ratio, ssr.ratio);

        paint.setColor(Color.BLACK);
        //设置背景图片
        canvas.drawBitmap(iback, BACK_XOFFSET, BACK_YOFFSET, paint);
        //添加键盘
        canvas.drawBitmap(keys[4][0],KET_FIRST_X+KEY_INTERAL*0,KET_FIRST_Y,paint);
        canvas.drawBitmap(keys[5][2],KET_FIRST_X+KEY_INTERAL*1,KET_FIRST_Y,paint);
        canvas.drawBitmap(keys[1][0],KET_FIRST_X+KEY_INTERAL*2,KET_FIRST_Y,paint);
        canvas.drawBitmap(keys[4][1],KET_FIRST_X+KEY_INTERAL*3,KET_FIRST_Y,paint);
        canvas.drawBitmap(keys[4][3],KET_FIRST_X+KEY_INTERAL*4,KET_FIRST_Y,paint);
        canvas.drawBitmap(keys[6][0],KET_FIRST_X+KEY_INTERAL*5,KET_FIRST_Y,paint);
        canvas.drawBitmap(keys[5][0],KET_FIRST_X+KEY_INTERAL*6,KET_FIRST_Y,paint);
        canvas.drawBitmap(keys[2][0],KET_FIRST_X+KEY_INTERAL*7,KET_FIRST_Y,paint);
        canvas.drawBitmap(keys[3][2],KET_FIRST_X+KEY_INTERAL*8,KET_FIRST_Y,paint);
        canvas.drawBitmap(keys[3][3],KET_FIRST_X+KEY_INTERAL*9,KET_FIRST_Y,paint);

        canvas.drawBitmap(keys[0][0],KET_SECOND_X+KEY_INTERAL*0,KET_SECOND_Y,paint);
        canvas.drawBitmap(keys[4][2],KET_SECOND_X+KEY_INTERAL*1,KET_SECOND_Y,paint);
        canvas.drawBitmap(keys[0][3],KET_SECOND_X+KEY_INTERAL*2,KET_SECOND_Y,paint);
        canvas.drawBitmap(keys[1][1],KET_SECOND_X+KEY_INTERAL*3,KET_SECOND_Y,paint);
        canvas.drawBitmap(keys[1][2],KET_SECOND_X+KEY_INTERAL*4,KET_SECOND_Y,paint);
        canvas.drawBitmap(keys[1][3],KET_SECOND_X+KEY_INTERAL*5,KET_SECOND_Y,paint);
        canvas.drawBitmap(keys[2][1],KET_SECOND_X+KEY_INTERAL*6,KET_SECOND_Y,paint);
        canvas.drawBitmap(keys[2][2],KET_SECOND_X+KEY_INTERAL*7,KET_SECOND_Y,paint);
        canvas.drawBitmap(keys[2][3],KET_SECOND_X+KEY_INTERAL*8,KET_SECOND_Y,paint);

        canvas.drawBitmap(keys[6][1],KET_THIRD_X+KEY_INTERAL*0,KET_THIRD_Y,paint);
        canvas.drawBitmap(keys[5][3],KET_THIRD_X+KEY_INTERAL*1,KET_THIRD_Y,paint);
        canvas.drawBitmap(keys[0][2],KET_THIRD_X+KEY_INTERAL*2,KET_THIRD_Y,paint);
        canvas.drawBitmap(keys[5][1],KET_THIRD_X+KEY_INTERAL*3,KET_THIRD_Y,paint);
        canvas.drawBitmap(keys[0][1],KET_THIRD_X+KEY_INTERAL*4,KET_THIRD_Y,paint);
        canvas.drawBitmap(keys[3][1],KET_THIRD_X+KEY_INTERAL*5,KET_THIRD_Y,paint);
        canvas.drawBitmap(keys[3][0],KET_THIRD_X+KEY_INTERAL*6,KET_THIRD_Y,paint);

        canvas.drawBitmap(keys[6][2],KET_THIRD_X+KEY_INTERAL*7,KET_THIRD_Y,paint);


        paintwords.setTextSize(PAINT_SIZE);
        paintwords.setTypeface(Typeface.createFromAsset(getContext().getAssets(),"fonnts/zt1.ttf"));
        for(Words w:activity.ca.allwords){
            if(w.show){
                canvas.drawText(w.word,w.x,w.y,paintwords);
            }
        }

        paintin.setColor(Color.BLACK);
        paintin.setUnderlineText(true);
        paintin.setTextSize(PAINT_SIZE);
        paintin.setTypeface(Typeface.createFromAsset(getContext().getAssets(),"fonnts/zt2.ttf"));
        canvas.drawText(sb.toString(),IN_SHOW_X,IN_SHOW_Y,paintin);

        canvas.restore();
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
      //  initCardsForControl(SanGuoActivity.cardListStr);
        if(viewdraw==null)
        {
            viewdraw=new GameViewDrawThread(this);
            viewdraw.flag=true;
            viewdraw.start();
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        boolean reatry=true;
        viewdraw.flag=false;
        while(reatry){
            try{
                viewdraw.join();
                reatry=false;
            }catch(InterruptedException e){
                e.printStackTrace();
            }
        }
    }

    public void repaint() {
        SurfaceHolder surfaceholder=this.getHolder();
        Canvas canvas=surfaceholder.lockCanvas();
        try
        {
            synchronized(surfaceholder)
            {
                onDraw(canvas);
            }
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
            if(canvas!=null)
            {
                surfaceholder.unlockCanvasAndPost(canvas);
            }
        }
    }
    public void kill(char ch){
            //判断输入的这个 和之前和构成 能不能消灭 那个单词
            //如果可以就向服务器发送相关数据
        sb.append(ch);
        if(currid<activity.ca.allwords.size()&&sb.toString().equals(activity.ca.allwords.get(currid).word)){
            try {
                int num=activity.ca.selfnum;
                activity.ca.dout.writeUTF("<#KILL#>"+num+"|"+currid+"|"+sb.toString());
                activity.ca.allwords.get(currid).show=false;                                //不再绘制
                System.out.println("<#KILL#>"+num+"|"+currid+"|"+sb.toString());
                sb.delete(0,sb.length());
                activity.playSound(5,0);
                currid=currid+1;
                System.out.println("因为消灭之后，现在id为"+currid);

            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }
}
