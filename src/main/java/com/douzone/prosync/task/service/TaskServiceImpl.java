package com.douzone.prosync.task.service;

import com.douzone.prosync.common.PageResponseDto;
import com.douzone.prosync.exception.ApplicationException;
import com.douzone.prosync.exception.ErrorCode;

import com.douzone.prosync.member.dto.response.MemberGetResponse;
import com.douzone.prosync.member.repository.MemberRepository;
import com.douzone.prosync.notification.notienum.NotificationCode;
import com.douzone.prosync.notification.service.NotificationService;
import com.douzone.prosync.project.entity.Project;
import com.douzone.prosync.project.service.ProjectService;
import com.douzone.prosync.task.dto.request.TaskPatchDto;
import com.douzone.prosync.task.dto.request.TaskPostDto;
import com.douzone.prosync.task.dto.response.GetTaskResponse;
import com.douzone.prosync.task.dto.response.GetTasksResponse;
import com.douzone.prosync.task.entity.Task;
import com.douzone.prosync.task.repository.TaskJpaRepository;
import com.douzone.prosync.task.repository.TaskMapper;
import com.douzone.prosync.task_status.service.TaskStatusService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
@Transactional
@RequiredArgsConstructor
public class TaskServiceImpl implements TaskService {

    private final TaskMapper taskMapper;
    private final TaskJpaRepository taskJpaRepository;
    private final TaskStatusService taskStatusService;
    private final ProjectService projectService;



    private final NotificationService notificationService;


    @Override
    public Long createTask(TaskPostDto dto, Long projectId, Long memberId) {
        // TODO : 프로젝트 회원 + writer인지 확인
        Project findProject = projectService.findProject(projectId);

        // check task_status of project
        verifyTaskStatus(findProject.getProjectId(), dto.getTaskStatusId(), memberId);

        dto.setCreatedAt(LocalDateTime.now());

        taskMapper.save(dto, projectId);
        return dto.getTaskId();
    }

    @Override
    public void updateTask(TaskPatchDto dto, Long taskId, Long memberId) {

        GetTaskResponse findTask = findExistTask(taskId);
        dto.setTaskId(taskId);
        dto.setModifiedAt(LocalDateTime.now());

        // find task_status of project
        if (Optional.ofNullable(dto.getTaskStatusId()).isPresent()) {
            verifyTaskStatus(findTask.getProjectId(), dto.getTaskStatusId(), memberId);
        }



        taskMapper.update(dto);
    }

    @Override
    public void deleteTask(Long taskId, Long memberId) {
        GetTaskResponse task = verifyExistTask(taskId);
        //soft delete
        taskMapper.delete(taskId);


        // Todo: 해당 Task의 멤버들에게 알림을 전달하는 로직 작성
//        taskMapper.
//        notificationService.saveAndSendNotification(memberId, NotificationCode.TASK_REMOVE, task, );
    }

    @Transactional(readOnly = true)
    @Override
    public GetTaskResponse findTask(Long taskId, Long memberId) {
        return findExistTask(taskId);
    }

    /**
     * 업무 리스트 조회
     */
    @Transactional(readOnly = true)
    @Override
    public PageResponseDto<GetTasksResponse.PerTasksResponse> findTaskList(Long projectId, Pageable pageable, String search, boolean isActive, String view, Long memberId) {
        pageable = PageRequest.of(pageable.getPageNumber() == 0 ? 0 : pageable.getPageNumber() - 1, pageable.getPageSize(), pageable.getSort());

        Page<Task> pages = search != null && !search.trim().isEmpty() ?
                taskJpaRepository.findAllByProjectIdAndSearch(projectId, search, pageable) :
                taskJpaRepository.findByProjectIdAndIsDeletedNull(projectId, pageable);

        // isActive = true면 체크된 업무만 조회
        List<GetTasksResponse> res = isActive ? pages.getContent()
                .stream()
                .map(task -> GetTasksResponse.of(task))
                .filter(task -> task.getSeq() != 0)
                .collect(Collectors.toList()) :
                pages.getContent()
                .stream()
                .map(task -> GetTasksResponse.of(task))
                .collect(Collectors.toList());

        // 보드뷰일 경우 task_status별 응답 리턴
        if (view != null && view.equals("board")) {
            return new PageResponseDto(GetTasksResponse.PerTasksResponse.of(res), pages);
        }
        return new PageResponseDto(res, pages);
    }

    /**
     * 업무 담당자 지정
     */
    @Override
    public void createTaskMember(Long taskId, List<Long> memberIds, Long memberId) {
        // TODO : 프로젝트 회원 + writer 인지 검증
        // TODO : MEMBER_TASK 해당되는 값이 없을 경우 처리

        GetTaskResponse task = verifyExistTask(taskId);

        taskMapper.saveTaskMember(taskId, memberIds);


        notificationService.saveAndSendNotification(memberId, NotificationCode.TASK_ASSIGNMENT,
                task, memberIds);



    }

    /**
     * 업무 담당자 삭제
     */
    @Override
    public void deleteTaskMember(Long taskId, List<Long> memberIds, Long memberId) {
        // TODO : 프로젝트 회원 + writer 인지 검증
        // TODO : MEMBER_TASK 해당되는 값이 없을 경우 처리
        GetTaskResponse task = verifyExistTask(taskId);

        taskMapper.deleteTaskMember(taskId, memberIds);

        notificationService.saveAndSendNotification(memberId, NotificationCode.TASK_EXCLUDED, task, memberIds);
    }

    @Override
    public List<MemberGetResponse.SimpleResponse> findTaskMembers(Long taskId, long memberId) {
        return taskMapper.findTaskMembers(taskId);
    }

    private GetTaskResponse findExistTask(Long taskId) {
        return taskMapper.findById(taskId).orElseThrow(() -> new ApplicationException(ErrorCode.TASK_NOT_FOUND));
    }

    /**
     * 삭제된 task인 경우 예외 처리
     */
    private GetTaskResponse verifyExistTask(Long taskId) {

        GetTaskResponse task = taskMapper.findById(taskId).orElse(null);
        if (task == null) {
            throw new ApplicationException(ErrorCode.TASK_NOT_FOUND);
        }

        return task;
    }

    private void verifyTaskStatus(Long projectId, Integer taskStatusId, Long memberId) {
        taskStatusService.getTaskStatusByProject(projectId, false, memberId)
                .stream()
                .filter(status -> status.getTaskStatusId() == taskStatusId).findFirst()
                .orElseThrow(() -> new ApplicationException(ErrorCode.TASK_STATUS_NOT_FOUND));
    }

}
