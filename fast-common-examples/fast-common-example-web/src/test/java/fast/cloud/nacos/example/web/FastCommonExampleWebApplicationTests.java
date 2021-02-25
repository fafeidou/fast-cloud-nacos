package fast.cloud.nacos.example.web;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@Slf4j
public class FastCommonExampleWebApplicationTests {

	@Test
	public void testSubmit() {
		ExecutorService executorService= Executors.newFixedThreadPool(1);

		executorService.submit(()->{
			int i=1/0;
		});


		executorService.submit(()->{
			System.out.println("当线程池抛出异常后继续新的任务");
		});

	}

	@Test
	public void testExecute() {
		ExecutorService executorService=Executors.newFixedThreadPool(1);

		executorService.execute(()->{
			int i=1/0;
		});

		executorService.execute(()->{
			System.out.println("当线程池抛出异常后继续新的任务");
		});

	}

	@Test
	public void testSetDefaultUncaughtExceptionHandler() {
		ExecutorService executorService=Executors.newFixedThreadPool(1);

		Thread thread=new Thread(()->{
			int i=1/0;
		});


		thread.setDefaultUncaughtExceptionHandler((Thread t, Throwable e)->{
			System.out.println("exceptionHandler"+e.getMessage());
		});

		executorService.execute(thread);
	}

	@Test
	public void testThreadFactoryExecute() {
		//1.实现一个自己的线程池工厂
		ThreadFactory factory = (Runnable r) -> {
			//创建一个线程
			Thread t = new Thread(r);
			//给创建的线程设置UncaughtExceptionHandler对象 里面实现异常的默认逻辑
			t.setDefaultUncaughtExceptionHandler((Thread thread1, Throwable e) -> {
				System.out.println("线程工厂设置的exceptionHandler" + e.getMessage());
			});
			return t;
		};

		//2.创建一个自己定义的线程池，使用自己定义的线程工厂
		ExecutorService service = new ThreadPoolExecutor(1, 1, 0, TimeUnit.MILLISECONDS,new LinkedBlockingQueue(10),factory);

		//3.提交任务
		service.execute(()->{
			int i=1/0;
		});

	}

	@Test
	public void testThreadFactorySubmit() {
		//1.实现一个自己的线程池工厂
		ThreadFactory factory = (Runnable r) -> {
			//创建一个线程
			Thread t = new Thread(r);
			//给创建的线程设置UncaughtExceptionHandler对象 里面实现异常的默认逻辑
			t.setDefaultUncaughtExceptionHandler((Thread thread1, Throwable e) -> {
				System.out.println("线程工厂设置的exceptionHandler" + e.getMessage());
			});
			return t;
		};

		//2.创建一个自己定义的线程池，使用自己定义的线程工厂
		ExecutorService service = new ThreadPoolExecutor(1, 1, 0, TimeUnit.MILLISECONDS,new LinkedBlockingQueue(10),factory);

		//3.提交任务
		service.submit(()->{
			int i=1/0;
		});

	}

	@Test
	public void testAfterExecute() {
		//1.创建一个自己定义的线程池,重写afterExecute方法
		ExecutorService service = new ThreadPoolExecutor(1, 1, 0, TimeUnit.MILLISECONDS,new LinkedBlockingQueue(10)){
			@Override
			protected void afterExecute(Runnable r, Throwable t) {
				super.afterExecute(r, t);
				System.out.println("afterExecute里面获取到异常信息"+t.getMessage());
			}
		};

		//2.提交任务
		service.execute(()->{
			int i=1/0;
		});

		service.execute(()->{
			System.out.println("当线程池抛出异常后继续新的任务");
		});
	}


	@Test
	public void testAfterExecuteSubmit() {
		//定义线程池
		ExecutorService service = new ThreadPoolExecutor(1, 1, 0, TimeUnit.MILLISECONDS, new LinkedBlockingQueue(10)) {

			//重写afterExecute方法
			@SneakyThrows
			@Override
			protected void afterExecute(Runnable r, Throwable t) {
				super.afterExecute(r, t);
				if (t != null) { //这个是excute提交的时候
					System.out.println("afterExecute里面获取到异常信息" + t.getMessage());
				}

				//如果r的实际类型是FutureTask 那么是submit提交的，所以可以在里面get到异常
				if (r instanceof FutureTask) {
					try {
						Future<?> future = (Future<?>) r;
						future.get();
					} catch (Exception e) {
						log.error("future里面取执行异常", e);
						throw e;
					}
				}
			}
		};

		//2.提交任务
		service.submit(() -> {
			int i = 1 / 0;
		});

		service.execute(()->{
			System.out.println("当线程池抛出异常后继续新的任务");
		});
	}
}
