package com.izen.module.employee;

import com.izen.module.employee.dto.request.EmployeeProfilesRequestDto;
import com.izen.module.employee.dto.response.DepartmentResponseDto;
import com.izen.module.employee.dto.response.EmployeeCodeResponseDto;
import com.izen.module.employee.dto.response.EmployeeProfileResponseDto;
import com.izen.module.employee.dto.response.PositionResponseDto;
import com.izen.module.employee.dto.vo.EmployeeProfileVo;
import com.izen.module.employee.type.EmployeeRole;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Optional;

@Mapper
public interface EmployeeMapper {
    List<EmployeeProfileVo> findEmployeeProfileList(EmployeeProfilesRequestDto dto);

    List<DepartmentResponseDto> findDepartmentList();

    List<PositionResponseDto> findPositionList();

    long countEmployeeProfileList();

    Optional<EmployeeProfileResponseDto> findEmployeeProfileByEmployeeId(@Param("employeeId") Long employeeId);

    Optional<EmployeeCodeResponseDto> createEmployeeCode();

    Optional<String> findEmployeeTeam(@Param("employeeId") Long employeeId);

    int createProfile(
            @Param("employeeCode") String employeeCode,
            @Param("employeeName") String employeeName,
            @Param("teamCode") String teamCode,
            @Param("positionCode") String positionCode,
            @Param("employeeRole") EmployeeRole employeeRole,
            @Param("createdBy") Long createdBy,
            @Param("updatedBy") Long updatedBy
    );

    int updateProfile(
            @Param("employeeId") Long employeeId,
            @Param("teamCode") String teamCode,
            @Param("positionCode") String positionCode,
            @Param("employeeRole") EmployeeRole employeeRole,
            @Param("updatedBy") Long updatedBy
    );
}
