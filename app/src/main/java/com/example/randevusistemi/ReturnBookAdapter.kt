package com.example.randevusistemi

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import java.text.SimpleDateFormat
import java.util.Locale


class ReturnBookAdapter(private val onReturnClick: (Book) -> Unit) : RecyclerView.Adapter<ReturnBookAdapter.BookViewHolder>() {
    private var books: List<Book> = emptyList()
    private val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())

    fun submitList(newBooks: List<Book>) {
        books = newBooks
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_return_book, parent, false)
        return BookViewHolder(view, onReturnClick)
    }

    override fun onBindViewHolder(holder: BookViewHolder, position: Int) {
        holder.bind(books[position])
    }

    override fun getItemCount() = books.size

    class BookViewHolder(itemView: View, private val onReturnClick: (Book) -> Unit) : RecyclerView.ViewHolder(itemView) {
        private val titleView: TextView = itemView.findViewById(R.id.title)
        private val dueDateView: TextView = itemView.findViewById(R.id.dueDate)
        private val penaltyView: TextView = itemView.findViewById(R.id.penalty)

        fun bind(book: Book) {
            titleView.text = book.title
            dueDateView.text = "Due Date: ${dateFormat.format(book.dueDate)}"
            penaltyView.text = "Debt: ${book.penalty} TL"
            itemView.setOnClickListener { onReturnClick(book) }
        }

        companion object {
            private val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        }
    }
}
