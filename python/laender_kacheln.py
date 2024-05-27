import math
import json

'''

@author Tobi

Das Skript 'laender_kacheln.py' generiert eine Textdatei aller Kacheln Deutschlands der Zoom-Stufe 14 nach dem Schema des OpenStreetMap-Rasters.
Es liest die Geodaten der Grenzen Deutschlands und aller Bundesländer im 'geodata/'-Verzeichnis ein und gibt eine Liste aller Kacheln mit X- und Y-Tile-ID und entsprechendem Bundesländer-Kürzel als 'all_tiles.txt' aus.

=> 154.537 Kacheln für Deutschland insgesamt

Die Ländergrenzen wurden mithilfe einer API der Website www.osm-boundaries.com erstellt und im Vorfeld im GEOJSON-Format heruntergeladen.
Da es sich hierbei um ein "nebenläufiges" Skript handelt, dass kein Bestandteil des Spring-Backends ist, wurde auf eine umfassende Dokumentation des Codes verzichtet.

FEATURES
- Parsen aller GEOJSON-Dateien und Ausgabe als Textdokument
- Umwandeln von Koordinaten in entsprechende Kacheln und zurück
- Zuordnen der Kacheln Deutschlands zu den entsprechenden Bundesländern
- Ausfüllen der Grenzumrisse für Generieren der inneren Kacheln
- Erkennen von komplexen Grenzumrissen ("Inseln" und "Löcher")
- Erkennen von Lücken im Grenzumriss und eigenständige Nachbesserung

KNOWN BUGS
Der Großteil der Fläche Brandenburgs wird aus unbekannten Gründen nicht erkannt, daher generiert das Skript ca. 12.000 Kacheln, die es keinem Bundesland zuordnen kann. Der Fehler konnte im Skript nicht behoben werden, daher wurden die Kürzel Brandenburgs BB nachträglich zu den Kacheln ohne definiertes Bundesland per Hand hinzugefügt.
Einige Teile der Elbe zwischen Niedersachsen und Schleswig-Holstein erstrecken sich über den Rahmen einer Kachel der Zoom-Stufe 14. Da die Ländergrenzen keine Wasserfläche miteinbeziehen, generiert das Skript ca. 10 Kacheln, die keinem der beiden Bundesländer zugeordnet werden können. Die Kürzel NI,SH wurden ebenfalls für die Kacheln nachträglich manuell ergänzt. 
Dies erfolgte im engen Austausch mit dem Max der Android-Gruppe, um die fehlenden Kacheln zu identifizieren und zuzuordnen.

Weitere Quellen:
- API der Ländergrenzen                 https://osm-boundaries.com/
- Tile Map Explorer                     https://chrishewett.com/blog/slippy-tile-explorer/
- Umrechnung Koordinaten in Kacheln     https://wiki.openstreetmap.org/wiki/Slippy_map_tilenames
- Tabelle der Zoom-Level                https://wiki.openstreetmap.org/wiki/Zoom_levels

'''


BL_KUERZEL = ["BB", "BW", "BY", "BE", "BB", "HB", "HH", "HE", "MV", "NI", "NW", "RP", "SL", "SN", "ST", "SH", "TH"]
INPUT_PATH = 'geodata/'
OUTPUT_PATH = ''

TILE_MAP = dict()

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

def tile_to_str(tile):
    return ','.join(str(x) for x in tile)

def str_to_tile(s):
    return [int(s.split(',')[0]), int(s.split(',')[1])]

def has_tile_gap(tile1, tile2):
    return abs(tile1[0]-tile2[0]) > 1 or 1 < abs(tile1[1]-tile2[1])

def write_tile_map(file_name, tile_map):
    with open(OUTPUT_PATH + file_name, 'w') as f:
        for key in tile_map:
            f.write(str(key) + " " + str(tile_map[key]) + "\n")

def get_all_possible_tiles():
    tile_set = set()

    for x in range(8448, 8960):
        for y in range(5120, 5888):
            tile_set.add('{},{}'.format(x, y))
    return tile_set


### TILE SHENANIGANS

def fill_gap(coords1, coords2):
    tile1 = coords_to_tile(coords1[0], coords1[1], 14)
    tile2 = coords_to_tile(coords2[0], coords2[1], 14)
    new_tiles = []

    if tile1[0] == tile2[0] or tile1[1] == tile2[1]: 
        # only one coord is off
        for x in range(tile1[0], tile2[0]+1):
            for y in range(tile1[1], tile2[1]+1):
                new_tiles.append(tile_to_str([x, y]))
    elif abs(tile1[0]-tile2[0]) == abs(tile1[1]-tile2[1]): 
        # both coords have same diff
        minX = min(tile1[0], tile2[0])
        minY = min(tile1[1], tile2[1])
        maxX = max(tile1[0], tile2[0])
        for i in range(maxX - minX + 1):
            new_tiles.append(tile_to_str([minX + i, minY + i]))
    else: 
        # manually create tiles in-between
        distanceX = coords1[0] - coords2[0]
        distanceY = coords1[1] - coords2[1]
        step_count = 10000
        step_lengthX = distanceX / step_count
        step_lengthY = distanceY / step_count
        
        for i in range(step_count):
            new_coords = [coords1[0] - (i * step_lengthX), coords1[1] - (i * step_lengthY)]
            new_tile = coords_to_tile(new_coords[0], new_coords[1], 14)
            if tile_to_str(new_tile) not in new_tiles:
                new_tiles.append(tile_to_str(new_tile))

    return new_tiles

