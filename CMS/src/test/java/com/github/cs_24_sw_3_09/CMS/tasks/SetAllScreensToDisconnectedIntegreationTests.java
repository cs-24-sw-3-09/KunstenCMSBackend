package com.github.cs_24_sw_3_09.CMS.tasks;

package com.github.cs_24_sw_3_09.CMS.tasks;

import com.github.cs_24_sw_3_09.CMS.TestDataUtil;
import com.github.cs_24_sw_3_09.CMS.model.entities.DisplayDeviceEntity;
import com.github.cs_24_sw_3_09.CMS.model.entities.TimeSlotEntity;
import com.github.cs_24_sw_3_09.CMS.services.CleanUpDataBaseService;
import com.github.cs_24_sw_3_09.CMS.services.DisplayDeviceService;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.github.cs_24_sw_3_09.CMS.services.TimeSlotService;

import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@ExtendWith(SpringExtension.class)
@AutoConfigureMockMvc
public class SetAllScreensToDisconnectedIntegreationTests {
    private DisplayDeviceService displayDeviceService;

    @Autowired
    public SetAllScreensToDisconnectedIntegreationTests(DisplayDeviceService displayDeviceServicec) {
        this.displayDeviceService = displayDeviceService;
    }

    // @Test
    // public void newname() throws Exception {
    // DisplayDeviceEntity dd = TestDataUtil.createDisplayDeviceEntity();
    // dd.setConnectedState(true);
    // displayDeviceService.save(dd);

    // // id 1 should be the one just added
    // boolean queryResult = displayDeviceService.connectScreen(1);
    // assertEquals(false, queryResult, "The query failed");
    // queryResult = displayDeviceService.disconnectScreen(1);
    // assertEquals(true, queryResult, "The query failed");

    // DisplayDeviceEntity dd2 = TestDataUtil.createDisplayDeviceEntity();
    // dd2.setConnectedState(false);
    // displayDeviceService.save(dd2);

    // // id 2 should be the one just added
    // queryResult = displayDeviceService.disconnectScreen(2);
    // assertEquals(false, queryResult, "The query failed");
    // }
}
