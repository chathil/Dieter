package com.example.dieter.data.source.domain

enum class NutrientType(val nutrientName: String, val nutrientUnit: String, val maxValue: Int? = null) {
    CA("Calcium", "mg"),
    CHOCDF("Carbs", "g", 19),
    CHOLE("Cholesterol", "mg"),
    FAMS("Monounsaturated", "g"),
    FAPU("Polyunsaturated", "g"),
    SUGAR("Sugars", "g"),
    FAT("Fat", "g", 19),
    FATRN("Trans", "g"),
    FE("Iron", "mg"),
    FIBTG("Fiber", "g", 40),
    FOLDFE("Folate (Equivalent)", "æg"),
    K("Potassium", "mg"),
    MG("Magnesium", "mg"),
    NA("Sodium", "mg"),
    VITB6A("Vitamin B6", "mg"),
    ENERC_KCAL("Energy", "kcal", 2000),
    NIA("Niacin (B3)", "mg"),
    P("Phosphorus", "mg"),
    PROCNT("Protein", "g", 10),
    RIBF("Riboflavin (B2)", "mg"),
    FASAT("Saturated", "g"),
    TOCPHA("Vitamin E", "mg"),
    VITA_RAE("Vitamin A", "æg"),
    VITB12("Vitamin B12", "æg"),
    FOLFD("Folate (food)", "æg"),
    VITC("Vitamin C", "mg"),
    VITD("Vitamin D", "æg"),
    VITK1("Vitamin K", "æg"),
    THIA("Thiamin (B1)", "mg"),
    ZN("Zinc", "mg"), // TODO: might be wrong
    FOLAC("Folac", "mg"), // TODO: might be wrong
    WATER("Water", "mg"), // TODO: might be wrong
    UNKNOWN("UNKNOWN", "--") // TODO: might be wrong
}
