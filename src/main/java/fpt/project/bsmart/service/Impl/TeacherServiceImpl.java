package fpt.project.bsmart.service.Impl;

import fpt.project.bsmart.entity.Account;
import fpt.project.bsmart.entity.Role;
import fpt.project.bsmart.entity.common.ApiException;
import fpt.project.bsmart.entity.common.ApiPage;
import fpt.project.bsmart.entity.common.EAccountDetailStatus;
import fpt.project.bsmart.entity.common.EAccountRole;
import fpt.project.bsmart.entity.response.AccountDetailResponse;
import fpt.project.bsmart.repository.AccountRepository;
import fpt.project.bsmart.repository.RoleRepository;
import fpt.project.bsmart.service.ITeacherService;
import fpt.project.bsmart.util.ConvertUtil;
import fpt.project.bsmart.util.PageUtil;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
public class TeacherServiceImpl implements ITeacherService {

    private final AccountRepository accountRepository ;

    private final RoleRepository roleRepository ;

    public TeacherServiceImpl(AccountRepository accountRepository, RoleRepository roleRepository) {
        this.accountRepository = accountRepository;
        this.roleRepository = roleRepository;
    }


    @Override
    public List<Account> getTeacher() {
//        List<Account> allTeacher = accountRepository.findAccountByRole(eAccountRole);
        List<Account> allTeacher1 = accountRepository.findAll();

        return  allTeacher1;
    }

    @Override
    public ApiPage<AccountDetailResponse> getAllTeacher(Pageable pageable) {
        Role role_not_found = roleRepository.findRoleByCode(EAccountRole.TEACHER)
                .orElseThrow(() -> ApiException.create(HttpStatus.NOT_FOUND).withMessage("Role not found"));
        Page<Account> accountByRoleAndAccountDetailStatus = accountRepository.findAccountByRoleAndAccountDetailStatus(role_not_found, EAccountDetailStatus.APPROVE, pageable);
       return PageUtil.convert(accountByRoleAndAccountDetailStatus.map(account -> {
           return ConvertUtil.doConvertEntityToResponse(account.getAccountDetail());
       })) ;
    }
}
