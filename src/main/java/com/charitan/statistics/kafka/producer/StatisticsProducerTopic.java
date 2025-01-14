package com.charitan.statistics.kafka.producer;

public enum StatisticsProducerTopic {
    DONOR_GET_DONATION("donor-donation-statistics"),
    TOP_DONOR_MONTH("donors-of-the-month"),

    CHARITY_GET_PROJECT("project.get-all-projects-by-charitan-id"),
    CHARITY_GET_PROJECT_BY_FILTER("project.get-all-projects-by-filter"),
    CHARITY_DONATION_TOTAL("charity.donation.total"),
    TOP_DONOR_MONTH_CHARITY("charity-donors-of-the-month"),

    PROJECT_COUNT("project.count"),
    DONATION_VALUE("donation.value"),

    CHARITY_DONATION_STATISTICS("charity-donation-statistics"),

    GET_NEW_USER("auth.get.new.users");

    // Private field for the enum value
    private final String topic;

    // Enum constructor to assign value to topic
    StatisticsProducerTopic(String topic) {
        this.topic = topic;
    }

    // Getter method to retrieve the topic
    public String getTopic() {
        return topic;
    }
    // public static final String CHARITIES_STATISTICS_ALL =
    // "charities.statistics.all";
}
