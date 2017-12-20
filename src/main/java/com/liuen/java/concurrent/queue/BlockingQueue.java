package com.liuen.java.concurrent.queue;

/**
 * 
 * BlockingQueue：阻塞队列是java.util.concurrent下用来控制线程同步的工具，主要用来控制线程同步和任务调度，
 * 
 * 常用方法如下：
 *	入栈：
 * 	 add(anObject)：把anObject加到BlockingQueue里,即如果BlockingQueue可以容纳,则返回true,否则抛出异常,不好
 * 	 offer(anObject)：表示如果可能的话,将anObject加到BlockingQueue里,即如果BlockingQueue可以容纳,则返回true,否则返回false.
 * 	 put(anObject)：把anObject加到BlockingQueue里,如果BlockQueue没有空间,则调用此方法的线程被阻断直到BlockingQueue里面有空间再继续, 有阻塞, 放不进去就等待
 *  出栈：
 * 	 poll(time)：取走BlockingQueue里排在首位的对象,若不能立即取出,则可以等time参数规定的时间,取不到时返回null; 取不到返回null
 * 	 take()：取走BlockingQueue里排在首位的对象,若BlockingQueue为空,阻断进入等待状态直到Blocking有新的对象被加入为止; 阻塞, 取不到就一直等
 * 	 poll：
 *  其他：
 *   int remainingCapacity();返回队列剩余的容量，在队列插入和获取的时候，不要瞎搞，数	据可能不准, 不能保证数据的准确性
 *	 boolean remove(Object o); 从队列移除元素，如果存在，即移除一个或者更多，队列改	变了返回true
 *	 public boolean contains(Object o); 查看队列是否存在这个元素，存在返回true
 *	 int drainTo(Collection<? super E> c); //移除此队列中所有可用的元素,并将它们添加到给定 collection 中。取出放到集合中
 *	 int drainTo(Collection<? super E> c, int maxElements); 和上面方法的区别在于，指定了移	动的数量; 取出指定个数放到集合
 *
 * BlockingQueue由四个实现类，常用实现的两个实现类如下：
 *  1.ArrayBlockingQueue：由数组组成的有界队列，必须指定队列的大小，
 *  	按FIFO（先入先出）的规则来存取队列中的对象。
 *  2.LinkedBlockingQueue：大小不定的队列，默认队列大小是Integer.MAX_VALUE，可以指定队列的大小，
 *  	按FIFO（先入先出）的规则来存取队列中的对象。
 *  
 * 示例代码：生产者和消费者
 * @author liuen
 *
 */
public class BlockingQueue {

}
