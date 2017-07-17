package com.nowcoder;

import org.springframework.aop.ThrowsAdvice;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by ruiwen on 2017/7/14.
 */


class MyThread extends Thread {
    private int tid;

    public MyThread(int tid) {
        this.tid = tid;
    }

    @Override
    public void run() {
        try {
            for (int i = 0; i < 10; ++i) {
                Thread.sleep(50);
                System.out.println(String.format("T1 %d: %d", tid, i));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

class Consumer implements Runnable {
    private BlockingQueue<String> q;
    public Consumer(BlockingQueue<String> q) {
        this.q = q;
    }
    @Override
    public void run() {
        try {
            while (true) {
                System.out.println(Thread.currentThread().getName() + ":" + q.take());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

class Producer implements Runnable {
    private BlockingQueue<String> q;
    public Producer(BlockingQueue<String> q) {
        this.q = q;
    }
    @Override
    public void run() {
        try {
            for (int i = 0; i < 100; ++i) {
                Thread.sleep(500);
                q.put(String .valueOf(i));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

public class MultiThreadTests {
    public static void testThread() {
        for (int k = 0; k < 2; ++k) {
            //new MyThread(j).start();
        }//异步实现方法

        //用实现Runnable接口的对象作为Thread对象的实例化
        for (int j = 0; j < 2; ++j) {
            final int finalI = j;
            new Thread(new Runnable() {
                //自定义一个类并实现Runnable接口
                @Override
                //这里run方法只是执行方法，实现run方法
                public void run() {
                    try {
                        for (int i = 0; i < 10; ++i) {
                            Thread.sleep(1000);
                            System.out.println(String.format("T2 %d: %d", finalI, i));
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }
    }

    //Synchronized块
    private static Object obj = new Object();

    public static void testSynchronized1() {
        synchronized (obj) {
            try {
                for (int j = 0; j < 5; ++j) {
                    //Thread.sleep(1000);
                    System.out.println(String.format("T3 %d", j));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static void testSynchronized2() {
        synchronized (obj) {
            try {
                for (int j = 0; j < 5; ++j) {
                    //Thread.sleep(1000);
                    System.out.println(String.format("T4 %d", j));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    public static void testSynchronized() {
        for (int i = 0; i < 3; ++i) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    testSynchronized1();
                    testSynchronized2();
                }
            }).start();
        }
    }

    public static void testBlockingQueue() {
        BlockingQueue<String> q = new ArrayBlockingQueue<String>(10);
        new Thread(new Producer(q)).start();
        new Thread(new Consumer(q), "Consumer1").start();
        new Thread(new Consumer(q), "Consumer2").start();
    }

    public static ThreadLocal<Integer> threadLocalUserIds = new ThreadLocal<>();
    private static int userId;

    public static void testThreadLocal(){
        for (int i = 0; i < 5; ++i){
            final int fianlI = i;
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        threadLocalUserIds.set(fianlI);
                        Thread.sleep(1000);
                        System.out.println("ThreadLocal:" + threadLocalUserIds.get());
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
            }).start();
        }
        for (int i = 0; i < 10; ++i) {
            final int finalI = i;
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        userId = finalI;
                        Thread.sleep(2000);
                        System.out.println("UserId:" + userId);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }
    }

    public static void testExecutor() {
        //ExecutorService service = Executors.newSingleThreadExecutor();
        ExecutorService service = Executors.newFixedThreadPool(2);
        service.submit(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < 10; ++i) {
                    try {
                        Thread.sleep(1000);
                        System.out.println("Executor1:" + i);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        service.submit(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < 10; ++i) {
                    try {
                        Thread.sleep(1000);
                        System.out.println("Executor2:" + i);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        service.shutdown();
        while (!service.isTerminated()) {
            try {
                Thread.sleep(1000);
                System.out.println("Wait for termination.");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private static int counter = 0;
    private static AtomicInteger atomicInteger = new AtomicInteger(0);

    public static void testWithoutAtomic() {
        for (int i = 0; i < 10000; ++i) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(1000);
                        for (int j = 0; j < 10; ++j) {
                            counter++;
                            System.out.println(counter);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }
    }
    public static void testWithAtomic() {
        for (int i = 0; i < 1000; ++i) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(1000);
                        for (int j = 0; j < 10; ++j) {
                            System.out.println(atomicInteger.incrementAndGet());
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }
    }
    public static  void testAtomic() {
        testWithoutAtomic();
        //testWithAtomic();
    }

    public static void testFuture() {
        ExecutorService service = Executors.newSingleThreadExecutor();
        Future<Integer> future = service.submit(new Callable<Integer>() {
            @Override
            public Integer call() throws Exception {
                //Thread.sleep(1000);
                throw new IllegalArgumentException("异常");
                //return 1;
            }
        });

        service.shutdown();
        try {
            System.out.println(future.get());
            //System.out.println(future.get(100, TimeUnit.MILLISECONDS));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        //testThread();
        testSynchronized();
        //testBlockingQueue();
        //testThreadLocal();
        //testExecutor();
        //testAtomic();
    }
}
