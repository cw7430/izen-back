package com.izen.module.employee;

import com.izen.common.api.exception.CustomException;
import com.izen.common.api.response.PageResponse;
import com.izen.common.api.type.ResponseCode;
import com.izen.common.config.security.JwtUtil;
import com.izen.module.auth.AuthMapper;
import com.izen.module.employee.dto.request.CreateEmployeeProfileRequestDto;
import com.izen.module.employee.dto.request.EmployeeProfilesRequestDto;
import com.izen.module.employee.dto.request.UpdateEmployeeProfileRequestDto;
import com.izen.module.employee.dto.response.*;
import com.izen.module.employee.dto.vo.EmployeeProfileVo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmployeeService {
    private final EmployeeMapper employeeMapper;
    private final AuthMapper authMapper;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;

    private static final Set<String> ALLOWED_PROFILE_TEAMS = Set.of("TM100", "TM200");

    private void checkProfilePermission(Long id) {
        String requesterTeam = employeeMapper.findEmployeeTeam(id)
                .orElseThrow(() -> new CustomException(ResponseCode.FORBIDDEN));

        if (!ALLOWED_PROFILE_TEAMS.contains(requesterTeam)) {
            throw new CustomException(ResponseCode.FORBIDDEN);
        }
    }

    public EmployeeProfileListResponseDto getEmployeeProfileList(EmployeeProfilesRequestDto reqDto) {
        Long accountId = jwtUtil.getCurrentUserId();
        List<EmployeeProfileVo> employeeProfileList =
                employeeMapper.findEmployeeProfileList(reqDto);
        long totalElements = employeeMapper.countEmployeeProfileList();
        PageResponse<EmployeeProfileVo> pagedEmployeeProfileList = PageResponse.of(employeeProfileList, reqDto, totalElements);
        List<DepartmentResponseDto> departmentList = employeeMapper.findDepartmentList();
        List<PositionResponseDto> positionList = employeeMapper.findPositionList();

        log.info("Employee Profiles requested by account ID: {}", accountId);
        return new EmployeeProfileListResponseDto(pagedEmployeeProfileList, departmentList, positionList, ALLOWED_PROFILE_TEAMS);
    }

    public EmployeeProfileResponseDto getEmployeeProfile(Long id) {
        Long accountId = jwtUtil.getCurrentUserId();
        EmployeeProfileResponseDto employeeProfile = employeeMapper.findEmployeeProfileByEmployeeId(id)
                .orElseThrow(() -> new CustomException(ResponseCode.RESOURCE_NOT_FOUND));
        List<DepartmentResponseDto> departmentList = employeeMapper.findDepartmentList();
        List<PositionResponseDto> positionList = employeeMapper.findPositionList();
        employeeProfile.setDepartments(departmentList);
        employeeProfile.setPositions(positionList);
        employeeProfile.setAllowedProfileTeams(ALLOWED_PROFILE_TEAMS);
        log.info("Employee Profile requested by account ID: {}", accountId);
        log.info("employee ID: {}", id);
        return employeeProfile;
    }

    public EmployeeCodeResponseDto getEmployeeCode() {
        Long accountId = jwtUtil.getCurrentUserId();
        checkProfilePermission(accountId);
        EmployeeCodeResponseDto employeeCode = employeeMapper.createEmployeeCode()
                .orElseThrow(() -> new CustomException(ResponseCode.INTERNAL_SERVER_ERROR));
        log.info("Employee Code requested by account ID: {}", accountId);
        log.info("Employee Code: {}", employeeCode.employeeCode());
        return employeeCode;
    }

    @Transactional
    public void createEmployeeProfile(CreateEmployeeProfileRequestDto reqDto) {
        Long accountId = jwtUtil.getCurrentUserId();
        checkProfilePermission(accountId);
        int insertAccountCnt = authMapper.createAccount(
                reqDto.employeeCode(),
                passwordEncoder.encode(reqDto.employeeCode()),
                reqDto.phone(),
                reqDto.email()
        );
        if (insertAccountCnt == 0) {
            throw new CustomException(ResponseCode.INTERNAL_SERVER_ERROR);
        }
        int insertProfileCnt = employeeMapper.createProfile(
                reqDto.employeeCode(),
                reqDto.employeeName(),
                reqDto.teamCode(),
                reqDto.positionCode(),
                reqDto.employeeRole(),
                accountId,
                accountId
        );
        if (insertProfileCnt == 0) {
            throw new CustomException(ResponseCode.INTERNAL_SERVER_ERROR);
        }
        log.info("Create Employee Profile requested by account ID: {}", accountId);
    }

    public void updateEmployeeProfile(Long id, UpdateEmployeeProfileRequestDto reqDto) {
        Long accountId = jwtUtil.getCurrentUserId();
        checkProfilePermission(accountId);
        int updateProfileCnt = employeeMapper.updateProfile(
                id, reqDto.teamCode(),
                reqDto.positionCode(),
                reqDto.employeeRole(),
                accountId
        );
        if (updateProfileCnt == 0) {
            throw new CustomException(ResponseCode.INTERNAL_SERVER_ERROR);
        }
        log.info("Update Employee Profile requested by account ID: {}", accountId);
        log.info("Updated Employee Id: {}", id);
    }
}
