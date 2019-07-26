package com.picpay.tools

import java.text.Normalizer

/*
* Created By neomatrix on 2019-07-19
*/
enum class UIStatusType {
    PROGRESS, ERROR, LIST
}


fun String.cleanForSearch(): String {
    return Normalizer.normalize(this, Normalizer.Form.NFD)
        .replace("[^\\p{ASCII}]".toRegex(), "")
        .toLowerCase()
}