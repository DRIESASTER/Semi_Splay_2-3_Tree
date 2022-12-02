# Verslag Project Algoritmen en Datastructuren

X^2^Verslag door Dries Huybens, informatica ba2 2022-2023



In dit project hebben we een normale 2-3 boom, een 2-3 boom met semi-splay die via de Bottom-Up manier werkt en een 2-3 boom met semi-splay die via de Top-Down manier werkt.

In dit verslag ga ik bespreken welke van de drie het efficientst is per scenario, we zullen het toevoegen, verwijderen en opzoeken van een element bespreken. We gaan dit zowel doen op een normale manier waarop de elementen steeds in de zelfde volgorde verwijderd worden als ze werden toegevoegd, en wanneer ze op een willekeurige andere volgorde worden verwijderd.

## Het toevoegen van elementen

Het testen van het toevoegen van elementen heb ik gedaan door 50 maal een boom op te bouwen met eerst 10.000 elementen dan 20.000, 30.000... 

Zoals u hieronder op de grafiek kan zien is de gewone 2-3 boom veruit het efficienst. Dit komt natuurlijk doordat we hier niet moeten splayen wat uitendelijk steeds wel wat operaties voor nodig zijn. 


![grafiek betreffende toevoeg-operatie](/Users/drieshuybens/ad2_project/bijlagen verslag/toevoeg.png);