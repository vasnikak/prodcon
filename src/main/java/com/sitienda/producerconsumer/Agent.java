/**
 * Copyright (C) 2019, by Vasileios Nikakis
 *
 * prodcon: a producer/consumer framework for Java
 */
package com.sitienda.producerconsumer;

/**
 * Abstract class, that will be inherited by the producers and the consumers.
 * 
 * @param <T> the type of items that are being produced/consumed.
 * 
 * @author Vasileios Nikakis
 */
abstract class Agent<T> extends Thread {
    
}
