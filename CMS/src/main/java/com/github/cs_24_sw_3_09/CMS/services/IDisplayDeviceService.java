package com.github.cs_24_sw_3_09.CMS.services;

import org.springframework.http.ResponseEntity;

public interface IDisplayDeviceService {
    public ResponseEntity<Object> deleteDisplayDevice(int id);
}
