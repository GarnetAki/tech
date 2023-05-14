package ru.soloviev.Exchanges;

import lombok.Data;

public @Data class StringResponse {

    private String message;

    public StringResponse(String string){
        message = string;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}
