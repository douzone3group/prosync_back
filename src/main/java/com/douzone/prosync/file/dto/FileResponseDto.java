package com.douzone.prosync.file.dto;

import com.douzone.prosync.file.entity.File;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class FileResponseDto {

    @ApiModelProperty(value = "파일식별자", example = "1")
    private Long fileId;

    @ApiModelProperty(value = "파일크기", example = "10000")
    private Long size;

    @ApiModelProperty(value = "파일경로", example = "http://~")
    private String path;

    @ApiModelProperty(value = "파일이름", example = "파일이름.jpg")
    private String fileName;

    @ApiModelProperty(value = "생성일자", example = "2023-08-27 22:25:26")
    private String createdAt;

    @ApiModelProperty(value = "파일정보식별자", example = "1")
    private Long fileInfoId;

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public static FileResponseDto response(File file, Boolean isResponse) {
        FileResponseDto response = FileResponseDto.builder()
                .fileId(file.getFileId())
                .path(file.getPath())
                .size(file.getSize())
                .fileName(file.getFileName())
                .createdAt(file.getCreatedAt().toString().replace("T", " "))
                .build();

        if (isResponse) {
            response.setFileName(getOriginalFileName(response.getFileName()));
        }
        return response;
    }

    public static String getOriginalFileName(String fileName) {
       return fileName.substring(0, fileName.lastIndexOf("_"));
    }
}
