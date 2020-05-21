package com.example.redis.taskpool;

import java.util.concurrent.*;

public class MunalTaskPool<T> {



    private static class MunualTaskPoolBuilder {
        private static final MunalTaskPool INSTANCE = new MunalTaskPool();
    }

    private BlockingQueue<Runnable> queue;
    private RejectedExecutionHandler policy;
    private ThreadPoolExecutor threadPoolExecutor;
    private ExecutorCompletionService<T> esc;

    private final static Integer QUEUE_SIZE = 20;

    private MunalTaskPool(){
        int poolSize = Runtime.getRuntime().availableProcessors() * 2;
        queue = new ArrayBlockingQueue<>(QUEUE_SIZE);
        policy = new ThreadPoolExecutor.CallerRunsPolicy ();
        threadPoolExecutor = new ThreadPoolExecutor(poolSize, poolSize, 0, TimeUnit.SECONDS, queue, policy);
        esc = new ExecutorCompletionService<>(threadPoolExecutor);
    }

    public static MunalTaskPool getInstance(){
        return MunualTaskPoolBuilder.INSTANCE;
    }

    public Future<T> submit(Callable<T> callable){
       return esc.submit(callable);
    }

    public T take() throws InterruptedException, ExecutionException {
        return esc.take().get();
    }

    public void execute(Runnable thread){
        threadPoolExecutor.execute(thread);
    }





}
