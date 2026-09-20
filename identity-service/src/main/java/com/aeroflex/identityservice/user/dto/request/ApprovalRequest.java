package com.aeroflex.identityservice.user.dto.request;

import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class ApprovalRequest {

    @Size(max = 1000)
    private String remarks;
}
