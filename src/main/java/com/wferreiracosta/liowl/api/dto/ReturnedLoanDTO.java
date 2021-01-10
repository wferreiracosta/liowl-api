package com.wferreiracosta.liowl.api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReturnedLoanDTO {
    
    @SuppressWarnings("all")
    private Boolean returned;
    
}
