package com.ad.tibi.lib.interf;

import java.util.List;

/**
 * 权限申请
 */
public interface PermissionListener {
    void onPermission(List<String> permissions);
}
