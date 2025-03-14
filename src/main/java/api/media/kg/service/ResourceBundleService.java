package api.media.kg.service;

import api.media.kg.config.ResourceBundleConfig;
import api.media.kg.enums.AppLanguage;
import org.springframework.stereotype.Service;

import java.util.Locale;

@Service
public class ResourceBundleService {
    private final ResourceBundleConfig bundleMessage;

    public ResourceBundleService(ResourceBundleConfig bundleMessage) {
        this.bundleMessage = bundleMessage;
    }

    public String getMessage(String code, AppLanguage lang) {
        return bundleMessage.messageSource().getMessage(code, null, new Locale(lang.name()));
    }
}
