package com.jm.online_store.service.interf;

public interface TelegramBotService {
    String getActualStocks();

    String getRepairOrder(String orderNumber);

    String getHelloMessage();

    String getDefaultMessage();

    String getHelpMessage();
}