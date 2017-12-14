package com.liuen.java.concurrent.lock;

import java.util.ArrayList;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Lock和synchronized的区别：
 * 	1. synchronized是Java的关键字，是Java的内置特性，Lock是java.util.concurrent.locks包下的一个类，需要通过这个类来实现同步访问；
 *  2. synchronized在代码执行完或发生异常的情况下可以自动释放锁，Lock必须手工释放锁，在忘记手工释放锁的情况下会导致出现死锁现象。
 * 
 * Lock接口的特点：
 * 	1. 实现Lock接口的锁，必须手工释放，就算线程内发生异常，锁也不会被释放；
 *  2. 必须在try{}catch{}代码块中使用Lock，并且要将锁释放的操作放在finally代码块中。
 *  
 * 获取锁的方法：
 *  lock()：平时使用最多的上锁的方法，如果锁被其他线程锁定，则继续等待；
 *  tryLock()：上锁成功返回true，否则返回false，即线程上锁失败后，不会一直等待再次上锁；
 *  tryLock(long time, TimeUnit unit)：tryLock方法的扩展，在指定时间范围内上锁返回true，否则返回false；
 *  lockInterruptibly()：多个线程争抢着上锁，当其中一个上锁成功后，则调用其他线程的thread.interrupted()中断其他线程抢锁的行为，
 *  	用synchronized同步代码块的话，其他没有上锁成功的线程只能无期限的等待下去。
 * 
 * 释放锁的方法：
 *  unLock()
 * 
 * 常用的实现了Lock接口的类：
 * 	ReentrantLock：可重入锁
 *  ReentrantReadWriteLock：可重入读写锁
 * 
 * @author liuen
 *
 */
public class LockInterruptiblyDemo {
	private static ArrayList<String> arrayList = new ArrayList<String>();// 存放两个线程内总共循环的次数
	public static ReentrantLock reentrantLock = new ReentrantLock();// 公共锁
	
	public static void main(String[] args) {
		new Thread(() -> {
			String thName = Thread.currentThread().getName();
			for (int i = 0; i < 100000; i++) {
				boolean isTrue = reentrantLock.tryLock();//上锁
				if (isTrue) {
					try {
						System.out.println(thName + "线程上锁成功，开始作业");
						System.out.println(thName + " running ... " + i);
						arrayList.add(thName + i);
//					Thread.sleep(RandomUtil.generateRandomNum(10)*100);
						System.out.println(thName + "线程作业完毕，准备释放锁");
						System.out.println("-----------------------------------------------------------");
					} catch (Exception e) {
						e.printStackTrace();
					} finally {
						reentrantLock.unlock();//释放锁
					}
				}
			}
			System.out.println(thName + "：" + arrayList.size());
			System.out.println("-----------------------------------------------------------");
		}).start();
		
		new Thread(() -> {
			String thName = Thread.currentThread().getName();
			for (int i = 0; i < 100000; i++) {
				boolean isTrue = reentrantLock.tryLock();//上锁
				if (isTrue) {
					try {
						System.out.println(thName + "线程上锁成功，开始作业");
						System.out.println(thName + " running ... " + i);
						arrayList.add(thName + i);
//					Thread.sleep(RandomUtil.generateRandomNum(10)*100);
						System.out.println(thName + "线程作业完毕，准备释放锁");
						System.out.println("-----------------------------------------------------------");
					} catch (Exception e) {
						e.printStackTrace();
					} finally {
						reentrantLock.unlock();//释放锁
					}
				}
			}
			System.out.println(thName + "：" + arrayList.size());
			System.out.println("-----------------------------------------------------------");
		}).start();
	}
	
}
