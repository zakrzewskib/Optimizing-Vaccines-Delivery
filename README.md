### About

Project was made in Java during Game of tanks was a desktop game made during 3rd Semester of Applied Computer Science at the Warsaw University of Technology.
<br>
Final results are not optimized, so project was not finished successfuly.
<br>
Documentation starting the project I thought there would be an easy method of optimizing the total costs of distributing vaccines.
That caused that project was a failure.
<br>
Best way to solve that problem was to use 
<a href="https://en.wikipedia.org/wiki/Bellman%E2%80%93Ford_algorithm" target="_blank" title="Wikipedia">Bellmann-Ford algorithm</a> and <a href="https://en.wikipedia.org/wiki/Ford%E2%80%93Fulkerson_algorithm" target="_blank" title="Wikipedia">Ford-Fulkerson algorithm</a> - solving - <a href="https://en.wikipedia.org/wiki/Minimum-cost_flow_problem" target="_blank" title="Wikipedia">minimum-cost flow problem</a>. 

### Project duration time
11th November 2020 - 3rd Devember 2020

### Technologies used
* Java (JDK 14)
* JUnit 4.13
* IntelliJ IDEA 2020.2.3 Community Edition

Only the final version of the project was pushed to the github repository, because it was provided by the university (on university's website).

### Examplary input file
```
# Producenci szczepionek (id | nazwa | dzienna produkcja)
0 | BioTech 2.0 | 900
1 | Eko Polska 2020 | 1300
2 | Post-Covid Sp. z o.o. | 1100
# Apteki (id | nazwa | dzienne zapotrzebowanie)
0 | CentMedEko Centrala | 450
1 | CentMedEko 24h | 690
2 | CentMedEko Nowogrodzka | 1200
# Połączenia producentów i aptek (id producenta | id apteki | dzienna maksymalna liczba dostarczanych szczepionek | koszt szczepionki [zł] )
0 | 0 | 800 | 70.5
0 | 1 | 600 | 70
0 | 2 | 750 | 90.99
1 | 0 | 900 | 100
1 | 1 | 600 | 80
1 | 2 | 450 | 70
2 | 0 | 900 | 80
2 | 1 | 900 | 90
2 | 2 | 300 | 100
```

### Examplary output file

```
BioTech 2.0     -> CentMedEko Centrala [Koszt = 300 * 70.5 = 21150 zł]
Eko Polska 2020 -> CentMedEko Centrala [Koszt = 150 * 100 = 15000 zł]
...
Opłaty całkowite: 36150 zł
```
