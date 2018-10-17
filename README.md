# NoSQL
Projet HMIN313 Université de Montpellier

GraphStore - Liste d'incidence

En programmation orientée objet, la structure dite liste d'incidence suggérée par exemple par Goodrich and Tamassia dans leur livre4 possède des classes d'objets pour les sommets et pour les arêtes. Chaque objet sommet a une variable d'instance pointant vers une collection qui répertorie les objets qui sont les arêtes incidentes. À son tour, chaque arête pointe vers les deux objets sommet qui sont ses extrémités. Cette version de la liste d'adjacence utilise plus de mémoire que la version dans laquelle les sommets adjacents sont listés directement, mais l'existence d'objets arête explicites permet une flexibilité accrue, par exemple pour l'enregistrement d'informations supplémentaires sur les arêtes.