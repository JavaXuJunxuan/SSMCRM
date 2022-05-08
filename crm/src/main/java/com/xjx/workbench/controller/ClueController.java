package com.xjx.workbench.controller;

import com.xjx.commons.constants.Constants;
import com.xjx.commons.domain.ReturnObject;
import com.xjx.commons.utils.DateUtils;
import com.xjx.commons.utils.UUIDUtils;
import com.xjx.settings.domain.DictionaryValue;
import com.xjx.settings.domain.User;
import com.xjx.settings.service.DictionaryValueService;
import com.xjx.settings.service.UserService;
import com.xjx.workbench.domain.Activity;
import com.xjx.workbench.domain.Clue;
import com.xjx.workbench.domain.ClueActivityRelation;
import com.xjx.workbench.domain.ClueRemark;
import com.xjx.workbench.service.ActivityService;
import com.xjx.workbench.service.ClueActivityRelationService;
import com.xjx.workbench.service.ClueRemarkService;
import com.xjx.workbench.service.ClueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.*;

@Controller
public class ClueController {
    @Autowired
    private UserService userService;
    @Autowired
    private DictionaryValueService dictionaryValueService;
    @Autowired
    private ClueService clueService;
    @Autowired
    private ClueRemarkService clueRemarkService;
    @Autowired
    private ActivityService activityService;
    @Autowired
    private ClueActivityRelationService clueActivityRelationService;


    @RequestMapping("/workbench/clue/index.do")
    public String index(HttpServletRequest request){
        List<User> userList = userService.queryAllUsers();
        List<DictionaryValue> appellationList = dictionaryValueService.selectDictionaryValueByTypeCode("appellation");
        List<DictionaryValue> clueStateList = dictionaryValueService.selectDictionaryValueByTypeCode("clueState");
        List<DictionaryValue> sourceList = dictionaryValueService.selectDictionaryValueByTypeCode("source");
        request.setAttribute("userList",userList);
        request.setAttribute("appellationList",appellationList);
        request.setAttribute("clueStateList",clueStateList);
        request.setAttribute("sourceList",sourceList);
        return "workbench/clue/index";
    }

    @RequestMapping("/workbench/clue/saveCreateClue.do")
    @ResponseBody
    public Object saveCreateClue(Clue clue, HttpSession session){
        User user=(User)session.getAttribute(Constants.SESSION_USER);
        //封装参数
        clue.setId(UUIDUtils.getUUID());
        clue.setCreateTime(DateUtils.formateDateTime(new Date()));
        clue.setCreateBy(user.getId());
        ReturnObject returnObject=new ReturnObject();
        try {
            //调用service层方法，保存创建的线索
            int count = clueService.saveCreateClue(clue);
            if(count>0){
                returnObject.setCode(Constants.RETURN_OBJECT_CODE_SUCCESS);
            }else{
                returnObject.setCode(Constants.RETURN_OBJECT_CODE_FAIL);
                returnObject.setMessage("系统忙，请稍后重试....");
            }
        }catch (Exception e){
            e.printStackTrace();
            returnObject.setCode(Constants.RETURN_OBJECT_CODE_FAIL);
            returnObject.setMessage("系统忙，请稍后重试....");
        }
        return returnObject;
    }
    @RequestMapping("/workbench/clue/queryClueByConditionForPage.do")
    @ResponseBody
    public Object queryClueByConditionForPage(String name, String company, String mphone, String source,
                                              String owner, String phone, String state, int pageNo, int pageSize){
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("fullname", name);
        map.put("company", company);
        map.put("mphone", mphone);
        map.put("source", source);
        map.put("owner", owner);
        map.put("phone", phone);
        map.put("state", state);
        map.put("beginNo", (pageNo - 1) * pageSize);
        map.put("pageSize", pageSize);
        List<Clue> clueList = clueService.selectActivityByConditionForPage(map);
        for (Clue clue : clueList){
            if (clue.getAppellation() == null){
                clue.setAppellation("");
            }
            if (clue.getSource() == null){
                clue.setSource("");
            }
            if (clue.getState() == null){
                clue.setState("");
            }
        }
        int count = clueService.selectCountByConditionForPage(map);
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("clueList", clueList);
        result.put("totalRows", count);
        return result;
    }

