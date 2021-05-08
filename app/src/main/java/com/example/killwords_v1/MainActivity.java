package com.example.killwords_v1;

import androidx.annotation.NonNull;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.StrictMode;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.HashMap;

import screen.ScreenScaleResult;
import screen.ScreenScaleUtil;

enum WhichView {WELCOMEVIEW,MAIN_MENU,IP_VIEW,GAME_VIEW,WAIT_OTHER,WIN,LOST,EXIT,FULL,HELP}  //界面枚举
public class MainActivity extends Activity {
    MainMenuView mmv;
    GameView gameview;
    WhichView curr;
    ClientAgent ca;
    SoundPool soundPool;
    HashMap<Integer,Integer> soundPoolMap;
    Button lianjie;

    Handler hd=new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            switch (msg.what){
                case 0:   //进入等待界面
                    setContentView(R.layout.wait);
                    curr=WhichView.WAIT_OTHER;
                    lianjie.setClickable(true);		//解锁按钮
                    break;
                case 1:  //进入游戏界面
                    gotoGameView();
                    curr=WhichView.GAME_VIEW;
                    break;
                case 2:  //进入你赢了界面
                    setContentView(R.layout.win);
                    playSound(4,0);
                    curr=WhichView.WIN;
                    break;
                case 3:  //进入你输了界面
                    setContentView(R.layout.die);
                    playSound(3,0);
                    curr=WhichView.LOST;
                    break;
                case 4:  //进入有玩家退出界面
                    setContentView(R.layout.exit);
                    curr=WhichView.EXIT;
                    break;
                case 5:  //人数已满
                    setContentView(R.layout.full);
                    curr=WhichView.FULL;
                    break;
                case 6:   //进入帮助页面
                    setContentView(R.layout.help);
                    curr=WhichView.HELP;
                    break;
                case 8:  //进去菜单界面
                    goToMainMenu();
                    curr=WhichView.WELCOMEVIEW;
                    break;
                case 9://界面弹出Toast显示信息
                    Toast.makeText(MainActivity.this,"联网失败，请稍后再试!", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };


    static ScreenScaleResult ssr;
    static int SW;
    static int SH;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        //竖屏
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        this.getWindow().setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
        );
        //全屏
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        initSounds();

        DisplayMetrics metrics=new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        SW=metrics.widthPixels;
        SH=metrics.heightPixels;
        ssr= ScreenScaleUtil.calScale(SW,SH);

