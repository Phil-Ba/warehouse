package at.adverity.warehouse.service

import at.adverity.warehouse.Constants.datePattern
import at.adverity.warehouse.entity.CampaignData
import at.adverity.warehouse.repository.CampaignDataRepository
import com.github.doyaaaaaken.kotlincsv.dsl.csvReader
import mu.KotlinLogging
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.io.File
import java.time.LocalDate

private val logger = KotlinLogging.logger {}

@Service
class CsvParserService(private val campaignDataRepository: CampaignDataRepository) {

    @Transactional
    fun importData() {
        val data = csvReader().readAllWithHeader(File("src/main/resources/PIxSyyrIKFORrCXfMYqZBI.csv"))
            .map {
                CampaignData(
                    datasource = it["Datasource"]!!,
                    campaign = it["Campaign"]!!,
                    date = LocalDate.parse(
                        it["Daily"],
                        datePattern
                    ),
                    clicks = it["Clicks"]!!.toLong(),
                    impressions = it["Impressions"]!!.toLong()
                )
            }
        logger.info {
            "Imported data[$data]"
        }
        campaignDataRepository.saveAll(data)
    }
}