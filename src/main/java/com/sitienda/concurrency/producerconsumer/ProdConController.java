/**
 * Copyright (C) 2019, by Vasileios Nikakis
 *
 * prodcon: a producer/consumer framework for Java
 */
package com.sitienda.concurrency.producerconsumer;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * The controller class that dispatches the producer and the consumer threads.
 *
 * @param <T> the type of the items that are being consumed.
 * 
 * @author Vasileios Nikakis
 */
public class ProdConController<T> {
    
    /**
     * The producer's action
     */
    private final ProducerAction<T> producerAction;
    /**
     * The consumer's action
     */
    private final ConsumerAction<T> consumerAction;
    /**
     * The number of producers
     */
    private final int producerNum;
    /**
     * The number of consumers
     */
    private final int consumerNum;
    /**
     * The maximum number of total objects that will be produced/consumed
     */
    private final int maxObjectsNum;
    /**
     * The maximum number of objects inside the store.
     */
    private final int maxStoreSize;

    /**
     * Constructor.
     * 
     * @param producerAction the producer's action
     * @param consumerAction the consumer's action
     * @param producerNum the number of producers
     * @param consumerNum the number of consumers
     * @param maxObjectsNum the maximum number of total objects that will be 
     *                      produced/consumed
     * @param maxStoreSize the maximum number of objects inside the store
     */
    public ProdConController(ProducerAction<T> producerAction, 
                             ConsumerAction<T> consumerAction, 
                             int producerNum, int consumerNum, 
                             int maxObjectsNum, int maxStoreSize) {
        this.producerAction = producerAction;
        this.consumerAction = consumerAction;
        this.producerNum = producerNum;
        this.consumerNum = consumerNum;
        this.maxObjectsNum = maxObjectsNum;
        this.maxStoreSize = maxStoreSize;
        // Input check
        if (producerAction == null)
            throw new IllegalArgumentException("producerAction cannot be null");
        if (consumerAction == null)
            throw new IllegalArgumentException("consumerAction cannot be null");
        if (producerNum < 1)
            throw new IllegalArgumentException("producerNum has to be a positive integer");
        if (consumerNum < 1)
            throw new IllegalArgumentException("consumerNum has to be a positive integer");
        if (maxObjectsNum < 1)
            throw new IllegalArgumentException("maxObjectsNum has to be a positive integer");
        if (maxStoreSize < 1)
            throw new IllegalArgumentException("maxStoreSize has to be a positive integer");
    }
    
    public void exec() { 
        // Create the shared counters
        AtomicInteger producerCount = new AtomicInteger(0);
        AtomicInteger consumerCount = new AtomicInteger(0);
        AtomicBoolean producerFinishedFlag = new AtomicBoolean(false);
        AtomicBoolean consumerFinishedFlag = new AtomicBoolean(false);
        // Create the necessary semaphores
        Semaphore producerSem = new Semaphore(maxStoreSize);
        Semaphore consumerSem = new Semaphore(0);
        Semaphore storeSem = new Semaphore(1);
        // Create the store
        Store<T> store = new Store<>();
        // Create the threads
        List<Agent> agents = new ArrayList<>();
        for (int i = 0; i < producerNum; i++) { 
             agents.add(new Producer(
                            i+1, 
                            store, 
                            producerAction, 
                            maxObjectsNum,
                            producerSem, 
                            consumerSem,
                            storeSem, 
                            producerCount, 
                            producerFinishedFlag));
        }
        for (int i = 0; i < consumerNum; i++) { 
             agents.add(new Consumer(
                            i+1,
                            store,
                            consumerAction,
                            maxObjectsNum,
                            consumerSem, 
                            producerSem,
                            storeSem, 
                            consumerCount, 
                            consumerFinishedFlag));
        }
        // Start the threads
        for (Agent agent : agents)
             agent.start();
        
        try { 
            for (Agent agent : agents)
                agent.join();
        }
        catch (InterruptedException e) { 
            System.out.println(e);
        }
    }
    
}
