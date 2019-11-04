package lhy.lhylibrary.utils;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 线程池管理者,两个线程
 * Created by lilaoda on 2016/8/25.
 */
public class ThreadManager {

    private ThreadPoolProxy smallThreadPoolProxy;
    private ThreadPoolProxy longThreadPoolProxy;

    /**
     * 适合联网操作
     * @return
     */
    public  synchronized ThreadPoolProxy createSmallThradPool(){
        if(smallThreadPoolProxy==null){
            smallThreadPoolProxy=new ThreadPoolProxy(3,3,5000L);
        }
        return  smallThreadPoolProxy;
    }

    /**
     * 适合本地操作
     * @return
     */
    public synchronized  ThreadPoolProxy createLongThreadPool(){
        if(longThreadPoolProxy==null){
            longThreadPoolProxy=new ThreadPoolProxy(5,5,500L);
        }
        return  longThreadPoolProxy;
    }


    public class ThreadPoolProxy{
        private ThreadPoolExecutor threadPoolExecutor;
        private int corePoolSize;
        private int maximumPoolSize;
        private long keepAliveTime;

        public ThreadPoolProxy(int corePoolSize, int maximumPoolSize, long keepAliveTime) {
            this.corePoolSize = corePoolSize;
            this.keepAliveTime = keepAliveTime;
            this.maximumPoolSize = maximumPoolSize;
        }

        public void execute(Runnable runnable){
            if(threadPoolExecutor==null){
                threadPoolExecutor=  new ThreadPoolExecutor(corePoolSize,maximumPoolSize,keepAliveTime, TimeUnit.MICROSECONDS,new LinkedBlockingQueue<Runnable>(10), new ThreadPoolExecutor.CallerRunsPolicy());
            }
            threadPoolExecutor.execute(runnable);
        }
        public void cancel(Runnable runnable){
            if(threadPoolExecutor!=null&&!threadPoolExecutor.isShutdown()&&!threadPoolExecutor.isTerminated()){
                threadPoolExecutor.remove(runnable);
            }
        }
    }

}
