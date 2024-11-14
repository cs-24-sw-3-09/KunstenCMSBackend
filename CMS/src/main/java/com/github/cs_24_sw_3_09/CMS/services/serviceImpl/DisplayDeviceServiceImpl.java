package com.github.cs_24_sw_3_09.CMS.services.serviceImpl;

import com.github.cs_24_sw_3_09.CMS.dao.IDisplayDeviceDao;
import com.github.cs_24_sw_3_09.CMS.services.IDisplayDeviceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class DisplayDeviceServiceImpl implements IDisplayDeviceService {

    IDisplayDeviceDao displayDeviceDao;

    @Autowired
    public DisplayDeviceServiceImpl(IDisplayDeviceDao displayDeviceDao) {
        this.displayDeviceDao = displayDeviceDao;
    }

    @Override
    public ResponseEntity<Object> deleteDisplayDevice(int id) {
        // Delete from database
        try {
            displayDeviceDao.delete(id);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Got error while deleting: " + e.getMessage());
        }

        return ResponseEntity.status(HttpStatus.OK).body("succesfully deleted");
    }
}
