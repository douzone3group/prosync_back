package com.douzone.prosync.notification.entity;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class NotificationTarget {
    private Long notificationTargetId;
    private Long notificationId;
    private Long memberId;
    private String platform;
    private boolean isRead;
    private boolean isDeleted;
    private Long updateUserNo;
    private boolean isTransmit;
}
