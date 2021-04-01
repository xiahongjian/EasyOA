package tech.hongjian.oa.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.flowable.bpmn.converter.BpmnXMLConverter;
import org.flowable.bpmn.model.Process;
import org.flowable.bpmn.model.*;
import org.flowable.cmmn.model.CmmnModel;
import org.flowable.editor.language.json.converter.BpmnJsonConverter;
import org.flowable.engine.RepositoryService;
import org.flowable.engine.repository.ProcessDefinition;
import org.flowable.image.impl.DefaultProcessDiagramGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tech.hongjian.oa.entity.Model;
import tech.hongjian.oa.exception.CommonServiceException;
import tech.hongjian.oa.service.ModelService;
import tech.hongjian.oa.service.ProcessResourceService;
import tech.hongjian.oa.util.ImageGenerator;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * Created by xiahongjian on 2021/3/28.
 */
@Slf4j
@Service
public class ProcessResourceServiceImpl implements ProcessResourceService {
    private static float THUMBNAIL_WIDTH = 300f;
    protected BpmnXMLConverter bpmnXmlConverter = new BpmnXMLConverter();
    protected BpmnJsonConverter bpmnJsonConverter = new BpmnJsonConverter();
    protected ObjectMapper objectMapper = new ObjectMapper();

    public static final String DEFAULT_FONT = "宋体";
    public static final String DEFAULT_IMAGE_TYPE = "png";


    @Setter(onMethod_ = {@Autowired})
    private ModelService modelService;

    @Setter(onMethod_ = {@Autowired})
    private RepositoryService repositoryService;


    @Override
    public byte[] generateThumbnailImage(Model model) {
        BpmnModel bpmnModel = modelService.getBpmnModel(model);

        double scaleFactor = 1.0;
        GraphicInfo diagramInfo = calculateDiagramSize(bpmnModel);
        if (diagramInfo.getWidth() > THUMBNAIL_WIDTH) {
            scaleFactor = diagramInfo.getWidth() / THUMBNAIL_WIDTH;
            scaleDiagram(bpmnModel, scaleFactor);
        }

        BufferedImage modelImage = ImageGenerator.createImage(bpmnModel, scaleFactor);
        if (modelImage != null) {
            return ImageGenerator.createByteArrayForImage(modelImage, "png");
        }
        return null;
    }

    @Override
    public byte[] generateProcessImage(Model model) {
        BpmnModel bpmnModel = modelService.getBpmnModel(model);
        return generateProcessImage(bpmnModel);
    }

    @Override
    public byte[] generateProcessImage(String procDefId) {
        ProcessDefinition definition = repositoryService.createProcessDefinitionQuery().processDefinitionId(procDefId).singleResult();
        if (definition == null) {
            throw new CommonServiceException("为找到ID为[" + procDefId + "]的流程定义。");
        }
        BpmnModel bpmnModel = repositoryService.getBpmnModel(procDefId);
        return generateProcessImage(bpmnModel);
    }

    private byte[] generateProcessImage(BpmnModel bpmnModel) {
        DefaultProcessDiagramGenerator diagramGenerator = new DefaultProcessDiagramGenerator();
        BufferedImage bufferedImage = diagramGenerator.generateImage(bpmnModel, DEFAULT_IMAGE_TYPE, Collections.emptyList(),
                Collections.emptyList(), DEFAULT_FONT, DEFAULT_FONT, DEFAULT_FONT,
                ClassLoader.getSystemClassLoader(), 1.0, true);
        return ImageGenerator.createByteArrayForImage(bufferedImage, DEFAULT_IMAGE_TYPE);
    }

    protected GraphicInfo calculateDiagramSize(BpmnModel bpmnModel) {
        GraphicInfo diagramInfo = new GraphicInfo();

        for (Pool pool : bpmnModel.getPools()) {
            GraphicInfo graphicInfo = bpmnModel.getGraphicInfo(pool.getId());
            double elementMaxX = graphicInfo.getX() + graphicInfo.getWidth();
            double elementMaxY = graphicInfo.getY() + graphicInfo.getHeight();

            if (elementMaxX > diagramInfo.getWidth()) {
                diagramInfo.setWidth(elementMaxX);
            }
            if (elementMaxY > diagramInfo.getHeight()) {
                diagramInfo.setHeight(elementMaxY);
            }
        }

        for (Process process : bpmnModel.getProcesses()) {
            calculateWidthForFlowElements(process.getFlowElements(), bpmnModel, diagramInfo);
            calculateWidthForArtifacts(process.getArtifacts(), bpmnModel, diagramInfo);
        }
        return diagramInfo;
    }

    protected void scaleDiagram(BpmnModel bpmnModel, double scaleFactor) {
        for (Pool pool : bpmnModel.getPools()) {
            GraphicInfo graphicInfo = bpmnModel.getGraphicInfo(pool.getId());
            scaleGraphicInfo(graphicInfo, scaleFactor);
        }

        for (Process process : bpmnModel.getProcesses()) {
            scaleFlowElements(process.getFlowElements(), bpmnModel, scaleFactor);
            scaleArtifacts(process.getArtifacts(), bpmnModel, scaleFactor);
            for (Lane lane : process.getLanes()) {
                scaleGraphicInfo(bpmnModel.getGraphicInfo(lane.getId()), scaleFactor);
            }
        }
    }

