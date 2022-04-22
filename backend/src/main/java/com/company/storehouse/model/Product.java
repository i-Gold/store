package com.company.storehouse.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonView;
import com.opencsv.bean.CsvDate;
import com.company.storehouse.controller.response.View;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "products")
@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class Product implements Serializable {
    private static final long serialVersionUID = -2216460203861446542L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonView({View.Product.class, View.Waybill.class})
    private Long id;

    @JsonView({View.Product.class, View.Waybill.class})
    private String name;

    @JsonView({View.Product.class, View.Waybill.class})
    private String manufacturer;

    @JsonView({View.Product.class, View.Waybill.class})
    private Double weight;

    @JsonFormat(pattern = "yyyy-MM-dd")
    @CsvDate(value = "dd.MM.yyyy")
    @JsonView({View.Product.class, View.Waybill.class})
    private LocalDate producedAt;

    @JsonFormat(pattern = "yyyy-MM-dd")
    @CsvDate(value = "dd.MM.yyyy")
    @JsonView({View.Product.class, View.Waybill.class})
    private LocalDate expiredAt;

    @NotNull
    @JsonView({View.Waybill.class})
    private Long importerId;

    @JsonView({View.Waybill.class})
    private Long exporterId;

    @Builder.Default
    @JsonView({View.Waybill.class})
    private Boolean isAvailable = Boolean.FALSE;

    @Builder.Default
    @JsonView({View.Waybill.class})
    private Boolean isPendingImport = Boolean.FALSE;

    @JsonFormat(pattern = "dd-MM-yyyy HH:mm")
    @JsonView({View.Product.class, View.Waybill.class})
    private LocalDateTime importedAt;

    @Builder.Default
    @JsonView({View.Waybill.class})
    private Boolean isPendingExport = Boolean.FALSE;

    @Builder.Default
    @JsonView({View.Waybill.class})
    private Boolean forExport = Boolean.FALSE;

    @Builder.Default
    @JsonView({View.Waybill.class})
    private Boolean isExported = Boolean.FALSE;

    @Builder.Default
    @JsonView({View.Waybill.class})
    private Boolean isRejectedForImport = Boolean.FALSE;

}
