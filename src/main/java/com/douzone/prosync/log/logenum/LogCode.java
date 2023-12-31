package com.douzone.prosync.log.logenum;

public enum LogCode {
    TASK_REMOVE("업무삭제"), TASK_ASSIGNMENT("업무지정"), TASK_MODIFICATION("업무수정"), TASK_EXCLUDED("업무제외"), TASK_CREATE("업무생성"),
    PROJECT_ASSIGNMENT("프로젝트지정"), PROJECT_EXCLUDED("프로젝트제외"), PROJECT_EXIT("프로젝트탈퇴"),
    PROJECT_MODIFICATION("프로젝트수정"), PROJECT_REMOVE("프로젝트삭제"),
    COMMENT_ADD("댓글추가"),  COMMENT_REMOVE("댓글삭제"),  COMMENT_MODIFICATION("댓글수정"), CHANGE_AUTHORITY("권한변경");



    final private String code;

    LogCode(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}
