package com.example.svbookmarket.activities.common

import com.example.svbookmarket.R


object Constants {
    //App
    const val VMTAG = "VMTAG"


    //Intents
    const val SPLASH_INTENT = "splashIntent"

    //Bundles
    const val ITEM = "ITEM_TO_DISPLAY"


    //References

    const val USERS_REF = "accounts"
    const val BOOK_REF = "books"
    const val ADDRESS_REF = "userDeliverAddresses"


    //Fields
    const val NAME = "name"
    const val EMAIL = "email"
    const val PHOTO_URL = "photoUrl"
    const val CREATED_AT = "createdAt"
    const val RATING = "rating"
    const val DEFAULT_ADDRESS_POS = 0   //use to set select position after delete address
    const val DEFAULT_IMG_PLACEHOLDER = R.drawable.bg_button_white


    enum class ACTIVITY {
       PROFILE, SEARCH, CART, CATEGORY, CATEGORY_DETAIL, ONLINE_SERVICE;

        override fun toString(): String {
            return name.toLowerCase().capitalize()
        }
    }
    enum class TRANSACTION{
        COMPLETE,DELIVERING,CANCEL,CONFIRMED, WAITING;
        override fun toString(): String {
            return name.toLowerCase().capitalize()
        }
    }

    enum class CATEGORY{
        CHUNG, HV, VB2, LTCQ, NB, PH;
        override fun toString(): String {
            val result = when(this){
                CHUNG -> "Chung"
                HV -> "Học vụ"
                VB2 -> "Văn bằng 2"
                LTCQ -> "Liên thông chính quy"
                NB -> "Nghỉ/ bù"
                PH -> "Phòng học"
            }
            return result
        }

        fun getShortName(): String{
            val result = when(this){
                CHUNG -> "Chung"
                HV -> "Học vụ"
                VB2 -> "Văn bằng 2"
                LTCQ -> "LTCQ"
                NB -> "Nghỉ/ bù"
                PH -> "Phòng học"
            }
            return result.toUpperCase()
        }
    }
}