package tech.hongjian.oa.util;

import javax.xml.stream.XMLInputFactory;

/**
 * Created by xiahongjian on 2021/3/20.
 */
public class XmlUtil {

    /**
     * 'safe' is here reflecting: http://www.jorambarrez.be/blog/2013/02/19/uploading -a-funny-xml-can-bring-down-your-server/ and
     * http://www.flowable.org/docs/userguide/index.html#advanced.safe.bpmn.xml
     */
    public static XMLInputFactory createSafeXmlInputFactory() {
        XMLInputFactory xif = XMLInputFactory.newInstance();
        if (xif.isPropertySupported(XMLInputFactory.IS_REPLACING_ENTITY_REFERENCES)) {
            xif.setProperty(XMLInputFactory.IS_REPLACING_ENTITY_REFERENCES, false);
        }

        if (xif.isPropertySupported(XMLInputFactory.IS_SUPPORTING_EXTERNAL_ENTITIES)) {
            xif.setProperty(XMLInputFactory.IS_SUPPORTING_EXTERNAL_ENTITIES, false);
        }

        if (xif.isPropertySupported(XMLInputFactory.SUPPORT_DTD)) {
            xif.setProperty(XMLInputFactory.SUPPORT_DTD, false);
        }
        return xif;
    }
}
