package com.carbon.backend.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

@Data
@AllArgsConstructor(staticName = "of")
public class Co2Record implements Serializable {

    private static final long serialVersionUID = 1L;

    private String country;
    private Integer recordYear;
    private Double co2PerCapita;
    private Double renewable_share;


}
