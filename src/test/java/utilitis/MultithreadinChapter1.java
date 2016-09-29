package utilitis;

import java.util.Random;
import java.util.concurrent.*;

/**
 * Created by wereckip on 16.09.2016.
 */
public class MultithreadinChapter1 {

    public static void main(String[]args){
        MultithreadinChapter1 chapter1 = new MultithreadinChapter1();
        chapter1.test1();
    }

    public void test1(){
        Callable<Integer>task = () -> {
            int result = 0;

            while(result < 50){
                result += new Random().nextInt(15);

                if(Thread.currentThread().isInterrupted()) throw new InterruptedException("Przerwano watek!");

                if(result % 11 == 0) throw new RuntimeException("Nie oczekiwany random :) " + result);

                try{
                    System.out.println(result);
                    Thread.sleep(1000);
                }catch (InterruptedException e){
                    e.printStackTrace();
                }
            }
            return result;
        };

        ExecutorService executorService = Executors.newCachedThreadPool();
        Future<Integer> result = executorService.submit(task);
        System.out.println("After task but before future.get()");
        try {
            System.out.println("Result = " + result.get());
            System.out.println("After future.get()");
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        executorService.shutdown();
        System.out.println(executorService.isShutdown() + " , " + executorService.isTerminated());
    }

    public void test2(){

    }
}
