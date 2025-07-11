package com.example.spring_boot_base.exception;

public class OutOfStockException extends RuntimeException{
    public OutOfStockException(String message) {
        super(message);
    }
}