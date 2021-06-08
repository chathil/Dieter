Dieter - Future cool app tagline
================================
![Workflow result](https://github.com/chathil/Dieter/workflows/Build/badge.svg)

![Mockup](screenshots/banner.png)

[**To be replaced with proper description**] According to RISKESDAS, WHO and UNICEF the number of overweight Indonesian citizen has doubled over the past two decades. Obese people are more likely to suffer from non-communicable diseases, on the other hand Underweight is also a problem, 11.2% of Indonesian measured underweight that could lead to infertility. UNICEF Representative Debora Comini said "Good nutrition is not just about having enough food to eat but also getting the right food to eat, ", she also said that "many parents do not have sufficient knowledge to take the best decisions regarding their family’s food choices." With this app we aim to provide knowledge to the user  to make the right choice about what to eat. We'll recommend food and give nutritional value to help them make that decision.

# Getting Started
This project use Jetpack Compose, Android’s modern toolkit for building native UI. Jetpack Compose is currently in beta and only works with Canary Build of Android Studio.
- Clone this repository and open it in the latest [Android Studio (Canary build)](https://developer.android.com/studio/preview).
- Get the `google-services.json` file that I've sent in Discord/ or create another one.
- Place the file in the `app` directory
- Create a file called `secret.properties` in this project root dir and copy, paste and replace what's needed
    ```groovy
    EDAMAM_API_KEY="YOUR_EDAMAM_API_KEY"
    EDAMAM_APP_ID="YOUR EDAMAM APP ID"
    EMULATE_SERVER=false // boolean depends if you want to emulate firebase locally or not
    ```
- You can't commit directly, therefore you have to fork this repository and submit a pull request.

# Tutorial
- When you open the app for the first time, you'll be greeted with welcome screen. You can skip it by tapping <b>Skip</b> button, or you can go to the next slide by sliding it or tap the next until you finally reach the third page where you can sign in.
- Then you'll get to the home screen. Tap the ![eat](https://raw.githubusercontent.com/google/material-design-icons/master/png/maps/restaurant_menu/materialicons/18dp/2x/baseline_restaurant_menu_black_18dp.png) icon to take picture. If all ingredients are detected tap next, otherwise add them manually by tapping the search icon.
- Next you'll see summary, when you're done reading it tap <b>Save</b>
- You'll be back to the home screen wait about 2 seconds, and you will see the result.
- You should also see burn calories button, where you can burn calories if you want to. But since you just started using the app you'll see something like `-\-` on the button. that is because we don't have enough information to recommend how much calories you should burn. 
- Keep using the app so we can get to know you better and provide the best recommendations.

# Screenshots

<img src="screenshots/welcome1.jpg" width="260">&emsp;<img src="screenshots/preview.gif" width="247">

# Measurement Units
(will be translated to English and put into the app soon...)

| Ingredients   | Familiar units |
| ------------- | ------------- |
| Rice  | 1sdm : 15g  |
| Egg  | 1butir : 60g  |
| Tempeh  | 1buah segitiga : 20gr <br> 1buah sedang : 50gr <br> 1buah kecil : 25gr  |
| Tofu  | 1potong kecil : 20gr <br> 1potong sedang : 40gr <br>1potong besar : 80gr <br>1buah kecil : 30gr <br> 1buah besar : 40gr  |

# Code formatting
The CI uses [Spotless](https://github.com/diffplug/spotless) to check if your code is formatted correctly and contains the right licenses.
Internally, Spotless uses [ktlint](https://github.com/pinterest/ktlint) to check the formatting of your code. Here's how to configure it for use with Android Studio (instructions adapted
from the ktlint [README](https://github.com/shyiko/ktlint/blob/master/README.md)):

- Close Android Studio if it's open

- Download ktlint using these [installation instructions](https://github.com/pinterest/ktlint/blob/master/README.md#installation)

- Apply ktlint settings to Android Studio using these [instructions](https://github.com/pinterest/ktlint/blob/master/README.md#-with-intellij-idea)

- Start Android Studio


Before committing your code, run `./gradlew app:spotlessApply` to automatically format your code.

# Third Party Content
- All illustrations used in this app are from stories.freepik.com
- [EDAMAM API](https://developer.edamam.com/) is used to get nutritional values
- [Poppins](https://fonts.google.com/specimen/Poppins?query=poppi#about) font, licensed under the [Open Font License](https://scripts.sil.org/cms/scripts/page.php?site_id=nrsi&id=OFL).

# License
Working on it!
