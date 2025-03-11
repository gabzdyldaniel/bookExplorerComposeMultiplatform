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
        val title: String?,
        val authors: List<String>?,
        val publishedDate: String?,
        val description: String?,
        val imageLinks: ImageLinks?,
        val infoLink: String?
    )

    @Serializable
    data class ImageLinks(
        val smallThumbnail: String?,
        val thumbnail: String?
    )
}