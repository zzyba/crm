package com.zybtest.crm.workbench.web.controller;

import com.zybtest.crm.commons.constants.Constants;
import com.zybtest.crm.commons.domain.ReturnObject;
import com.zybtest.crm.commons.util.DateUtils;
import com.zybtest.crm.commons.util.UUIDUtils;
import com.zybtest.crm.settings.domain.User;
import com.zybtest.crm.settings.service.UserService;
import com.zybtest.crm.workbench.domain.Activity;
import com.zybtest.crm.workbench.service.ActivityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class ActivityController {
    @Autowired
    private UserService userService;
    @Autowired
    private ActivityService activityService;

    @RequestMapping("/workbench/activity/index.do")
    public String index(HttpServletRequest request){
        List<User> userLlist = userService.quaryAllUser();
        request.setAttribute("userlist",userLlist);
        return "/workbench/activity/index";
    }

    @RequestMapping("/workbench/activity/saveCreateActivity.do")
    public @ResponseBody Object saveCreateActivity(Activity activity, HttpSession session){
        User user = (User)session.getAttribute(Constants.SESSION_SYSTEMUSER);
        //        封装数据
        activity.setId(UUIDUtils.getUUID());
        activity.setCreateTime(DateUtils.formateDateTime(new Date()));
        activity.setCreateBy(user.getId());

        ReturnObject returnObject=new ReturnObject();
        try {
            int ret = activityService.saveCreateActivity(activity);
            if (ret>0){
                returnObject.setCode(Constants.RETURN_OBJECT_CODE_SUCCEED);
            }else {
                returnObject.setCode(Constants.RETURN_OBJECT_CODE_FAIL);
                returnObject.setMessage("系统忙...请稍后再试");
            }
        }catch (Exception e){
            returnObject.setCode(Constants.RETURN_OBJECT_CODE_FAIL);
            returnObject.setMessage("系统忙...请稍后再试");
        }
        return returnObject;
    }
    @RequestMapping("/workbench/activity/queryActivityByConditionForPage.do")
    public @ResponseBody Object queryActivityByConditionForPage(String name,String owner,String starDate,String endDate,int pageNo,int pageSize){
//        封装数据
        Map<String,Object> map =new HashMap<>();
        map.put("name",name);
        map.put("owner",owner);
        map.put("starDate",starDate);
        map.put("endDate",endDate);
        map.put("pageNo",pageNo);
        map.put("pageSize",pageSize);
        map.put("beginNo",(pageNo-1)*pageSize);
//        调用server
        List<Activity> activities = activityService.queryActivityByConditionForPage(map);
        int totalRaws = activityService.queryCountOfActivityByCondition(map);
//        将得到的数据封装为map，返回后会自动解析为json字符串。
        System.out.println(totalRaws);
        System.out.println(activities.toString());
        Map<String,Object> retmap=new HashMap<>();
        retmap.put("activities",activities);
        retmap.put("totalRaws",totalRaws);
        return retmap;
//      11111
    }
}
