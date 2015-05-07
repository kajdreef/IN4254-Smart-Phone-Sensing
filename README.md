# IN4254-Smart-Phone-Sensing
Goal is to create an android application which can monitor the users activity and can localise the user.
This is done as a project for the Smart Phone Sensing class of the Delft University of Technology.

## Activity Monitoring

The activity monitoring uses k Nearest Neighbors to classify the users activity (walking/queuing).
At this moment four different feature extractors have been implemented:

* Mean
* Magnitude
* Autocorrelation
* Standard Deviation

## Localisation (Particle Filter)

The inside locatilization (for the 9th floor of the EWI faculty) still needs to be implemented, but will be done with a particle filter.
