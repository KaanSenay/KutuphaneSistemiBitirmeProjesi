package com.example.randevusistemi

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class TableAdapter : RecyclerView.Adapter<TableAdapter.TableViewHolder>() {
    private var tables: List<Table> = emptyList()

    fun submitList(newTables: List<Table>) {
        tables = newTables
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TableViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_table, parent, false)
        return TableViewHolder(view)
    }

    override fun onBindViewHolder(holder: TableViewHolder, position: Int) {
        holder.bind(tables[position])
    }

    override fun getItemCount() = tables.size

    class TableViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tableNameView: TextView = itemView.findViewById(R.id.table_name)
        private val userNameView: TextView = itemView.findViewById(R.id.user_name)

        fun bind(table: Table) {
            tableNameView.text = "Masa Adı: ${table.name}"
            userNameView.text = "Kullanıcı: ${table.occupiedBy}"
        }
    }
}
