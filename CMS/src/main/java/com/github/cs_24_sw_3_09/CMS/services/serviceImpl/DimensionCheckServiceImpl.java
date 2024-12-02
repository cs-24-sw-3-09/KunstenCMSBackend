package com.github.cs_24_sw_3_09.CMS.services.serviceImpl;

import com.github.cs_24_sw_3_09.CMS.services.DimensionCheckService;

public class DimensionCheckServiceImpl implements DimensionCheckService {

    @Override
    public Boolean checkDimensions(String a, String b) {
        String[] aArr = a.split("x");
        String[] bArr = b.split("x");
        String aHorizontal = aArr[0], aVertical = aArr[1];
        String bHorizontal = bArr[0], bVertical = bArr[1];
        
        return aHorizontal == bHorizontal && aVertical == bVertical; 
    }
    
}
