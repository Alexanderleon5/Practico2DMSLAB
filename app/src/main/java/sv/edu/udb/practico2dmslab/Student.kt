package sv.edu.udb.practico2dmslab

import java.io.Serializable

data class Student(
    var id: String? = null,
    var firstName: String = "",
    var lastName: String = "",
    var grade: String = "",
    var subject: String = "",
    var finalScore: Double = 0.0
) : Serializable