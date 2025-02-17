package com.bankdata.swiftmanager.controller;

import com.bankdata.swiftmanager.service.SWIFTCodesService;
import org.springframework.stereotype.Controller;

@Controller
public class SWIFTCodesController {
    SWIFTCodesService SWIFTCodesService;

    public SWIFTCodesController(com.bankdata.swiftmanager.service.SWIFTCodesService SWIFTCodesService) {
        this.SWIFTCodesService = SWIFTCodesService;
    }
}
