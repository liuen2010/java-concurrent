package com.liuen.java.util;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class OSUtil {

	/**
	 * 获取CPU核数
	 * @return
	 */
	public static int getCpuKernelNum() {
		return Runtime.getRuntime().availableProcessors();
	}
	
	/**
	 * 获取本机IP地址
	 * @return
	 * @throws UnknownHostException
	 */
	public static String getHostAddress() throws UnknownHostException {
		InetAddress localHost = InetAddress.getLocalHost();
		return localHost.getHostAddress();
	}
	
	/**
	 * 获取本机主机名
	 * @return
	 * @throws UnknownHostException
	 */
	public static String getHostName() throws UnknownHostException {
		InetAddress localHost = InetAddress.getLocalHost();
		return localHost.getHostName();
	}
	
	public static void main(String[] args) throws UnknownHostException {
		System.out.println(getHostAddress());
	}
	
}
