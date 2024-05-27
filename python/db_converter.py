import math
import uuid

'''

@author Tobi

Das Skript 'db_converter.py' generiert zwei JSON-Dateien, 'users.json' und 'locations.json', die in die Trailblazer-Datenbank importiert werden können.
Es liest die zuvor generierten Kacheln Deutschlands in 'all_tiles_functional.txt' ein und generiert auf der Grundlage entsprechende User- und Locations-Objekte im JSON-Format. Zudem liest es die Datei 'route-horb-nbg.txt' ein, die dem Datev-User zugeordnet werden. Die Textdatei enthält eine Liste aller Koordinaten der Strecke zwischen Horb und Nürnberg, die mit einer GPS-Tracker-App während der Fahrt aufgezeichnet wurden.

Es werden drei User generiert:
- horb@test.de      Nur die Kachel der DHBW in Horb ist freigeschalten
- datev@test.de     Die Route zwischen Horb und Nürnberg ist freigeschalten
- schach@test.de    Die gesamte Karte Deutschlands ist im Schachbrettmuster freigeschalten

Der Fortschritt des Aufdeckungs-Prozentteils sowie Achievements werden beim Erstellen der Daten ignoriert, da diese nach einem Request ans Spring-Backend automatisch erstellt werden. Außerdem wurde auf vordefinierte Freunde sowie ein voreingestelltes Profilbild der User verzichtet.

Bei der Entwicklung des Skripts wurde besonders auf einen modularen Aufbau des Codes geachtet, um sich flexibel an den Wünschen des Frontends oder Änderungen des Datenmodells anpassen zu können.
Da es sich hierbei um ein "nebenläufiges" Skript handelt, dass kein Bestandteil des Spring-Backends ist, wurde auf eine umfassende Dokumentation des Codes verzichtet.

FEATURES
- Strukturieren der User- und Location-Daten im JSON-Format für Datenbank-Import
- Erstellen von Test-User mit unterschiedlichen, bereits freigeschalteten Locations
- Parsen und Umwandeln von eigenen Routen als Liste von Koordinaten

QUELLEN
- MongoDB JSON Data Import              https://www.mongodb.com/docs/compass/current/import-export/
- Rechner für Tile Count Deutschlands   https://tools.geofabrik.de/calc/#type=geofabrik_standard&bbox=5.538062,47.236312,15.371071,54.954937
- Verwendete GPS-Tracker-App            https://play.google.com/store/apps/details?id=eu.basicairdata.graziano.gpslogger                       
- Umrechnung Koordinaten in Kacheln     https://wiki.openstreetmap.org/wiki/Slippy_map_tilenames
- Standort der DHBW Horb im Raster      https://tile.openstreetmap.org/14/8587/5664.png 

'''


PATH = ''
FILE_COORDS = 'coords-horb-nbg.txt'
TILE_MAP = dict()

LINE_LOCATIONS = '{{"_id": "{}", "kuerzel": [{}], "tile": {{"xTile": {}, "yTile": {}, "zoomLevel": {}, "position": [{}]}}, "_class": "{}"}}'
LINE_USERS = '{{"_id": "{}", "firstname": "{}", "lastname": "{}", "email": "{}", "password": "{}", "username": "{}", "friends": [], "stats": {{}}, "achievementIds": [], "profilePictureId": null, "locationIds": [{}], "_class": "{}"}}'
LOCATIONS = []
USERS = []

HORB_USER_ID = 'aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa'
DATEV_USER_ID = 'bbbbbbbb-bbbb-bbbb-bbbb-bbbbbbbbbbbb'
SCHACH_USER_ID = 'cccccccc-cccc-cccc-cccc-cccccccccccc'

### TILE & COORDS UTIL

def coords_to_tile(lat, lon, zoom):
    lat_rad = lat / 180.0 * math.pi
    n = 2**zoom
    xtile = n * ((lon + 180.0) / 360.0)
    ytile = n * (1 - (math.log(math.tan(lat_rad) + (1 / math.cos(lat_rad))) / math.pi)) / 2.0

    return [int(xtile), int(ytile)]

