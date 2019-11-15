/**
 * Copyright (C) 2019, by Vasileios Nikakis
 *
 * prodcon: a producer/consumer framework for Java
 */
package com.sitienda.concurrency.producerconsumer;

import java.util.ArrayList;


/**
 * The store class that is used for the communication between the producers and 
 * the consumers.
 *
 * @param <T> the type of items that the store will hold
 * 
 * @author Vasileios Nikakis
 */
class Store<T> {
    
    /**
     * The actual store.
     */
    private final ArrayList<T> store;
    
    /**
     * Constructor
     */
    public Store() { 
        store = new ArrayList<>();
    }
    
    /**
     * Inserts an item in the store.
     * 
     * @param obj the item to be inserted
     */
    public void addItem(T obj) { 
        store.add(obj);
    }
    
    /**
     * Returns the number of items inside  the store.
     * 
     * @return the number of items of the store
     */
    public int size() { 
        return store.size();
    }
    
    /**
     * Checks if the store is empty.
     * 
     * @return true or false according to if the store is empty
     */
    public boolean isEmpty() { 
        return store.isEmpty();
    }
    
    /**
     * Deletes and returns the first item in the store.
     * <p>If the store is empty, it returns null.</p>
     * 
     * @return the first item in the store (null if the store is empty).
     */
    public T poll() { 
        if (isEmpty())
            return null;
        T obj = store.get(0);
        store.remove(0);
        return obj;
    }
    
}
