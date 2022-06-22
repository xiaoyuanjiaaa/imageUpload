package com.example.imageuploaddemo.controller;

import cn.hutool.extra.servlet.ServletUtil;
import com.example.imageuploaddemo.config.ServerConfig;
import com.example.imageuploaddemo.utils.DateUtils;
import com.example.imageuploaddemo.utils.IdUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

@RestController
public class Upload {
    @Value("${profile}")
    private String path;
    @Autowired
    private ServerConfig serverConfig;
    private static final Logger log = LoggerFactory.getLogger(Upload.class);
    @PostMapping(value="/common/upload")
    public String uploadFile(@RequestPart("file") MultipartFile file) throws Exception
    {

            int fileNamelength = file.getOriginalFilename().length();

            String fileName = file.getOriginalFilename();
            log.info("filename:=====" + fileName);
            String extension = FilenameUtils.getExtension(file.getOriginalFilename());
            log.info("extension:=====" + extension);
            fileName =IdUtils.fastUUID() + "." + extension;
            log.info("fileName:=====" + fileName);

        File desc = new File(path + File.separator + fileName);

        if (!desc.exists())
        {
            if (!desc.getParentFile().exists())
            {
                desc.getParentFile().mkdirs();
            }
        }
            log.info("desc======" + desc);
            file.transferTo(desc);
        int dirLastIndex = path.length() + 1;
        log.info("fileName====" + fileName);
        String currentDir = StringUtils.substring(path, dirLastIndex);
        String pathFileName = currentDir + "/" + fileName;
        log.info("pathFileName====" + pathFileName);
            log.info(pathFileName+"========");
            return serverConfig.getUrl() + pathFileName;
    }
    @RequestMapping(method = RequestMethod.GET, value = "/{filename:.+}")
    public byte[] getFile(@PathVariable String filename) throws FileNotFoundException, IOException {

        File file = new File(path + filename);
        FileInputStream inputStream = new FileInputStream(file);
        byte[] bytes = new byte[inputStream.available()];
        inputStream.read(bytes, 0, inputStream.available());
        return bytes;
    }
}
