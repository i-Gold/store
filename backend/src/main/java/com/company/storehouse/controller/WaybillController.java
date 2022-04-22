package com.company.storehouse.controller;

import com.fasterxml.jackson.annotation.JsonView;
import com.company.storehouse.controller.response.View;
import com.company.storehouse.controller.response.pagination.ApiPageable;
import com.company.storehouse.controller.response.pagination.PageableResponse;
import com.company.storehouse.model.Waybill;
import com.company.storehouse.model.enumeration.ProcessingStatus;
import com.company.storehouse.service.WaybillService;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
@Slf4j
public class WaybillController {

    private final WaybillService waybillService;

    @Autowired
    public WaybillController(WaybillService waybillService) {
        this.waybillService = waybillService;
    }

    @ApiOperation(value = "Create a waybill for import", notes = "Please, use token.")
    @PostMapping("/waybills/for-import")
    @JsonView(View.Waybill.class)
    public ResponseEntity<Waybill> createWaybillForImport() {
        return ResponseEntity.ok(waybillService.createWaybillForImport());
    }

    @ApiOperation(value = "Create a waybill for export", notes = "Please, use token.")
    @PostMapping("/waybills/for-export")
    @JsonView(View.Waybill.class)
    public ResponseEntity<Waybill> createWaybillForExport() {
        return ResponseEntity.ok(waybillService.createWaybillForExport());
    }

    @ApiOperation(value = "Approve or Reject the waybill ***", notes = "Please, use token.")
    @PreAuthorize("hasAuthority('IMPORT_EXPORT')")
    @PutMapping("/waybills/{id}")
    @JsonView(View.Waybill.class)
    public ResponseEntity<Waybill> approveWaybill(@PathVariable Long id, @RequestParam ProcessingStatus status) {
        return ResponseEntity.ok(waybillService.approveOrRejectWaybill(id, status));
    }

    @ApiOperation(value = "Get the waybill by ID", notes = "Please, use token.")
    @GetMapping("/waybills/{id}")
    @JsonView(View.Waybill.class)
    public ResponseEntity<Waybill> getById(@PathVariable Long id) {
        return ResponseEntity.ok(waybillService.getById(id));
    }

    @ApiOperation(value = "Get all waybills ***", notes = "Please, use token.")
    @ApiPageable
    @PreAuthorize("hasAuthority('IMPORT_EXPORT')")
    @GetMapping("/waybills/completed")
    @JsonView(View.Waybill.class)
    public ResponseEntity<PageableResponse<Waybill>> getAllWaybills(Pageable pageable) {
        return ResponseEntity.ok(new PageableResponse<>(waybillService.getAllWaybills(pageable)));
    }

    @ApiOperation(value = "Get all waybills pending import confirmation ***", notes = "Please, use token.")
    @ApiPageable
    @PreAuthorize("hasAuthority('IMPORT_EXPORT')")
    @GetMapping("/waybills/pending-import")
    @JsonView(View.Waybill.class)
    public ResponseEntity<PageableResponse<Waybill>> getWaybillsForApprovalImport(Pageable pageable) {
        return ResponseEntity.ok(new PageableResponse<>(waybillService.getWaybillsForConfirmationImport(pageable)));
    }

    @ApiOperation(value = "Get all waybills pending export confirmation ***", notes = "Please, use token.")
    @ApiPageable
    @PreAuthorize("hasAuthority('IMPORT_EXPORT')")
    @GetMapping("/waybills/pending-export")
    @JsonView(View.Waybill.class)
    public ResponseEntity<PageableResponse<Waybill>> getWaybillsForApprovalExport(Pageable pageable) {
        return ResponseEntity.ok(new PageableResponse<>(waybillService.getWaybillsForConfirmationExport(pageable)));
    }

    @ApiOperation(value = "Get all waybills pending auto-export confirmation ***", notes = "Please, use token.")
    @ApiPageable
    @PreAuthorize("hasAuthority('IMPORT_EXPORT')")
    @GetMapping("/waybills/pending-auto-export")
    @JsonView(View.Waybill.class)
    public ResponseEntity<PageableResponse<Waybill>> getWaybillsForApprovalAutoExport(Pageable pageable) {
        return ResponseEntity.ok(new PageableResponse<>(waybillService.getWaybillsForConfirmationAutoExport(pageable)));
    }

    @ApiOperation(value = "Get all rejected waybills ***", notes = "Please, use token.")
    @ApiPageable
    @PreAuthorize("hasAuthority('IMPORT_EXPORT')")
    @GetMapping("/waybills/rejected")
    @JsonView(View.Waybill.class)
    public ResponseEntity<PageableResponse<Waybill>> getRejectedWaybills(Pageable pageable) {
        return ResponseEntity.ok(new PageableResponse<>(waybillService.getRejectedWaybills(pageable)));
    }

    @ApiOperation(value = "Get pending waybills of current user", notes = "Please, use token.")
    @PreAuthorize("hasAuthority('APPROVE_ON_IMPORT_EXPORT')")
    @ApiPageable
    @GetMapping("/waybills/current/pending")
    @JsonView(View.Waybill.class)
    public ResponseEntity<PageableResponse<Waybill>> getPendingWaybillsOfCurrentUser(Pageable pageable) {
        return ResponseEntity.ok(new PageableResponse<>(waybillService.getPendingWaybillsOfCurrentUser(pageable)));
    }

    @ApiOperation(value = "Get completed waybills of current user", notes = "Please, use token.")
    @ApiPageable
    @GetMapping("/waybills/current/completed")
    @JsonView(View.Waybill.class)
    public ResponseEntity<PageableResponse<Waybill>> getCompletedWaybillsOfCurrentUser(Pageable pageable) {
        return ResponseEntity.ok(new PageableResponse<>(waybillService.getCompletedWaybillsOfCurrentUser(pageable)));
    }

    @ApiOperation(value = "Get rejected waybills of current user", notes = "Please, use token.")
    @PreAuthorize("hasAuthority('APPROVE_ON_IMPORT_EXPORT')")
    @ApiPageable
    @GetMapping("/waybills/current/rejected")
    @JsonView(View.Waybill.class)
    public ResponseEntity<PageableResponse<Waybill>> getRejectedWaybillsOfCurrentUser(Pageable pageable) {
        return ResponseEntity.ok(new PageableResponse<>(waybillService.getRejectedWaybillsOfCurrentUser(pageable)));
    }

}
