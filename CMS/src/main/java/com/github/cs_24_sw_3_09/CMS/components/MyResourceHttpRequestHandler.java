package com.github.cs_24_sw_3_09.CMS.components;

import java.io.File;
import java.io.IOException;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.resource.ResourceHttpRequestHandler;

import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;

@Component
final class MyResourceHttpRequestHandler extends ResourceHttpRequestHandler {

    private final static String ATTR_FILE = MyResourceHttpRequestHandler.class.getName() + ".file";

    @Override
    protected Resource getResource(HttpServletRequest request) throws IOException {

        final File file = (File) request.getAttribute(ATTR_FILE);
        return new FileSystemResource(file);
    }
}