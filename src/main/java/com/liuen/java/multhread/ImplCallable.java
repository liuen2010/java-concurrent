package com.liuen.java.multhread;

import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletionService;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;

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
public class ImplCallable{

	public static void main(String[] args) throws Exception {
		Callable<Integer> callable = ImplCallable.createThread();
		
		// 使用call方法执行线程
//		Object call = callable.call();
//		System.out.println(call);
		
		// FutureTask实现了Runnable、Future两个接口，所以它既可以作为Runnable被执行，也可以作为Future获取执行线程的结果
		FutureTask<Integer> futureTask = new FutureTask<>(callable);
//		futureTask.run();
		// 通过FutureTask启动线程
		new Thread(futureTask).start();
		new Thread(futureTask).start();
		new Thread(futureTask).start();
		new Thread(futureTask).start();
	}
	
	/**
	 * 创建实现Callable接口的新线程
	 * @return
	 */
	public static Callable<Integer> createThread() {
		Callable<Integer> callable = () -> {
			System.out.println("创建新线程：" + Thread.currentThread().getName());
			System.out.println("do something...");
			Thread.sleep(1000);
			return new Random().nextInt(100);
		};
		return callable;
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
