package com.douzone.prosync.member_project.dto;

import com.douzone.prosync.member_project.entity.MemberProject;
import com.douzone.prosync.member_project.status.ProjectMemberAuthority;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class MemberProjectResponseDto {

    @ApiModelProperty(value = "프로젝트 회원 식별자", example = "1")
    private Long memberProjectId;

    @ApiModelProperty(value = "회원 식별자", example = "1")
    private Long memberId;

    @ApiModelProperty(value = "프로젝트 식별자", example = "1")
    private Long projectId;

    @ApiModelProperty(value = "권한 식별자", example = "1")
    private Long authorityId;

    @ApiModelProperty(value = "권한", example = "ADMIN / WRITER / READER")
    private ProjectMemberAuthority authority;

    @ApiModelProperty(value = "회원 이름", example = "홍길동")
    private String name;

    @ApiModelProperty(value = "회원 프로필", example = "http://~")
    private String profileImage;

    @ApiModelProperty(value = "프로젝트회원 상태", example = "QUIT / ACTIVE")
    private MemberProject.MemberProjectStatus status;


}
