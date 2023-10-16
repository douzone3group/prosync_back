package com.douzone.prosync.comment.dto.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@ApiModel("[REQUEST] COMMENT - POST")
public class CommentPostDto {

    @ApiModelProperty(value = "댓글 식별자", required = true, example = "댓글 식별자")
    private Long commentId;

    @NotBlank
    @ApiModelProperty(value = "댓글 내용", required = true, example = "댓글 내용")
    @Length(max = 300, message = "댓글을 300자 이내여야 합니다")
    private String content;

    private Long memberId;

    private Long taskId;

    private Long memberProjectId;

    @ApiModelProperty(value = "파일식별자", example = "[1, 2, 3]")
    private List<Long> fileIds = new ArrayList<>();
}
