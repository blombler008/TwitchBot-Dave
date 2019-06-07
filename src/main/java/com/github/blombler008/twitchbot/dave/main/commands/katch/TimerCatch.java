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

public class TimerCatch {

    private final CatchConfig config;
    private A timing;

    private Thread waiter;

    private boolean katch = true;

    private long time;

    private String winner;

    public TimerCatch(CatchConfig config) {
        timing = new A();
        this.config = config;
    }

    public void set() {
        waiter = new Thread(() -> {
            try {
                while (!waiter.isInterrupted()) {
                    timing.put();
                    newCatch();
                }


            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }, "TimerCatch-0");


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
        try {
            timing.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
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

    private long getRandom() {
        return (new Random().nextInt(config.getTimerMax()-config.getTimerMin())) + config.getTimerMin();
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
            timing.get();
            JSONFile.hide();
            this.time = System.currentTimeMillis();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private class A {

        private void put() throws InterruptedException {
            Thread.sleep(getRandom());
            synchronized (this) {
                if(!katch) {

                    wait();
                    newCatch();
                }
            }
        }

        private void get() throws InterruptedException {

            Thread.yield();
            synchronized (this) {
                notify();
            }
        }
    }

    public static interface Callback {
        void end();
    }
}
