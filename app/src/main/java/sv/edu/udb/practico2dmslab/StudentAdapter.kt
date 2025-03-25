import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import sv.edu.udb.practico2dmslab.R
import sv.edu.udb.practico2dmslab.model.Student

class StudentAdapter(private val studentList: ArrayList<Student>) :
    RecyclerView.Adapter<StudentAdapter.StudentViewHolder>() {

    private var listener: OnItemClickListener? = null

    interface OnItemClickListener {
        fun onItemClick(position: Int)
    }

    fun setOnItemClickListener(listener: OnItemClickListener) {
        this.listener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StudentViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.student_item, parent, false)
        return StudentViewHolder(itemView, listener)
    }

    override fun onBindViewHolder(holder: StudentViewHolder, position: Int) {
        val currentItem = studentList[position]
        holder.name.text = "${currentItem.name} ${currentItem.lastName}"
        holder.grade.text = currentItem.grade
        holder.subject.text = currentItem.subject
        holder.score.text = currentItem.score.toString()
    }

    override fun getItemCount(): Int = studentList.size

    class StudentViewHolder(itemView: View, listener: OnItemClickListener?) :
        RecyclerView.ViewHolder(itemView) {
        val name: TextView = itemView.findViewById(R.id.studentName)
        val grade: TextView = itemView.findViewById(R.id.studentGrade)
        val subject: TextView = itemView.findViewById(R.id.studentSubject)
        val score: TextView = itemView.findViewById(R.id.studentScore)

        init {
            itemView.setOnClickListener {
                listener?.onItemClick(adapterPosition)
            }
        }
    }
}