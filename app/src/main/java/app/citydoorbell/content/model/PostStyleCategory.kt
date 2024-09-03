package app.citydoorbell.content.model

enum class CategoryStyleType(val description: String) {
    All("All"),
    SettlingIn("Settling In"),
    CareerDevelopment("Career Development"),
    Education("Education"),
    TravelAndExploration("Travel and Exploration"),
    HousingAndRentals("Housing and Rentals"),
    DatingAndRelationships("Dating and Relationships"),
    SocialLife("Social Life"),
    CityNavigation("City Navigation"),
    JobSearch("Job Search"),
    Finances("Finances"),
    HealthAndWellness("Health and Wellness"),
    FoodAndDrink("Food and Drink"),
    CultureShock("Culture Shock"),
    Networking("Networking"),
    PersonalGrowth("Personal Growth"),
    SafetyAndSecurity("Safety and Security"),
    Technology("Technology"),
    MentalHealth("Mental Health"),
    Fitness("Fitness"),
    Shopping("Shopping"),
    Events("Events"),
    ProfessionalServices("Professional Services"),
    CommunityInvolvement("Community Involvement");


    override fun toString(): String {
        return description
    }
}