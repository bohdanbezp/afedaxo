package com.afedaxo.ui.adapter

import android.content.Context
import android.graphics.Bitmap
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.afedaxo.R
import com.afedaxo.model.repository.FilesRepository
import com.afedaxo.model.room.Dish
import kotlinx.android.synthetic.main.dish_list_item.view.*
import kotlinx.android.synthetic.main.dish_list_item.view.dish_item_image
import kotlinx.android.synthetic.main.result_dish_list_item.view.*

class DishResultAdapter(val dishesImgs: List<Pair<Int, Bitmap>>,
                  val context: Context) : RecyclerView.Adapter<ViewHolderResult>() {

    // Gets the number of animals in the list
    override fun getItemCount(): Int {
        return dishesImgs.size
    }

    // Inflates the item views
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderResult {
        return ViewHolderResult(LayoutInflater.from(context).inflate(R.layout.result_dish_list_item, parent, false))
    }

    // Binds each animal in the ArrayList to a view
    override fun onBindViewHolder(holder: ViewHolderResult, position: Int) {
        holder.dishImage.setImageBitmap(dishesImgs.get(position).second)
        holder.title.setText(context.getString(R.string.person_num, dishesImgs.get(position).first))
    }
}

class ViewHolderResult (view: View) : RecyclerView.ViewHolder(view) {
    // Holds the TextView that will add each animal to
    val dishImage = view.dish_item_image
    val title = view.dish_item_person
}