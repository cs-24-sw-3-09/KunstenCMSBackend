package com.github.cs_24_sw_3_09.CMS;

import com.github.cs_24_sw_3_09.CMS.model.dto.DisplayDeviceDto;
import com.github.cs_24_sw_3_09.CMS.model.dto.VisualMediaDto;
import com.github.cs_24_sw_3_09.CMS.model.entities.DisplayDeviceEntity;
import com.github.cs_24_sw_3_09.CMS.model.entities.VisualMediaEntity;

public class TestDataUtil {

    public static DisplayDeviceDto createDisplayDeviceDto() {

        return DisplayDeviceDto.builder()
                .displayOrientation("Horizontal")
                .connectedState(false)
                .location("Esbjerg")
                .model("Samsung")
                .name("Skærm Esbjerg")
                .resolution("1920x1080")
                .build();


    }

    public static DisplayDeviceEntity createDisplayDeviceEntity() {

        return DisplayDeviceEntity.builder()
                .displayOrientation("Horizontal")
                .connectedState(false)
                .location("Esbjerg")
                .model("Samsung")
                .name("Skærm Esbjerg")
                .resolution("1920x1080")
                .build();
    }

    public static VisualMediaDto createVisualMediaDto() {
        return VisualMediaDto.builder()
                .description("dkaoidkao test descpt")
                .fileType("jpg")
                .lastDateModified("30/10/2003")
                .location("/djao/dhau")
                .name("Billede navn")
                .build();
    }

    public static VisualMediaEntity createVisualMediaEntity() {
        return VisualMediaEntity.builder()
                .description("dkaoidkao test descpt")
                .fileType("jpg")
                .lastDateModified("30/10/2003")
                .location("/djao/dhau")
                .name("Billede navn")
                .build();
    }


}
