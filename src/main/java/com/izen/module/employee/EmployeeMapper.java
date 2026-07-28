package com.izen.module.employee;

import com.izen.module.employee.dto.request.EmployeeProfilesRequestDto;
import com.izen.module.employee.dto.response.EmployeeProfileResponseDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface EmployeeMapper {
    List<EmployeeProfileResponseDto> findEmployeeProfileList(EmployeeProfilesRequestDto dto);
}
