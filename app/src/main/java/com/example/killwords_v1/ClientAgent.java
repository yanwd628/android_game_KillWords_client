package com.example.killwords_v1;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import static com.example.killwords_v1.Constant.*;

public class ClientAgent extends Thread {
    MainActivity father;
    Socket sc;
    DataInputStream din;
    DataOutputStream dout;
    boolean flag=true;

    List<Words> allwords=new ArrayList<>();
    Words currentword;
    Words lastword;

    int selfnum=0;
    public ClientAgent(MainActivity father,Socket sc,DataInputStream din,DataOutputStream dout){
        this.father=father;
        this.sc=sc;
        this.din=din;
        this.dout=dout;
     //   father.playSound(1,-1);
    }

    @Override
    public void run() {
        while (flag){
            try{
                final String msg=din.readUTF();
                System.out.println("收到msg："+msg);
                if(msg.startsWith("<#ACCEPT#>")){               //可以加入
                    String numStr=msg.substring(10);
                    selfnum=Integer.parseInt(numStr);
                    father.hd.sendEmptyMessage(0);
                }else if(msg.startsWith("<#START#>")){          //开始游戏
                    new Thread()
                    {
                        public void run() {
                            GameView.initBitmap(father.getResources());
                            father.hd.sendEmptyMessage(1);
                        }
                    }.start();
                } else if(msg.startsWith("<#DATA#>")){
                    synchronized(this) {
                        currentword = new Words();
                        String worddata = msg.substring(8);
                        String[] ta = worddata.split("\\|");
                        currentword.id = Integer.parseInt(ta[0]);
                        currentword.word = ta[1];
                        currentword.show = ta[2].startsWith("t");
                        currentword.x = (int) (Math.random() * SCREEN_WIDTH);
                        currentword.y = 0;
                        if (currentword.id == allwords.size())
                            allwords.add(currentword);
                    }
                } else if(msg.startsWith("<#FINISH#>")){             //游戏结束
                    int tempnum=Integer.parseInt(msg.substring(10)); //胜利or失败，不同的界面
                    if(tempnum==selfnum)
                    {
                        father.hd.sendEmptyMessage(2);
                    }
                    else
                    {
                        father.hd.sendEmptyMessage(3);
                    }
                    this.father.gameview.viewdraw.flag=false;
                    this.flag=false;
                    this.din.close();
                    this.dout.close();
                    this.sc.close();
                }else if(msg.startsWith("<#EXIT#>")){               //退出游戏
                        father.hd.sendEmptyMessage(4);
                    this.father.gameview.viewdraw.flag=false;
                    this.flag=false;
                    this.din.close();
                    this.dout.close();
                    this.sc.close();
                }else if(msg.startsWith("<#FULL#>")){               //服务器已满
                    father.hd.sendEmptyMessage(5);
                    this.father.gameview.viewdraw.flag=false;
                    this.flag=false;
                    this.din.close();
                    this.dout.close();
                    this.sc.close();
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }
}
