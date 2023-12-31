package com.douzone.prosync.project.repository;

import com.douzone.prosync.project.dto.request.ProjectPatchDto;
import com.douzone.prosync.project.dto.request.ProjectPostDto;
import com.douzone.prosync.project.dto.request.ProjectSearchCond;
import com.douzone.prosync.project.dto.response.GetProjectsResponse;
import com.douzone.prosync.project.entity.Project;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Optional;

@Mapper
public interface ProjectMapper {

    // 프로젝트 단일 조회
    Optional<Project> findById(Long projectId);

    // 프로젝트 생성
    Integer createProject(ProjectPostDto dto);

    // 프로젝트 수정
    Integer updateProject(ProjectPatchDto dto);

    // 프로젝트 삭제
    Integer deleteProject(Long projectId);

    List<GetProjectsResponse> findAll(ProjectSearchCond searchCond);

    List<GetProjectsResponse> findByMemberId(@Param("memberId") Long memberId, @Param("search") ProjectSearchCond searchCond);

    List<GetProjectsResponse> findByMemberIdPartOfAdmin(Long memberId);
    List<Long> findMembersInProject(Long projectId);


}
