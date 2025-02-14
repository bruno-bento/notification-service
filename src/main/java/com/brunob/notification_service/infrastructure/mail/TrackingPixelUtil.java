package com.brunob.notification_service.infrastructure.mail;

import org.springframework.stereotype.Component;

import java.util.Base64;

@Component
public class TrackingPixelUtil {
    private static final byte[] TRANSPARENT_GIF = Base64.getDecoder().decode(
            "R0lGODlhAQABAIAAAAAAAP///yH5BAEAAAAALAAAAAABAAEAAAIBRAA7"
    );

    public byte[] getTrackingPixel() {
        return TRANSPARENT_GIF;
    }
}