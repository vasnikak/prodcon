/**
 * Copyright (C) 2019, by Vasileios Nikakis
 *
 * prodcon: a producer/consumer framework for Java
 */
package com.sitienda.concurrency.producerconsumer;

/**
 * The interface for a producer's action.
 *
 * @param <T> the type of the objects that is produced.
 *
 * @author Vasileios Nikakis
 */
public interface ProducerAction<T> extends AgentAction<T> {
    
    /**
     * Produces and returns an item.
     * 
     * @return the produced item
     */
    public T produce();
    
}
