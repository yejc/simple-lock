package reentrantLock;

import aqs.MyAqs;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

/**
 * @program: simple-reentrantLock
 * @description:
 * @author: yejc
 * @create: 2019-11-16 22:23
 **/
public class MyReentrantLock implements Lock {

    private MyAqs myAqs = new MyAqs(0) {
        @Override
        protected boolean tryAcquire(int arg) {
            boolean isAcquire = state.compareAndSet(0, arg);
            if (isAcquire) {
                exclusiveOwner.set(Thread.currentThread());
            }
            return isAcquire;
        }

        @Override
        protected boolean tryRelease(int arg) {
            boolean isRelease = state.compareAndSet(arg, 0);
            if (isRelease) {
                exclusiveOwner = null;
            }
            return isRelease;
        }
    };

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