        gotoWelcomeView();
    }

    public void initSounds() {
        soundPool=new SoundPool(5, AudioManager.STREAM_MUSIC,100);
        soundPoolMap=new HashMap<Integer, Integer>();
        soundPoolMap.put(1,soundPool.load(this,R.raw.bg,1));
        soundPoolMap.put(2,soundPool.load(this,R.raw.sound,1));
        soundPoolMap.put(3,soundPool.load(this,R.raw.die,1));
        soundPoolMap.put(4,soundPool.load(this,R.raw.win,1));
        soundPoolMap.put(5,soundPool.load(this,R.raw.ding,1));
    }
    public void playSound(int sound,int loop){
        AudioManager mgr=(AudioManager)this.getSystemService(Context.AUDIO_SERVICE);
        float streamVolumeCurrent = mgr.getStreamVolume(AudioManager.STREAM_MUSIC);//当前音量
        float streamVolumeMax = mgr.getStreamMaxVolume(AudioManager.STREAM_MUSIC);//最大音量
        float volume = streamVolumeCurrent / streamVolumeMax;
        soundPool.play
                (
                        soundPoolMap.get(sound), //声音资源id
                        volume, 				 //左声道音量
                        volume, 				 //右声道音量
                        1,				 //优先级
                        loop, 					 //循环次数 -1带表永远循环
                        1.0f		     //回放速度0.5f～2.0f之间
                );

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        //监听手机键盘按下事件
        if(keyCode==4)//调制上一个界面的键
                {//根据记录的当前是哪个界面信息的curr可以知道要跳转到的是哪个界面
                    if(curr==WhichView.WIN||curr==WhichView.LOST||curr==WhichView.EXIT)
                    {
                        goToMainMenu();
                        return true;
                    }
                    if(curr==WhichView.WELCOMEVIEW)
                    {
                        return true;
                    }
                    if(curr==WhichView.IP_VIEW)
                    {//跳转到MainMenu
                        goToMainMenu();
                        return true;
                    }
                    if(curr==WhichView.GAME_VIEW)
                    {//跳转到EXIT界面
                        try
                        {
                             ca.dout.writeUTF("<#EXIT#>");
                        } catch (IOException e1)
                        {
                    e1.printStackTrace();
                }
                return true;
            }
            if(curr==WhichView.WAIT_OTHER)
            {//不跳转
                return true;
            }
            if(curr==WhichView.MAIN_MENU)
            {//退出游戏
                System.exit(0);
            }
            if(curr==WhichView.FULL)
            {//跳转到IPView
                gotoIpView();
                return true;
            }
            if(curr==WhichView.HELP)
            {//跳转到MainMenu
                goToMainMenu();
                return true;
            }
        }
        return false;
    }

    private void gotoWelcomeView() {
        WelcomeView mySurfaceView = new WelcomeView(this);
        this.setContentView(mySurfaceView);
        curr=WhichView.WELCOMEVIEW;
    }
    private void goToMainMenu() {
        //去主界面的方法
        if(mmv==null)
        {
            mmv=new MainMenuView(this);
        }
        setContentView(mmv);
        //当前的View为MAIN_MENU;
        curr=WhichView.MAIN_MENU;
    }
    public void gotoIpView()
    {//去主IP和端口号的界面的方法
        setContentView(R.layout.main);
        lianjie=(Button)this.findViewById(R.id.Button01);
        final Button fanhui=(Button)this.findViewById(R.id.Button02);

        lianjie.setOnClickListener
                (
                        new  View.OnClickListener()
                        {
                            @Override
                            public void onClick(View v)
                            {
                                lianjie.setClickable(false);	//成功后锁住线程
                                playSound(2, 0);
                                //得到每个EditText的引用
                                final EditText eta=(EditText)findViewById(R.id.EditText01);
                                final EditText etb=(EditText)findViewById(R.id.EditText02);
                                final String ipStr=eta.getText().toString();//得到EditText里面的信息
                                String portStr=etb.getText().toString();
                                String[] ipA=ipStr.split("\\.");
                                if(ipA.length!=4)
                                {//判断IP的格式是否合法
                                    Toast.makeText
                                            (MainActivity.this, "服务器IP地址不合法", Toast.LENGTH_SHORT).show();
                                    return;
                                }

                                for(String s:ipA)
                                {//在IP的格式合法的前提下判断端口号是否合法
                                    try
                                    {
                                        int ipf=Integer.parseInt(s);
                                        if(ipf>255||ipf<0)
                                        {//判断Ip的合法性
                                            Toast.makeText(MainActivity.this, "服务器IP地址不合法", Toast.LENGTH_SHORT).show();
                                            return;
                                        }
                                    }
                                    catch(Exception e)
                                    {
                                        Toast.makeText(MainActivity.this, "服务器IP地址不合法!", Toast.LENGTH_SHORT).show();
                                        return;
                                    }
                                    finally
                                    {
                                        lianjie.setClickable(true);	//成功后放开锁
                                    }
                                }
                                try
                                {
                                    int port=Integer.parseInt(portStr);
                                    if(port>65535||port<0)
                                    {//判断端口号是否合法
                                        Toast.makeText(MainActivity.this, "服务器端口号不合法!", Toast.LENGTH_SHORT).show();
                                        return;
                                    }
                                }
                                catch(Exception e)
                                { Toast.makeText(MainActivity.this, "服务器端口号不合法!", Toast.LENGTH_SHORT).show();
                                    return;
                                }
                                finally
                                {
                                    lianjie.setClickable(true);	//成功后放开锁
                                }
                                //验证过关
                                final int port=Integer.parseInt(portStr);
                                new Thread()
                                {
                                    @Override
                                    public void run(){
                                        try
                                        {//验证过关后启动代理的客户端线程
                                            Socket sc=new Socket(ipStr,port);
                                            DataInputStream din=new DataInputStream(sc.getInputStream());
                                            DataOutputStream dout=new DataOutputStream(sc.getOutputStream());
                                            ca=new ClientAgent(MainActivity.this, sc,din, dout);
                                            ca.start();
                                        }
                                        catch(Exception e)
                                        {
                                            hd.sendEmptyMessage(9);
                                            e.printStackTrace();
                                            return;
                                        }
                                    }
                                }.start();
                            }
                        }
                );
        fanhui.setOnClickListener
                (//对返回按钮设置监听   跳转到主界面
                        new  View.OnClickListener()
                        {
                            @Override
                            public void onClick(View v)
                            {
                                playSound(2, 0);
                                goToMainMenu();
                            }
                        }
                );
        //当前的View为IP_VIEW;
        curr=WhichView.IP_VIEW;
    }
    private void gotoGameView() {
        gameview=new GameView(this);
        setContentView(gameview);
        curr=WhichView.GAME_VIEW;
    }
}
