package fpt.project.bsmart.service.Impl;

import fpt.project.bsmart.entity.Role;
import fpt.project.bsmart.entity.User;
import fpt.project.bsmart.entity.common.ApiException;
import fpt.project.bsmart.entity.request.RoleRequest;
import fpt.project.bsmart.entity.response.AccountResponse;
import fpt.project.bsmart.entity.response.RoleResponse;
import fpt.project.bsmart.moodle.repository.MoodleRoleRepository;

import fpt.project.bsmart.repository.AccountRepository;
import fpt.project.bsmart.repository.RoleRepository;
import fpt.project.bsmart.service.IRoleService;
import fpt.project.bsmart.util.ObjectUtil;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class RoleServiceImpl implements IRoleService {
    private final RoleRepository roleRepository;
    private final AccountRepository accountRepository;
    private final MoodleRoleRepository moodleRoleRepository;

    public RoleServiceImpl(RoleRepository roleRepository, AccountRepository accountRepository, MoodleRoleRepository moodleRoleRepository) {
        this.roleRepository = roleRepository;
        this.accountRepository = accountRepository;
        this.moodleRoleRepository = moodleRoleRepository;
    }

    @Override
    public List<RoleResponse> getAll() {
        List<Role> roles = roleRepository.findAll();

        return roles.stream().map(role -> {
            RoleResponse roleResponse = convertRoleToRoleResponse(role);
            return roleResponse;
        }).collect(Collectors.toList());
    }

    @Override
    public RoleResponse create(RoleRequest roleRequest) {
        List<Long> accountIds = roleRequest.getAccountIds();
        List<User> accounts = null;
        if (!accountIds.isEmpty()) {
            accounts = accountRepository.findAllById(accountIds);
            if (accounts.size() < accountIds.size()) {
                throw ApiException.create(HttpStatus.CONFLICT).withMessage("Invalid to add account into new role");
            }
        }
        Role role = new Role();
        role.setName(roleRequest.getName());
        role.setAccounts(accounts);
        roleRepository.save(role);
        return convertRoleToRoleResponse(role);
    }

    @Override
    public RoleResponse update(RoleRequest roleRequest, Long id) {
        List<Long> accountIds = roleRequest.getAccountIds();
        Role oldRole = roleRepository.findById(id).orElseThrow(() -> ApiException.create(HttpStatus.NOT_FOUND).withMessage("Role not found by id:" + id));
        List<User> accounts = null;
        if (!accountIds.isEmpty()) {
            accounts = accountRepository.findAllById(accountIds);
            if (accounts.size() < accountIds.size()) {
                throw ApiException.create(HttpStatus.CONFLICT).withMessage("Invalid to add account into new role");
            }
        }
        oldRole.setName(roleRequest.getName());
        oldRole.setAccounts(accounts);
        roleRepository.save(oldRole);
        return convertRoleToRoleResponse(oldRole);
    }

    @Override
    public Boolean delete(Long id) {
        Role oldRole = roleRepository.findById(id).orElseThrow(() -> ApiException.create(HttpStatus.NOT_FOUND).withMessage("Role not found by id:" + id));
        roleRepository.delete(oldRole);
        return true;
    }

    private RoleResponse convertRoleToRoleResponse(Role role) {
        RoleResponse roleResponse = ObjectUtil.copyProperties(role, new RoleResponse(), RoleResponse.class, true);
        List<AccountResponse> accountResponses = role.getAccounts().stream().map(account -> {
            return ObjectUtil.copyProperties(account, new AccountResponse(), AccountResponse.class, true);
        }).collect(Collectors.toList());
        roleResponse.setAccountResponseList(accountResponses);
        return roleResponse;
    }


}
