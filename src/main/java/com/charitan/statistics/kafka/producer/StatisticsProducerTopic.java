package com.charitan.statistics.kafka.producer;

public enum StatisticsProducerTopic {
    DONOR_GET_DONATION("donor-donation-statistics"),

    CHARITY_GET_PROJECT("project.get-all-projects-by-charitan-id"),
    CHARITY_DONATION_TOTAL("charity.donation.total"),

    PROJECT_COUNT("project.count"),
    DONATION_VALUE("donation.value");

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
//    public static final String CHARITIES_STATISTICS_ALL = "charities.statistics.all";
}
