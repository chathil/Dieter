package com.example.dieter.data.source.remote.response

import com.example.dieter.data.source.domain.DetailIngredientModel
import com.example.dieter.data.source.domain.NutrientType
import kotlinx.serialization.Serializable

@Serializable
data class NutrientResponse(
    val dietLabels: List<String>? = emptyList(),
    val healthLabels: List<String>? = emptyList(),
    val cautions: List<String>? = emptyList(),
    val totalNutrients: Map<String, NutrientsResponse>,
    val totalDaily: Map<String, NutrientsResponse>
) {
    @Serializable
    data class NutrientsResponse(
        val label: String,
        val quantity: Float?,
        val unit: String
    )
}

// DetailIngredientModel

fun NutrientResponse.asDomainModel(): DetailIngredientModel {

    val totalNutrients = totalNutrients.map {
        try {
            NutrientType.valueOf(it.key)
        } catch (e: Exception) {
            NutrientType.UNKNOWN
        } to it.value.quantity
    }.toMap()
    return DetailIngredientModel(
        cautions ?: emptyList(),
        dietLabels ?: emptyList(),
        healthLabels ?: emptyList(),
        totalNutrients
    )
}

// "uri": "http://www.edamam.com/ontologies/edamam.owl#c0767b02-94c3-4308-8f85-132be0dd297a",
// "calories": 710,
// "totalWeight": 1000.0,
// "dietLabels": [],
// "healthLabels": [
// "LOW_SUGAR",
// "KETO_FRIENDLY",
// "PESCATARIAN",
// "PALEO",
// "SPECIFIC_CARBS",
// "DAIRY_FREE",
// "GLUTEN_FREE",
// "WHEAT_FREE",
// "EGG_FREE",
// "MILK_FREE",
// "PEANUT_FREE",
// "TREE_NUT_FREE",
// "SOY_FREE",
// "FISH_FREE",
// "PORK_FREE",
// "RED_MEAT_FREE",
// "CELERY_FREE",
// "MUSTARD_FREE",
// "SESAME_FREE",
// "LUPINE_FREE",
// "MOLLUSK_FREE",
// "ALCOHOL_FREE",
// "NO_OIL_ADDED",
// "NO_SUGAR_ADDED"
// ],
// "cautions": [],
// "totalNutrients": {
//     "ENERC_KCAL": {
//         "label": "Energy",
//         "quantity": 710.0,
//         "unit": "kcal"
//     },
//     "FAT": {
//         "label": "Fat",
//         "quantity": 10.1,
//         "unit": "g"
//     },
//     "FASAT": {
//         "label": "Saturated",
//         "quantity": 2.6100000000000003,
//         "unit": "g"
//     },
//     "FATRN": {
//         "label": "Trans",
//         "quantity": 0.18,
//         "unit": "g"
//     },
//     "FAMS": {
//         "label": "Monounsaturated",
//         "quantity": 1.81,
//         "unit": "g"
//     },
//     "FAPU": {
//         "label": "Polyunsaturated",
//         "quantity": 2.9499999999999997,
//         "unit": "g"
//     },
//     "CHOCDF": {
//         "label": "Carbs",
//         "quantity": 9.1,
//         "unit": "g"
//     },
//     "FIBTG": {
//         "label": "Fiber",
//         "quantity": 0.0,
//         "unit": "g"
//     },
//     "SUGAR": {
//         "label": "Sugars",
//         "quantity": 0.0,
//         "unit": "g"
//     },
//     "PROCNT": {
//         "label": "Protein",
//         "quantity": 136.1,
//         "unit": "g"
//     },
//     "CHOLE": {
//         "label": "Cholesterol",
//         "quantity": 1260.0,
//         "unit": "mg"
//     },
//     "NA": {
//         "label": "Sodium",
//         "quantity": 5660.0,
//         "unit": "mg"
//     },
//     "CA": {
//         "label": "Calcium",
//         "quantity": 540.0,
//         "unit": "mg"
//     },
//     "MG": {
//         "label": "Magnesium",
//         "quantity": 220.0,
//         "unit": "mg"
//     },
//     "K": {
//         "label": "Potassium",
//         "quantity": 1130.0,
//         "unit": "mg"
//     },
//     "FE": {
//         "label": "Iron",
//         "quantity": 2.1,
//         "unit": "mg"
//     },
//     "ZN": {
//         "label": "Zinc",
//         "quantity": 9.7,
//         "unit": "mg"
//     },
//     "P": {
//         "label": "Phosphorus",
//         "quantity": 2440.0,
//         "unit": "mg"
//     },
//     "VITA_RAE": {
//         "label": "Vitamin A",
//         "quantity": 540.0,
//         "unit": "µg"
//     },
//     "VITC": {
//         "label": "Vitamin C",
//         "quantity": 0.0,
//         "unit": "mg"
//     },
//     "THIA": {
//         "label": "Thiamin (B1)",
//         "quantity": 0.2,
//         "unit": "mg"
//     },
//     "RIBF": {
//         "label": "Riboflavin (B2)",
//         "quantity": 0.15,
//         "unit": "mg"
//     },
//     "NIA": {
//         "label": "Niacin (B3)",
//         "quantity": 17.78,
//         "unit": "mg"
//     },
//     "VITB6A": {
//         "label": "Vitamin B6",
//         "quantity": 1.61,
//         "unit": "mg"
//     },
//     "FOLDFE": {
//         "label": "Folate equivalent (total)",
//         "quantity": 190.0,
//         "unit": "µg"
//     },
//     "FOLFD": {
//         "label": "Folate (food)",
//         "quantity": 190.0,
//         "unit": "µg"
//     },
//     "FOLAC": {
//         "label": "Folic acid",
//         "quantity": 0.0,
//         "unit": "µg"
//     },
//     "VITB12": {
//         "label": "Vitamin B12",
//         "quantity": 11.100000000000001,
//         "unit": "µg"
//     },
//     "VITD": {
//         "label": "Vitamin D",
//         "quantity": 1.0,
//         "unit": "µg"
//     },
//     "TOCPHA": {
//         "label": "Vitamin E",
//         "quantity": 13.200000000000001,
//         "unit": "mg"
//     },
//     "VITK1": {
//         "label": "Vitamin K",
//         "quantity": 3.0,
//         "unit": "µg"
//     },
//     "WATER": {
//         "label": "Water",
//         "quantity": 830.1,
//         "unit": "g"
//     }
// },
// "totalDaily": {
//     "ENERC_KCAL": {
//         "label": "Energy",
//         "quantity": 35.5,
//         "unit": "%"
//     },
//     "FAT": {
//         "label": "Fat",
//         "quantity": 15.538461538461538,
//         "unit": "%"
//     },
//     "FASAT": {
//         "label": "Saturated",
//         "quantity": 13.050000000000002,
//         "unit": "%"
//     },
//     "CHOCDF": {
//         "label": "Carbs",
//         "quantity": 3.033333333333333,
//         "unit": "%"
//     },
//     "FIBTG": {
//         "label": "Fiber",
//         "quantity": 0.0,
//         "unit": "%"
//     },
//     "PROCNT": {
//         "label": "Protein",
//         "quantity": 272.2,
//         "unit": "%"
//     },
//     "CHOLE": {
//         "label": "Cholesterol",
//         "quantity": 420.0,
//         "unit": "%"
//     },
//     "NA": {
//         "label": "Sodium",
//         "quantity": 235.83333333333334,
//         "unit": "%"
//     },
//     "CA": {
//         "label": "Calcium",
//         "quantity": 54.0,
//         "unit": "%"
//     },
//     "MG": {
//         "label": "Magnesium",
//         "quantity": 52.38095238095238,
//         "unit": "%"
//     },
//     "K": {
//         "label": "Potassium",
//         "quantity": 24.04255319148936,
//         "unit": "%"
//     },
//     "FE": {
//         "label": "Iron",
//         "quantity": 11.666666666666666,
//         "unit": "%"
//     },
//     "ZN": {
//         "label": "Zinc",
//         "quantity": 88.18181818181817,
//         "unit": "%"
//     },
//     "P": {
//         "label": "Phosphorus",
//         "quantity": 348.57142857142856,
//         "unit": "%"
//     },
//     "VITA_RAE": {
//         "label": "Vitamin A",
//         "quantity": 60.0,
//         "unit": "%"
//     },
//     "VITC": {
//         "label": "Vitamin C",
//         "quantity": 0.0,
//         "unit": "%"
//     },
//     "THIA": {
//         "label": "Thiamin (B1)",
//         "quantity": 16.666666666666668,
//         "unit": "%"
//     },
//     "RIBF": {
//         "label": "Riboflavin (B2)",
//         "quantity": 11.538461538461538,
//         "unit": "%"
//     },
//     "NIA": {
//         "label": "Niacin (B3)",
//         "quantity": 111.125,
//         "unit": "%"
//     },
//     "VITB6A": {
//         "label": "Vitamin B6",
//         "quantity": 123.84615384615384,
//         "unit": "%"
//     },
//     "FOLDFE": {
//         "label": "Folate equivalent (total)",
//         "quantity": 47.5,
//         "unit": "%"
//     },
//     "VITB12": {
//         "label": "Vitamin B12",
//         "quantity": 462.5000000000001,
//         "unit": "%"
//     },
//     "VITD": {
//         "label": "Vitamin D",
//         "quantity": 6.666666666666667,
//         "unit": "%"
//     },
//     "TOCPHA": {
//         "label": "Vitamin E",
//         "quantity": 88.0,
//         "unit": "%"
//     },
//     "VITK1": {
//         "label": "Vitamin K",
//         "quantity": 2.5,
//         "unit": "%"
//     }
// }
