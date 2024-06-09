import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.randevusistemi.Book
import com.example.randevusistemi.R

class BookAdapter(private val onBookClick: (Book) -> Unit) : RecyclerView.Adapter<BookAdapter.BookViewHolder>() {
    private var books: List<Book> = emptyList()

    fun submitList(newBooks: List<Book>) {
        books = newBooks
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.book_item, parent, false)
        return BookViewHolder(view, onBookClick)
    }

    override fun onBindViewHolder(holder: BookViewHolder, position: Int) {
        holder.bind(books[position])
    }

    override fun getItemCount() = books.size

    class BookViewHolder(itemView: View, private val onBookClick: (Book) -> Unit) : RecyclerView.ViewHolder(itemView) {
        private val titleView: TextView = itemView.findViewById(R.id.title)

        fun bind(book: Book) {
            titleView.text = book.title
            itemView.setOnClickListener { onBookClick(book) }
        }
    }
}
