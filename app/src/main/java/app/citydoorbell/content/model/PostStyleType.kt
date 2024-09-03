package app.citydoorbell.content.model

enum class PostStyleType(private val emoji: String, private val description: String) {
    Popular("🍾", "Popular"),
    All("🌐", "All"),
    Share("🔁", "Share"),
    SMART("🧠", "Smart"),
    FUNNY("😂", "Funny"),
    INFORMATIVE("📚", "Informative"),
    RANDOM("🎲", "Random"),
    MOTIVATIONAL("💪", "Motivational"),
    CREATIVE("🎨", "Creative"),
    LOVE("❤️", "Love"),
    NEWS("📰", "News"),
    REVIEW("⭐", "Review"),
    ADVICE("💡", "Advice"),
    ACHIEVEMENT("🏆", "Achievement"),
    Donkey("🫏", "Donkey"),
    QUESTION("❓", "Question"),
    ANNOUNCEMENT("📢", "Announcement"),
    None("","");

    fun getEmoji(): String {
        return this.emoji
    }

    override fun toString(): String {
        return "$emoji $description"
    }
}