package com.nttdata.BancaProductActive.domain;

import lombok.*;

import java.time.LocalDate;

@Data
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class Client {

    private String idClient;
    private String identityNumber;
    private String name;
    private String lastName;
    private String address;
    private String phone;
    private String city;
    private String email;
    private String typeClient;
    private String ruc;
    private String companyName;
    private LocalDate dateRegister;


}
