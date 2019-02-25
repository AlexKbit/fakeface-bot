package com.alexkbit.fakefacebot.service;

import com.alexkbit.fakefacebot.model.PhotoType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.FileNotFoundException;

@Slf4j
@Service
public class PhotoService {

    public File getPhoto(String name, PhotoType type) {
        try {
            return ResourceUtils.getFile(type.getPath() + name);
        } catch (FileNotFoundException e) {
            log.error("Error load photo with name {}", e, name);
        }
        return null;
    }
}
