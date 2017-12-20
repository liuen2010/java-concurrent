package com.liuen.java.concurrent.thread;

import java.util.concurrent.Callable;
import java.util.concurrent.CompletionService;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;

import com.liuen.java.util.RandomUtil;

/**
 * 
 * 实现Callable接口的线程，可以抛出异常，有返回值
 * Callable：执行线程产生结果
 * Future：获取Callable线程执行的结果
 * 
 * 返回值的任务必须实现Callable接口
 * @author liuen
 *
 */
public class CallableImplDemo{

	public static void main(String[] args) throws Exception {
	}
	
	public void testFuture() throws Exception {
		// FutureTask实现了Runnable、Future两个接口，所以它既可以Runnable被执行，也可以作为Future获取执行线程的结果
		FutureTask<Integer> futureTask = new FutureTask<>(CallableImplDemo.createCallable());

		// 通过FutureTask启动线程
		new Thread(futureTask).start();
		
		Thread.sleep(1000);
		System.out.println("futureTask-future：" + futureTask.get());

	}
	
	/**
	 * 创建Callable实例对象
	 * @return
	 */
	public static Callable<Integer> createCallable() {
		return () -> {
			String tdName = Thread.currentThread().getName();
			System.out.println(tdName + "线程call方法开始执行，开始作业" );
			System.out.println("do something...");
			Thread.sleep(RandomUtil.generateRandomNum(10)*100);
			System.out.println(tdName + "线程执行完毕，结束作业" );
			System.out.println("-----------------------------------------------------------");
			return RandomUtil.generateRandomNum(100);
		};
	}
	
	public static void someThing() {
		//     其实也可以不使用CompletionService，可以先创建一个装Future类型的集合，用Executor提交的任务返回值添加到集合中，最后遍历集合取出数据，
        ExecutorService threadPool = Executors.newCachedThreadPool();
        CompletionService<Integer> cs = new ExecutorCompletionService<Integer>(threadPool);
        for(int i = 1; i < 5; i++) {
            final int taskID = i;
            cs.submit(new Callable<Integer>() {
                public Integer call() throws Exception {
                    return taskID;
                }
            });
        }
        
        // 主线程的其他任务
        for(int i = 1; i < 5; i++) {
            try {
				System.out.println(cs.take().get());
			} catch (InterruptedException | ExecutionException e) {
				e.printStackTrace();
			}
        }
	}
}
