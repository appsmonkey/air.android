<p align="center"><a href="https://apps.apple.com/us/app/cityos-air/id1149070470"><img src="images/cityos-air.png" /></a></p>
<p align="center">
	<a href="https://play.google.com/store/apps/details?id=io.cityos.cityosair&hl=en">
		<img src="https://play.google.com/intl/en_us/badges/images/generic/en_badge_web_generic.png" width="250" />
	</a>
</p>


![Version: v2.1](https://img.shields.io/badge/version-v2.1-lightgrey)

CityOS Air is the highest rated enironmental app in the South European region.

|         | Features  |
----------|-----------------
:thumbsup: | The power of clean air is right at your fingertips with the air app
:thumbsup: | Monitor all sensors in real time for a comprehensive picture of the air quality and the environment
:thumbsup: | Automatic syncing to the cloud, your air data goes with you no matter where you are.
:thumbsup: | The app provides you with comprehensive data and graph trends to give you a deeper understanding of your surroundings, air pollution patterns, and insights into the source of poor air quality as it happens.
:octocat: | 100% free and open source

## Contributing

We are always looking for contributions from **all skill levels**! A great way to get started is by helping [organize and/or squish bugs](https://github.com/appsmonkey/air.android/issues?q=is%3Aopen+is%3Aissue+label%3Abug). If you're looking to ease your way into the project try out a [good first issue](https://github.com/appsmonkey/air.android/issues/25).

### Screenshots

<table align="center" border="0">

<tr>
<td> <img src="images/cityos-air1.png"> </td>
<td> <img src="images/cityos-air2.png"> </td>
<td> <img src="images/cityos-air3.png"> </td>
</tr>

<tr>
<td> <img src="images/cityos-air4.png"> </td>
<td> <img src="images/cityos-air5.png"> </td>
<td> <img src="images/cityos-air6.png"> </td>
</tr>

</table>


### Setup
**Requirements**
- JDK 8
- Latest Android SDK tools
- Latest Android platform tools
- Android SDK 29
- AndroidX

**Dependencies**

- Minimum **SDK 21**
- **MVP**-architecture
- [**RxJava2**](https://github.com/ReactiveX/RxJava) & [**RxAndroid**](https://github.com/ReactiveX/RxAndroid) for Retrofit & background threads
- [**Retrofit**](https://github.com/square/retrofit) for constructing the REST API
- [**ButterKnife**](https://github.com/JakeWharton/butterknife) for view binding
- [**Fabric**](https://fabric.io/kits/android/crashlytics) analytics & crash reporting.
- **Android Support Libraries**
...

**Build**

    ./gradlew assembleDebug

### Open Android Studio 3.5

Navigate to the repost directory and open the `src` directory.

Sync gradle and then hit <kbd>ctrl</kbd> + <kbd>R</kbd>, Done!


### Running the app for the first time and onboarding

The first time you will be presented with welcome screen.

<p align="center"><img src="images/welcome.png" width="300" /></p>

Click **Login Into Device** and you will be presented with the login screen. Here we start with the onboarding process by either logging in with one of social accounts, which is quick and easy, or by registering with an email address and password, which require email confirmation. This process is augmented with **deep links** to ease the onboarding process. Because the *Social Login* registration is very easy we will describe here only the email/password onbarding. Click on the **Sign up with email** button on the *Login Screen* and you will be presented with the first step in creating an account. On this screen type in the email you want to use and click continue. It must not be already registered or used with either of the social logins. If the email is validated the first step is done and you will be presented with the screen which will inform you that your are to receive an email to the email address used.

<table align="center" border="0">

<tr>
<td> <img src="images/registration1.png"> </td>
<td> <img src="images/registration2.png"> </td>
<td> <img src="images/registration3.png"> </td>
</tr>

</table>

Open your mail app on your Android phone in order for **deep links** to work properly and to get to the last screen in the registration process.

<p align="center"><img src="images/registration4.png" width="300" /></p>

Click on **Verify email** or on link if the former does not open CityOS Air app and you should be presented with the following screen.

<p align="center"><img src="images/registration5.png" width="300" /></p>

Enter the data, click **Create Account**, and if all is validated the app will take you to login screen and automatically login. You can opt to save password for the screen if offered by iOS.

<p align="center"><img src="images/registration6.png" width="300" /></p>

Click on the top right icon/button and you will be transfered to the City Map. On the map you can see devices and by clicking on any of them an info window will slide in on the bottom to show PM10 and PM2.5 data as well as temperature, if there are data data readings. Otherwise the info will say either no data or device offline. By clicking on the info window you will be transfered to the device's dashboard. It is good to know that via side menu we can access only our devices, mine switch on map, for the rest of the devices, devices that belong to someone else, to access their dashboard map is the way to go.

<table align="center" border="0">

<tr>
<td> <img src="images/map1.png"> </td>
<td> <img src="images/map2.png"> </td>
<td> <img src="images/map3.png"> </td>
</tr>

</table>

## Add Device

Now is the time to add our own device. Bellow is the ordered flow and underneath some clarifications and instructions. Open side menu and click on *Add Device*. You will be presented with the Connect Device screen.

<table align="center" border="0">

<tr>
<td> <img src="images/adddevice-android1.jpeg"> </td>
<td> <img src="images/adddevice-android2.jpeg"> </td>
<td> <img src="images/adddevice-android3.jpeg"> </td>
</tr>

<tr>
<td> <img src="images/adddevice-android4.jpeg"> </td>
<td> <img src="images/adddevice-android5.jpeg"> </td>
<td> <img src="images/adddevice-android6.jpeg"> </td>
</tr>

</table>

Click on the **Open Settings** button and you will be transfered to the Android Wifi settings. Select Wifi whose SSID starts with Boxy-{code}. Once connected **go back to CityOS Air via Android back software or hardware button** and click on the **Configure Device** button. On the **Configure Device** screen select your internet WiFi SSID and the SSID field should be automatically filled in. Enter the password carefully, because if the wrong password is entered Boxy will not be able to access the internet, and thus to send the data. You will need to re-add it again in that case. Once you entered the password click **Save** and you should be transfered to the **Device Location** screen. Since your iPhone was connected to the Boxy's SSID, it might take a few moments for connection to reset to your WiFi. When that is done map will show on the screen. Select location of your BoxY and click **Continue**. On the next, **Device Info** screen, enter the device's name and switch on **Indoor** if the device is an indoor device. Click **Done** and you will be transfered to the device's dahsboard screen. Congratulations, you have your first Boxy set up feeding you data about how healthy is the air you breathe, in house, or oudtoors, which is important thing to know, and you can do it now in a way previously available only to big institutions for a lot of money. 

## Resources

- The [docs](docs/) contain information about our development practices.

## Open Source & Copying

We ship CityOS Air on the Google Play Store for free and provide its entire source code for free as well.

However, **please do not ship this app** under your own account?

## Why are we building this?

We are group of people who care about the envirnment and the better future of humanity.

## Code of Conduct

We aim to share our knowledge and findings as we work daily to improve our
product, for our community, in a safe and open space. We work as we live, as
kind and considerate human beings who learn and grow from giving and receiving
positive, constructive feedback.