def get_outline_tiles(coords_list):
        
    tile_set = set()
    for i in range(len(coords_list)):

        current_coords = [coords_list[i][1], coords_list[i][0]]
        last_coords = [coords_list[i-1][1], coords_list[i-1][0]]
        current_tile = coords_to_tile(current_coords[0], current_coords[1], 14)
        last_tile = coords_to_tile(last_coords[0], last_coords[1], 14)
        has_gap = has_tile_gap(last_tile, current_tile)

        if has_gap: # check for gaps
            gap_fills = fill_gap(current_coords, last_coords)
            # print("GAP AT " + tile_to_str(last_tile) + " & " + tile_to_str(current_tile) + ", ADDED " + str(' '.join(gap_fills)))
            for gap_fill in gap_fills:
                if gap_fill not in tile_set:
                    tile_set.add(gap_fill)

        if tile_to_str(current_tile) not in tile_set:
            tile_set.add(tile_to_str(current_tile))

    return tile_set

def get_filled_outline_tiles(outlines, kuerzel):
    
    # initialize full map
    full_map = dict()
    all_tiles = get_all_possible_tiles()
    for tile in all_tiles:
        full_map[tile] = None

    # set borders
    for outside_border_tile in get_outline_tiles(outlines[0]):
        full_map[outside_border_tile] = "OUTSIDE_BORDER"
    for i in range(1, len(outlines)):
        for inside_border_tile in get_outline_tiles(outlines[i]):
            full_map[inside_border_tile] = "INSIDE_BORDER"

    # fill map with AIR starting from top left corner of map border, replace None with AIR
    full_map['8448,5120'] = 'AIR'
    flag = True
    while flag:
        flag = False
        for x in range(8448, 8960):
            for y in range(5120, 5888):
                if full_map[str(x) + ',' + str(y)] == 'AIR':
                    if (str(x-1) + ',' + str(y)) in full_map and full_map[str(x-1) + ',' + str(y)] == None:
                        full_map[str(x-1) + ',' + str(y)] = 'AIR'
                        flag = True
                    if (str(x+1) + ',' + str(y)) in full_map and full_map[str(x+1) + ',' + str(y)] == None:
                        full_map[str(x+1) + ',' + str(y)] = 'AIR'
                        flag = True
                    if (str(x) + ',' + str(y-1)) in full_map and full_map[str(x) + ',' + str(y-1)] == None:
                        full_map[str(x) + ',' + str(y-1)] = 'AIR'
                        flag = True
                    if (str(x) + ',' + str(y+1)) in full_map and full_map[str(x) + ',' + str(y+1)] == None:
                        full_map[str(x) + ',' + str(y+1)] = 'AIR'
                        flag = True

    # fill map with INSIDE starting from OUTSIDE_BORDER, replace None with INSIDE
    flag = True
    while flag:
        flag = False
        for x in range(8448, 8960):
            for y in range(5120, 5888):
                if full_map[str(x) + ',' + str(y)] == 'OUTSIDE_BORDER' or full_map[str(x) + ',' + str(y)] == 'INSIDE':
                    if (str(x-1) + ',' + str(y)) in full_map and full_map[str(x-1) + ',' + str(y)] == None:
                        full_map[str(x-1) + ',' + str(y)] = 'INSIDE'
                        flag = True
                    if (str(x+1) + ',' + str(y)) in full_map and full_map[str(x+1) + ',' + str(y)] == None:
                        full_map[str(x+1) + ',' + str(y)] = 'INSIDE'
                        flag = True
                    if (str(x) + ',' + str(y-1)) in full_map and full_map[str(x) + ',' + str(y-1)] == None:
                        full_map[str(x) + ',' + str(y-1)] = 'INSIDE'
                        flag = True
                    if (str(x) + ',' + str(y+1)) in full_map and full_map[str(x) + ',' + str(y+1)] == None:
                        full_map[str(x) + ',' + str(y+1)] = 'INSIDE'
                        flag = True

    # add OUTSIDE_BORDER, INSIDE and INSIDE_BORDER to tile set, ignore AIR and None
    tile_set = set()
    for key in full_map:
        if full_map[key] == 'OUTSIDE_BORDER' or full_map[key] == 'INSIDE' or full_map[key] == 'INSIDE_BORDER':
            tile_set.add(key)

    return tile_set

def get_all_tiles_for(kuerzel):
    
    with open(INPUT_PATH + kuerzel + '_outline.geojson', mode='r', encoding='utf-8') as f:
        
        input = json.loads(f.read())
        full_map = dict()

        for i in range(len(input['features'][0]['geometry']['coordinates'])):

            tile_set = get_filled_outline_tiles(input['features'][0]['geometry']['coordinates'][i], kuerzel)
            for tile in tile_set:
                full_map[tile] = kuerzel
            print(kuerzel + " #" + str(i) + ": read " + str(len(input['features'][0]['geometry']['coordinates'][i][0])) + ' coords to ' + str(len(tile_set)) + ' tile/s.')

        print('Read {} total tiles for {}.'.format(str(len(full_map)), kuerzel))
        return full_map

### MAIN

print('Starting... (this may take a while)')
de_map = get_all_tiles_for('DE')

for kuerzel in BL_KUERZEL:
    tile_map = get_all_tiles_for(kuerzel)
    for tile in tile_map.keys():
        de_map[tile] += ',' + tile_map[tile]

write_tile_map('all_tiles.txt', de_map)
print('Finished. Wrote {} tiles to all_tiles.txt'.format(str(len(de_map))))