package com.smartcontact.helpers;

public class ResourceNotFoundException extends RuntimeException{
    public ResourceNotFoundException(String mssg){
        super(mssg);
    }

    public ResourceNotFoundException(){
        super("Resource not found");
    }
}
