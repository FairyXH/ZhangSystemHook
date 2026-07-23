/*
 * This file is created by fankes on 2022/6/4.
 */
package io.github.fairyxh.ZhangSystemHook.bean

import java.io.Serializable

data class AppFiltersBean(
    var name: String = "",
    var type: AppFiltersType = AppFiltersType.ALL
) : Serializable