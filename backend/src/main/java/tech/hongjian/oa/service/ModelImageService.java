package tech.hongjian.oa.service;

import com.fasterxml.jackson.databind.node.ObjectNode;
import tech.hongjian.oa.entity.Model;

import javax.jws.WebParam;

/**
 * Created by xiahongjian on 2021/3/28.
 */
public interface ModelImageService {
    byte[] generateThumbnailImage(Model model);

    byte[] generateProcessImage(Model model);
}
