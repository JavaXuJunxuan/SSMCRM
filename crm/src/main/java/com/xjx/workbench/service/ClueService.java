package com.xjx.workbench.service;

import com.xjx.workbench.domain.Clue;

import java.util.List;
import java.util.Map;

public interface ClueService {
    int saveCreateClue(Clue clue);

    List<Clue> selectActivityByConditionForPage(Map map);

    int selectCountByConditionForPage(Map map);

    Clue queryClueForDetailById(String id);

    Clue selectClueById(String id);

    int saveEditClue(Clue clue);

    int deleteClueById(String[] ids);

    void saveConvertClue(Map map);
}
