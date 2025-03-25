import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import sv.edu.udb.practico2dmslab.databinding.ActivityAddStudentBinding
import sv.edu.udb.practico2dmslab.model.Student

class AddStudentActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddStudentBinding
    private lateinit var database: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddStudentBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Configurar Spinners
        val grades = arrayOf("1° Grado", "2° Grado", "3° Grado", "4° Grado", "5° Grado")
        val subjects = arrayOf("Matemáticas", "Lenguaje", "Ciencias", "Sociales", "Inglés")

        val gradeAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, grades)
        val subjectAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, subjects)

        binding.gradeSpinner.adapter = gradeAdapter
        binding.subjectSpinner.adapter = subjectAdapter

        binding.saveButton.setOnClickListener {
            val name = binding.nameEditText.text.toString()
            val lastName = binding.lastNameEditText.text.toString()
            val grade = binding.gradeSpinner.selectedItem.toString()
            val subject = binding.subjectSpinner.selectedItem.toString()
            val scoreText = binding.scoreEditText.text.toString()

            if (name.isEmpty() || lastName.isEmpty() || scoreText.isEmpty()) {
                Toast.makeText(this, "Todos los campos son obligatorios", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val score = scoreText.toDouble()
            if (score < 0 || score > 10) {
                Toast.makeText(this, "La nota debe estar entre 0 y 10", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            database = FirebaseDatabase.getInstance().getReference("students")
            val studentId = database.push().key

            val student = Student(studentId, name, lastName, grade, subject, score)

            studentId?.let {
                database.child(it).setValue(student).addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(this, "Estudiante registrado", Toast.LENGTH_SHORT).show()
                        finish()
                    } else {
                        Toast.makeText(this, "Error al registrar", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }
}