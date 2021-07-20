## CountDownLatch
   **CountDownLatch能够使一个或多个线程等待其他线程完成各自的工作后再执行**；CountDownLatch是JDK 5+里面闭锁的一个实现。
   
   **闭锁（Latch）：** 一种同步方法，可以延迟线程的进度直到线程到达某个终点状态。通俗的讲就是，一个闭锁相当于一扇大门，在大门打开之前所有线程都被阻断，一旦大门打开所有线程都将通过，但是一旦大门打开，所有线程都通过了，那么这个闭锁的状态就失效了，门的状态也就不能变了，只能是打开状态。也就是说闭锁的状态是一次性的，它确保在闭锁打开之前所有特定的活动都需要在闭锁打开之后才能完成。
   
   与CountDownLatch第一次交互是主线程等待其它的线程，主线程必须在启动其它线程后立即调用await方法，这样主线程的操作就会**在这个方法上阻塞**，直到其他线程完成各自的任务。
   
   **其他的N个线程必须引用闭锁对象**，因为他们需要通知CountDownLatch对象，他们已经完成了各自的任务，这种机制就是通过countDown()方法来完成的。每调用一次这个方法，在构造函数中初始化的count值就减1，
   所以当N个线程都调用了这个方法c**ount的值等于0**，然后**主线程就能通过await方法，恢复自己的任务。**
   
   示例:CountDownLatchExample1 、CountDownlatchTest
   
   **注:**
   
   1. latch.countDown(); 建议放到finally语句里。
   
   2. 对这个计数器的操作都是原子操作，同时只能有一个线程去操作这个计数器。
   
   方法：
   
    1、CountDownLatch(int count)  构造函数初始化 count 个
    2、await()    当前线程等待 latch 为 0 的时候再执行 （常用）
    3、await(long timeout, TimeUnit unit)  当前线程等待超时后中断 （常用）
    4、countDown()  每次执行，latch 减 1，知道 latch 为 0. （常用）
    5、getCount() 获取当前 latch 的数量
   
## CyclicBarrier
栅栏类似于闭锁，它能阻塞一组线程直到某个事件的发生。栅栏与闭锁的关键区别在于，所有的线程必须同时到达栅栏位置，才能继续执行。闭锁用于等待事件，而栅栏用于等待其他线程。

CyclicBarrier可以使一定数量的线程反复地在栅栏位置处汇集。当线程到达栅栏位置时将调用await方法，这个方法将阻塞直到所有线程都到达栅栏位置。如果所有线程都到达栅栏位置，那么栅栏将打开，此时所有的线程都将被释放，而栅栏将被重置以便下次使用。

![](https://img-blog.csdnimg.cn/20181218144511688)

![](https://img-blog.csdnimg.cn/img_convert/fa4d24955103ee1c8c0564ab45eebe26.png)

示例：CyclicBarrierExample3

## cyclicbarrier和countdownlatch的区别
   
   * cyclicbarrier支持重用
   * countdownlatch不支持重用
   * countdownlatch用于保持多个线程之间的执行顺序
   * cyclicbarrier用于一组线程互相等待对方执行一些操作后再一起继续执行
   * 用的侧重点不同，但是countdownlatch在某些时候是可以和cyclicbarrier互换的
   * 如果要用countdownlatch代替cyclicbarrier那么必须在每个线程的操作完成之后调用countDown方法
   * CountDownLatch一般用于某个线程A等待若干个其他线程执行完任务之后，它才执行；
   * 而CyclicBarrier一般用于一组线程互相等待至某个状态，然后这一组线程再同时执行；
   * countdownlatch是计数器，线程完成一个就记一个，就像报数，只不过是递减的
   * 而CyclicBarrier更像一个水闸，线程执行就想水流动，但是会在水闸处停止，直至水满（线程都运行完毕）才开始泄流
   * CyclicBarrier的计数器由自己控制，而CountDownLatch的计数器则由使用者来控制，在CyclicBarrier中线程调用await方法不仅会将自己阻塞还会将计数器减1，而在CountDownLatch中线程调用await方法只是将自己阻塞而不会减少计数器的值。
   * CountDownLatch只能拦截一轮，而CyclicBarrier可以实现循环拦截
   
   
        https://blog.csdn.net/wangzibai/article/details/102669112
        
## Semaphore
**Semaphore(信号量)**：是一种计数器，用来保护一个或者多个共享资源的访问。如果线程要访问一个资源就必须先获得信号量。如果信号量内部计数器大于0，信号量减1，然后允许共享这个资源；否则，如果信号量的计数器等于0，信号量将会把线程置入休眠直至计数器大于0.当信号量使用完时，必须释放。

主要方法：

     Semaphore(int permits, boolean fair); permits 初始许可数，也就是最大访问线程数.fair 当设置为false时，创建的信号量为非公平模式；当设置为true时，信号量是公平模式
     void acquire() ：从信号量获取一个许可，如果无可用许可前将一直阻塞等待，
     void acquire(int permits) ：获取指定数目的许可，如果无可用许可前也将会一直阻塞等待.permits 初始许可数，也就是最大访问线程数
     boolean tryAcquire()：从信号量尝试获取一个许可，如果无可用许可，直接返回false，不会阻塞
     boolean tryAcquire(int permits)： 尝试获取指定数目的许可，如果无可用许可直接返回false
     boolean tryAcquire(int permits, long timeout, TimeUnit unit)： 在指定的时间内尝试从信号量中获取许可，如果在指定的时间内获取成功，返回true，否则返回false
     void release()： 释放一个许可，别忘了在finally中使用，注意：多次调用该方法，会使信号量的许可数增加，达到动态扩展的效果，如：初始permits为1， 调用了两次release，最大许可会改变为2
     int availablePermits()： 获取当前信号量可用的许可
     
**Semaphore采用了CAS来实现，尽量避免锁的使用，提高了性能**

示例：SemaphoreTest2

 