package hr.ferit.brunopetric.trgovina

import com.google.type.DateTime
import java.time.LocalDateTime

data class proizvod(
    var korisnik: String?=null,
    var description: String?=null,
    var name: String?=null,
    var photoUrl: String?=null,
    var price: Double?=null,
    var date: String ?=null,
    var kontakt: String ?=null,
    var id: String=""
)
