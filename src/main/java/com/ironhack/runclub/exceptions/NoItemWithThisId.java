package com.ironhack.runclub.exceptions;

public class NoItemWithThisId extends RuntimeException {
    public NoItemWithThisId(String message){
        super(message);
    }
}
