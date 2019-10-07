package com.kslimweb.one2many.utils;

import com.kslimweb.one2many.BuildConfig;

public class BuildTypeUtil {
    static public Boolean isReleaseBuild() {
        return BuildConfig.BUILD_VARIANT.equals("release");
    }
}
