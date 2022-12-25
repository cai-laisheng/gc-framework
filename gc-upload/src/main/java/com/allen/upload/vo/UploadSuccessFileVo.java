package com.allen.upload.vo;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class UploadSuccessFileVo implements Serializable {

    private String id;

    private String fileName;

    private String filePath;

}
