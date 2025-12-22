package pl.edu.ug.neuromapa.screens.place.data

import neuromapa.composeapp.generated.resources.Res
import neuromapa.composeapp.generated.resources.category_dark_food
import neuromapa.composeapp.generated.resources.no_photo
import neuromapa.composeapp.generated.resources.sensory_property_dark_access
import neuromapa.composeapp.generated.resources.sensory_property_dark_quiet
import neuromapa.composeapp.generated.resources.sensory_property_dark_relax
import neuromapa.composeapp.generated.resources.sensory_property_dark_staff
import pl.edu.ug.neuromapa.screens.place.models.*

val mockPlaceKotkaCafe = Place(
    name = "Kotka Café (dla 14+)",
    photo = Res.drawable.no_photo,
    photoDescription = "Zdjęcie kasy na którym widać kotów.",
    categoryIcon = Res.drawable.category_dark_food,
    categoryIconDescription = "Ikona kategorii 'Jedzenie'",
    description = "Kameralna kawiarnia z obecnością kotów, tworząca ciepłą, spokojną  atmosferę. Bliski kontakt ze zwierzętami oraz możliwość wyboru miejsca  sprzyjają relaksowi i wyciszeniu.",
    address = "Ludwika Waryńskiego 26/27, Gdańsk",
    properties = listOf(
        SensoryProperty("Ciche miejsce", Res.drawable.sensory_property_dark_quiet),
        SensoryProperty("Łatwy dojazd", Res.drawable.sensory_property_dark_access),
        SensoryProperty("Strefa relaksu", Res.drawable.sensory_property_dark_relax),
        SensoryProperty("Pomocny personel", Res.drawable.sensory_property_dark_staff)
    ),
    links = listOf(
        Link(LinkType.Website, "https://www.kotkacafe.pl", "kotkacafe.pl"),
    )
)
