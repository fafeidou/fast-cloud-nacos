package com.fast.boot.servlet.demo;

import javax.servlet.AsyncContext;
import javax.servlet.ReadListener;
import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@WebServlet(value = "/NonBlockingServlet", asyncSupported = true)
public class NonBlockingServlet extends HttpServlet {
    private static ThreadPoolExecutor executor = new ThreadPoolExecutor(100, 200, 50000L, TimeUnit.MILLISECONDS, new ArrayBlockingQueue<>(100));

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        AsyncContext asyncContext = request.startAsync();
        long t1 = System.currentTimeMillis();
        ServletInputStream inputStream = request.getInputStream();
        //为ServletInputStream添加了一个ReadListener
        inputStream.setReadListener(new ReadListener() {
            @Override
            public void onDataAvailable() {

            }

            @Override
            public void onAllDataRead() {
                executor.execute(() -> {
                    // 模拟耗时操作
                    try {
                        TimeUnit.SECONDS.sleep(5);
                    } catch (InterruptedException e) {
                    }

                    try {
                        asyncContext.getResponse().getWriter().write("hello world!");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    asyncContext.complete();
                });
            }

            @Override
            public void onError(Throwable t) {
                asyncContext.complete();
            }
        });
        System.out.println("async use:" + (System.currentTimeMillis() - t1));

    }
}

