-Dans buttonHandler : "String action" mieux d'avoir un enum.
                      List<Object> information, tres dangereux car information peut etre tous types. Plus besoin d'un cast
-Trop de couplage: solution = interface
-Trop de constante: enum peut etre mieux.
-Model trop faible: ajouter plus de responsabilité au model.