package com.pan.chap8Middleware.disruptor;

import com.lmax.disruptor.EventHandler;

public class ApplicationConsumer implements EventHandler<LongEvent> {
    @Override
    public void onEvent(LongEvent longEvent, long l, boolean b) throws Exception {
        System.out.println(Thread.currentThread().getName() + " Application Event: " + longEvent.getValue());
    }
}
