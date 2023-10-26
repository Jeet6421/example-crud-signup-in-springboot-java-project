package com.example.example.exceptions;

public class ResourceNotFound extends RuntimeException{
    public  ResourceNotFound(long id){
        super("Resource not available for id :" +id);
    }
}
