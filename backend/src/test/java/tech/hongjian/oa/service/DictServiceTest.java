package tech.hongjian.oa.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.core.metadata.IPage;

import tech.hongjian.oa.TestCaseBase;
import tech.hongjian.oa.entity.Dict;
import tech.hongjian.oa.entity.enums.Status;
import tech.hongjian.oa.util.JSONUtil;

public class DictServiceTest extends TestCaseBase {

    @Autowired
    private DictService dictService;

    @Test
    @Transactional
    public void testGetDict() {
        IPage<Dict> dict = dictService.getDict("性别", null, null, 0, 10);
        System.out.println(JSONUtil.toJSON(dict));

        IPage<Dict> res = dictService.getDict("性别", null, Status.NORMAL, 0, 10);
        System.out.println(JSONUtil.toJSON(res));
    }
}