    protected void calculateWidthForFlowElements(Collection<FlowElement> elementList, BpmnModel bpmnModel, GraphicInfo diagramInfo) {
        for (FlowElement flowElement : elementList) {
            List<GraphicInfo> graphicInfoList = new ArrayList<>();
            if (flowElement instanceof SequenceFlow) {
                List<GraphicInfo> flowGraphics = bpmnModel.getFlowLocationGraphicInfo(flowElement.getId());
                if (flowGraphics != null && flowGraphics.size() > 0) {
                    graphicInfoList.addAll(flowGraphics);
                }
            } else {
                GraphicInfo graphicInfo = bpmnModel.getGraphicInfo(flowElement.getId());
                if (graphicInfo != null) {
                    graphicInfoList.add(graphicInfo);
                }
            }

            processGraphicInfoList(graphicInfoList, diagramInfo);
        }
    }

    protected void calculateWidthForArtifacts(Collection<Artifact> artifactList, BpmnModel bpmnModel, GraphicInfo diagramInfo) {
        for (Artifact artifact : artifactList) {
            List<GraphicInfo> graphicInfoList = new ArrayList<>();
            if (artifact instanceof Association) {
                graphicInfoList.addAll(bpmnModel.getFlowLocationGraphicInfo(artifact.getId()));
            } else {
                graphicInfoList.add(bpmnModel.getGraphicInfo(artifact.getId()));
            }

            processGraphicInfoList(graphicInfoList, diagramInfo);
        }
    }

    protected void processGraphicInfoList(List<GraphicInfo> graphicInfoList, GraphicInfo diagramInfo) {
        for (GraphicInfo graphicInfo : graphicInfoList) {
            double elementMaxX = graphicInfo.getX() + graphicInfo.getWidth();
            double elementMaxY = graphicInfo.getY() + graphicInfo.getHeight();

            if (elementMaxX > diagramInfo.getWidth()) {
                diagramInfo.setWidth(elementMaxX);
            }
            if (elementMaxY > diagramInfo.getHeight()) {
                diagramInfo.setHeight(elementMaxY);
            }
        }
    }

    protected void scaleFlowElements(Collection<FlowElement> elementList, BpmnModel bpmnModel, double scaleFactor) {
        for (FlowElement flowElement : elementList) {
            List<GraphicInfo> graphicInfoList = new ArrayList<>();
            if (flowElement instanceof SequenceFlow) {
                List<GraphicInfo> flowList = bpmnModel.getFlowLocationGraphicInfo(flowElement.getId());
                if (flowList != null) {
                    graphicInfoList.addAll(flowList);
                }

                // no graphic info for Data Objects
            } else if (!DataObject.class.isInstance(flowElement)) {
                graphicInfoList.add(bpmnModel.getGraphicInfo(flowElement.getId()));
            }

            scaleGraphicInfoList(graphicInfoList, scaleFactor);

            if (flowElement instanceof SubProcess) {
                SubProcess subProcess = (SubProcess) flowElement;
                scaleFlowElements(subProcess.getFlowElements(), bpmnModel, scaleFactor);
            }
        }
    }

    protected void scaleArtifacts(Collection<Artifact> artifactList, BpmnModel bpmnModel, double scaleFactor) {
        for (Artifact artifact : artifactList) {
            List<GraphicInfo> graphicInfoList = new ArrayList<>();
            if (artifact instanceof Association) {
                List<GraphicInfo> flowList = bpmnModel.getFlowLocationGraphicInfo(artifact.getId());
                if (flowList != null) {
                    graphicInfoList.addAll(flowList);
                }
            } else {
                graphicInfoList.add(bpmnModel.getGraphicInfo(artifact.getId()));
            }

            scaleGraphicInfoList(graphicInfoList, scaleFactor);
        }
    }


    protected void scaleAssociations(List<org.flowable.cmmn.model.Association> associationList, CmmnModel cmmnModel, double scaleFactor) {
        for (org.flowable.cmmn.model.Association association : associationList) {
            List<org.flowable.cmmn.model.GraphicInfo> flowList = cmmnModel.getFlowLocationGraphicInfo(association.getId());
            scaleCmmnGraphicInfoList(flowList, scaleFactor);
        }
    }

    protected void scaleGraphicInfoList(List<GraphicInfo> graphicInfoList, double scaleFactor) {
        for (GraphicInfo graphicInfo : graphicInfoList) {
            scaleGraphicInfo(graphicInfo, scaleFactor);
        }
    }

    protected void scaleGraphicInfo(GraphicInfo graphicInfo, double scaleFactor) {
        graphicInfo.setX(graphicInfo.getX() / scaleFactor);
        graphicInfo.setY(graphicInfo.getY() / scaleFactor);
        graphicInfo.setWidth(graphicInfo.getWidth() / scaleFactor);
        graphicInfo.setHeight(graphicInfo.getHeight() / scaleFactor);
    }

    protected void scaleCmmnGraphicInfoList(List<org.flowable.cmmn.model.GraphicInfo> graphicInfoList, double scaleFactor) {
        if (graphicInfoList != null) {
            for (org.flowable.cmmn.model.GraphicInfo graphicInfo : graphicInfoList) {
                scaleCmmnGraphicInfo(graphicInfo, scaleFactor);
            }
        }
    }

    protected void scaleCmmnGraphicInfo(org.flowable.cmmn.model.GraphicInfo graphicInfo, double scaleFactor) {
        graphicInfo.setX(graphicInfo.getX() / scaleFactor);
        graphicInfo.setY(graphicInfo.getY() / scaleFactor);
        graphicInfo.setWidth(graphicInfo.getWidth() / scaleFactor);
        graphicInfo.setHeight(graphicInfo.getHeight() / scaleFactor);
    }

}
