package com.example.svbookmarket.activities.common

import com.example.svbookmarket.activities.model.AppAccount
import com.example.svbookmarket.activities.model.Book
import com.example.svbookmarket.activities.model.Order
import com.example.svbookmarket.activities.model.User
import com.google.firebase.firestore.QueryDocumentSnapshot

object AppUtil {
    var currentUser: User = User()
    var currentAccount: AppAccount = AppAccount("", "", currentUser)
    var currentOrder: Order = Order()
    var currentSeller: AppAccount =AppAccount("","", User())

    fun toBook(doc: QueryDocumentSnapshot): Book {
        var bookItem = Book()
        bookItem.Description = doc["Description"].toString()
        bookItem.Image = doc["Image"].toString()
        bookItem.Kind = doc["Kind"].toString()
        bookItem.Name = doc["Name"].toString()
        return bookItem
    }

    fun checkName(str: String): Boolean {
        val regex =
            "^[a-zA-ZÀÁÂÃÈÉÊÌÍÒÓÔÕÙÚĂĐĨŨƠàáâãèéêìíòóôõùúăđĩũơƯĂẠẢẤẦẨẪẬẮẰẲẴẶẸẺẼỀỀỂẾưăạảấầẩẫậắằẳẵặẹẻẽềềểếỄỆỈỊỌỎỐỒỔỖỘỚỜỞỠỢỤỦỨỪễệỉịọỏốồổỗộớờởỡợụủứừỬỮỰỲỴÝỶỸửữựỳỵỷỹ\\s\\W|_][^\\d?!@#\\\$%\\^\\&*\\)\\(:';,\"+=._-`~{}|/\\\\]{1,}\$".toRegex()
        return str.matches(regex)
    }

    fun checkAddress(str: String): Boolean {
        val regex =
            "^[a-zA-ZÀÁÂÃÈÉÊÌÍÒÓÔÕÙÚĂĐĨŨƠàáâãèéêìíòóôõùúăđĩũơƯĂẠẢẤẦẨẪẬẮẰẲẴẶẸẺẼỀỀỂẾưăạảấầẩẫậắằẳẵặẹẻẽềềểếỄỆỈỊỌỎỐỒỔỖỘỚỜỞỠỢỤỦỨỪễệỉịọỏốồổỗộớờởỡợụủứừỬỮỰỲỴÝỶỸửữựỳỵỷỹ\\s\\W|_][^?!@#\\\$%\\^\\&*\\)(:'\"+=_-`~{}|]{3,}\$".toRegex()
        return str.matches(regex)
    }

    fun checkPhoneNumber(str:String):Boolean{
        val regex = "(84|0[3|5|7|8|9])+([0-9]{8})\\b".toRegex()
        return str.matches(regex)
    }


}