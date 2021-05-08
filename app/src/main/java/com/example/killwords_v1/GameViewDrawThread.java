package com.example.killwords_v1;

import static com.example.killwords_v1.Constant.*;

public class GameViewDrawThread extends Thread {
    GameView gameView;
    boolean flag = true;

    public GameViewDrawThread(GameView gameView) {
        this.gameView = gameView;
    }

    @Override
    public void run() {

        while (flag) {

            for (Words w : gameView.activity.ca.allwords) {
                if (w.show) {
                    if ((w.x + w.word.length() * PAINT_SIZE) > SCREEN_WIDTH)    //超出框线则拉回来
                        w.x = SCREEN_WIDTH - w.word.length() * PAINT_SIZE;
                    w.y++;
                    if (w.y >= WORDS_MAXY) {
                        w.show = false;
                        gameView.currid = gameView.currid + 1;         //同时用户当前不能再输入这个单词
                        System.out.println("因为超出画面，当前id为：" + gameView.currid);
                    }
                }
            }

            gameView.repaint();

            try {
                Thread.sleep(sleeptime);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
