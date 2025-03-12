package cz.gabzdyldaniel.data.models
import kotlinx.serialization.Serializable

@Serializable
data class VolumesResponse(
    val totalItems: Int,
    val items: List<Volume>?
)

@Serializable
data class Volume(
    val id: String,
    val volumeInfo: Info
) {

    @Serializable
    data class Info(
        val title: String? = null,
        val authors: List<String>? = null,
        val publishedDate: String? = null,
        val description: String? = null,
        val imageLinks: ImageLinks? = null,
        val infoLink: String? = null
    )

    @Serializable
    data class ImageLinks(
        val smallThumbnail: String? = null,
        val thumbnail: String? = null
    )
}