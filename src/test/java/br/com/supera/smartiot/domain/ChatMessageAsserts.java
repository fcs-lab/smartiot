package br.com.supera.smartiot.domain;

import static org.assertj.core.api.Assertions.assertThat;

public class ChatMessageAsserts {

    /**
     * Asserts that the entity has all properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertChatMessageAllPropertiesEquals(ChatMessage expected, ChatMessage actual) {
        assertChatMessageAutoGeneratedPropertiesEquals(expected, actual);
        assertChatMessageAllUpdatablePropertiesEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all updatable properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertChatMessageAllUpdatablePropertiesEquals(ChatMessage expected, ChatMessage actual) {
        assertChatMessageUpdatableFieldsEquals(expected, actual);
        assertChatMessageUpdatableRelationshipsEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all the auto generated properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertChatMessageAutoGeneratedPropertiesEquals(ChatMessage expected, ChatMessage actual) {
        assertThat(expected)
            .as("Verify ChatMessage auto generated properties")
            .satisfies(e -> assertThat(e.getId()).as("check id").isEqualTo(actual.getId()));
    }

    /**
     * Asserts that the entity has all the updatable fields set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertChatMessageUpdatableFieldsEquals(ChatMessage expected, ChatMessage actual) {
        assertThat(expected)
            .as("Verify ChatMessage relevant properties")
            .satisfies(e -> assertThat(e.getMessageId()).as("check messageId").isEqualTo(actual.getMessageId()))
            .satisfies(e -> assertThat(e.getMessageContent()).as("check messageContent").isEqualTo(actual.getMessageContent()))
            .satisfies(e -> assertThat(e.getMessageTimestamp()).as("check messageTimestamp").isEqualTo(actual.getMessageTimestamp()));
    }

    /**
     * Asserts that the entity has all the updatable relationships set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertChatMessageUpdatableRelationshipsEquals(ChatMessage expected, ChatMessage actual) {
        assertThat(expected)
            .as("Verify ChatMessage relationships")
            .satisfies(e -> assertThat(e.getSender()).as("check sender").isEqualTo(actual.getSender()))
            .satisfies(e -> assertThat(e.getChatSession()).as("check chatSession").isEqualTo(actual.getChatSession()));
    }
}