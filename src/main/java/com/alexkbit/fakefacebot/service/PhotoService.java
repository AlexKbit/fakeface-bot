package com.alexkbit.fakefacebot.service;

import com.alexkbit.fakefacebot.model.PhotoType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
public class PhotoService {

    private Map<String, String> cache = new HashMap<>();

    public SendPhoto getPhoto(String name, PhotoType type) {
        String key = type.getPath() + name;
        SendPhoto photo = new SendPhoto();
        if (cache.containsKey(key)) {
            photo.setPhoto(cache.get(key));
            return photo;
        }
        try {
            File file = ResourceUtils.getFile(key);
            photo.setPhoto(file);
            return photo;
        } catch (FileNotFoundException e) {
            log.error("Error load photo with name {}", e, name);
        }
        return null;
    }

    public void updateCache(String name, PhotoType type, String photoId) {
        String key = type.getPath() + name;
        if (cache.containsKey(key)) {
            return;
        }
        cache.put(key, photoId);
    }
}
