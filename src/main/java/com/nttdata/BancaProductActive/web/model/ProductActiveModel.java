package com.nttdata.BancaProductActive.web.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductActiveModel {
    @JsonIgnore
    private String idProductActive;
    private String numberContract;
    private String amount;
    private String limitCredit;
    private String identityNumber;
    private String holder;
    private String signatory;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate dateRegister;
    private String availableAmount;
    private String typeCredit;
}
