package com.company.storehouse.util;

import com.company.storehouse.controller.exception.FreeSlotsLimitException;
import com.company.storehouse.repository.ProductRepository;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public final class FreeSlotsUtil {

    /**
     * The utility allows to check if free slots for import are available
     *
     * @param amountOfProductsForImport amount of new products for import
     * @param productRepository         repository for save data
     * @param maxSlotsInStorage         maximum capacity of the storage
     */
    public static void check(int amountOfProductsForImport, ProductRepository productRepository, String maxSlotsInStorage) {
        final long amountOfProducts = productRepository.countAllProductsInStock().longValue();
        final long freeSlots = Long.parseLong(maxSlotsInStorage) - amountOfProducts;
        if (amountOfProductsForImport > freeSlots) {
            final String message = freeSlots + " free slots for products left in stock!\nYou have exceeded the limit";
            log.error(message);
            throw new FreeSlotsLimitException(message);
        }
    }

}
