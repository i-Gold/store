package com.company.storehouse.repository;

import com.company.storehouse.model.Waybill;
import com.company.storehouse.model.enumeration.ProcessingStatus;
import com.company.storehouse.model.enumeration.WaybillType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface WaybillRepository extends JpaRepository<Waybill, Long> {

    Page<Waybill> findAllByStatusAndTypeAndIsApprovedFalseAndCreatedAtNull(ProcessingStatus status, WaybillType type, Pageable pageable);

    @Query(value = "SELECT w.* FROM waybills w WHERE w.status = 'REJECTED' " +
            "ORDER BY w.created_at DESC " +
            "LIMIT ?#{#pageable.offset}, ?#{#pageable.offset + #pageable.pageSize}",
            countQuery = "SELECT COUNT(DISTINCT w.id) FROM waybills w WHERE w.status = 'REJECTED'",
            nativeQuery = true)
    Page<Waybill> getRejectedWaybills(Pageable pageable);

    Page<Waybill> findAllByOwnerIdAndStatusOrderByIdDesc(Long currentUserId, ProcessingStatus status, Pageable pageable);

    Page<Waybill> findAllByOrderByIdDesc(Pageable pageable);

}