    @RequestMapping("/workbench/clue/detailClue.do")
    public String detailClue(String id,HttpServletRequest request){
        //调用service层方法，查询数据
        Clue clue = clueService.queryClueForDetailById(id);
        List<ClueRemark> remarkList = clueRemarkService.queryClueRemarkForDetailByClueId(id);
        List<Activity> activityList = activityService.queryActivityForDetailByClueId(id);
        //把数据保存到request中
        request.setAttribute("clue",clue);
        request.setAttribute("remarkList",remarkList);
        request.setAttribute("activityList",activityList);
        //请求转发
        return "workbench/clue/detail";
    }

    @RequestMapping("/workbench/clue/queryClueById.do")
    @ResponseBody
    public Object detailClue(String id){
        Clue clue = clueService.selectClueById(id);
        return clue;
    }
    @RequestMapping("/workbench/clue/editClue.do")
    @ResponseBody
    public Object editClue(Clue clue,HttpSession session){
        ReturnObject returnObject = new ReturnObject();
        User user = (User) session.getAttribute(Constants.SESSION_USER);
        clue.setEditBy(user.getName());
        clue.setEditTime(DateUtils.formateDateTime(new Date()));
        try {
            int count = clueService.saveEditClue(clue);
            if (count > 0){
                returnObject.setCode(Constants.RETURN_OBJECT_CODE_SUCCESS);
            }else {
                returnObject.setCode(Constants.RETURN_OBJECT_CODE_FAIL);
                returnObject.setMessage("系统繁忙，请稍后重试");
            }
        }catch (Exception e){
            e.printStackTrace();
            returnObject.setCode(Constants.RETURN_OBJECT_CODE_FAIL);
            returnObject.setMessage("系统繁忙，请稍后重试");
        }
        return returnObject;
    }
    @RequestMapping("/workbench/clue/deleteClueById.do")
    @ResponseBody
    public Object deleteClueById(String[] id) {
        ReturnObject returnObject = new ReturnObject();
        try {
            int count = clueService.deleteClueById(id);
            if (count > 0) {
                returnObject.setCode(Constants.RETURN_OBJECT_CODE_SUCCESS);
            } else {
                returnObject.setCode(Constants.RETURN_OBJECT_CODE_FAIL);
                returnObject.setMessage("系统繁忙，请稍后重试");
            }
        } catch (Exception e) {
            e.printStackTrace();
            returnObject.setCode(Constants.RETURN_OBJECT_CODE_FAIL);
            returnObject.setMessage("系统繁忙，请稍后重试");
        }
        return returnObject;
    }

    @RequestMapping("/workbench/clue/queryActivityForDetailByNameClueId.do")
    @ResponseBody
    public Object queryActivityForDetailByNameClueId(String activityName,String clueId){
        //封装参数
        Map<String,Object> map=new HashMap<>();
        map.put("activityName",activityName);
        map.put("clueId",clueId);
        //调用service层方法，查询市场活动
        List<Activity> activityList = activityService.queryActivityForDetailByNameClueId(map);
        //根据查询结果，返回响应信息
        return activityList;
    }

