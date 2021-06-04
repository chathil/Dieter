/* eslint-disable require-jsdoc */
"use strict";

const functions = require("firebase-functions");
const admin = require("firebase-admin");
// const {user} = require("firebase-functions/lib/providers/auth");
admin.initializeApp();

function convertTZ(date, tzString) {
  return new Date((typeof date === "string" ? new Date(date) : date)
      .toLocaleString("en-US", {timeZone: tzString}));
}

exports.onAddMeal = functions.database
    .ref("user_intakes/{userRepId}/{mealId}/summary/nutrients")
    .onCreate((snapshot, context) => {
      const userRepId = context.params.userRepId;

      const todayDate = convertTZ(new Date(), "Asia/Jakarta");
      const todayDateString = ("0" + todayDate.getDate()).slice(-2) + "-" +
      ("0" + (todayDate.getMonth() + 1)).slice(-2) + "-" +
      todayDate.getFullYear();

      const userDailyRef = snapshot.ref
          .parent.parent.parent.parent.parent
          .child("user_daily/" + userRepId + "/nutrients");

      const todaySnap = snapshot;
      const updatedNutrientsMap = new Map();

      const tomorrowDate = new Date();
      tomorrowDate.setDate(tomorrowDate.getDate() + 1);
      const tomorrowDateString =
      ("0" + tomorrowDate.getDate()).slice(-2) + "-" +
    ("0" + (tomorrowDate.getMonth() + 1)).slice(-2) + "-" +
    tomorrowDate.getFullYear();

      todaySnap.forEach((nutrient) => {
        updatedNutrientsMap.set(nutrient.key, parseFloat(nutrient.val()));
      });

      return userDailyRef.once("value", (children) => {
        if (children.hasChild(todayDateString)) {
          updatedNutrientsMap.forEach((value, key) => {
            if (children.child(todayDateString).child(key).exists) {
              const oldValue = children.child(todayDateString).child(key).val();
              if (key == "Energy") {
                if (value + oldValue > 2000) {
                  userDailyRef.parent.child("workouts")
                      .child(tomorrowDateString).child("caloriesToBurn")
                      .set(value + oldValue - 2000);
                }
              }
              updatedNutrientsMap.set(key, value + oldValue);
            }
          });
        }
        const formatted = Array.from(updatedNutrientsMap)
            .reduce((obj, [key, value]) => {
              obj[key] = value;
              return obj;
            }, {});
        return userDailyRef.child(todayDateString).set(formatted);
      });
    });

exports.onDeleteMeal = functions.database
    .ref("user_intakes/{userRepId}/{mealId}/summary/nutrients")
    .onDelete((snapshot, context) => {
      const userRepId = context.params.userRepId;

      const todayDate = convertTZ(new Date(), "Asia/Jakarta");
      const todayDateString = ("0" + todayDate.getDate()).slice(-2) + "-" +
      ("0" + (todayDate.getMonth() + 1)).slice(-2) + "-" +
      todayDate.getFullYear();

      const userDailyRef = snapshot.ref
          .parent.parent.parent.parent.parent
          .child("user_daily/" + userRepId + "/nutrients");

      const deletedNutrients = snapshot;
      const updatedNutrientsMap = new Map();

      const tomorrowDate = new Date();
      tomorrowDate.setDate(tomorrowDate.getDate() + 1);
      const tomorrowDateString =
      ("0" + tomorrowDate.getDate()).slice(-2) + "-" +
    ("0" + (tomorrowDate.getMonth() + 1)).slice(-2) + "-" +
    tomorrowDate.getFullYear();

    deletedNutrients.forEach((nutrient) => {
      updatedNutrientsMap.set(nutrient.key, parseFloat(nutrient.val()));
    });

    return userDailyRef.once("value", (children) => {
        if (children.hasChild(todayDateString)) { 
            updatedNutrientsMap.forEach((value, key) => {
                if (children.child(todayDateString).child(key).exists) {
                  const oldValue = children.child(todayDateString).child(key).val();
                  if (key == "Energy") {
                    if (Math.abs(value - oldValue) < 2000) {
                      userDailyRef.parent.child("workouts")
                          .child(tomorrowDateString).child("caloriesToBurn")
                          .set(null);
                    }
                  }

                  var newValue = 0
                  if((oldValue - value) <= 0) {
                      newValue = 0
                  }else {
                    newValue = oldValue - value
                  }

                  updatedNutrientsMap.set(key, newValue);
                }
              });
         }

         const formatted = Array.from(updatedNutrientsMap)
            .reduce((obj, [key, value]) => {
              obj[key] = value;
              return obj;
            }, {});
        return userDailyRef.child(todayDateString).set(formatted);

    })

    });

exports.onBurnedCalories = functions.database
    .ref("user_workouts/{userRepId}/{workoutId}/totalCaloriesBurned")
    .onCreate((snapshot, context) => {
      const userRepId = context.params.userRepId;

      const todayDate = convertTZ(new Date(), "Asia/Jakarta");
      const todayDateString = ("0" + todayDate.getDate()).slice(-2) + "-" +
      ("0" + (todayDate.getMonth() + 1)).slice(-2) + "-" +
      todayDate.getFullYear();

      cons
      t toAdd = snapshot.val();

      const userDailyRef = snapshot.ref
          .parent.parent.parent.parent
          .child("user_daily/" + userRepId + "/workouts");

      return userDailyRef.once("value", (children) => {
        if (children.hasChild(todayDateString)) {
          if (children.child(todayDateString).child("caloriesBurned").exists) {
            const oldValue = children.child(todayDateString)
                .child("caloriesBurned").val();
            return userDailyRef.child(todayDateString)
                .child("caloriesBurned").set(toAdd + oldValue);
          } else {
            return userDailyRef.child(todayDateString)
                .child("caloriesBurned").set(toAdd);
          }
        } else {
          return userDailyRef.child(todayDateString)
              .child("caloriesBurned").set(toAdd);
        }
      });
    });

    exports.onUnBurnCalories = functions.database
    .ref("user_workouts/{userRepId}/{workoutId}/totalCaloriesBurned")
    .onDelete((snapshot, context) => {
        const userRepId = context.params.userRepId;


      const todayDate = convertTZ(new Date(), "Asia/Jakarta");
      const todayDateString = ("0" + todayDate.getDate()).slice(-2) + "-" +
      ("0" + (todayDate.getMonth() + 1)).slice(-2) + "-" +
      todayDate.getFullYear();

      const toDeduct = snapshot.val();

      const userDailyRef = snapshot.ref
          .parent.parent.parent.parent
          .child("user_daily/" + userRepId + "/workouts");

    return userDailyRef.once("value", (children) => {
        if (children.hasChild(todayDateString)) {
          if (children.child(todayDateString).child("caloriesBurned").exists) {
            const oldValue = children.child(todayDateString)
                .child("caloriesBurned").val();
                var newValue = 0
                if(oldValue - toDeduct <= 0) {
                    newValue = 0
                } else {
                    newValue = oldValue - toDeduct
                }
            return userDailyRef.child(todayDateString)
                .child("caloriesBurned").set(newValue);
          } else {
            return userDailyRef.child(todayDateString)
                .child("caloriesBurned").set(0);
          }
        } else {
          return userDailyRef.child(todayDateString)
              .child("caloriesBurned").set(0);
        }
      });

    })
