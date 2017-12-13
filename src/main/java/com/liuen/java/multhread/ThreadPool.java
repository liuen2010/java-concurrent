package com.liuen.java.multhread;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

/**
 * 线程池的类型：
 * 	FixedThreadPool：定长线程池，提交任务时创建线程，直到池的最大容量，如果有线程非预期结束，会补充新线程
 * 	CachedThreadPool：可变线程池，它犹如一个弹簧，如果没有任务需求时，它回收空闲线程，如果需求增加，则按需增加线程，不对池的大小做限制
 * 	SingleThreadExecutor：单线程。处理不过来的任务会进入FIFO队列等待执行
 * 	SecheduledThreadPool：周期性线程池。支持执行周期性线程任务
 * 备注：创建线程池的本质是使用ThreadPoolExecutor的重载构造方法创建不同线程池
 * 
 * 线程池中的消息队列：
 * 	BlockingQueue：主要用来控制线程同步和任务调度，阻塞队列的常用方法如下：
 *	入栈：
 * 	 add(anObject)：把anObject加到BlockingQueue里,即如果BlockingQueue可以容纳,则返回true,否则抛出异常,不好
 * 	 offer(anObject)：表示如果可能的话,将anObject加到BlockingQueue里,即如果BlockingQueue可以容纳,则返回true,否则返回false.
 * 	 put(anObject)：把anObject加到BlockingQueue里,如果BlockQueue没有空间,则调用此方法的线程被阻断直到BlockingQueue里面有空间再继续, 有阻塞, 放不进去就等待
 *  出栈：
 * 	 poll(time)：取走BlockingQueue里排在首位的对象,若不能立即取出,则可以等time参数规定的时间,取不到时返回null; 取不到返回null
 * 	 take()：取走BlockingQueue里排在首位的对象,若BlockingQueue为空,阻断进入等待状态直到Blocking有新的对象被加入为止; 阻塞, 取不到就一直等
 * 	 poll：
 *  其他：
 *   int remainingCapacity();返回队列剩余的容量，在队列插入和获取的时候，不要瞎搞，数	据可能不准, 不能保证数据的准确性
 *	 boolean remove(Object o); 从队列移除元素，如果存在，即移除一个或者更多，队列改	变了返回true
 *	 public boolean contains(Object o); 查看队列是否存在这个元素，存在返回true
 *	 int drainTo(Collection<? super E> c); //移除此队列中所有可用的元素,并将它们添加到给定 collection 中。取出放到集合中
 *	 int drainTo(Collection<? super E> c, int maxElements); 和上面方法的区别在于，指定了移	动的数量; 取出指定个数放到集合

 * 
 * 线程池的常用配置：
 * 1. corePoolSize(int)：线程池的基本大小；
 * 2. maximumPoolSize(int)：线程池的最大大小；
 * 3. keepAliveTime(long)：线程没有任务执行时，最多保持多久时间才会被终止。默认情况下，当线程池中的线程数量大于corePoolSize的时候，keepAliveTime才会起作用；
 * 4. unit(TimeUnit)：keepAliveTIme的时间单位，默认是TimeUnit.SECONDS；
 * 5. workQueue(BlockingQueue)：一个阻塞的队列，用来存储等待执行的任务；
 * 6. threadFactory(ThreadFactory)：线程工厂，主要用来创建线程；
 * 7. handler(RejectedExecutionHandler)：拒绝执行任务时的策略，默认策略是AbortPolicy，JDK提供以下四种策略，可以根据业务场景来选择：
 * 	ThreadPoolExecutor.AbortPolicy：直接抛出异常
 * 	ThreadPoolExecutor.DiscardPolicy：直接丢弃当前任务，代码里面是空实现
 * 	ThreadPoolExecutor.DiscardOldestPolicy：丢弃队列前面的任务，执行当前被拒绝的任务
 * 	ThreadPoolExecutor.CallerRunsPolicy：由调用当前线程的父线程来处理任务
 * 8. 线程个数的参考公式：
 *	CPU密集型任务：需要尽量压榨CPU，参考值可以设为 NCPU+1，N指CPU的个数
 *	IO密集型任务，参考值可以设置为2*N，N指CPU的个数
 * 	启动的线程数 = [ 任务执行时间 / ( 任务执行时间 - IO等待时间 ) ] x CPU内核数
 *	以上的公式仅供参考，具体的线程个数需要根据实际情况进行调整，可以先将线程池的大小设置为参考值，然后根据观察任务运行的情况、系统资源利用率、系统负载情况等情况进行适当的调整
 * @author liuen
 *
 */
public class ThreadPool {

	public static void main(String[] args) {
		int cpuCount = Runtime.getRuntime().availableProcessors();
		System.out.println(cpuCount);
		System.exit(0);
		
		// 单线程线程池，它只会用唯一的工作线程来执行任务，保证所有任务按照指定顺序(FIFO, LIFO, 优先级)执行。
		ExecutorService newSingleThreadExecutor = Executors.newSingleThreadExecutor();

		// 调度型的单线程池，用来调度任务在指定的时间执行
		ScheduledExecutorService newSingleThreadScheduledExecutor = Executors.newSingleThreadScheduledExecutor();
		
		// 可缓存的线程池，线程池里有很多线程需要同时执行，老的可用线程将被新的任务触发重新执行，如果线程超过60秒内没执行，那么将被终止并从池中删除，
		ExecutorService newCachedThreadPool = Executors.newCachedThreadPool();
		
		// 定长线程池，拥有固定线程数的线程池，如果没有任务执行，那么线程也会一直等待。如果线程池内的线程都被使用，任务会在队列中等待。
		// 可以和CPU的核数设置的一致，获取CPU核数的方法：int cpuCount = Runtime.getRuntime().availableProcessors();
		ExecutorService newFixedThreadPool = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
		
		// 调度型的定长线程池，支持定时或周期性任务执行
		ScheduledExecutorService newScheduledThreadPool = Executors.newScheduledThreadPool(Runtime.getRuntime().availableProcessors());
		
		
	}
}
