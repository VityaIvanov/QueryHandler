package com.kpi.ivanov;

import java.time.Duration;
import java.time.LocalDate;

/**
 * Response of customers support.
 */
final class ResponseEntry {
    private final Service service;
    private final Question question;
    private final ResponseType responseType;
    private final LocalDate date;
    private final Duration responseTime;

    private ResponseEntry(Builder builder) {
        this.service = builder.service;
        this.question = builder.question;
        this.responseType = builder.responseType;
        this.date = builder.date;
        this.responseTime = builder.responseTime;
    }

    Service getService() {
        return service;
    }

    Question getQuestion() {
        return question;
    }

    ResponseType getResponseType() {
        return responseType;
    }

    LocalDate getDate() {
        return date;
    }

    Duration getResponseTime() {
        return responseTime;
    }

    /**
     * Builder for ResponseEntry.
     */
    final static class Builder {
        private Service service;
        private Question question;
        private ResponseType responseType;
        private LocalDate date;
        private Duration responseTime;

        Builder setService(Service service) {
            this.service = service;
            return this;
        }

        Builder setQuestion(Question question) {
            this.question = question;
            return this;
        }

        Builder setResponseType(ResponseType responseType) {
            this.responseType = responseType;
            return this;
        }

        Builder setDate(LocalDate date) {
            this.date = date;
            return this;
        }

        Builder setDuration(Duration responseTime) {
            this.responseTime = responseTime;
            return this;
        }

        ResponseEntry build() {
            return new ResponseEntry(this);
        }
    }
}
