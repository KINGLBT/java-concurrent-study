package com.rebote.concurrent;

public class StopThreadUnsaft {

    public static User user = new User();

    public static class ChangeUserRunnable implements Runnable {

        public void run() {
            while (true) {
                synchronized (user) {
                    int v = (int) (System.currentTimeMillis()/1000);
                    user.setUserId(v);
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    user.setUserName(v+"");
                }
            }
        }

    }

    public static class ReadObjectThread implements Runnable {

        public void run() {
            while (true) {
                synchronized (user) {
                    if (user.getUserName()!=null && !user.getUserName().equals(user.getUserId().toString())) {
                        System.out.println(user);
                    }
                }
            }
        }
    }


    public static void main(String[] args) throws InterruptedException {
        Thread t2 = new Thread(new ReadObjectThread());
        t2.start();

        while(true) {
            Thread t1 = new Thread(new ChangeUserRunnable());
            t1.start();
            Thread.sleep(150);
            t1.stop();
        }
    }


}
