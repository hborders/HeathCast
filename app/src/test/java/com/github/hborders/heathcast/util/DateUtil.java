package com.github.hborders.heathcast.util;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.Date;

public final class DateUtil {
    private DateUtil() {

    }

    public static Date from(int year, int month, int dayOfMonth) {
        return Date.from(
                ZonedDateTime
                        .of(
                                2019,
                                1,
                                1,
                                0,
                                0,
                                0,
                                0,
                                ZoneOffset
                                        .ofHours(0)
                                        .normalized()
                        )
                        .toInstant()
        );
    }
}
