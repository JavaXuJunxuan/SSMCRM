package com.xjx.workbench.service;

import com.xjx.workbench.domain.Activity;

import java.util.List;
import java.util.Map;

public interface ActivityService {
    int saveCreateActivity(Activity activity);
    List<Activity> selectActivityByConditionForPage(Map map);

    int selectCountByConditionForPage(Map map);

    int deleteActivityByIds(String[] strings);

    Activity selectActivityById(String id);

    int updateActivity(Activity activity);

    List<Activity> selectAllActivities();

    int saveCreateActivityByList(List<Activity> list);

    List<Activity> selectSelectedActivities(String[] id);

    Activity selectActivityForDetailById(String id);

    List<Activity> queryActivityForDetailByClueId(String id);

    List<Activity> queryActivityForDetailByNameClueId(Map<String, Object> map);

    List<Activity> queryActivityForDetailByIds(String[] activityId);

    List<Activity> queryActivityForConvertByNameClueId(Map<String, Object> map);
}
