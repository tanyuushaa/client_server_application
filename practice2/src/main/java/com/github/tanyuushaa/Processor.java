package com.github.tanyuushaa;

// обробка повідомлень
public interface Processor {
    Message process(Message message);
}
