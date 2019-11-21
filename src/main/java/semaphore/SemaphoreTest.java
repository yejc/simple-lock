package semaphore;

/**
 * @program: simple-lock
 * @description:
 * @author: yejc
 * @create: 2019-11-17 14:43
 **/
public class SemaphoreTest {

    public static void main(String[] args) throws InterruptedException {
        MySemaphore mySemaphore = new MySemaphore(2);
        for (int i = 0; i < 10; i++) {
            new Thread(() -> {
                try {
                    mySemaphore.acquire();
                    System.out.println("I'm here.");
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    mySemaphore.release();
                }
            }).start();
        }
        Thread.sleep(5000);
        System.out.println("done!");
    }
}
