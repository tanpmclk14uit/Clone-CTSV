package com.example.svbookmarket.activities.data

import android.net.Uri
import com.example.svbookmarket.R
import com.example.svbookmarket.activities.model.*

class DataSource {
    fun loadCheckoutCard(): MutableList<CheckoutCard> {
        return mutableListOf(
            CheckoutCard("dsadsa", "name", "adr", "100", "123"),
            CheckoutCard("dsadsa", "name", "adr", "100", "123"),
            CheckoutCard("dsadsa", "name", "adr", "100", "123"),
            CheckoutCard("dsadsa", "name", "adr", "100", "123"),
            CheckoutCard("dsadsa", "name", "adr", "100", "123"),
            CheckoutCard("dsadsa", "name", "adr", "100", "123"),

            )
    }
//
//    fun loadCart(): MutableList<Cart> {
//        return mutableListOf(
//            CartModel("",Uri.parse(""),"name", "name", 1, 10,true),
//            CartModel("",Uri.parse(""),"name", "name", 1, 9, true),
//            CartModel("",Uri.parse(""),"name", "name1",1, 8, true),
//            CartModel("",Uri.parse(""),"name", "name", 1, 12,true),
//            CartModel("",Uri.parse(""),"name", "name", 1, 3, true),
//            CartModel("",Uri.parse(""),"name", "name1",1, 1, true),
//
//            )
//    }

    fun loadAddressCard(): MutableList<AddressModel> {
        return mutableListOf(
            AddressModel(
                "Khuong Duy",
                "0869256174",
                "Ktx khu A, DHQG",
                "Phuong Linh Trung",
                "Quan Thu Duc",
                "Tp.Ho Chi Minh"
            ),
            AddressModel(
                "Khuong Duy",
                "0869256174",
                "Ktx khu A, DHQG",
                "Phuong Linh Trung",
                "Quan Thu Duc",
                "Tp.Ho Chi Minh"
            ),
            AddressModel(
                "Khuong Duy",
                "0869256174",
                "Ktx khu A, DHQG",
                "Phuong Linh Trung",
                "Quan Thu Duc",
                "Tp.Ho Chi Minh"
            ),
        )
    }

    // moved to home viewmodel


    fun loadCategory(): ArrayList<Category> {
        return arrayListOf(
            Category(R.drawable.img_art, "Art"),
            Category(R.drawable.img_fiction, "Fiction"),
            Category(R.drawable.img_comic, "Comic"),
            Category(R.drawable.img_novel, "Novel"),
            Category(R.drawable.img_business, "Business"),
            Category(R.drawable.img_tech, "Technology"),
        )
    }

    fun loadAdvertise(): ArrayList<Advertise> {
        return arrayListOf(
            Advertise("25/4/2021 - 30/4/2021", "Save off all product \nup to 70%", Uri.parse("")),
            Advertise("21/4/2021 - 30/4/2021", "Save off all product \nup to 20%", Uri.parse("")),
            Advertise("20/4/2021 - 30/4/2021", "Save off all product \nup to 50%", Uri.parse("")),
        )
    }

    fun loadFeatureCard(): MutableList<Feature_Item> {
        return mutableListOf(
            Feature_Item(R.drawable.img_book_card_example, "Khang", "Khang"),
            Feature_Item(R.drawable.img_book_card_example, "Khang", "Khang"),
            Feature_Item(R.drawable.img_book_card_example, "Khang", "Khang"),
            Feature_Item(R.drawable.img_book_card_example, "Khang", "Khang"),
            Feature_Item(R.drawable.img_book_card_example, "Khang", "Khang"),
            Feature_Item(R.drawable.img_book_card_example, "Khang", "Khang"),
            Feature_Item(R.drawable.img_book_card_example, "Khang", "Khang"),
            Feature_Item(R.drawable.img_book_card_example, "Khang", "Khang"),
            Feature_Item(R.drawable.img_book_card_example, "Khang", "Khang"),
            Feature_Item(R.drawable.img_book_card_example, "Khang", "Khang"),
            Feature_Item(R.drawable.img_book_card_example, "Khang", "Khang"),
            Feature_Item(R.drawable.img_book_card_example, "Khang", "Khang")
        )
    }

    fun loadBestsellingCard(): MutableList<Feature_Item> {
        return mutableListOf(
            Feature_Item(R.drawable.img_book_card_example, "Khang", "Khang", "đ 90000"),
            Feature_Item(R.drawable.img_book_card_example, "Khang", "Khang", "đ 12000"),
            Feature_Item(R.drawable.img_book_card_example, "Khang", "Khang", "đ 15000"),
            Feature_Item(R.drawable.img_book_card_example, "Khang", "Khang", "đ 20000"),
            Feature_Item(R.drawable.img_book_card_example, "Khang", "Khang", "đ 90000"),
            Feature_Item(R.drawable.img_book_card_example, "Khang", "Khang", "đ 12000"),
            Feature_Item(R.drawable.img_book_card_example, "Khang", "Khang", "đ 15000"),
            Feature_Item(R.drawable.img_book_card_example, "Khang", "Khang", "đ 20000")
        )
    }

    fun loadSuggestCard(): MutableList<Feature_Item> {
        return mutableListOf(
            Feature_Item(
                R.drawable.img_book_card_example,
                "Khang",
                "Khang",
                "đ 90000",
                "99 Reviews"
            ),
            Feature_Item(
                R.drawable.img_book_card_example,
                "Khang",
                "Khang",
                "đ 12000",
                "99 Reviews"
            ),
            Feature_Item(
                R.drawable.img_book_card_example,
                "Khang",
                "Khang",
                "đ 15000",
                "99 Reviews"
            ),
            Feature_Item(
                R.drawable.img_book_card_example,
                "Khang",
                "Khang",
                "đ 20000",
                "99 Reviews"
            ),
            Feature_Item(
                R.drawable.img_book_card_example,
                "Khang",
                "Khang",
                "đ 90000",
                "99 Reviews"
            ),
            Feature_Item(
                R.drawable.img_book_card_example,
                "Khang",
                "Khang",
                "đ 12000",
                "99 Reviews"
            ),
            Feature_Item(
                R.drawable.img_book_card_example,
                "Khang",
                "Khang",
                "đ 15000",
                "99 Reviews"
            ),
            Feature_Item(
                R.drawable.img_book_card_example,
                "Khang",
                "Khang",
                "đ 20000",
                "99 Reviews"
            )
        )
    }

    fun loadSearchItem(): MutableList<SearchResultItem> {
        return mutableListOf(
            SearchResultItem("Nguyễn Nhật Ánh", "21:00, May 8, 2021"),
            SearchResultItem("Địa chính trị", "21:00, May 8, 2021"),
            SearchResultItem("Địa Lý", "21:00, May 8, 2021"),
            SearchResultItem("Lịch Sử", "21:00, May 8, 2021"),
            SearchResultItem("OOP fundamentals", "21:00, May 8, 2021"),
            SearchResultItem("Head of Design Pattern", "21:00, May 8, 2021"),
            SearchResultItem("Toán", "21:00, May 8, 2021"),
            SearchResultItem("Những tù nhân của địa lý", "21:00, May 8, 2021"),
            SearchResultItem("Sinh viên", "21:00, May 8, 2021"),
            SearchResultItem("ĐH CNTT", "21:00, May 8, 2021"),
            SearchResultItem("Công nghệ phần mềm", "21:00, May 8, 2021"),
            SearchResultItem("Kỹ thuật phần mềm", "21:00, May 8, 2021"),
        )
    }


}