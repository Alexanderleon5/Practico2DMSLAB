package sv.edu.udb.practico2dmslab

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.database.*
import android.widget.Toast
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener

class MainActivity : AppCompatActivity() {
    private lateinit var studentsRef: DatabaseReference
    private lateinit var adapter: StudentAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val rvStudents = findViewById<RecyclerView>(R.id.rvStudents)
        val fabAddStudent = findViewById<FloatingActionButton>(R.id.fabAddStudent)

        rvStudents.layoutManager = LinearLayoutManager(this)
        adapter = StudentAdapter(this)
        rvStudents.adapter = adapter

        studentsRef = FirebaseDatabase.getInstance().getReference("students")

        loadStudents()

        fabAddStudent.setOnClickListener {
            startActivity(Intent(this, AddStudentActivity::class.java))
        }
    }

    private fun loadStudents() {
        studentsRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val students = mutableListOf<Student>()
                for (studentSnapshot in snapshot.children) {
                    val student = studentSnapshot.getValue(Student::class.java)
                    student?.id = studentSnapshot.key
                    student?.let { students.add(it) }
                }
                adapter.setStudents(students)
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@MainActivity, "Error al cargar", Toast.LENGTH_SHORT).show()
            }
        })
    }
}