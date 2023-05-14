package ru.soloviev.Errors;

import org.springframework.http.HttpStatus;


public class ApiError {
    protected HttpStatus status;

    protected String error;

    public ApiError(HttpStatus status, String error){
        this.status = status;
        this.error = error;
    }

    public String getError(){
        return error;
    }

    public int getStatus(){
        return status.value();
    }
}
