package com.rebote.concurrent;

public class StopThreadSaft {

    public static User user = new User();

    public static class ChangeUserThread extends Thread {

        private boolean stopMe = false;

        public void stopMe(){
            stopMe = true;
        }

        public void run() {
            while (true) {
                if (stopMe) {
                    System.out.println("exit by stopMe");
                    break;
                }
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

    public static class ReadObjectThread extends Thread {

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
            Thread t1 = new ChangeUserThread();
            t1.start();
            Thread.sleep(150);
            ((ChangeUserThread) t1).stopMe();
        }
    }


}
