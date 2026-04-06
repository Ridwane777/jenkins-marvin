# My_Marvin - Automatisation Jenkins via Docker

## But du projet
**My_Marvin** est un projet d'infrastructure-as-code dont l'objectif est de déployer une instance **Jenkins** entièrement configurée de manière automatique. 

Au lieu de configurer Jenkins manuellement via l'interface graphique (clics), ce projet utilise **Docker**, **JCasC (Jenkins Configuration as Code)** et **Job DSL** pour :
1.  Déployer un serveur Jenkins prêt à l'emploi.
2.  Gérer les utilisateurs et leurs permissions via une stratégie de rôles (Admin, Gorilla, Ape, Assist).
3.  Créer automatiquement une structure de dossiers et des jobs de maintenance.
4.  Mettre en place un job **SEED** capable de générer dynamiquement d'autres jobs de compilation (CI) pour des projets C (Make).

---

## Structure du Projet
*   `Dockerfile` : Construit l'image Jenkins avec les plugins nécessaires.
*   `my_marvin.yml` : Configuration JCasC (Utilisateurs, Rôles, Sécurité).
*   `job_dsl.groovy` : Script Job DSL créant le dossier `Tools` et les jobs `SEED` et `clone-repository`.
*   `.env` : Fichier contenant les secrets et variables d'environnement (non versionné).

---


## Installation et Déploiement

### 1. Prérequis
Assurez-vous que **Docker** est installé sur votre machine Ubuntu.

### 2. Préparation des secrets
Créez un fichier `.env` à la racine du projet et remplissez les variables suivantes :
```bash

USER_CHOCOLATEEN_PASSWORD=votre_mot_de_passe
USER_VAUGIE_G_PASSWORD=votre_mot_de_passe
USER_I_DONT_KNOW_PASSWORD=votre_mot_de_passe
USER_NASSO_PASSWORD=votre_mot_de_passe


JAVA_OPTS=-Djenkins.install.runSetupWizard=false
CASC_JENKINS_CONFIG=/var/jenkins_home/my_marvin.yml

```

### 3. Build

Lancer votre conteneur
```bash

docker build --network=host -t my-marvin .

```
Lancer votre instance
```bash

docker run -d \
  --name jenkins \
  --network host \
  --env-file .env \
  -v jenkins_home:/var/jenkins_home \
  -v $(pwd)/my_marvin.yml:/var/jenkins_home/my_marvin.yml \
  -e CASC_JENKINS_CONFIG=/var/jenkins_home/my_marvin.yml \
  my-marvin

```