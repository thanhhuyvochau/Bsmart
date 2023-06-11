package fpt.project.bsmart.controller;


import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static fpt.project.bsmart.util.Constants.UrlConstants.COMMON_REFERRAL_CODE;
import static fpt.project.bsmart.util.Constants.UrlConstants.COMMON_ROOT;


@RestController
@RequestMapping(COMMON_ROOT  + COMMON_REFERRAL_CODE)
public class ReferralCodeController {
}
