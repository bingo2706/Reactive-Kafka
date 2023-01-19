package com.tanthanh.accountservice.utils;


public class Constant {
    public static final String PAYMENT_REQUEST_TOPIC = "paymentRequest";
    public static final String PAYMENT_CREATED_TOPIC = "paymentCreated";
    public static final String PAYMENT_COMPLETED_TOPIC = "paymentCompleted";

    public static final String STATUS_PAYMENT_CREATING = "CREATING";
    public static final String STATUS_PAYMENT_REJECTED = "REJECTED";
    public static final String STATUS_PAYMENT_PROCESSING = "PROCESSING";
    public static final String STATUS_PAYMENT_SUCCESSFUL = "SUCCESSFUL";
}
