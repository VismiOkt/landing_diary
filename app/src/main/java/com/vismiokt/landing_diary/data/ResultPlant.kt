package com.vismiokt.landing_diary.data

import androidx.annotation.StringRes
import com.vismiokt.landing_diary.R

enum class ResultPlant (@StringRes val text: Int) {
    positive (R.string.result_positive),
    negative (R.string.result_negative),
    neutral (R.string.result_neutral),
    unknown (R.string.result_unknown)
}