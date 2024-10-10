package bi.accounting.service;

import bi.accounting.dto.AccountOpenIdDTO;
import jakarta.inject.Singleton;

import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

@Singleton
public class TenantService {

    public List<AccountOpenIdDTO> mapTenantsToDTO(List<HashMap<String, Object>> tenantList) {
        return tenantList.stream()
                .filter(tenant -> "ORGANISATION".equalsIgnoreCase((String) tenant.get("tenantType")))
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    private AccountOpenIdDTO convertToDTO(HashMap<String, Object> tenant) {
        AccountOpenIdDTO dto = new AccountOpenIdDTO();
        dto.setId((String) tenant.get("id"));
        dto.setTenantId((String) tenant.get("tenantId"));
        dto.setTenantType((String) tenant.get("tenantType"));
        dto.setTenantName((String) tenant.get("tenantName"));
        dto.setCreatedDateUtc((String) tenant.get("createdDateUtc"));
        dto.setUpdatedDateUtc((String) tenant.get("updatedDateUtc"));
        return dto;
    }
}