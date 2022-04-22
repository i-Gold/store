package com.company.storehouse.controller.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.company.storehouse.validation.DateMatching;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDate;

@Data
@DateMatching
public class ImportProductRequest implements Serializable {
    private static final long serialVersionUID = 8146051102530729248L;

    private String name;

    private String manufacturer;

    private Double weight;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    @ApiModelProperty(dataType = "java.lang.String", example = "10-10-2020")
    private LocalDate producedAt;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    @ApiModelProperty(dataType = "java.lang.String", example = "10-10-2020")
    private LocalDate expiredAt;

}
