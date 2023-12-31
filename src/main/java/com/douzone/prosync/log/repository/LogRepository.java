package com.douzone.prosync.log.repository;

import com.douzone.prosync.log.dto.LogDto;
import com.douzone.prosync.log.dto.request.LogPatchDto;
import com.douzone.prosync.log.dto.response.LogResponse;
import com.douzone.prosync.searchcondition.LogSearchCondition;

import java.util.List;


public interface LogRepository {

    Long saveLog(LogDto dto);


    Long deleteLog(Long logId);

    List<LogResponse> getLogList(LogSearchCondition condition);

    Long updateLog(Long logId,LogPatchDto dto);


    LogResponse findById(Long logId);


    Integer getLogListCount(Long projectId);

}
