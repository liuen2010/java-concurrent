package com.liuen.java.concurrent.thread;

/**
 * ThreadLocal存放的值是线程内共享的，线程间互斥的
 * @author liuen
 *
 */
public class TheadLocalDemo {
	public static final ThreadLocal<Object> tdl = new ThreadLocal<>();
	
	
}
