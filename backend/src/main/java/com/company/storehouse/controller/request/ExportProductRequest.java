package com.company.storehouse.controller.request;

import lombok.Data;

import java.io.Serializable;
import java.util.Set;

@Data
public class ExportProductRequest implements Serializable {
    private static final long serialVersionUID = 6385282758234264190L;

    private Set<Long> productsIds;

}
