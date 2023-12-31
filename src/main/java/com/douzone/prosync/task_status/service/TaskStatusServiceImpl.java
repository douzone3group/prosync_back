package com.douzone.prosync.task_status.service;

import com.douzone.prosync.exception.ApplicationException;
import com.douzone.prosync.exception.ErrorCode;
import com.douzone.prosync.task_status.dto.TaskStatusDto;
import com.douzone.prosync.task_status.repository.TaskStatusMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class TaskStatusServiceImpl implements TaskStatusService {

    private final TaskStatusMapper taskStatusMapper;


    @Override
    public Long createTaskStatus(Long projectId, TaskStatusDto.PostDto requestBody) {
        taskStatusMapper.save(projectId, requestBody);
        return requestBody.getTaskStatusId();
    }

    @Override
    public void updateTaskStatus(Long taskStatusId, TaskStatusDto.PatchDto requestBody, Long memberId) {
        requestBody.setTaskStatusId(taskStatusId);
        findTaskStatus(taskStatusId);
        taskStatusMapper.update(requestBody);
    }

    @Override
    public void deleteTaskStatus(Long taskStatusId, Long memberId) {
        verifyExistTaskStatus(taskStatusId);
        // task에 삭제하는 task status가 지정되어 있으면 예외 던짐
        if (taskStatusMapper.findTaskByTaskStatus(taskStatusId) != 0) {
            throw new ApplicationException(ErrorCode.TASK_EXISTS);
        }
        taskStatusMapper.delete(taskStatusId);
    }

    @Override
    public List<TaskStatusDto.GetResponseDto> getTaskStatusByProject(Long projectId, boolean isActive, Long memberId) {
        return taskStatusMapper.findTaskStatusByProject(projectId, isActive);
    }

    @Override
    public TaskStatusDto.GetResponseDto getTaskStatus(Long taskStatusId, Long memberId) {
        TaskStatusDto.GetResponseDto findTaskStatus = findTaskStatus(taskStatusId);
        return findTaskStatus;
    }

    @Override
    public void updateSeq(Long projectId, List<TaskStatusDto.SeqRequestDto> taskStatusSeqList, Long memberId) {
        for (TaskStatusDto.SeqRequestDto taskStatusSeq : taskStatusSeqList) {
            taskStatusMapper.updateSeq(projectId, taskStatusSeq);
        }
    }

    private void verifyExistTaskStatus(Long taskStatusId) {
        if (taskStatusMapper.findExistsTaskStatus(taskStatusId) == 0) {
            throw new ApplicationException(ErrorCode.TASK_STATUS_NOT_FOUND);
        }
    }

    private TaskStatusDto.GetResponseDto findTaskStatus(Long taskStatusId) {
        return taskStatusMapper.findTaskStatus(taskStatusId).orElseThrow(() -> new ApplicationException(ErrorCode.TASK_STATUS_NOT_FOUND));
    }
}
