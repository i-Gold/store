package com.company.storehouse.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonView;
import com.company.storehouse.controller.response.View;
import com.company.storehouse.model.enumeration.ProcessingStatus;
import com.company.storehouse.model.enumeration.WaybillType;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "waybills")
@Getter
@Setter
@EqualsAndHashCode
public class Waybill implements Serializable {
    private static final long serialVersionUID = -1649371831137840947L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonView({View.Waybill.class})
    private Long id;

    @NotNull
    @Enumerated(EnumType.STRING)
    @JsonView({View.Waybill.class})
    private ProcessingStatus status;

    @NotNull
    @Enumerated(EnumType.STRING)
    @JsonView({View.Waybill.class})
    private WaybillType type;

    @JsonView({View.Waybill.class})
    private Long ownerId;

    @JsonView({View.Waybill.class})
    private Long approverId;

    @JsonView({View.Waybill.class})
    private Boolean isApproved = Boolean.FALSE;

    @JsonFormat(pattern = "dd-MM-yyyy HH:mm")
    @JsonView({View.Waybill.class})
    private LocalDateTime createdAt;

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH})
    @JoinTable(name = "waybills_products",
            joinColumns = @JoinColumn(name = "waybill_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "product_id", referencedColumnName = "id"))
    @JsonView({View.Waybill.class})
    private List<Product> products = new ArrayList<>();

}
