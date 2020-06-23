package com.pan.chap8Middleware.disruptor;

import com.lmax.disruptor.EventHandler;

/**
 * 将输入数据写入持久性日志文件的消费者
 */
public class JournalConsumer implements EventHandler<LongEvent> {

    @Override
    public void onEvent(LongEvent longEvent, long sequence, boolean endOfBatch) throws Exception {
        System.out.println(Thread.currentThread().getName() + " Persist Event: " + longEvent.getValue());
    }
}
