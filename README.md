# magenta rest service test task
# endpoint 1
### /api/calculator/getAll
Returns all cities' ID and Name from database as XML:
# endpoint 2
### /api/calculator/calculate
parameters:
* type -- string "straight" or "matrix" -- determines calculations type
* reload -- boolean true or false -- forces data update from database
* list of cities' ids from -- to use as start point in calculations
* list of citie's ids to -- to use as end point in calculations
<br> returns results of calculations as text</br>
# endpoint 3
### /api/calculator/upload
parameters:
* file.xml
<br> Saves data from uploaded XML file in database. XML file has to have "citiesAndDistances" bracket as root, then list of cities and then list distances.</br>
<br>example in src/main/resources/test.xml</br>
