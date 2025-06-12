# VocodeurDuPauvre

> Un programme de traitement des signaux audio numériques permettant de modifier la hauteur de la voix sans altérer le débit de parole, implémenté dans le cadre d'une SAÉ BUT Informatique.

## Repository original

**Repository original :** https://github.com/aylanh45/SAE1-2

## À propos du projet

Le **Vocodeur du Pauvre** (ou "Pauvocodeur") est une implémentation simplifiée d'un vocodeur permettant de reproduire l'une des fonctionnalités principales des logiciels de traitement audio : **la modification de la hauteur de la voix**.

**Développé par :**
- **Mehdi LAFAY** - Groupe S1.B2
- **Aylan HADDOUCHI** - Groupe S1.B1

### Objectifs

- Modifier la hauteur d'une voix (plus aigüe ou plus grave)
- Préserver le débit de parole original
- Maintenir la durée du fichier audio
- Minimiser les artefacts sonores (clics, distorsions)

### Contexte académique

Ce projet a été développé dans le cadre d'une **SAÉ** en BUT Informatique :

- **SAÉ 1.01** : Implémentation d'un besoin client (développement du Pauvocodeur)

**Livrables réalisés :**
- Code source Java complet avec interface graphique
- Traces d'exécution et jeux d'essais documentés
- Captures d'écran de validation
- Code vérifié avec SonarQube

### Résultats académiques

**Note obtenue : 20/20**
- Soutenance orale réussie avec excellence
- Code fonctionnel et documentation complète
- Respect intégral des spécifications techniques
- Collaboration exemplaire en binôme

## Principe de fonctionnement

L'algorithme se décompose en **deux phases principales** :

### 1. Ré-échantillonnage
- **Facteur > 1** : Suppression d'échantillons → Son plus aigu et plus court
- **Facteur < 1** : Duplication d'échantillons → Son plus grave et plus long
- Exemple : `frech = 1.3` rend la voix plus aigüe, `frech = 0.7` la rend plus grave

### 2. Dilatation temporelle
Restauration de la durée originale par découpage en séquences courtes (≈100ms) et réassemblage intelligent.

#### Versions d'implémentation :
- **Version minimale** : Découpage simple avec artefacts
- **Version sans clics** : Transitions douces avec zones de recouvrement
- **Version optimisée** : Recherche de motifs pour minimiser la distorsion

## Technologies utilisées

- **Langage** : Java
- **Bibliothèques** : StdAudio (lecture/écriture WAV), StdDraw (visualisation)
- **Traitement audio** : Algorithmes de traitement du signal temporel
- **Mathématiques** : Pondération progressive
- **Format audio** : WAV 16-bits, 44100 Hz

## Structure du projet

```
VocodeurDuPauvre/
├── Pauvocoder.java           # Classe principale avec algorithmes de traitement
├── PauvocoderInterface.java  # Interface graphique Swing
├── Main.java                 # Tests et utilitaires de tri
├── StdAudio.java            # Bibliothèque de traitement audio Princeton
├── StdDraw.java             # Bibliothèque graphique Princeton
├── linformatique.wav        # Fichier audio principal de test (voix)
├── sinusoide.wav           # Fichier audio de test (signal sinusoïdal)
├── preuve_execution.txt    # Traces complètes d'exécution
├── PreuveProgramme*.png    # Captures d'écran de validation
├── fichiers_generes/       # Fichiers audio générés par les algorithmes
│   ├── *_Resampled.wav    # Résultats du ré-échantillonnage
│   ├── *_Simple.wav       # Résultats de la dilatation simple
│   ├── *_SimpleOver.wav   # Résultats avec chevauchement
│   └── *_SimpleOverCrossEcho.wav # Résultats avec écho
└── README.md
```

## Méthodes implémentées

### Classe Pauvocoder - Algorithmes principaux
- **`resample(double[] inputWav, double freqScale)`** : Ré-échantillonnage du signal
- **`vocodeSimple(double[] inputWav, double dilatation)`** : Dilatation simple sans chevauchement
- **`vocodeSimpleOver(double[] inputWav, double dilatation)`** : Dilatation avec zones de chevauchement progressif
- **`vocodeSimpleOverCross(double[] inputWav, double dilatation)`** : Dilatation optimisée avec recherche de motifs
- **`echo(double[] input, double delayMs, double gain, int sampleRate)`** : Ajout d'effet d'écho paramétrable
- **`joue(double[] input)`** : Lecture et visualisation du signal audio
- **`displayWaveform(double[] wav)`** : Affichage graphique de la forme d'onde

