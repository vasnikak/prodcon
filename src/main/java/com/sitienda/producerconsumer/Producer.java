/**
 * Copyright (C) 2019, by Vasileios Nikakis
 *
 * prodcon: a producer/consumer framework for Java
 */
package com.sitienda.producerconsumer;

import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * The producer class.
 *
 * @param <T> the type of the items that are being produced.
 * 
 * @author Vasileios Nikakis
 */
class Producer<T> extends Agent<T> {
    
    /**
     * The id of the producer
     */
    private final int id;
    /**
     * The common store that contains the objects
     */
    private final Store<T> store;
    /**
     * The producer's action
     */
    private final ProducerAction<T> action;
    /**
     * The maximum number of objects that will be produced
     */
    private final int maxObjectsNum;
    /**
     * Semaphore to control the producers
     */
    private final Semaphore producerSem;
    /**
     * Semaphore to control the consumers
     */
    private final Semaphore consumerSem;
    /**
     * Semaphore (mutex) to control the access to the store
     */
    private final Semaphore storeSem;
    /**
     * Shared counter between the producers that counts the number of items
     * that have been produced
     */
    private final AtomicInteger producerCount;
    /**
     * Shared flag between the producers
     */
    private final AtomicBoolean producerFinishedFlag;

    /**
     * Constructor
     * 
     * @param id the producer's id
     * @param store the common store
     * @param action the action
     * @param maxObjectsNum the maximum number of objects
     * @param producerSem the producer semaphore
     * @param consumerSem the consumer semaphore
     * @param storeSem the store mutex
     * @param producerCount the shared counter between the producers
     * @param producerFinishedFlag the shared flag between the producers
     */
    public Producer(int id, Store<T> store, ProducerAction action, 
                    int maxObjectsNum, Semaphore producerSem, 
                    Semaphore consumerSem, Semaphore storeSem, 
                    AtomicInteger producerCount, 
                    AtomicBoolean producerFinishedFlag) {
        this.id = id;
        this.store = store;
        this.action = action;
        this.maxObjectsNum = maxObjectsNum;
        this.producerSem = producerSem;
        this.consumerSem = consumerSem;
        this.storeSem = storeSem;
        this.producerCount = producerCount;
        this.producerFinishedFlag = producerFinishedFlag;
    }
    
    @Override
    public void run() {
        try {
            while (true) {
                // Check producerFinishedFlag
                if (producerFinishedFlag.get()) {
                    producerSem.release();
                    return;
                }

                // Produce the item
                T item = action.produce();
                producerSem.acquire();
                storeSem.acquire();
                if (producerCount.intValue() == maxObjectsNum)
                    producerFinishedFlag.set(true);
                else {
                    store.addItem(item);
                    producerCount.incrementAndGet();
                    consumerSem.release();
                }
                storeSem.release();
            }
        } catch (InterruptedException e) {
            System.out.println(e);
        }
    }
    
}
