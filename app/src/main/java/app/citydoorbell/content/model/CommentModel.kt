package app.citydoorbell.content.model

import java.time.LocalDateTime


data class CommentModel(
    val date: LocalDateTime = LocalDateTime.now(),

    var postStyles: List<PostStyleType> = mutableListOf(),
    var favorites: List<String> = mutableListOf(),

    var commentContent: String = "",

    var postStyleType: PostStyleType = PostStyleType.None,
    var isFavorite: Boolean = false,

    var action: ((String) -> Unit)? = null
) {
    fun createComment(
        comment: String,
    ) {
        try {
            CommentModel(
                commentContent = comment,
                isFavorite = isFavorite
            )
            if (comment.length <= 140) {
                action?.let { it("Post content must be 140 characters or less") }
            } else if (comment.all { it.isLetterOrDigit() || it.isWhitespace() }) {
                action?.let { it("Post content can only contain letters, numbers, and spaces") }
            } else {
                action?.let { it("") }
            }
        } catch (e: IllegalArgumentException) {
            action?.let { it("Error creating post: ${e.message}") }
        }
    }
}