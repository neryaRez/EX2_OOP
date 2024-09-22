package Part_2;

import java.util.List;
import java.util.concurrent.*;

public class CustomExecutor extends ThreadPoolExecutor {

    public CustomExecutor() {
        super(2, 3, 300, TimeUnit.MILLISECONDS, new PriorityBlockingQueue<>());

    }

    /** @submit
     * Adapter_Task extends Future_Task
     * FutureTask takes Callable in its constructor
     * FutureTask implements Runnable
     * Therefore , we can sort the task in the PriorityBlockingQueue according to the Comparable implementation of the AdapterTask
     * @param task
     * @return AdapterTask
     */
    Future submit(Task task) {

        Adapter_Task b = new Adapter_Task(task);
        this.execute(b);

        return b;
    }


    Future submit(Callable callable, TaskType type) {
        return this.submit(Task.createTask(callable, type));
    }

    @Override
    public Future submit(Callable callable) {
        return this.submit(Task.createTask(callable));
    }

    public int getCurrentMax() {
       return 0;
    }


    /**@gracefullyTerminate
     * Make sure that all the workers have done and the queue is empty
     * Then shutdown the ThreadPoolExecutor
     */
    public void gracefullyTerminate() {
        while (this.getActiveCount() > 0 || this.getQueue().size() > 0) {
        }
        this.shutdown();
    }

    public static void main(String[] args) throws ExecutionException, InterruptedException{
        CustomExecutor custom = new CustomExecutor();
        int a = 2;
        for (int i = 0; i < 300; i++) {
            Task a2 = Task.createTask(()->{ int sum = 0; for (int h = 1; h <= 10; h++) { sum += h; } return sum; }, TaskType.COMPUTATIONAL);
            Task a3 = Task.createTask(()->{ int sum = 0; for (int h = 1; h <= 10; h++) { sum += h; } return sum; }, TaskType.IO);
            Task a4 = Task.createTask(()->{ String sum = "0"; for (int h = 1; h <= 10; h++) { sum += h; } return sum; }, TaskType.OTHER);
            Callable n = ()->{ int sum = 0; for (int h = 1; h <= 10; h++) { sum += h; } return sum; };
            Future t=  custom.submit(a2);
            custom.submit(a3);
            custom.submit(n);
            Future t5= custom.submit(a4);
            System.out.println(custom.getQueue().toString());
            System.out.println(t.get());
            System.out.println(t5.get());

            //System.out.println(custom.exec.getPoolSize());
        }
//        custom.shutdown();
        custom.gracefullyTerminate();


//    FutureTask<Integer>  a4 = new FutureTask<>(a2);
//    System.out.println(a4 instanceof Future<?>);
//    System.out.println();
    }
}