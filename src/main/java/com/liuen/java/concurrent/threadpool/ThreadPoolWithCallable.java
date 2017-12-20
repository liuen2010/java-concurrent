package com.liuen.java.concurrent.threadpool;

import java.util.concurrent.Callable;

import com.liuen.java.util.RandomUtil;

public class ThreadPoolWithCallable {
	
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
}
