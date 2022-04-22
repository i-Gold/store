package com.company.storehouse.schedule;

import com.company.storehouse.model.Product;
import com.company.storehouse.model.Waybill;
import com.company.storehouse.model.enumeration.ProcessingStatus;
import com.company.storehouse.model.enumeration.WaybillType;
import com.company.storehouse.repository.ProductRepository;
import com.company.storehouse.repository.WaybillRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
@Transactional
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class CheckExpiredProducts {

    private final ProductRepository productRepository;
    private final WaybillRepository waybillRepository;

    /**
     * Every midnight, the expired products are checked at the stock
     * and the auto-output waybill for expired products is formed.
     *
     * This waybill must be confirmed or rejected by manager with confirmation authority.
     */
    @Scheduled(cron = "0 0 0 * * ?")
    public void createWaybillForExpiredProducts() {
        List<Product> expiredProducts = productRepository.getExpiredProducts();
        if (!expiredProducts.isEmpty()) {
            expiredProducts.forEach(product -> {
                product.setForExport(Boolean.TRUE);
                product.setIsPendingExport(Boolean.TRUE);
            });
            Waybill autoExportWaybill = new Waybill();
            autoExportWaybill.setStatus(ProcessingStatus.PENDING);
            autoExportWaybill.setType(WaybillType.AUTO_OUTPUT);
            autoExportWaybill.setProducts(expiredProducts);
            waybillRepository.save(autoExportWaybill);
        }
    }

}