    @RequestMapping("/workbench/clue/saveBund.do")
    @ResponseBody
    public Object saveBund(String[] activityId,String clueId){
        //封装参数
        ClueActivityRelation car = null;
        List<ClueActivityRelation> relationList = new ArrayList<>();
        for(String ai:activityId){
            car = new ClueActivityRelation();
            car.setActivityId(ai);
            car.setClueId(clueId);
            car.setId(UUIDUtils.getUUID());
            relationList.add(car);
        }
        ReturnObject returnObject=new ReturnObject();
        try {
            //调用service方法，批量保存线索和市场活动的关联关系
            int count = clueActivityRelationService.saveCreateClueActivityRelationByList(relationList);
            if(count > 0){
                returnObject.setCode(Constants.RETURN_OBJECT_CODE_SUCCESS);
                List<Activity> activityList=activityService.queryActivityForDetailByIds(activityId);
                returnObject.setRtn(activityList);
            }else{
                returnObject.setCode(Constants.RETURN_OBJECT_CODE_FAIL);
                returnObject.setMessage("系统忙，请稍后重试....");
            }
        }catch (Exception e){
            e.printStackTrace();
            returnObject.setCode(Constants.RETURN_OBJECT_CODE_FAIL);
            returnObject.setMessage("系统忙，请稍后重试....");
        }
        return returnObject;
    }
    @RequestMapping("/workbench/clue/saveUnbund.do")
    public @ResponseBody Object saveUnbund(ClueActivityRelation relation){
        ReturnObject returnObject=new ReturnObject();
        try {
            //调用service层方法，删除线索和市场活动的关联关系
            int count = clueActivityRelationService.deleteClueActivityRelationByClueIdActivityId(relation);
            if(count > 0){
                returnObject.setCode(Constants.RETURN_OBJECT_CODE_SUCCESS);
            }else{
                returnObject.setCode(Constants.RETURN_OBJECT_CODE_FAIL);
                returnObject.setMessage("系统忙，请稍后重试....");
            }
        }catch (Exception e){
            e.printStackTrace();
            returnObject.setCode(Constants.RETURN_OBJECT_CODE_FAIL);
            returnObject.setMessage("系统忙，请稍后重试....");
        }
        return returnObject;
    }
    @RequestMapping("/workbench/clue/toConvert.do")
    public String toConvert(String id,HttpServletRequest request){
        //调用service层方法，查询线索的明细信息
        Clue clue = clueService.queryClueForDetailById(id);
        List<DictionaryValue> stageList = dictionaryValueService.selectDictionaryValueByTypeCode("stage");
        //把数据保存到request中
        request.setAttribute("clue",clue);
        request.setAttribute("stageList",stageList);
        //请求转发
        return "workbench/clue/convert";
    }
    @RequestMapping("/workbench/clue/queryActivityForConvertByNameClueId.do")
    @ResponseBody
    public  Object queryActivityForConvertByNameClueId(String activityName,String clueId){
        //封装参数
        Map<String,Object> map=new HashMap<>();
        map.put("activityName",activityName);
        map.put("clueId",clueId);
        //调用service层方法，查询市场活动
        List<Activity> activityList = activityService.queryActivityForConvertByNameClueId(map);
        //根据查询结果，返回响应信息
        return activityList;
    }

    @RequestMapping("/workbench/clue/convertClue.do")
    @ResponseBody
    public Object convertClue(String clueId,String money,String name,String expectedDate,String stage,String activityId,String isCreateTran,HttpSession session){
        ReturnObject returnObject = new ReturnObject();
        Map<String,Object> map = new HashMap<>();
        map.put("clueId",clueId);
        map.put("money",money);
        map.put("name",name);
        map.put("expectedDate",expectedDate);
        map.put("stage",stage);
        map.put("activityId",activityId);
        map.put("isCreateTran",isCreateTran);
        map.put(Constants.SESSION_USER,session.getAttribute(Constants.SESSION_USER));
        try {
            clueService.saveConvertClue(map);
            returnObject.setCode(Constants.RETURN_OBJECT_CODE_SUCCESS);
        }catch (Exception e){
            e.printStackTrace();
            returnObject.setCode(Constants.RETURN_OBJECT_CODE_FAIL);
            returnObject.setMessage("系统忙，请稍后重试....");
        }
        return returnObject;
    }
}