### Interface PauvocoderInterface - Interface utilisateur
- **Interface graphique Swing** complète avec contrôles en temps réel
- **Sélection de fichiers** avec filtrage WAV
- **Contrôle de fréquence** avec boutons +/-
- **Modes de traitement** : Simple, SimpleOver, OverCross, Echo
- **Lecture audio** intégrée avec visualisation
- **Sauvegarde** des fichiers traités

### Constantes techniques
- **`SEQUENCE = 4410`** : Taille des segments (1/10 seconde à 44.1kHz)
- **`OVERLAP = 882`** : Zone de chevauchement (1/5 de SEQUENCE)
- **`SEEK_WINDOW = 661`** : Fenêtre de recherche pour optimisation


## Installation et utilisation

### Prérequis
- **Java JDK** (version 8 ou supérieure)
- Environnement Unix/Linux pour la compilation
- Fichiers WAV 16-bits à 44100 Hz

### Installation
```bash
git clone https://github.com/LAFAYMehdi/VocodeurDuPauvre.git
cd VocodeurDuPauvre

# Compilation des bibliothèques Princeton
javac StdAudio.java StdDraw.java

# Compilation du projet principal
javac Pauvocoder.java

# Compilation de l'interface graphique
javac PauvocoderInterface.java
```

### Utilisation en ligne de commande
```bash
# Traitement complet automatique
java Pauvocoder linformatique.wav 1.3  # Voix plus aigüe (facteur 1.3)
java Pauvocoder sinusoide.wav 0.7      # Signal plus grave (facteur 0.7)

# Génère automatiquement 4 fichiers :
# - *_Resampled.wav (ré-échantillonnage)
# - *_Simple.wav (dilatation simple)
# - *_SimpleOver.wav (avec chevauchement)
# - *_SimpleOverCrossEcho.wav (avec écho)
```

### Utilisation avec interface graphique
```bash
# Lancement de l'interface Swing
java PauvocoderInterface
```

**Fonctionnalités de l'interface :**
- **Sélection** de fichiers WAV avec explorateur
- **Contrôle fréquence** avec boutons +/- (de 0.1 à 3.0)
- **Modes de traitement** : Resampling, Simple, SimpleOver, OverCross
- **Effet écho** paramétrable (gain réglable)
- **Lecture audio** intégrée avec visualisation
- **Sauvegarde** des fichiers traités
- **Reset** pour recommencer

### Paramètres configurables
```java
// Constantes dans la classe Pauvocoder
SEQUENCE_LENGTH    // Taille des séquences (ms)
OVERLAP_LENGTH     // Durée de recouvrement
SEEK_WINDOW        // Fenêtre de recherche optimisation
SAMPLE_RATE        // Fréquence d'échantillonnage (44100 Hz)
```

## Exemples de résultats

| Type | Facteur | Description |
|------|---------|-------------|
| **Original** | 1.0 | Voix de référence |
| **Aigüe** | 1.3 | Hauteur augmentée sans accélération |
| **Grave** | 0.7 | Hauteur diminuée sans ralentissement |

## Paramètres configurables

- **`frech`** : Facteur de ré-échantillonnage (0.5 - 2.0)
- **`seq`** : Taille des séquences en ms (50-100ms)
- **`oLap`** : Durée des zones de recouvrement
- **`SEEK_WINDOW`** : Fenêtre d'exploration pour l'optimisation

## Performances

- **Fréquence d'échantillonnage** : 44100 Hz (standard CD)
- **Format supporté** : WAV 16-bits
- **Temps de traitement** : ~1:1 (temps réel)

## Tests et validation

### Jeux d'essais fournis
- **Fichier principal** : `linformatique.wav` (enregistrement vocal, ~844KB)
- **Fichier test** : `sinusoide.wav` (signal synthétique, ~884KB)
- **Traces complètes** : `preuve_execution.txt` (203 lignes de logs)
- **Captures d'écran** : `PreuveProgramme*.png` (validation visuelle)

