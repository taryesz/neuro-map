package pl.edu.ug.neuromapa.screens.home.data.models

import org.jetbrains.compose.resources.StringResource
import neuromapa.composeapp.generated.resources.Res
import neuromapa.composeapp.generated.resources.*

data class TeamMember(
    val nameRes: StringResource,
    val affiliationIds: List<Int>
)

val teamMembersData = listOf(
    TeamMember(Res.string.team_member_1, listOf(1, 4)),
    TeamMember(Res.string.team_member_2, listOf(2, 3)),
    TeamMember(Res.string.team_member_3, listOf(4, 6)),
    TeamMember(Res.string.team_member_4, listOf(5, 6)),
    TeamMember(Res.string.team_member_5, listOf(7))
)

val affiliationsData = mapOf(
    1 to Res.string.affiliation_1,
    2 to Res.string.affiliation_2,
    3 to Res.string.affiliation_3,
    4 to Res.string.affiliation_4,
    5 to Res.string.affiliation_5,
    6 to Res.string.affiliation_6,
    7 to Res.string.affiliation_7
)