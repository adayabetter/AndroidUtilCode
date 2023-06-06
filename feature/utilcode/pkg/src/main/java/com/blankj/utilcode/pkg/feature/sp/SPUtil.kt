package com.blankj.utilcode.pkg.feature.span
import android.content.Context
import android.content.SharedPreferences
import com.aliexpress.service.app.ApplicationContext

/**
 * 工具类：
 * 每日累计x次，以天为维度（比如某个弹窗，1天出现3次）
 */
object SPUtil {
    private const val SP_KEY_ADD_ITEM = "spAddItem"
    private const val SP_CONTENT_SPLIT = "|"

    private val prefs : SharedPreferences by lazy { ApplicationContext.getContext().getSharedPreferences("channel-sp", Context.MODE_PRIVATE) }

    /**
     * 凑单卡出现频率为每日累计x次，x在gop配置，以天为维度
     * 存储格式
     * key: "spAddItem"
     * value: "2023-06-05|1" “|”前面日期，后面次数
     */
    fun getInsertCount(): Int {
        val spContent = SPUtil.getString(SP_KEY_ADD_ITEM, "")
        if (!TextUtils.isEmpty(spContent)) {
            val dateAndCount = spContent?.split(SP_CONTENT_SPLIT)
            if (!dateAndCount.isNullOrEmpty() && dateAndCount.size == 2) {
                val currentDate = getDateStr()
                if (TextUtils.equals(currentDate, dateAndCount[0])) {
                    return dateAndCount[1].toIntOrNull() ?: 0
                }
            }
            return 0
        }
        return 0
    }

    fun setInsertCount(count: Int) {
        val dateAndCount = StringBuilder()
        dateAndCount.append(getDateStr()).append(SP_CONTENT_SPLIT).append(count)
        SPUtil.putString(SP_KEY_ADD_ITEM, dateAndCount.toString())
    }

    private fun getDateStr(): String {
        val format = SimpleDateFormat("yyyy-MM-dd", Locale.US)
        return format.format(Date())
    }

    fun putString(key: String, value: String) {
        with(prefs.edit()) {
            putString(key, value)
        }.apply()
    }

    fun getString(key: String, defaultValue: String): String? {
        return with(prefs) {
            getString(key, defaultValue)
        }
    }

}}