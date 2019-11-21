package readWriteLock;

import aqs.MyAqs;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;

/**
 * @program: simple-lock
 * @description:
 * @author: yejc
 * @create: 2019-11-17 15:04
 **/
public class MyReadWriteLock implements ReadWriteLock {
    private ReadLock readLock;
    private WriteLock writeLock;

    public MyReadWriteLock() {
        MyAqs myAqs = new MyAqs(0) {
            @Override
            protected boolean tryAcquire(int arg) {
                // 有读的时候，不能写
                if (state.get() != 0) {
                    return false;
                } else {
                    return exclusiveOwner.compareAndSet(null, Thread.currentThread());
                }
            }

            @Override
            protected int tryAcquireShared(int arg) {
                // 如果当前有线程占用了写锁，则不允许再加锁，除非是同一个线程
                if (exclusiveOwner.get() != null && !exclusiveOwner.get().equals(Thread.currentThread())) {
                    return -1;
                }
                return state.incrementAndGet();
            }

            @Override
            protected boolean tryRelease(int arg) {
                return exclusiveOwner.compareAndSet(Thread.currentThread(), null);
            }

            @Override
            protected boolean tryReleaseShared(int arg) {
                return state.decrementAndGet() >= 0;
            }
        };
        readLock = new ReadLock(myAqs);
        writeLock = new WriteLock(myAqs);
    }

    @Override
    public Lock readLock() {
        return readLock;
    }

    @Override
    public Lock writeLock() {
        return writeLock;
    }

    public static class ReadLock implements Lock {

        private MyAqs myAqs;

        public ReadLock(MyAqs myAqs) {
            this.myAqs = myAqs;
        }

        @Override
        public void lock() {
            myAqs.acquireShared(1);
        }

        @Override
        public void lockInterruptibly() throws InterruptedException {

        }

        @Override
        public boolean tryLock() {
            return false;
        }

        @Override
        public boolean tryLock(long time, TimeUnit unit) throws InterruptedException {
            return false;
        }

        @Override
        public void unlock() {
            myAqs.releaseShared(1);
        }

        @Override
        public Condition newCondition() {
            return null;
        }
    }

    public static class WriteLock implements Lock {

        private MyAqs myAqs;

        public WriteLock(MyAqs myAqs) {
            this.myAqs = myAqs;
        }

        @Override
        public void lock() {
            myAqs.acquire(1);
        }

        @Override
        public void lockInterruptibly() throws InterruptedException {

        }

        @Override
        public boolean tryLock() {
            return false;
        }

        @Override
        public boolean tryLock(long time, TimeUnit unit) throws InterruptedException {
            return false;
        }

        @Override
        public void unlock() {
            myAqs.release(1);
        }

        @Override
        public Condition newCondition() {
            return null;
        }
    }
}
