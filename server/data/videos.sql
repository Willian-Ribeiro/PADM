DROP TABLE IF EXISTS videos;

CREATE TABLE videos (
    id INTEGER PRIMARY KEY, 
    title TEXT, 
    event TEXT, 
    duration TEXT, 
    recorded TEXT, 
    speaker TEXT, 
    views INTEGER, 
    likes INTEGER, 
    dislikes INTEGER,
    source TEXT,
    thumbnail TEXT, 
    file TEXT
);


INSERT INTO videos  (
        id, 
        title, 
        event, 
        duration, 
        recorded, 
        speaker, 
        views, 
        likes,
        dislikes,
        source,
        thumbnail,
        file
    ) 
    VALUES (
        "1", 
        "How Mars might hold the secret to the origin of life",
        "TED2015", 
        "16:02", 
        "Mar 2015", 
        "Nathalie Cabrol", 
        373719, 
        0,
        0,
        "https://www.ted.com/talks/nathalie_cabrol_how_mars_might_hold_the_secret_to_the_origin_of_life", 
        "NathalieCabrol_2015-480p.jpg",
        "NathalieCabrol_2015-480p_small.webm"
    )
;

INSERT INTO videos  (
        id, 
        title, 
        event, 
        duration, 
        recorded, 
        speaker, 
        views, 
        likes,
        dislikes,
        source, 
        thumbnail, 
        file
    ) 
    VALUES (
        "2", 
        "Math is forever",
        "TEDxRiodelaPlata", 
        "10:14", 
        "Oct 2014", 
        "Eduardo Sáenz de Cabezón", 
        656081, 
        0,
        0,
        "https://www.ted.com/talks/eduardo_saenz_de_cabezon_math_is_forever", 
        "EduardoSaenzdeCabezon_2014X-480p.jpg",
        "EduardoSaenzdeCabezon_2014X-480p_small.webm"
    )
;

INSERT INTO videos  (
        id, 
        title, 
        event, 
        duration, 
        recorded, 
        speaker, 
        views, 
        likes,
        dislikes,
        source, 
        thumbnail, 
        file
    ) 
    VALUES (
        "3", 
        "What if 3D printing was 100x faster?",
        "TED2015", 
        "10:45", 
        "Mar 2015", 
        "Joseph DeSimone", 
        1048986, 
        0,
        0,
        "https://www.ted.com/talks/joe_desimone_what_if_3d_printing_was_25x_faster", 
        "JosephDeSimone_2015-480p.jpg",
        "JosephDeSimone_2015-480p_small.webm"
    )
;

INSERT INTO videos  (
        id, 
        title, 
        event, 
        duration, 
        recorded, 
        speaker, 
        views, 
        likes,
        dislikes,
        source, 
        thumbnail, 
        file
    ) 
    VALUES (
        "4", 
        "Think your email's private? Think again",
        "TEDGlobal 2014", 
        "12:09", 
        "Oct 2014", 
        "Andy Yen", 
        956321, 
        0,
        0,
        "https://www.ted.com/talks/andy_yen_think_your_email_s_private_think_again", 
        "AndyYen_2014G-480p.jpg",
        "AndyYen_2014G-480p_small.webm"
    )
;

INSERT INTO videos  (
        id, 
        title, 
        event, 
        duration, 
        recorded, 
        speaker, 
        views, 
        likes,
        dislikes,
        source, 
        thumbnail, 
        file
    ) 
    VALUES (
        "5", 
        "How virtual reality can create the ultimate empathy machine",
        "TED2015", 
        "10:16", 
        "Mar 2015", 
        "Chris Milk", 
        445479, 
        0,
        0,
        "https://www.ted.com/talks/chris_milk_how_virtual_reality_can_create_the_ultimate_empathy_machine", 
        "ChrisMilk_2015-480p.jpg",
        "ChrisMilk_2015-480p_small.webm"
    )
;
