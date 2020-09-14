package ru.mikhailskiy.livedata.adapters

import com.squareup.picasso.Picasso
import com.xwray.groupie.kotlinandroidextensions.GroupieViewHolder
import com.xwray.groupie.kotlinandroidextensions.Item
import kotlinx.android.synthetic.main.item_with_text.*
import ru.mikhailskiy.livedata.R
import ru.mikhailskiy.livedata.data.Movie

class MovieItem(private val content: Movie) : Item() {

    override fun getLayout() = R.layout.item_with_text

    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        viewHolder.description.text = content.title
        viewHolder.movie_rating.rating = content.rating
        Picasso.get()
            .load(content.posterPath)
            .into(viewHolder.image_preview)
    }
}

