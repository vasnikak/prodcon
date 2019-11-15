/**
 * Copyright (C) 2019, by Vasileios Nikakis
 *
 * prodcon: a producer/consumer framework for Java
 */
package com.sitienda.concurrency.producerconsumer;

import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * The consumer class.
 *
 * @param <T> the type of the items that are being consumed.
 * 
 * @author Vasileios Nikakis
 */
class Consumer<T> extends Agent<T> {
    
    /**
     * The id of the consumer
     */
    private final int id;
    /**
     * The common store that contains the objects
     */
    private final Store<T> store;
    /**
     * The consumer's action
     */
    private final ConsumerAction<T> action;
    /**
     * The maximum number of objects that will be consumed
     */
    private final int maxObjectsNum;
    /**
     * Semaphore to control the consumers
     */
    private final Semaphore consumerSem;
    /**
     * Semaphore to control the producers
     */
    private final Semaphore producerSem;
    /**
     * Semaphore (mutex) to control the access to the store
     */
    private final Semaphore storeSem;
    /**
     * Shared counter between the consumers that counts the number of items
     * that have been consumed
     */
    private final AtomicInteger consumerCount;
    /**
     * Shared flag between the consumers
     */
    private final AtomicBoolean consumerFinishedFlag;

    /**
     * Constuctor.
     * 
     * @param id the consumer's id
     * @param store the common store
     * @param action the action
     * @param maxObjectsNum the maximum number of objects
     * @param consumerSem the consumer semaphore
     * @param producerSem the producer semaphore
     * @param storeSem the store mutex
     * @param consumerCount the shared counter between the consumers
     * @param consumerFinishedFlag  the shared flag between the consumers
     */
    public Consumer(int id, Store<T> store, ConsumerAction<T> action, 
                    int maxObjectsNum, Semaphore consumerSem, 
                    Semaphore producerSem, Semaphore storeSem, 
                    AtomicInteger consumerCount, 
                    AtomicBoolean consumerFinishedFlag) {
        this.id = id;
        this.store = store;
        this.action = action;
        this.maxObjectsNum = maxObjectsNum;
        this.consumerSem = consumerSem;
        this.producerSem = producerSem;
        this.storeSem = storeSem;
        this.consumerCount = consumerCount;
        this.consumerFinishedFlag = consumerFinishedFlag;
    }
    
    @Override
    public void run() {
        try {
            while (true) {
                // Check consumerFinishedFlag
                if (consumerFinishedFlag.get()) {
                    consumerSem.release();
                    return;
                }

                consumerSem.acquire();
                storeSem.acquire();
                // Get the item
                T item = store.poll();
                if (item != null) {
                    consumerCount.incrementAndGet();
                    producerSem.release();
                }
                storeSem.release();
                if (consumerCount.intValue() == maxObjectsNum)
                    consumerFinishedFlag.set(true);
                // Consume the item
                action.consume(item);
            }
        } catch (InterruptedException e) {
            System.out.println(e);
            System.exit(1);
        }
    }
    
}
