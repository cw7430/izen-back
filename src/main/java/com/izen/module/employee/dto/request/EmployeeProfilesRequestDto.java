package com.izen.module.employee.dto.request;

import com.izen.common.api.request.PageRequest;
import com.izen.common.api.type.SortOrder;
import com.izen.module.employee.type.EmployeeProfileSortPath;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(name = "EmployeeProfilesRequest")
public class EmployeeProfilesRequestDto extends PageRequest {
    public EmployeeProfilesRequestDto() {
        super();
    }

    private EmployeeProfileSortPath sortPath = EmployeeProfileSortPath.EMPLOYEE;

    private SortOrder sortOrder = SortOrder.ASC;
}
