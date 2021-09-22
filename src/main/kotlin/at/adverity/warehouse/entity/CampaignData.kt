package at.adverity.warehouse.entity

import java.time.LocalDate
import java.util.*
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.Version

@Entity
class CampaignData(
    @Id
    val id: UUID = UUID.randomUUID(),
    val datasource: String,
    val campaign: String,
    val date: LocalDate,
    val clicks: Long,
    val impressions: Long
) {
    @Version
    private val version: Long? = null

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as CampaignData

        if (id != other.id) return false
        if (version != other.version) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + (version?.hashCode() ?: 0)
        return result
    }

    override fun toString(): String {
        return "CampaignData(id=$id, datasource='$datasource', campaign='$campaign', date=$date, clicks=$clicks, impressions=$impressions, version=$version)"
    }


}