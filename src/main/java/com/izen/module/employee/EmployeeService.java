package com.izen.module.employee;

import com.izen.common.api.exception.CustomException;
import com.izen.common.api.response.PageResponse;
import com.izen.common.api.type.ResponseCode;
import com.izen.common.config.security.JwtUtil;
import com.izen.module.auth.AuthMapper;
import com.izen.module.employee.dto.request.EmployeeProfilesRequestDto;
import com.izen.module.employee.dto.response.*;
import com.izen.module.employee.dto.vo.EmployeeProfileVo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmployeeService {
    private final EmployeeMapper employeeMapper;
    private final AuthMapper authMapper;
    private final JwtUtil jwtUtil;

    public EmployeeProfileListResponseDto getEmployeeProfileList(EmployeeProfilesRequestDto reqDto) {
        Long accountId = jwtUtil.getCurrentUserId();
        List<EmployeeProfileVo> employeeProfileList =
                employeeMapper.findEmployeeProfileList(reqDto);
        long totalElements = employeeMapper.countEmployeeProfileList();
        PageResponse<EmployeeProfileVo> pagedEmployeeProfileList = PageResponse.of(employeeProfileList, reqDto, totalElements);
        List<DepartmentResponseDto> departmentList = employeeMapper.findDepartmentList();
        List<PositionResponseDto> positionList = employeeMapper.findPositionList();

        log.info("Employee Profiles requested by account ID: {}", accountId);
        return new EmployeeProfileListResponseDto(pagedEmployeeProfileList, departmentList, positionList);
    }

    public EmployeeProfileResponseDto getEmployeeProfile(Long id) {
        Long accountId = jwtUtil.getCurrentUserId();
        EmployeeProfileResponseDto employeeProfile = employeeMapper.findEmployeeProfileByEmployeeId(id)
                .orElseThrow(() -> new CustomException(ResponseCode.RESOURCE_NOT_FOUND));
        List<DepartmentResponseDto> departmentList = employeeMapper.findDepartmentList();
        List<PositionResponseDto> positionList = employeeMapper.findPositionList();
        employeeProfile.setDepartments(departmentList);
        employeeProfile.setPositions(positionList);
        log.info("Employee Profile requested by account ID: {}", accountId);
        log.info("employee ID: {}", id);
        return employeeProfile;
    }

    public EmployeeCodeResponseDto getEmployeeCode() {
        Long accountId = jwtUtil.getCurrentUserId();
        EmployeeCodeResponseDto employeeCode = employeeMapper.findEmployeeCode()
                .orElseThrow(() -> new CustomException(ResponseCode.INTERNAL_SERVER_ERROR));
        log.info("Employee Code requested by account ID: {}", accountId);
        log.info("Employee Code: {}", employeeCode.employeeCode());
        return employeeCode;
    }
}
