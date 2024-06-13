# Kenshins Journey

Kenshins Journey ist ein 2D-Action Plattformer. Dabei wird der aktuelle Spielstand persistiert, so dass bei Neustarten
der Anwendung das aktuelle Level wieder gestartet wird. Mehrere Spieler können im lokalen Netz "zusammen" spielen, indem
angezeigt wird, was der aktuelle Highscore der Personen ist und wo sie sich im Spiel befinden.

## Installation

1. Klonen der Git-Branch

```bash
git clone https://github.com/ASidorenkoCode/Kenshins-Journey.git
```

2. Öffnen des Projektes in IntelliJ IDEA. Bei der Verwendung einer anderen IDEA muss das Projekt dementsprechend geladen
   werden, denn es handelt sich um ein IntelliJ IDEA Projekt

3. Maven Build Laden

4. Eine Github Repository mit einer Datei namens Data.txt erstellen

5. GitHub Access Token
   generieren (https://docs.github.com/de/authentication/keeping-your-account-and-data-secure/managing-your-personal-access-tokens).
   Dabei sollte es sich um einen klassischen Token handeln.

6. In der Datei unter src/main/java/network/client/GithubClient.java die entsprechenden Parameter füllen.

```java
private static final String REPO_OWNER = "DEIN-BENUTZERNAME";
private static final String REPO_NAME = "NAME-DER-REPOSITORY";
private static final String FILE_PATH = "data.txt";
private static final String GITHUB_TOKEN = "DEINEN-KLASSISCHEN-GITHUB-TOKEN";
```

7. Spiel starten über die Build Configuration in src/main/java/GameStart.java

## License

[MIT](https://choosealicense.com/licenses/mit/)
