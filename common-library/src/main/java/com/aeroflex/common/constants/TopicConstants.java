package com.aeroflex.common.constants;

public final class TopicConstants {

    private TopicConstants() {}

    public static final String NOTIFICATION_DELIVERY_TOPIC = "notification-delivery";
    public static final String NOTIFICATION_EVENT_TOPIC = "notification-event";
    public static final String BUSINESS_EVENT_TOPIC = "business-events";

    // Saga and Inter-Service Event Topics
    public static final String BOOKING_SAGA_TOPIC = "booking-saga-events";
    public static final String SEAT_EVENTS_TOPIC = "seat-events";
    public static final String PAYMENT_EVENTS_TOPIC = "payment-events";
    public static final String FLIGHT_EVENTS_TOPIC = "flight-events";
}
