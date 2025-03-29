package sv.edu.udb.practico2dmslab

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class StudentAdapter(private val context: Context) : RecyclerView.Adapter<StudentAdapter.StudentViewHolder>() {
    private var students = mutableListOf<Student>()

    inner class StudentViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvName: TextView = itemView.findViewById(R.id.tvName)
        private val tvGrade: TextView = itemView.findViewById(R.id.tvGrade)
        private val tvSubject: TextView = itemView.findViewById(R.id.tvSubject)
        private val tvScore: TextView = itemView.findViewById(R.id.tvScore)

        fun bind(student: Student) {
            tvName.text = "${student.firstName} ${student.lastName}"
            tvGrade.text = student.grade
            tvSubject.text = student.subject
            tvScore.text = student.finalScore.toString()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StudentViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_student, parent, false)
        return StudentViewHolder(view)
    }

    override fun onBindViewHolder(holder: StudentViewHolder, position: Int) {
        holder.bind(students[position])
    }

    override fun getItemCount() = students.size

    fun setStudents(newStudents: List<Student>) {
        students.clear()
        students.addAll(newStudents)
        notifyDataSetChanged()
    }
}