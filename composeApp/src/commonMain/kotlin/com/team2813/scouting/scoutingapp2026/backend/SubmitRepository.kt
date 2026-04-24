package com.team2813.scouting.scoutingapp2026.backend

import com.team2813.scouting.scoutingapp2026.backend.SubmitRepository.SubmitData
import kotlin.time.Duration

class SubmitRepositoryActual : SubmitRepository {
    private fun msTime(time: Duration): String {
        return "${time.inWholeMilliseconds}"
    }

    private fun booleanStr(bool: Boolean): String {
        return if (bool) {
            "True"
        } else {
            "False"
        }
    }

    private val formBuilder: FormBuilder<SubmitData> =
        FormBuilder.build(
            "https://docs.google.com/forms/d/e/1FAIpQLSfsWvXdhB2BDt3N1G57wFay-UM80MOFPXS4VqGu5WPE_uo9Rg") {
                wrap(SubmitData::preMatchData) {
                    addMutable(
                        "entry.763576937", PreMatchData::scoutingTeamNumber)
                    addMutable("entry.1229660068", PreMatchData::nameOfHuman)
                    addMutable("entry.1064933507", PreMatchData::matchNumber)
                }
                wrap(SubmitData::autonomousData) {
                    addMutable(
                        "entry.284009297", AutoData::shootPreload, ::booleanStr)
                    addMutable(
                        "entry.257281052", AutoData::fuelOutpost, ::booleanStr)
                    addMutable(
                        "entry.1712861368", AutoData::fuelDepot, ::booleanStr)
                    addMutable(
                        "entry.1380976125", AutoData::fuelNeutral, ::booleanStr)
                    addMutable("entry.672098235", AutoData::climb, ::booleanStr)
                    addMutable("entry.554128120", AutoData::approxFuel)
                }
                wrap(SubmitData::teleopData) {
                    addMutable("entry.2069256175", TeleopData::Fuel)
                    addMutable(
                        "entry.230562156",
                        TeleopData::incapacitatedTime,
                        ::msTime)
                    addMutable(
                        "entry.1064277816", TeleopData::defenseTime, ::msTime)
                    addMutable(
                        "entry.1720238000", TeleopData::Herding, ::booleanStr)
                }
                wrap(SubmitData::endgameData) {
                    addMutable("entry.1582430388", EndgameData::selectedClimb)
                    addMutable("entry.581821715", EndgameData::selectedSide)
                    addMutable(
                        "entry.290189424", EndgameData::isFailed, ::booleanStr)
                    addMutable("entry.1299729214", EndgameData::climbTime) {
                        "${it * 1000}"
                    }
                    addMutable("entry.1931120353", EndgameData::notes)
                    addMutable("entry.747053636", EndgameData::fuel)
                }
                add("entry.2006523818", SubmitData::uuid)
                add("entry.1249158849", SubmitData::eventCode)
            }

    override suspend fun submitPost(data: SubmitData) {
        formBuilder.postFormData(data)
    }

    override fun getSubmitLink(data: SubmitData): String {
        return formBuilder.getAutomaticSubmitLink(data)
    }
}

class SubmitRepositorySkeleton : SubmitRepository {
    override fun getSubmitLink(data: SubmitData): String {
        return "https://team2813.com"
    }

    override suspend fun submitPost(data: SubmitData) {
        // no-op
    }
}

interface SubmitRepository {
    data class SubmitData(
        val preMatchData: PreMatchData,
        val autonomousData: AutoData,
        val teleopData: TeleopData,
        val endgameData: EndgameData,
        val eventCode: String,
        val uuid: String,
    )

    suspend fun submitPost(data: SubmitData)

    fun getSubmitLink(data: SubmitData): String
}
