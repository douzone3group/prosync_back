package com.douzone.prosync.task.service;

import com.douzone.prosync.common.PageResponseDto;
import com.douzone.prosync.task.dto.request.TaskMemberResponseDto;
import com.douzone.prosync.task.dto.request.TaskPatchDto;
import com.douzone.prosync.task.dto.request.TaskPostDto;
import com.douzone.prosync.task.dto.response.GetTaskResponse;
import com.douzone.prosync.task.dto.response.GetTasksResponse;

import java.util.List;

public interface TaskService {

    Long createTask(TaskPostDto dto, Long projectId, Long memberId);

    void updateTask(TaskPatchDto dto, Long taskId, Long memberId);

    void deleteTask(Long taskId, Long memberId);

    GetTaskResponse findTask(Long taskId, Long memberId);

    PageResponseDto<GetTasksResponse.PerTasksResponse> findTaskList(Long projectId, Integer page, Integer size, String search, boolean isActive, String view, String status, Long memberId);

    void createTaskMember(Long taskId, List<Long> projectMemberIds, Long memberId);

    void deleteTaskMember(Long taskId, List<Long> projectMemberIds, Long memberId);

    List<TaskMemberResponseDto> findTaskMembers(Long taskId, Long memberId);
}