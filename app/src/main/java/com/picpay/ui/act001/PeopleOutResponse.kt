package com.picpay.ui.act001

import android.os.Bundle
import com.picpay.entity.People

/*
* Created By neomatrix on 2019-07-12
*/
sealed class PeopleOutResponse {

    data class Peoples(
        var peoples: ArrayList<People>
    ) : PeopleOutResponse()

    data class NavigationCreditCardAdd(
        var bundle: Bundle
    ) : PeopleOutResponse()

    data class NavigationTransfer(
        var bundle: Bundle
    ) : PeopleOutResponse()

}