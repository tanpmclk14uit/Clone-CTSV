package com.example.svbookmarket.activities.common

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class RecyclerViewItemMargin(margin: Int, row: Int): RecyclerView.ItemDecoration() {
    var row: Int = 0
    var margin: Int = 0

    /**
     * constructor
     * @param margin desirable margin size in px between the views in the recyclerView
     * @param columns number of columns of the RecyclerView
     */
    init {
            this.margin = margin
            this.row = row
    }


    /**
     * Set different margins for the items inside the recyclerView: no top margin for the first row
     * and no left margin for the first column.
     */
    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State): Unit{

        var position: Int = parent.getChildLayoutPosition(view);

            outRect.right = margin
            outRect.left = margin

        if (position == 0 )
        {
            outRect.left = 30
        }
    }
}
