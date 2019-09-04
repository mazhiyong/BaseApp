package com.lr.biyou.utils.tool;

import android.os.AsyncTask;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import androidx.annotation.MainThread;
import androidx.annotation.WorkerThread;

public class AsyncTaskUtil  {

    private static TaskCallback callback;
    public static ProgressBack mcallback;


    public static TaskCallback getCallback() {
        return callback;
    }

    public static ProgressBack getMcallback() {
        return mcallback;
    }

    public static void setMcallback(ProgressBack mcallback) {
        AsyncTaskUtil.mcallback = mcallback;
    }

    public static void setCallback(TaskCallback callback) {
        AsyncTaskUtil.callback = callback;
    }



    /**
     * 设置核心线程数并开始执行
     * @param num  核心线程数
     * @param callback  执行结果回调处理
     */
    public static void excute(int num,TaskCallback callback){
         //设置核心线程数
         setCoreThreadNum(num);
         new MyAsyncTask().execute();
         setCallback(callback);
         LogUtilDebug.i("show","当前线程："+Thread.currentThread().getName());
     }


    /**
     * AsyncTask  支持串行（线程同步） 和 并行（线程异步 默认4个线程异步）
     * 通过反射机制  自定义并行的线程数
     * @param num num == 1  为线程同步（串行）
     */
     private static void setCoreThreadNum(int num){
         try {
             //Class<?> cls = Class.forName("AsyncTask");
             Method setExecutor = AsyncTask.class.getMethod("setDefaultExecutor", Executor.class);

             //阻塞任务队列 最大128个任务
             LinkedBlockingQueue<Runnable> sPoolWorkQueue = new LinkedBlockingQueue<Runnable>(128);

             //新建线程工厂
             final ThreadFactory sThreadFactory = new ThreadFactory() {
                 private final AtomicInteger mCount = new AtomicInteger(1);

                 public Thread newThread(Runnable r) {
                     return new Thread(r, "AsyncTask #" + mCount.getAndIncrement());
                 }
             };
             //核心线程池大小  最大线程池大小  空闲线程存活时间  时间单位
             int CPU_COUNT = Runtime.getRuntime().availableProcessors();
             int CORE_POOL_COUNT = Math.max(2,Math.min(CPU_COUNT-1,4));
             int MAX_POOL_COUNT = CPU_COUNT*2+1;
             LogUtilDebug.i("show","CPU_COUNT:"+CPU_COUNT+"  默认：CORE_POOL_COUNT:"+CORE_POOL_COUNT+"   MAX_POOL_COUNT:"+MAX_POOL_COUNT);
             ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(
                     num, MAX_POOL_COUNT, 30, TimeUnit.SECONDS,
                     sPoolWorkQueue, sThreadFactory);
             threadPoolExecutor.allowCoreThreadTimeOut(true);

             Executor THREAD_POOL_EXECUTOR = threadPoolExecutor;

             AsyncTask task =new MyAsyncTask();

             setExecutor.invoke(task,THREAD_POOL_EXECUTOR);

         } catch (NoSuchMethodException e) {
             e.printStackTrace();
         } catch (IllegalAccessException e) {
             e.printStackTrace();

         } catch (
                 InvocationTargetException e) {
             e.printStackTrace();
         }

     }

    /**
     * 自定义AsyncTask
     */



    private static class MyAsyncTask extends AsyncTask<Map<Object,Object>,Integer,Map<Object,Object>> {
        public MyAsyncTask() {
            mcallback = new ProgressBack(){
                @WorkerThread
                @Override
                public void updateProgress(Integer... values) {
                    publishProgress(values[0]);
                }
            };
            setMcallback(mcallback);

        }

        //开始执行前（UI线程里的初始化工作）
        @MainThread
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if(callback != null){
                callback.initOnUI();
            }
        }


        //取消
        @Override
        protected void onCancelled() {
            super.onCancelled();
            if(callback != null){
                callback.cancellTask();
            }
        }

        //子线程执行
        @WorkerThread
        @Override
        protected Map<Object, Object> doInBackground(Map<Object, Object>... maps) {
            if(callback != null){
                return callback.doTask(maps);
            }

            return null;
        }


        //子线程执行进度
        @MainThread
        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            if(callback != null){
                callback.progressTask(values);
            }
        }

        //子线程执行完成
        @MainThread
        @Override
        protected void onPostExecute(Map<Object, Object> objectObjectMap) {
            super.onPostExecute(objectObjectMap);
            if(callback != null){
                callback.resultTask(objectObjectMap);
            }
        }


    }

    public static interface TaskCallback{
        //UI线程中初始化
        void initOnUI();
        //取消线程任务
        void cancellTask();
        //执行线程任务，执行完毕返回执行结果
        Map<Object, Object> doTask(Map<Object, Object>... maps);
        //执行线程任务进度
        void progressTask(Integer... values);
        //执行完成获取执行结果并处理
        void resultTask(Map<Object, Object> objectObjectMap);

    }

    public interface ProgressBack{
        //更新进度值
        void updateProgress(Integer... values);
    }
}
