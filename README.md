# Simulator for local anesthetic agents
## Summary
- This program is a computer-based simulator
   that reproduces the local anesthetic training items
   used in student training at Matsumoto Dental University.
- The application published on [sim-la-web](https://github.com/toshi-ara/sim-la-web)
   was rewritten using Java.


## About training procedures
### Procedures for animal experiments on which the simulator is based
1. Shave the hair on the back of the guinea pig
1. Inject 0.1 mL of the following drugs intradermally,
   and each injection site papule is enclosed in a circle
   marked by a magic marker.
    - Saline
    - Pro: Procaine hydrochloride (1%)
    - Lid: Lidocaine hydrochloride (1%)
    - Mep: Mepivacaine hydrochloride (1%)
    - Bup: Bupivacaine hydrochloride (1%)
    - Lid + Adr: Lidocaine hydrochloride (1%) with adrenaline (1/100,000)
1. Stimulate 6 times at each papule with needle,
    with the number of the skin contractions counted,
    defining this number as the score (minimum is 0, and maximum is 6).
    - Stimulate at 5 min interval up to 120 min
1. When a score of 6 is obtained three times in a row,
    finish the stimulation and define that time as the duration.
    - No need to stimulate after completion
1. Compare the sum of score values and duration of action for each drug.

### How to use the simulator
1. Press **Start** button to start measurement
1. When you click inside the circle,
    "Response" or "No response" will be displayed
    at the bottom left of the screen.
   If there is a response,
    the circle will temporarily turn red.
1. You can change the speed (times) of time passing with the slider.
    - Values can be changed both while running and stopped.
1. Press the **Pause** button to stop time passing,
    and press the **Restart** button to resume time passing
1. You can run the experiment from the beginning with new parameter values
    by pressing the **New Exp.** button.
    - This means conducting experiments using different animals.
1. Press the **Quit** button and then exit the program.
    - Please perform this operation to delete the parameter information
      because this remains in the browser,

### Notes on this simulator
- This simulator is modeled based on actual training results,
   but it may not necessarily yield the same results as animal experiments.
    - Because drug parameter values are set with random numbers,
      the duration of drug action may differ from the original drug order.
    - Because the presence or absence of a response
      when stimulated is determined by random numbers,
      the number of responses repeats up and down
       and does not necessarily increase over time.


## Reference
A paper on the model formula and parameter values used in this simulator.

- Toshiaki Ara and Hiroyuki Kitamura:
  Development of a Predictive Statistical Pharmacological Model
   for Local Anesthetic Agent Effects
   with Bayesian Hierarchical Model Parameter Estimation.
  Medicines 2023, 10(11), 61
  (https://doi.org/10.3390/medicines10110061)
- Toshiaki Ara and Hiroyuki Kitamura:
  Improvement of statistical models by considering correlations
   among parameters:
  Local anesthetic agent simulator for pharmacological education
   2024, 4(4), 2133-2148
  (https://doi.org/10.3390/biomedinformatics4040114)


## How to build
This program is build under Java21.

- generate normal jar file using gradle
```
./gradlew clean build
```

- generate minified jar file using ProGuard
```
./gradlew clean build -PuseProguard
```


## When changing the source code (for developers only)
### How to display license
- If you want to modify program, please edit the `LICENSE` file.
  Please add the copyright holder after `Copyright (c) 2026 ARA Toshiaki`
   and include this `LICENSE` file in the source code.


## ChangeLog
### v1.0.2 (2026-5-26)
- Exclude SimLocalAnesthesia.jar from the generated files for Windows

### v1.0.1 (2026-5-26)
- Change the generated file for Windows to exe.

### v1.0.0 (2026-5-23)
- Initial release

## About this program
- Author: Toshiaki Ara (Matsumoto Dental University, Department of Pharmacology)
- Year: 2026
- License: MIT License

## Notice
This product includes software developed at
The Apache Software Foundation (http://www.apache.org/).

