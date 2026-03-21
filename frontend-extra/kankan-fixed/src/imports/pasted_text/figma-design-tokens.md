1. Das moderne Farb- & Schrift-System (Figma Styles)

Lege diese Styles zuerst in Figma an, damit dein Design konsistent bleibt.
A. Farben (Colors)

    Hintergrund (Background):

        #FFFFFF (Reinweiß) – Für den Hauptinhalt, Karten und Modale.

        #F4F5F7 (Sehr helles Grau) – Optional für den Bereich hinter den Spalten, um die weißen Karten abzuheben (wie bei Trello).

    Primärfarbe (Blau):

        #0052CC (Modernes, kräftiges Blau) – Für Header, Buttons (Aktionen), Links und aktive Zustände.

    Textfarbe (Dunkelgrau):

        #172B4D (Sehr dunkles Blau-Grau) – Für Überschriften und Haupttext. Bietet exzellenten Kontrast auf Weiß.

        #5E6C84 (Mittelgrau) – Für sekundären Text, Metadaten (z.B. Erstellungsdatum) und Platzhalter.

    Akzentfarben (Tags/Prioritäten):

        #FF5630 (Rot) – Hoch

        #FF991F (Orange) – Mittel

        #36B37E (Grün) – Niedrig

B. Typografie (Typography)

Nutze eine klare, serifenlose Schriftart wie Inter (sehr modern) oder Roboto.

    H1 (Titel): 28px, Bold, #172B4D

    H2 (Spaltentitel): 18px, Semi-Bold, #172B4D

    Body (Kartentext): 14px, Regular, #172B4D

    Caption (Metadaten): 12px, Regular, #5E6C84

2. Detaillierte Seitenbeschreibungen für Figma
Seite 1: Landing Page (Öffentlich)

    Header (Navigation):

        Hintergrund: #0052CC (Blau)

        Logo: Weißer Text "TeamSync"

        Links: Weißer Text "Features", "Preise"

        Button: Weißer Rand, weißer Text "Login", gefüllter weißer Button mit blauer Schrift "Registrieren"

    Hero Section:

        Hintergrund: #FFFFFF (Weiß)

        Titel (H1): "Projekte organisieren. Einfach. Effizient." (#172B4D)

        Button: #0052CC (Blau) mit weißem Text "Jetzt kostenlos starten"

    Features/Footer: Wie in der vorherigen Beschreibung, aber strikt im blauen/weißen Farbschema.

Seite 2: Auth Pages (Login & Register)

    Layout: Weißer Hintergrund (#FFFFFF) mit einem zentrierten, blauen Formular-Container (#0052CC) ODER ein zentriertes weißes Formular auf blauem Hintergrund.

    Inputs: Weißer Hintergrund, dunkelgraue Schrift (#172B4D), blauer Rahmen bei Fokus.

    Buttons: Gefüllt Blau (#0052CC) mit weißem Text.

Seite 3: Board View (Das Trello-Herzstück) – WICHTIG!

Hier setzen wir deinen Farbwunsch und den Trello-Style um.

    Layout (Auto Layout in Figma):

        Hintergrund der gesamten Seite: #0052CC (Blau) oder #F4F5F7 (Hellgrau), um die Spalten abzuheben.

    Header (Top Bar):

        Hintergrund: Ein etwas dunkleres oder transparenteres Blau als der Seitenhintergrund, um sich abzuheben.

        Inhalt: Board-Name (Weiß), Suchleiste (Weißer Hintergrund), User-Avatar.

    Kanban Area (Flex Layout):

        Spalten (Columns):

            Hintergrund: #EBECF0 (Helles Trello-Grau)

            Titel (H2): #172B4D (Dunkelgrau)

            Ecken: Abgerundet (3px oder 5px).

        Task-Karten (Cards) – Component:

            Hintergrund: #FFFFFF (Reinweiß) – WICHTIG!

            Titel (Body): #172B4D (Dunkelgrau)

            Ecken: Abgerundet (3px).

            Schatten: Sehr subtiler Drop Shadow (z.B. Y: 1, Blur: 1, Color: #000000, Opacity: 10%), um Tiefe zu erzeugen.

            Inhalt: Prioritäts-Tag (Farbe), Titel, Fälligkeitsdatum (#5E6C84), Avatar.

Seite 4: Modals (Task Details)

    Hintergrund: #FFFFFF (Weiß)

    Titel: H1, #172B4D (Dunkelgrau)

    Beschreibung/Kommentare: Body, #172B4D

    Buttons: Primär Blau (#0052CC), Sekundär Grau (#EBECF0) mit dunkelgrauer Schrift.