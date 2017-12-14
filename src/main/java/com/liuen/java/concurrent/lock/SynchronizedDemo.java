package com.liuen.java.concurrent.lock;

import com.liuen.java.util.RandomUtil;

/**
 * 同步代码块，释放锁的情况有以下三种：
 * 1. 同步代码块中的代码执行完毕，自动释放锁；
 * 2. 同步代码块中发生异常，自动释放锁；
 * 3. 手工调用lock.wait()释放锁，并调用lock.notifyAll()通知等待获取锁的线程。
 * 
 * 同步代码块的缺点：
 * 发生冲突的操作是写操作，但是同步代码块在执行读操作的时候，其他获取不到锁的线程只能干巴巴地等待下去，
 * 	而不能采取一些措施来结束这种无期限的等待，例如：在一定时间内获取不到锁的话，可以自行中断等待。
 * @author liuen
 *
 */
public class SynchronizedDemo {
	public static final Object lock1 = new Object();
	public static final Object lock2 = new Object();
	
	public static void main(String[] args) {
//		oneLockTest();
//		twoLockTest();
		deadLockTest();
	}
	
	/**
	 * 两个线程争抢同一把锁，同时只有一个线程在执行。
	 */
	public static void oneLockTest() {
		new Thread(() -> {
			String tdName = Thread.currentThread().getName();
			synchronized (lock1) {
				System.out.println(tdName + "线程run方法被调用，开始作业");
				for (int i = 0; i < 10; i++) {
					System.out.println(tdName + " running ... " + i);
					if (i == RandomUtil.generateRandomNum(10)) {
						i = 1/0;//线程内发生异常时，jvm会自动将锁释放
					}
					try {
						Thread.sleep(RandomUtil.generateRandomNum(10)*100);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				System.out.println(tdName + "线程run方法执行完毕，结束作业");
			}
		}).start();
		
		new Thread(() -> {
			String tdName = Thread.currentThread().getName();
			synchronized (lock1) {
				System.out.println(tdName + "线程run方法被调用，开始作业");
				for (int i = 0; i < 10; i++) {
					System.out.println(tdName + " running ... " + i);
					try {
						Thread.sleep(RandomUtil.generateRandomNum(10)*100);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				System.out.println(tdName + "线程run方法执行完毕，结束作业");
			}
		}).start();
	}
	
	/**
	 * 两个线程各自有持有一把锁，线程运行互不影响。
	 */
	public static void twoLockTest() {
		new Thread(() -> {
			String tdName = Thread.currentThread().getName();
			synchronized (lock1) {
				System.out.println(tdName + "线程run方法被调用，开始作业");
				for (int i = 0; i < 10; i++) {
					System.out.println(tdName + " running ... " + i);
					try {
						Thread.sleep(RandomUtil.generateRandomNum(10)*100);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				System.out.println(tdName + "线程run方法执行完毕，结束作业");
			}
		}).start();
		
		new Thread(() -> {
			String tdName = Thread.currentThread().getName();
			synchronized (lock2) {
				System.out.println(tdName + "线程run方法被调用，开始作业");
				for (int i = 0; i < 10; i++) {
					System.out.println(tdName + " running ... " + i);
					try {
						Thread.sleep(RandomUtil.generateRandomNum(10)*100);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				System.out.println(tdName + "线程run方法执行完毕，结束作业");
			}
		}).start();
	}
	
	/**
	 * 死锁测试，下面代码中Thread1只释放了lock2，lock1仍然被Thread1，
	 * 	Thread2获取到lock2后，获取不到lock1，所以Thread2后面的代码无法执行，
	 * 	此时Thread1一直持有lock1，但是获取不到lock2，所以Thread1后面的代码也无法执行，以上这种情况就叫做死锁。
	 */
	public static void deadLockTest() {
		// Thread1
		new Thread() {
			public void run() {
				String tdName = Thread.currentThread().getName();
				synchronized (lock1) {
					System.out.println(tdName + "线程run方法被调用，开始作业");
					synchronized (lock2) {
						for (int i = 0; i < 10; i++) {
							System.out.println(tdName + " running ... " + i);
							try {
								Thread.sleep(RandomUtil.generateRandomNum(10)*100);
								lock2.wait();//释放CPU资源
								lock2.notifyAll();//通知其他在等待的线程
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
					}
					System.out.println(tdName + "线程run方法执行完毕，结束作业");
				}
			}
		}.start();
		
		// Thead2
		new Thread() {
			public void run() {
				String tdName = Thread.currentThread().getName();
				synchronized (lock2) {
					System.out.println(tdName + "线程run方法被调用，开始作业");
					synchronized (lock1) {
						for (int i = 0; i < 10; i++) {
							System.out.println(tdName + " running ... " + i);
							try {
								Thread.sleep(RandomUtil.generateRandomNum(10)*100);
							} catch (InterruptedException e) {
								e.printStackTrace();
							}
						}
					}
					System.out.println(tdName + "线程run方法执行完毕，结束作业");
				}
			};
		}.start();
	}
	
}
