package readWriteLock;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.Lock;

/**
 * @program: simple-lock
 * @description:
 * @author: yejc
 * @create: 2019-11-17 15:20
 **/
public class ReadWriteLockTest {
    public static void main(String[] args) throws InterruptedException {
        MyReadWriteLock myReadWriteLock = new MyReadWriteLock();
        Counter counter = new Counter(myReadWriteLock);
        ExecutorService pool = Executors.newCachedThreadPool();

        for (int i = 0; i < 20; i++) {
            pool.execute(() -> {
                counter.add();
            });
        }
        for (int i = 0; i < 20; i++) {
            pool.execute(() -> {
                counter.get();
            });
        }
        pool.shutdown();
        Thread.sleep(1000);
        System.out.println("最后结果为：" + counter.get());
        System.out.println("done!");
    }

    public static class Counter {
        private MyReadWriteLock lock;
        int count = 0;

        public Counter(MyReadWriteLock lock) {
            this.lock = lock;
        }

        public void add() {
            try {
                lock.writeLock().lock();
                Thread.sleep(100);
                System.out.println(Thread.currentThread().getName() + "正在写");
                count++;
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                lock.writeLock().unlock();
            }
        }

        public int get() {
            Lock readLock = this.lock.readLock();
            try {
                readLock.lock();
                Thread.sleep(100);
                System.out.println(Thread.currentThread().getName() + "正在读");
                return count;
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                readLock.unlock();
            }
            return -1;
        }
    }
}
