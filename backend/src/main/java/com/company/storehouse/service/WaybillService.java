package com.company.storehouse.service;

import com.company.storehouse.model.Waybill;
import com.company.storehouse.model.enumeration.ProcessingStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public interface WaybillService {

    Waybill createWaybillForImport();

    Waybill createWaybillForExport();

    Waybill approveOrRejectWaybill(Long id, ProcessingStatus status);

    Waybill getById(Long id);

    Waybill uploadFromCSV(MultipartFile csvFile) throws IOException;

    void downloadInCSV(Long waybillId, HttpServletResponse response) throws IOException;

    Page<Waybill> getAllWaybills(Pageable pageable);

    Page<Waybill> getWaybillsForConfirmationImport(Pageable pageable);

    Page<Waybill> getWaybillsForConfirmationExport(Pageable pageable);

    Page<Waybill> getWaybillsForConfirmationAutoExport(Pageable pageable);

    Page<Waybill> getRejectedWaybills(Pageable pageable);

    Page<Waybill> getPendingWaybillsOfCurrentUser(Pageable pageable);

    Page<Waybill> getCompletedWaybillsOfCurrentUser(Pageable pageable);

    Page<Waybill> getRejectedWaybillsOfCurrentUser(Pageable pageable);

}
