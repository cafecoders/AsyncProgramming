package com.pan.chap8Middleware.disruptor;

import com.lmax.disruptor.BlockingWaitStrategy;
import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.ProducerType;
import com.lmax.disruptor.util.DaemonThreadFactory;

public class LongEventMain {
    public static void main(String[] args) throws InterruptedException {
        //创建RingBuffer中事件元素的工厂对象
        LongEventFactory factory = new LongEventFactory();

        //指定Ring Buffer的大小，必须为2的幂次方
        int bufferSize = 1024;

        //构造Disruptor
        Disruptor<LongEvent> disruptor = new Disruptor<LongEvent>(factory,
                                    bufferSize, DaemonThreadFactory.INSTANCE,
                                    ProducerType.SINGLE, new BlockingWaitStrategy());

        //注册消费者
        disruptor.handleEventsWith(new JournalConsumer(), new ReplicationConsumer())
                .then(new ApplicationConsumer());

        //启动disruptor, 启动线程运行
        disruptor.start();

        //从disruptor中获取ring buffer
        RingBuffer<LongEvent> ringBuffer = disruptor.getRingBuffer();

        //创建生产者
        LongEventProducer producer = new LongEventProducer(ringBuffer);

        //生产元素，放入ring buffer
        for(int i = 0; i < 100; i++) {
            producer.onData(i);

            Thread.sleep(1000);
        }

        Thread.currentThread().join();
    }
}
