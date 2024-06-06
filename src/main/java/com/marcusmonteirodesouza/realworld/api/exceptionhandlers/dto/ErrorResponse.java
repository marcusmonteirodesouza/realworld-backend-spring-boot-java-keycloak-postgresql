package com.marcusmonteirodesouza.realworld.api.exceptionhandlers.dto;

public class ErrorResponse {
    private ErrorResponseErrors errors;

    public ErrorResponse(String[] errors) {
        this.errors = new ErrorResponseErrors(errors);
    }

    public ErrorResponseErrors getErrors() {
        return errors;
    }

    public class ErrorResponseErrors {
        private String[] body;

        public ErrorResponseErrors(String[] errors) {
            this.body = errors;
        }

        public String[] getBody() {
            return body;
        }
    }
}
