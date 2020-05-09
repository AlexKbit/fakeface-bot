package com.alexkbit.fakefacebot.service;

import com.alexkbit.fakefacebot.model.PhotoType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

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
            InputStream is = new ClassPathResource(key).getInputStream();
            photo.setPhoto(UUID.randomUUID().toString(), is);
            return photo;
        } catch (IOException e) {
            log.error("Error load photo with name {}", e, name);
        }
        return null;
    }

    public InputStream loadPhoto(String name, PhotoType type) {
        String key = type.getPath() + name;
        try {
            InputStream is = new ClassPathResource(key).getInputStream();
            return is;
        } catch (IOException e) {
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
