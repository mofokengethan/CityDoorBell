package app.citydoorbell.content.model

import java.time.LocalDateTime
import java.util.UUID

data class PostModel(

    val uid: UUID = UUID.randomUUID(),
    val date: LocalDateTime = LocalDateTime.now(),

    var postContent: String = "",
    var postStyles: List<PostStyleType> = mutableListOf(),
    var favorites: List<String> = mutableListOf(),
    var comments: List<CommentModel> = mutableListOf(),

    var postStyleType: PostStyleType = PostStyleType.None,
    var isFavorite: Boolean = false,

    var commentModel: CommentModel = CommentModel(),
    val action: ((String) -> Unit)? = null
) {
    fun createPost(
        postContent: String,
        postStyleType: PostStyleType,
    ) {
        try {
            PostModel(
                postContent = postContent,
                postStyleType = postStyleType,
                isFavorite = isFavorite
            )
            if (postContent.length <= 140) {
                action?.let { it("Post content must be 140 characters or less") }
            } else if (postContent.all { it.isLetterOrDigit() || it.isWhitespace() }) {
                action?.let { it("Post content can only contain letters, numbers, and spaces") }
            } else {
                action?.let { it("") }
            }
        } catch (e: IllegalArgumentException) {
            action?.let { it("Error creating post: ${e.message}") }
        }
    }
}