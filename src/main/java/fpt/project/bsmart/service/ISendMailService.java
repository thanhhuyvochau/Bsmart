package fpt.project.bsmart.service;

import fpt.project.bsmart.entity.AccountDetail;
import fpt.project.bsmart.entity.dto.EmailDto;
import fpt.project.bsmart.entity.request.*;

import java.util.List;

public interface ISendMailService {

    Boolean sendMail (EmailDto emailDto, String title , String content,  String footer ) ;


    Boolean sendMailToRegisterDoTeacher(List<EmailDto> emailDto, AccountDetail accountDetail, String password);

//    Boolean sendMailToRegisterDoTeacher(AccountDetail accountDetail);
}
