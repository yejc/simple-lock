package reentrantLock;

import java.util.concurrent.locks.Lock;

/**
 * @program: simple-reentrantLock
 * @description:
 * @author: yejc
 * @create: 2019-11-16 22:38
 **/
public class ReentrantLockTest {

    public static void main(String[] args) throws InterruptedException {
        Lock lock = new MyReentrantLock();
        final Counter counter = new Counter(lock);

        for (int i = 0; i < 200; i++) {
            new Thread(() -> {
                counter.add();
            }).start();
        }
        Thread.sleep(2000);
        System.out.println(counter.get());
    }

    public static class Counter {
        private Lock lock;
        int count = 0;

        public Counter(Lock lock) {
            this.lock = lock;
        }

        public void add() {
            try {
                lock.lock();
                count++;
            } finally {
                lock.unlock();
            }
        }

        public int get() {
            return count;
        }

    }
}
