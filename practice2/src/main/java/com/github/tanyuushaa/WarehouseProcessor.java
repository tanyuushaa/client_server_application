package com.github.tanyuushaa;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

// реалізація команд (обробка повідомлень)
public class WarehouseProcessor implements Processor {
    private final Map<String, Integer> stock = new ConcurrentHashMap<>(); // назва - кількість
    private final Map<String, Double> prices = new ConcurrentHashMap<>(); // назва - ціна
    private final Map<String, List<String>> group = new ConcurrentHashMap<>(); // група - товари

    @Override
    public Message process(Message message) {
        int type = message.getType();
        String[] parts = message.getMessage().split(" ");
        String response = " ";

        switch (Command.values()[type]) {
            // зарахувати товар
            case ADD_STOCK: {
                String name = parts[0];
                int amount = Integer.parseInt(parts[1]);
                stock.merge(name, amount, (a, b) -> a + b);
                //response = "OK" + amount + " " + name;
                response = "OK";
                break;
            }
            // списати кількість товару
            case WRITE_OFF: {
                String name = parts[0];
                int amount = Integer.parseInt(parts[1]);
                stock.merge(name, -amount, (a, b) -> a + b);
                //response = "OK" + amount + " " + name;
                response = "OK";
                break;
            }
            //кількість товару
            case GET_QUANTITY: {
                String name = parts[0];
                int quantity = stock.getOrDefault(name, 0);
                //response = "OK" + name + " " + quantity;
                response = "OK";
                break;
            }
            //додати групу товару
            case ADD_GROUP: {
                String groupName = parts[0];
                group.putIfAbsent(groupName, new ArrayList<>());
                //response = "OK" + groupName;
                response = "OK";
                break;
            }
            //ціна на товар
            case SET_PRICE: {
                String name = parts[0];
                double price = Double.parseDouble(parts[1]);
                prices.put(name, price);
                //response = "OK" + name + " " + price;
                response = "OK";
                break;
            }
        }

        return new MessageBuilder()
                .type(type)
                .userId(message.getUserId())
                .message(response)
                .build();
    }
}
