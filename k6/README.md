# Tests de performance (k6)

Tests non-fonctionnels de l'API de gestion de livres avec [k6](https://k6.io/).

Le scénario exécute aléatoirement la création de livres (`POST /books`) et la
récupération de la liste (`GET /books`), à débit constant.

## Fichiers

- `index.js` — scénario : `setup()` insère un jeu de données initial, puis
  `test_api_endpoints_config()` tire au sort un cas d'usage à chaque itération.
- `config.json` — exécuteur `constant-arrival-rate` (50 req/s pendant 10 s) et
  seuils : moins de 1 % d'échecs et p(95) du temps de réponse < 50 ms.
- `tests/dataToCreate.json` — données insérées au démarrage.
- `tests/usecases.json` — répartition des cas d'usage (GET / POST) tirés au sort.
- `runTest.sh` — lance les tests.

## Prérequis

1. Installer k6 : https://k6.io/docs/get-started/installation/
2. Démarrer la base PostgreSQL puis l'application :
   ```sh
   ./gradlew bootRun
   ```
   L'API doit répondre sur `http://localhost:8080`.

## Lancer les tests

```sh
cd k6
k6 run index.js -c config.json
# ou
sh runTest.sh
```

Pour cibler une autre URL :

```sh
BASE_URL=http://mon-serveur:8080 k6 run index.js -c config.json
```

Un rapport `summary.html` est généré dans le dossier `k6/` à la fin de l'exécution.
