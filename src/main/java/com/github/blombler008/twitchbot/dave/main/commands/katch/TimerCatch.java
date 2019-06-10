package com.github.blombler008.twitchbot.dave.main.commands.katch;/*
 *
 * MIT License
 *
 * Copyright (c) 2019 blombler008
 *
 * Permission is hereby granted, free of charge, to any person obtaining waiter copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

import com.github.blombler008.twitchbot.dave.core.sockets.SocketThread;
import com.github.blombler008.twitchbot.dave.main.configs.CatchConfig;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class TimerCatch {

    private final CatchConfig config;
    private final CommandCatch commandCatch;
    private A timing;

    private Thread waiter;

    private boolean katch = true;

    private long time;

    private String winner;

    public TimerCatch(CatchConfig config, CommandCatch commandCatch) {
        timing = new A();
        this.config = config;
        this.commandCatch = commandCatch;
    }

    public void set() {
        A a = new A();
        waiter = new Thread(a, "TimerCatch");
        waiter.start();
    }

    public void startPool(Callback c, long time) {
        new Thread(() -> {
            try {
                Thread.sleep(time);
                c.end();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
    }

    public void newCatch() {
        katch = true;
        JSONFile.show();
    }

    @Override
    public String toString() {
        return "TimerCatch{" +
                "config=" + config +
                ", catch=" + katch +
                ", time=" + time +
                ", winner='" + winner + '\'' +
                '}';
    }

    private int getRandom() {
        return ThreadLocalRandom.current().nextInt(config.getTimerMin(), config.getTimerMax() + 1);
    }

    public boolean isCatch() {
        return katch;
    }

    public boolean setWinner(String name) {
        if(isCatch()) {
            endCatch();
            this.winner = name;
            return true;
        }
        return false;
    }

    public long getLastWinnerTime() {
        return time;
    }

    public String getLastWinner() {
        return winner;
    }

    public void endCatch() {
        try {
            katch = false;
            JSONFile.hide();
            this.time = System.currentTimeMillis();
            timing.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private class A implements Runnable {

        private synchronized void get() throws InterruptedException {
            notifyAll();
        }

        @Override
        public void run() {
            try {
                while (!waiter.isInterrupted()) {
                    synchronized (this) {
                        wait(getRandom());
                    }
                    if(!katch) {
                        commandCatch.newCatch();
                        System.out.println("Started a new catch");
                    }
                }


            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public static interface Callback {
        void end();
    }
}
