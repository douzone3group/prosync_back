package com.douzone.prosync.searchcondition;

import com.douzone.prosync.notification.notienum.NotificationCode;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class NotificationSearchCondition {

    private Long memberId;
    private String notiCode;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private String content;



}
