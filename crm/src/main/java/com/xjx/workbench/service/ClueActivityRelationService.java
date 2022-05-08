package com.xjx.workbench.service;

import com.xjx.workbench.domain.ClueActivityRelation;

import java.util.List;

public interface ClueActivityRelationService {
    public int saveCreateClueActivityRelationByList(List<ClueActivityRelation> list);

    public int deleteClueActivityRelationByClueIdActivityId(ClueActivityRelation relation);
}
