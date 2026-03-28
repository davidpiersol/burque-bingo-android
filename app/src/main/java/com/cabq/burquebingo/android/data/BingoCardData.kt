package com.cabq.burquebingo.android.data

data class BingoSquare(
    val id: String,
    val label: String,
    val hint: String,
    val learnMoreUrl: String,
)

data class BingoCardTheme(
    val id: String,
    val title: String,
    val subtitle: String,
    val squares: List<BingoSquare>,
)

fun defaultCardThemes(): List<BingoCardTheme> = listOf(
    BingoCardTheme(
        id = "public_art",
        title = "Murals & Public Art",
        subtitle = "Spot color across the city",
        squares = listOf(
            BingoSquare("m1", "A mural taller than you", "", "https://www.cabq.gov/culturalservices/public-art"),
            BingoSquare("m2", "Sculpture or statue", "", "https://www.cabq.gov/culturalservices/public-art"),
            BingoSquare("m3", "Something Route 66–era", "", "https://www.cabq.gov/artsculture"),
            BingoSquare("m4", "Tile or mosaic detail", "", "https://www.cabq.gov/culturalservices/public-art"),
            BingoSquare("m5", "Art you’d describe to a friend", "", "https://www.cabq.gov/culturalservices/special-events-festivals"),
        ),
    ),
    BingoCardTheme(
        id = "parks_open",
        title = "Parks & Open Space",
        subtitle = "Sandia views, trails, shade",
        squares = listOf(
            BingoSquare("p1", "A trailhead sign", "", "https://www.cabq.gov/parksandrecreation/open-space/lands"),
            BingoSquare("p2", "A bench with a view", "", "https://www.cabq.gov/parksandrecreation/parks"),
            BingoSquare("p3", "Native plant or tree you notice", "", "https://www.cabq.gov/parksandrecreation"),
            BingoSquare("p4", "Wildlife moment", "", "https://www.cabq.gov/parksandrecreation/open-space/open-space-visitor-center"),
            BingoSquare("p5", "Sandia peek from your spot", "", "https://www.cabq.gov/parksandrecreation"),
        ),
    ),
    BingoCardTheme(
        id = "burque_flavor",
        title = "Burque Flavor",
        subtitle = "Local sights & bites",
        squares = listOf(
            BingoSquare("b1", "Chile ristra or fresh roast smell", "", "https://www.cabq.gov/spring"),
            BingoSquare("b2", "Balloon or Fiesta nod", "", "https://www.cabq.gov/culturalservices/balloonmuseum"),
            BingoSquare("b3", "Local shop or maker", "", "https://www.cabq.gov/economicdevelopment/small-business/buy-local"),
            BingoSquare("b4", "Route 66 nod", "", "https://www.cabq.gov/artsculture/albuquerques-route-66-centennial-1"),
            BingoSquare("b5", "Something that says “One Albuquerque”", "", "https://www.cabq.gov/one-albuquerque"),
        ),
    ),
)
