package com.wl.listener;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.wl.entity.User;
import com.wl.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.ListUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
public class ExcelListener extends AnalysisEventListener<User> {

    //Listener不能被Spring管理 需要手动用构造函数赋值
    private UserService userService;
    private List<User> list =  new ArrayList<>();

    public ExcelListener() {
    }

    public ExcelListener(UserService userService) {
        this.userService = userService;
    }

    //一行一行读内容
    @Override
    public void invoke(User user, AnalysisContext analysisContext) {
        log.info(user.toString());
        list.add(user);
    }

    //读表头
    public void invokeHeadMap(Map<Integer, String> headMap, AnalysisContext context) {
           log.info("表头："+headMap);
    }

    //读取完成之后
    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {
        log.info("共{}条数据",list.size());
        userService.saveBatch(list);
        log.info("存储成功");
    }
}
