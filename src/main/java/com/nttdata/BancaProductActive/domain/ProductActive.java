package com.nttdata.BancaProductActive.domain;

import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;

@Data
@Builder
@ToString
@EqualsAndHashCode(of = {"identityNumber"})
@AllArgsConstructor
@NoArgsConstructor
@Document(value = "activeProduct")
public class ProductActive {
    @Id
    private String idProductActive;
    @NotNull
    private String numberContract;
    @NotNull
    private String amount;
    @NotNull
    private String limitCredit;

    private String identityNumber;
    @NotNull
    private String holder;
    @NotNull
    private String signatory;
    @NotNull
    private LocalDate dateRegister;
    @NotNull
    private String availableAmount;
    @NotNull
    private String typeCredit;

}
