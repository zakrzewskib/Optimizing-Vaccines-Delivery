2020-11-11 - 2020-12-03

Celem projektu było napisanie programu, który zminimalizuje koszty sprzedazy szczepionek do wszystkich podanych aptek.

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

```
BioTech 2.0     -> CentMedEko Centrala [Koszt = 300 * 70.5 = 21150 zł]
Eko Polska 2020 -> CentMedEko Centrala [Koszt = 150 * 100 = 15000 zł]
...
Opłaty całkowite: 36150 zł
```
