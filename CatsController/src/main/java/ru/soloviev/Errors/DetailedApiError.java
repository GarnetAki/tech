package ru.soloviev.Errors;

import org.springframework.http.HttpStatus;

import java.util.List;

public class DetailedApiError {

    protected HttpStatus status;

    protected String error;

    protected List<String> errorDetails;

    public DetailedApiError(HttpStatus status, String error, List<String> errorDetails){
        this.status = status;
        this.error = error;
        this.errorDetails = errorDetails;
    }

    public String getError(){
        return error;
    }

    public List<String> getErrorDetails(){
        return errorDetails;
    }

    public int getStatus(){
        return status.value();
    }
}
