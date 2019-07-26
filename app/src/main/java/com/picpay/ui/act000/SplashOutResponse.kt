package com.picpay.ui.act000

import android.os.Bundle
import com.picpay.tools.UIStatusType

/*
* Created By neomatrix on 2019-07-12
*/
sealed class SplashOutResponse {

    data class Navigation(
        var bundle: Bundle
    ) : SplashOutResponse()

    data class Failure(
        var status: UIStatusType
    ) : SplashOutResponse()

}