package fpt.project.bsmart.entity.request;

import java.util.ArrayList;
import java.util.List;

public class RoleRequest {
    private String name;
    private String role;
    private List<Long> accountIds = new ArrayList<>();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Long> getAccountIds() {
        return accountIds;
    }

    public void setAccountIds(List<Long> accountIds) {
        this.accountIds = accountIds;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
