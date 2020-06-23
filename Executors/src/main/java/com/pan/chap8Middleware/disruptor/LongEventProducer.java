package com.pan.chap8Middleware.disruptor;

import com.lmax.disruptor.RingBuffer;

public class LongEventProducer {
    private final RingBuffer<LongEvent> ringBuffer;

    public LongEventProducer(RingBuffer<LongEvent> ringBuffer) {
        this.ringBuffer = ringBuffer;
    }

    public void onData(long bb) {
        //第一阶段，获取序列号
        long sequence = ringBuffer.next();
        try {
            //获取序列号对应的元素
            LongEvent event = ringBuffer.get(sequence);
            //修改元素的值
            event.setValue(bb);
        } finally {
            //发布元素
            ringBuffer.publish(sequence);
        }
    }
}
