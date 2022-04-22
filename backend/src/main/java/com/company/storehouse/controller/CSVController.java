package com.company.storehouse.controller;

import com.fasterxml.jackson.annotation.JsonView;
import com.company.storehouse.controller.response.View;
import com.company.storehouse.model.Waybill;
import com.company.storehouse.service.WaybillService;
import com.company.storehouse.validation.CSVMatching;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RestController
@RequestMapping("/api/v1")
@Validated
@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class CSVController {

    private final WaybillService waybillService;

    @ApiOperation(value = "Upload a waybill from CSV for import products", notes = "Please, use token.")
    @PreAuthorize("hasAuthority('APPROVE_ON_IMPORT_EXPORT')")
    @PostMapping("/waybills/upload")
    @JsonView(View.Waybill.class)
    public ResponseEntity<Waybill> uploadWaybillFromCSV(@RequestBody @CSVMatching MultipartFile csvFile) {
        try {
            return ResponseEntity.ok(waybillService.uploadFromCSV(csvFile));
        } catch (IOException e) {
            log.error(e.getMessage());
        }
        return ResponseEntity.badRequest().build();
    }

    @ApiOperation(value = "Download the waybill to CSV file", notes = "Please, use token.")
    @GetMapping(value = "/waybills/download/{waybillId}", produces = "text/csv")
    @JsonView(View.Waybill.class)
    public void downloadWaybillInCSV(@PathVariable Long waybillId, HttpServletResponse response) {
        try {
            waybillService.downloadInCSV(waybillId, response);
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

}
