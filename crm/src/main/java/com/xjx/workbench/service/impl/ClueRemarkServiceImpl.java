package com.xjx.workbench.service.impl;

import com.xjx.workbench.dao.ClueRemarkMapper;
import com.xjx.workbench.domain.ClueRemark;
import com.xjx.workbench.service.ClueRemarkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClueRemarkServiceImpl implements ClueRemarkService {
    @Autowired
    private ClueRemarkMapper clueRemarkMapper;

    @Override
    public List<ClueRemark> queryClueRemarkForDetailByClueId(String clueId) {
        return clueRemarkMapper.selectClueRemarkForDetailByClueId(clueId);
    }

    @Override
    public int addClueRemark(ClueRemark clueRemark) {
        return clueRemarkMapper.addClueRemark(clueRemark);
    }
}