### Tests réalisés et documentés
```bash
# Tests avec différents facteurs de fréquence
java Pauvocoder linformatique.wav 0.2  # Très grave
java Pauvocoder linformatique.wav 0.7  # Grave
java Pauvocoder linformatique.wav 1.3  # Aigü
java Pauvocoder linformatique.wav 1.5  # Très aigü
```

### Fichiers de résultats générés
- **Ré-échantillonnage** : `*_Resampled.wav` (modification de durée)
- **Simple** : `*_Simple.wav` (dilatation basique)
- **SimpleOver** : `*_SimpleOver.wav` (avec chevauchement)
- **Avec écho** : `*_SimpleOverCrossEcho.wav` (post-traitement)

### Validation technique
- **Interface graphique** : Tests d'ergonomie et fonctionnalité
- **Gestion d'erreurs** : Formats non supportés, fichiers manquants
- **Qualité sonore** : Écoute subjective des résultats
- **Code quality** : Vérification avec SonarQube

## Concepts techniques abordés

### Traitement du signal numérique
- **Échantillonnage/Quantification** : Conversion analogique-numérique
- **Ré-échantillonnage** : Modification de la densité temporelle
- **Dilatation temporelle** : Préservation des caractéristiques fréquentielles
- **Effets audio** : Écho, réverbération

### Algorithmique et optimisation
- **Recherche de motifs** : Algorithmes de correspondance
- **Fenêtrage glissant** : Traitement par segments
- **Optimisation par recherche** : Minimisation de la distorsion
- **Complexité algorithmique** : Analyse comparative Big-O

### Programmation Java avancée
- **Manipulation de tableaux** : Traitement de grandes données
- **Gestion mémoire** : Optimisation pour l'audio temps réel
- **Modularité** : Conception orientée objet
- **Documentation** : Javadoc et commentaires techniques

### Ingénierie logicielle
- **Spécifications techniques** : Respect des contraintes strictes
- **Tests et validation** : Jeux d'essais méthodiques
- **Livrables académiques** : Archive, traces, rapport
- **Présentation technique** : Communication orale scientifique

## Améliorations possibles

- [ ] Support de formats audio supplémentaires (MP3, FLAC)
- [ ] Interface graphique utilisateur
- [ ] Préréglages pour différents types de voix
- [ ] Traitement en temps réel
- [ ] Optimisations de performance

## Contribution

Ce projet étant réalisé dans un cadre académique strict, les contributions externes ne sont pas acceptées. Cependant, n'hésitez pas à :
- Signaler des bugs via les issues
- Proposer des améliorations
- Utiliser ce code comme référence pour vos propres projets

### Contraintes académiques
- **Travail en binôme** : Développement collaboratif imposé
- **Anti-plagiat** : Detection automatique de similarités
- **Format strict** : Archive .tar.gz, respect des signatures
- **Compilation Unix** : Compatibilité GNU/Linux obligatoire
- **Limites de code** : 80 caractères par ligne maximum

## Licence

Ce projet est développé dans un cadre éducatif. Veuillez respecter les droits d'auteur et créditer l'auteur en cas de réutilisation.

## Auteurs

**Mehdi LAFAY** - Groupe S1.B2
- Étudiant BUT Informatique
- GitHub: [@LAFAYMehdi](https://github.com/LAFAYMehdi)
- Email: [mehdi.lafay@edu.univ-fcomte.fr](mailto:mehdi.lafay@edu.univ-fcomte.fr)

**Aylan HADDOUCHI** - Groupe S1.B1
- Étudiant BUT Informatique
- Développement collaboratif

## Remerciements

- **Équipe pédagogique BUT Informatique** pour l'encadrement technique
- **Jury de soutenance** pour l'évaluation constructive
- **Ressources académiques** sur le traitement du signal
- **Communauté open-source** pour les outils utilisés

---

> **Note technique** : Ce projet démontre une approche simplifiée mais efficace du traitement audio, privilégiant la compréhension des concepts fondamentaux aux optimisations avancées. 
