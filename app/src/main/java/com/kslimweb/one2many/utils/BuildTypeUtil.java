package com.kslimweb.one2many.utils;

import com.kslimweb.one2many.BuildConfig;

public class BuildTypeUtil {
    static public Boolean isReleaseMode() {
        return !BuildConfig.DEBUG;
    }
}
