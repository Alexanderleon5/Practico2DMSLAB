import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.database.*
import sv.edu.udb.practico2dmslab.databinding.ActivityStudentListBinding
import sv.edu.udb.practico2dmslab.model.Student
import sv.edu.udb.practico2dmslab.adapter.StudentAdapter

class StudentListActivity : AppCompatActivity() {
    private lateinit var binding: ActivityStudentListBinding
    private lateinit var database: DatabaseReference
    private lateinit var studentList: ArrayList<Student>
    private lateinit var adapter: StudentAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStudentListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.studentRecyclerView.layoutManager = LinearLayoutManager(this)
        binding.studentRecyclerView.setHasFixedSize(true)

        studentList = arrayListOf()
        adapter = StudentAdapter(studentList)
        binding.studentRecyclerView.adapter = adapter

        database = FirebaseDatabase.getInstance().getReference("students")

        database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                studentList.clear()
                if (snapshot.exists()) {
                    for (studentSnap in snapshot.children) {
                        val student = studentSnap.getValue(Student::class.java)
                        studentList.add(student!!)
                    }
                    adapter.notifyDataSetChanged()
                }
            }

            override fun onCancelled(error: DatabaseError) {
                // Manejar error
            }
        })

        adapter.setOnItemClickListener(object : StudentAdapter.OnItemClickListener {
            override fun onItemClick(position: Int) {
                val intent = Intent(this@StudentListActivity, StudentDetailActivity::class.java)
                intent.putExtra("studentId", studentList[position].id)
                startActivity(intent)
            }
        })
    }
}