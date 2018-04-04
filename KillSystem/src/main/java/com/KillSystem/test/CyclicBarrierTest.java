package com.KillSystem.test;

import java.io.IOException;
import java.util.Random;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CyclicBarrierTest {  
	  
    public static void main(String[] args) throws IOException, InterruptedException {  
        //如果将参数改为4，但是下面只加入了3个选手，这永远等待下去  
        //Waits until all parties have invoked await on this barrier.   
        CyclicBarrier barrier = new CyclicBarrier(800);  
  
        ExecutorService executor = Executors.newFixedThreadPool(2000);  
        for (int i = 0;i < 800;i++) {
        	Runner a = new Runner(barrier, i+"号选手");
        	executor.submit(new Thread(a)); 
        }
         
  
        executor.shutdown();  
        
    }  
}  
  
class Runner implements Runnable {  
    // 一个同步辅助类，它允许一组线程互相等待，直到到达某个公共屏障点 (common barrier point)  
    private CyclicBarrier barrier;  
  
    public static int count = 0;
    
    private String name;  
  
    private float excTime;
    
    public Runner(CyclicBarrier barrier, String name) {  
        super();  
        this.barrier = barrier;  
        this.name = name;  
    }  
  
    @Override  
    public void run() {  
        try {  
            Thread.sleep(2000 * (new Random()).nextInt(8));  
            System.out.println(name + " 准备好了...");  
            // barrier的await方法，在所有参与者都已经在此 barrier 上调用 await 方法之前，将一直等待。  
            barrier.await();  
        } catch (InterruptedException e) {  
            e.printStackTrace();  
        } catch (BrokenBarrierException e) {  
            e.printStackTrace();  
        }  
        System.out.println(name+"出发");
        long startTime=System.currentTimeMillis();
        String ret = HttpRequest.sendPost("http://localhost/KillSystem/createOrder.do", "tel_num=175"+name+"&address=xclzyjia"+name+"&goods_id=1");  
        long endTime=System.currentTimeMillis();
        System.out.println(name+"完成"+count++);
        excTime = (float)(endTime-startTime)/1000;
        System.out.println(name+":"+excTime+"s");
    }  
}  