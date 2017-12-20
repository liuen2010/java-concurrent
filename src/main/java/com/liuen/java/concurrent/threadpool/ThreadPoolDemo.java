package com.liuen.java.concurrent.threadpool;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import com.liuen.java.util.OSUtil;
import com.liuen.java.util.RandomUtil;

/**
 * java.util.concurrent主要包含原子量、并发集合、同步器、可重入锁，并对线程池的构造提供了强力的支持。
 * 
 * 线程池的类型：
 * 	FixedThreadPool：定长线程池，提交任务时创建线程，直到池的最大容量，如果有线程非预期结束，会补充新线程
 * 	CachedThreadPool：可变线程池，它犹如一个弹簧，如果没有任务需求时，它回收空闲线程，如果需求增加，则按需增加线程，不对池的大小做限制
 * 	SingleThreadExecutor：单线程。处理不过来的任务会进入FIFO队列等待执行
 * 	SecheduledThreadPool：周期性线程池。支持执行周期性线程任务
 * 备注：创建线程池的本质是使用ThreadPoolExecutor的重载构造方法创建不同线程池
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
 *
 * 线程池使用阻塞队列（BlockingQueue）来控制线程同步和任务调度。
 *
 * 线程池的使用：
 * 提交 Runnable ，任务完成后 Future 对象返回 null，
 * 	调用excute,提交任务, 匿名Runable重写run方法, run方法里是业务逻辑
 * 提交 Callable，该方法返回一个 Future 实例表示任务的状态，
 * 	调用submit提交任务, 匿名Callable,重写call方法, 有返回值, 获取返回值会阻塞,一直要等到线程任务返回结果
 *
 * 示例代码
 * @author liuen
 *
 */
public class ThreadPoolDemo {

	public static void main(String[] args) throws InterruptedException, ExecutionException {
		// 用于存放线程执行返回的结果，runnable无返回值，callable有返回值
		List<Future<?>> resultList = new ArrayList<Future<?>>();
		
		ScheduledExecutorService scheduledThreadPool = createScheduledThreadPool(OSUtil.getCpuKernelNum());
		
		Future<?> submit = null;
		for (int i = 0; i < 10; i++) {
			// runnable无返回值
//			submit = scheduledThreadPool.submit(createRunnable(i));
			
			// callable有返回值
			//对于schedulerPool来说，调用submit提交任务时，跟普通pool效果一致
//			submit = scheduledThreadPool.submit(createCallable(i));
			//对于schedulerPool来说，调用schedule提交任务时，则可按延迟，按间隔时长来调度线程的运行
			submit = scheduledThreadPool.schedule(createCallable(i), 2, TimeUnit.SECONDS);
			//存储线程执行结果
			
			resultList.add(submit);
		}
		
		Thread.sleep(5000);
		
		//打印结果
		for(Future<?> f: resultList){
			boolean done = f.isDone();
			System.out.println(done?"已完成":"未完成");  //从结果的打印顺序可以看到，即使未完成，也会阻塞等待
			System.out.println("线程返回future结果： " + f.get());
		}
		
		scheduledThreadPool.shutdown();
	}
	
	public static Callable<String> createCallable(int i) {
		return () -> {
			String tdName = Thread.currentThread().getName();
			System.out.println(tdName+" 启动时间：" + System.currentTimeMillis()/1000);
			
			try {
				Thread.sleep(RandomUtil.generateRandomNum(10)*100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			System.out.println(tdName + " is working..." + i);
			return i + "";
		};
	}
	
	public static Runnable createRunnable(int i) {
		return () -> {
			String tdName = Thread.currentThread().getName();
			System.out.println(tdName+" 启动时间：" + System.currentTimeMillis()/1000);
			
			try {
				Thread.sleep(RandomUtil.generateRandomNum(10)*100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			System.out.println(tdName + " is working..." + i);
		};
	}
	
	/**
	 * 单线程线程池，保证所有任务按照指定顺序(FIFO, LIFO, 优先级)执行。
	 * @return
	 */
	public static ExecutorService createSingleThreadExecutor() {
		return Executors.newSingleThreadExecutor();
	}
	
	/**
	 * 调度型的单线程池，用来调度任务在指定的时间执行
	 * @return
	 */
	public static ScheduledExecutorService createSingleThreadScheduledExecutor() {
		return Executors.newSingleThreadScheduledExecutor();
	}
	
	/**
	 * 可缓存的线程池，线程池中的可用线程会被新任务触发重新执行，超过60秒没被任务使用的线程，将被销毁并从线程池中移除
	 * @return
	 */
	public static ExecutorService createCachedThreadPool() {
		return Executors.newCachedThreadPool();
	}
	
	/**
	 * 定长线程池
	 * 如果没有任务执行，那么线程也会一直等待，不会自动销毁。如果线程池内的线程都被使用，任务会在队列中等待。
	 * @return
	 */
	public static ExecutorService createFixedThreadPool(int poolSize) {
		return Executors.newFixedThreadPool(poolSize);
	}
	
	/**
	 * 调度型的定长线程池，支持定时或周期性任务执行
	 * @return
	 */
	public static ScheduledExecutorService createScheduledThreadPool(int poolSize) {
		return Executors.newScheduledThreadPool(poolSize);
	}

}
