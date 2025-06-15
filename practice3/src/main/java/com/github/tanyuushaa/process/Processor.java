package com.github.tanyuushaa.process;

import com.github.tanyuushaa.core.Message;

// обробка повідомлень
public interface Processor {
    Message process(Message message);
}
