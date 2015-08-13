# Change Log
All notable changes to this project will be documented in this file.

## [Unreleased][unreleased]
### Added
- Manual Mode redet alle mit Vornamen an

## [0.9.2] - 2015-07-27
### Added
- Dieses Changelog hinzugefügt
- /settime als Befehl hinzugefügt und die Datenstruktur angepasst
- firstName wird mit in die Empfaenger.xml gespeichert
- XML Formatierung von Empfaenger.xml hinzugefügt
- Log an Dev Chat (Motivations Chat)
- Manual Mode hinzugefügt

### Changed
- abonnieren der Losung in Gruppen führt zum persönlichen abonnieren und zu Hinweis im Gruppenchat
- Empfaenger-Klasse wie Losung/AlleLosungen in AlleEmpaenger und Empfaenger aufgeteilt um sie mit Attrubuten zu versehen

### Fixed
- abonnieren wird jetzt überall mit zwei n geschrieben =)
- Logging refactored
- Datum wurde falsch angezeigt (1 Monat zuviel)

### Removed
- abonnieren der Losung für Gruppen

## [0.9.1] - 2015-07-23
### Added
- /getnow für Losung heute

### Fxixed
- In gruppen wird @LosungsBot ignoriert

### Changed
- AlleLosungen ist Singelton

## [0.9.0] - 2015-07-21
### Added
- PushLosung
- Senden der Losung an Empfänger
- Bot schreibt auch in Gruppenchat
- Logging in Datei

### Changed
- SDK abgespaltet
- Receiver verändert

## [0.8.0] - 2015-06-30
### Added
- Properties
- JAXB einlesen der Losung
- Connection to API
- Bot kann Nachrichten verschicken
- Receiver erstellt


[unreleased]: https://gitlab.com/danielketterer/losungsbot/compare/399bcaab...master
[0.9.2]: https://gitlab.com/danielketterer/losungsbot/compare/032034ba...399bcaab
[0.9.1]: https://gitlab.com/danielketterer/losungsbot/compare/b9e2018c...032034ba
[0.9.0]: https://gitlab.com/danielketterer/losungsbot/compare/612ad8f2...b9e2018c
[0.8.0]: https://gitlab.com/danielketterer/losungsbot/compare/23dbc1d8...612ad8f2