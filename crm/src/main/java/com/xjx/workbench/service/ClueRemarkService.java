package com.xjx.workbench.service;

import com.xjx.workbench.domain.ClueRemark;

import java.util.List;

public interface ClueRemarkService {
    List<ClueRemark> queryClueRemarkForDetailByClueId(String clueId);

    int addClueRemark(ClueRemark clueRemark);
}
