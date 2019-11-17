package aqs;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.locks.LockSupport;

/**
 * @program: MyAqs
 * @description:
 * @author: yejc
 * @create: 2019-11-16 21:57
 **/
public abstract class MyAqs {
    /**
     * 同步资源状态
     */
    protected volatile AtomicInteger state;

    /**
     * 资源当前占用的线程
     */
    protected volatile AtomicReference<Thread> exclusiveOwner;

    /**
     * 等待资源的线程集合
     */
    protected volatile BlockingQueue<Thread> waiters = new LinkedBlockingQueue<Thread>();

    public MyAqs(int state) {
        this.state = new AtomicInteger(state);
        exclusiveOwner = new AtomicReference<>();
    }

    /**
     * 独占状态下获取资源方法
     *
     * @param arg
     */
    public final void acquire(int arg) {

        // 进入锁池
        waiters.offer(Thread.currentThread());
        while (!tryAcquire(arg)) {
            LockSupport.park();
        }
        // 将线程移除锁池
        waiters.remove(Thread.currentThread());
    }

    /**
     * 共享状态下获取资源方法
     *
     * @param arg
     */
    public final void acquireShared(int arg) {
        // 进入锁池
        waiters.offer(Thread.currentThread());
        while (tryAcquireShared(arg) <= 0) {
            LockSupport.park();
        }
        // 将线程移除锁池
        waiters.remove(Thread.currentThread());
    }

    protected boolean tryAcquire(int arg) {
        throw new UnsupportedOperationException();
    }

    protected int tryAcquireShared(int arg) {
        throw new UnsupportedOperationException();
    }

    /**
     * 独占状态下释放资源方法
     *
     * @param arg
     * @return
     */
    public final boolean release(int arg) {
        if (tryRelease(arg)) {
            // 唤醒锁池里的一个线程
            Thread thread = waiters.poll();
            LockSupport.unpark(thread);
        }
        return true;
    }

    /**
     * 共享状态下释放资源方法
     *
     * @param arg
     * @return
     */
    public final boolean releaseShared(int arg) {
        if (tryReleaseShared(arg)) {
            // 唤醒锁池里的所有线程
            for (Thread waiter : waiters) {
                LockSupport.unpark(waiter);
            }
        }
        return true;
    }

    protected boolean tryRelease(int arg) {
        throw new UnsupportedOperationException();
    }

    protected boolean tryReleaseShared(int arg) {
        throw new UnsupportedOperationException();
    }

}
