package sv.edu.udb.practico2dmslab
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.DatabaseReference


class AddStudentActivity : AppCompatActivity() {
    private lateinit var studentsRef: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_student)

        val etFirstName = findViewById<EditText>(R.id.etFirstName)
        val etLastName = findViewById<EditText>(R.id.etLastName)
        val spGrade = findViewById<Spinner>(R.id.spGrade)
        val spSubject = findViewById<Spinner>(R.id.spSubject)
        val etFinalScore = findViewById<EditText>(R.id.etFinalScore)
        val btnSave = findViewById<Button>(R.id.btnSave)

        // Configurar Spinners
        ArrayAdapter.createFromResource(
            this,
            R.array.grades,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spGrade.adapter = adapter
        }

        ArrayAdapter.createFromResource(
            this,
            R.array.subjects,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spSubject.adapter = adapter
        }

        studentsRef = FirebaseDatabase.getInstance().getReference("students")

        btnSave.setOnClickListener {
            val firstName = etFirstName.text.toString().trim()
            val lastName = etLastName.text.toString().trim()
            val grade = spGrade.selectedItem.toString()
            val subject = spSubject.selectedItem.toString()
            val finalScoreText = etFinalScore.text.toString()

            if (firstName.isEmpty() || lastName.isEmpty() || finalScoreText.isEmpty()) {
                Toast.makeText(this, "Complete todos los campos", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val finalScore = try {
                finalScoreText.toDouble()
            } catch (e: NumberFormatException) {
                Toast.makeText(this, "Nota inválida", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (finalScore < 0 || finalScore > 10) {
                Toast.makeText(this, "Nota debe ser 0-10", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val student = Student(
                firstName = firstName,
                lastName = lastName,
                grade = grade,
                subject = subject,
                finalScore = finalScore
            )

            studentsRef.push().setValue(student)
                .addOnSuccessListener {
                    Toast.makeText(this, "Estudiante agregado", Toast.LENGTH_SHORT).show()
                    finish()
                }
                .addOnFailureListener {
                    Toast.makeText(this, "Error al guardar", Toast.LENGTH_SHORT).show()
                }
        }
    }
}