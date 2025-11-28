package pl.edu.ug.neuromapa.screens.favorites.data

import neuromapa.composeapp.generated.resources.Res
import neuromapa.composeapp.generated.resources.access
import neuromapa.composeapp.generated.resources.cafe
import neuromapa.composeapp.generated.resources.food
import neuromapa.composeapp.generated.resources.quiet
import neuromapa.composeapp.generated.resources.relax
import neuromapa.composeapp.generated.resources.staff
import pl.edu.ug.neuromapa.screens.place.models.*

val CafePlace = Place(
    name = "Bardzo Długa i Ciekawa Nazwa Lokalizacji Znajdującej Się w Gdańsku",
    photo = Res.drawable.cafe,
    photoDescription = "Zdjęcie cafe z bardzo dluga nazwa.",
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
