/**
 * Copyright (C) 2019, by Vasileios Nikakis
 *
 * prodcon: a producer/consumer framework for Java
 */
package com.sitienda.concurrency.producerconsumer;

/**
 * The interface for a consumer's action.
 *
 * @param <T> the type of the objects that is consumed.
 *
 * @author Vasileios Nikakis
 */
public interface ConsumerAction<T> extends AgentAction<T> {
    
    /**
     * Consumes an item.
     * 
     * @param obj the item to be consumed.
     */
    public void consume(T obj);
    
}
