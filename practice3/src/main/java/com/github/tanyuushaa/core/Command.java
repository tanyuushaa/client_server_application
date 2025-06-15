package com.github.tanyuushaa.core;

// команди
public enum Command {
    GET_QUANTITY, // взнати кількість товару на складі
    WRITE_OFF, // списати певну кількість товару
    ADD_STOCK, // зарахувати певну кількість товару
    ADD_GROUP, // додати групу товарів
    ADD_PRODUCT_TO_GROUP, // додати назву товару до групи
    SET_PRICE // встановити ціну на конкретний товар
}
