package com.xjx.workbench.service.impl;

import com.xjx.workbench.dao.ActivityMapper;
import com.xjx.workbench.domain.Activity;
import com.xjx.workbench.service.ActivityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service("activityService")
public class ActivityServiceImpl implements ActivityService {
    @Autowired
    ActivityMapper activityMapper;

    @Override
    public int saveCreateActivity(Activity activity) {
        return activityMapper.insertActivity(activity);
    }

    @Override
    public List<Activity> selectActivityByConditionForPage(Map map) {
        return activityMapper.selectActivityByConditionForPage(map);
    }

    @Override
    public int selectCountByConditionForPage(Map map) {
        return activityMapper.selectCountByConditionForPage(map);
    }

    @Override
    public int deleteActivityByIds(String[] strings) {
        return activityMapper.deleteActivityByIds(strings);
    }

    @Override
    public Activity selectActivityById(String id) {
        return activityMapper.selectActivityById(id);
    }

    @Override
    public int updateActivity(Activity activity) {
        return activityMapper.updateActivity(activity);
    }

    @Override
    public List<Activity> selectAllActivities() {
        return activityMapper.selevtAllActivities();
    }

    @Override
    public int saveCreateActivityByList(List<Activity> list) {
        return activityMapper.insertActivityByList(list);
    }

    @Override
    public List<Activity> selectSelectedActivities(String[] id) {
        return activityMapper.selectSelectedActivities(id);
    }
}
