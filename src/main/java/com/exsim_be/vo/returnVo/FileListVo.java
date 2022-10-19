package com.exsim_be.vo.returnVo;

import lombok.Data;
import java.util.List;

/**
 * @author 贾楠
 * @version 1.0
 * @date 2022-10-19
 */
@Data
public class FileListVo {
    private List<FileRetVo> files;
    private long total;
    private long size;
    private long current;
}
