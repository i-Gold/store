package com.company.storehouse.controller.response.pagination;

import com.fasterxml.jackson.annotation.JsonView;
import com.company.storehouse.controller.response.View;
import lombok.Data;
import org.springframework.data.domain.Page;

import java.io.Serializable;
import java.util.List;

@Data
@JsonView({View.Product.class, View.Waybill.class})
public class PageableResponse<T> implements Serializable {
    private static final long serialVersionUID = 4499654783312864767L;

    private List<T> content;
    private int number;
    private long totalElements;
    private int totalPages;

    public PageableResponse(Page<T> page) {
        this.totalElements = page.getTotalElements();
        this.totalPages = page.getTotalPages();
        this.number = page.getNumber();
        this.content = page.getContent();
    }
}
