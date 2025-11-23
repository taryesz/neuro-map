package pl.edu.ug.neuromapa.screens.place.data

import neuromapa.composeapp.generated.resources.Res
import neuromapa.composeapp.generated.resources.access
import neuromapa.composeapp.generated.resources.place_photo_example
import neuromapa.composeapp.generated.resources.food
import neuromapa.composeapp.generated.resources.quiet
import neuromapa.composeapp.generated.resources.relax
import neuromapa.composeapp.generated.resources.staff
import pl.edu.ug.neuromapa.screens.place.models.*

val mockPlaceKotkaCafe = Place(
    name = "Kotka Café (dla 14+)",
    photo = Res.drawable.place_photo_example,
    photoDescription = "Zdjęcie kasy na którym widać kotów.",
    categoryIcon = Res.drawable.food,
    categoryIconDescription = "Ikona kategorii 'Jedzenie'",
    description = "Kameralna kawiarnia z obecnością kotów, tworząca ciepłą, spokojną  atmosferę. Bliski kontakt ze zwierzętami oraz możliwość wyboru miejsca  sprzyjają relaksowi i wyciszeniu.",
    address = "Ludwika Waryńskiego 26/27, Gdańsk",
    properties = listOf(
        SensoryProperty("Ciche miejsce", Res.drawable.quiet),
        SensoryProperty("Łatwy dojazd", Res.drawable.access),
        SensoryProperty("Strefa relaksu", Res.drawable.relax),
        SensoryProperty("Pomocny personel", Res.drawable.staff)
    ),
    links = listOf(
        Link(LinkType.Website, "https://www.kotkacafe.pl", "kotkacafe.pl"),
    )
)
