package lock;

import aqs.MyAqs;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

/**
 * @program: simple-lock
 * @description:
 * @author: yejc
 * @create: 2019-11-16 22:23
 **/
public class MyReentrantLock implements Lock {

    private MyAqs myAqs = new MyAqs(1) {
        @Override
        protected boolean tryAcquire(int arg) {
            return state.compareAndSet(arg, 0);
        }

        @Override
        protected boolean tryRelease(int arg) {
            return state.compareAndSet(0, arg);
        }
    };

    public void lock() {
        myAqs.acquire(1);
    }

    public void lockInterruptibly() throws InterruptedException {

    }

    public boolean tryLock() {
        return false;
    }

    public boolean tryLock(long time, TimeUnit unit) throws InterruptedException {
        return false;
    }

    public void unlock() {
        myAqs.release(1);
    }

    public Condition newCondition() {
        return null;
    }
}
