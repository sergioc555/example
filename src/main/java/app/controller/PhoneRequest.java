package app.controller;

import lombok.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class PhoneRequest {
    private Long id;
    private String number;
    private String citycode;
    private String countrycode;
}
