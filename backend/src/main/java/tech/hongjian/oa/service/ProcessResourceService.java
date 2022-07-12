package tech.hongjian.oa.service;

import tech.hongjian.oa.entity.Model;

/**
 * Created by xiahongjian on 2021/3/28.
 */
public interface ProcessResourceService {
    byte[] generateThumbnailImage(Model model);

    byte[] generateProcessImage(Model model);

    byte[] generateProcessImage(String procDefId);

    byte[] generateXmlData(Model model);

    byte[] generateXmlData(String procDefId);
}
