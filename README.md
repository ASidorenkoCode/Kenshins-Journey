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

3. Maven Build Laden (https://www.jetbrains.com/help/idea/delegate-build-and-run-actions-to-maven.html#ignore_pom)

4. Erstelle eine neue Github Repository mit einer Datei namens data.txt. Weitere Dateien dürfen nicht enthalten sein.

5. GitHub Access Token
   generieren (https://docs.github.com/de/authentication/keeping-your-account-and-data-secure/managing-your-personal-access-tokens).
   Dabei sollte es sich um einen klassischen Token handeln. Dabei sollte jeder Nutzer des Spiels für seinen eigenen
   Github Account einen Token generieren.
6. Jeder Account eines Spielers sollte auf die erstellte Github Repository als Contributor berechtigt
   werden (https://docs.github.com/de/repositories/managing-your-repositorys-settings-and-features/managing-repository-settings/managing-teams-and-people-with-access-to-your-repository)

6. In der Datei unter src/main/java/network/client/GithubClient.java sollte jeder Spieler die entsprechenden Parameter
   füllen. Der Github Token ist bei jedem Spieler anders. Die anderen Attribute sind gleich.

```java
private static final String REPO_OWNER = "DEIN-BENUTZERNAME";
private static final String REPO_NAME = "NAME-DER-REPOSITORY";
private static final String FILE_PATH = "data.txt";
private static final String GITHUB_TOKEN = "DEINEN-KLASSISCHEN-GITHUB-TOKEN";
```

7. Spiel starten über die Build Configuration in src/main/java/GameStart.java

## License

[MIT](https://choosealicense.com/licenses/mit/)