def tile_to_coords(xtile, ytile, zoom):
    n = 2**zoom
    lon = xtile / n * 360.0 - 180.0
    lat = math.atan(math.sinh(math.pi * (1 - 2 * ytile / n))) * 180.0 / math.pi
    return [lat, lon]

### DATA 

def read_tile_map():
    with open(PATH + 'all_tiles_functional.txt', 'r') as f:
        for line in f.readlines():
            tile = line.strip().split(' ')[0]
            kuerzel = line.strip().split(' ')[1].strip().split(',')
            TILE_MAP[tile] = uuid.uuid4()
            LOCATIONS.append(get_location_line(TILE_MAP[tile], kuerzel, tile))

def get_schach_tiles():
    tile_set = set()
    for x in range(8448, 8960):
        for y in range(5120, 5888):
            tile = '{},{}'.format(x, y)
            if x % 2 == y % 2 and tile in TILE_MAP:
                tile_set.add(tile)
    return tile_set           

### LOCATION & USER LINE

def get_location_line(location_id, kuerzel, tile):
    # output format: id, kuerzel, xTile, yTile, zoomLevel, [lat, lon], class_name
    
    zoom_level = 14
    class_name = 'de.dhbw.trackingappbackend.entity.location.Location'

    x_tile = int(tile.split(',')[0])
    y_tile = int(tile.split(',')[1])
    c = tile_to_coords(x_tile, y_tile, zoom_level)

    s = LINE_LOCATIONS
    s = s.format(location_id, ','.join(('"' + k + '"') for k in kuerzel), x_tile, y_tile, zoom_level, ','.join(str(x) for x in c), class_name)
    return s

def get_user_line(idx, user_id, tile_set):
    # output format: id, first_name, last_name, email, password, shown_name, friend_ids, location_ids, class_name

    first_name = ['Horb', 'Datev', 'Schach']
    last_name = 'Nutzer'
    mail = ['horb@test.de', 'datev@test.de', 'schach@test.de']
    password = '$2a$10$Lx4QFm1QbC4IdjwvGrodk.LUjP0pdy1vTGfJNziV.pmVunTLX.vbG'
    shown_name = ['horber', 'datevler', 'schachler']
    class_name = 'de.dhbw.trackingappbackend.entity.AppUser'

    location_ids = [TILE_MAP[tile] for tile in tile_set]

    s = LINE_USERS
    s = s.format(user_id, first_name[idx], last_name, mail[idx], password, shown_name[idx], 
                 (','.join(('"' + str(x) + '"') for x in location_ids)), class_name)
    return s

### READ WRITE

def parse_coords_from(file_name):
    tile_set = set()

    with open(PATH + file_name) as f:
        input = [x.strip().split(',') for x in f.readlines()]
        for line in input:
            coords = [float(line[1]), float(line[0])]
            tile = coords_to_tile(coords[0], coords[1], 14)
            tile_set.add('{},{}'.format(tile[0], tile[1]))

    return tile_set

def parse_tiles_from(file_name):
    tile_set = set()
    with open(PATH + file_name, 'r') as f:
        tile_set.update([line.strip() for line in f.readlines()])
    print(len(tile_set))
    return tile_set

def write_location_json(file_name):
    with open(PATH + file_name, 'w') as f:
        f.write('[\n')
        f.write(',\n'.join(str(l) for l in LOCATIONS))
        f.write('\n]')

def write_user_json(file_name):
    with open(PATH + file_name, 'w') as f:

        f.write('[\n')

        # Horb
        line = get_user_line(0, HORB_USER_ID, ['8587,5664'])
        f.write(line)
        f.write(',\n')

        # Datev
        line = get_user_line(1, DATEV_USER_ID, parse_coords_from('route-horb-nbg.txt'))
        f.write(line)
        f.write(',\n')

        # Schach
        line = get_user_line(2, SCHACH_USER_ID, get_schach_tiles())
        f.write(line)

        f.write('\n]')

### MAIN

print("Starting...")
read_tile_map()

write_location_json('locations.json')
write_user_json('users.json')
print("Finished. Wrote output to users.json and locations.json.")
