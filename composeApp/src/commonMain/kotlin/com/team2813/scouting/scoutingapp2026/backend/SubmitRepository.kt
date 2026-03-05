package com.team2813.scouting.scoutingapp2026.backend

import com.team2813.scouting2025.backend.FormBuilder
import kotlin.time.Duration

class SubmitRepository {
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

    data class SubmitData(
        val preMatchData: PreMatchData,
        val autonomousData: AutoData,
        val teleopData: TeleopData,
        val endgameData: EndgameData,
    )

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
                    addMutable("entry.420184502", AutoData::cycleNumber)
                    addMutable("entry.554128120", AutoData::approxFuel)
                }
                wrap(SubmitData::teleopData) {
                    addMutable("entry.2076833081", TeleopData::Cycles)
                    addMutable("entry.2069256175", TeleopData::Fuel)
                    addMutable(
                        "entry.230562156",
                        TeleopData::incapacitatedTime,
                        ::msTime)
                    addMutable(
                        "entry.1720238000", TeleopData::Herding, ::booleanStr)
                }
                wrap(SubmitData::endgameData) {
                    addMutable("entry.1582430388", EndgameData::selectedClimb)
                    addMutable("entry.581821715", EndgameData::selectedSide)
                    addMutable("entry.664881520", EndgameData::numCycles)
                    addMutable(
                        "entry.290189424", EndgameData::isFailed, ::booleanStr)
                    addMutable("entry.1299729214", EndgameData::climbTime) {
                        "${it * 1000}"
                    }
                    addMutable("entry.1931120353", EndgameData::notes)
                }
            }

    suspend fun submitPost(data: SubmitData) {
        formBuilder.postFormData(data)
    }

    fun getSubmitLink(data: SubmitData): String {
        return formBuilder.getAutomaticSubmitLink(data)
    }
}
