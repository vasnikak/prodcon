/**
 * Copyright (C) 2019, by Vasileios Nikakis
 *
 * prodcon: a producer/consumer framework for Java
 */
package com.sitienda.concurrency.producerconsumer;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for class ProdConController.
 * 
 * @author Vasileios Nikakis
 */
public class ProdConControllerTest {
    
    public ProdConControllerTest() {
        
    }

    private static final String CHARS = "123456789abcdefghijklmnopqrstuvwxyz";
    
    private static int getRandom(int min, int max) {
        return min + (int)(Math.random() * ((max - min) + 1));
    }
    
    private static String getRandomStr(int length) { 
        String str = "";
        for (int i = 0; i < length; i++)
            str += CHARS.charAt(getRandom(0,CHARS.length()-1));
        return str;
    }
    
    /**
     * Test of exec method, of class ProdConController.
     */
    @Test
    public void testExec() {
        int producerNum = 300;
        int consumerNum = 5;
        int maxObjectsNum = 10000;
        int maxStoreSize = 50;
        ProdConController<String> prodConController = new ProdConController<>(
                new ProducerAction<String>() { 
                    @Override
                    public String produce() {
                        return getRandomStr(5);
                    }
                },
                new ConsumerAction<String>() { 
                    @Override
                    public void consume(String obj) {
                        
                    }
                },
                producerNum,
                consumerNum, 
                maxObjectsNum,
                maxStoreSize
        );
        prodConController.exec();
    }
    
}
