package com.liuen.java.concurrent.thread;

import com.liuen.java.util.RandomUtil;

/**
 * 线程内不能抛出异常，并且没有返回值，所以无返回值的任务必须继承Thread类或实现Runnable接口
 * @author liuen
 *
 */
public class RunnableImplDemo{
	
	public static Runnable createRunnable() {
		return () -> {
			String tdName = Thread.currentThread().getName();
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
		};
	}

	public static void main(String[] args) {
		Thread thread1 = new Thread(RunnableImplDemo.createRunnable());
		Thread thread2 = new Thread(RunnableImplDemo.createRunnable());
		// 直接调用Thread.run()，JVM会将runnable当做一个普通的对象来调用run方法，不会启动新线程
		thread1.run();
		thread2.run();
		
		// 使用Thread.start()来启动新线程
		thread1.start();
		thread2.start();
		
		System.out.println("主进程执行完毕");
		
		// System.exit()，程序退出，无论是否还有子线程在执行
//		System.exit(0);
	}
}
