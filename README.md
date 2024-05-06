# Codex Naturalis Implementation (Java + Apple visionOS)

<p align="center">
<img src=".github/resources/codex_box.png" width="400" alt="codex logo"/>
</p>

## Project Overview

This project is a Java-based implementation of the table game "**Codex Naturalis**", developed as the final project for
the 2023/2024 Software Engineering course at Politecnico di Milano. The team comprises Mattia Colombo, Piervito Creanza,
Simone Curci, and Marco Febbo.

The implementation features both a command-line interface (CLI) and a JavaFX graphical user interface. It is structured
to accommodate two main development approaches for flexibility.

## Status of the work

| Functionality     | Status                  | 
|-------------------|-------------------------|
| Basic rules       | :white_check_mark:      |
| Complete rules    | :white_check_mark:      |
| Socket connection | :construction:          |
| RMI connection    | :construction:          |
| CLI               | :construction:          |
| GUI               | :ballot_box_with_check: |
| Multiple games    | :white_check_mark:      |
| Persistence       | :x:                     |
| Resilience        | :ballot_box_with_check: |
| Chat              | :construction:          |

**Legend**
| Symbol | Functionality status |
| --- | --- |
| :white_check_mark: | Completed |
| :ballot_box_with_check: | Planned, not started yet |
| :construction: | Work in progress |
| :x: | Not planned to be implemented |

### Game Description

"Codex Naturalis" is a strategic table game by Cranio Creations, where players compete to create the most valuable
manuscript through card placement and layering. Learn more about the
game [here](https://www.craniocreations.it/prodotto/codex-naturalis).
Rules of the
game [here](https://www.craniocreations.it/storage/media/product_downloads/126/1516/CODEX_ITA_Rules_compressed.pdf).

## Development Approaches

### Approach A: Maven and JavaFX

1. **Initial Setup**: Begin with the "maven-archetype-archetype."
2. **Integrating JavaFX**: Integrate JavaFX into the Maven project, requiring additional configuration and dependency
   management.

### Approach B: IntelliJ and JavaFX

1. **Initial Setup**: Start with the IntelliJ JavaFX template.
2. **Modifications**: Initially, some parts of the code will be commented out. These sections are designed for future
   use and can be uncommented as needed.

## Future Scope

The project is poised for adaptation to Apple's visionOS, suggesting plans for expanded compatibility and potential new
features for the visionOS environment.

## Legal

Codex Naturalis è un gioco da tavolo sviluppato ed edito da Cranio Creations S.R.L.
I contenuti grafici di questo progetto riconducibili al prodotto editoriale da tavolo sono utilizzati previa
approvazione di Cranio Creations S.R.L. A solo scopo didattico. È vietata la distribuzione, la copia o la riproduzione
dei contenuti e immagini in qualsiasi forma al di fuori del progetto, così come la redistribuzione e la pubblicazione
dei contenuti e immagini a fini diversi da quello sopracitato. È inoltre vietato l'utilizzo commerciale di suddetti
contenuti.
