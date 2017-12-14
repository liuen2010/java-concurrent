package com.liuen.java.concurrent.thread;

import com.liuen.java.util.RandomUtil;

/**
 * 线程内不能抛出异常，并且没有返回值，所以无返回值的任务必须继承Thread类或实现Runnable接口
 * @author liuen
 */
public class ThreadExtendedDemo extends Thread {
	
	@Override
	public void run() {
		String tdName = Thread.currentThread().getName();
		System.out.println(tdName + "线程的run方法被调用，开始作业");
		for (int i = 0; i < 10; i++) {
			System.out.println(tdName + " running ... " + i);
			try {
				Thread.sleep(RandomUtil.generateRandomNum(10)*100); //线程随机休眠一段时间
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		System.out.println(tdName + "线程的run方法执行完毕，结束作业");
	}
	
	public static void main(String[] args) {
		ThreadExtendedDemo thread1 = new ThreadExtendedDemo();
		ThreadExtendedDemo thread2 = new ThreadExtendedDemo();
		
		// 直接调用Thread.run()，JVM会将thread当做一个普通的对象来调用run方法，不会启动新线程
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
