package com.izen.module.employee;

import com.izen.common.api.response.PageResponse;
import com.izen.common.config.security.JwtUtil;
import com.izen.module.employee.dto.request.EmployeeProfilesRequestDto;
import com.izen.module.employee.dto.response.EmployeeProfileResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmployeeService {
    private final EmployeeMapper employeeMapper;
    private final JwtUtil jwtUtil;

    public PageResponse<EmployeeProfileResponseDto> getEmployeeProfileList(EmployeeProfilesRequestDto reqDto) {
        Long accountId = jwtUtil.getCurrentUserId();
        List<EmployeeProfileResponseDto> employeeProfileList =
                employeeMapper.findEmployeeProfileList(reqDto);
        long totalElements = employeeMapper.countEmployeeProfileList();
        log.info("Employee Profiles requested by account ID: {}", accountId);
        return PageResponse.of(employeeProfileList, reqDto, totalElements);
    }
}
