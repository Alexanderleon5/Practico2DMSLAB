import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import sv.edu.udb.practico2dmslab.databinding.ActivityStudentDetailBinding
import sv.edu.udb.practico2dmslab.model.Student

class StudentDetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityStudentDetailBinding
    private lateinit var database: DatabaseReference
    private lateinit var studentId: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStudentDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        studentId = intent.getStringExtra("studentId") ?: ""

        database = Firebase.database.reference.child("students").child(studentId)

        loadStudentData()

        binding.updateButton.setOnClickListener {
            updateStudent()
        }

        binding.deleteButton.setOnClickListener {
            deleteStudent()
        }
    }

    private fun loadStudentData() {
        database.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val student = snapshot.getValue(Student::class.java)
                student?.let {
                    binding.nameEditText.setText(it.name)
                    binding.lastNameEditText.setText(it.lastName)
                    binding.scoreEditText.setText(it.score.toString())
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@StudentDetailActivity, "Error al cargar datos", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun updateStudent() {
        val name = binding.nameEditText.text.toString()
        val lastName = binding.lastNameEditText.text.toString()
        val scoreText = binding.scoreEditText.text.toString()

        if (name.isEmpty() || lastName.isEmpty() || scoreText.isEmpty()) {
            Toast.makeText(this, "Todos los campos son obligatorios", Toast.LENGTH_SHORT).show()
            return
        }

        val score = scoreText.toDouble()
        if (score < 0 || score > 10) {
            Toast.makeText(this, "La nota debe estar entre 0 y 10", Toast.LENGTH_SHORT).show()
            return
        }

        val updates = hashMapOf<String, Any>(
            "name" to name,
            "lastName" to lastName,
            "score" to score
        )

        database.updateChildren(updates).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Toast.makeText(this, "Estudiante actualizado", Toast.LENGTH_SHORT).show()
                finish()
            } else {
                Toast.makeText(this, "Error al actualizar", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun deleteStudent() {
        database.removeValue().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Toast.makeText(this, "Estudiante eliminado", Toast.LENGTH_SHORT).show()
                finish()
            } else {
                Toast.makeText(this, "Error al eliminar", Toast.LENGTH_SHORT).show()
            }
        }
    }
}