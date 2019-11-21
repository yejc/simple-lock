package semaphore;

import aqs.MyAqs;

/**
 * @program: simple-reentrantLock
 * @description:
 * @author: yejc
 * @create: 2019-11-16 23:30
 **/
public class MySemaphore {

    private MyAqs myAqs;

    public MySemaphore(int count) {
        myAqs = new MyAqs(count) {
            @Override
            protected int tryAcquireShared(int arg) {
                while (true) {
                    int curState = state.get();
                    int n = curState - 1;
                    if (curState <= 0 || n < 0) {
                        return -1;
                    }
                    return state.compareAndSet(curState, n) ? 1 : -1;
                }
            }

            @Override
            protected boolean tryReleaseShared(int arg) {
                return state.incrementAndGet() >= 0;
            }
        };
    }

    public void acquire() throws InterruptedException {
        myAqs.acquireShared(1);
    }

    public void release() {
        myAqs.releaseShared(1);
    }
}
