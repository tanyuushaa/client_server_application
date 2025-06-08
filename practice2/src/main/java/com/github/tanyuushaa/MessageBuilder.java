package com.github.tanyuushaa;

// будова message
public final class MessageBuilder {
    int type;
    int userId;
    String message;

    public MessageBuilder type(int type) {
        this.type = type;
        return this;
    }

    public MessageBuilder userId(int userId) {
        this.userId = userId;
        return this;
    }

    public MessageBuilder message(String message) {
        this.message = message;
        return this;
    }

    public Message build() {
        return new Message(this);
    }
}
