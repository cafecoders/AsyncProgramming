package com.pan.chap8Middleware.disruptor;

import com.lmax.disruptor.EventHandler;

public class ReplicationConsumer implements EventHandler<LongEvent> {
    @Override
    public void onEvent(LongEvent longEvent, long sequence, boolean endOfBatch) throws Exception {
        System.out.println(Thread.currentThread().getName() + " Replication Event: " + longEvent.getValue());
    }
}
